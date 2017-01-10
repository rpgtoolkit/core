/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import net.rpgtoolkit.common.Selectable;

/**
 * A board sprite.
 * 
 * @author Joshua Michael Daly
 */
public class BoardSprite extends BasicType implements Cloneable, Selectable {

  private Item spriteFile; // Item filename
  private String fileName;

  private long x;
  private long y;
  private long layer;
  private long activate;

  private String initialVariable;
  private String initialValue;
  private String finalVariable;
  private String finalValue;
  private String loadingVariable;
  private String loadingValue;

  private long activationType; // Defines how the sprite is activated (step-on or key-press)

  private String activationProgram; // Override activation program
  private String multitaskingProgram; // Override multitask program

  private BoardVector boardPath; // TK3.10 relic - not used.

  private boolean selected;

  /**
   *
   */
  public BoardSprite() {
    super();

    x = 0;
    y = 0;
    layer = 0;
    activate = 0;
    initialVariable = "";
    initialValue = "";
    finalVariable = "";
    finalValue = "";
    loadingVariable = "";
    loadingValue = "";
    activationType = 0;
    activationProgram = "";
    multitaskingProgram = "";
    selected = false;
  }

  /**
   *
   * @return
   */
  public String getFileName() {
    return fileName;
  }
  
  /**
   *
   * @return
   */
  public long getX() {
    return x;
  }

  /**
   *
   * @return
   */
  public long getY() {
    return y;
  }

  /**
   *
   * @return
   */
  public int getWidth() {
    return 0; // return spriteFile.getAnimationFrame().getWidth();
  }

  /**
   *
   * @return
   */
  public int getHeight() {
    return 0; // return spriteFile.getAnimationFrame().getHeight();
  }

  /**
   *
   * @return
   */
  public long getLayer() {
    return layer;
  }

  /**
   *
   * @return
   */
  public long getActivate() {
    return activate;
  }

  /**
   *
   * @return
   */
  public Item getSpriteFile() {
    return spriteFile;
  }

  /**
   *
   * @return
   */
  public String getInitialVariable() {
    return initialVariable;
  }

  /**
   *
   * @return
   */
  public String getFinalVariable() {
    return finalVariable;
  }

  /**
   *
   * @return
   */
  public String getInitialValue() {
    return initialValue;
  }

  /**
   *
   * @return
   */
  public String getFinalValue() {
    return finalValue;
  }

  /**
   *
   * @return
   */
  public String getLoadingVariable() {
    return loadingVariable;
  }

  /**
   *
   * @return
   */
  public String getLoadingValue() {
    return loadingValue;
  }

  /**
   *
   * @return
   */
  public long getActivationType() {
    return activationType;
  }

  /**
   *
   * @return
   */
  public String getActivationProgram() {
    return activationProgram;
  }

  /**
   *
   * @return
   */
  public String getMultitaskingProgram() {
    return multitaskingProgram;
  }

  /**
   *
   * @return
   */
  public BoardVector getBoardPath() {
    return boardPath;
  }

  /**
   *
   * @param fileName
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;

    //System.out.println("Loading Item: " + fileName);
    //spriteFile = new Item(new File(System.getProperty("project.path")
    //        + "/Item/" + fileName));
    //spriteFile.loadAnimations();
    //spriteFile.setActiveAnimation(0); // SOUTH FACING
  }

  /**
   *
   * @param x
   */
  public void setX(long x) {
    this.x = x;
  }

  /**
   *
   * @param y
   */
  public void setY(long y) {
    this.y = y;
  }

  /**
   *
   * @param layer
   */
  public void setLayer(long layer) {
    this.layer = layer;
  }

  /**
   *
   * @param activate
   */
  public void setActivate(long activate) {
    this.activate = activate;
  }

  /**
   *
   * @param initialVariable
   */
  public void setInitialVariable(String initialVariable) {
    this.initialVariable = initialVariable;
  }

  /**
   *
   * @param finalVariable
   */
  public void setFinalVariable(String finalVariable) {
    this.finalVariable = finalVariable;
  }

  /**
   *
   * @param initialValue
   */
  public void setInitialValue(String initialValue) {
    this.initialValue = initialValue;
  }

  /**
   *
   * @param finalValue
   */
  public void setFinalValue(String finalValue) {
    this.finalValue = finalValue;
  }

  /**
   *
   * @param loadingVariable
   */
  public void setLoadingVariable(String loadingVariable) {
    this.loadingVariable = loadingVariable;
  }

  /**
   *
   * @param loadingValue
   */
  public void setLoadingValue(String loadingValue) {
    this.loadingValue = loadingValue;
  }

  /**
   *
   * @param activationType
   */
  public void setActivationType(long activationType) {
    this.activationType = activationType;
  }

  /**
   *
   * @param activationProgram
   */
  public void setActivationProgram(String activationProgram) {
    this.activationProgram = activationProgram;
  }

  /**
   *
   * @param multitaskingProgram
   */
  public void setMultitaskingProgram(String multitaskingProgram) {
    this.multitaskingProgram = multitaskingProgram;
  }

  /**
   *
   * @param boardPath
   */
  public void setBoardPath(BoardVector boardPath) {
    this.boardPath = boardPath;
  }

  @Override
  public boolean isSelected() {
    return selected;
  }

  @Override
  public void setSelectedState(boolean state) {
    selected = state;
  }

  /**
   * Directly clones the board sprite.
   * 
   * @return a clone
   * @throws CloneNotSupportedException
   */
  @Override
  public Object clone() throws CloneNotSupportedException {
    super.clone();

    BoardSprite clone = new BoardSprite();
    clone.activate = activate;
    clone.activationProgram = activationProgram;
    clone.activationType = activationType;
    clone.boardPath = (BoardVector) boardPath.clone();
    clone.finalValue = finalValue;
    clone.finalVariable = finalVariable;
    clone.initialValue = initialValue;
    clone.initialVariable = initialVariable;
    clone.layer = layer;
    clone.loadingValue = loadingValue;
    clone.loadingVariable = loadingVariable;
    clone.multitaskingProgram = multitaskingProgram;
    clone.spriteFile = spriteFile;
    clone.fileName = fileName;
    clone.x = x;
    clone.y = y;

    return clone;
  }

}
