package xyz.brassgoggledcoders.shadyskies.conditional.conditional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public record HasAdvancementConditional(
        ResourceLocation advancementId
) implements IConditional {
    public static Codec<HasAdvancementConditional> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("advancementId").forGetter(HasAdvancementConditional::advancementId)
    ).apply(instance, HasAdvancementConditional::new));

    @Override
    public boolean test(IConditionalTarget predicateTarget) {
        if (predicateTarget.getEntity() instanceof ServerPlayer serverPlayer && predicateTarget.getLevel() instanceof ServerLevel serverLevel) {
            Advancement advancement = serverLevel.getServer().getAdvancements().getAdvancement(advancementId);
            if (advancement != null) {
                return serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone();
            }
        }
        return false;
    }

    @Override
    public Codec<? extends IConditional> getCodec() {
        return CODEC;
    }
}
