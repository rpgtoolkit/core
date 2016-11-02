package net.rpgtoolkit.common.assets;

public abstract class AbstractAsset implements Asset {

    protected AssetDescriptor descriptor;

    public AbstractAsset(AssetDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public void reset() {}

    @Override
    public AssetDescriptor getDescriptor() {
        return this.descriptor;
    }
    
    @Override
    public void setDescriptor(AssetDescriptor assetDescriptor) {
      descriptor = assetDescriptor;
    }

}
