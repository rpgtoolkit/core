/*
 * Copyright (c) 2015, rpgtoolkit.net & Contributors
 *
 * See LICENSE.md in the distribution for the full license text including,
 * but not limited to, a notice of warranty and distribution rights.
 */
package net.rpgtoolkit.core.assets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public interface AssetHandle {

    public AssetDescriptor getDescriptor();

    public Asset getAsset();

    public void setAsset(Asset asset);

    public InputStream getInputStream() throws IOException;

    public OutputStream getOutputStream() throws IOException;

}
