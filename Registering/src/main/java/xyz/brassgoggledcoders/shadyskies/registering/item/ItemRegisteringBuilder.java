package xyz.brassgoggledcoders.shadyskies.registering.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringEntry;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.eventhandler.CreativeTabsRegisteringObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("unused")
public class ItemRegisteringBuilder<P, I extends Item> extends RegisteringBuilder<P, ItemRegisteringBuilder<P, I>, Item, I> {
    private final Function<Properties, I> itemConstructor;
    private final Map<ResourceKey<CreativeModeTab>, TabVisibility> creativeTabs;

    private Properties properties;
    private BiFunction<ResourceKey<CreativeModeTab>, Item, List<ItemStack>> createStacksForTabs;

    public ItemRegisteringBuilder(Registering registering, P parent, String name, Function<Properties, I> itemConstructor) {
        super(registering, parent, name, Registries.ITEM);
        this.itemConstructor = itemConstructor;
        this.properties = new Properties();
        this.creativeTabs = new HashMap<>();
        this.createStacksForTabs = (tab, item) -> List.of(new ItemStack(item));
    }

    public ItemRegisteringBuilder<P, I> withProperties(Function<Properties, Properties> modifier) {
        this.properties = modifier.apply(this.properties);
        return this;
    }

    @SafeVarargs
    public final ItemRegisteringBuilder<P, I> withCreativeTabs(ResourceKey<CreativeModeTab>... creativeTabs) {
        return this.withCreativeTabs(TabVisibility.PARENT_AND_SEARCH_TABS, creativeTabs);
    }

    @SafeVarargs
    public final ItemRegisteringBuilder<P, I> withCreativeTabs(TabVisibility visibility, ResourceKey<CreativeModeTab>... creativeTabs) {
        for (ResourceKey<CreativeModeTab> creativeTab : creativeTabs) {
            this.creativeTabs.put(creativeTab, visibility);
        }
        return this;
    }

    public ItemRegisteringBuilder<P, I> withCreateStackFunction(BiFunction<ResourceKey<CreativeModeTab>, Item, List<ItemStack>> createStacksForTabs) {
        this.createStacksForTabs = createStacksForTabs;
        return this;
    }

    @Override
    protected @NotNull I create() {
        return this.itemConstructor.apply(properties);
    }

    @Override
    protected void afterRegister(IRegisteringEntry<I, Item> entry) {
        super.afterRegister(entry);
        if (entry instanceof ItemLikeEntry<I, Item> itemLikeEntry && !this.creativeTabs.isEmpty()) {
            this.getRegistering()
                    .addRegisteringObject(new CreativeTabsRegisteringObject(
                            itemLikeEntry,
                            this.createStacksForTabs,
                            this.creativeTabs
                    ));
        }
    }

    @Override
    public @NotNull ItemRegisteringBuilder<P, I> self() {
        return this;
    }

    @Override
    protected @NotNull IRegisteringEntry<I, Item> createEntry() {
        return new ItemEntry<>(this.getRegistering(), this.createDeferredHolder());
    }

    @Override
    public @NotNull ItemEntry<I> register() {
        return (ItemEntry<I>) super.register();
    }
}
