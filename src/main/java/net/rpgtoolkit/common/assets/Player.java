/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.rpgtoolkit.common.utilities.PropertiesSingleton;

public class Player extends AbstractSprite {

  // Constants
  //TODO: can these be changed to enums or something?
  public static final int DIRECTION_NORTH = 1;
  public static final int DIRECTION_EAST = 2;
  public static final int DIRECTION_SOUTH = 0;
  public static final int DIRECTION_WEST = 3;

  private String name;
  private String expVariableName;
  private String dpVariableName;
  private String fpVariableName;
  private String hpVariableName;
  private String maxHPVariableName;
  private String nameVariableName;
  private String mpVariableName;
  private String maxMPVariableName;
  private String lvlVariableName;
  private int initialExperience;
  private int initialHP;
  private int initialMaxHP;
  private int initialDP;
  private int initialFP;
  private int initialMP;
  private int initialMaxMP;
  private int initialLevel;
  private String profilePicture;
  private ArrayList<PlayerSpecialMove> specialMoveList;
  private String specialMovesName;
  private boolean hasSpecialMoves;
  private ArrayList<String> accessoryNames;
  private boolean armourTypes[] = new boolean[7];
  private int levelType;
  private int expIncreaseFactor;
  private int maxLevel;
  private int percentHPIncrease;
  private int percentMPIncrease;
  private int percentDPIncrease;
  private int percentFPIncrease;
  private String programOnLevelUp;
  private byte levelUpType;
  private byte characterSize;

  // Active animation for the engine
  private Animation activeAnimation;
  private int frameNumber;
  private int frameCount;
  private Timer animationTimer;

  // Engine Variables
  private int currentXLocation;
  private int currentYLocation;
  private int vectorCorrectionX;
  private int vectorCorrectionY;
  private Area baseArea;

  /**
   * Opens a player from an existing file
   *
   * @param descriptor Character (.tem) file to open
   */
  public Player(AssetDescriptor descriptor) {
    super(descriptor);
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the expVariableName
   */
  public String getExpVariableName() {
    return expVariableName;
  }

  /**
   * @param expVariableName the expVariableName to set
   */
  public void setExpVariableName(String expVariableName) {
    this.expVariableName = expVariableName;
  }

  /**
   * @return the dpVariableName
   */
  public String getDpVariableName() {
    return dpVariableName;
  }

  /**
   * @param dpVariableName the dpVariableName to set
   */
  public void setDpVariableName(String dpVariableName) {
    this.dpVariableName = dpVariableName;
  }

  /**
   * @return the fpVariableName
   */
  public String getFpVariableName() {
    return fpVariableName;
  }

  /**
   * @param fpVariableName the fpVariableName to set
   */
  public void setFpVariableName(String fpVariableName) {
    this.fpVariableName = fpVariableName;
  }

  /**
   * @return the hpVariableName
   */
  public String getHpVariableName() {
    return hpVariableName;
  }

  /**
   * @param hpVariableName the hpVariableName to set
   */
  public void setHpVariableName(String hpVariableName) {
    this.hpVariableName = hpVariableName;
  }

  /**
   * @return the maxHPVariableName
   */
  public String getMaxHPVariableName() {
    return maxHPVariableName;
  }

  /**
   * @param maxHPVariableName the maxHPVariableName to set
   */
  public void setMaxHPVariableName(String maxHPVariableName) {
    this.maxHPVariableName = maxHPVariableName;
  }

  /**
   * @return the nameVariableName
   */
  public String getNameVariableName() {
    return nameVariableName;
  }

  /**
   * @param nameVariableName the nameVariableName to set
   */
  public void setNameVariableName(String nameVariableName) {
    this.nameVariableName = nameVariableName;
  }

  /**
   * @return the mpVariableName
   */
  public String getMpVariableName() {
    return mpVariableName;
  }

  /**
   * @param mpVariableName the mpVariableName to set
   */
  public void setMpVariableName(String mpVariableName) {
    this.mpVariableName = mpVariableName;
  }

  /**
   * @return the maxMPVariableName
   */
  public String getMaxMPVariableName() {
    return maxMPVariableName;
  }

  /**
   * @param maxMPVariableName the maxMPVariableName to set
   */
  public void setMaxMPVariableName(String maxMPVariableName) {
    this.maxMPVariableName = maxMPVariableName;
  }

  /**
   * @return the lvlVariableName
   */
  public String getLvlVariableName() {
    return lvlVariableName;
  }

  /**
   * @param lvlVariableName the lvlVariableName to set
   */
  public void setLvlVariableName(String lvlVariableName) {
    this.lvlVariableName = lvlVariableName;
  }

  /**
   * @return the initialExperience
   */
  public int getInitialExperience() {
    return initialExperience;
  }

  /**
   * @param initialExperience the initialExperience to set
   */
  public void setInitialExperience(int initialExperience) {
    this.initialExperience = initialExperience;
  }

  /**
   * @return the initialHP
   */
  public int getInitialHP() {
    return initialHP;
  }

  /**
   * @param initialHP the initialHP to set
   */
  public void setInitialHP(int initialHP) {
    this.initialHP = initialHP;
  }

  /**
   * @return the initialMaxHP
   */
  public int getInitialMaxHP() {
    return initialMaxHP;
  }

  /**
   * @param initialMaxHP the initialMaxHP to set
   */
  public void setInitialMaxHP(int initialMaxHP) {
    this.initialMaxHP = initialMaxHP;
  }

  /**
   * @return the initialDP
   */
  public int getInitialDP() {
    return initialDP;
  }

  /**
   * @param initialDP the initialDP to set
   */
  public void setInitialDP(int initialDP) {
    this.initialDP = initialDP;
  }

  /**
   * @return the initialFP
   */
  public int getInitialFP() {
    return initialFP;
  }

  /**
   * @param initialFP the initialFP to set
   */
  public void setInitialFP(int initialFP) {
    this.initialFP = initialFP;
  }

  /**
   * @return the initialMP
   */
  public int getInitialMP() {
    return initialMP;
  }

  /**
   * @param initialMP the initialMP to set
   */
  public void setInitialMP(int initialMP) {
    this.initialMP = initialMP;
  }

  /**
   * @return the initialMaxMP
   */
  public int getInitialMaxMP() {
    return initialMaxMP;
  }

  /**
   * @param initialMaxMP the initialMaxMP to set
   */
  public void setInitialMaxMP(int initialMaxMP) {
    this.initialMaxMP = initialMaxMP;
  }

  /**
   * @return the initialLevel
   */
  public int getInitialLevel() {
    return initialLevel;
  }

  /**
   * @param initialLevel the initialLevel to set
   */
  public void setInitialLevel(int initialLevel) {
    this.initialLevel = initialLevel;
  }

  /**
   * @return the profilePicture
   */
  public String getProfilePicture() {
    return profilePicture;
  }

  /**
   * @param profilePicture the profilePicture to set
   */
  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }

