package xyz.brassgoggledcoders.shadyskies.registering.menu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringEntry;

public class MenuRegisteringEntry<M extends AbstractContainerMenu> extends RegisteringEntry<MenuType<M>, MenuType<?>> {
    public MenuRegisteringEntry(DeferredHolder<MenuType<?>, MenuType<M>> deferredHolder) {
        super(deferredHolder, Registries.MENU);
    }
}
