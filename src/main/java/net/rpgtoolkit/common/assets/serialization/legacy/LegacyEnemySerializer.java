/**
 * Copyright (c) 2016, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization.legacy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import net.rpgtoolkit.common.assets.AbstractAssetSerializer;
import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.Enemy;
import net.rpgtoolkit.common.io.ByteBufferHelper;
import net.rpgtoolkit.common.io.Paths;

/**
 *
 * @author Joel Moore
 */
public class LegacyEnemySerializer extends AbstractAssetSerializer {

  private final String HEADER_MAGIC = "RPGTLKIT ENEMY";
  private final int HEADER_VERSION_MAJOR = 2;
  private final int HEADER_VERSION_MINOR = 1;

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI());
    return ext.endsWith(".ene");
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return serializable(descriptor);
  }

  @Override
  public void serialize(AssetHandle handle) throws IOException, AssetException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void deserialize(AssetHandle handle) throws IOException, AssetException {
    System.out.println("Loading Enemy " + handle.getDescriptor());

    try (final ReadableByteChannel channel = handle.read()) {
      
      final Enemy enemy = new Enemy(handle.getDescriptor());
      final ByteBuffer buffer = ByteBuffer.allocate((int) handle.size());
      
      channel.read(buffer);
      
      buffer.rewind();
      buffer.order(ByteOrder.LITTLE_ENDIAN);

      // Read and validate the file header

      final String header = ByteBufferHelper.getTerminatedString(buffer);
      final int versionMajor = buffer.getShort();
      final int versionMinor = buffer.getShort();

      checkVersion(header, versionMajor, versionMinor);

      enemy.setName(ByteBufferHelper.getTerminatedString(buffer));
      enemy.setHitPoints(buffer.getInt());
      enemy.setMagicPoints(buffer.getInt());
      enemy.setFightPower(buffer.getInt());
      enemy.setDefencePower(buffer.getInt());
      enemy.canRunAway(buffer.get() == 1);
      enemy.setSneakChance(buffer.getShort());
      enemy.setSurpriseChance(buffer.getShort());
      int specialMoveCount = buffer.getShort();
      for (int i = 0; i < specialMoveCount + 1; i++) {
        enemy.getSpecialMoves().add(ByteBufferHelper.getTerminatedString(buffer));
      }
      int weaknessCount = buffer.getShort();
      for (int i = 0; i < weaknessCount + 1; i++) {
        enemy.getWeaknesses().add(ByteBufferHelper.getTerminatedString(buffer));
      }
      int strengthCount = buffer.getShort();
      for (int i = 0; i < strengthCount + 1; i++) {
        enemy.getStrengths().add(ByteBufferHelper.getTerminatedString(buffer));
      }
      enemy.setAiLevel(buffer.get());
      enemy.useRPGCodeTatics(buffer.get() == 1);
      enemy.setTacticsFile(ByteBufferHelper.getTerminatedString(buffer));
      enemy.setExperienceAwarded(buffer.getInt());
      enemy.setGoldAwarded(buffer.getInt());
      enemy.setBeatEnemyProgram(ByteBufferHelper.getTerminatedString(buffer));
      enemy.setRunAwayProgram(ByteBufferHelper.getTerminatedString(buffer));
      int graphicsCount = buffer.getShort();
      for (int i = 0; i < graphicsCount; i++) {
        enemy.getStandardGraphics().add(
            ByteBufferHelper.getTerminatedString(buffer).replace("\\", "/"));
      }

      ByteBufferHelper.getTerminatedString(buffer); // skip one extra string in the file
      long customGraphicsCount = buffer.getInt(); // TK saves as a long, not a int, so need to read the correct value.
      //TODO: This appears to read 5 custom graphics even when there are fewer than 5, and occasionally read extra blank ones when there are more than 5
//                out.println(customGraphicsCount);

      for (int i = 0; i < customGraphicsCount; i++) {
        enemy.getCustomizedGraphics().add(ByteBufferHelper.getTerminatedString(buffer));
        enemy.getCustomizedGraphicsNames().add(ByteBufferHelper.getTerminatedString(buffer));
      }

      enemy.setMaxHitPoints(enemy.getHitPoints());
      enemy.setMaxMagicPoints(enemy.getMagicPoints());

      // Set enemy as handle asset

      handle.setAsset(enemy);
      
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
