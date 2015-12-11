/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.utilities;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import net.rpgtoolkit.common.assets.TileSet;

/**
 * Stores a cache of loaded TileSets for reuse between boards.
 *
 * @author Geoff Wilson
 * @author Joshua Michael Daly
 */
public class TileSetCache {

  // Singleton.
  private static final TileSetCache instance = new TileSetCache();

  private final HashMap<String, TileSet> tileSets;

  private TileSetCache() {
    tileSets = new HashMap<>();
  }

  /**
   *
   * @return
   */
  public static TileSetCache getInstance() {
    return instance;
  }

  /**
   * Gets the tile set with the specified key, if it is present in the cache
   *
   * @param key Filename of the tile set to retrieve
   * @return the Tile set with the corresponding filename
   */
  public TileSet getTileSet(String key) {
    if (tileSets.containsKey(key)) {
      return tileSets.get(key);
    } else {
      return null;
    }
  }

  /**
   * Allows the calling object to check if the tile set has already been loaded, this should be
   * checked before calling loadTileSet
   *
   * @param key Tile Set file to check for
   * @return true if the tile set is already present in the cache
   */
  public boolean contains(String key) {
    return tileSets.containsKey(key);
  }

  /**
   * Adds the specified tile set into the cache, it will only load the file if it is not already
   * present in the cache, it is important to call contains(String key) before calling this method.
   *
   * @param fileName Tile set to attempt to load into the cache
   * @return The loaded tile set is returned, this is to remove the need to call getTileSet(String
   * key) straight after loading a set
   */
  public TileSet addTileSet(String fileName) {
    TileSet set;

    if (!tileSets.containsKey(fileName)) {
      set = new TileSet(new File(System.getProperty("project.path")
              + "/"
              + PropertiesSingleton.getProperty("toolkit.directory.tileset")
              + "/" + fileName));
      tileSets.put(fileName, set);

      return set;
    } else {
      set = getTileSet(fileName);

      return set;
    }
  }

  /**
   * Removes the specified TileSet from the cache, it will only remove the TileSet if the number of
   * board references have reached 0.
   *
   * @param fileName TileSet to attempt to load into the cache
   * @return the remove TileSet is returned.
   */
  public TileSet removeTileSet(String fileName) {
    if (tileSets.containsKey(fileName)) {
      TileSet set = tileSets.get(fileName);

      return set;
    }

    return null;
  }

  /**
   * Removes the specified TileSets from the cache, it will only remove the TileSet if the number of
   * board references have reached 0.
   *
   * @param fileNames list of TileSet file names to remove
   * @return the removed TileSets
   */
  public LinkedList<TileSet> removeTileSets(LinkedList<String> fileNames) {
    LinkedList<TileSet> removedSets = new LinkedList<>();

    for (String fileName : fileNames) {
      removedSets.add(removeTileSet(fileName));
    }

    return removedSets;
  }

}
