/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization.legacy;

import net.rpgtoolkit.common.assets.AbstractAssetSerializer;
import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.SpecialMove;
import net.rpgtoolkit.common.io.ByteBufferHelper;
import net.rpgtoolkit.common.io.Paths;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import net.rpgtoolkit.common.utilities.CoreProperties;

/**
 * Serializes legacy special moves.
 */
public class LegacySpecialMoveSerializer
    extends AbstractAssetSerializer {

  private static final String HEADER_MAGIC = "RPGTLKIT SPLMOVE";
  private static final short HEADER_VERSION_MAJOR = 2;
  private static final short HEADER_VERSION_MINOR = 2;

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI());
    return (ext.endsWith(CoreProperties.getProperty("toolkit.specialmove.extension.legacy")));
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return serializable(descriptor);
  }

  @Override
  public void serialize(AssetHandle handle) throws IOException, AssetException {

    // TODO: handle case where sum of path lengths can exceed the buffer size

    final SpecialMove asset = (SpecialMove) handle.getAsset();

    try (final WritableByteChannel channel = handle.write()) {

      final Charset charset = Charset.forName("US-ASCII");
      final ByteBuffer buffer = ByteBuffer.allocate(4096);
      buffer.order(ByteOrder.LITTLE_ENDIAN);

      // write header

      ByteBufferHelper.putTerminatedString(buffer, HEADER_MAGIC, charset);
      buffer.putShort(HEADER_VERSION_MAJOR);
      buffer.putShort(HEADER_VERSION_MINOR);

      // write special move properties

      ByteBufferHelper.putTerminatedString(buffer, asset.getName(), charset);

      buffer.putInt(asset.getFightPower());
      buffer.putInt(asset.getMovePowerCost());

      String programPath = null;

      if (asset.getProgram() != null) {
        programPath = Paths.filename(asset.getProgram().getURI());
      }

      ByteBufferHelper.putTerminatedString(buffer, programPath, charset);

      buffer.putInt(asset.getMovePowerDrainedFromTarget());
      buffer.put((byte) (asset.isUsableInBattle() ? 1 : 0));
      buffer.put((byte) (asset.isUsableInMenu() ? 1 : 0));

      String statusEffectPath = null;

      if (asset.getStatusEffect() != null) {
        statusEffectPath = Paths.filename(asset.getStatusEffect().getURI());
      }

      ByteBufferHelper.putTerminatedString(buffer, statusEffectPath, charset);

      String animationPath = null;

      if (asset.getAnimation() != null) {
        animationPath = Paths.filename(asset.getAnimation().getURI());
      }

      ByteBufferHelper.putTerminatedString(buffer, animationPath, charset);
      ByteBufferHelper.putTerminatedString(buffer, asset.getDescription(), charset);

      buffer.flip();
      channel.write(buffer);

    }

  }

  @Override
  public void deserialize(AssetHandle handle) throws IOException, AssetException {

    // TODO: handle case where path lengths can exceed the buffer size

    final SpecialMove asset = new SpecialMove(handle.getDescriptor());

    try (final ReadableByteChannel channel = handle.read()) {

      final ByteBuffer buffer = ByteBuffer.allocate(4096);

      channel.read(buffer);

      buffer.flip();
      buffer.order(ByteOrder.LITTLE_ENDIAN);

      // Read legacy header

      final String header = ByteBufferHelper.getTerminatedString(buffer);
      final int versionMajor = buffer.getShort();
      final int versionMinor = buffer.getShort();

      checkVersion(header, versionMajor, versionMinor);

      // Read special move

      asset.setName(ByteBufferHelper.getTerminatedString(buffer));
      asset.setFightPower(buffer.getInt());
      asset.setMovePowerCost(buffer.getInt());

      final String programPath = ByteBufferHelper.getTerminatedString(buffer);

      if (programPath != null && programPath.length() > 0) {
        asset.setProgram(
            AssetDescriptor.parse("file:/" + programPath));
      }

      asset.setMovePowerDrainedFromTarget(buffer.getInt());
      asset.isUsableInBattle(buffer.get() == 0);
      asset.isUsableInMenu(buffer.get() == 0);

      final String statusEffectPath = ByteBufferHelper.getTerminatedString(buffer);

      if (statusEffectPath != null && statusEffectPath.length() > 0) {
        asset.setStatusEffect(
            AssetDescriptor.parse("file:/" + statusEffectPath));
      }

      final String animationPath = ByteBufferHelper.getTerminatedString(buffer);

      if (animationPath != null && animationPath.length() > 0) {
        asset.setAnimation(
            AssetDescriptor.parse("file:/" + animationPath));
      }

      asset.setDescription(ByteBufferHelper.getTerminatedString(buffer));

    }

    handle.setAsset(asset);

  }

  protected void checkVersion(String header, int major, int minor)
      throws AssetException {
    if (!HEADER_MAGIC.equals(header)
        || HEADER_VERSION_MAJOR != major || HEADER_VERSION_MINOR != minor) {
      throw new AssetException("unsupported file version");
    }
  }

}
