package xyz.brassgoggledcoders.shadyskies.registering.menu;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringEntry;

public class MenuRegisteringEntry<M extends AbstractContainerMenu> implements IRegisteringEntry<MenuType<M>, MenuType<?>> {
    private final DeferredHolder<MenuType<?>, MenuType<M>> deferredHolder;

    public MenuRegisteringEntry(DeferredHolder<MenuType<?>, MenuType<M>> deferredHolder) {
        this.deferredHolder = deferredHolder;
    }

    @Override
    @NotNull
    public Holder<MenuType<?>> getHolder() {
        return this.deferredHolder;
    }

    @Override
    @NotNull
    public ResourceLocation getId() {
        return this.deferredHolder.getId();
    }

    @Override
    public MenuType<M> get() {
        return this.deferredHolder.get();
    }
}