  /**
   * @return the specialMoveList
   */
  public ArrayList<PlayerSpecialMove> getSpecialMoveList() {
    return specialMoveList;
  }

  /**
   * 
   * @param specialMoveList 
   */
  public void setSpecialMoveList(ArrayList<PlayerSpecialMove> specialMoveList) {
    this.specialMoveList = specialMoveList;
  }

  /**
   * @return the specialMoveName
   */
  public String getSpecialMovesName() {
    return specialMovesName;
  }

  /**
   * @param specialMoveName the specialMoveName to set
   */
  public void setSpecialMovesName(String specialMoveName) {
    this.specialMovesName = specialMoveName;
  }

  /**
   * @return the hasSepcialMoves
   */
  public boolean getHasSpecialMoves() {
    return hasSpecialMoves;
  }

  /**
   * @param hasSpecialMoves the hasSpecialMoves to set
   */
  public void setHasSpecialMoves(boolean hasSpecialMoves) {
    this.hasSpecialMoves = hasSpecialMoves;
  }

  /**
   * @return the accessoryName
   */
  public ArrayList<String> getAccessoryNames() {
    return accessoryNames;
  }

  /**
   * 
   * @param accessoryNames 
   */
  public void setAccessoryNames(ArrayList<String> accessoryNames) {
    this.accessoryNames = accessoryNames;
  }

  /**
   * @return an array of booleans indicating whether the Player can equip the type of armor for that
   * slot
   */
  public boolean[] getArmourTypes() {
    return armourTypes;
  }

  /**
   *
   * @param armourTypes
   */
  public void setArmourTypes(boolean[] armourTypes) {
    this.armourTypes = armourTypes;
  }

  /**
   * @return the levelType
   */
  public int getLevelType() {
    return levelType;
  }

  /**
   * @param levelType the levelType to set
   */
  public void setLevelType(int levelType) {
    this.levelType = levelType;
  }

  /**
   * @return the expIncreaseFactor
   */
  public int getExpIncreaseFactor() {
    return expIncreaseFactor;
  }

  /**
   * @param expIncreaseFactor the expIncreaseFactor to set
   */
  public void setExpIncreaseFactor(int expIncreaseFactor) {
    this.expIncreaseFactor = expIncreaseFactor;
  }

  /**
   * @return the maxLevel
   */
  public int getMaxLevel() {
    return maxLevel;
  }

  /**
   * @param maxLevel the maxLevel to set
   */
  public void setMaxLevel(int maxLevel) {
    this.maxLevel = maxLevel;
  }

  /**
   * @return the percentHPIncrease
   */
  public int getPercentHPIncrease() {
    return percentHPIncrease;
  }

  /**
   * @param percentHPIncrease the percentHPIncrease to set
   */
  public void setPercentHPIncrease(int percentHPIncrease) {
    this.percentHPIncrease = percentHPIncrease;
  }

