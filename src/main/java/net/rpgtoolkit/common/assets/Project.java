/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class manages the GAM file type for the RPG Toolkit
 *
 * @author Geoff Wilson
 * @version 0.1
 */
public class Project extends AbstractAsset {

  // Project Variables

  private String projectPath;
  private String gameTitle;
  private int mainScreenType;
  private int extendToFullScreen;
  private int mainResolution;
  private int mainDisableProtectReg;
  private String languageFile;
  private String startupPrg;
  private String initBoard;
  private String initChar;
  private String runTime;
  private int runKey;
  private int menuKey;
  private int key;
  private List<RunTimeKey> runTimeArray;
  private String menuPlugin;
  private String fightPlugin;
  private int fightingEnabled;
  private List<EnemySkillPair> enemyArray;
  private int fightType;
  private long fightChance;
  private int useCustomBattleSystem;
  private String battleSystemProgram;
  private String gameOverProgram;
  private String buttonGraphic;
  private String windowGraphic;
  private List<String> pluginArray;
  private int useDayNight;
  private int dayNightType;
  private long dayLengthInMins;
  private String cursorMoveSound;
  private String cursorSelectSound;
  private String cursorCancelSound;
  private int enableJoyStick;
  private int colordepth;
  private int gameSpeed;
  private int usePixelBasedMovement;
  private String mouseCursor;
  private int hotSpotX;
  private int hotSpotY;
  private long transpcolor;
  private long resolutionWidth;
  private long resolutionHeight;
  private int displayFPSInTitle;

  // Variables Specific to 3.1
  private int pathfindingAlgo;
  private long drawVectors;
  private long pathColor;
  private long movementControls;
  private List<Integer> movementKeys;

  public Project(AssetDescriptor descriptor) {
    super(descriptor);
    runTimeArray = new ArrayList<>();
    enemyArray = new ArrayList<>();
    pluginArray = new ArrayList<>();
    movementKeys = new ArrayList<>();
    reset();
  }

  public Project(AssetDescriptor descriptor, String projectPath, String title) {
    this(descriptor);
    this.setProjectPath(projectPath);
    this.setGameTitle(title);
  }

  public long getResolutionHeight() {
    return resolutionHeight;
  }

  public void setResolutionHeight(long newHeight) {
    resolutionHeight = newHeight;
  }

  public long getResolutionWidth() {
    return resolutionWidth;
  }

  public void setResolutionWidth(long newWidth) {
    resolutionWidth = newWidth;
  }

  public String getCursorMoveSound() {
    return cursorMoveSound;
  }

  public String getCursorSelectSound() {
    return cursorSelectSound;
  }

  public String getCursorCancelSound() {
    return cursorCancelSound;
  }

  public String getProjectPath() {
    return this.projectPath;
  }

  public void setProjectPath(String projectPath) {
    this.projectPath = projectPath;
  }

  public String getGameTitle() {
    return this.gameTitle;
  }

  public void setGameTitle(String gameTitle) {
    this.gameTitle = gameTitle;
  }

  public int getMainScreenType() {
    return mainScreenType;
  }

  public void setMainScreenType(int mainScreenType) {
    this.mainScreenType = mainScreenType;
  }

  public boolean getFullscreenMode() {
    return extendToFullScreen == 1;
  }

  public void setExtendToFullScreen(int extendToFullScreen) {
    this.extendToFullScreen = extendToFullScreen;
  }

  public int getResolutionMode() {
    return mainResolution;
  }

  public int getColourDepth() {
    return colordepth;
  }

  public boolean getJoystickStatus() {
    return enableJoyStick == 1;
  }

  public String getInitBoard() {
    return initBoard;
  }

  public String getInitChar() {
    return initChar;
  }

  public String getStartupPrg() {
    return startupPrg;
  }

  public int getExtendToFullScreen() {
    return extendToFullScreen;
  }

  public int getMainResolution() {
    return mainResolution;
  }

  public int getMainDisableProtectReg() {
    return mainDisableProtectReg;
  }

  public String getLanguageFile() {
    return languageFile;
  }

  public String getRunTime() {
    return runTime;
  }

  public int getRunKey() {
    return runKey;
  }

