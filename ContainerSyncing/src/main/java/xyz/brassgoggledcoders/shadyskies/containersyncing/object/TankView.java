package xyz.brassgoggledcoders.shadyskies.containersyncing.object;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public record TankView(
        FluidStack fluidStack,
        int capacity
) {
    public static TankView NULL = new TankView(
            FluidStack.EMPTY,
            FluidType.BUCKET_VOLUME
    );

    public boolean checkEquals(TankView other) {
        return other.capacity() == this.capacity() &&
                other.fluidStack().isFluidStackIdentical(this.fluidStack());
    }

    public TankView copy() {
        return new TankView(
                this.fluidStack().copy(),
                this.capacity()
        );
    }

    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeFluidStack(this.fluidStack());
        friendlyByteBuf.writeInt(this.capacity());
    }

    public static TankView read(FriendlyByteBuf friendlyByteBuf) {
        return new TankView(
                friendlyByteBuf.readFluidStack(),
                friendlyByteBuf.readInt()
        );
    }

    public static TankView fromFluidTank(FluidTank fluidTank) {
        return new TankView(
                fluidTank.getFluid(),
                fluidTank.getCapacity()
        );
    }
}
