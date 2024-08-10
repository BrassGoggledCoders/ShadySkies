package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata;

import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier.IContentCarrier;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier.ItemContentCarrier;

public class ItemStackCarriedData extends CarriedData {
    private final ItemContentCarrier itemContentCarrier;

    public ItemStackCarriedData(ItemContentCarrier itemContentCarrier) {
        this.itemContentCarrier = itemContentCarrier;
    }

    @Override
    protected IContentCarrier getCarrier() {
        return this.itemContentCarrier;
    }
}
