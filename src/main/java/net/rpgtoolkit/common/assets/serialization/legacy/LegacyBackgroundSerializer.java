/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization.legacy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import net.rpgtoolkit.common.assets.AbstractAssetSerializer;
import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.Background;
import net.rpgtoolkit.common.io.ByteBufferHelper;
import net.rpgtoolkit.common.io.Paths;

/**
 * Serializes background assets
 *
 * @author Joel Moore (based on existing binary open/save)
 */
public class LegacyBackgroundSerializer
    extends AbstractAssetSerializer {

  private static final String HEADER_MAGIC = "RPGTLKIT BKG";
  private static final int HEADER_VERSION_MAJOR = 2;
  private static final int HEADER_VERSION_MINOR = 3;

  @Override
  public int priority() {
    return 1; // not our first choice
  }

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI().getPath());
    return (ext.endsWith(".itm"));
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return serializable(descriptor);
  }

  @Override
  public void serialize(AssetHandle handle)
      throws IOException, AssetException {
    System.out.println("Saving Background " + handle.getDescriptor());
    
    try (final WritableByteChannel channel = handle.write()) {

      final Background bkg = (Background) handle.getAsset();
      final ByteBuffer buffer = ByteBuffer.allocate(16); //tiny because Strings use separate buffers
      buffer.order(ByteOrder.LITTLE_ENDIAN);
      
      channel.write(ByteBufferHelper.getBuffer(HEADER_MAGIC));
      
      buffer.putShort((short) HEADER_VERSION_MAJOR);
      buffer.putShort((short) HEADER_VERSION_MINOR);
      buffer.flip();
      channel.write(buffer);
      buffer.compact();
      
      channel.write(ByteBufferHelper.getBuffer(bkg.getBackgroundImage().toString()));
      channel.write(ByteBufferHelper.getBuffer(bkg.getBackgroundMusic().toString()));
      channel.write(ByteBufferHelper.getBuffer(bkg.getSelectingSound().toString()));
      channel.write(ByteBufferHelper.getBuffer(bkg.getSelectionSound().toString()));
      channel.write(ByteBufferHelper.getBuffer(bkg.getReadySound().toString()));
      channel.write(ByteBufferHelper.getBuffer(bkg.getInvalidSelectionSound().toString()));
      
    }
  }

  @Override
  public void deserialize(AssetHandle handle) throws IOException, AssetException {
    System.out.println("Loading Background " + handle.getDescriptor());

    try (final ReadableByteChannel channel = handle.read()) {

      final Background bkg = new Background(handle.getDescriptor());
      final ByteBuffer buffer = ByteBuffer.allocate((int) handle.size());

      channel.read(buffer);

      buffer.rewind();
      buffer.order(ByteOrder.LITTLE_ENDIAN);

      // Read and validate the file header
      final String header = ByteBufferHelper.getTerminatedString(buffer);
      final int versionMajor = buffer.getShort();
      final int versionMinor = buffer.getShort();

      checkVersion(header, versionMajor, versionMinor);

      //general data
      bkg.setBackgroundImage(AssetDescriptor.parse(
          "file:///" + ByteBufferHelper.getTerminatedString(buffer)));
      bkg.setBackgroundMusic(AssetDescriptor.parse(
          "file:///" + ByteBufferHelper.getTerminatedString(buffer)));
      bkg.setSelectingSound(AssetDescriptor.parse(
          "file:///" + ByteBufferHelper.getTerminatedString(buffer)));
      bkg.setSelectionSound(AssetDescriptor.parse(
          "file:///" + ByteBufferHelper.getTerminatedString(buffer)));
      bkg.setReadySound(AssetDescriptor.parse(
          "file:///" + ByteBufferHelper.getTerminatedString(buffer)));
      bkg.setInvalidSelectionSound(AssetDescriptor.parse(
          "file:///" + ByteBufferHelper.getTerminatedString(buffer)));

      // Set player as handle asset
      handle.setAsset(bkg);

    }
  }
  
  protected void checkVersion(String header, int major, int minor)
      throws AssetException {
    if (!HEADER_MAGIC.equals(header)
        || HEADER_VERSION_MAJOR != major || HEADER_VERSION_MINOR != minor) {
      throw new AssetException("unsupported file version");
    }
  }

}
