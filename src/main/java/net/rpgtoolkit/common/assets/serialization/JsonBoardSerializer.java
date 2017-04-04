/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.Board;
import net.rpgtoolkit.common.assets.BoardLayer;
import net.rpgtoolkit.common.assets.BoardProgram;
import net.rpgtoolkit.common.assets.BoardSprite;
import net.rpgtoolkit.common.assets.BoardVector;
import net.rpgtoolkit.common.io.Paths;
import net.rpgtoolkit.common.utilities.CoreProperties;
import net.rpgtoolkit.common.assets.StartingPosition;
import net.rpgtoolkit.common.assets.Tile;
import net.rpgtoolkit.common.assets.TileSet;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Joshua Michael Daly
 * @author Chris Hutchinson
 */
public class JsonBoardSerializer extends AbstractJsonSerializer {

    @Override
    public boolean serializable(AssetDescriptor descriptor) {
        final String ext = Paths.extension(descriptor.getURI());
        return (ext.equals(CoreProperties.getFullExtension("toolkit.board.extension.json")));
    }

    @Override
    public boolean deserializable(AssetDescriptor descriptor) {
        return serializable(descriptor);
    }

    @Override
    protected void load(AssetHandle handle, JSONObject json) throws AssetException {

        final Board board = new Board(handle.getDescriptor());

        board.setName(json.getString("name"));
        board.setWidth(json.getInt("width"));
        board.setHeight(json.getInt("height"));

        JSONArray tileSets = json.getJSONArray("tileSets");
        List<String> tileSetNames = getStringArrayList(tileSets);
        board.setTileSets(getTileSets(tileSets));

        board.setSprites(getSprites(json.getJSONArray("sprites")));

        board.setLayers(getBoardLayers(json.getJSONArray("layers"), board, tileSetNames));

        JSONObject startingPosition = json.getJSONObject("startingPosition");
        board.setStartingPosition(new StartingPosition(
                startingPosition.getInt("x"),
                startingPosition.getInt("y"),
                startingPosition.getInt("layer"))
        );

        // ADD?
//        board.setDirectionalLinks(getStringArrayList(json.optJSONArray("directionalLinks")));
//        board.setBackgroundMusic(json.getString("backgroundMusic"));
//        board.setFirstRunProgram(json.getString("firstRunProgram"));
        board.setBoardDimensions(new int[board.getWidth()][board.getHeight()][board.getLayers().size()]);

        handle.setAsset(board);

    }

    @Override
    protected void store(AssetHandle handle, JSONObject json) throws AssetException {
        super.store(handle, json);

        final Board board = (Board) handle.getAsset();

        json.put("name", board.getName());
        json.put("width", board.getWidth());
        json.put("height", board.getHeight());

        // Serialize TileSets.
        // Stored in LinkedHashMap which the original insertion order.
        final JSONArray tileSets = new JSONArray();
        for (TileSet tileSet : board.getTileSets().values()) {
            tileSets.put(tileSet.getName());
        }
        json.put("tileSets", tileSets);

        // Serialize sprites
        final JSONArray sprites = new JSONArray();
        for (final BoardSprite sprite : board.getSprites()) {
            final JSONObject s = new JSONObject();
            s.put("name", sprite.getFileName());
            JSONObject spritePosition = new JSONObject();
            spritePosition.put("x", sprite.getX());
            spritePosition.put("y", sprite.getY());
            spritePosition.put("layer", sprite.getLayer());
            s.put("startingPosition", spritePosition);
            sprites.put(s);
        }
        json.put("sprites", sprites);

        // Serialize layers.
        final JSONArray layers = new JSONArray();
        for (BoardLayer boardLayer : board.getLayers()) {
            JSONObject layer = new JSONObject();
            layer.put("name", boardLayer.getName());

            // Tiles.
            int width = board.getWidth();
            int height = board.getHeight();
            JSONArray tiles = new JSONArray();
            Tile[][] layerTiles = boardLayer.getTiles();

            int count = width * height;
            int x = 0;
            int y = 0;
            for (int j = 0; j < count; j++) {
                // Default values for a blank tile.
                int tileSetIndex = -1;
                int tileIndex = -1;

                Tile tile = layerTiles[x][y];
                if (tile.getTileSet() != null) {
                    tileSetIndex = new ArrayList<>(board.getTileSets().keySet())
                            .indexOf(tile.getTileSet().getName());
                    tileIndex = tile.getIndex();
                }

                String tileIndexer = tileSetIndex + ":" + tileIndex;
                tiles.put(tileIndexer);

                x++;
                if (x == width) {
                    x = 0;
                    y++;
                    if (y == height) {
                        break;
                    }
                }
            }
            layer.put("tiles", tiles);

            // Vectors.
            JSONArray vectors = serializeBoardVectors(boardLayer.getVectors());
            layer.put("vectors", vectors);

            layers.put(layer);
        }
        json.put("layers", layers);

        JSONObject startingPosition = new JSONObject();
        startingPosition.put("x", board.getStartingPositionX());
        startingPosition.put("y", board.getStartingPositionY());
        startingPosition.put("layer", board.getStartingLayer());
        json.put("startingPosition", startingPosition);

        handle.setAsset(board);

//                json.put("layerTitles", board.getLayerNames());
//        json.put("directionalLinks", board.getDirectionalLinks());
//        json.put("backgroundMusic", board.getBackgroundMusic());
//        json.put("firstRunProgram", board.getFirstRunProgram());
//
//        final JSONArray programs = new JSONArray();
//        for (final BoardProgram program : board.getPrograms()) {
//            final JSONObject p = new JSONObject();
//
//            p.put("fileName", program.getFileName());
//            p.put("graphic", program.getGraphic());
//            p.put("initialVariable", program.getInitialVariable());
//            p.put("initialValue", program.getInitialValue());
//            p.put("finalVariable", program.getFinalVariable());
//            p.put("finalValue", program.getFinalValue());
//            p.put("activate", program.getActivate());
//            p.put("activationType", program.getActivationType());
//            p.put("distanceRepeat", program.getDistanceRepeat());
//            p.put("layer", program.getLayer());
//
//            final JSONArray points = new JSONArray();
//
//            for (final Point point : program.getVector().getPoints()) {
//                final JSONObject pt = new JSONObject();
//                pt.put("x", point.x);
//                pt.put("y", point.y);
//                points.put(pt);
//            }
//
//            p.put("points", points);
//            p.put("isClosed", program.getVector().isClosed());
//            p.put("handle", program.getVector().getHandle());
//
//            programs.put(p);
//        }
    }