  public int getMenuKey() {
    return menuKey;
  }

  public int getKey() {
    return key;
  }

  public List<RunTimeKey> getRunTimeArray() {
    return runTimeArray;
  }

  public String getMenuPlugin() {
    return menuPlugin;
  }

  public String getFightPlugin() {
    return fightPlugin;
  }

  public int getFightingEnabled() {
    return fightingEnabled;
  }

  public List<EnemySkillPair> getEnemyArray() {
    return enemyArray;
  }

  public int getFightType() {
    return fightType;
  }

  public long getFightChance() {
    return fightChance;
  }

  public int getUseCustomBattleSystem() {
    return useCustomBattleSystem;
  }

  public String getBattleSystemProgram() {
    return battleSystemProgram;
  }

  public String getGameOverProgram() {
    return gameOverProgram;
  }

  public String getButtonGraphic() {
    return buttonGraphic;
  }

  public String getWindowGraphic() {
    return windowGraphic;
  }

  public List<String> getPluginArray() {
    return pluginArray;
  }

  public int getUseDayNight() {
    return useDayNight;
  }

  public int getDayNightType() {
    return dayNightType;
  }

  public long getDayLengthInMins() {
    return dayLengthInMins;
  }

  public int getEnableJoyStick() {
    return enableJoyStick;
  }

  public int getColorDepth() {
    return colordepth;
  }

  public int getGameSpeed() {
    return gameSpeed;
  }

  public int getUsePixelBasedMovement() {
    return usePixelBasedMovement;
  }

  public String getMouseCursor() {
    return mouseCursor;
  }

  public int getHotSpotX() {
    return hotSpotX;
  }

  public int getHotSpotY() {
    return hotSpotY;
  }

  public long getTransparentColor() {
    return transpcolor;
  }

  public int getDisplayFPSInTitle() {
    return displayFPSInTitle;
  }

  public int getPathfindingAlgorithm() {
    return pathfindingAlgo;
  }

  public long getDrawVectors() {
    return drawVectors;
  }

  public long getPathColor() {
    return pathColor;
  }

  public long getMovementControls() {
    return movementControls;
  }

  public List<Integer> getMovementKeys() {
    return movementKeys;
  }

  public void setMainResolution(int mainResolution) {
    this.mainResolution = mainResolution;
  }

  public void setMainDisableProtectReg(int mainDisableProtectReg) {
    this.mainDisableProtectReg = mainDisableProtectReg;
  }

  public void setLanguageFile(String languageFile) {
    this.languageFile = languageFile;
  }

  public void setStartupPrg(String startupPrg) {
    this.startupPrg = startupPrg;
  }

  public void setInitBoard(String initBoard) {
    this.initBoard = initBoard;
  }

  public void setInitChar(String initChar) {
    this.initChar = initChar;
  }

  public void setRunTime(String runTime) {
    this.runTime = runTime;
  }

  public void setRunKey(int runKey) {
    this.runKey = runKey;
  }

  public void setMenuKey(int menuKey) {
    this.menuKey = menuKey;
  }

  public void setKey(int key) {
    this.key = key;
  }

  public void setRunTimeArray(Collection<RunTimeKey> runTimeArray) {
    this.runTimeArray.clear();
    this.runTimeArray.addAll(runTimeArray);
  }

  public void setMenuPlugin(String menuPlugin) {
    this.menuPlugin = menuPlugin;
  }

  public void setFightPlugin(String fightPlugin) {
    this.fightPlugin = fightPlugin;
  }

  public void setFightingEnabled(int fightingEnabled) {
    this.fightingEnabled = fightingEnabled;
  }

  public void setEnemyArray(Collection<EnemySkillPair> enemyArray) {
    this.enemyArray.clear();
    this.enemyArray.addAll(enemyArray);
  }

  public void setFightType(int fightType) {
    this.fightType = fightType;
  }

  public void setFightChance(long fightChance) {
    this.fightChance = fightChance;
  }

  public void setUseCustomBattleSystem(int useCustomBattleSystem) {
    this.useCustomBattleSystem = useCustomBattleSystem;
  }

  public void setBattleSystemProgram(String battleSystemProgram) {
    this.battleSystemProgram = battleSystemProgram;
  }

