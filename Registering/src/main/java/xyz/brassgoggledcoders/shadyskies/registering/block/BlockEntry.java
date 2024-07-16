package xyz.brassgoggledcoders.shadyskies.registering.block;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemLikeEntry;

public class BlockEntry<B extends Block> extends ItemLikeEntry<B, Block> {
    public BlockEntry(DeferredHolder<Block, B> holder) {
        super(holder);
    }
}
