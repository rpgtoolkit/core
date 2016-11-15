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
import net.rpgtoolkit.common.assets.BoardVector;
import net.rpgtoolkit.common.assets.Player;
import net.rpgtoolkit.common.assets.PlayerSpecialMove;
import net.rpgtoolkit.common.io.ByteBufferHelper;
import net.rpgtoolkit.common.io.Paths;

/**
 *
 * @author Joel Moore (based on existing binary open/save)
 */
public class LegacyPlayerSerializer extends AbstractAssetSerializer {

  private static final String HEADER_MAGIC = "RPGTLKIT CHAR";
  private static final int HEADER_VERSION_MAJOR = 2;
  private static final int HEADER_VERSION_MINOR = 8;

  @Override
  public int priority() {
    return 1; // not our first choice
  }

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI());
    return ext.endsWith(".tem");
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
    System.out.println("Loading Player " + handle.getDescriptor());

    try (final ReadableByteChannel channel = handle.read()) {
      
      final Player player = new Player(handle.getDescriptor());
      final ByteBuffer buffer = ByteBuffer.allocate((int) handle.size());
      
      channel.read(buffer);
      
      buffer.rewind();
      buffer.order(ByteOrder.LITTLE_ENDIAN);

      // Read and validate the file header
      final String header = ByteBufferHelper.getTerminatedString(buffer);
      final int versionMajor = buffer.getShort();
      final int versionMinor = buffer.getShort();

      checkVersion(header, versionMajor, versionMinor);

      player.setName(ByteBufferHelper.getTerminatedString(buffer));
      player.setExpVariableName(ByteBufferHelper.getTerminatedString(buffer));
      player.setDpVariableName(ByteBufferHelper.getTerminatedString(buffer));
      player.setFpVariableName(ByteBufferHelper.getTerminatedString(buffer));
      player.setHpVariableName(ByteBufferHelper.getTerminatedString(buffer));
      player.setMaxHPVariableName(ByteBufferHelper.getTerminatedString(buffer));
      player.setNameVariableName(ByteBufferHelper.getTerminatedString(buffer));
      player.setMpVariableName(ByteBufferHelper.getTerminatedString(buffer));
      player.setMaxMPVariableName(ByteBufferHelper.getTerminatedString(buffer));
      player.setLvlVariableName(ByteBufferHelper.getTerminatedString(buffer));
      player.setInitialExperience(buffer.getInt());
      player.setInitialHP(buffer.getInt());
      player.setInitialMaxHP(buffer.getInt());
      player.setInitialDP(buffer.getInt());
      player.setInitialFP(buffer.getInt());
      player.setInitialMP(buffer.getInt());
      player.setInitialMaxMP(buffer.getInt());
      player.setInitialLevel(buffer.getInt());
      player.setProfilePicture(ByteBufferHelper.getTerminatedString(buffer));
      for (int i = 0; i < 201; i++) {
        String name = ByteBufferHelper.getTerminatedString(buffer);
        long minExp = buffer.getInt();
        long minLevel = buffer.getInt();
        String cVar = ByteBufferHelper.getTerminatedString(buffer);
        String cVarTest = ByteBufferHelper.getTerminatedString(buffer);

        PlayerSpecialMove newSpecialMove = new PlayerSpecialMove(
            name, minExp, minLevel, cVar, cVarTest);
        player.getSpecialMoveList().add(newSpecialMove);
      }
      player.setSpecialMovesName(ByteBufferHelper.getTerminatedString(buffer));
      player.setHasSpecialMoves(buffer.get() == 1);
      for (int i = 0; i < 11; i++) {
        player.getAccessoryNames().add(ByteBufferHelper.getTerminatedString(buffer));
      }
      for (int i = 0; i < 7; i++) {
        player.getArmourTypes()[i] = (buffer.get() == 1);
      }
      if (versionMajor == 3) {
        player.setLevelType(buffer.get());
      } else {
        player.setLevelType(buffer.getInt());
      }
      player.setExpIncreaseFactor(buffer.getShort());
      player.setMaxLevel(buffer.getInt());
      player.setPercentHPIncrease(buffer.getShort());
      player.setPercentDPIncrease(buffer.getShort());
      player.setPercentFPIncrease(buffer.getShort());
      player.setPercentMPIncrease(buffer.getShort());
      player.setProgramOnLevelUp(ByteBufferHelper.getTerminatedString(buffer));
      player.setLevelUpType(buffer.get());
      player.setCharacterSize(buffer.get());

      if (versionMinor > 4) {
        for (int i = 0; i < 14; i++) {
          String fileName = ByteBufferHelper.getTerminatedString(buffer);
          fileName = fileName.replace("\\", "/");
          player.getStandardGraphics().add(fileName);
        }

        if (versionMinor > 5) {
          for (int i = 0; i < 8; i++) {
            player.getStandingGraphics().add(ByteBufferHelper.getTerminatedString(buffer));
          }
        }

        if (versionMinor > 6) {
          player.setIdleTimeBeforeStanding(buffer.getDouble());
          player.setFrameRate(buffer.getDouble());
        } else {
          player.setIdleTimeBeforeStanding(3);
          player.setFrameRate(0.05);
        }

        long animationCount = buffer.getInt();
        for (int i = 0; i < animationCount + 1; i++) {
          player.getCustomGraphics().add(ByteBufferHelper.getTerminatedString(buffer));
          player.getCustomGraphicsNames().add(ByteBufferHelper.getTerminatedString(buffer));
        }

        if (versionMinor > 7) {
          int collisionCount = buffer.getShort();
          for (int i = 0; i < collisionCount + 1; i++) {
            int pointCount = buffer.getShort();
            BoardVector tempVect = new BoardVector();

            for (int j = 0; j < pointCount + 1; j++) {
              long x = buffer.getInt();
              long y = buffer.getInt();
              tempVect.addPoint(x, y);
            }

            if (i == 0) {
              player.setBaseVector(tempVect);
            } else {
              player.setActivationVector(tempVect);
            }
          }
        }
      }
      
      player.loadAnimations();

      // Set player as handle asset
      handle.setAsset(player);
      
    }
    
  }

  protected void checkVersion(String header, int major, int minor)
      throws AssetException {
    if (!HEADER_MAGIC.equals(header) || HEADER_VERSION_MAJOR != major) {
      //different minor versions can be ok
      throw new AssetException("unsupported file version");
    }
  }
  
}
