/*
 * Copyright (c) 2015, rpgtoolkit.net & Contributors
 *
 * See LICENSE.md in the distribution for the full license text including,
 * but not limited to, a notice of warranty and distribution rights.
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
