/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joshua Michael Daly
 */
public class CoreProperties {

  private static final CoreProperties INSTANCE = new CoreProperties();
  private final Properties properties = new Properties();
  private String[] directories;
  
  private CoreProperties() {
    try (InputStream in = CoreProperties.class.
            getResourceAsStream("/core/properties/toolkit.properties")) {
      properties.load(in);
      
      directories = new String[] {
      properties.getProperty("toolkit.directory.bitmap"),
      properties.getProperty("toolkit.directory.background"),
      properties.getProperty("toolkit.directory.board"),
      properties.getProperty("toolkit.directory.character"),
      properties.getProperty("toolkit.directory.enemy"),
      properties.getProperty("toolkit.directory.font"),
      properties.getProperty("toolkit.directory.item"),
      properties.getProperty("toolkit.directory.media"),
      properties.getProperty("toolkit.directory.misc"),
      properties.getProperty("toolkit.directory.plugin"),
      properties.getProperty("toolkit.directory.program"),
      properties.getProperty("toolkit.directory.specialmove"),
      properties.getProperty("toolkit.directory.statuseffect"),
      properties.getProperty("toolkit.directory.tileset")};
    }
    catch (IOException ex) {
      Logger.getLogger(CoreProperties.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public static String getProperty(String key) {
    return INSTANCE.properties.getProperty(key);
  }
  
  public static String getProjectsDirectory() {
    return System.getProperty("user.home") + File.separator + 
            INSTANCE.properties.getProperty("toolkit.directory.projects");
  }
  
  public static String[] getDirectories() {
    return INSTANCE.directories;
  }
  
}
