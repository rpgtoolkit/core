/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import net.rpgtoolkit.common.utilities.BinaryIO;
import net.rpgtoolkit.common.CorruptAssetException;
import net.rpgtoolkit.common.assets.events.AnimationChangedEvent;
import net.rpgtoolkit.common.assets.listeners.AnimationChangeListener;

/**
 * This class is responsible for reading and writing RPG Toolkit 3.1 compatible Animation files.
 *
 * @author geoff wilson
 * @version svn
 */
public class Animation extends BasicType {

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

  public Animation() {
    animationWidth = 50;
    animationHeight = 50;
    
    soundEffect = "";
    
    frameCount = 0;
    frames = new ArrayList<>();
    frameRate = 0.5;
  }
  
  /**
   * Opens an exsiting Aniamtion based on given input file
   *
   * @param fileName File object of the animation to open
   */
  public Animation(File fileName) {
    super(fileName);
    System.out.println("\tLoading Animation: " + fileName);
    this.openBinary();
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

  /**
   * Opens the Animation, this method is only called from the constructor and should not be made
   * public
   *
   * @return
   * @deprecated 
   */
  private boolean openBinary() {
    try {
      // Configure the IO
      inputStream = new FileInputStream(this.file);
      binaryIO = new BinaryIO(inputStream);

      // Initialize the Array List of frames.
      frames = new ArrayList<>();

      // Check the header
      if (binaryIO.readBinaryString().equals(FILE_HEADER)) {
        int majorVersion = binaryIO.readBinaryInteger(); // not used
        int minorVersion = binaryIO.readBinaryInteger();
        if (minorVersion == 3) {
          animationWidth = binaryIO.readBinaryLong();
          animationHeight = binaryIO.readBinaryLong();

          // How many frames in this animation?
          frameCount = binaryIO.readBinaryLong();
          for (int i = 0; i < frameCount + 1; i++) // get each frame
          {
            String frameName = binaryIO.readBinaryString();
            long transparentColour = binaryIO.readBinaryLong(); // not used currently
            String frameSound = binaryIO.readBinaryString();

            // Do not add blank frames to the animation
            if (!frameName.equals("")) {
              AnimationFrame frame = new AnimationFrame(frameName, transparentColour, frameSound);
              frames.add(frame);
            }
          }

          frameRate = binaryIO.readBinaryDouble(); // Seconds per Frame
        } else {
          throw new CorruptAssetException("Animation data is corrupt");
        }
      } else {
        throw new CorruptAssetException(this.file.getName() + " is not an animation file");
      }

      inputStream.close(); // Close IO

      return true;
    } // Need to implement better error handling
    catch (FileNotFoundException e) {
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
   * Saves the animation to its existing file.
   *
   * @return true for successful save, false if save failed.
   * @deprecated 
   */
  public boolean saveBinary() {
    try {
      // Configure IO
      outputStream = new FileOutputStream(this.file);
      
      if (binaryIO == null) {
        inputStream = new FileInputStream(this.file);
        binaryIO = new BinaryIO(inputStream);
      }
      
      binaryIO.setOutputStream(outputStream);

      // For TK3.1 compatibility we do not change versions or headers.
      binaryIO.writeBinaryString(FILE_HEADER);
      binaryIO.writeBinaryInteger(MAJOR_VERSION);
      binaryIO.writeBinaryInteger(MINOR_VERSION);

      binaryIO.writeBinaryLong(animationWidth);
      binaryIO.writeBinaryLong(animationHeight);

      binaryIO.writeBinaryLong(frames.size() - 1); // store the correct frame count
      for (AnimationFrame frame : frames) {
        binaryIO.writeBinaryString(frame.getFrameName());
        binaryIO.writeBinaryLong(frame.getTransparentColour());
        binaryIO.writeBinaryString(frame.getFrameSound());
      }

      binaryIO.writeBinaryDouble(frameRate);

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

  /**
   * Saves the Animation to a new file
   *
   * @param fileName File object of the new Animation file
   * @return true for successful save, false if save failed
   */
  public boolean saveAs(File fileName) {
    this.file = fileName;
    return this.saveBinary();
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
