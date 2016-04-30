package net.rpgtoolkit.common.assets.serialization.legacy;

import net.rpgtoolkit.common.assets.AbstractAssetSerializer;
import net.rpgtoolkit.common.assets.Asset;
import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.StatusEffect;
import net.rpgtoolkit.common.assets.StatusEffectAttribute;
import net.rpgtoolkit.common.assets.StatusEffectAttributeKind;
import net.rpgtoolkit.common.io.ByteBufferHelper;
import net.rpgtoolkit.common.io.Paths;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;

public class LegacyStatusEffectSerializer extends AbstractAssetSerializer {

  private static final String HEADER_MAGIC = "RPGTLKIT STATUSE";
  private static final short HEADER_VERSION_MAJOR = 2;
  private static final short HEADER_VERSION_MINOR = 0;

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI());
    return (ext.endsWith(".ste"));
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return serializable(descriptor);
  }

  @Override
  public void serialize(AssetHandle handle) throws IOException, AssetException {

  }

  @Override
  public void deserialize(AssetHandle handle) throws IOException, AssetException {

    final StatusEffect asset = new StatusEffect(handle.getDescriptor());

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

      // Read status effect

      final StatusEffectAttribute speed = new StatusEffectAttribute(StatusEffectAttributeKind.SPEED);
      final StatusEffectAttribute slow = new StatusEffectAttribute(StatusEffectAttributeKind.SPEED);
      final StatusEffectAttribute disable = new StatusEffectAttribute(StatusEffectAttributeKind.DISABLE);
      final StatusEffectAttribute hp = new StatusEffectAttribute(StatusEffectAttributeKind.HP);
      final StatusEffectAttribute smp = new StatusEffectAttribute(StatusEffectAttributeKind.SMP);

      asset.setName(ByteBufferHelper.getTerminatedString(buffer));

      final int durationInRounds = buffer.getShort();

      speed.setDuration(durationInRounds);
      speed.setMagnitude(buffer.getShort());

      if (speed.getMagnitude() != 0) {
        asset.getAttributes().add(speed);
      }

      slow.setDuration(durationInRounds);
      slow.setMagnitude(-buffer.getShort());

      if (slow.getMagnitude() != 0) {
        asset.getAttributes().add(slow);
      }

      disable.setDuration(durationInRounds);
      disable.setMagnitude(buffer.getShort());

      if (disable.getMagnitude() != 0) {
        asset.getAttributes().add(disable);
      }

      final boolean isHpEnabled = buffer.get() != 0;

      hp.setDuration(durationInRounds);
      hp.setMagnitude(buffer.getShort());

      if (isHpEnabled) {
        asset.getAttributes().add(hp);
      }

      final boolean isSmpEnabled = buffer.get() != 0;

      smp.setDuration(durationInRounds);
      smp.setMagnitude(buffer.getShort());

      if (isSmpEnabled) {
        asset.getAttributes().add(smp);
      }

      final boolean isProgramEnabled = buffer.get() != 0;
      final String program = ByteBufferHelper.getTerminatedString(buffer);

      asset.setProgramEnabled(isProgramEnabled);

      if (program.length() > 0) {
        asset.setProgram(
          AssetDescriptor.parse("file:/" + program));
      }

    }

    handle.setAsset(asset);

  }

  private void checkVersion(String header, int major, int minor)
    throws AssetException {
    if (!HEADER_MAGIC.equals(header)
      || HEADER_VERSION_MAJOR != major || HEADER_VERSION_MINOR != minor) {
      throw new AssetException("unsupported file version");
    }
  }

}
