/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.rpgtoolkit.common.utilities.TileSetCache;
import net.rpgtoolkit.common.CorruptAssetException;
import net.rpgtoolkit.common.Selectable;
import net.rpgtoolkit.common.utilities.BinaryIO;
import net.rpgtoolkit.common.utilities.PropertiesSingleton;

/**
 * A model that represents <code>Board</code> files in the RPGToolkit engine and editor. Used during
 * the serialization processes to various formats, at the moment it contains the old binary routines
 * for opening 3.x formats which should be removed in 4.1.
 *
 * @author Geoff Wilson
 * @author Joshua Michael Daly
 */
public final class Board extends BasicType implements Asset, Selectable {

  // Non-IO
  private final LinkedList<BoardChangeListener> boardChangeListeners = new LinkedList<>();
  private final LinkedList<BoardLayer> layers = new LinkedList<>();
  private LinkedList<String> tileSetNames = new LinkedList<>();
  private boolean selectedState;

  // Constants
  public static final String FILE_HEADER = "RPGTLKIT BOARD";
  public static final int MAJOR_VERSION = 2;
  public static final int MINOR_VERSION = 4;
  public static final int STANDARD = 1;
  public static final int ISO_STACKED = 2;
  public static final int ISO_ROTATED = 6;

  // Variables
  private int width;
  private int height;
  private int layerCount;
  private int coordinateType;
  private ArrayList<String> tileNameIndex;      // Contains string names e.g. default.tst1
  private ArrayList<Tile> loadedTilesIndex;     // Contains tile objects of e.g. default.tst1
  private int[][][] boardDimensions;            // x, y, z
  private long shadingLayer;
  private ArrayList<BoardLayerShade> tileShading;
  private ArrayList<BoardImage> boardImages;
  private ArrayList<BoardImage> spriteImages;
  private ArrayList<BoardImage> backgroundImages;
  private long backgroundColour;
  private ArrayList<BoardProgram> programs;
  private ArrayList<BoardLight> lights;
  private ArrayList<BoardVector> vectors;
  private ArrayList<BoardSprite> sprites;
  private ArrayList<String> threads;
  private ArrayList<String> constants;
  private ArrayList<String> layerTitles;
  private ArrayList<String> directionalLinks;
  private String backgroundMusic;
  private String firstRunProgram;
  private String battleBackground;
  private int enemyBattleLevel;
  private boolean allowBattles;
  private boolean allowSaving;
  private Color ambientEffect;
  private int startingPositionX;
  private int startingPositionY;
  private int startingLayer;
  private String lastLoadedTileSet;
  private HashMap<String, TileSet> tileSets;

  // TODO: Remove these attributes from 4.1.
  private int ubShading;
  private byte randomByte;
  private int associatedVector;
  private int imageTransluceny;

  /**
   * Creates an empty board.
   */
  public Board() {

  }

  /**
   * Opens the specified board.
   *
   * @param file board file to open
   * @throws java.io.FileNotFoundException
   */
  public Board(File file) throws FileNotFoundException {
    super(file);
    reset();

    System.out.println("Loading Board " + file);
  }

  /**
   * Creates a new board with the specified width and height.
   *
   * @param width board width
   * @param height board height
   */
  public Board(int width, int height) {
    reset();
    this.width = width;
    this.height = height;
  }

  /**
   * Gets this boards layers.
   *
   * @return the board layers
   */
  public LinkedList<BoardLayer> getLayers() {
    return layers;
  }

  /**
   * Gets a tile used by this board based on its index.
   *
   * @param index
   * @return tile at index
   */
  public Tile getTileFromIndex(int index) {
    return loadedTilesIndex.get(index);
  }

  /**
   * Gets the width of this board.
   *
   * @return width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Sets the width of this board.
   *
   * @param width new width
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * Gets the height of this board.
   *
   * @return height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Sets the height of this board.
   *
   * @param height new height
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Gets an index list of all the tiles used by this board.
   *
   * @return index list of tiles
   */
  public ArrayList<String> getTileNameIndex() {
    return tileNameIndex;
  }

  /**
   * Sets the index list for the tiles used on this board.
   *
   * @param tileIndex new index list of tiles
   */
  public void getTileNameIndex(ArrayList<String> tileIndex) {
    tileNameIndex = tileIndex;
  }

  /**
   * Gets the background images used on this board.
   *
   * @return background images on board
   */
  public ArrayList<BoardImage> getBackgroundImages() {
    return backgroundImages;
  }

  /**
   * Sets the background images used on this board.
   *
   * @param backgroundImages new background images
   */
  public void setBackgroundImages(ArrayList<BoardImage> backgroundImages) {
    this.backgroundImages = backgroundImages;
  }

  /**
   * Gets the board programs used on this used board.
   *
   * @return board programs on board
   */
  public ArrayList<BoardProgram> getPrograms() {
    return programs;
  }

  /**
   * Sets the board programs used on this board.
   *
   * @param programs new board programs
   */
  public void setPrograms(ArrayList<BoardProgram> programs) {
    this.programs = programs;
  }

  /**
   * Gets the vectors used on this board.
   *
   * @return vectors on board
   */
  public ArrayList<BoardVector> getVectors() {
    return vectors;
  }

  /**
   * Sets the vectors used on this board.
   *
   * @param vectors new vectors
   */
  public void setVectors(ArrayList<BoardVector> vectors) {
    this.vectors = vectors;
  }

  /**
   * Gets the sprites used on this board.
   *
   * @return sprites on board
   */
  public ArrayList<BoardSprite> getSprites() {
    return sprites;
  }

  /**
   * Sets the sprites used on this board.
   *
   * @param sprites new sprites
   */
  public void setSprites(ArrayList<BoardSprite> sprites) {
    this.sprites = sprites;
  }

  /**
   * Gets the players starting x position.
   *
   * @return starting x position
   */
  public int getStartingPositionX() {
    return startingPositionX;
  }

  /**
   * Sets the players starting x position.
   *
   * @param startingPositionX new starting x position
   */
  public void setStartingPositionX(int startingPositionX) {
    this.startingPositionX = startingPositionX;
  }

  /**
   * Gets the players starting y position.
   *
   * @return starting y position
   */
  public int getStartingPositionY() {
    return startingPositionY;
  }

