/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization.legacy;

import net.rpgtoolkit.common.assets.*;
import net.rpgtoolkit.common.io.ByteBufferHelper;
import net.rpgtoolkit.common.io.Paths;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import net.rpgtoolkit.common.utilities.CoreProperties;

public class LegacyBoardSerializer extends AbstractAssetSerializer {

  // TODO: Consider replacing exception messages with localized messages

  private static final String HEADER_MAGIC = "RPGTLKIT BOARD";
  private static final int HEADER_VERSION_MAJOR = 2;
  private static final int HEADER_VERSION_MINOR = 4;

  @Override
  public int priority() {
    return 1; // not our first choice
  }

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI());
    return ext.endsWith(CoreProperties.getProperty("toolkit.board.extension.legacy"));
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return serializable(descriptor);
  }

  @Override
  public void serialize(AssetHandle handle)
    throws IOException, AssetException {

    throw new NotImplementedException();

  }

  @Override
  public void deserialize(AssetHandle handle)
    throws IOException, AssetException {

    try (final ReadableByteChannel channel = handle.read()) {

      final Board board = new Board(handle.getDescriptor());
      final ByteBuffer buffer = ByteBuffer.allocate((int) handle.size());

      channel.read(buffer);

      buffer.rewind();
      buffer.order(ByteOrder.LITTLE_ENDIAN);

      // Read and validate the file header

      final String header = ByteBufferHelper.getTerminatedString(buffer);
      final int versionMajor = buffer.getShort();
      final int versionMinor = buffer.getShort();

      checkVersion(header, versionMajor, versionMinor);

      // Read board dimensions and geometric perspective

      board.setWidth(buffer.getShort());
      board.setHeight(buffer.getShort());
      board.setLayerCount(buffer.getShort());
      board.setPerspective(parsePerspective(buffer.getShort()));

      // Read the tile lookup table (LUT). Boards contain an array (LUT) of filesystem paths to a tile source
      // such as an individual tile image or a specific tile in a tileset. This avoids the cost
      // and redundancy of storing the tile source path along with every instance of a tile.

      final int lookupTableSize = buffer.getShort();
      buffer.get(); // skip

      for (int i = 0; i < lookupTableSize; i++) {
        final String tileName = ByteBufferHelper.getTerminatedString(buffer);
        board.getTileNameIndex().add(tileName);
      }

      // Read the tile matrix
      //
      // The tile matrix is either non-compressed with each index corresponding to a specific tile in
      // a square grid, or compressed with a form of run-length encoding (RLE).

      final int[][][] tiles = board.getBoardDimensions();

      int size = board.getWidth() * board.getHeight() * board.getLayerCount();
      int offset = 0 ;
      int x = 0;
      int y = 0;
      int z = 0;

      while (offset < size) {

        int index = buffer.getShort();    // The tile index in LUT
        int count = 1;                    // The number of times the tile repeats

        // An negative index implies RLE compression. Invert the index to retrieve the number of
        // repetitions and then read the tile index we need to repeat.

        if (index < 0) {
          count = -index;
          index = buffer.getShort();
        }

        // Place a tile (index) up to (count) times

        for (int i = 0; i < count; i++) {

          tiles[x][y][z] = index;

          // Move on to the next tile position

          offset++;

          // Increment the horizontal position. If we've reached the board's width then move on to the next
          // row of tiles. If we've reached the board's height then move to the next layer. For each movement
          // we reset the corresponding axis to 0 to avoid moving beyond the bounds of the board.

          x++;
          if (x == board.getWidth()) {
            x = 0;
            y++;
            if (y == board.getHeight()) {
              y = 0;
              z++;
            }
          }

        }
      }

      buffer.getShort(); // skip (ubShading)

      // Read shading layers

      final int shadingLayerIndex = buffer.getShort();
      final int shadingLayerTileCount = board.getWidth() * board.getHeight();

      int shadingLayerOffset = 0;

      while (shadingLayerOffset < shadingLayerTileCount) {

        final int count = buffer.getShort();
        final int r = buffer.get();
        final int g = buffer.get();
        final int b = buffer.get();

        final BoardLayerShade shade = new BoardLayerShade(r, g, b, count);
        board.getTileShading().add(shade);

        shadingLayerOffset += count;

      }

      // Read lights

      final int lightCount = buffer.getShort();

      for (int i = 0; i < lightCount; i++) {

        final BoardLight light = new BoardLight();

        light.setLayer(buffer.getInt());
        light.setType(buffer.getInt());

        final int lightPointCount = buffer.getShort() + 1;
        for (int j = 0; j < lightPointCount; j++) {
          final int px = buffer.getInt();
          final int py = buffer.getInt();
          light.addPoint(new Point(px, py));
        }

        final int lightColorCount = buffer.getShort() + 1;
        for (int j = 0; j < lightColorCount; j++) {
          final int r = buffer.getShort();
          final int g = buffer.getShort();
          final int b = buffer.getShort();
          light.addColor(new Color(r, g, b));
        }

        board.getLights().add(light);

      }

      // Read vectors

      final int vectorCount = buffer.getShort() + 1;
      for (int i = 0; i < vectorCount; i++) {

        final BoardVector vector = new BoardVector();

        final int vectorPointCount = buffer.getShort() + 1;
        for (int j = 0; j < vectorPointCount; j++) {
          final int px = buffer.getInt();
          final int py = buffer.getInt();
          vector.addPoint(px, py);
        }

        vector.setAttributes(buffer.getShort());
        vector.setClosed(buffer.getShort() == 1);
        vector.setLayer(buffer.getShort());
        vector.setTileType(parseTileType(buffer.getShort()));
        vector.setHandle(ByteBufferHelper.getTerminatedString(buffer));

        board.getVectors().add(vector);

      }

      // Read programs

      final int programCount = buffer.getShort() + 1;
      for (int i = 0; i < programCount; i++) {

        final BoardProgram program = new BoardProgram();

        program.setFileName(ByteBufferHelper.getTerminatedString(buffer));
        program.setGraphic(ByteBufferHelper.getTerminatedString(buffer));
        program.setInitialVariable(ByteBufferHelper.getTerminatedString(buffer));
        program.setInitialValue(ByteBufferHelper.getTerminatedString(buffer));
        program.setFinalVariable(ByteBufferHelper.getTerminatedString(buffer));
        program.setFinalValue(ByteBufferHelper.getTerminatedString(buffer));
        program.setActivate(buffer.getShort());
        program.setActivationType(buffer.getShort());
        program.setDistanceRepeat(buffer.getShort());
        program.setLayer(buffer.getShort());

        final BoardVector programVector = new BoardVector();

        final int programVectorPointCount = buffer.getShort() + 1;
        for (int j = 0; j < programVectorPointCount; j++) {
          final int px = buffer.getInt();
          final int py = buffer.getInt();
          programVector.addPoint(px, py);
        }

        programVector.setClosed(buffer.getShort() == 1);
        programVector.setHandle(ByteBufferHelper.getTerminatedString(buffer));

        program.setVector(programVector);

        board.getPrograms().add(program);

      }

      // Read sprites

      final int spriteCount = buffer.getShort() + 1;
      for (int i = 0; i < spriteCount; i++) {

        final BoardSprite sprite = new BoardSprite();

        sprite.setFileName(ByteBufferHelper.getTerminatedString(buffer));
        sprite.setActivationProgram(ByteBufferHelper.getTerminatedString(buffer));
        sprite.setMultitaskingProgram(ByteBufferHelper.getTerminatedString(buffer));
        sprite.setInitialVariable(ByteBufferHelper.getTerminatedString(buffer));
        sprite.setInitialValue(ByteBufferHelper.getTerminatedString(buffer));
        sprite.setFinalVariable(ByteBufferHelper.getTerminatedString(buffer));
        sprite.setFinalValue(ByteBufferHelper.getTerminatedString(buffer));
        sprite.setLoadingVariable(ByteBufferHelper.getTerminatedString(buffer));
        sprite.setLoadingValue(ByteBufferHelper.getTerminatedString(buffer));
        sprite.setActivate(buffer.getShort());
        sprite.setActivationType(buffer.getShort());
        sprite.setX(buffer.getShort());
        sprite.setY(buffer.getShort());
        sprite.setLayer(buffer.getShort());

        buffer.getShort(); // skip (associated vector)

        board.getSprites().add(sprite);

      }

      // Read images

      final int imagesCount = buffer.getShort() + 1;
      for (int i = 0; i < imagesCount; i++) {

        final BoardImage image = new BoardImage();

        image.setFileName(ByteBufferHelper.getTerminatedString(buffer));
        image.setBoundLeft(buffer.getInt());
        image.setBoundTop(buffer.getInt());
        image.setLayer(buffer.getShort());
        image.setDrawType(buffer.getShort());
        image.setTransparentColour(buffer.getInt());

        buffer.getShort(); // skip (transluceny)

        board.getImages().add(image);

      }

      // Read threads

      final int threadCount = buffer.getShort() + 1;
      for (int i = 0; i < threadCount; i++) {
        board.getThreads().add(ByteBufferHelper.getTerminatedString(buffer));
      }

      // Read constants

      final int constantsCount = buffer.getShort() + 1;
      for (int i = 0; i < constantsCount; i++) {
        board.getConstants().add(ByteBufferHelper.getTerminatedString(buffer));
      }

      // Read layer titles

      final int layerTitleCount = board.getLayerCount() + 1;
      for (int i = 0 ; i < layerTitleCount; i++) {
        board.getLayerTitles().add(ByteBufferHelper.getTerminatedString(buffer));
      }

      // Read directional links

      for (int i = 0; i < 4; i++) {
        board.getDirectionalLinks().add(ByteBufferHelper.getTerminatedString(buffer));
      }

      // Read background image

      final BoardImage backgroundImage = new BoardImage();

      backgroundImage.setFileName(ByteBufferHelper.getTerminatedString(buffer));
      backgroundImage.setDrawType(buffer.getInt());
      backgroundImage.setScrollRatio(20); // 1 pixel for every 10 movements

      board.getBackgroundImages().add(backgroundImage);

      board.setBackgroundColour(buffer.getInt());
      board.setBackgroundMusic(ByteBufferHelper.getTerminatedString(buffer));
      board.setFirstRunProgram(ByteBufferHelper.getTerminatedString(buffer));
      board.setBattleBackground(ByteBufferHelper.getTerminatedString(buffer));
      board.setEnemyBattleLevel(buffer.getShort());
      board.setAllowBattles(buffer.getShort() == -1);
      board.setAllowSaving(buffer.getShort() != -1);

      // Read ambient color

      int ar = Math.max(buffer.getShort(), 0);
      int ag = Math.max(buffer.getShort(), 0);
      int ab = Math.max(buffer.getShort(), 0);

      board.setAmbientEffect(new Color(ar, ag, ab));

      board.setStartingPositionX(buffer.getShort());
      board.setStartingPositionY(buffer.getShort());
      board.setStartingLayer(buffer.getShort());

      board.updateTileSetCache();
      board.createLayers();

      // Set board as handle asset

      handle.setAsset(board);

    }

  }

  protected void checkVersion(String header, int major, int minor)
    throws AssetException {
    if (!HEADER_MAGIC.equals(header) || HEADER_VERSION_MAJOR != major || HEADER_VERSION_MINOR != minor) {
      throw new AssetException("unsupported file version");
    }
  }

  protected Board.Perspective parsePerspective(int perspective)
    throws AssetException {
    switch (perspective) {
      case 1:
        return Board.Perspective.ORTHOGONAL;
      case 2:
        return Board.Perspective.ISOMETRIC_STACKED;
      case 6:
        return Board.Perspective.ISOMETRIC_ROTATED;
      default:
        throw new AssetException("unsupported board perspective");
    }
  }

  protected TileType parseTileType(int type)
    throws AssetException {
    switch (type) {
      case 0:
        return TileType.NORMAL;
      case 1:
        return TileType.SOLID;
      case 2:
        return TileType.UNDER;
      case 4:
        return TileType.UNDIRECTIONAL;
      case 8:
        return TileType.STAIRS;
      case 16:
        return TileType.WAYPOINT;
      default:
        return TileType.NULL;
    }
  }

}
