package xyz.brassgoggledcoders.shadyskies.conditional;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryBuilder;
import xyz.brassgoggledcoders.shadyskies.conditional.conditional.*;

@Mod(ConditionalMod.ID)
@EventBusSubscriber
public class ConditionalMod {
    public static final String ID = "conditional";

    public static final ResourceKey<Registry<Codec<? extends IConditional>>> KEY = ResourceKey.createRegistryKey(rl(ID));

    public ConditionalMod() {

    }

    @SubscribeEvent
    public static void newRegistry(NewRegistryEvent newRegistryEvent) {
        newRegistryEvent.create(new RegistryBuilder<>().setName(new ResourceLocation(ID, ID))
                .disableSync()
                .disableSaving()
        );
    }

    @SubscribeEvent
    public static void registerConditionals(RegisterEvent registerEvent) {
        registerEvent.register(KEY, helper -> {
            helper.register(rl("has_advancement"), HasAdvancementConditional.CODEC);
            helper.register(rl("in_level"), InLevelConditional.CODEC);
            helper.register(rl("fluidstate"), FluidStateConditional.CODEC);
        });
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}
