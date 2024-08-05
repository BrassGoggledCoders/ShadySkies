package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carriedcontent;

import net.minecraft.world.level.block.state.BlockState;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier.IContentCarrier;

public abstract class CarriedContent implements ICarriedContent {
    private final BlockState viewState;

    public CarriedContent(BlockState viewState) {
        this.viewState = viewState;
    }

    @Override
    public BlockState getViewState(IContentCarrier carrier) {
        return this.viewState;
    }
}
