package xyz.brassgoggledcoders.shadyskies.registering.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("unused")
public class ItemRegisteringBuilder<I extends Item> implements IRegisteringBuilder<ItemRegisteringEntry<I>> {
    private final Registering registering;
    private final String name;

    private final Map<ResourceKey<CreativeModeTab>, TabVisibility> creativeTabs;

    private Function<Properties, I> itemConstructor;
    private Properties properties;

    private BiFunction<ResourceKey<CreativeModeTab>, Item, List<ItemStack>> createStacksForTabs;

    public ItemRegisteringBuilder(Registering registering, String name) {
        this.registering = registering;
        this.name = name;
        this.properties = new Properties();
        this.creativeTabs = new HashMap<>();
        this.createStacksForTabs = (tab, item) -> List.of(new ItemStack(item));
    }

    public ItemRegisteringBuilder<I> withConstructor(Function<Properties, I> constructor) {
        this.itemConstructor = constructor;
        return this;
    }

    public ItemRegisteringBuilder<I> withProperties(Function<Properties, Properties> modifier) {
        this.properties = modifier.apply(this.properties);
        return this;
    }

    @SafeVarargs
    public final ItemRegisteringBuilder<I> withCreativeTabs(ResourceKey<CreativeModeTab>... creativeTabs) {
        return this.withCreativeTabs(TabVisibility.PARENT_AND_SEARCH_TABS, creativeTabs);
    }

    @SafeVarargs
    public final ItemRegisteringBuilder<I> withCreativeTabs(TabVisibility visibility, ResourceKey<CreativeModeTab>... creativeTabs) {
        for (ResourceKey<CreativeModeTab> creativeTab : creativeTabs) {
            this.creativeTabs.put(creativeTab, visibility);
        }
        return this;
    }

    public ItemRegisteringBuilder<I> withCreateStackFunction(BiFunction<ResourceKey<CreativeModeTab>, Item, List<ItemStack>> createStacksForTabs) {
        this.createStacksForTabs = createStacksForTabs;
        return this;
    }

    @Override
    public ItemRegisteringEntry<I> build() {
        ItemRegisteringEntry<I> entry = new ItemRegisteringEntry<>(registering.getDeferredRegister(Registries.ITEM)
                .register(this.name, () -> this.itemConstructor.apply(this.properties)),
                this.createStacksForTabs,
                this.creativeTabs
        );

        registering.addRegisteringEntry(entry);

        return entry;
    }
}