  /**
   * Sets the players starting y position.
   *
   * @param startingPositionY new starting y position
   */
  public void setStartingPositionY(int startingPositionY) {
    this.startingPositionY = startingPositionY;
  }

  /**
   * Gets the players starting layer index.
   *
   * @return starting layer index.
   */
  public int getStartingLayer() {
    return startingLayer;
  }

  /**
   * Sets the players starting layer index.
   *
   * @param startingLayer new starting layer index
   */
  public void setStartingLayer(int startingLayer) {
    this.startingLayer = startingLayer;
  }

  /**
   * Gets the tile index at the specified location.
   *
   * @param x x location
   * @param y y location
   * @param z z layer index
   * @return tile index
   */
  public int getIndexAtLocation(int x, int y, int z) {
    return layers.get(z).getTiles()[x][y].getIndex();
  }

  /**
   * Gets the layer count for this board.
   *
   * @return layer count
   */
  public int getLayerCount() {
    return layerCount;
  }

  /**
   * Sets the layer counter for this board.
   *
   * @param count new layer count
   */
  public void setLayerCount(int count) {
    layerCount = count;
  }

  /**
   * Gets the board coordinate type.
   *
   * @return current coordinate type
   */
  public int getCoordinateType() {
    return coordinateType;
  }

  /**
   * Sets the board coordinate type.
   *
   * @param coordinateType new coordinate type
   */
  public void setCoordinateType(int coordinateType) {
    this.coordinateType = coordinateType;
  }

  /**
   * Gets the loaded tiles index, the list of all the loaded tiles.
   *
   * @return loaded tiles for this board
   */
  public ArrayList<Tile> getLoadedTilesIndex() {
    return loadedTilesIndex;
  }

  /**
   * Sets the loaded tiles index, the list of all the loaded tiles.
   *
   * @param loadedTilesIndex new loaded tiles for this board
   */
  public void setLoadedTilesIndex(ArrayList<Tile> loadedTilesIndex) {
    this.loadedTilesIndex = loadedTilesIndex;
  }

  /**
   * Gets the board dimensions.
   *
   * @return x, y, z, where z is the number of layers
   */
  public int[][][] getBoardDimensions() {
    return boardDimensions;
  }

  /**
   * Sets the board dimensions.
   *
   * @param boardDimensions new board dimensions x, y, z
   */
  public void setBoardDimensions(int[][][] boardDimensions) {
    this.boardDimensions = boardDimensions;
  }

  /**
   * Gets the boards tile shading as a list of <code>BoardLayerShade</code>.
   *
   * @return tile shades
   */
  public ArrayList<BoardLayerShade> getTileShading() {
    return tileShading;
  }

  /**
   * Sets the boards tile shading as a list of <code>BoardLayerShade</code>.
   *
   * @param tileShading new tile shades
   */
  public void setTileShading(ArrayList<BoardLayerShade> tileShading) {
    this.tileShading = tileShading;
  }

  /**
   * Gets the board images.
   *
   * @return board images
   */
  public ArrayList<BoardImage> getImages() {
    return boardImages;
  }

  /**
   * Sets the board images.
   *
   * @param images new board images
   */
  public void setImages(ArrayList<BoardImage> images) {
    boardImages = images;
  }

  /**
   * Gets the sprite images.
   *
   * @return sprite images
   */
  public ArrayList<BoardImage> getSpriteImages() {
    return spriteImages;
  }

  /**
   * Sets the sprite images.
   *
   * @param spriteImages new sprite images.
   */
  public void setSpriteImages(ArrayList<BoardImage> spriteImages) {
    this.spriteImages = spriteImages;
  }

  /**
   * Gets the background colour.
   *
   * @return background colour
   */
  public long getBackgroundColour() {
    return backgroundColour;
  }

  /**
   * Sets the background colour.
   *
   * @param backgroundColour new background colour
   */
  public void setBackgroundColour(long backgroundColour) {
    this.backgroundColour = backgroundColour;
  }

  /**
   * Gets the board lights.
   *
   * @return board lights
   */
  public ArrayList<BoardLight> getLights() {
    return lights;
  }

  /**
   * Sets the board lights.
   *
   * @param lights new board lights
   */
  public void setLights(ArrayList<BoardLight> lights) {
    this.lights = lights;
  }

  /**
   * Gets the board thread names.
   *
   * @return board thread names
   */
  public ArrayList<String> getThreads() {
    return threads;
  }

  /**
   * Sets the board thread names.
   *
   * @param threads new board thread names
   */
  public void setThreads(ArrayList<String> threads) {
    this.threads = threads;
  }

  /**
   * Gets the board constants.
   *
   * @return board constants
   */
  public ArrayList<String> getConstants() {
    return constants;
  }

  /**
   * Sets the board constants.
   *
   * @param constants new constants
   */
  public void setConstants(ArrayList<String> constants) {
    this.constants = constants;
  }

  /**
   * Gets the board layer titles.
   *
   * @return board layer titles
   */
  public ArrayList<String> getLayerTitles() {
    return layerTitles;
  }

  /**
   * Sets the board layer titles.
   *
   * @param layerTitles new board layer titles
   */
  public void setLayerTitles(ArrayList<String> layerTitles) {
    this.layerTitles = layerTitles;
  }

  /**
   * Gets the layer title by index.
   *
   * @param index layer index
   * @return layer title
   */
  public String getLayerTitle(int index) {
    return layerTitles.get(index);
  }

  /**
   * Sets the layer title by index.
   *
   * @param index layer index
   * @param title new layer title
   */
  public void setLayerTitle(int index, String title) {
    layerTitles.set(index, title);
    fireBoardChanged();
  }

  /**
   * Gets the board directional links e.g. N, S, E, W.
   *
   * @return directional links
   */
  public ArrayList<String> getDirectionalLinks() {
    return directionalLinks;
  }

  /**
   * Sets the board directional links e.g. N, S, E, W.
   *
   * @param directionalLinks new directional links
   */
  public void setDirectionalLinks(ArrayList<String> directionalLinks) {
    this.directionalLinks = directionalLinks;
  }

  /**
   * Gets the board background music.
   *
   * @return background music filename
   */
  public String getBackgroundMusic() {
    return backgroundMusic;
  }

  /**
   * Sets the board background music.
   *
   * @param backgroundMusic new background music filename
   */
  public void setBackgroundMusic(String backgroundMusic) {
    this.backgroundMusic = backgroundMusic;
  }

