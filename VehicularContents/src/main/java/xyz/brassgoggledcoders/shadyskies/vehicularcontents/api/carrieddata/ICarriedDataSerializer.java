package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata;

import net.minecraft.core.Registry;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.VehicularContentsAPI;

public interface ICarriedDataSerializer<V> {
    ResourceKey<Registry<ICarriedDataSerializer<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(
            VehicularContentsAPI.rl("carried_data_serializers")
    );

    @NotNull Tag write(@NotNull V value);

    @NotNull V read(@NotNull Tag value);

    void write(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull V value);

    @NotNull V read(@NotNull FriendlyByteBuf friendlyByteBuf);

    @NotNull V copy(@NotNull V value);
}
