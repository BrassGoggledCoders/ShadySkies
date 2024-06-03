package xyz.brassgoggledcoders.shadyskies.registering.blockentity;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringEntry;

public class BlockEntityRegisteringEntry<B extends BlockEntity> implements IRegisteringEntry<BlockEntityType<B>, BlockEntityType<?>> {
    private final DeferredHolder<BlockEntityType<?>, BlockEntityType<B>> deferredHolder;

    public BlockEntityRegisteringEntry(DeferredHolder<BlockEntityType<?>, BlockEntityType<B>> deferredHolder) {
        this.deferredHolder = deferredHolder;
    }

    @Override
    @NotNull
    public Holder<BlockEntityType<?>> getHolder() {
        return this.deferredHolder;
    }

    @Override
    @NotNull
    public ResourceLocation getId() {
        return this.deferredHolder.getId();
    }

    @Override
    public BlockEntityType<B> get() {
        return this.deferredHolder.get();
    }
}
