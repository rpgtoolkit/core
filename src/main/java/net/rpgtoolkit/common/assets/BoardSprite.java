/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.rpgtoolkit.common.Selectable;
import net.rpgtoolkit.common.utilities.CoreProperties;

/**
 * A board sprite.
 * 
 * @author Joshua Michael Daly
 */
public class BoardSprite implements Cloneable, Selectable {

  private Item spriteFile; // Item filename
  private String fileName;

  private long x;
  private long y;
  private long layer;

  private EventType eventType; // Defines how the sprite is activated
  private String eventProgram; // Override activation program
  
  private String thread; // Override multitask program

  private boolean selected;
  
  private AnimationFrame southAnimationFrame;

  /**
   *
   */
  public BoardSprite() {
    super();

    x = 0;
    y = 0;
    layer = 0;
    eventType = EventType.OVERLAP;
    eventProgram = "";
    thread = "";
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
  public int getX() {
    return (int)x;
  }

  /**
   *
   * @return
   */
  public int getY() {
    return (int)y;
  }

  /**
   *
   * @return
   */
  public long getLayer() {
    return layer;
  }
  
  public int getWidth() {
    return southAnimationFrame.getFrameImage().getWidth();
  }
  
  public int getHeight() {
    return southAnimationFrame.getFrameImage().getHeight();
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
   * @param item 
   */
  public void setSpriteFile(Item item) {
      spriteFile = item;
  }

  /**
   *
   * @return
   */
  public EventType getEventType() {
    return eventType;
  }

  /**
   *
   * @return
   */
  public String getEventProgram() {
    return eventProgram;
  }

  /**
   *
   * @return
   */
  public String getThread() {
    return thread;
  }

  /**
   *
   * @param fileName
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
    
    // TODO: This is should not be in here!
    AnimationFrame frame = null;
    if (!fileName.isEmpty()) {
        File file = new File(
                System.getProperty("project.path")
                + File.separator
                + CoreProperties.getProperty("toolkit.directory.item"), 
                fileName);
        
        AssetHandle handle;
        try {
            handle = AssetManager.getInstance().deserialize(
                    new AssetDescriptor(file.toURI()));
            
            Item item = (Item) handle.getAsset();
            spriteFile = item;

            String southAnimation = item.animations.get(AnimationEnum.SOUTH.toString());
            if (!southAnimation.isEmpty()) {
                file = new File(
                        System.getProperty("project.path")
                        + File.separator
                        + CoreProperties.getProperty("toolkit.directory.misc"),
                        southAnimation);
                
                handle = AssetManager.getInstance().deserialize(
                new AssetDescriptor(file.toURI()));
                Animation animation = (Animation) handle.getAsset();

                if (animation != null) {
                    frame = animation.getFrame(0);
                }
            }
        } catch (IOException | AssetException ex) {
            Logger.getLogger(BoardSprite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    southAnimationFrame = frame;
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
   * @param eventType
   */
  public void setEventType(EventType eventType) {
    this.eventType = eventType;
  }

  /**
   *
   * @param activationProgram
   */
  public void setEventProgram(String activationProgram) {
    this.eventProgram = activationProgram;
  }

  /**
   *
   * @param multitaskingProgram
   */
  public void setThread(String multitaskingProgram) {
    this.thread = multitaskingProgram;
  }

  @Override
  public boolean isSelected() {
    return selected;
  }

  @Override
  public void setSelectedState(boolean state) {
    selected = state;
  }

  public AnimationFrame getSouthAnimationFrame() {
    return southAnimationFrame;
  }

  public void setSouthAnimationFrame(AnimationFrame southAnimationFrame) {
    this.southAnimationFrame = southAnimationFrame;
    
        if (spriteFile != null) {
            spriteFile.fireSpriteAnimationUpdated();
        }
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
    clone.eventProgram = eventProgram;
    clone.eventType = eventType;
    clone.layer = layer;
    clone.thread = thread;
    clone.spriteFile = spriteFile;
    clone.fileName = fileName;
    clone.x = x;
    clone.y = y;
    clone.southAnimationFrame = southAnimationFrame;

    return clone;
  }

}
