/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import java.io.File;
import java.io.IOException;
import net.rpgtoolkit.common.CorruptAssetException;
import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.Project;
import net.rpgtoolkit.common.io.Paths;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author Joshua Michael Daly
 */
public class JsonProjectSerializer extends AbstractJsonSerializer {

  @Override
  public void populateJSON(JSONStringer json, AssetHandle handle) {
    Project project = (Project) handle.getAsset();

    json.key("projectPath").value(project.getProjectPath());
    json.key("gameTitle").value(project.getGameTitle());
    json.key("mainScreenType").value(project.getMainScreenType());
    json.key("extendToFullScreen").value(project.getFullscreenMode());
    json.key("mainResolution").value(project.getResolutionMode());
    json.key("mainDisableProtectReg").value(project.getMainDisableProtectReg());
    json.key("languageFile").value(project.getLanguageFile());
    json.key("startupPrg").value(project.getStartupPrg());
    json.key("initBoard").value(project.getInitBoard());
    json.key("initChar").value(project.getInitChar());
    json.key("runTime").value(project.getRunTime());
    json.key("runKey").value(project.getRunKey());
    json.key("menuKey").value(project.getMenuKey());
    json.key("key").value(project.getKey());

    json.key("runTimeArray").array();
    for (int i = 0; i < 51; i++) {
      json.key("runTimeValue").array();
      json.value(project.getRunTimeArray().get(i).getKey());
      json.value(project.getRunTimeArray().get(i).getProgram());
      json.endArray();
    }
    json.endArray();

    json.key("menuPlugin").value(project.getMenuPlugin());
    json.key("fightPlugin").value(project.getFightPlugin());
    json.key("fightingEnabled").value(project.getFightingEnabled());

    json.key("enemyArray").array();
    for (int i = 0; i < 501; i++) {
      json.key("enemy").array();
      json.key(project.getEnemyArray().get(i).getEnemy());
      json.key(Integer.toString(project.getEnemyArray().get(i).getSkill()));
      json.endArray();
    }
    json.endArray();

    json.key("fightType").value(project.getFightType());
    json.key("fightChance").value(project.getFightChance());
    json.key("useCustomBattleSystem").value(project.getUseCustomBattleSystem());
    json.key("battleSystemProgram").value(project.getBattleSystemProgram());
    json.key("gameOverProgram").value(project.getGameOverProgram());
    json.key("buttonGraphic").value(project.getButtonGraphic());
    json.key("windowGraphic").value(project.getWindowGraphic());

    json.key("pluginArray").array();
    for (String plugin : project.getPluginArray()) {
      json.value(plugin);
    }
    json.endArray();
    
    json.key("useDayNight").value(project.getUseDayNight());
    json.key("dayNightType").value(project.getDayNightType());
    json.key("dayLengthInMins").value(project.getDayLengthInMins());
    json.key("cursorMoveSound").value(project.getCursorMoveSound());
    json.key("cursorSelectSound").value(project.getCursorSelectSound());
    json.key("cursorCancelSound").value(project.getCursorCancelSound());
    json.key("enableJoyStick").value(project.getEnableJoyStick());
    json.key("colordepth").value(project.getColordepth());
    json.key("gameSpeed").value(project.getGameSpeed());
    json.key("usePixelBasedMovement").value(project.getUsePixelBasedMovement());
    json.key("mouseCursor").value(project.getMouseCursor());
    json.key("hotSpotX").value(project.getHotSpotX());
    json.key("hotSpotY").value(project.getHotSpotY());
    json.key("transpcolor").value(project.getTranspcolor());
    json.key("resolutionWidth").value(project.getResolutionWidth());
    json.key("resolutionHeight").value(project.getResolutionHeight());
    json.key("displayFPSInTitle").value(project.getDisplayFPSInTitle());
    json.key("pathfindingAlgo").value(project.getPathfindingAlgo());
    json.key("drawVectors").value(project.getDrawVectors());
    json.key("pathColor").value(project.getPathColor());
    json.key("movementControls").value(project.getMovementControls());
    
    json.key("movementKeys").array();
    for (Integer keyCode : project.getMovementKeys()) {
      json.value(keyCode);
    }
    json.endArray();
  }

  @Override
  public boolean canSerialize(AssetDescriptor descriptor) {
    final String ext = Paths.getExtension(descriptor.getURI().getPath());
    return (ext.contains(".gam.json"));
  }

  @Override
  public boolean canDeserialize(AssetDescriptor descriptor) {
    return canSerialize(descriptor);
  }

  @Override
  public void deserialize(AssetHandle handle) throws IOException, CorruptAssetException {
    JSONObject json = load(handle);
    final Project asset = new Project(new File(handle.getDescriptor().getURI()),
            System.getProperty("project.path"));

    handle.setAsset(asset);
  }

  private void harvestJSON(JSONObject json, Project project) {

  }

}