  /**
   * Gets the board first run program.
   *
   * @return first run program on enter
   */
  public String getFirstRunProgram() {
    return firstRunProgram;
  }

  /**
   * Sets the board first run program.
   *
   * @param firstRunProgram new first run program on enter
   */
  public void setFirstRunProgram(String firstRunProgram) {
    this.firstRunProgram = firstRunProgram;
  }

  /**
   * Gets the board battle background.
   *
   * @return battle background
   */
  public String getBattleBackground() {
    return battleBackground;
  }

  /**
   * Sets the board battle background.
   *
   * @param battleBackground new battle background
   */
  public void setBattleBackground(String battleBackground) {
    this.battleBackground = battleBackground;
  }

  /**
   * Gets board enemy battle level.
   *
   * @return enemy battle level
   */
  public int getEnemyBattleLevel() {
    return enemyBattleLevel;
  }

  /**
   * Sets the board enemy battle level.
   *
   * @param enemyBattleLevel new battle level
   */
  public void setEnemyBattleLevel(int enemyBattleLevel) {
    this.enemyBattleLevel = enemyBattleLevel;
  }

  /**
   * Does the board allow random battles?
   *
   * @return true = allow, false = disallow
   */
  public boolean isAllowBattles() {
    return allowBattles;
  }

  /**
   * Sets the board allow random battle state.
   *
   * @param allowBattles new random battle state
   */
  public void setAllowBattles(boolean allowBattles) {
    this.allowBattles = allowBattles;
  }

  /**
   * Does the board allow saving.
   *
   * @return true = allow, false = disallow
   */
  public boolean isAllowSaving() {
    return allowSaving;
  }

  /**
   * Set the board allow saving state.
   *
   * @param allowSaving new save state
   */
  public void setAllowSaving(boolean allowSaving) {
    this.allowSaving = allowSaving;
  }

  /**
   * Gets the board ambient effect.
   *
   * @return ambient effect
   */
  public Color getAmbientEffect() {
    return ambientEffect;
  }

  /**
   * Sets the board ambient effect.
   *
   * @param ambientEffect new ambient effect
   */
  public void setAmbientEffect(Color ambientEffect) {
    this.ambientEffect = ambientEffect;
  }

  /**
   * Gets the last loaded tile set.
   *
   * @return last loaded tile set
   */
  public String getLastLoadedTileSet() {
    return lastLoadedTileSet;
  }

  /**
   * Gets a hash of the board tile sets indexed by name.
   *
   * @return tile set hash
   */
  public HashMap<String, TileSet> getTileSets() {
    return tileSets;
  }

  /**
   * Sets the hash of board tile sets indexed by name
   *
   * @param tileSets new tile set hash
   */
  public void setTileSets(HashMap<String, TileSet> tileSets) {
    this.tileSets = tileSets;
  }

  /**
   * Gets the tile set names.
   *
   * @return tile set names
   */
  public LinkedList<String> getTileSetNames() {
    return tileSetNames;
  }

  /**
   * Sets the tile set names,
   *
   * @param tileSetNames new tile set names.
   */
  public void setTileSetNames(LinkedList<String> tileSetNames) {
    this.tileSetNames = tileSetNames;
  }

  /**
   * Is this board selected in the editor?
   *
   * @return selected state
   */
  @Override
  public boolean isSelected() {
    return selectedState;
  }

  /**
   * Set the selected state of this board in the editor
   *
   * @param state new state
   */
  @Override
  public void setSelectedState(boolean state) {
    selectedState = state;
  }

  /**
   * Gets the compressed tile index for writing to a file.
   *
   * @return
   */
  public ArrayList<Integer> getCompressedTileIndex() {
    int x;
    int y;
    int z;
    int count;
    int index;
    int[] array;
    ArrayList<Integer> compressedIndex = new ArrayList<>();

    for (int k = 0; k < layerCount; k++) {
      for (int j = 0; j < height; j++) {
        for (int i = 0; i < width; i++) {
          x = i;
          y = j;
          z = k;

          array = findDuplicateTiles(x, y, z);

          count = array[0];
          index = boardDimensions[x][y][z];

          if (count > 1) {
            compressedIndex.add(-count);
            compressedIndex.add(index);

            i = array[1] - 1;
            j = array[2];
            k = array[3];
          } else {
            compressedIndex.add(index);
          }
        }
      }
    }

    return compressedIndex;
  }

  /**
   * Sets the boards compressed tile index.
   *
   * @param indices
   */
  public void setCompressedTileIndex(ArrayList<Integer> indices) {
    int x = 0;
    int y = 0;
    int z = 0;
    int tilesLoaded = 0;
    int totalTiles = width * height * layerCount;

    while (tilesLoaded < totalTiles) {
      int index = indices.remove(0);
      int count = 1;

      if (index < 0) { // compressed data
        count = -index;
        index = indices.remove(0);
      }

      for (int i = 0; i < count; i++) {
        boardDimensions[x][y][z] = index;
        tilesLoaded++;
        x++;

        if (x == width) {
          x = 0;
          y++;

          if (y == height) {
            y = 0;
            z++;
          }
        }
      }
    }
  }

