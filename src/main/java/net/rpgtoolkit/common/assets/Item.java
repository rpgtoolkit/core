/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.util.ArrayList;
import java.util.List;

public class Item extends AbstractSprite {

  //TODO: refactor animation lists to not use integer constant indexes. maybe use map and/or enum?
  public final static int ITEM_WALK_S = 0;
  public final static int ITEM_WALK_N = 1;
  public final static int ITEM_WALK_E = 2;
  public final static int ITEM_WALK_W = 3;
  public final static int ITEM_WALK_NW = 4;
  public final static int ITEM_WALK_NE = 5;
  public final static int ITEM_WALK_SW = 6;
  public final static int ITEM_WALK_SE = 7;
  public final static int ITEM_REST = 8;
 
  private double speed;
  
  // Specific to the item type.
  private String description;
  private boolean isEquippable;
  private boolean isMenuDriven;
  private boolean isBoardDriven;
  private boolean isBattleDriven;
  private boolean usersSpecified;
  private List<String> userChar;
  private long buyPrice;
  private long sellPrice;
  private boolean isKeyItem;
  private List<Boolean> equipLocation;
  private String accessory;
  private long equipHP;
  private long equipDP;
  private long equipFP;
  private long equipSMP;
  private String equipProgram;
  private String removeProgram;
  private long menuHP;
  private long menuSMP;
  private String menuProgram;
  private long fightHP;
  private long fightSMP;
  private String fightProgram;
  private String fightAnimation;
  private String boardMultitaskProgram;
  private String boardPickUpProgram;
  private boolean isWide;

  public Item(AssetDescriptor descriptor) {
    super(descriptor);
    this.userChar = new ArrayList<>();
    this.equipLocation = new ArrayList<>();
    this.standardGraphics = new ArrayList<>();
    this.standardGraphicsAnimations = new ArrayList<>();
    this.customGraphics = new ArrayList<>();
    this.customGraphicsNames = new ArrayList<>();
    this.standingGraphics = new ArrayList<>();
    reset();
  }

  @Override
  public final void reset() {

    name = "";
    description = "";
    speed = 0.05;
    idleTimeBeforeStanding = 3;
    isEquippable = false;
    isMenuDriven = false;
    isBoardDriven = false;
    isBattleDriven = false;
    
    usersSpecified = false;
    userChar.clear();

    buyPrice = 0;
    sellPrice = 0;
    isKeyItem = false;
    equipLocation.clear();
    accessory = "";
    equipHP = 0;
    equipDP = 0;
    equipFP = 0;
    equipSMP = 0;
    equipProgram = "";
    removeProgram = "";
    menuHP = 0;
    menuSMP = 0;
    menuProgram = "";
    fightHP = 0;
    fightSMP = 0;
    fightProgram = "";
    fightAnimation = "";
    boardMultitaskProgram = "";
    boardPickUpProgram = "";
    isWide = true;
    standardGraphics.clear();
    standingGraphics.clear();
    customGraphics.clear();
    customGraphicsNames.clear();
    for (int i = 0; i != 5; i++) {
      customGraphics.add("");
      customGraphicsNames.add("");
    }
    baseVector = makeDefaultSpriteVector(true, false);
    activationVector = makeDefaultSpriteVector(false, false);
  }

