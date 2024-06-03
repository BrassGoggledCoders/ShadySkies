package xyz.brassgoggledcoders.shadyskies.registering.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringEntry;

public class ItemRegisteringEntry<I extends Item> extends RegisteringEntry<I, Item> implements ItemLike {

    public ItemRegisteringEntry(DeferredHolder<Item, I> holder) {
        super(holder);
    }

    @Override
    @NotNull
    public Item asItem() {
        return this.get();
    }

    public ItemStack asStack() {
        return new ItemStack(this);
    }
}