  public void setGameOverProgram(String gameOverProgram) {
    this.gameOverProgram = gameOverProgram;
  }

  public void setButtonGraphic(String buttonGraphic) {
    this.buttonGraphic = buttonGraphic;
  }

  public void setWindowGraphic(String windowGraphic) {
    this.windowGraphic = windowGraphic;
  }

  public void setPluginArray(Collection<String> pluginArray) {
    this.pluginArray.clear();
    this.pluginArray.addAll(pluginArray);
  }

  public void setUseDayNight(int useDayNight) {
    this.useDayNight = useDayNight;
  }

  public void setDayNightType(int dayNightType) {
    this.dayNightType = dayNightType;
  }

  public void setDayLengthInMins(long dayLengthInMins) {
    this.dayLengthInMins = dayLengthInMins;
  }

  public void setCursorMoveSound(String cursorMoveSound) {
    this.cursorMoveSound = cursorMoveSound;
  }

  public void setCursorSelectSound(String cursorSelectSound) {
    this.cursorSelectSound = cursorSelectSound;
  }

  public void setCursorCancelSound(String cursorCancelSound) {
    this.cursorCancelSound = cursorCancelSound;
  }

  public void setEnableJoyStick(int enableJoyStick) {
    this.enableJoyStick = enableJoyStick;
  }

  public void setColorDepth(int colordepth) {
    this.colordepth = colordepth;
  }

  public void setGameSpeed(int gameSpeed) {
    this.gameSpeed = gameSpeed;
  }

  public void setUsePixelBasedMovement(int usePixelBasedMovement) {
    this.usePixelBasedMovement = usePixelBasedMovement;
  }

  public void setMouseCursor(String mouseCursor) {
    this.mouseCursor = mouseCursor;
  }

  public void setHotSpotX(int hotSpotX) {
    this.hotSpotX = hotSpotX;
  }

  public void setHotSpotY(int hotSpotY) {
    this.hotSpotY = hotSpotY;
  }

  public void setTransparentColor(long transpcolor) {
    this.transpcolor = transpcolor;
  }

  public void setDisplayFPSInTitle(int displayFPSInTitle) {
    this.displayFPSInTitle = displayFPSInTitle;
  }

  public void setPathfindingAlgorithm(int pathfindingAlgo) {
    this.pathfindingAlgo = pathfindingAlgo;
  }

  public void setDrawVectors(long drawVectors) {
    this.drawVectors = drawVectors;
  }

  public void setPathColor(long pathColor) {
    this.pathColor = pathColor;
  }

  public void setMovementControls(long movementControls) {
    this.movementControls = movementControls;
  }

  public void setMovementKeys(Collection<Integer> movementKeys) {
    this.movementKeys.clear();
    this.movementKeys.addAll(movementKeys);
  }

  @Override
  public void reset() {
    runTimeArray.clear();
    enemyArray.clear();
    pluginArray.clear();
    movementKeys.clear();

    mainScreenType = 1;
    extendToFullScreen = 0;
    mainResolution = 0;
    mainDisableProtectReg = 0;
    languageFile = "";
    startupPrg = "";
    initBoard = "";
    initChar = "";
    runTime = "";
    runKey = 0;
    menuKey = 0;
    key = 0;
    menuPlugin = "";
    fightPlugin = "";
    fightingEnabled = 0;
    fightType = 0;
    fightChance = 0;
    useCustomBattleSystem = 0;
    battleSystemProgram = "";
    gameOverProgram = "";
    buttonGraphic = "";
    windowGraphic = "";
    useDayNight = 0;
    dayNightType = 0;
    dayLengthInMins = 0;
    cursorMoveSound = "";
    cursorSelectSound = "";
    cursorCancelSound = "";
    enableJoyStick = 1;
    colordepth = 0;
    gameSpeed = 128;
    usePixelBasedMovement = 0;
    mouseCursor = "";
    hotSpotX = 0;
    hotSpotY = 0;
    transpcolor = 255;
    resolutionWidth = 0;
    resolutionHeight = 0;
    displayFPSInTitle = 0;
    pathfindingAlgo = 1;
    drawVectors = 0;
    pathColor = 0;
    movementControls = 0;
  }

}
