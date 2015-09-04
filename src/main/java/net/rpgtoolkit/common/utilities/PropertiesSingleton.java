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

  private final PropertiesSingleton instance = new PropertiesSingleton();
  private static final Properties properties = new Properties();;
  
  private PropertiesSingleton() throws IOException {
    
    
    try (InputStream in = getClass().getClassLoader().getResourceAsStream("toolkit.properties")) {
      properties.load(in);
    }
  }
  
  public static String getProperty(String key) {
    return properties.getProperty(key);
  }
  
}
