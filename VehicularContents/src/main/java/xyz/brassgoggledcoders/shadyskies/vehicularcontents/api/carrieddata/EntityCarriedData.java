package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata;

import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier.EntityContentCarrier;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier.IContentCarrier;

public class EntityCarriedData extends CarriedData {
    private final EntityContentCarrier contentCarrier;

    public EntityCarriedData(EntityContentCarrier contentCarrier) {
        this.contentCarrier = contentCarrier;
    }

    @Override
    protected IContentCarrier getCarrier() {
        return this.contentCarrier;
    }
}
