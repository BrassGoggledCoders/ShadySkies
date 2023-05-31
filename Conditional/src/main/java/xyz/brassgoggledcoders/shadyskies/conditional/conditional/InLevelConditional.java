package xyz.brassgoggledcoders.shadyskies.conditional.conditional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record InLevelConditional(
        ResourceKey<Level> levelId
) implements IConditional {
    public static final Codec<InLevelConditional> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Level.RESOURCE_KEY_CODEC.fieldOf("levelId").forGetter(InLevelConditional::levelId)
    ).apply(instance, InLevelConditional::new));

    @Override
    public boolean test(IConditionalTarget predicateTarget) {
        return predicateTarget.getLevel().dimension() == this.levelId();
    }

    @Override
    public Codec<? extends IConditional> getCodec() {
        return CODEC;
    }
}



