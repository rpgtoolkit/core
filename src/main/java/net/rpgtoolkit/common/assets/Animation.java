/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import net.rpgtoolkit.common.assets.events.AnimationChangedEvent;
import net.rpgtoolkit.common.assets.listeners.AnimationChangeListener;

/**
 * This class is responsible for reading and writing RPG Toolkit 3.1 compatible Animation files.
 *
 * @author geoff wilson
 * @author Joshua Michael Daly
 * @version svn
 */
public class Animation extends AbstractAsset {

  private final LinkedList<AnimationChangeListener> animationChangeListeners = new LinkedList<>();

  // Constants
  private final String FILE_HEADER = "RPGTLKIT ANIM";
  private final int MAJOR_VERSION = 2;
  private final int MINOR_VERSION = 3;

  // Variables relvelant to animation
  private long animationWidth;
  private long animationHeight;
  
  private String soundEffect;
  
  private long frameCount;
  private ArrayList<AnimationFrame> frames;
  private double frameRate;

  public Animation(AssetDescriptor descriptor) {
    super(descriptor);
    init();
  }
  
  /**
   * Returns all of an Animation's frames.
   * 
   * @return 
   */
  public ArrayList<AnimationFrame> getFrames() {
    return frames;
  }

  /**
   * Returns a frame of animation based on the index specified
   *
   * @param index Frame to return
   * @return AnimationFrame object of the frame requested
   */
  public AnimationFrame getFrame(int index) {
    return frames.get(index);
  }

  /**
   * Set a frame of animation based on the index specified
   *
   * @param frame
   * @param index
   */
  public void setFrame(AnimationFrame frame, int index) {
    frames.set(index, frame);
    fireAnimationChanged();
  }

  /**
   * Gets the height (Y value) of the animation, this is necessary for both the editor and the
   * graphics uk.co.tkce.engine.
   *
   * @return Height value of the animation,
   */
  public long getAnimationHeight() {
    return animationHeight;
  }

  /**
   * Changes the height of the animation, it will attempt to preserve the existing data
   *
   * @param newHeight New height value for the animation.
   */
  public void setAnimationHeight(long newHeight) {
    animationHeight = newHeight;
    fireAnimationChanged();
  }

  /**
   * Gets the width (X value) of the animation, this is necessary for both the editor and the
   * graphics uk.co.tkce.engine.
   *
   * @return Width value of the animation,
   */
  public long getAnimationWidth() {
    return animationWidth;
  }

  /**
   * Changes the width of the animation, it will attempt to preserve the existing data
   *
   * @param newWidth New width value for the animation.
   */
  public void setAnimationWidth(long newWidth) {
    animationWidth = newWidth;
    fireAnimationChanged();
  }

  /**
   * 
   * @return 
   */
  public String getSoundEffect() {
    return soundEffect;
  }

  /**
   * 
   * @param soundEffect 
   */
  public void setSoundEffect(String soundEffect) {
    this.soundEffect = soundEffect;
    fireAnimationChanged();
  }
  
  /**
   * Gets the total number of frames in the animation
   *
   * @return Number representing the frame count of the Animation
   */
  public long getFrameCount() {
    return frames.size();
  }

  /**
   * Gets the Frame Delay (seconds between each frame) of the animation, this is required for the
   * graphics uk.co.tkce.engine to correctly configure animation timers.
   *
   * @return Frame delay value for the animation
   */
  public double getFrameRate() {
    return frameRate;
  }
  
  public void setFramRate(double rate) {
    frameRate = rate;
    fireAnimationChanged();
  }

  /**
   * Add a new <code>AnimationChangeListener</code> for this board.
   *
   * @param listener new change listener
   */
  public void addAnimationChangeListener(AnimationChangeListener listener) {
    animationChangeListeners.add(listener);
  }

  /**
   * Remove an existing <code>AnimationChangeListener</code> for this animation.
   *
   * @param listener change listener
   */
  public void removeAnimationChangeListener(AnimationChangeListener listener) {
    animationChangeListeners.remove(listener);
  }

  /**
   * Fires the <code>AnimationChangedEvent</code> informs all the listeners that this animation has
   * changed.
   */
  public void fireAnimationChanged() {
    AnimationChangedEvent event = null;
    Iterator iterator = animationChangeListeners.iterator();

    while (iterator.hasNext()) {
      if (event == null) {
        event = new AnimationChangedEvent(this);
      }

      ((AnimationChangeListener) iterator.next()).animationChanged(event);
    }
  }

  /**
   * Fires the <code>AnimationChangedEvent</code> informs all the listeners that this animation has
   * changed.
   */
  public void fireAnimationFrameAdded() {
    AnimationChangedEvent event = null;
    Iterator iterator = animationChangeListeners.iterator();

    while (iterator.hasNext()) {
      if (event == null) {
        event = new AnimationChangedEvent(this);
      }

      ((AnimationChangeListener) iterator.next()).animationFrameAdded(event);
    }
  }

  /**
   * Fires the <code>AnimationChangedEvent</code> informs all the listeners that this animation has
   * changed.
   */
  public void fireAnimationFrameRemoved() {
    AnimationChangedEvent event = null;
    Iterator iterator = animationChangeListeners.iterator();

    while (iterator.hasNext()) {
      if (event == null) {
        event = new AnimationChangedEvent(this);
      }

      ((AnimationChangeListener) iterator.next()).animationFrameRemoved(event);
    }
  }
  
  private void init() {
    animationWidth = 50;
    animationHeight = 50;
    
    soundEffect = "";
    
    frameCount = 0;
    frames = new ArrayList<>();
    frameRate = 0.5;
  }

  /**
   * Adds a new frame to the animation at the end of the current timeline
   *
   * @param newFrame AnimationFrame object to be added to the Animation
   */
  public void addFrame(AnimationFrame newFrame) {
    frames.add(newFrame);
    frameCount = frames.size();
    fireAnimationFrameAdded();
  }

  /**
   * Removes a frame from the Animation based on the index location specified.
   *
   * @param frameIndex Index of the frame to remove
   */
  public void removeFrame(int frameIndex) {
    frames.remove(frameIndex);
    frameCount = frames.size();
    fireAnimationFrameRemoved();
  }

}
