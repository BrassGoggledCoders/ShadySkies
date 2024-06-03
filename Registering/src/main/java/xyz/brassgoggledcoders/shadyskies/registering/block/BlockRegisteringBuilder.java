package xyz.brassgoggledcoders.shadyskies.registering.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.neoforge.registries.DeferredHolder;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.blockentity.BlockEntityRegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.blockentity.BlockEntityRegisteringEntry;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemRegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemRegisteringEntry;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BlockRegisteringBuilder<T extends Block, I extends Item> implements IRegisteringBuilder<BlockRegisteringEntry<T, I>> {
    private final Registering registering;
    private final String name;

    private Function<Properties, T> blockConstructor;
    private Properties properties;

    private BiFunction<ItemRegisteringBuilder<I>, Supplier<T>, ItemRegisteringBuilder<I>> itemCreator;
    private Function<BlockEntityRegisteringBuilder<BlockEntity>, BlockEntityRegisteringBuilder<BlockEntity>> blockEntityCreator;

    public BlockRegisteringBuilder(Registering registering, String name) {
        this.registering = registering;
        this.name = name;
        this.properties = Properties.of();
    }

    public BlockRegisteringBuilder<T, I> withBlock(Function<Properties, T> blockConstructor) {
        this.blockConstructor = blockConstructor;
        return this;
    }

    public BlockRegisteringBuilder<T, I> withProperties(Function<Properties, Properties> modifier) {
        this.properties = modifier.apply(properties);
        return this;
    }

    public BlockRegisteringBuilder<T, I> withItemBuilder(BiFunction<ItemRegisteringBuilder<I>, Supplier<T>, ItemRegisteringBuilder<I>> itemCreator) {
        this.itemCreator = itemCreator;
        return this;
    }

    public BlockRegisteringBuilder<T, I> withBlockEntity(BlockEntitySupplier<BlockEntity> blockEntitySupplier) {
        this.blockEntityCreator = blockEntityRegisteringBuilder -> blockEntityRegisteringBuilder.withBlockEntitySupplier(blockEntitySupplier);
        return this;
    }

    @Override
    public BlockRegisteringEntry<T, I> build() {
        DeferredHolder<Block, T> blockHolder = registering.getDeferredRegister(Registries.BLOCK)
                .register(this.name, () -> this.blockConstructor.apply(this.properties));

        ItemRegisteringEntry<I> itemRegisteringEntry = null;
        if (itemCreator != null) {
            itemRegisteringEntry = registering.register(
                    ItemRegisteringBuilder::new,
                    this.name,
                    itemRegisteringBuilder -> this.itemCreator.apply(itemRegisteringBuilder, blockHolder)
                            .build()
            );
        }

        BlockEntityRegisteringEntry<BlockEntity> blockEntityRegisteringEntry = null;
        if (blockEntityCreator != null) {
            blockEntityRegisteringEntry = registering.register(
                    BlockEntityRegisteringBuilder::new,
                    this.name,
                    blockEntityRegisteringBuilder -> this.blockEntityCreator.apply(blockEntityRegisteringBuilder)
                            .withValidBlocks(blockHolder.get())
                            .build()
            );
        }


        return new BlockRegisteringEntry<>(
                blockHolder,
                itemRegisteringEntry,
                blockEntityRegisteringEntry
        );
    }

    public static <B1 extends Block, I1 extends Item> BlockRegisteringBuilder<B1, I1> begin(Registering registering, String name) {
        return registering.begin(
                BlockRegisteringBuilder::new,
                name
        );
    }
}
