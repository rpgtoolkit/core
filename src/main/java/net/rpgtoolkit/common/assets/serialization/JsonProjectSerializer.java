/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import java.util.ArrayList;

import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.EnemySkillPair;
import net.rpgtoolkit.common.assets.Project;
import net.rpgtoolkit.common.assets.RunTimeKey;
import net.rpgtoolkit.common.io.Paths;
import net.rpgtoolkit.common.utilities.CoreProperties;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Joshua Michael Daly
 */
public class JsonProjectSerializer extends AbstractJsonSerializer {

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI().getPath());
    return (ext.contains(CoreProperties.getFullExtension("toolkit.project.extension.json")));
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return serializable(descriptor);
  }

  @Override
  protected void load(AssetHandle handle, JSONObject json) throws AssetException {

    final Project project = new Project(handle.getDescriptor());

    project.setProjectPath(json.optString("projectPath"));
    project.setGameTitle(json.optString("gameTitle"));
    project.setMainScreenType(json.optInt("mainScreenType"));
    project.setExtendToFullScreen(json.getBoolean("extendToFullScreen"));
    project.setMainResolution(json.optInt("MainResolution"));
    project.setMainDisableProtectReg(json.optInt("mainDisableProtectReg"));
    project.setLanguageFile(json.getString("languageFile"));
    project.setStartupPrg(json.getString("startupPrg"));
    project.setInitBoard(json.getString("initBoard"));
    project.setInitChar(json.getString("initChar"));
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
    project.setColorDepth(json.optInt("colordepth"));
    project.setGameSpeed(json.optInt("gameSpeed"));
    project.setUsePixelBasedMovement(json.optInt("usePixelBasedMovement"));
    project.setMouseCursor(json.getString("mouseCursor"));
    project.setHotSpotX(json.optInt("hotSpotX"));
    project.setHotSpotY(json.optInt("hotSpotY"));
    project.setTransparentColor(json.optLong("transpcolor"));
    project.setResolutionWidth(json.optLong("resolutionWidth"));
    project.setResolutionHeight(json.optLong("resolutionHeight"));
    project.setDisplayFPSInTitle(json.optInt("displayFPSInTitle"));
    project.setPathfindingAlgorithm(json.optInt("pathfindingAlgo"));
    project.setDrawVectors(json.optLong("drawVectors"));
    project.setMovementControls(json.optLong("movementControls"));
    project.setMovementKeys(getMovementKeys(json.getJSONArray("movementKeys")));

    handle.setAsset(project);

  }

  @Override
  protected void store(AssetHandle handle, JSONObject json) throws AssetException {

    final Project project = (Project) handle.getAsset();

    json.put("projectPath", project.getProjectPath());
    json.put("gameTitle", project.getGameTitle());
    json.put("mainScreenType", project.getMainScreenType());
    json.put("extendToFullScreen", project.getFullscreenMode());
    json.put("mainResolution", project.getMainResolution());
    json.put("mainDisableProtectReg", project.getMainDisableProtectReg());
    json.put("languageFile", project.getLanguageFile());
    json.put("startupPrg", project.getStartupPrg());
    json.put("initBoard", project.getInitBoard());
    json.put("initChar", project.getInitChar());
    json.put("runTime", project.getRunTime());
    json.put("runKey", project.getRunKey());
    json.put("menuKey", project.getMenuKey());
    json.put("key", project.getKey());

    final JSONArray runtimeKeys = new JSONArray();
    for (final RunTimeKey key : project.getRunTimeArray()) {
      runtimeKeys.put(key.getKey());
      runtimeKeys.put(key.getProgram());
    }

    json.put("runTimeArray", runtimeKeys);
    json.put("menuPlugin", project.getMenuPlugin());
    json.put("fightPlugin", project.getFightPlugin());
    json.put("fightingEnabled", project.getFightingEnabled());

    final JSONArray enemies = new JSONArray();
    for (final EnemySkillPair skill : project.getEnemyArray()) {
      final JSONArray enemy = new JSONArray();
      enemy.put(skill.getEnemy());
      enemy.put(skill.getSkill());
      enemies.put(enemy);
    }

    json.put("enemyArray", enemies);

    json.put("fightType", project.getFightType());
    json.put("fightChance", project.getFightChance());
    json.put("useCustomBattleSystem", project.getUseCustomBattleSystem());
    json.put("battleSystemProgram", project.getBattleSystemProgram());
    json.put("gameOverProgram", project.getGameOverProgram());
    json.put("buttonGraphic", project.getButtonGraphic());
    json.put("windowGraphic", project.getWindowGraphic());
    json.put("pluginArray", project.getPluginArray());

    json.put("useDayNight", project.getUseDayNight());
    json.put("dayNightType", project.getDayNightType());
    json.put("dayLengthInMins", project.getDayLengthInMins());
    json.put("cursorMoveSound", project.getCursorMoveSound());
    json.put("cursorSelectSound", project.getCursorSelectSound());
    json.put("cursorCancelSound", project.getCursorCancelSound());
    json.put("enableJoyStick", project.getEnableJoyStick());
    json.put("colordepth", project.getColorDepth());
    json.put("gameSpeed", project.getGameSpeed());
    json.put("usePixelBasedMovement", project.getUsePixelBasedMovement());
    json.put("mouseCursor", project.getMouseCursor());
    json.put("hotSpotX", project.getHotSpotX());
    json.put("hotSpotY", project.getHotSpotY());
    json.put("transpcolor", project.getTransparentColor());
    json.put("resolutionWidth", project.getResolutionWidth());
    json.put("resolutionHeight", project.getResolutionHeight());
    json.put("displayFPSInTitle", project.getDisplayFPSInTitle());
    json.put("pathfindingAlgo", project.getPathfindingAlgorithm());
    json.put("drawVectors", project.getDrawVectors());
    json.put("pathColor", project.getPathColor());
    json.put("movementControls", project.getMovementControls());

    json.put("movementKeys", project.getMovementKeys());

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
