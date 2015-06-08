/*
 */
package net.rpgtoolkit.core.assets;

import java.io.IOException;

/**
 *
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public interface AssetSerializer {

    boolean serializable(AssetHandle descriptor);

    boolean deserializable(AssetHandle descriptor);

    void serialize(AssetHandle handle)
            throws AssetException, IOException;

    void deserialize(AssetHandle handle)
            throws AssetException, IOException;

}
