/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import net.rpgtoolkit.common.Selectable;

/**
 * A board program.
 * 
 * @author Joshua Michael Daly
 */
public class BoardProgram extends BasicType implements Cloneable, Selectable {

  private long layer;
  private String graphic;
  private String fileName;
  private long activate;
  private String initialVariable;
  private String finalVariable;
  private String initialValue;
  private String finalValue;
  private long activationType;

  private BoardVector vector;
  private long distanceRepeat;

  /**
   * Creates a new blank board program.
   */
  public BoardProgram() {
    super();
    vector = new BoardVector();
  }

  /**
   * Gets the associated layer.
   *  
   * @return layer index
   */
  public long getLayer() {
    return layer;
  }
  
  /**
   * Sets the layer index for this board program.
   * 
   * @param layer
   */
  public void setLayer(long layer) {
    this.layer = layer;
  }

  /**
   * Gets the associated graphic.
   * 
   * @return graphic name
   */
  public String getGraphic() {
    return graphic;
  }
  
  /**
   * Sets the graphic for this board program.
   * 
   * @param graphic
   */
  public void setGraphic(String graphic) {
    this.graphic = graphic;
  }

  /**
   * Gets the associated program filename.
   *
   * @return program filename
   */
  public String getFileName() {
    return fileName;
  }
  
  /**
   * Sets the filename for this board program.
   * 
   * @param fileName
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Gets the activate state.
   * 
   * @return state
   */
  public long getActivate() {
    return activate;
  }
  
  /**
   * Sets the activate state for this board program.
   * 
   * @param activate
   */
  public void setActivate(long activate) {
    this.activate = activate;
  }

  /**
   * Gets the initial variable name.
   * 
   * @return initial variable name
   */
  public String getInitialVariable() {
    return initialVariable;
  }
  
  /**
   * Sets the initial variable for this board program.
   * 
   * @param initialVariable
   */
  public void setInitialVariable(String initialVariable) {
    this.initialVariable = initialVariable;
  }

  /**
   * Gets the final variable name.
   * 
   * @return final variable name
   */
  public String getFinalVariable() {
    return finalVariable;
  }
  
  /**
   * Sets the final variable for this board program.
   * 
   * @param finalVariable
   */
  public void setFinalVariable(String finalVariable) {
    this.finalVariable = finalVariable;
  }

  /**
   * Gets the initial value.
   * 
   * @return initial value name
   */
  public String getInitialValue() {
    return initialValue;
  }
  
  /**
   * Sets the initial value for this board program.
   * 
   * @param initialValue
   */
  public void setInitialValue(String initialValue) {
    this.initialValue = initialValue;
  }

  /**
   * Gets the final value.
   * 
   * @return final value name
   */
  public String getFinalValue() {
    return finalValue;
  }
  
  /**
   * Sets the final value for this program.
   * 
   * @param finalValue
   */
  public void setFinalValue(String finalValue) {
    this.finalValue = finalValue;
  }

  /**
   * Gets the activation type.
   * 
   * @return activation type
   */
  public long getActivationType() {
    return activationType;
  }
  
  /**
   * Sets the activation type for this board program.
   * 
   * @param activationType
   */
  public void setActivationType(long activationType) {
    this.activationType = activationType;
  }

  /**
   * Gets the associated vector with this program.
   * 
   * @return board vector
   */
  public BoardVector getVector() {
    return vector;
  }
  
  /**
   * Sets the vector for this board program.
   * 
   * @param vector
   */
  public void setVector(BoardVector vector) {
    this.vector = vector;
  }

  /**
   * Gets the distance repeat for this program.
   * 
   * @return repeat over distance
   */
  public long getDistanceRepeat() {
    return distanceRepeat;
  }

  /**
   * Sets the distance repeat for this board program.
   * 
   * @param distanceRepeat
   */
  public void setDistanceRepeat(long distanceRepeat) {
    this.distanceRepeat = distanceRepeat;
  }

  /**
   * Is the board program selected in the editor.
   * 
   * @return 
   */
  @Override
  public boolean isSelected() {
    return this.vector.isSelected();
  }

  /**
   * Sets the selected state of this board program in the editor.
   * 
   * @param state 
   */
  @Override
  public void setSelectedState(boolean state) {
    this.vector.setSelectedState(state);
  }

  /**
   * Directly clones this board program.
   * 
   * @return a clone
   * @throws CloneNotSupportedException
   */
  @Override
  public Object clone() throws CloneNotSupportedException {
    super.clone();

    BoardProgram clone = new BoardProgram();
    clone.activate = this.activate;
    clone.activationType = this.activationType;
    clone.distanceRepeat = this.distanceRepeat;
    clone.fileName = this.fileName;
    clone.finalValue = this.finalValue;
    clone.finalVariable = this.finalVariable;
    clone.graphic = this.graphic;
    clone.initialValue = this.initialValue;
    clone.initialVariable = this.initialVariable;
    clone.layer = this.layer;
    clone.vector = (BoardVector) this.vector.clone();

    return clone;
  }
}