  /**
   * @return the percentMPIncrease
   */
  public int getPercentMPIncrease() {
    return percentMPIncrease;
  }

  /**
   * @param percentMPIncrease the percentMPIncrease to set
   */
  public void setPercentMPIncrease(int percentMPIncrease) {
    this.percentMPIncrease = percentMPIncrease;
  }

  /**
   * @return the percentDPIncrease
   */
  public int getPercentDPIncrease() {
    return percentDPIncrease;
  }

  /**
   * @param percentDPIncrease the percentDPIncrease to set
   */
  public void setPercentDPIncrease(int percentDPIncrease) {
    this.percentDPIncrease = percentDPIncrease;
  }

  /**
   * @return the percentFPIncrease
   */
  public int getPercentFPIncrease() {
    return percentFPIncrease;
  }

  /**
   * @param percentFPIncrease the percentFPIncrease to set
   */
  public void setPercentFPIncrease(int percentFPIncrease) {
    this.percentFPIncrease = percentFPIncrease;
  }

  /**
   * @return the programOnLevelUp
   */
  public String getProgramOnLevelUp() {
    return programOnLevelUp;
  }

  /**
   * @param programOnLevelUp the programOnLevelUp to set
   */
  public void setProgramOnLevelUp(String programOnLevelUp) {
    this.programOnLevelUp = programOnLevelUp;
  }

  /**
   * @return the levelUpType
   */
  public byte getLevelUpType() {
    return levelUpType;
  }

  /**
   * @param levelUpType the levelUpType to set
   */
  public void setLevelUpType(byte levelUpType) {
    this.levelUpType = levelUpType;
  }

  /**
   * @return the characterSize
   */
  public byte getCharacterSize() {
    return characterSize;
  }

  /**
   * @param characterSize the characterSize to set
   */
  public void setCharacterSize(byte characterSize) {
    this.characterSize = characterSize;
  }

  /**
   * Loads the animations into memory, used in the engine, this is not called during load as it
   * would use too much memory to load every animation into memory for all players/enemies/items at
   * once.
   * <p>
   * This should be called before rendering/game play takes place to avoid hindering performance
   */
  public void loadAnimations() {
    System.out.println("Loading Animations for " + this.name);

    standardGraphicsAnimations = new ArrayList<>();
    for (String anmFile : standardGraphics) {
      if (!anmFile.equals("")) {
        try {
          File animationFile = new File(System.getProperty("project.path")
                  + File.pathSeparator
                  + PropertiesSingleton.getProperty("toolkit.directory.misc")
                  + File.pathSeparator
                  + anmFile);

          AssetHandle handle = AssetManager.getInstance().deserialize(
                  new AssetDescriptor(animationFile.toURI()));
          Animation a = (Animation) handle.getAsset();
          standardGraphicsAnimations.add(a);
        } catch (AssetException | IOException ex) {
          Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

    animationTimer = new Timer();
    animationTimer.schedule(new AnimationTimer(), 0, 1000 / 6);
  }

  public void setActiveAnimation(int id) {
    activeAnimation = standardGraphicsAnimations.get(id);
    frameNumber = 0;
    frameCount = (int) activeAnimation.getFrameCount();
  }

  public BufferedImage getAnimationFrame() {
    return activeAnimation.getFrame(frameNumber).getFrameImage();
  }

  @Override
  public void reset() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private class AnimationTimer extends TimerTask {

    public AnimationTimer() {
      super();
    }

    @Override
    public void run() {
      if (frameNumber == (frameCount - 1)) {
        frameNumber = 0;
      } else {
        frameNumber++;
      }
    }

  }

  public void preparePhysics(int width, int height) {
    vectorCorrectionX = width / 2;
    vectorCorrectionY = height;

    currentXLocation = 1;
    currentYLocation = 1;
  }

  public int getVectorCorrectionX() {
    return vectorCorrectionX;
  }

  public int getVectorCorrectionY() {
    return vectorCorrectionY;
  }

  public Area getCollisionArea(int correctX, int correctY, int shiftX, int shiftY) {
    Polygon collisionPoly = new Polygon();

    for (Point point : baseVector.getPoints()) {
      collisionPoly.addPoint(point.x + correctX + (currentXLocation + shiftX),
              point.y + correctY + (currentYLocation + shiftY));
    }
    baseArea = new Area(collisionPoly);
    return baseArea;
  }

  public int getXLocation() {
    return currentXLocation;
  }

  public int getYLocation() {
    return currentYLocation;
  }

  public void setXLocation(int newXLocation) {
    this.currentXLocation = newXLocation;
  }

  public void setYLocation(int newYLocation) {
    this.currentYLocation = newYLocation;
  }

  public void adjustX(int adjustment) {
    currentXLocation += adjustment;
  }

  public void adjustY(int adjustment) {
    currentYLocation += adjustment;
  }

}
