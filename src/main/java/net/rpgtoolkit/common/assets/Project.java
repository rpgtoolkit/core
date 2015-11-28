/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import net.rpgtoolkit.common.CorruptAssetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.rpgtoolkit.common.utilities.BinaryIO;
import net.rpgtoolkit.common.utilities.PropertiesSingleton;

/**
 * This class manages the GAM file type for the RPG Toolkit
 *
 * @author Geoff Wilson
 * @version 0.1
 */
public class Project extends BasicType implements Asset {
  // Some useful constants

  private final String DEFAULT_MENU_PLUGIN = "tk3menu.dll";
  private final String DEFAULT_FIGHT_PLUGIN = "tk3fight.dll";
  private final String FILE_HEADER = "RPGTLKIT MAIN";
  private final String DEFAULT_MOUSE_CURSOR = "TK DEFAULT";
  private final int MAJOR_VERSION = 2;
  private final int MINOR_VERSION = 9;

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
  private ArrayList<RunTimeKey> runTimeArray;
  private String menuPlugin;
  private String fightPlugin;
  private int fightingEnabled;
  private ArrayList<EnemySkillPair> enemyArray;
  private int fightType;
  private long fightChance;
  private int useCustomBattleSystem;
  private String battleSystemProgram;
  private String gameOverProgram;
  private String buttonGraphic;
  private String windowGraphic;
  private ArrayList<String> pluginArray;
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
  private ArrayList<Integer> movementKeys;

  /**
   * Creates a new project file
   *
   * @param path
   * @param title
   */
  public Project(String path, String title) {
    file = new File(path + File.separator + title + ".gam.json");
    projectPath = path;
    gameTitle = title;
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
    runTimeArray = new ArrayList<>();
    menuPlugin = "";
    fightPlugin = "";
    fightingEnabled = 0;
    enemyArray = new ArrayList<>();
    fightType = 0;
    fightChance = 0;
    useCustomBattleSystem = 0;
    battleSystemProgram = "";
    gameOverProgram = "";
    buttonGraphic = "";
    windowGraphic = "";
    pluginArray = new ArrayList<>();
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
    movementKeys = new ArrayList<>();
  }

