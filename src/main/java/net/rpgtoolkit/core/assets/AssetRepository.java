/*
 */
package net.rpgtoolkit.core.assets;

import java.io.IOException;

/**
 * A repository that stores, serializes, and caches
 * game assets.
 * 
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public interface AssetRepository {
    
    /**
     * Registers an asset handle resolver. Asset handle resolvers are
     * used to resolve asset descriptors (URIs) into handles that pair
     * descriptors with assets and expose I/O.
     * 
     * @param resolver asset handle resolver (non null)
     */
    void register(AssetHandleResolver resolver);
    
    /**
     * Registers an asset serializer. Asset serializers are used to
     * read (deserialize) or write (serialize) assets. 
     * 
     * @see AssetSerializer#load
     * @param serializer 
     */
    void register(AssetSerializer serializer);
    
    /**
     * Returns an asset handle for the asset if the
     * asset was previously loaded by the repository.
     * 
     * @param asset asset to retrieve handle for (non null)
     * @return asset handle if asset was previously loaded
     */
    AssetHandle handle(Asset asset);
    
    /**
     * Returns an asset handle for an asset matching the descriptor.
     *
     * @param descriptor asset descriptor (non null)
     * @return asset handle if asset was previously loaded
     */
    AssetHandle handle(AssetDescriptor descriptor);
    
    /**
     * Loads a game asset with the specified descriptor. The
     * descriptor will be parsed for validity.
     * 
     * @see AssetRepository#load(AssetDescriptor)
     * @param descriptor asset descriptor URI
     * @return Asset instance if descriptor is resolvable and deserializable
     * @throws AssetException if asset is corrupted, missing, etc.
     * @throws IOException if I/O error occurs
     */
    Asset load(String descriptor) 
            throws AssetException, IOException;
    
    /**
     * Loads a game asset with the specified descriptor. The
     * repository will search for an asset handle resolver that is
     * capable of resolving the descriptor into a handle. 
     * 
     * If the handle is valid and resolvable the repository searches for
     * an asset serializer that is capable of deserializing the handle.
     * 
     * If the handle is still valid and deserializable then the repository
     * uses the serializer to deserialize the asset and attach it to the
     * handle.
     * 
     * @param descriptor asset descriptor
     * @return asset instance when descriptor is resolvable and deserializable
     * @throws AssetException when asset is corrupted, invalid, missing, etc.
     * @throws IOException when an unexpected I/O error occurs.
     */
    Asset load(AssetDescriptor descriptor) 
            throws AssetException, IOException;
    
    /**
     * Removes the specified asset from repository management.
     * 
     * @param asset asset (non null)
     */
    void unload(Asset asset);
    
    /**
     * Removes the asset identified by the descriptor 
     * from repository management.
     * 
     * @param descriptor asset descriptor (non null)
     */
    void unload(AssetDescriptor descriptor);
    
}
