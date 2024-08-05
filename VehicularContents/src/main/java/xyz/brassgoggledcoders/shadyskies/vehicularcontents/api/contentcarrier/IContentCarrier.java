package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata.CarriedData;

import java.util.UUID;

public interface IContentCarrier {

    Component getName();

    IAttachmentHolder getAttachmentHolder();

    CarriedData getCarriedData();

    Level getLevel();

    Vec3 getPosition();

    boolean isValid();
}
