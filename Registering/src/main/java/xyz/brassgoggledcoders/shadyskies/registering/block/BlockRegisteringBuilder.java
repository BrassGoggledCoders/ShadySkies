package xyz.brassgoggledcoders.shadyskies.registering.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemLikeEntry;

import java.util.Objects;
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
}
