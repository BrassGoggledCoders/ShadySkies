package xyz.brassgoggledcoders.shadyskies.vehicularcontents.carriedcontent.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carriedcontent.ICarriedContent;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier.IContentCarrier;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.menu.CarriedContainer;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.menu.CarrierMenuProvider;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.content.VCAttachments;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public record ItemStorageCarriedContent(
        StorageSize size,
        boolean showScreen,
        BlockState viewState,
        Optional<BlockState> openState
) implements ICarriedContent {
    public static final Codec<ItemStorageCarriedContent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            StorageSize.CODEC.fieldOf("storageSize").forGetter(ItemStorageCarriedContent::size),
            Codec.BOOL.fieldOf("showScreen").forGetter(ItemStorageCarriedContent::showScreen),
            BlockState.CODEC.fieldOf("viewState").forGetter(ItemStorageCarriedContent::viewState),
            BlockState.CODEC.optionalFieldOf("openState").forGetter(ItemStorageCarriedContent::openState)
    ).apply(instance, ItemStorageCarriedContent::new));



    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public InteractionResult interact(IContentCarrier carrier, Player pPlayer, InteractionHand pHand) {
        if (this.showScreen) {
            pPlayer.openMenu(this.getMenuProvider(carrier, pPlayer));
            Level level = carrier.getLevel();
            if (!level.isClientSide) {
                level.gameEvent(pPlayer, GameEvent.CONTAINER_OPEN, carrier.getPosition());
                PiglinAi.angerNearbyPiglins(pPlayer, true);
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    @Nullable
    @ParametersAreNonnullByDefault
    public MenuProvider getMenuProvider(IContentCarrier carrier, Player player) {
        if (this.showScreen) {
            return new CarrierMenuProvider(
                    carrier,
                    this::createMenu
            );
        }
        return null;
    }

    @Override
    public Codec<? extends ICarriedContent> getCodec() {
        return CODEC;
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(IContentCarrier contentCarrier, int pContainerId, Inventory pInventory, Player pPlayer) {
        return showScreen ? size.createMenu(
                pContainerId,
                pInventory,
                new CarriedContainer(
                        contentCarrier.getAttachmentHolder()
                                .getData(VCAttachments.ITEMSTACK_HANDLER),
                        contentCarrier
                )
        ) : null;
    }

    @Override
    public BlockState getViewState(IContentCarrier carrier) {
        if (this.openState().isPresent()) {
            if (carrier.getAttachmentHolder().getData(VCAttachments.OPENERS) > 0) {
                return this.openState().get();
            }
        }

        return this.viewState();
    }
}
