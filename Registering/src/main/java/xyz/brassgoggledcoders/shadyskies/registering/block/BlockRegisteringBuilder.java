package xyz.brassgoggledcoders.shadyskies.registering.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemLikeEntry;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemRegisteringBuilder;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("unused")
public class BlockRegisteringBuilder<P, T extends Block> extends RegisteringBuilder<P, BlockRegisteringBuilder<P, T>, Block, T> {
    private final Function<Properties, T> blockConstructor;

    private Properties properties;

    public BlockRegisteringBuilder(Registering registering, P parent, String name, Function<Properties, T> blockConstructor) {
        super(registering, parent, name, Registries.BLOCK);
        this.blockConstructor = blockConstructor;
        this.properties = Properties.of();
    }

    public BlockRegisteringBuilder<P, T> withProperties(Function<Properties, Properties> propertiesFunc) {
        this.properties = Objects.requireNonNull(propertiesFunc.apply(this.properties));
        return this;
    }

    public BlockRegisteringBuilder<P, T> withDefaultItem() {
        return this.withItem()
                .build();
    }

    public ItemRegisteringBuilder<BlockRegisteringBuilder<P, T>, BlockItem> withItem() {
        return this.withItem(BlockItem::new);
    }

    public <I extends Item> ItemRegisteringBuilder<BlockRegisteringBuilder<P, T>, I> withItem(BiFunction<T, Item.Properties, I> itemCreator) {
        return this.getRegistering()
                .item(this, (itemProperties) -> itemCreator.apply(this.getBlock(), itemProperties));
    }

    @Override
    public @NotNull BlockEntry<T> register() {
        return (BlockEntry<T>) super.register();
    }

    @Override
    public @NotNull BlockRegisteringBuilder<P, T> self() {
        return this;
    }

    @Override
    protected @NotNull ItemLikeEntry<T, Block> createEntry() {
        return new BlockEntry<>(this.createDeferredHolder());
    }

    @Override
    protected @NotNull T create() {
        return this.blockConstructor.apply(this.properties);
    }

    @SuppressWarnings("unchecked")
    private final T getBlock() {
        return (T) this.getRegistering()
                .getRegisteringEntry(this.getRegistryKey(), this.getName())
                .get();
    }
}
