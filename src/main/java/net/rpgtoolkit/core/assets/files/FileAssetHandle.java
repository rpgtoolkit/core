/*
 * Copyright (c) 2015, rpgtoolkit.net & Contributors
 *
 * See LICENSE.md in the distribution for the full license text including,
 * but not limited to, a notice of warranty and distribution rights.
 */
package net.rpgtoolkit.core.assets.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.rpgtoolkit.core.Precondition;
import net.rpgtoolkit.core.assets.Asset;
import net.rpgtoolkit.core.assets.AssetDescriptor;
import net.rpgtoolkit.core.assets.AssetHandle;

/**
 *
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public class FileAssetHandle
        implements AssetHandle {

    protected final File file;
    protected final AssetDescriptor descriptor;
    protected Asset asset;

    public FileAssetHandle(AssetDescriptor descriptor) {
        Precondition.notNull("descriptor", descriptor);
        this.descriptor = descriptor;
        this.file = new File(descriptor.getURI());
        this.asset = null;
    }

    public File getFile() {
        return this.file;
    }

    @Override
    public AssetDescriptor getDescriptor() {
        return this.descriptor;
    }

    @Override
    public Asset getAsset() {
        return this.asset;
    }

    @Override
    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(this.file);
    }

}
