package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

public class EntityContentCarrier implements IContentCarrier {
    private final Entity entity;

    public EntityContentCarrier(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Component getName() {
        return this.entity.getName();
    }

    @Override
    public IAttachmentHolder getAttachmentHolder() {
        return this.entity;
    }

    @Override
    public Level getLevel() {
        return this.entity.level();
    }
}
