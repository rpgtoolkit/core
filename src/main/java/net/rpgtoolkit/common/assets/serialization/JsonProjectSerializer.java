/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.Board;
import net.rpgtoolkit.common.assets.EnemySkillPair;
import net.rpgtoolkit.common.assets.Player;
import net.rpgtoolkit.common.assets.Program;
import net.rpgtoolkit.common.assets.Project;
import net.rpgtoolkit.common.assets.RunTimeKey;
import net.rpgtoolkit.common.io.Paths;
import net.rpgtoolkit.common.utilities.PropertiesSingleton;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author Joshua Michael Daly
 */
public class JsonProjectSerializer extends AbstractJsonSerializer {

  @Override
  public void populate(JSONStringer json, AssetHandle handle) {
    Project project = (Project) handle.getAsset();

    json.key("projectPath").value(project.getProjectPath());
    json.key("gameTitle").value(project.getGameTitle());
    json.key("mainScreenType").value(project.getMainScreenType());
    json.key("extendToFullScreen").value(project.getFullscreenMode());
    json.key("mainResolution").value(project.getResolutionMode());
    json.key("mainDisableProtectReg").value(project.getMainDisableProtectReg());
    json.key("languageFile").value(project.getLanguageFile());
    
    if (project.getStartupPrg() != null) {
      json.key("startupPrg").value(project.getStartupPrg().getFile().getAbsolutePath());
    } else {
      json.key("startupPrg").value("");
    }
    
    if (project.getInitBoard() != null) {
      json.key("initBoard").value(project.getInitBoard().getFile().getAbsolutePath());
    } else {
      json.key("initBoard").value("");
    }
    
    if (project.getInitChar() != null) {
      json.key("initChar").value(project.getInitChar().getFile().getAbsolutePath());
    } else {
      json.key("initChar").value("");
    }
    
    json.key("runTime").value(project.getRunTime());
    json.key("runKey").value(project.getRunKey());
    json.key("menuKey").value(project.getMenuKey());
    json.key("key").value(project.getKey());

    json.key("runTimeArray").array();
    ArrayList<RunTimeKey> runTimeArray = project.getRunTimeArray();
    for (RunTimeKey runTime : runTimeArray) {
      json.key("runTimeValue").array();
      json.value(runTime.getKey());
      json.value(runTime.getProgram());
      json.endArray();
    }
    json.endArray();

    json.key("menuPlugin").value(project.getMenuPlugin());
    json.key("fightPlugin").value(project.getFightPlugin());
    json.key("fightingEnabled").value(project.getFightingEnabled());

    json.key("enemyArray").array();
    ArrayList<EnemySkillPair> enemyArray = project.getEnemyArray();
    for (EnemySkillPair enemySkillPair : enemyArray) {
      json.key("enemy").array();
      json.key(enemySkillPair.getEnemy());
      json.key(Integer.toString(enemySkillPair.getSkill()));
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
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI().getPath());
    return (ext.contains(".gam.json"));
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return serializable(descriptor);
  }

  @Override
  public void deserialize(AssetHandle handle) throws IOException, AssetException {
    JSONObject json = load(handle);
    final Project asset = new Project(new File(handle.getDescriptor().getURI()),
            System.getProperty("project.path"));
    harvest(json, asset);

    handle.setAsset(asset);
  }