  /**
   * Opens a project from an existing file
   *
   * @param file Project (.GAM) file to open
   * @param projectPath current project path
   */
  public Project(File file, String projectPath) {
    super(file);

    // Property is not shared between editor and common projects!
    System.setProperty("project.path", projectPath);
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

  public String getDEFAULT_MENU_PLUGIN() {
    return DEFAULT_MENU_PLUGIN;
  }

  public String getDEFAULT_FIGHT_PLUGIN() {
    return DEFAULT_FIGHT_PLUGIN;
  }

  public String getFILE_HEADER() {
    return FILE_HEADER;
  }

  public String getDEFAULT_MOUSE_CURSOR() {
    return DEFAULT_MOUSE_CURSOR;
  }

  public int getMAJOR_VERSION() {
    return MAJOR_VERSION;
  }

  public int getMINOR_VERSION() {
    return MINOR_VERSION;
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

  public ArrayList<RunTimeKey> getRunTimeArray() {
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

  public ArrayList<EnemySkillPair> getEnemyArray() {
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

  public ArrayList<String> getPluginArray() {
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

  public int getColordepth() {
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

  public long getTranspcolor() {
    return transpcolor;
  }

  public int getDisplayFPSInTitle() {
    return displayFPSInTitle;
  }

  public int getPathfindingAlgo() {
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

  public ArrayList<Integer> getMovementKeys() {
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

  public void setRunTimeArray(ArrayList<RunTimeKey> runTimeArray) {
    this.runTimeArray = runTimeArray;
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

  public void setEnemyArray(ArrayList<EnemySkillPair> enemyArray) {
    this.enemyArray = enemyArray;
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

  public void setPluginArray(ArrayList<String> pluginArray) {
    this.pluginArray = pluginArray;
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

  public void setColordepth(int colordepth) {
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

  public void setTranspcolor(long transpcolor) {
    this.transpcolor = transpcolor;
  }

  public void setDisplayFPSInTitle(int displayFPSInTitle) {
    this.displayFPSInTitle = displayFPSInTitle;
  }

  public void setPathfindingAlgo(int pathfindingAlgo) {
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

  public void setMovementKeys(ArrayList<Integer> movementKeys) {
    this.movementKeys = movementKeys;
  }

  /**
   * Used to open the old binary file format from the TK 3.x era, this remains here simply for those
   * few who wish to migrate to TK 4.0. However it will be removed in the next iteration of 4.1.
   *
   * @return true for success, false for failure
   * @deprecated
   */
  public boolean openBinary() {
    try {
      // Prepare objects be we begin
      runTimeArray = new ArrayList<>();
      enemyArray = new ArrayList<>();
      pluginArray = new ArrayList<>();

      // First thing we must do is check the header
      if (binaryIO.readBinaryString().equals(FILE_HEADER)) // Valid file we can proceede
      {
        // get file version number (normally 2.9)
        int majorVersion = binaryIO.readBinaryInteger(); // should be 2
        int minorVersion = binaryIO.readBinaryInteger(); // should be 9

        // File spec seems to have two useless read calls here
        inputStream.skip(2); // Skip ahead 2 bytes
        binaryIO.readBinaryString(); // "NOCODE" skipped

        projectPath = binaryIO.readBinaryString();

        // appends a leading slash to the games project folder
        projectPath = "/" + projectPath.toLowerCase().replace("\\", "/");

        gameTitle = binaryIO.readBinaryString();
        mainScreenType = binaryIO.readBinaryInteger();
        extendToFullScreen = binaryIO.readBinaryInteger();
        mainResolution = binaryIO.readBinaryInteger();

        if (minorVersion < 3) {
          inputStream.skip(2); // Skip over the old parallax value
        }

        mainDisableProtectReg = binaryIO.readBinaryInteger();
        languageFile = binaryIO.readBinaryString();

        startupPrg = binaryIO.readBinaryString();
        initBoard = binaryIO.readBinaryString();
        initChar = binaryIO.readBinaryString();

        runTime = binaryIO.readBinaryString();
        runKey = binaryIO.readBinaryInteger();
        menuKey = binaryIO.readBinaryInteger();
        key = binaryIO.readBinaryInteger();

        // Read in array of run time keys
        int numberOfKeys = binaryIO.readBinaryInteger();
        for (int i = 0; i < numberOfKeys + 1; i++) {
          int runTimeKey = binaryIO.readBinaryInteger();
          String runTimeProgram = binaryIO.readBinaryString();
          RunTimeKey newKey = new RunTimeKey(runTimeKey, runTimeProgram);
          runTimeArray.add(newKey);
        }

        // Deal with lack of plugin data in main file
        if (minorVersion >= 3) {
          menuPlugin = binaryIO.readBinaryString();
          fightPlugin = binaryIO.readBinaryString();
        } else {
          inputStream.skip(8); // Junk Data to be skipped
          menuPlugin = DEFAULT_MENU_PLUGIN;
          fightPlugin = DEFAULT_FIGHT_PLUGIN;
        }

        fightingEnabled = binaryIO.readBinaryInteger();
        int numberOfEnemies = binaryIO.readBinaryInteger(); // Pointless!
        for (int i = 0; i < numberOfEnemies + 1; i++) {
          String enemy = binaryIO.readBinaryString();
          int skill = binaryIO.readBinaryInteger();
          EnemySkillPair newEnemy = new EnemySkillPair(enemy, skill);
          enemyArray.add(newEnemy);
        }

        fightType = binaryIO.readBinaryInteger();
        fightChance = binaryIO.readBinaryLong();
        useCustomBattleSystem = binaryIO.readBinaryInteger();
        battleSystemProgram = binaryIO.readBinaryString();

        if (minorVersion <= 2) {
          inputStream.skip(2); // Old fight style option
        }

        gameOverProgram = binaryIO.readBinaryString();

        buttonGraphic = binaryIO.readBinaryString();
        windowGraphic = binaryIO.readBinaryString();

        int pluginCount = binaryIO.readBinaryInteger();
        if (minorVersion <= 2) {
          for (int i = 0; i < pluginCount + 1; i++) {
            int readin = binaryIO.readBinaryInteger();
            if (readin == 1) {
              pluginArray.add("tkplug" + i + ".dll");
            }
          }
        } else {
          for (int i = 0; i < pluginCount + 1; i++) {
            pluginArray.add(binaryIO.readBinaryString());
          }
        }

        useDayNight = binaryIO.readBinaryInteger();
        dayNightType = binaryIO.readBinaryInteger();
        dayLengthInMins = binaryIO.readBinaryLong();

        if (minorVersion >= 3) {
          cursorMoveSound = binaryIO.readBinaryString();
          cursorSelectSound = binaryIO.readBinaryString();
          cursorCancelSound = binaryIO.readBinaryString();
          enableJoyStick = inputStream.read();
          colordepth = inputStream.read();
        }

        if (minorVersion >= 4) {
          gameSpeed = inputStream.read();
          usePixelBasedMovement = inputStream.read();
        } else {
          gameSpeed = 0;
        }

        if (minorVersion <= 7) {
          // Does something with gamespeed
        }

        if (minorVersion <= 5) {
          if (minorVersion == 5) {
            if (inputStream.read() == 1) {
              mouseCursor = DEFAULT_MOUSE_CURSOR;
            } else {
              mouseCursor = "";
            }
          } else {
            mouseCursor = DEFAULT_MOUSE_CURSOR;
          }
          hotSpotX = 0;
          hotSpotY = 0;
        } else {
          mouseCursor = binaryIO.readBinaryString();
          hotSpotX = inputStream.read();
          hotSpotY = inputStream.read();
          transpcolor = binaryIO.readBinaryLong();
        }

        if (mouseCursor.equals(DEFAULT_MOUSE_CURSOR)) {
          transpcolor = 255;
        }

        if (minorVersion >= 7) {
          resolutionWidth = binaryIO.readBinaryLong();
          resolutionHeight = binaryIO.readBinaryLong();
        }

        if (minorVersion >= 8) {
          displayFPSInTitle = inputStream.read();
        }

        if (minorVersion >= 9) {
          pathfindingAlgo = binaryIO.readBinaryInteger();
          drawVectors = binaryIO.readBinaryLong();
          pathColor = binaryIO.readBinaryLong();
          movementControls = binaryIO.readBinaryLong();

          movementKeys = new ArrayList();

          for (int i = 0; i < 8; i++) {

            movementKeys.add(binaryIO.readBinaryInteger());
          }
        }

      }

      // Release the file
      this.inputStream.close();
      this.binaryIO.closeInput();

      return true;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return false;
    } catch (CorruptAssetException e) {
      e.printStackTrace();
      return false;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

  }

  /**
   * Used to save the old binary file format from the TK 3.x era, this remains here simply because
   * it took so long to implement it that throwing it away for 4.0 would be a waste of work. It will
   * be removed in 4.1.
   *
   * @return true for success, and false for failure
   * @deprecated
   */
  public boolean saveBinary() {
    try {
      outputStream = new FileOutputStream(this.file);
      binaryIO.setOutputStream(outputStream);

      binaryIO.writeBinaryString(FILE_HEADER);
      binaryIO.writeBinaryInteger(MAJOR_VERSION);
      binaryIO.writeBinaryInteger(MINOR_VERSION);
      binaryIO.writeBinaryInteger(1);
      binaryIO.writeBinaryString("NOCODE");
      binaryIO.writeBinaryString(projectPath);
      binaryIO.writeBinaryString(gameTitle);
      binaryIO.writeBinaryInteger(mainScreenType);
      binaryIO.writeBinaryInteger(extendToFullScreen);
      binaryIO.writeBinaryInteger(mainResolution);
      binaryIO.writeBinaryInteger(mainDisableProtectReg);
      binaryIO.writeBinaryString(languageFile);
      binaryIO.writeBinaryString(startupPrg);
      binaryIO.writeBinaryString(initBoard);
      binaryIO.writeBinaryString(initChar);
      binaryIO.writeBinaryString(runTime);
      binaryIO.writeBinaryInteger(runKey);
      binaryIO.writeBinaryInteger(menuKey);
      binaryIO.writeBinaryInteger(key);
      binaryIO.writeBinaryInteger(50);
      for (int i = 0; i < 51; i++) {
        binaryIO.writeBinaryInteger(runTimeArray.get(i).getKey());
        binaryIO.writeBinaryString(runTimeArray.get(i).getProgram());
      }
      binaryIO.writeBinaryString(menuPlugin);
      binaryIO.writeBinaryString(fightPlugin);
      binaryIO.writeBinaryInteger(fightingEnabled);
      binaryIO.writeBinaryInteger(500);
      for (int i = 0; i < 501; i++) {
        binaryIO.writeBinaryString(enemyArray.get(i).getEnemy());
        binaryIO.writeBinaryInteger(enemyArray.get(i).getSkill());
      }
      binaryIO.writeBinaryInteger(fightType);
      binaryIO.writeBinaryLong(fightChance);
      binaryIO.writeBinaryInteger(useCustomBattleSystem);
      binaryIO.writeBinaryString(battleSystemProgram);
      binaryIO.writeBinaryString(gameOverProgram);
      binaryIO.writeBinaryString(buttonGraphic);
      binaryIO.writeBinaryString(windowGraphic);
      binaryIO.writeBinaryInteger(pluginArray.size() - 1);
      for (String aPluginArray : pluginArray) {
        binaryIO.writeBinaryString(aPluginArray);
      }
      binaryIO.writeBinaryInteger(useDayNight);
      binaryIO.writeBinaryInteger(dayNightType);
      binaryIO.writeBinaryLong(dayLengthInMins);
      binaryIO.writeBinaryString(cursorMoveSound);
      binaryIO.writeBinaryString(cursorSelectSound);
      binaryIO.writeBinaryString(cursorCancelSound);
      outputStream.write(enableJoyStick);
      outputStream.write(colordepth);
      outputStream.write(gameSpeed);
      outputStream.write(usePixelBasedMovement);
      binaryIO.writeBinaryString(mouseCursor);
      outputStream.write(hotSpotX);
      outputStream.write(hotSpotY);
      binaryIO.writeBinaryLong(transpcolor);
      binaryIO.writeBinaryLong(resolutionWidth);
      binaryIO.writeBinaryLong(resolutionHeight);
      outputStream.write(displayFPSInTitle);
      binaryIO.writeBinaryInteger(pathfindingAlgo);
      binaryIO.writeBinaryLong(drawVectors);
      binaryIO.writeBinaryLong(pathColor);
      binaryIO.writeBinaryLong(movementControls);
      for (Integer aKeyCode : movementKeys) {
        binaryIO.writeBinaryInteger(aKeyCode);
      }
      outputStream.close();
      return true;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return false;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean save() {
    if (file.getName().endsWith(".brd")) {
      if (binaryIO == null) {
        binaryIO = new BinaryIO();
      }
      saveBinary();
    }

    try {
      AssetManager.getInstance().serialize(AssetManager.getInstance().getHandle(this));
      return true;
    } catch (IOException | AssetException ex) {
      Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
  }

  public boolean saveAs(File fileName) {
    this.file = fileName;
    return this.saveBinary();
  }

  @Override
  public AssetDescriptor getDescriptor() {
    return new AssetDescriptor(this.getFile().toURI());
  }

  @Override
  public void reset() {

  }

}
