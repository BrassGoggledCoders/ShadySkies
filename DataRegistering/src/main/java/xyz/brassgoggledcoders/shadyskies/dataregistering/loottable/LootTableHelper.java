package xyz.brassgoggledcoders.shadyskies.dataregistering.loottable;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class LootTableHelper {

    public static <B extends Block> LootTable.Builder dropsSelf(B block) {
        return dropsOther(block);
    }

    public static LootTable.Builder dropsOther(ItemLike item) {
        return createSingleItemTable(item);
    }

    public static LootTable.Builder createSingleItemTable(ItemLike item) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(ExplosionCondition.survivesExplosion())
                        .add(LootItem.lootTableItem(item))
                );
    }
}
