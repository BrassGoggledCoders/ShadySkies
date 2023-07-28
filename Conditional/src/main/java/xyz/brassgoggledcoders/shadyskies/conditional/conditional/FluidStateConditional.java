package xyz.brassgoggledcoders.shadyskies.conditional.conditional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;

public record FluidStateConditional(
        HolderSet<Fluid> fluid
) implements IConditional {
    public static final Codec<FluidStateConditional> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.FLUID)
                    .fieldOf("fluid")
                    .forGetter(FluidStateConditional::fluid)
    ).apply(instance, FluidStateConditional::new));

    @Override
    public boolean test(IConditionalTarget iConditionalTarget) {
        return this.fluid().contains(iConditionalTarget.getFluidState().holder());
    }

    @Override
    public Codec<? extends IConditional> getCodec() {
        return CODEC;
    }
}