    private ArrayList<Integer> getTileIndices(JSONArray array) {
        ArrayList<Integer> tileIndices = new ArrayList<>();

        int length = array.length();
        for (int i = 0; i < length; i++) {
            tileIndices.add(array.getInt(i));
        }

        return tileIndices;
    }

    private Map<String, TileSet> getTileSets(JSONArray array) {
        Map<String, TileSet> tileSets = new HashMap<>();

        TileSet tileSet;
        int length = array.length();
        for (int i = 0; i < length; i++) {
            tileSet = new TileSet(null);
            String name = array.getString(i);
            tileSet.setName(name);

            tileSets.put(name, tileSet);
        }

        return tileSets;
    }

    private ArrayList<BoardProgram> getPrograms(JSONArray array) {
        ArrayList<BoardProgram> programs = new ArrayList<>();

        BoardProgram program;
        int length = array.length();
        for (int i = 0; i < length; i++) {
            JSONObject object = array.getJSONObject(i);
            program = new BoardProgram();
            program.setFileName(object.getString("fileName"));
            program.setGraphic(object.getString("graphic"));
            program.setInitialVariable(object.getString("initialVariable"));
            program.setInitialValue(object.getString("initialValue"));
            program.setFinalVariable(object.getString("finalVariable"));
            program.setFinalValue(object.getString("finalValue"));
            program.setActivate(object.getLong("activate"));
            program.setActivationType(object.getLong("activationType"));
            program.setDistanceRepeat(object.getLong("distanceRepeat"));
            program.setLayer(object.getLong("layer"));
            program.getVector().setPoints(getPoints(object.getJSONArray("points")));
            program.getVector().setClosed(object.getBoolean("isClosed"));
            program.getVector().setHandle(object.getString("handle"));

            programs.add(program);
        }

        return programs;
    }

    private LinkedList<BoardLayer> getBoardLayers(JSONArray array, Board board, List<String> tileSetNames) {
        LinkedList<BoardLayer> layers = new LinkedList<>();

        int width = board.getWidth();
        int height = board.getHeight();

        BoardLayer layer;
        int length = array.length();
        for (int i = 0; i < length; i++) {
            JSONObject object = array.getJSONObject(i);
            layer = new BoardLayer(board);
            layer.setName(object.getString("name"));
            layer.setNumber(i);

            // Tiles.
            JSONArray tiles = object.getJSONArray("tiles");
            int count = width * height;
            int x = 0;
            int y = 0;
            for (int j = 0; j < count; j++) {
                String[] tileIndexer = tiles.getString(j).split(":");
                int tileSetIndex = Integer.parseInt(tileIndexer[0]);
                int tileIndex = Integer.parseInt(tileIndexer[1]);

                Tile tile = new Tile();
                if (!(tileSetIndex == -1 && tileIndex == -1)) { // Check for blank tile.
                    TileSet tileSet = board.getTileSets().get(tileSetNames.get(tileSetIndex));
                    tile = new Tile(tileSet, tileIndex);
                }

                layer.setTileAt(x, y, tile);

                x++;
                if (x == width) {
                    x = 0;
                    y++;
                    if (y == height) {
                        break;
                    }
                }
            }

            // Vectors.
            JSONArray vectors = object.getJSONArray("vectors");
            ArrayList<BoardVector> boardVectors = deserializeBoardVectors(vectors);
            for (BoardVector boardVector : boardVectors) {
                boardVector.setLayer(i);
            }
            layer.setVectors(boardVectors);

            // Sprites.
            for (BoardSprite sprite : board.getSprites()) {
                if (sprite.getLayer() == i) {
                    layer.getSprites().add(sprite);
                }
            }

            layers.add(layer);
        }

        return layers;
    }

    private ArrayList<BoardSprite> getSprites(JSONArray array) {
        ArrayList<BoardSprite> sprites = new ArrayList<>();

        BoardSprite sprite;
        int length = array.length();
        for (int i = 0; i < length; i++) {
            JSONObject object = array.getJSONObject(i);
            sprite = new BoardSprite();
            sprite.setFileName(object.getString("name"));

            JSONObject startingPosition = object.getJSONObject("startingPosition");
            sprite.setX(startingPosition.getInt("x"));
            sprite.setY(startingPosition.getInt("y"));
            sprite.setLayer(startingPosition.getInt("layer"));

            sprites.add(sprite);
        }

        return sprites;
    }

    private ArrayList<Point> getPoints(JSONArray array) {
        ArrayList<Point> points = new ArrayList<>();

        int length = array.length();
        for (int i = 0; i < length; i++) {
            JSONObject point = array.getJSONObject(i);

            points.add(new Point(point.getInt("x"), point.getInt("y")));
        }

        return points;
    }

}