  private BoardVector makeDefaultSpriteVector(boolean isCollisionVector, boolean isIsometric) {
    BoardVector toReturn = new BoardVector();
    if (isCollisionVector) {
      if (isIsometric) {
        toReturn.addPoint(-15, 0);
        toReturn.addPoint(0, 7);
        toReturn.addPoint(15, 0);
        toReturn.addPoint(0, -7);
      } else {
        //toReturn.setTileType(TT_SOLID);   //WARNING: needs to work when tiletypes exist
        toReturn.addPoint(-15, -15);
        toReturn.addPoint(15, -15);
        toReturn.addPoint(15, 0);
        toReturn.addPoint(-15, 0);
      }
    } else {
      if (isIsometric) {
        toReturn.addPoint(-31, 0);
        toReturn.addPoint(0, 15);
        toReturn.addPoint(31, 0);
        toReturn.addPoint(0, -15);
      } else {
        //toReturn.setTileType(TT_SOLID);   //WARNING: needs to work when tiletypes exist
        toReturn.addPoint(-24, -24);
        toReturn.addPoint(24, -24);
        toReturn.addPoint(24, 8);
        toReturn.addPoint(-24, 8);
      }
    }
    return (toReturn);
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the isEquippable
   */
  public boolean isIsEquippable() {
    return isEquippable;
  }

  /**
   * @param isEquippable the isEquippable to set
   */
  public void setIsEquippable(boolean isEquippable) {
    this.isEquippable = isEquippable;
  }

  /**
   * @return the isMenuDriven
   */
  public boolean isIsMenuDriven() {
    return isMenuDriven;
  }

  /**
   * @param isMenuDriven the isMenuDriven to set
   */
  public void setIsMenuDriven(boolean isMenuDriven) {
    this.isMenuDriven = isMenuDriven;
  }

  /**
   * @return the isBoardDriven
   */
  public boolean isIsBoardDriven() {
    return isBoardDriven;
  }

  /**
   * @param isBoardDriven the isBoardDriven to set
   */
  public void setIsBoardDriven(boolean isBoardDriven) {
    this.isBoardDriven = isBoardDriven;
  }

  /**
   * @return the isBattleDriven
   */
  public boolean isIsBattleDriven() {
    return isBattleDriven;
  }

  /**
   * @param isBattleDriven the isBattleDriven to set
   */
  public void setIsBattleDriven(boolean isBattleDriven) {
    this.isBattleDriven = isBattleDriven;
  }

  /**
   * @return the usersSpecified
   */
  public boolean getUsersSpecified() {
    return usersSpecified;
  }

  /**
   * @param usersSpecified the usersSpecified to set
   */
  public void setUsersSpecified(boolean usersSpecified) {
    this.usersSpecified = usersSpecified;
  }

  /**
   * @return the userChar
   */
  public List<String> getUserChar() {
    return userChar;
  }

  /**
   * 
   * @param userChar 
   */
  public void setUserChar(List<String> userChar) {
    this.userChar = userChar;
  }
  

  /**
   * @return the buyPrice
   */
  public long getBuyPrice() {
    return buyPrice;
  }

  /**
   * @param buyPrice the buyPrice to set
   */
  public void setBuyPrice(long buyPrice) {
    this.buyPrice = buyPrice;
  }

  /**
   * @return the sellPrice
   */
  public long getSellPrice() {
    return sellPrice;
  }

  /**
   * @param sellPrice the sellPrice to set
   */
  public void setSellPrice(long sellPrice) {
    this.sellPrice = sellPrice;
  }

  /**
   * @return the isKeyItem
   */
  public boolean getIsKeyItem() {
    return isKeyItem;
  }

  /**
   * @param isKeyItem the isKeyItem to set
   */
  public void setIsKeyItem(boolean isKeyItem) {
    this.isKeyItem = isKeyItem;
  }

  /**
   * @return the equipLocation
   */
  public List<Boolean> getEquipLocation() {
    return equipLocation;
  }

  /**
   * 
   * @param equipLocation 
   */
  public void setEquipLocation(List<Boolean> equipLocation) {
    this.equipLocation = equipLocation;
  }

  /**
   * @return the accessory
   */
  public String getAccessory() {
    return accessory;
  }

  /**
   * @param accessory the accessory to set
   */
  public void setAccessory(String accessory) {
    this.accessory = accessory;
  }

  /**
   * @return the equipHP
   */
  public long getEquipHP() {
    return equipHP;
  }

  /**
   * @param equipHP the equipHP to set
   */
  public void setEquipHP(long equipHP) {
    this.equipHP = equipHP;
  }

  /**
   * @return the equipDP
   */
  public long getEquipDP() {
    return equipDP;
  }

  /**
   * @param equipDP the equipDP to set
   */
  public void setEquipDP(long equipDP) {
    this.equipDP = equipDP;
  }

  /**
   * @return the equipFP
   */
  public long getEquipFP() {
    return equipFP;
  }

  /**
   * @param equipFP the equipFP to set
   */
  public void setEquipFP(long equipFP) {
    this.equipFP = equipFP;
  }

  /**
   * @return the equipSMP
   */
  public long getEquipSMP() {
    return equipSMP;
  }

  /**
   * @param equipSMP the equipSMP to set
   */
  public void setEquipSMP(long equipSMP) {
    this.equipSMP = equipSMP;
  }

  /**
   * @return the equipProgram
   */
  public String getEquipProgram() {
    return equipProgram;
  }

  /**
   * @param equipProgram the equipProgram to set
   */
  public void setEquipProgram(String equipProgram) {
    this.equipProgram = equipProgram;
  }

  /**
   * @return the removeProgram
   */
  public String getRemoveProgram() {
    return removeProgram;
  }

  /**
   * @param removeProgram the removeProgram to set
   */
  public void setRemoveProgram(String removeProgram) {
    this.removeProgram = removeProgram;
  }

  /**
   * @return the menuHP
   */
  public long getMenuHP() {
    return menuHP;
  }

  /**
   * @param menuHP the menuHP to set
   */
  public void setMenuHP(long menuHP) {
    this.menuHP = menuHP;
  }

  /**
   * @return the menuSMP
   */
  public long getMenuSMP() {
    return menuSMP;
  }

  /**
   * @param menuSMP the menuSMP to set
   */
  public void setMenuSMP(long menuSMP) {
    this.menuSMP = menuSMP;
  }

  /**
   * @return the menuProgram
   */
  public String getMenuProgram() {
    return menuProgram;
  }

  /**
   * @param menuProgram the menuProgram to set
   */
  public void setMenuProgram(String menuProgram) {
    this.menuProgram = menuProgram;
  }

  /**
   * @return the fightHP
   */
  public long getFightHP() {
    return fightHP;
  }

  /**
   * @param fightHP the fightHP to set
   */
  public void setFightHP(long fightHP) {
    this.fightHP = fightHP;
  }

  /**
   * @return the fightSMP
   */
  public long getFightSMP() {
    return fightSMP;
  }

  /**
   * @param fightSMP the fightSMP to set
   */
  public void setFightSMP(long fightSMP) {
    this.fightSMP = fightSMP;
  }

  /**
   * @return the fightProgram
   */
  public String getFightProgram() {
    return fightProgram;
  }

  /**
   * @param fightProgram the fightProgram to set
   */
  public void setFightProgram(String fightProgram) {
    this.fightProgram = fightProgram;
  }

  /**
   * @return the fightAnimation
   */
  public String getFightAnimation() {
    return fightAnimation;
  }

  /**
   * @param fightAnimation the fightAnimation to set
   */
  public void setFightAnimation(String fightAnimation) {
    this.fightAnimation = fightAnimation;
  }

  /**
   * @return the boardMultitaskProgram
   */
  public String getBoardMultitaskProgram() {
    return boardMultitaskProgram;
  }

  /**
   * @param boardMultitaskProgram the boardMultitaskProgram to set
   */
  public void setBoardMultitaskProgram(String boardMultitaskProgram) {
    this.boardMultitaskProgram = boardMultitaskProgram;
  }

  /**
   * @return the boardPickUpProgram
   */
  public String getBoardPickUpProgram() {
    return boardPickUpProgram;
  }

  /**
   * @param boardPickUpProgram the boardPickUpProgram to set
   */
  public void setBoardPickUpProgram(String boardPickUpProgram) {
    this.boardPickUpProgram = boardPickUpProgram;
  }

  /**
   * @return the isWide
   */
  public boolean isIsWide() {
    return isWide;
  }

  /**
   * @param isWide the isWide to set
   */
  public void setIsWide(boolean isWide) {
    this.isWide = isWide;
  }

  /**
   * @return the speed
   */
  public double getSpeed() {
    return speed;
  }

  /**
   * @param speed the speed to set
   */
  public void setSpeed(double speed) {
    this.speed = speed;
  }

}
