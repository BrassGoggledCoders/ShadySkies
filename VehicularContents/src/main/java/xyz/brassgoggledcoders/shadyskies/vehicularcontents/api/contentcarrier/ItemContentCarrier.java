package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata.CarriedData;

public class ItemContentCarrier implements IContentCarrier {


    @Override
    public Component getName() {
        return null;
    }

    @Override
    public IAttachmentHolder getAttachmentHolder() {
        return null;
    }

    @Override
    public CarriedData getCarriedData() {
        return null;
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
        return false;
    }
}
