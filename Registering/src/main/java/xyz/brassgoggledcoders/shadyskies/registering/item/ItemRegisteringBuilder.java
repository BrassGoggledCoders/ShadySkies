package xyz.brassgoggledcoders.shadyskies.registering.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;

import java.util.function.Function;

public class ItemRegisteringBuilder<I extends Item> implements IRegisteringBuilder<ItemRegisteringEntry<I>> {
    private final Registering registering;
    private final String name;

    private Function<Properties, I> itemConstructor;
    private Properties properties;

    public ItemRegisteringBuilder(Registering registering, String name) {
        this.registering = registering;
        this.name = name;
        this.properties = new Properties();
    }

    public ItemRegisteringBuilder<I> withConstructor(Function<Properties, I> constructor) {
        this.itemConstructor = constructor;
        return this;
    }

    public ItemRegisteringBuilder<I> withProperties(Function<Properties, Properties> modifier) {
        this.properties = modifier.apply(this.properties);
        return this;
    }

    @Override
    public ItemRegisteringEntry<I> build() {
        return new ItemRegisteringEntry<>(registering.getDeferredRegister(Registries.ITEM)
                .register(this.name, () -> this.itemConstructor.apply(this.properties))
        );
    }
}
