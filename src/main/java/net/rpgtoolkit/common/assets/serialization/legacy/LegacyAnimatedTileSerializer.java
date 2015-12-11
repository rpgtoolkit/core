/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization.legacy;

import net.rpgtoolkit.common.CorruptAssetException;
import net.rpgtoolkit.common.assets.AbstractAssetSerializer;
import net.rpgtoolkit.common.assets.AnimatedTile;
import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.io.ByteBufferHelper;
import net.rpgtoolkit.common.io.Paths;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;

/**
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public class LegacyAnimatedTileSerializer extends AbstractAssetSerializer {

  private final String HEADER_MAGIC = "RPGTLKIT TILEANIM";
  private final int HEADER_VERSION_MAJOR = 2;
  private final int HEADER_VERSION_MINOR = 0;

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI().toString());
    return ext.contains("tan");
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return this.serializable(descriptor);
  }

  @Override
  public void serialize(AssetHandle handle)
          throws IOException, AssetException {

  }

  @Override
  public void deserialize(AssetHandle handle)
          throws IOException, AssetException {

    try (final ReadableByteChannel channel = handle.read()) {

      final ByteBuffer buffer
              = ByteBuffer.allocateDirect((int) handle.size());

      channel.read(buffer);

      buffer.order(ByteOrder.LITTLE_ENDIAN);
      buffer.position(0);

      final String header = ByteBufferHelper.getTerminatedString(buffer);
      final int versionMajor = buffer.getShort();
      final int versionMinor = buffer.getShort();

      // Ensure header magic is correct
      if (!header.equals(HEADER_MAGIC)) {
        throw new CorruptAssetException("Invalid asset header");
      }

      if (versionMajor < HEADER_VERSION_MAJOR || versionMinor < HEADER_VERSION_MINOR) {
        throw new CorruptAssetException("Unsupported asset version");
      }

      final AnimatedTile tile = new AnimatedTile(handle.getDescriptor());

      final int fps = buffer.getInt();     // frame duration (ms)
      final int count = buffer.getInt();   // frame count

      for (int i = 0; i < count; ++i) {
        final String frameTarget = ByteBufferHelper.getTerminatedString(buffer);
        if (frameTarget != null && frameTarget.length() > 0) {
          final AnimatedTile.Frame frame = tile.new Frame(
                  new AssetDescriptor("file:///" + frameTarget), frameTarget);
          frame.setDuration(fps);
          tile.getFrames().add(frame);
        }
      }
      handle.setAsset(tile);
    }
  }
}
