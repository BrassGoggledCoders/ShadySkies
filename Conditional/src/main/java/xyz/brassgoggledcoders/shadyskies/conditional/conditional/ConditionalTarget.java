package xyz.brassgoggledcoders.shadyskies.conditional.conditional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class ConditionalTarget implements IConditionalTarget {
    private final Entity entity;
    private final Level level;
    private final BlockPos position;

    private final Lazy<BlockEntity> blockEntityLazy;
    private final Lazy<BlockState> blockStateLazy;
    private final Lazy<FluidState> fluidStateLazy;

    public ConditionalTarget(Entity entity, Level level) {
        this(entity, level, entity.getOnPos());
    }

    @SuppressWarnings("unused")
    public ConditionalTarget(Entity entity) {
        this(entity, entity.getLevel());
    }

    public ConditionalTarget(Entity entity, Level level, BlockPos blockPos) {
        this.entity = entity;
        this.level = level;
        this.position = blockPos;

        this.blockEntityLazy = Lazy.of(() -> this.getLevel().getBlockEntity(this.getPosition()));
        this.blockStateLazy = Lazy.of(() -> this.getLevel().getBlockState(this.getPosition()));
        this.fluidStateLazy = Lazy.of(() -> this.getLevel().getFluidState(this.getPosition()));
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    @NotNull
    public BlockPos getPosition() {
        return this.position;
    }

    @Override
    @Nullable
    public BlockEntity getBlockEntity() {
        return this.blockEntityLazy.get();
    }

    @Override
    @NotNull
    public BlockState getBlockState() {
        return this.blockStateLazy.get();
    }

    @Override
    @NotNull
    public FluidState getFluidState() {
        return this.fluidStateLazy.get();
    }

    @Override
    @NotNull
    public Level getLevel() {
        return level;
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return this.getEntity() != null ? this.getEntity().getCapability(cap, side) : this.getLevel().getCapability(cap, side);
    }
}
