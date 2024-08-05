package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata.serializer;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata.ICarriedDataSerializer;

public class IntegerCarriedDataSerializer implements ICarriedDataSerializer<Integer> {
    @Override
    public @NotNull Tag write(@NotNull Integer value) {
        return IntTag.valueOf(value);
    }

    @Override
    public @NotNull Integer read(@NotNull Tag value) {
        if (value instanceof IntTag) {
            return ((IntTag) value).getAsInt();
        } else {
            return 0;
        }
    }

    @Override
    public void write(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull Integer value) {
        friendlyByteBuf.writeInt(value);
    }

    @Override
    public @NotNull Integer read(@NotNull FriendlyByteBuf friendlyByteBuf) {
        return friendlyByteBuf.readInt();
    }

    @Override
    public @NotNull Integer copy(@NotNull Integer value) {
        return value;
    }
}
