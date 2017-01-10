/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import net.rpgtoolkit.common.assets.events.SpriteChangedEvent;
import net.rpgtoolkit.common.assets.listeners.SpriteChangeListener;

/**
 * A common abstract class for all sprite like assets to inherit (Player/Character, Enemy, Item).
 * 
 * @author Joshua Michael Daly
 */
public abstract class AbstractSprite extends AbstractAsset {

  // Non-IO
  protected final LinkedList<SpriteChangeListener> spriteChangeListeners = new LinkedList<>();
  
  protected String name;

  // Graphics Variables
  protected ArrayList<String> standardGraphics; // 13 Values, S,N,E,W,NW,NE,SW,SE,Att,Def,Spec,Die,Rst
  protected ArrayList<Animation> standardGraphicsAnimations;

  protected ArrayList<String> customGraphics;
  protected ArrayList<String> customGraphicsNames;
  protected ArrayList<String> standingGraphics;

  protected double idleTimeBeforeStanding;
  protected double frameRate; //Seconds between each step
  protected int loopSpeed;

  protected BoardVector baseVector;
  protected BoardVector activationVector;

  protected Point baseVectorOffset;
  protected Point activationVectorOffset;

  public AbstractSprite(AssetDescriptor descriptor) {
    super(descriptor);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the standardGraphics
   */
  public ArrayList<String> getStandardGraphics() {
    return standardGraphics;
  }

  /**
   *
   * @param standardGraphics
   */
  public void setStandardGraphics(ArrayList<String> standardGraphics) {
    this.standardGraphics = standardGraphics;
  }

  /**
   * @return the standardGraphicsAnimations
   */
  public ArrayList<Animation> getStandardGraphicsAnimations() {
    return standardGraphicsAnimations;
  }

  /**
   * @return the customGraphics
   */
  public ArrayList<String> getCustomGraphics() {
    return customGraphics;
  }

  /**
   *
   * @param customGraphics
   */
  public void setCustomGraphics(ArrayList<String> customGraphics) {
    this.customGraphics = customGraphics;
  }

  /**
   * @return the customGraphicNames
   */
  public ArrayList<String> getCustomGraphicsNames() {
    return customGraphicsNames;
  }

  /**
   *
   * @param customGraphicNames
   */
  public void setCustomGraphicNames(ArrayList<String> customGraphicNames) {
    this.customGraphicsNames = customGraphicNames;
  }

  /**
   * @return the standingGraphics
   */
  public ArrayList<String> getStandingGraphics() {
    return standingGraphics;
  }

  /**
   *
   * @param standingGraphics
   */
  public void setStandingGraphics(ArrayList<String> standingGraphics) {
    this.standingGraphics = standingGraphics;
  }

  /**
   * @return the idleTimeBeforeStanding
   */
  public double getIdleTimeBeforeStanding() {
    return idleTimeBeforeStanding;
  }

  /**
   * @param idleTimeBeforeStanding the idleTimeBeforeStanding to set
   */
  public void setIdleTimeBeforeStanding(double idleTimeBeforeStanding) {
    this.idleTimeBeforeStanding = idleTimeBeforeStanding;
  }

  /**
   * @return the frameRate (seconds between each step)
   */
  public double getFrameRate() {
    return frameRate;
  }

  /**
   * @param frameRate the frameRate to set (seconds between each step)
   */
  public void setFrameRate(double frameRate) {
    this.frameRate = frameRate;
  }

  public BoardVector getBaseVector() {
    return baseVector;
  }

  /**
   * @param baseVector the baseVector to set
   */
  public void setBaseVector(BoardVector baseVector) {
    this.baseVector = baseVector;
    fireSpriteChanged();
  }

  public BoardVector getActivationVector() {
    return activationVector;
  }

  /**
   * @param activationVector the activationVector to set
   */
  public void setActivationVector(BoardVector activationVector) {
    this.activationVector = activationVector;
    fireSpriteChanged();
  }

  public Point getBaseVectorOffset() {
    return baseVectorOffset;
  }

  public void setBaseVectorOffset(Point baseVectorOffset) {
    this.baseVectorOffset = baseVectorOffset;
    fireSpriteChanged();
  }

  public Point getActivationVectorOffset() {
    return activationVectorOffset;
  }

  public void setActivationVectorOffset(Point activationVectorOffset) {
    this.activationVectorOffset = activationVectorOffset;
    fireSpriteChanged();
  }

  /**
   * Add a new <code>SpriteChangeListener</code> for this sprite.
   *
   * @param listener new change listener
   */
  public void addSpriteChangeListener(SpriteChangeListener listener) {
    spriteChangeListeners.add(listener);
  }

  /**
   * Remove an existing <code>PlayerChangeListener</code> for this player.
   *
   * @param listener change listener
   */
  public void removeSpriteChangeListener(SpriteChangeListener listener) {
    spriteChangeListeners.remove(listener);
  }

  public void updateStandardGraphics(int index, String path) {
    standardGraphics.set(index, path);
    fireSpriteAnimationUpdated();
  }

  public void updateStandingGraphics(int index, String path) {
    standingGraphics.set(index, path);
    fireSpriteAnimationUpdated();
  }

  public void addCustomGraphics(String path) {
    customGraphics.add("");
    fireSpriteAnimationAdded();
  }

  public void updateCustomGraphics(int index, String path) {
    customGraphics.set(index, path);
    fireSpriteAnimationUpdated();
  }

  public void removeCustomGraphics(int index) {
    customGraphics.remove(index);
    fireSpriteAnimationRemoved();
  }

  /**
   * Fires the <code>SpriteChangedEvent</code> informs all the listeners that this sprite has
   * changed.
   */
  public void fireSpriteChanged() {
    SpriteChangedEvent event = null;
    Iterator iterator = spriteChangeListeners.iterator();

    while (iterator.hasNext()) {
      if (event == null) {
        event = new SpriteChangedEvent(this);
      }

      ((SpriteChangeListener) iterator.next()).spriteChanged(event);
    }
  }

  /**
   * Fires the <code>SpriteChangedEvent</code> informs all the listeners that this sprite has had an
   * animation added.
   */
  public void fireSpriteAnimationAdded() {
    SpriteChangedEvent event = null;
    Iterator iterator = spriteChangeListeners.iterator();

    while (iterator.hasNext()) {
      if (event == null) {
        event = new SpriteChangedEvent(this);
      }

      ((SpriteChangeListener) iterator.next()).spriteAnimationAdded(event);
    }
  }

  /**
   * Fires the <code>SpriteChangedEvent</code> informs all the listeners that this sprite has had an
   * animation updated.
   */
  public void fireSpriteAnimationUpdated() {
    SpriteChangedEvent event = null;
    Iterator iterator = spriteChangeListeners.iterator();

    while (iterator.hasNext()) {
      if (event == null) {
        event = new SpriteChangedEvent(this);
      }

      ((SpriteChangeListener) iterator.next()).spriteAnimationUpdated(event);
    }
  }

  /**
   * Fires the <code>SpriteChangedEvent</code> informs all the listeners that this sprite has had an
   * animation removed.
   */
  public void fireSpriteAnimationRemoved() {
    SpriteChangedEvent event = null;
    Iterator iterator = spriteChangeListeners.iterator();

    while (iterator.hasNext()) {
      if (event == null) {
        event = new SpriteChangedEvent(this);
      }

      ((SpriteChangeListener) iterator.next()).spriteAnimationRemoved(event);
    }
  }

}
