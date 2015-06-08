/*
 * Copyright (c) 2015, rpgtoolkit.net & Contributors
 *
 * See LICENSE.md in the distribution for the full license text including,
 * but not limited to, a notice of warranty and distribution rights.
 */
package net.rpgtoolkit.core.assets;

import java.io.IOException;

/**
 * Simple adapter to ease implementation of asset serializers.
 *
 * @see AssetSerializer
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public class AssetSerializerAdapter
        implements AssetSerializer {

    @Override
    public boolean serializable(AssetHandle descriptor) {
        return false;
    }

    @Override
    public boolean deserializable(AssetHandle descriptor) {
        return false;
    }

    @Override
    public void serialize(AssetHandle handle) throws IOException {
        // Do nothing
    }

    @Override
    public void deserialize(AssetHandle handle) throws IOException {
        // Do nothing
    }

}
