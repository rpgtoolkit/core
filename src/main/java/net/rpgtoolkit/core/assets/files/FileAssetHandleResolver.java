/*
 */
package net.rpgtoolkit.core.assets.files;

import java.io.File;
import net.rpgtoolkit.core.assets.AssetDescriptor;
import net.rpgtoolkit.core.assets.AssetException;
import net.rpgtoolkit.core.assets.AssetHandle;
import net.rpgtoolkit.core.assets.AssetHandleResolver;

/**
 *
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public class FileAssetHandleResolver
        implements AssetHandleResolver {

    @Override
    public boolean resolvable(AssetDescriptor descriptor) {
        return (descriptor.getScheme().equals("file"));
    }

    @Override
    public AssetHandle resolve(AssetDescriptor descriptor)
            throws AssetException {
        final FileAssetHandle handle = new FileAssetHandle(descriptor);
        final File file = handle.getFile();
        if (!file.exists()) {
            throw new AssetException("requested asset does not exist");
        }
        return handle;
    }

}
