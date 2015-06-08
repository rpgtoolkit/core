/*
 */
package net.rpgtoolkit.core.assets;

/**
 * Resolves an asset descriptor into an asset handle that can be
 * used to query and serialize game assets.
 * 
 * @see Asset
 * @see AssetDescriptor
 * @see AssetHandle
 * 
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public interface AssetHandleResolver {
    
    /**
     * Determines if the asset descriptor can (potentially) be resolved
     * into a valid asset handle. Compliant implementations should determine
     * if the descriptor represents a valid path, physically exists,
     * is possible to locate, has security clearance, and any other check
     * that reveals the descriptor can be almost definitely be deserialized
     * into an asset.
     * 
     * @param descriptor asset descriptor
     * @return true if resolvable, false otherwise
     */
    boolean resolvable(AssetDescriptor descriptor);
    
    /**
     * Resolves an asset descriptor into an asset handle. The handle
     * can be used for serialization or maintaining a reference to
     * a loaded asset. This does not check if the descriptor is
     * resolvable before resolution. Depending the asset path and 
     * associated resolver, the operation may block for an extended
     * period of time.
     * 
     * @see AssetHandleResolver#resolvable
     * @param descriptor asset descriptor (non-null)
     * @return asset handle
     * @throws AssetException when asset resolution fails unexpectedly
     */
    AssetHandle resolve(AssetDescriptor descriptor) throws AssetException;
    
}
