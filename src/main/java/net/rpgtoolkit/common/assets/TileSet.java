/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.util.LinkedList;

import net.rpgtoolkit.common.utilities.DOSColors;

/**
 * This class is responsible for managing a tilset inside the editor It stores all of the tiles in
 * the set in a big Array of tiles!
 *
 * @author Geoff Wilson
 * @author Joshua Michael Daly
 */
public class TileSet extends AbstractAsset {

  private String name;
  
  private final DOSColors dosColors = new DOSColors(); // Needed for low colour tilesets

  private LinkedList<Tile> tiles;

  private int tilesetVersion;
  private int numberOfTiles;
  private int tilesetType;

  private int tileWidth;
  private int tileHeight;
  private boolean rgbColor = false;
  private boolean hasAlpha = false;

  /*
   * *************************************************************************
   * Public Constructors
   * *************************************************************************
   */
  /**
   * Creates a new tiles
   * @param descriptor
   */
  public TileSet(AssetDescriptor descriptor) {
    super(descriptor);
    tiles = new LinkedList<>();
    tileWidth = 32;
    tileHeight = 32;
  }

  /*
   * *************************************************************************
   * Public Getters and Setters
   * *************************************************************************
   */
  /**
   * Gets a tile from a specified location in the array.
   *
   * @param index Index of the array to get the tile from
   * @return Tile object representing the tile from the requested index
   */
  public Tile getTile(int index) {
    return tiles.get(index);
  }

  public int getTileIndex(Tile tile) {
    return tiles.indexOf(tile);
  }

  /**
   * Returns an array of all the tiles in the tiles
   *
   * @return Object array of all the tiles in the tiles
   */
  public LinkedList<Tile> getTiles() {
    return tiles;
  }

  public int getTileCount() {
    return numberOfTiles;
  }

  public int getTileWidth() {
    return tileWidth;
  }

  public int getTileHeight() {
    return tileHeight;
  }

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public int getTilesetVersion() {
    return tilesetVersion;
  }

  public void setTilesetVersion(int tilesetVersion) {
    this.tilesetVersion = tilesetVersion;
  }

  public int getNumberOfTiles() {
    return numberOfTiles;
  }

  public void setNumberOfTiles(int numberOfTiles) {
    this.numberOfTiles = numberOfTiles;
  }

  public int getTilesetType() {
    return tilesetType;
  }

  public void setTilesetType(int tilesetType) {
    this.tilesetType = tilesetType;
  }

  public boolean isRgbColor() {
    return rgbColor;
  }

  public void setRgbColor(boolean rgbColor) {
    this.rgbColor = rgbColor;
  }

  public boolean isHasAlpha() {
    return hasAlpha;
  }

  public void setHasAlpha(boolean hasAlpha) {
    this.hasAlpha = hasAlpha;
  }

  public void setTiles(LinkedList<Tile> tiles) {
    this.tiles = tiles;
  }

  public void setTileWidth(int tileWidth) {
    this.tileWidth = tileWidth;
  }

  public void setTileHeight(int tileHeight) {
    this.tileHeight = tileHeight;
  }

  public DOSColors getDosColors() {
    return dosColors;
  }

  /*
   * *************************************************************************
   * Public Methods
   * *************************************************************************
   */
  /**
   * Adds a new tile to the tiles, it will add the tile at the end of the array
   *
   * @param newTile Tile object to add to the array
   */
  public void addTile(Tile newTile) {
    tiles.add(newTile);
    numberOfTiles++; // Increment tile count
  }
  
}
