package xyz.brassgoggledcoders.shadyskies.registering.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;

public class ItemEntry<T extends Item> extends ItemLikeEntry<T, Item> {
    public ItemEntry(Registering registering, DeferredHolder<Item, T> holder) {
        super(registering, holder, Registries.ITEM);
    }
}
