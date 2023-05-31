package xyz.brassgoggledcoders.shadyskies.conditional.conditional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

public interface IConditionalTarget extends ICapabilityProvider {
    @NotNull
    Level getLevel();

    Entity getEntity();

    @NotNull
    BlockPos getPosition();

    BlockEntity getBlockEntity();

    BlockState getBlockState();

    FluidState getFluidState();
}
