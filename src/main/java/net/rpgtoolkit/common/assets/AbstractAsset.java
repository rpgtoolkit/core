package net.rpgtoolkit.common.assets;

public abstract class AbstractAsset implements Asset {

    protected final AssetDescriptor descriptor;

    public AbstractAsset(AssetDescriptor descriptor) {
        if (descriptor == null) {
            throw new IllegalArgumentException();
        }
        this.descriptor = descriptor;
    }

    @Override
    public void reset() {}

    @Override
    public AssetDescriptor getDescriptor() {
        return this.descriptor;
    }

}
