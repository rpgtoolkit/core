/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.files;


import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetHandle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 *
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public class FileAssetHandle extends AssetHandle {

  public FileAssetHandle(AssetDescriptor descriptor) {
    super(descriptor);
  }

  @Override
  public ReadableByteChannel read() throws IOException {
    final String path = descriptor.getURI().getPath();
    return new FileInputStream(path).getChannel();
  }

  @Override
  public WritableByteChannel write() throws IOException {
    final String path = descriptor.getURI().getPath();
    return new FileOutputStream(path).getChannel();
  }

  @Override
  public long size() throws IOException {
    final String path = descriptor.getURI().getPath();
    final File file = new File(path);
    if (file.exists()) {
      return file.length();
    }
    return -1;
  }

}