  private void harvest(JSONObject json, Project project) {
    try {
      project.setProjectPath(json.optString("projectPath"));
      project.setGameTitle(json.optString("gameTitle"));
      project.setMainScreenType(json.optInt("mainScreenType"));
      project.setExtendToFullScreen(json.getBoolean("extendToFullScreen") ? 1 : 0);
      project.setMainResolution(json.optInt("MainResolution"));
      project.setMainDisableProtectReg(json.optInt("mainDisableProtectReg"));
      project.setLanguageFile(json.getString("languageFile"));
      
      String startupPrg = json.getString("startupPrg");
      if (!startupPrg.isEmpty()) {
      project.setStartupPrg(
              new Program(System.getProperty("project.path")
                      + PropertiesSingleton.getProperty("toolkit.directory.program")
                      + "/" + startupPrg));
      }
      
      String initBoard = json.getString("initBoard");
      if (!initBoard.isEmpty()) {
        project.setInitBoard(
                new Board(new File(System.getProperty("project.path")
                        + PropertiesSingleton.getProperty("toolkit.directory.board")
                        + "/" + initBoard)));
      }
      
      String initChar = json.getString("initChar");
      if (!initChar.isEmpty()) {
      project.setInitChar(
              new Player(new File(System.getProperty("project.path")
                      + PropertiesSingleton.getProperty("toolkit.directory.character")
                      + "/" + initChar)));
      }
      
      project.setRunTime(json.optString("runTime"));
      project.setRunKey(json.optInt("runKey"));
      project.setMenuKey(json.optInt("menuKey"));
      project.setKey(json.optInt("key"));
      project.setRunTimeArray(getRunTimeArray(json.optJSONArray("runTimeArray")));
      project.setMenuPlugin(json.optString("menuPlugin"));
      project.setFightPlugin("fightPlugin");
      project.setFightingEnabled(json.optInt("fightingEnabled"));
      project.setEnemyArray(getEnemyArray(json.optJSONArray("enemyArray")));
      project.setFightType(json.optInt("fightType"));
      project.setFightChance(json.optLong("fightChance"));
      project.setUseCustomBattleSystem(json.optInt("useCustomBattleSystem"));
      project.setGameOverProgram(json.optString("gameOverProgram"));
      project.setButtonGraphic(json.optString("buttonGraphic"));
      project.setWindowGraphic("windowGraphic");
      project.setPluginArray(getPluginArray(json.getJSONArray("pluginArray")));
      project.setUseDayNight(json.optInt("useDayNight"));
      project.setDayNightType(json.optInt("dayNightType"));
      project.setDayLengthInMins(json.optLong("dayLengthInMins"));
      project.setCursorMoveSound(json.getString("cursorMoveSound"));
      project.setCursorSelectSound(json.optString("cursorSelectSound"));
      project.setCursorCancelSound(json.optString("cursorCancelSound"));
      project.setEnableJoyStick(json.optInt("enableJoyStick"));
      project.setColordepth(json.optInt("colordepth"));
      project.setGameSpeed(json.optInt("gameSpeed"));
      project.setUsePixelBasedMovement(json.optInt("usePixelBasedMovement"));
      project.setMouseCursor(json.getString("mouseCursor"));
      project.setHotSpotX(json.optInt("hotSpotX"));
      project.setHotSpotY(json.optInt("hotSpotY"));
      project.setTranspcolor(json.optLong("transpcolor"));
      project.setResolutionWidth(json.optLong("resolutionWidth"));
      project.setResolutionHeight(json.optLong("resolutionHeight"));
      project.setDisplayFPSInTitle(json.optInt("displayFPSInTitle"));
      project.setPathfindingAlgo(json.optInt("pathfindingAlgo"));
      project.setDrawVectors(json.optLong("drawVectors"));
      project.setMovementControls(json.optLong("movementControls"));
      project.setMovementKeys(getMovementKeys(json.getJSONArray("movementKeys")));
    } catch (FileNotFoundException e) {
      System.out.println(e.toString());
    }
  }

  private ArrayList<RunTimeKey> getRunTimeArray(JSONArray array) {
    ArrayList<RunTimeKey> runTimeKeys = new ArrayList<>();

    int length = array.length();
    for (int i = 0; i < length; i++) {
      JSONArray pair = array.getJSONArray(i);
      runTimeKeys.add(new RunTimeKey(pair.getInt(0), pair.getString(1)));
    }

    return runTimeKeys;
  }

  private ArrayList<EnemySkillPair> getEnemyArray(JSONArray array) {
    ArrayList<EnemySkillPair> enemySkillPairs = new ArrayList<>();

    int length = array.length();
    for (int i = 0; i < length; i++) {
      JSONArray pair = array.getJSONArray(i);
      enemySkillPairs.add(new EnemySkillPair(pair.getString(0), pair.getInt(1)));
    }

    return enemySkillPairs;
  }

  private ArrayList<String> getPluginArray(JSONArray array) {
    ArrayList<String> plugins = new ArrayList<>();

    int length = array.length();
    for (int i = 0; i < length; i++) {
      plugins.add(array.getString(i));
    }

    return plugins;
  }

  private ArrayList<Integer> getMovementKeys(JSONArray array) {
    ArrayList<Integer> movementKeys = new ArrayList<>();

    int length = array.length();
    for (int i = 0; i < length; i++) {
      movementKeys.add(array.getInt(i));
    }

    return movementKeys;
  }

}