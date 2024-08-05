package xyz.brassgoggledcoders.shadyskies.vehicularcontents.nbt;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

public class NBTAttachmentSerializer<A extends INBTSerializable<CompoundTag>> implements IAttachmentSerializer<CompoundTag, A> {
    private final Supplier<A> newInstance;

    public NBTAttachmentSerializer(Supplier<A> newInstance) {
        this.newInstance = newInstance;
    }

    @Override
    @NotNull
    public CompoundTag write(@NotNull A a) {
        return a.serializeNBT();
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public A read(IAttachmentHolder holder, CompoundTag tag) {
        A attachment = newInstance.get();
        attachment.deserializeNBT(tag);
        return attachment;
    }
}
