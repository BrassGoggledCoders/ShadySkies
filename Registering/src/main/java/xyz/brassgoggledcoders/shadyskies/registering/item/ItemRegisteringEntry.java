package xyz.brassgoggledcoders.shadyskies.registering.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringEntry;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ItemRegisteringEntry<I extends Item> extends RegisteringEntry<I, Item> implements ItemLike {
    private final BiFunction<ResourceKey<CreativeModeTab>, Item, List<ItemStack>> createStacksForCreative;
    private final Map<ResourceKey<CreativeModeTab>, TabVisibility> creativeTabs;

    public ItemRegisteringEntry(DeferredHolder<Item, I> holder, BiFunction<ResourceKey<CreativeModeTab>, Item, List<ItemStack>> createStacksForCreative,
                                Map<ResourceKey<CreativeModeTab>, TabVisibility> creativeTabs) {
        super(holder);
        this.createStacksForCreative = createStacksForCreative;
        this.creativeTabs = creativeTabs;
    }

    @Override
    @NotNull
    public Item asItem() {
        return this.get();
    }

    public ItemStack asStack() {
        return new ItemStack(this);
    }

    public void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        TabVisibility visibility = this.creativeTabs.get(event.getTabKey());
        if (visibility != null) {
            event.acceptAll(this.createStacksForCreative.apply(event.getTabKey(), this.asItem()), visibility);
        }
    }
}
