/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.rpgtoolkit.common.utilities.PropertiesSingleton;

/**
 * This class stores the necessary data for a single frame, Animations are made up of an ArrayList
 * of these objects.
 *
 * @author Geoff Wilson
 * @author Joshua Michael Daly
 */
public class AnimationFrame {

  // This is not a TK3.1 file format so there is no header, or versions
  // Variables
  private String frameName;
  private Tile frameTile;
  private long transparentColour;
  private String frameSound;
  private BufferedImage image;
  private int imageType = 0; // temp 0 = tile, 1 = png

  /**
   * Creates a new frame based on the specified parameters.
   *
   * @param frameName String value for the name of the new frame.
   * @param transparentColour Number value representing the transparent color used in the animation.
   * @param frameSound File name of the sound effect that goes with this frame.
   */
  public AnimationFrame(String frameName, long transparentColour, String frameSound) {
    this.frameName = frameName;

    System.out.println("\t\tLoading Frame: " + frameName);
    // Check if we are using PNG or TST frames, will add more file formats in a future version.
    if ((frameName.toLowerCase().endsWith("png")) || (frameName.toLowerCase().endsWith("gif"))) {
      try {
        frameName = frameName.replace("\\", "/");
        FileInputStream fis = new FileInputStream(System.getProperty("project.path")
                + "/"
                + PropertiesSingleton.getProperty("toolkit.directory.bitmap")
                + "/"
                + frameName);
        image = ImageIO.read(fis);
        imageType = 1;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Set the transparent color and frame sound, these are not currently used.
    this.transparentColour = transparentColour;
    this.frameSound = frameSound;
  }

  public String getFrameName() {
    return this.frameName;
  }

  public void setFrameName(String frameName) {
    this.frameName = frameName;
  }

  public long getTransparentColour() {
    return this.transparentColour;
  }

  public void setTransparentColour(long tC) {
    this.transparentColour = tC;
  }

  public String getFrameSound() {
    return this.frameSound;
  }

  public void setFrameSound(String frameSound) {
    this.frameSound = frameSound;
  }

  public Tile getFrameTile() {
    return frameTile;
  }

  public BufferedImage getFrameImage() {
    if (imageType == 1) {
      return image;
    } else {
      return frameTile.getTileAsImage();
    }
  }
}