  /**
   * Saves the board file, if the existing file ends with the 3.x extension it writes the new JSON
   * format with the same file name but with an append ".json" extension.
   *
   * @return true = success, false = failure
   */
  public boolean save() {
    if (file.getName().endsWith(".brd")) {
      saveBinary();
    }

    try {
      AssetManager.getInstance().serialize(AssetManager.getInstance().getHandle(this));
      return true;
    } catch (IOException | AssetException ex) {
      Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
  }

  /**
   * Saves the board file as the specified file.
   *
   * @param fileName
   * @return true = success, false = failure
   */
  public boolean saveAs(File fileName) {
    file = fileName;
    return save();
  }

  @Override
  public AssetDescriptor getDescriptor() {
    return new AssetDescriptor(this.getFile().toURI());
  }

  @Override
  public void reset() {
    tileNameIndex = new ArrayList<>();
    loadedTilesIndex = new ArrayList<>();
    tileShading = new ArrayList<>();
    boardImages = new ArrayList<>();
    spriteImages = new ArrayList<>();
    programs = new ArrayList<>();
    threads = new ArrayList<>();
    lights = new ArrayList<>();
    vectors = new ArrayList<>();
    sprites = new ArrayList<>();
    constants = new ArrayList<>();
    layerTitles = new ArrayList<>();
    directionalLinks = new ArrayList<>();
    backgroundImages = new ArrayList<>();
    tileSets = new HashMap<>();

    width = 0;
    height = 0;
    layerCount = 0;
    coordinateType = STANDARD;
    boardDimensions = new int[width][height][layerCount];
    ubShading = 0;
    shadingLayer = 0;
    backgroundColour = 0;
    backgroundMusic = "";
    firstRunProgram = "";
    battleBackground = "";
    enemyBattleLevel = 0;
    allowBattles = false;
    allowSaving = false;
    ambientEffect = new Color(0, 0, 0);
    startingPositionX = 0;
    startingPositionY = 0;
    startingLayer = 0;
    lastLoadedTileSet = "";
    randomByte = (byte) 0;
    associatedVector = 0;
    imageTransluceny = 0;
  }

  /**
   * Update the global <code>TileSetCache</code> with any new tile sets that appear on this board.
   */
  public void updateTileSetCache() {
    TileSetCache cache = TileSetCache.getInstance();

    // Load the tiles into memory
    for (String indexString : tileNameIndex) {
      if (!indexString.isEmpty()) {
        if (indexString.substring(indexString.length() - 3).equals("tan")) {
          String assetPath = "file:///"
                  + System.getProperty("project.path")
                  + PropertiesSingleton.getProperty("toolkit.directory.tileset")
                  + "/" + indexString;

          try {
            AnimatedTile aTile = (AnimatedTile) AssetManager.getInstance().deserialize(
                    new AssetDescriptor(assetPath)).getAsset();
            indexString = aTile.getFrames().get(0).getFrameTarget();
          } catch (IOException | AssetException ex) {
            System.out.println(ex.toString());
          }
        }

        String tileSetName = indexString.split(".tst")[0] + ".tst";

        if (!tileSetNames.contains(tileSetName)) {
          tileSetNames.add(tileSetName);
          tileSets.put(tileSetName, cache.addTileSet(tileSetName));
        }

        loadedTilesIndex.add(tileSets.get(tileSetName).getTile(Integer.parseInt(indexString.split(".tst")[1]) - 1));

      } else {
        loadedTilesIndex.add(null);
      }
    }
  }

  /**
   * Add a new <code>BoardChangeListener</code> for this board.
   *
   * @param listener new change listener
   */
  public void addBoardChangeListener(BoardChangeListener listener) {
    boardChangeListeners.add(listener);
  }

  /**
   * Remove an existing <code>BoardChangeListener</code> for this board.
   *
   * @param listener change listener
   */
  public void removeBoardChangeListener(BoardChangeListener listener) {
    boardChangeListeners.remove(listener);
  }

  /**
   * Fires the <code>BoardChangedEvent</code> informs all the listeners that this board has changed.
   */
  public void fireBoardChanged() {
    BoardChangedEvent event = null;
    Iterator iterator = boardChangeListeners.iterator();

    while (iterator.hasNext()) {
      if (event == null) {
        event = new BoardChangedEvent(this);
      }

      ((BoardChangeListener) iterator.next()).boardChanged(event);
    }
  }

  /**
   * Fires the <code>BoardChangedEvent</code> informs all the listeners that this board has changed.
   *
   * @param layer new layer
   */
  public void fireBoardLayerAdded(BoardLayer layer) {
    BoardChangedEvent event = null;
    Iterator iterator = boardChangeListeners.iterator();

    while (iterator.hasNext()) {
      if (event == null) {
        event = new BoardChangedEvent(this);
        event.setLayer(layer);
      }

      ((BoardChangeListener) iterator.next()).boardLayerAdded(event);
    }
  }

  /**
   * Fires the <code>BoardChangedEvent</code> informs all the listeners that this board has changed.
   *
   * @param layer effected layer
   */
  public void fireBoardLayerMovedUp(BoardLayer layer) {
    BoardChangedEvent event = null;
    Iterator iterator = boardChangeListeners.iterator();

    while (iterator.hasNext()) {
      if (event == null) {
        event = new BoardChangedEvent(this);
        event.setLayer(layer);
      }

      ((BoardChangeListener) iterator.next()).boardLayerMovedUp(event);
    }
  }

  /**
   * Fires the <code>BoardChangedEvent</code> informs all the listeners that this board has changed.
   *
   * @param layer effected layer
   */
  public void fireBoardLayerMovedDown(BoardLayer layer) {
    BoardChangedEvent event = null;
    Iterator iterator = boardChangeListeners.iterator();

    while (iterator.hasNext()) {
      if (event == null) {
        event = new BoardChangedEvent(this);
        event.setLayer(layer);
      }

      ((BoardChangeListener) iterator.next()).boardLayerMovedDown(event);
    }
  }

  /**
   * Fires the <code>BoardChangedEvent</code> informs all the listeners that this board has changed.
   *
   * @param layer cloned layer
   */
  public void fireBoardLayerCloned(BoardLayer layer) {
    BoardChangedEvent event = null;
    Iterator iterator = boardChangeListeners.iterator();

    while (iterator.hasNext()) {
      if (event == null) {
        event = new BoardChangedEvent(this);
        event.setLayer(layer);
      }

      ((BoardChangeListener) iterator.next()).boardLayerCloned(event);
    }
  }

  /**
   * Fires the <code>BoardChangedEvent</code> informs all the listeners that this board has changed.
   *
   * @param layer deleted layer
   */
  public void fireBoardLayerDeleted(BoardLayer layer) {
    BoardChangedEvent event = null;
    Iterator iterator = boardChangeListeners.iterator();

    while (iterator.hasNext()) {
      if (event == null) {
        event = new BoardChangedEvent(this);
        event.setLayer(layer);
      }

      ((BoardChangeListener) iterator.next()).boardLayerDeleted(event);
    }
  }

  /**
   * Add a new blank layer to this board.
   */
  public void addLayer() {
    int layerNumber = layers.size() + 1;

    BoardLayer layer = new BoardLayer(this);
    layer.setName("Untitled Layer " + layerNumber);
    layer.setNumber(layers.size());
    layers.add(layer);

    fireBoardLayerAdded(layer);
  }

  /**
   * Moves the layer up to the specified index if possible.
   *
   * @param index higher index
   * @return was it moved
   */
  public boolean moveLayerUp(int index) {
    // Highest possible index, can't be move up!
    if (index == layers.size()) {
      return false;
    }

    BoardLayer down = layers.get(index + 1);
    BoardLayer up = layers.get(index);
    layers.set(index + 1, up);
    layers.set(index, down);

    down.moveLayerDown();
    up.moveLayerUp();

    fireBoardLayerMovedUp(up);

    return true;
  }

  /**
   * Moves the layer down to the specified index if possible.
   *
   * @param index lower index
   * @return was it moved
   */
  public boolean moveLayerDown(int index) {
    // Lowest possible layer, can't be move down!
    if (index == 0) {
      return false;
    }

    BoardLayer down = layers.get(index);
    BoardLayer up = layers.get(index - 1);
    layers.set(index - 1, down);
    layers.set(index, up);

    down.moveLayerDown();
    up.moveLayerUp();

    fireBoardLayerMovedDown(down);

    return true;
  }

  /**
   * Clones the layer at the specified index.
   *
   * @param index clone layer at index
   */
  public void cloneLayer(int index) {
    try {
      Iterator iterator = layers.listIterator(index + 1);

      while (iterator.hasNext()) {
        BoardLayer layer = (BoardLayer) iterator.next();
        layer.moveLayerUp();
      }

      BoardLayer clone = (BoardLayer) layers.get(index).clone();
      layers.add(index + 1, clone);

      fireBoardLayerCloned(clone);
    } catch (CloneNotSupportedException e) {
      System.out.println(e.toString());
    }

  }

  /**
   * Deletes the layer at the specified index.
   *
   * @param index delete layer at index
   */
  public void deleteLayer(int index) {
    Iterator iterator = layers.listIterator(index + 1);

    while (iterator.hasNext()) {
      BoardLayer layer = (BoardLayer) iterator.next();
      layer.moveLayerDown();
    }

    BoardLayer removedLayer = layers.get(index);
    layers.remove(index);

    fireBoardLayerDeleted(removedLayer);
  }

  /**
   * Creates the board layers and their containing components.
   */
  public void createLayers() {
    for (int i = 0; i < layerCount; i++) {
      BoardLayer layer = new BoardLayer(this);
      layer.setName(layerTitles.get(i));
      layer.setNumber(i);

      int count = width * height;
      int x = 0;
      int y = 0;

      for (int j = 0; j < count; j++) {
        if (boardDimensions[x][y][i] - 1 >= 0) {
          try {
            layer.getTiles()[x][y] = getTileFromIndex(boardDimensions[x][y][i] - 1);
          } catch (Exception e) {
            System.out.println(boardDimensions[x][y][i] - 1);
          }
        }

        x++;
        if (x == width) {
          x = 0;
          y++;
          if (y == height) {
            break;
          }
        }
      }

      for (BoardLight light : lights) {
        if (light.getLayer() == i) {
          layer.getLights().add(light);
        }
      }

      for (BoardVector vector : vectors) {
        if (vector.getLayer() == i) {
          layer.getVectors().add(vector);
        }
      }

      for (BoardProgram program : programs) {
        if (program.getLayer() == i) {
          layer.getPrograms().add(program);
        }
      }

      for (BoardSprite sprite : sprites) {
        if (sprite.getLayer() == i) {
          layer.getSprites().add(sprite);
        }
      }

      for (BoardImage image : boardImages) {
        if (image.getLayer() == i) {
          layer.getImages().add(image);
        }
      }

      layers.add(layer);
    }
  }

  /**
   * Searches ahead of a given position for duplicate tiles, and returns the amount followed by the
   * position at which they end at.
   *
   * @param x starting x position (width)
   * @param y starting y position (height)
   * @param z starting z position (layerCount)
   * @return returns an array containing 4 <code>int</code>'s, the first element contains the number
   * of duplicate tile, the others contain the positions of x, y, and z at the end of the loop. They
   * would have normally been passed by reference but Java doesn't support
   */
  public int[] findDuplicateTiles(int x, int y, int z) {
    int tile = boardDimensions[x][y][z];
    int count = 0;

    int k;
    int j = 0;
    int i = 0;

    for (k = z; k < layerCount; k++) {
      for (j = y; j < height; j++) {
        for (i = x; i < width; i++) {
          if (boardDimensions[i][j][k] != tile) {
            int[] array
                    = {
                      count, i, j, k
                    };

            return array;
          }

          count++;
        }

        x = 0;
      }

      y = 0;
    }

    int[] array
            = {
              count, i, j, k
            };

    return array;
  }

  /**
   * Updates all of the IO attributes used in saving, gets the new information from all of the
   * layers that have been created. Maintains compatability with Geoff's original open and save
   * routines.
   */
  public void updateBoardIO() {
    layerCount = layers.size();
    layerTitles.clear();
    lights.clear();
    vectors.clear();
    programs.clear();
    sprites.clear();
    boardImages.clear();

    boardDimensions = new int[width][height][layerCount];
    int count = width * height;
    int layerIndex = 0;

    for (BoardLayer layer : layers) {
      layerTitles.add(layer.getName());

      int x = 0;
      int y = 0;

      for (int i = 0; i < count; i++) {
        Tile tile = layer.getTileAt(x, y);

        if (tile.getTileSet() != null) {
          int index = tileNameIndex.indexOf(layer.getTileAt(x, y).getName());

          if (index == -1) {
            tileNameIndex.add(layer.getTileAt(x, y).getName());
            index = tileNameIndex.size() - 1;
          }

          boardDimensions[x][y][layerIndex] = index + 1;
        }

        x++;

        if (x == width) {
          x = 0;
          y++;

          if (y == height) {
            break;
          }
        }
      }

      lights.addAll(layer.getLights());
      vectors.addAll(layer.getVectors());
      programs.addAll(layer.getPrograms());
      sprites.addAll(layer.getSprites());
      boardImages.addAll(layer.getImages());

      layerIndex++;
    }
  }

  /**
   * Used to open the old binary file format from the TK 3.x era, this remains here simply for those
   * few who wish to migrate to TK 4.0. However it will be removed in the next iteration of 4.1.
   *
   * @return true for success, false for failure
   * @deprecated
   */
  public boolean openBinary() {
    try {
      binaryIO = new BinaryIO(new FileInputStream(file));

      if (binaryIO.readBinaryString().equals(FILE_HEADER)) {
        int majorVersion = binaryIO.readBinaryInteger();
        int minorVersion = binaryIO.readBinaryInteger();

        width = binaryIO.readBinaryInteger();
        height = binaryIO.readBinaryInteger();
        layerCount = binaryIO.readBinaryInteger();
        coordinateType = binaryIO.readBinaryInteger();

        if (coordinateType == ISO_ROTATED) {
          int tmpWidth = width;
          int tmpHeight = height;

          width += tmpHeight;
          height += tmpWidth;
        }

        boardDimensions = new int[width][height][layerCount];

        // Total number of distinct tile types used, if we add a
        // new tile we will have to check if the tileIndex already
        // contains the name of the tile e.g. default.tst2
        int lookUpTableSize = binaryIO.readBinaryInteger();

        // Appears to be a random "" null string at the start of
        // the look up table. To avoid any issues we must "eat" this
        // null string.
        randomByte = binaryIO.readBinaryByte();

        for (int i = 0; i < lookUpTableSize; i++) {
          // Read in the name of the tiles used on this board.
          tileNameIndex.add(binaryIO.readBinaryString());
        }

        int totalTiles = width * height * layerCount;

        int x = 0;
        int y = 0;
        int z = 0;
        int tilesLoaded = 0;

        while (tilesLoaded < totalTiles) {
          int index = binaryIO.readBinaryInteger();
          int count = 1;

          if (index < 0) { // compressed data
            count = -index;
            index = binaryIO.readBinaryInteger();
          }

          for (int i = 0; i < count; i++) {
            boardDimensions[x][y][z] = index;
            tilesLoaded++;
            x++;

            if (x == width) {
              x = 0;
              y++;

              if (y == height) {
                y = 0;
                z++;
              }
            }
          }
        }

        /* Tile Shading Data Notes
         *
         * Unsure why ubShading and then shadingLayer are read since 
         * the shading is only applied to one(?)
         * layer, perhaps this was to allow for more shading layerCount in 
         * the future, however it is now unnecessary
         * and so the ubShading will be ignored.
         */
        ubShading = binaryIO.readBinaryInteger();

        // apply shading from this layer down
        shadingLayer = binaryIO.readBinaryLong();

        // WE ARE ASSUMING NO SHADING FOR NOW!
        // only one layer, so total tiles is just w * h
        int totalShading = width * height;
        int shadingLoaded = 0;

        while (shadingLoaded < totalShading) {
          int count = binaryIO.readBinaryInteger();
          shadingLoaded += count;

          int red = binaryIO.readBinaryInteger();
          int green = binaryIO.readBinaryInteger();
          int blue = binaryIO.readBinaryInteger();

          tileShading.add(new BoardLayerShade(red, green, blue,
                  count));
        }

        // Lights
        int numberLights = binaryIO.readBinaryInteger();
        for (int i = 0; i < numberLights + 1; i++) {
          BoardLight newLight = new BoardLight();

          newLight.setLayer(binaryIO.readBinaryLong());
          newLight.setType(binaryIO.readBinaryLong());

          int numberPoints = binaryIO.readBinaryInteger();
          for (int j = 0; j < numberPoints + 1; j++) {
            newLight.addPoint(new Point(
                    (int) binaryIO.readBinaryLong(),
                    (int) binaryIO.readBinaryLong()));
          }

          int numColors = binaryIO.readBinaryInteger();
          for (int j = 0; j < numColors + 1; j++) {
            newLight.addColor(new Color(
                    binaryIO.readBinaryInteger(),
                    binaryIO.readBinaryInteger(),
                    binaryIO.readBinaryInteger()));
          }

          lights.add(newLight);
        }

        // Vector count is one less than it should be so +1
        // to vectors all round!
        int numberVectors = binaryIO.readBinaryInteger();
        for (int i = 0; i < numberVectors + 1; i++) {
          BoardVector newVector = new BoardVector();

          // How Many Points in said vector?
          int numberPoints = binaryIO.readBinaryInteger();

          for (int j = 0; j < numberPoints + 1; j++) {
            newVector.addPoint(binaryIO.readBinaryLong(), binaryIO.readBinaryLong());
          }

          newVector.setAttributes(binaryIO.readBinaryInteger());
          newVector.setClosed(binaryIO.readBinaryInteger() == 1);
          newVector.setLayer(binaryIO.readBinaryInteger());
          newVector.setTileType(binaryIO.readBinaryInteger());
          newVector.setHandle(binaryIO.readBinaryString());

          vectors.add(newVector);
        }

        // Programs
        int numberPrograms = binaryIO.readBinaryInteger();

        for (int i = 0; i < numberPrograms + 1; i++) {
          BoardProgram newProgram = new BoardProgram();

          newProgram.setFileName(binaryIO.readBinaryString());
          newProgram.setGraphic(binaryIO.readBinaryString());
          newProgram.setInitialVariable(binaryIO.readBinaryString());
          newProgram.setInitialValue(binaryIO.readBinaryString());
          newProgram.setFinalVariable(binaryIO.readBinaryString());
          newProgram.setFinalValue(binaryIO.readBinaryString());
          newProgram.setActivate(binaryIO.readBinaryInteger());
          newProgram.setActivationType(binaryIO.readBinaryInteger());
          newProgram.setDistanceRepeat(binaryIO.readBinaryInteger());
          newProgram.setLayer(binaryIO.readBinaryInteger());

          BoardVector programVector = new BoardVector();
          int numberPoints = binaryIO.readBinaryInteger();

          for (int j = 0; j < numberPoints + 1; j++) {
            programVector.addPoint(binaryIO.readBinaryLong(),
                    binaryIO.readBinaryLong());
          }

          programVector.setClosed(binaryIO.readBinaryInteger() == 1);
          programVector.setHandle(binaryIO.readBinaryString());

          newProgram.setVector(programVector);
          programs.add(newProgram);
        }

        // Sprites
        int numberSprites = binaryIO.readBinaryInteger();

        for (int i = 0; i < numberSprites + 1; i++) {
          BoardSprite newSprite = new BoardSprite();

          newSprite.setFileName(binaryIO.readBinaryString());
          newSprite.setActivationProgram(binaryIO.readBinaryString());
          newSprite.setMultitaskingProgram(binaryIO.readBinaryString());
          newSprite.setInitialVariable(binaryIO.readBinaryString());
          newSprite.setInitialValue(binaryIO.readBinaryString());
          newSprite.setFinalVariable(binaryIO.readBinaryString());
          newSprite.setFinalValue(binaryIO.readBinaryString());
          newSprite.setLoadingVariable(binaryIO.readBinaryString());
          newSprite.setLoadingValue(binaryIO.readBinaryString());
          newSprite.setActivate(binaryIO.readBinaryInteger());
          newSprite.setActivationType(binaryIO.readBinaryInteger());
          newSprite.setX(binaryIO.readBinaryInteger());
          newSprite.setY(binaryIO.readBinaryInteger());
          newSprite.setLayer(binaryIO.readBinaryInteger());

          // skip one INT of data
          associatedVector = binaryIO.readBinaryInteger();

          sprites.add(newSprite);
        }

        //Images
        int numberImage = binaryIO.readBinaryInteger();

        for (int i = 0; i < numberImage + 1; i++) {
          BoardImage newImage = new BoardImage();
          newImage.setFileName(binaryIO.readBinaryString());
          newImage.setBoundLeft(binaryIO.readBinaryLong());
          newImage.setBoundTop(binaryIO.readBinaryLong());
          newImage.setLayer(binaryIO.readBinaryInteger());
          newImage.setDrawType(binaryIO.readBinaryInteger());
          newImage.setTransparentColour(binaryIO.readBinaryLong());

          // skip one INT of data
          imageTransluceny = binaryIO.readBinaryInteger();

          boardImages.add(newImage);
        }

        // Threads
        int numberThread = binaryIO.readBinaryInteger();

        for (int i = 0; i < numberThread + 1; i++) {
          threads.add(binaryIO.readBinaryString());
        }

        // Constants
        int numberConstants = binaryIO.readBinaryInteger();

        for (int i = 0; i < numberConstants + 1; i++) {
          constants.add(binaryIO.readBinaryString());
        }

        // Layer Titles
        // Geoff's random +1 here causes problems at save time!
        for (int i = 0; i < layerCount + 1; i++) {
          layerTitles.add(binaryIO.readBinaryString());
        }

        for (int i = 0; i < 4; i++) {
          directionalLinks.add(binaryIO.readBinaryString());
        }

        BoardImage backgroundImage = new BoardImage();
        backgroundImage.setFileName(binaryIO.readBinaryString());
        backgroundImage.setDrawType(binaryIO.readBinaryLong());
        backgroundImage.setScrollRatio(20); // 1 Pixel for every 10 the player moves
        backgroundImages.add(backgroundImage);

        backgroundColour = binaryIO.readBinaryLong();
        backgroundMusic = binaryIO.readBinaryString();

        firstRunProgram = binaryIO.readBinaryString();
        battleBackground = binaryIO.readBinaryString();
        enemyBattleLevel = binaryIO.readBinaryInteger();
        allowBattles = binaryIO.readBinaryInteger() == -1;
        allowSaving = !(binaryIO.readBinaryInteger() == -1);

        try {
          ambientEffect = new Color(
                  binaryIO.readBinaryInteger(),
                  binaryIO.readBinaryInteger(),
                  binaryIO.readBinaryInteger());
        } catch (CorruptAssetException | IllegalArgumentException e) {
          ambientEffect = new Color(0, 0, 0);
        }

        startingPositionX = binaryIO.readBinaryInteger();
        startingPositionY = binaryIO.readBinaryInteger();
        startingLayer = binaryIO.readBinaryInteger();

        updateTileSetCache();
        createLayers();
      }

      binaryIO.closeInput();
      inputStream.close();
    } catch (AssetException | IOException e) {
      System.out.println(e.toString());
    }

    return true;
  }

  /**
   * Used to save the old binary file format from the TK 3.x era, this remains here simply because
   * it took so long to implement it that throwing it away for 4.0 would be a waste of work. It will
   * be removed in 4.1.
   *
   * @return true for success, and false for failure
   * @deprecated
   */
  public boolean saveBinary() {
    updateBoardIO();

    try {
      outputStream = new FileOutputStream(file);
      binaryIO.setOutputStream(outputStream);

      binaryIO.writeBinaryString(FILE_HEADER);
      binaryIO.writeBinaryInteger(MAJOR_VERSION);
      binaryIO.writeBinaryInteger(MINOR_VERSION);

      binaryIO.writeBinaryInteger(width);
      binaryIO.writeBinaryInteger(height);
      binaryIO.writeBinaryInteger(layerCount);
      binaryIO.writeBinaryInteger(coordinateType);

      binaryIO.writeBinaryInteger(tileNameIndex.size());
      binaryIO.writeBinaryByte(randomByte);

      for (String tile : tileNameIndex) {
        binaryIO.writeBinaryString(tile);
      }

      // Tiles
      int x;
      int y;
      int z;
      int count;
      int index;
      int[] array;

      for (int k = 0; k < layerCount; k++) {
        for (int j = 0; j < height; j++) {
          for (int i = 0; i < width; i++) {
            x = i;
            y = j;
            z = k;

            array = findDuplicateTiles(x, y, z);

            count = array[0];
            index = boardDimensions[x][y][z];

            if (count > 1) {
              binaryIO.writeBinaryInteger(-count);
              binaryIO.writeBinaryInteger(index);

              i = array[1] - 1;
              j = array[2];
              k = array[3];
            } else {
              binaryIO.writeBinaryInteger(index);
            }
          }
        }
      }

      // Shading
      binaryIO.writeBinaryInteger(ubShading);
      binaryIO.writeBinaryLong(shadingLayer);

      for (BoardLayerShade layerShade : tileShading) {
        binaryIO.writeBinaryInteger((int) layerShade.getLayer());
        binaryIO.writeBinaryInteger(layerShade.getColour().getRed());
        binaryIO.writeBinaryInteger(layerShade.getColour().getGreen());
        binaryIO.writeBinaryInteger(layerShade.getColour().getBlue());
      }

      // Lights
      binaryIO.writeBinaryInteger(lights.size() - 1);

      for (BoardLight light : lights) {
        binaryIO.writeBinaryLong(light.getLayer());
        binaryIO.writeBinaryLong(light.getType());

        for (Point point : light.getPoints()) {
          binaryIO.writeBinaryLong(point.x);
          binaryIO.writeBinaryLong(point.y);
        }

        for (Color color : light.getColors()) {
          binaryIO.writeBinaryInteger(color.getRed());
          binaryIO.writeBinaryInteger(color.getGreen());
          binaryIO.writeBinaryInteger(color.getBlue());
        }
      }

      // Vectors
      binaryIO.writeBinaryInteger(vectors.size() - 1);

      for (BoardVector vector : vectors) {
        binaryIO.writeBinaryInteger(vector.getPoints().size() - 1);

        for (Point point : vector.getPoints()) {
          binaryIO.writeBinaryLong((long) point.x);
          binaryIO.writeBinaryLong((long) point.y);
        }

        binaryIO.writeBinaryInteger(vector.getAttributes());

        if (vector.isClosed()) {
          binaryIO.writeBinaryInteger(1);
        } else {
          binaryIO.writeBinaryInteger(0);
        }

        binaryIO.writeBinaryInteger(vector.getLayer());
        binaryIO.writeBinaryInteger(vector.getTileType());
        binaryIO.writeBinaryString(vector.getHandle());
      }

      // Programs
      binaryIO.writeBinaryInteger(programs.size() - 1);

      for (BoardProgram program : programs) {
        binaryIO.writeBinaryString(program.getFileName());
        binaryIO.writeBinaryString(program.getGraphic());
        binaryIO.writeBinaryString(program.getInitialVariable());
        binaryIO.writeBinaryString(program.getInitialValue());
        binaryIO.writeBinaryString(program.getFinalVariable());
        binaryIO.writeBinaryString(program.getFinalValue());
        binaryIO.writeBinaryInteger((int) program.getActivate());
        binaryIO.writeBinaryInteger((int) program.getActivationType());
        binaryIO.writeBinaryInteger((int) program.getDistanceRepeat());
        binaryIO.writeBinaryInteger((int) program.getLayer());

        BoardVector programVector = program.getVector();
        binaryIO.writeBinaryInteger(programVector.getPointCount() - 1);

        for (Point point : programVector.getPoints()) {
          binaryIO.writeBinaryLong((long) point.x);
          binaryIO.writeBinaryLong((long) point.y);
        }

        if (programVector.isClosed()) {
          binaryIO.writeBinaryInteger(1);
        } else {
          binaryIO.writeBinaryInteger(0);
        }

        binaryIO.writeBinaryString(programVector.getHandle());
      }

      // Sprites
      binaryIO.writeBinaryInteger(sprites.size() - 1);

      for (BoardSprite sprite : sprites) {
        binaryIO.writeBinaryString(sprite.getFileName());
        binaryIO.writeBinaryString(sprite.getActivationProgram());
        binaryIO.writeBinaryString(sprite.getMultitaskingProgram());
        binaryIO.writeBinaryString(sprite.getInitialVariable());
        binaryIO.writeBinaryString(sprite.getInitialValue());
        binaryIO.writeBinaryString(sprite.getFinalVariable());
        binaryIO.writeBinaryString(sprite.getFinalValue());
        binaryIO.writeBinaryString(sprite.getLoadingVariable());
        binaryIO.writeBinaryString(sprite.getLoadingValue());
        binaryIO.writeBinaryInteger((int) sprite.getActivate());
        binaryIO.writeBinaryInteger((int) sprite.getActivationType());
        binaryIO.writeBinaryInteger((int) sprite.getX());
        binaryIO.writeBinaryInteger((int) sprite.getY());
        binaryIO.writeBinaryInteger((int) sprite.getLayer());

        // INT will be skipped.
        binaryIO.writeBinaryInteger(associatedVector);
      }

      // Images
      binaryIO.writeBinaryInteger(boardImages.size() - 1);

      for (BoardImage image : boardImages) {
        binaryIO.writeBinaryString(image.getFileName());
        binaryIO.writeBinaryLong(image.getBoundLeft());
        binaryIO.writeBinaryLong(image.getBoundTop());
        binaryIO.writeBinaryInteger((int) image.getLayer());
        binaryIO.writeBinaryInteger((int) image.getDrawType());
        binaryIO.writeBinaryLong(image.getTransparentColour());

        // INT will be skipped.
        binaryIO.writeBinaryInteger(imageTransluceny);
      }

      // Threads
      binaryIO.writeBinaryInteger(threads.size() - 1);

      for (String thread : threads) {
        binaryIO.writeBinaryString(thread);
      }

      // Constants
      binaryIO.writeBinaryInteger(constants.size() - 1);

      for (String constant : constants) {
        binaryIO.writeBinaryString(constant);
      }

      // Bug must write out a null string here.
      binaryIO.writeBinaryString("");

      // Layer Titles
      for (String layerTitle : layerTitles) {
        binaryIO.writeBinaryString(layerTitle);
      }

      // Directonal Links
      for (String link : directionalLinks) {
        binaryIO.writeBinaryString(link);
      }

      // Background Image
      BoardImage backgroundImage = backgroundImages.get(0);
      binaryIO.writeBinaryString(backgroundImage.getFileName());
      binaryIO.writeBinaryLong(backgroundImage.getDrawType());

      // Misc
      binaryIO.writeBinaryLong(backgroundColour);
      binaryIO.writeBinaryString(backgroundMusic);

      binaryIO.writeBinaryString(firstRunProgram);
      binaryIO.writeBinaryString(battleBackground);
      binaryIO.writeBinaryInteger(enemyBattleLevel);

      if (allowBattles) {
        binaryIO.writeBinaryInteger(-1);
      } else {
        binaryIO.writeBinaryInteger(0);
      }

      if (allowSaving) {
        binaryIO.writeBinaryInteger(0);
      } else {
        binaryIO.writeBinaryInteger(-1);
      }

      binaryIO.writeBinaryInteger(ambientEffect.getRed());
      binaryIO.writeBinaryInteger(ambientEffect.getGreen());
      binaryIO.writeBinaryInteger(ambientEffect.getBlue());
      binaryIO.writeBinaryInteger(startingPositionX);
      binaryIO.writeBinaryInteger(startingPositionY);
      binaryIO.writeBinaryInteger(startingLayer);

      binaryIO.closeOutput();

      return true;
    } catch (IOException e) {
      System.out.println(e.toString());
      return false;
    }
  }

}
