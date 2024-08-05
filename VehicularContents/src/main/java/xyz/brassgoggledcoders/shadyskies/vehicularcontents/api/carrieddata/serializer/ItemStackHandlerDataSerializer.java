package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata.serializer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata.ICarriedDataSerializer;

public class ItemStackHandlerDataSerializer implements ICarriedDataSerializer<ItemStackHandler> {

    @Override
    public @NotNull Tag write(@NotNull ItemStackHandler value) {
        return value.serializeNBT();
    }

    @Override
    public @NotNull ItemStackHandler read(@NotNull Tag value) {
        if (value instanceof CompoundTag) {
            ItemStackHandler handler = new ItemStackHandler();
            handler.deserializeNBT((CompoundTag) value);
            return handler;
        }
        return new ItemStackHandler();
    }

    @Override
    public void write(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull ItemStackHandler value) {
        friendlyByteBuf.writeInt(value.getSlots());
        for (int i = 0; i < value.getSlots(); i++) {
            friendlyByteBuf.writeItem(value.getStackInSlot(i));
        }
    }

    @Override
    public @NotNull ItemStackHandler read(@NotNull FriendlyByteBuf friendlyByteBuf) {
        ItemStackHandler handler = new ItemStackHandler(friendlyByteBuf.readInt());
        for (int i = 0; i < handler.getSlots(); i++) {
            handler.setStackInSlot(i, friendlyByteBuf.readItem());
        }
        return handler;
    }

    @Override
    public @NotNull ItemStackHandler copy(@NotNull ItemStackHandler value) {
        ItemStackHandler copy = new ItemStackHandler(value.getSlots());
        for (int i = 0; i < value.getSlots(); i++) {
            copy.setStackInSlot(i, value.getStackInSlot(i).copy());
        }
        return copy;
    }
}
