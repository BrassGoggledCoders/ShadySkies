package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata.CarriedData;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata.ItemStackCarriedData;

public class ItemContentCarrier implements IContentCarrier {
    private final ItemStack itemStack;
    private final ItemStackCarriedData carriedData;

    public ItemContentCarrier(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.carriedData = new ItemStackCarriedData(this);
    }

    @Override
    public Component getName() {
        return this.itemStack.getDisplayName();
    }

    @Override
    public IAttachmentHolder getAttachmentHolder() {
        return this.itemStack;
    }

    @Override
    public CarriedData getCarriedData() {
        return this.carriedData;
    }

    @Override
    public Level getLevel() {
        throw new IllegalStateException("Can't get Level in ItemContentCarrier");
    }

    @Override
    public Vec3 getPosition() {
        throw new IllegalStateException("Can't get Level in ItemContentCarrier");
    }

    @Override
    public boolean isValid() {
        return !this.itemStack.isEmpty();
    }
}
