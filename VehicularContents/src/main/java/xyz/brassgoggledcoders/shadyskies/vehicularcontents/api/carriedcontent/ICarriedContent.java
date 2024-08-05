package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carriedcontent;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.VehicularContentsAPI;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata.CarriedData;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier.IContentCarrier;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

public interface ICarriedContent {

    ResourceKey<Registry<ICarriedContent>> REGISTRY_KEY = ResourceKey.createRegistryKey(
            VehicularContentsAPI.rl("carried_content")
    );

    ResourceKey<Registry<Codec<? extends ICarriedContent>>> CODEC_REGISTRY_KEY = ResourceKey.createRegistryKey(
            VehicularContentsAPI.rl("carried_content_codec")
    );

    Codec<ICarriedContent> CODEC = ExtraCodecs.lazyInitializedCodec(
            () -> VehicularContentsAPI.getCarriedContentCodecRegistry()
                    .byNameCodec()
                    .dispatch(ICarriedContent::getCodec, Function.identity())
    );

    BlockState getViewState(IContentCarrier carrier);

    default Component getName(IContentCarrier carrier) {
        return this.getViewState(carrier)
                .getBlock()
                .getName();
    }

    default void initializeData(@NotNull CarriedData data) {

    }

    @NotNull
    @ParametersAreNonnullByDefault
    default InteractionResult interact(IContentCarrier carrier, Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Nullable
    @ParametersAreNonnullByDefault
    default MenuProvider getMenuProvider(IContentCarrier carrier, Player player) {
        return null;
    }

    Codec<? extends ICarriedContent> getCodec();
}
