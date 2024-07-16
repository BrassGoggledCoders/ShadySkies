package xyz.brassgoggledcoders.shadyskies.registering.blockentity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringEntry;

public class BlockEntityRegisteringEntry<B extends BlockEntity> extends RegisteringEntry<BlockEntityType<B>, BlockEntityType<?>> {
    public BlockEntityRegisteringEntry(DeferredHolder<BlockEntityType<?>, BlockEntityType<B>> deferredHolder) {
        super(deferredHolder);
    }
}
