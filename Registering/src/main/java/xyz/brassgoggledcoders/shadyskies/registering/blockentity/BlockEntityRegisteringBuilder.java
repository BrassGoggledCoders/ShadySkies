package xyz.brassgoggledcoders.shadyskies.registering.blockentity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BlockEntityRegisteringBuilder<P, B extends BlockEntity> extends RegisteringBuilder<P, BlockEntityRegisteringBuilder<P, B>, BlockEntityType<?>, BlockEntityType<B>> {

    private final BlockEntitySupplier<B> supplier;
    private final List<Supplier<? extends Block>> validBlocks;

    public BlockEntityRegisteringBuilder(Registering registering, P parent, String name, BlockEntitySupplier<B> supplier) {
        super(registering, parent, name, Registries.BLOCK_ENTITY_TYPE);
        this.supplier = supplier;
        this.validBlocks = new ArrayList<>();
    }

    public BlockEntityRegisteringBuilder<P, B> withValidBlocks(Block... blocks) {
        Arrays.stream(blocks)
                .forEach(block -> this.validBlocks.add(() -> block));
        return this;
    }

    @SafeVarargs
    public final BlockEntityRegisteringBuilder<P, B> withValidBlocks(Supplier<? extends Block>... blocks) {
        this.validBlocks.addAll(Arrays.asList(blocks));
        return this;
    }


    @Override
    protected @NotNull BlockEntityRegisteringEntry<B> createEntry() {
        return new BlockEntityRegisteringEntry<>(this.createDeferredHolder());
    }


    @Override
    @SuppressWarnings("DataFlowIssue")
    protected @NotNull BlockEntityType<B> create() {
        return new BlockEntityType<>(
                this.supplier,
                this.validBlocks
                        .stream()
                        .map(Supplier::get)
                        .collect(Collectors.toSet()),
                null
        );
    }

    @Override
    public @NotNull BlockEntityRegisteringEntry<B> register() {
        return (BlockEntityRegisteringEntry<B>) super.register();
    }

    @Override
    public @NotNull BlockEntityRegisteringBuilder<P, B> self() {
        return this;
    }
}
