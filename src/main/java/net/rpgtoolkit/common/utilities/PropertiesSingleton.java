/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Joshua Michael Daly
 */
public class PropertiesSingleton {

  private static final PropertiesSingleton instance = new PropertiesSingleton();
  private final Properties properties = new Properties();
  
  private PropertiesSingleton() {
    try (InputStream in = PropertiesSingleton.class.
            getResourceAsStream("/core/properties/toolkit.properties")) {
      properties.load(in);
    }
    catch (IOException ex) {
      System.out.println(ex.toString());
    }
  }
  
  public static String getProperty(String key) {
    return instance.properties.getProperty(key);
  }
  
}
