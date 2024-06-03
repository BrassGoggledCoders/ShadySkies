package xyz.brassgoggledcoders.shadyskies.registering.blockentity;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.neoforged.neoforge.registries.DeferredHolder;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.block.BlockRegisteringEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockEntityRegisteringBuilder<B extends BlockEntity> implements IRegisteringBuilder<BlockEntityRegisteringEntry<B>> {
    private final Registering registering;
    private final String name;

    private final List<Block> validBlocks;

    private BlockEntitySupplier<B> supplier;

    public BlockEntityRegisteringBuilder(Registering registering, String name) {
        this.registering = registering;
        this.name = name;
        this.validBlocks = new ArrayList<>();
    }

    public BlockEntityRegisteringBuilder<B> withValidBlocks(Block... blocks) {
        this.validBlocks.addAll(Arrays.asList(blocks));
        return this;
    }

    public BlockEntityRegisteringBuilder<B> withBlockEntitySupplier(BlockEntitySupplier<B> supplier) {
        this.supplier = supplier;
        return this;
    }


    @Override
    @SuppressWarnings("DataFlowIssue")
    public BlockEntityRegisteringEntry<B> build() {
        return new BlockEntityRegisteringEntry<>(registering.getDeferredRegister(Registries.BLOCK_ENTITY_TYPE)
                .register(this.name, () -> BlockEntityType.Builder.of(
                                        this.supplier,
                                        this.validBlocks.toArray(Block[]::new)
                                )
                                .build(null)
                )
        );
    }
}
