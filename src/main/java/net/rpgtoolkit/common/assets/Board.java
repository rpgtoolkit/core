/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.rpgtoolkit.common.Selectable;
import net.rpgtoolkit.common.assets.events.BoardChangedEvent;
import net.rpgtoolkit.common.assets.listeners.BoardChangeListener;
import net.rpgtoolkit.common.utilities.TileSetCache;
import net.rpgtoolkit.common.utilities.PropertiesSingleton;

/**
 * A model that represents <code>Board</code> files in the RPGToolkit engine and editor. Used during
 * the serialization processes to various formats, at the moment it contains the old binary routines
 * for opening 3.x formats which should be removed in 4.1.
 *
 * @author Geoff Wilson
 * @author Joshua Michael Daly
 */
public final class Board extends AbstractAsset implements Selectable {

  /**
   * Geometric perspective of a board for use with directional movement, rendering, raycasting,
   * and other mathematical transformations.
   *
   * @author Chris Hutchinson
   */
  public enum Perspective {

    ORTHOGONAL,
    ISOMETRIC_STACKED,
    ISOMETRIC_ROTATED;

  }

  // Non-IO
  private final LinkedList<BoardChangeListener> boardChangeListeners = new LinkedList<>();
  private final LinkedList<BoardLayer> layers = new LinkedList<>();
  private LinkedList<String> tileSetNames = new LinkedList<>();
  private boolean selectedState;

  // Variables
  private int width;
  private int height;
  private int layerCount;
  private Perspective perspective;
  private ArrayList<String> tileNameIndex;      // Contains string names e.g. default.tst1
  private ArrayList<Tile> loadedTilesIndex;     // Contains tile objects of e.g. default.tst1
  private int[][][] boardDimensions;            // x, y, z
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

  /**
   * Creates an empty board.
   * @param descriptor
   */
  public Board(AssetDescriptor descriptor) {
    super(descriptor);
    this.tileNameIndex = new ArrayList<>();
    this.loadedTilesIndex = new ArrayList<>();
    this.tileShading = new ArrayList<>();
    this.boardImages = new ArrayList<>();
    this.spriteImages = new ArrayList<>();
    this.programs = new ArrayList<>();
    this.threads = new ArrayList<>();
    this.lights = new ArrayList<>();
    this.vectors = new ArrayList<>();
    this.sprites = new ArrayList<>();
    this.constants = new ArrayList<>();
    this.layerTitles = new ArrayList<>();
    this.directionalLinks = new ArrayList<>();
    this.backgroundImages = new ArrayList<>();
    this.tileSets = new HashMap<>();
  }

  /**
   * Creates a new board with the specified width and height.
   *
   * @param descriptor
   * @param width  board width
   * @param height board height
   */
  public Board(AssetDescriptor descriptor, int width, int height) {
    this(descriptor);
    reset();
    this.setWidth(width);
    this.setHeight(height);
  }

  /**
   * Gets this boards layers.
   *
   * @return the board layers
   */
  public List<BoardLayer> getLayers() {
    return this.layers;
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
    if (width <= 0) {
      throw new IllegalArgumentException("width must be > 0");
    }
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
    if (height <= 0) {
      throw new IllegalArgumentException("height must be > 0");
    }
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
    if (count <= 0) {
      throw new IllegalArgumentException("layer count must be > 0");
    }
    this.layerCount = count;
  }

  /**
   * Gets the board's geometric perspective.
   *
   * @return current geometric perspective
   */
  public Perspective getPerspective() {
    return this.perspective;
  }

  /**
   * Sets the board's geometric perspective.
   *
   * @param perspective new geometric perspective
   */
  public void setPerspective(Perspective perspective) {
    this.perspective = perspective;
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
  public List<String> getConstants() {
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
  public List<String> getLayerTitles() {
    return layerTitles;
  }

  /**
   * Sets the board layer titles.
   *
   * @param layerTitles new board layer titles
   */
  public void setLayerTitles(Collection<String> layerTitles) {
    this.layerTitles.clear();
    this.layerTitles.addAll(layerTitles);
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
  public List<String> getDirectionalLinks() {
    return directionalLinks;
  }

  /**
   * Sets the board directional links e.g. N, S, E, W.
   *
   * @param directionalLinks new directional links
   */
  public void setDirectionalLinks(Collection<String> directionalLinks) {
    this.directionalLinks.clear();
    this.directionalLinks.addAll(directionalLinks);
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
  public Map<String, TileSet> getTileSets() {
    return tileSets;
  }

  /**
   * Sets the hash of board tile sets indexed by name
   *
   * @param tileSets new tile set hash
   */
  public void setTileSets(Map<String, TileSet> tileSets) {
    this.tileSets.clear();
    this.tileSets.putAll(tileSets);
  }

  /**
   * Gets the tile set names.
   *
   * @return tile set names
   */
  public List<String> getTileSetNames() {
    return tileSetNames;
  }

  /**
   * Sets the tile set names,
   *
   * @param tileSetNames new tile set names.
   */
  public void setTileSetNames(Collection<String> tileSetNames) {
    this.tileSetNames.clear();
    this.tileSetNames.addAll(tileSetNames);
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

  @Override
  public void reset() {
    
    tileNameIndex.clear();
    loadedTilesIndex.clear();
    tileShading.clear();
    boardImages.clear();
    spriteImages.clear();
    programs.clear();
    threads.clear();
    lights.clear();
    vectors.clear();
    sprites.clear();
    constants.clear();
    layerTitles.clear();
    directionalLinks.clear();
    backgroundImages.clear();
    tileSets.clear();

    width = 0;
    height = 0;
    layerCount = 0;
    perspective = Perspective.ORTHOGONAL;
    boardDimensions = new int[width][height][layerCount];
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

  }

  /**
   * Update the global <code>TileSetCache</code> with any new tile sets that appear on this board.
   */
  public void updateTileSetCache() {
    // Load the tiles into memory
    for (String indexString : tileNameIndex) {
      if (!indexString.isEmpty()) {
        if (indexString.substring(indexString.length() - 3).equals("tan")) {
          String assetPath = System.getProperty("project.path")
            + PropertiesSingleton.getProperty("toolkit.directory.tileset")
            + File.separator + indexString;

          try {
            AnimatedTile aTile = (AnimatedTile) AssetManager.getInstance().deserialize(
              new AssetDescriptor(new File(assetPath).toURI())).getAsset();
            indexString = aTile.getFrames().get(0).getFrameTarget();
          } catch (IOException | AssetException ex) {
            System.out.println(ex.toString());
          }
        }

        String tileSetName = indexString.split(".tst")[0] + ".tst";

        if (!tileSetNames.contains(tileSetName)) {
          tileSetNames.add(tileSetName);
          tileSets.put(tileSetName, TileSetCache.addTileSet(tileSetName));
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

}
