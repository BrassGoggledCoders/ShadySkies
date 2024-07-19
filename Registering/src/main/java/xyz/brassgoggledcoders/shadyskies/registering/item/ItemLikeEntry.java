package xyz.brassgoggledcoders.shadyskies.registering.item;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringEntry;

public class ItemLikeEntry<T extends B, B> extends RegisteringEntry<T, B> implements ItemLike {
    public ItemLikeEntry(Registering registering, DeferredHolder<B, T> holder, ResourceKey<? extends Registry<B>> registryKey) {
        super(registering, holder, registryKey);
    }

    @Override
    @NotNull
    public Item asItem() {
        if (this.get() instanceof ItemLike itemLike) {
            return itemLike.asItem();
        }
        throw new IllegalStateException("ItemLike Entry Value is not an ItemLike");
    }

    @NotNull
    public ItemStack asItemStack() {
        return new ItemStack(this);
    }
}
