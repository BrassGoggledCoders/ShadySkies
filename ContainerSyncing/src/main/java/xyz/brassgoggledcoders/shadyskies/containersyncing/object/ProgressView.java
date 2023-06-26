package xyz.brassgoggledcoders.shadyskies.containersyncing.object;

import net.minecraft.network.FriendlyByteBuf;

public record ProgressView(
        int current,
        int max
) {
    public static final ProgressView NULL = new ProgressView(
            0,
            100
    );

    public int getOffset(int height) {
        return this.current() * height / this.max();
    }

    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.current());
        friendlyByteBuf.writeInt(this.max());
    }

    public static ProgressView read(FriendlyByteBuf friendlyByteBuf) {
        return new ProgressView(
                friendlyByteBuf.readInt(),
                friendlyByteBuf.readInt()
        );
    }
}
