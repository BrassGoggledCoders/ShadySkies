package xyz.brassgoggledcoders.shadyskies.registering.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemLikeEntry;

public class BlockEntry<B extends Block> extends ItemLikeEntry<B, Block> {
    public BlockEntry(Registering registering, DeferredHolder<Block, B> holder) {
        super(registering, holder, Registries.BLOCK);
    }
}
