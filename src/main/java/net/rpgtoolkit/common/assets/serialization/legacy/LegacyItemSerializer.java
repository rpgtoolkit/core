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

import net.rpgtoolkit.common.assets.AbstractAssetSerializer;
import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.BoardVector;
import net.rpgtoolkit.common.assets.Item;
import net.rpgtoolkit.common.io.ByteBufferHelper;
import net.rpgtoolkit.common.io.Paths;

/**
 * Serializes item assets
 *
 * @author Chris Hutchinson <chris@cshutchinson.com>
 * @author Joel Moore (based on existing binary open/save)
 */
public class LegacyItemSerializer
    extends AbstractAssetSerializer {

  private static final String HEADER_MAGIC = "RPGTLKIT ITEM";
  private static final int HEADER_VERSION_MAJOR = 2;
  private static final int HEADER_VERSION_MINOR = 7;

  private static final int USER_CHAR_COUNT = 51;
  private static final int EQUIP_COUNT = 8;
  private static final int ANIMATION_STANDARD_COUNT = 10;
  private static final int ANIMATION_STANDING_COUNT = 8;

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
    throw new UnsupportedOperationException("unimplemented");
  }

  @Override
  public void deserialize(AssetHandle handle) throws IOException, AssetException {
    System.out.println("Loading Item " + handle.getDescriptor());

    try (final ReadableByteChannel channel = handle.read()) {

      final Item item = new Item(handle.getDescriptor());
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
      item.setName(ByteBufferHelper.getTerminatedString(buffer));
      item.setDescription(ByteBufferHelper.getTerminatedString(buffer));
      item.setIsEquippable(buffer.get() == 1);
      item.setIsMenuDriven(buffer.get() == 1);
      item.setIsBoardDriven(buffer.get() == 1);
      item.setIsBattleDriven(buffer.get() == 1);
      item.setUsersSpecified(buffer.get() == 1);

      item.getUserChar().clear();
      for (int i = 0; i != USER_CHAR_COUNT; i++) {
        item.getUserChar().add(ByteBufferHelper.getTerminatedString(buffer));
      }

      if (versionMinor >= 3) {
        item.setBuyPrice(buffer.getInt());
        item.setSellPrice(buffer.getInt());
      } else {
        item.setBuyPrice(buffer.getShort());
        item.setSellPrice(buffer.getShort());
      }
      item.setIsKeyItem(buffer.get() == 1);

      //equipable information
      item.getEquipLocation().clear();
      for (int i = 0; i != EQUIP_COUNT; i++) {
        item.getEquipLocation().add(buffer.get() == 1);
      }
      item.setAccessory(ByteBufferHelper.getTerminatedString(buffer));
      if (versionMinor >= 3) {
        item.setEquipHP(buffer.getInt());
        item.setEquipDP(buffer.getInt());
        item.setEquipFP(buffer.getInt());
        item.setEquipSMP(buffer.getInt());
        item.setEquipProgram(ByteBufferHelper.getTerminatedString(buffer));
        item.setRemoveProgram(ByteBufferHelper.getTerminatedString(buffer));
        //menu-driven information
        item.setMenuHP(buffer.getInt());
        item.setMenuSMP(buffer.getInt());
        item.setMenuProgram(ByteBufferHelper.getTerminatedString(buffer));
        //battle-driven information
        item.setFightHP(buffer.getInt());
        item.setFightSMP(buffer.getInt());
        item.setFightProgram(ByteBufferHelper.getTerminatedString(buffer));
        item.setFightAnimation(ByteBufferHelper.getTerminatedString(buffer));
      } else {
        item.setEquipHP(buffer.getShort());
        item.setEquipDP(buffer.getShort());
        item.setEquipFP(buffer.getShort());
        item.setEquipSMP(buffer.getShort());
        item.setEquipProgram(ByteBufferHelper.getTerminatedString(buffer));
        item.setRemoveProgram(ByteBufferHelper.getTerminatedString(buffer));
        //menu-driven information
        item.setMenuHP(buffer.getShort());
        item.setMenuSMP(buffer.getShort());
        item.setMenuProgram(ByteBufferHelper.getTerminatedString(buffer));
        //battle-driven information
        item.setFightHP(buffer.getShort());
        item.setFightSMP(buffer.getShort());
        item.setFightProgram(ByteBufferHelper.getTerminatedString(buffer));
        item.setFightAnimation(ByteBufferHelper.getTerminatedString(buffer));
      }

      //board-driven information
      item.setBoardMultitaskProgram(ByteBufferHelper.getTerminatedString(buffer));
      item.setBoardPickUpProgram(ByteBufferHelper.getTerminatedString(buffer));

      //graphical standard information
      item.setIsWide(buffer.get() == 1);
      if (versionMinor >= 4) {  //tk3 item
        item.getStandardAnimationFiles().clear();
        for (int i = 0; i != ANIMATION_STANDARD_COUNT; i++) {
          item.getStandardAnimationFiles().add(ByteBufferHelper.getTerminatedString(buffer));
        }
        if (versionMinor >= 5) {
          item.getAnimationStanding().clear();
          for (int i = 0; i != ANIMATION_STANDING_COUNT; i++) {
            item.getAnimationStanding().add(ByteBufferHelper.getTerminatedString(buffer));
          }
          //speed information
          item.setSpeed(buffer.getDouble());
          item.setIdleTime(buffer.getDouble());
        }
        if (versionMinor < 6) {
          //handling the archaic rest graphic
          //standing might not have enough elements, add any necessary placeholders
          while (item.getAnimationStanding().size() <= Item.ITEM_WALK_S) {
            item.getAnimationStanding().add("");
          }
          item.getAnimationStanding().set(
              Item.ITEM_WALK_S, item.getStandardAnimationFiles().get(Item.ITEM_REST));
          //standard has been initialized for sure with ANIMATION_STANDARD_COUNT elements
          item.getStandardAnimationFiles().set(Item.ITEM_REST, "");
        }
        long tempMaxArrayVal;
        tempMaxArrayVal = buffer.getInt() + 1;   //future animationCustomHandle.size()
        for (long i = 0; i != tempMaxArrayVal; i++) {
          item.getAnimationCustom().add(ByteBufferHelper.getTerminatedString(buffer));
          item.getAnimationCustomHandle().add(ByteBufferHelper.getTerminatedString(buffer));
        }
        //vector information
        if (versionMinor >= 7) {  //modern item--uses vectors
          BoardVector tempV;
          long tempX;
          long tempY;
          int vectorCount = buffer.getShort() + 1;
          for (int i = 0; i != vectorCount; i++) {
            tempV = new BoardVector();
            tempMaxArrayVal = buffer.getShort() + 1;
            for (int j = 0; j != tempMaxArrayVal; j++) {
              tempX = buffer.getInt();
              tempY = buffer.getInt();
              tempV.addPoint(tempX, tempY);
            }
            if (i == 0) {
              item.setVectorBase(tempV);
            } else {
              item.setVectorActivate(tempV);
            }
          }
        }
      } else {  //old tk2 item (versionMinor < 4)
        //convert graphics to animations, etc.
        //WARNING: this part needs to be filled out
      }

      // Set player as handle asset
      handle.setAsset(item);

    }
  }

  /*
    public boolean save()   //WARNING: hasn't been tested yet
    {
        try
        {
            outputStream = new FileOutputStream(this.file);
            binaryIO.setOutputStream(outputStream);
            //header
            binaryIO.writeBinaryString(FILE_HEADER);
            binaryIO.writeBinaryInteger(MAJOR_VERSION);
            binaryIO.writeBinaryInteger(MINOR_VERSION);
            //general data
            binaryIO.writeBinaryString(name);
            binaryIO.writeBinaryString(description);
            binaryIO.writeBinaryByte(isEquippable);
            binaryIO.writeBinaryByte(isMenuDriven);
            binaryIO.writeBinaryByte(isBoardDriven);
            binaryIO.writeBinaryByte(isBattleDriven);
            binaryIO.writeBinaryByte(usersSpecified);
            for (int i = 0; i != USER_CHAR_COUNT; i++)
            {
                binaryIO.writeBinaryString(userChar.get(i));
            }
            binaryIO.writeBinaryLong(buyPrice);
            binaryIO.writeBinaryLong(sellPrice);
            binaryIO.writeBinaryByte(isKeyItem);
            //equippable information
            for (int i = 0; i != EQUIP_COUNT; i++)
            {
                binaryIO.writeBinaryByte(equipLocation.get(i));
            }
            binaryIO.writeBinaryString(accessory);
            binaryIO.writeBinaryLong(equipHP);
            binaryIO.writeBinaryLong(equipDP);
            binaryIO.writeBinaryLong(equipFP);
            binaryIO.writeBinaryLong(equipSMP);
            binaryIO.writeBinaryString(equipProgram);
            binaryIO.writeBinaryString(removeProgram);
            //menu-driven information
            binaryIO.writeBinaryLong(menuHP);
            binaryIO.writeBinaryLong(menuSMP);
            binaryIO.writeBinaryString(menuProgram);
            //battle-driven information
            binaryIO.writeBinaryLong(fightHP);
            binaryIO.writeBinaryLong(fightSMP);
            binaryIO.writeBinaryString(fightProgram);
            binaryIO.writeBinaryString(fightAnimation);
            //board-driven information
            binaryIO.writeBinaryString(boardMultitaskProgram);
            binaryIO.writeBinaryString(boardPickUpProgram);
            //graphical standard information
            binaryIO.writeBinaryByte(isWide);
            for (int i = 0; i != ANIMATION_STANDARD_COUNT; i++)
            {
                binaryIO.writeBinaryString(standardAnimationFiles.get(i));
            }
            for (int i = 0; i != ANIMATION_STANDING_COUNT; i++)
            {
                binaryIO.writeBinaryString(animationStanding.get(i));
            }
            //speed information
            binaryIO.writeBinaryDouble(speed);
            binaryIO.writeBinaryDouble(idleTime);
            binaryIO.writeBinaryLong(animationCustomHandle.size() - 1);
            for (int i = 0; i != animationCustomHandle.size(); i++)
            {
                binaryIO.writeBinaryString(animationCustom.get(i));
                binaryIO.writeBinaryString(animationCustomHandle.get(i));
            }
            //vectors
            binaryIO.writeBinaryInteger(1);
            binaryIO.writeBinaryInteger(vectorBase.getPointCount() - 1);
            for (int i = 0; i != vectorBase.getPointCount(); i++)
            {
                binaryIO.writeBinaryLong(vectorBase.getPointX(i));
                binaryIO.writeBinaryLong(vectorBase.getPointY(i));
            }
            binaryIO.writeBinaryInteger(vectorActivate.getPointCount() - 1);
            for (int i = 0; i != vectorActivate.getPointCount(); i++)
            {
                binaryIO.writeBinaryLong(vectorActivate.getPointX(i));
                binaryIO.writeBinaryLong(vectorActivate.getPointY(i));
            }
            //ending
            outputStream.close();
            return (true);
        }
        catch (IOException e)
        {
            System.err.println(e.toString());
            return false;
        }
    }
  */
  
  protected void checkVersion(String header, int major, int minor)
      throws AssetException {
    if (!HEADER_MAGIC.equals(header)
        || HEADER_VERSION_MAJOR != major || minor < 4) {
      //TODO: support TK2 (minor version < 4) by filling in deserialize's "old tk2 item" section
      throw new AssetException("unsupported file version");
    }
  }

}
