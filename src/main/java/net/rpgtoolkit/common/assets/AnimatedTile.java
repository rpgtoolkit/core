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
import net.rpgtoolkit.common.CorruptAssetException;
import net.rpgtoolkit.common.utilities.BinaryIO;

import java.util.ArrayList;

public class AnimatedTile extends BasicType {

  // Constants
  private final String FILE_HEADER = "RPGTLKIT TILEANIM";
  private final int MAJOR_VERSION = 2;
  private final int MINOR_VERSION = 0;

  // Animated Tile Variables
  private long frameCount;
  private ArrayList<String> frames;
  private long framesPerSecond;

  /**
   * Create a <code>AnimatedTile</code> from an existing file.
   *
   * @param fileName file to open
   */
  public AnimatedTile(File fileName) {
    super(fileName);
    open();
  }

  /**
   * Get the first frame.
   *
   * @return first frame.
   */
  public String getFirstFrame() {
    return frames.get(0);
  }

  /**
   * Attempt to open the <code>AnimatedTile</code>.
   *
   * @return true for success, false for failure
   */
  public final boolean open() {
    try {
      inputStream = new FileInputStream(file);
      binaryIO = new BinaryIO(inputStream);
      frames = new ArrayList<>();

      if (binaryIO.readBinaryString().equals(FILE_HEADER)) {
        int majorVersion = binaryIO.readBinaryInteger();
        binaryIO.readBinaryInteger();

        if (majorVersion == MAJOR_VERSION) {
          framesPerSecond = binaryIO.readBinaryLong();
          frameCount = binaryIO.readBinaryLong();
          for (int i = 0; i < frameCount; i++) {
            frames.add(binaryIO.readBinaryString());
          }
        }
      }

      inputStream.close();

      return true;
    } catch (FileNotFoundException e) {
      System.out.println(e.toString());
      return false;
    } catch (CorruptAssetException | IOException e) {
      System.out.println(e.toString());
      return false;
    }
  }

  public boolean save() {
    try {
      outputStream = new FileOutputStream(file);
      binaryIO.setOutputStream(outputStream);

      binaryIO.writeBinaryString(FILE_HEADER);
      binaryIO.writeBinaryInteger(MAJOR_VERSION);
      binaryIO.writeBinaryInteger(MINOR_VERSION);
      binaryIO.writeBinaryLong(framesPerSecond);
      binaryIO.writeBinaryLong(frames.size() - 1);

      for (String string : frames) {
        binaryIO.writeBinaryString(string);
      }

      outputStream.close();
      return true;
    } catch (FileNotFoundException e) {
      System.out.println(e.toString());
      return false;
    } catch (IOException e) {
      System.out.println(e.toString());
      return false;
    }
  }

  public boolean saveAs(File fileName) {
    file = fileName;
    return this.save();
  }
}
