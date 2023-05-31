package xyz.brassgoggledcoders.shadyskies.conditional.conditional;

import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.shadyskies.conditional.ConditionalMod;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IConditional extends Predicate<IConditionalTarget> {
    Codec<IConditional> CODEC = ExtraCodecs.lazyInitializedCodec(
            () -> RegistryManager.ACTIVE.getRegistry(ConditionalMod.KEY)
                    .getCodec()
                    .dispatch(
                            IConditional::getCodec,
                            Function.identity()
                    )
    );

    Codec<List<IConditional>> LIST_CODEC = ExtraCodecs.lazyInitializedCodec(CODEC::listOf);

    Codec<? extends IConditional> getCodec();
}
