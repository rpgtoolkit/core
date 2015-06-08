/*
 * Copyright (c) 2015, rpgtoolkit.net & Contributors
 *
 * See LICENSE.md in the distribution for the full license text including,
 * but not limited to, a notice of warranty and distribution rights.
 */
package net.rpgtoolkit.core.assets.files;

import java.io.File;
import net.rpgtoolkit.core.Precondition;
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

        Precondition.notNull("descriptor", descriptor);

        final FileAssetHandle handle = new FileAssetHandle(descriptor);

        checkFileExists(handle);
        checkFileHasNonZeroLength(handle);

        return handle;

    }

    private void checkFileHasNonZeroLength(FileAssetHandle handle)
            throws AssetException {

        Precondition.notNull("handle", handle);

        final File file = handle.getFile();

        if (file.length() <= 0) {
            throw new AssetException(
                    "asset empty or cannot be determined");
        }

    }

    private void checkFileExists(FileAssetHandle handle)
            throws AssetException {

        Precondition.notNull("handle", handle);

        final File file = handle.getFile();

        if (!file.exists()) {
            throw new AssetException("requested asset does not exist");
        }

    }

}
