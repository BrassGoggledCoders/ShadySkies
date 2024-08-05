package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.menu;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier.IContentCarrier;

import javax.annotation.ParametersAreNonnullByDefault;

public class CarrierMenuProvider implements MenuProvider {
    private final IContentCarrier contentCarrier;
    private final Function4<IContentCarrier, Integer, Inventory, Player, AbstractContainerMenu> createMenu;

    public CarrierMenuProvider(IContentCarrier contentCarrier,
                               Function4<IContentCarrier, Integer, Inventory, Player, AbstractContainerMenu> createMenu) {
        this.contentCarrier = contentCarrier;
        this.createMenu = createMenu;
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return this.contentCarrier.getName();
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return this.createMenu.apply(this.contentCarrier, i, inventory, player);
    }
}
