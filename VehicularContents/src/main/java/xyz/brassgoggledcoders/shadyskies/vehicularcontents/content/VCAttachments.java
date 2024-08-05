package xyz.brassgoggledcoders.shadyskies.vehicularcontents.content;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.VehicularContentsAPI;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.nbt.NBTAttachmentSerializer;

public class VCAttachments {
    private static final DeferredRegister<AttachmentType<?>> DEFERRED_REGISTER = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES,
            VehicularContentsAPI.MOD_ID
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ItemStackHandler>> ITEMSTACK_HANDLER = DEFERRED_REGISTER.register(
            "itemstack_handler",
            () -> AttachmentType.builder(() -> new ItemStackHandler())
                    .serialize(new NBTAttachmentSerializer<>(ItemStackHandler::new))
                    .copyOnDeath()
                    .build()
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> OPENERS = DEFERRED_REGISTER.register(
            "openers",
            () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT)
                    .copyOnDeath()
                    .build()
    );


    public static void setup(IEventBus eventBus) {
        DEFERRED_REGISTER.register(eventBus);
    }
}
