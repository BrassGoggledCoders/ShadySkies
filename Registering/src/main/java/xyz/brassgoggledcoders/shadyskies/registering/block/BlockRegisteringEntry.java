package xyz.brassgoggledcoders.shadyskies.registering.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringEntry;
import xyz.brassgoggledcoders.shadyskies.registering.blockentity.BlockEntityRegisteringEntry;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemRegisteringEntry;

public class BlockRegisteringEntry<B extends Block, I extends Item> extends RegisteringEntry<B, Block> implements ItemLike {

    private final ItemRegisteringEntry<I> itemRegisteringEntry;
    private final BlockEntityRegisteringEntry<BlockEntity> blockEntityRegisteringEntry;

    public BlockRegisteringEntry(DeferredHolder<Block, B> holder, ItemRegisteringEntry<I> itemRegisteringEntry, BlockEntityRegisteringEntry<BlockEntity> blockEntityRegisteringEntry) {
        super(holder);
        this.itemRegisteringEntry = itemRegisteringEntry;
        this.blockEntityRegisteringEntry = blockEntityRegisteringEntry;
    }

    public ItemRegisteringEntry<I> getItemEntry() {
        return itemRegisteringEntry;
    }

    public BlockEntityRegisteringEntry<BlockEntity> getBlockEntityEntry() {
        return blockEntityRegisteringEntry;
    }

    @Override
    @NotNull
    public Item asItem() {
        return this.get()
                .asItem();
    }
}
