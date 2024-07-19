package xyz.brassgoggledcoders.shadyskies.registering.blockentity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringEntry;

public class BlockEntityRegisteringEntry<B extends BlockEntity> extends RegisteringEntry<BlockEntityType<B>, BlockEntityType<?>> {
    public BlockEntityRegisteringEntry(Registering registering, DeferredHolder<BlockEntityType<?>, BlockEntityType<B>> deferredHolder) {
        super(registering, deferredHolder, Registries.BLOCK_ENTITY_TYPE);
    }
}
