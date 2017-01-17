/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import net.rpgtoolkit.common.assets.Asset;
import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.AssetSerializer;
import net.rpgtoolkit.common.assets.files.FileAssetHandle;
import net.rpgtoolkit.common.assets.resources.ResourceAssetHandle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 
 * @author Joel Moore
 */
public class AssetSerializerTestHelper {

  public static File serialize(Asset asset, AssetSerializer serializer)
          throws AssetException, IOException {
    final String filename = asset.getDescriptor().getURI().getPath().substring(1);
    final Path path = Files.createTempFile(filename + "-", ".tmp");
    final AssetDescriptor descriptor = new AssetDescriptor(path.toUri());
    final AssetHandle handle = new FileAssetHandle(descriptor);

    handle.setAsset(asset);
    serializer.serialize(handle);

    return path.toFile();
  }

  public static <T> T deserializeFile(String path, AssetSerializer serializer)
          throws AssetException, IOException {
    final AssetDescriptor descriptor = AssetDescriptor.parse("file:/" + path);
    final AssetHandle handle = new FileAssetHandle(descriptor);

    serializer.deserialize(handle);

    return (T) handle.getAsset();
  }

  public static <T> T deserializeResource(String path, AssetSerializer serializer)
          throws AssetException, IOException {
    final AssetDescriptor descriptor = AssetDescriptor.parse("res:/" + path);
    final AssetHandle handle = new ResourceAssetHandle(
            descriptor, Thread.currentThread().getContextClassLoader());

    serializer.deserialize(handle);

    return (T) handle.getAsset();
  }

}
