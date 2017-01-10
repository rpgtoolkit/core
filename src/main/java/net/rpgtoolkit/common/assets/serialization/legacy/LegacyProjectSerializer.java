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
import net.rpgtoolkit.common.assets.EnemySkillPair;
import net.rpgtoolkit.common.assets.Project;
import net.rpgtoolkit.common.assets.RunTimeKey;
import net.rpgtoolkit.common.io.ByteBufferHelper;
import net.rpgtoolkit.common.io.Paths;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;

public class LegacyProjectSerializer extends AbstractAssetSerializer {

  private static final String HEADER_MAGIC = "RPGTLKIT MAIN";
  private static final short HEADER_VERSION_MAJOR = 2;
  private static final short HEADER_VERSION_MINOR = 9;

  private static final String DEFAULT_MENU_PLUGIN = "tk3menu.dll";
  private static final String DEFAULT_BATTLE_PLUGIN = "tk3fight.dll";

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    return false;
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI().getPath());
    return (ext.endsWith(".gam"));
  }

  @Override
  public void serialize(AssetHandle handle) throws IOException, AssetException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deserialize(AssetHandle handle) throws IOException, AssetException {

    Project asset;

    try (final ReadableByteChannel channel = handle.read()) {

      // NOTE: the buffer allocation should not be fixed; most project files are less than
      // 8KB, but that's obviously a bad assumption. At the moment this serializer is only
      // for backwards compatibility and so the code quality suffers to save time...

      final ByteBuffer buffer = ByteBuffer.allocate(8192);

      channel.read(buffer);

      buffer.flip();
      buffer.order(ByteOrder.LITTLE_ENDIAN);

      // read legacy header

      final String header = ByteBufferHelper.getTerminatedString(buffer);
      final int versionMajor = buffer.getShort();
      final int versionMinor = buffer.getShort();

      checkVersion(header, versionMajor, versionMinor);

      buffer.getShort(); // powerhouse edition file? (deprecated)
      ByteBufferHelper.getTerminatedString(buffer); // powerhouse edition registration code (deprecated)

      final String projectPath = ByteBufferHelper.getTerminatedString(buffer);
      final String title = ByteBufferHelper.getTerminatedString(buffer);

      if (projectPath.length() == 0)
        throw new AssetException("project path is empty");

      // create project instance

      asset = new Project(handle.getDescriptor(), projectPath, title);

      asset.setMainScreenType(buffer.getShort());
      asset.setExtendToFullScreen(buffer.getShort());
      asset.setMainResolution(buffer.getShort());

      if (versionMinor < 3) {
        buffer.getShort(); // parallax scrolling (deprecated)
      }

      asset.setMainDisableProtectReg(buffer.getShort());
      asset.setLanguageFile(ByteBufferHelper.getTerminatedString(buffer));
      asset.setStartupPrg(ByteBufferHelper.getTerminatedString(buffer));
      asset.setInitBoard(ByteBufferHelper.getTerminatedString(buffer));
      asset.setInitChar(ByteBufferHelper.getTerminatedString(buffer));
      asset.setRunTime(ByteBufferHelper.getTerminatedString(buffer));
      asset.setRunKey(buffer.getShort());
      asset.setMenuKey(buffer.getShort());
      asset.setKey(buffer.getShort());

      // read extended runtime keys

      final int extendedKeyCount = buffer.getShort();

      for (int i = 0; i <= extendedKeyCount; i++) {
        final int key = buffer.getShort();
        final String program = ByteBufferHelper.getTerminatedString(buffer);
        if (program.length() > 0) {
          asset.getRunTimeArray().add(new RunTimeKey(key, program));
        }
      }

      if (versionMinor >= 3) {
        asset.setMenuPlugin(ByteBufferHelper.getTerminatedString(buffer));
        asset.setFightPlugin(ByteBufferHelper.getTerminatedString(buffer));
      }
      else {
        buffer.getShort();
        asset.setMenuPlugin(DEFAULT_MENU_PLUGIN);
        asset.setFightPlugin(DEFAULT_BATTLE_PLUGIN);
      }

      if (versionMinor <= 2) {
        buffer.getInt(); // multitasking speed (deprecated)
        buffer.getShort(); // version 1.4 memory protection (deprecated)
      }

      asset.setFightingEnabled(buffer.getShort());

      // battle system enemy registry

      final int enemyCount = buffer.getShort();

      for (int i = 0; i <= enemyCount; i++) {
        final String enemyPath = ByteBufferHelper.getTerminatedString(buffer);
        final int enemySkillLevel = buffer.getShort();
        if (enemyPath.length() > 0) {
          asset.getEnemyArray().add(
            new EnemySkillPair(enemyPath, enemySkillLevel));
        }
      }

      asset.setFightType(buffer.getShort());
      asset.setFightChance(buffer.getInt());
      asset.setUseCustomBattleSystem(buffer.getShort());
      asset.setBattleSystemProgram(ByteBufferHelper.getTerminatedString(buffer));

      if (versionMinor < 3) {
        buffer.getShort(); // fight style (deprecated)
      }

      asset.setGameOverProgram(ByteBufferHelper.getTerminatedString(buffer));

      // appearance

      asset.setButtonGraphic(ByteBufferHelper.getTerminatedString(buffer));
      asset.setWindowGraphic(ByteBufferHelper.getTerminatedString(buffer));

      // plugins

      // Version 2.2 and earlier supported plugins but did not allow arbitrary
      // plugin names, all plugins were of the form tkplug%.dll where % is a positive
      // integer. Later versions supported arbitrary plugins.

      if (versionMinor <= 2) {
        final int pluginCount = buffer.getShort();
        for (int i = 0; i <= pluginCount; i++) {
          final int pluginSlot = buffer.getShort();
          if (pluginSlot == 1) {
            asset.getPluginArray().add(String.format("tkplug%d.dll", i));
          }
        }
      }
      else {
        final int pluginCount = buffer.getShort();
        for (int i = 0; i <= pluginCount; i++) {
          final String pluginPath = ByteBufferHelper.getTerminatedString(buffer);
          if (pluginPath.length() > 0) {
            asset.getPluginArray().add(pluginPath);
          }
        }
      }

      // day/night cycles

      asset.setUseDayNight(buffer.getShort());
      asset.setDayNightType(buffer.getShort());
      asset.setDayLengthInMins(buffer.getInt());

      // TODO: clean up and document the versioning mess below

      if (versionMinor >= 3) {
        asset.setCursorMoveSound(ByteBufferHelper.getTerminatedString(buffer));
        asset.setCursorSelectSound(ByteBufferHelper.getTerminatedString(buffer));
        asset.setCursorCancelSound(ByteBufferHelper.getTerminatedString(buffer));
        asset.setEnableJoyStick(buffer.get());
        asset.setColorDepth(buffer.get());
      }

      if (versionMinor >= 4) {
        asset.setGameSpeed(buffer.get());
        asset.setUsePixelBasedMovement(buffer.get());
      }
      else {
        asset.setGameSpeed(0);
        asset.setUsePixelBasedMovement(0);
      }

      if (versionMinor < 6) {
        if (versionMinor == 5) {
          buffer.get(); // mouse cursor style (deprecated)
        }
        asset.setHotSpotX(0);
        asset.setHotSpotY(0);
        asset.setTransparentColor(0xff0000ff); // rgba(255, 0, 0, 255)
      }
      else {
        asset.setMouseCursor(ByteBufferHelper.getTerminatedString(buffer));
        asset.setHotSpotX(buffer.get());
        asset.setHotSpotY(buffer.get());
        asset.setTransparentColor(buffer.getInt());
      }

      if (versionMinor >= 7) {
        asset.setResolutionWidth(buffer.getInt());
        asset.setResolutionHeight(buffer.getInt());
      }

      if (versionMinor >= 8) {
        asset.setDisplayFPSInTitle(buffer.get());
      }

      if (versionMinor >= 9) {
        asset.setPathfindingAlgorithm(buffer.getShort());
        asset.setDrawVectors(buffer.getInt());
        asset.setPathColor(buffer.getInt());
        asset.setMovementControls(buffer.getInt());
        for (int i = 0; i < 8; i++) {
          asset.getMovementKeys().add((int) buffer.getShort());
        }
      }

    }

    handle.setAsset(asset);

  }

  private void checkVersion(String header, int major, int minor)
    throws AssetException {

    boolean supported = true;

    if (!HEADER_MAGIC.equals(header))
      supported = false;

    if (major < 2)
      supported = false;

    if (minor < 2 || minor > 9)
      supported = false;

    if (!supported)
      throw new AssetException("unsupported file version");

  }

}
