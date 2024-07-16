package xyz.brassgoggledcoders.shadyskies.registering.eventhandler;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemLikeEntry;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public record CreativeTabsRegisteringObject(
        ItemLikeEntry<?, ?> itemLikeEntry,
        BiFunction<ResourceKey<CreativeModeTab>, Item, List<ItemStack>> createStacksForCreative,
        Map<ResourceKey<CreativeModeTab>, CreativeModeTab.TabVisibility> creativeTabs
) {
    public void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab.TabVisibility visibility = this.creativeTabs()
                .get(event.getTabKey());
        if (visibility != null) {
            event.acceptAll(
                    this.createStacksForCreative()
                            .apply(
                                    event.getTabKey(),
                                    this.itemLikeEntry()
                                            .asItem()
                            ),
                    visibility
            );
        }
    }
}
