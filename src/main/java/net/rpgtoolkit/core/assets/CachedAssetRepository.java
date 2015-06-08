/*
 */
package net.rpgtoolkit.core.assets;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.rpgtoolkit.core.Precondition;

/**
 *
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public class CachedAssetRepository
        implements AssetRepository {
    
    private final Map<AssetDescriptor, AssetHandle> assets;
    private final Set<AssetHandleResolver> resolvers;
    private final Set<AssetSerializer> serializers;

    // NOTE: This obviously does not sort by priority. This needs
    // to be implemented in the future.
    
    public class PriorityComparator<T>
            implements Comparator<T> {

        @Override
        public int compare(T lhs, T rhs) {
            return lhs.hashCode() & ~rhs.hashCode();
        }

    }

    public CachedAssetRepository() {
        this.assets = new HashMap<>();
        this.resolvers = new TreeSet<>(new PriorityComparator<>());
        this.serializers = new TreeSet<>(new PriorityComparator<>());
    }
    
    @Override
    public AssetHandle handle(Asset asset) {
        Precondition.notNull(asset);
        for (AssetHandle handle : this.assets.values()) {
            if (handle.getAsset().equals(asset)) {
                return handle;
            }
        }
        return null;    
    }
    
    @Override
    public AssetHandle handle(AssetDescriptor descriptor) {
        Precondition.notNull("descriptor", descriptor);
        return this.assets.get(descriptor);
    }

    @Override
    public void register(AssetHandleResolver resolver) {
        Precondition.notNull("resolver", resolver);
        this.resolvers.add(resolver);
    }

    @Override
    public void register(AssetSerializer serializer) {
        Precondition.notNull("serializer", serializer);
        this.serializers.add(serializer);
    }

    @Override
    public Asset load(String descriptor) throws AssetException, IOException {
        return load(new AssetDescriptor(descriptor));
    }

    @Override
    public Asset load(AssetDescriptor descriptor)
            throws AssetException, IOException {

        Precondition.notNull("descriptor", descriptor);
        
        if (assets.containsKey(descriptor)) {
            final AssetHandle handle = assets.get(descriptor);
            final Asset asset = handle.getAsset();
            if (asset == null) {
                assets.remove(descriptor);
                return load(descriptor);
            }
            return asset;
        }

        for (AssetHandleResolver resolver : resolvers) {
            if (resolver.resolvable(descriptor)) {
                final AssetHandle handle = resolver.resolve(descriptor);
                for (AssetSerializer serializer : serializers) {
                    if (serializer.deserializable(handle)) {
                        serializer.deserialize(handle);
                    }
                    return handle.getAsset();
                }
            }
        }
        return null;
    }

    @Override
    public void unload(Asset asset) {
        AssetDescriptor descriptor = null;
        for (final AssetHandle handle : assets.values()) {
            if (asset.equals(handle.getAsset())) {
                descriptor = handle.getDescriptor();
                break;
            }
        }
        if (descriptor != null) {
            assets.remove(descriptor);
        }
    }

    @Override
    public void unload(AssetDescriptor descriptor) {
        Precondition.notNull("descriptor", descriptor);
        assets.remove(descriptor);
    }

}
