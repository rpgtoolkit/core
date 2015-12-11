/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.Board;
import net.rpgtoolkit.common.assets.BoardProgram;
import net.rpgtoolkit.common.assets.BoardSprite;
import net.rpgtoolkit.common.assets.BoardVector;
import net.rpgtoolkit.common.io.Paths;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * @author Joshua Michael Daly
 */
public class JsonBoardSerializer extends AbstractJsonSerializer {

  @Override
  public void populate(JSONStringer json, AssetHandle handle) {
    Board board = (Board) handle.getAsset();
    board.updateBoardIO();

    json.key("width").value(board.getWidth());
    json.key("height").value(board.getHeight());
    json.key("layerCount").value(board.getLayerCount());

    // Tile names e.g. default.tst1 etc.
    json.key("tileNames").array();
    for (String tile : board.getTileNameIndex()) {
      json.value(tile);
    }
    json.endArray();

    // Tile indexs in the look up table, compression is used to save space.
    json.key("tileIndex").array();
    for (Integer index : board.getCompressedTileIndex()) {
      json.value(index);
    }
    json.endArray();

    // Vectors.
    json.key("vectors").array();
    for (BoardVector vector : board.getVectors()) {
      json.object();
      json.key("points").array();
      for (Point point : vector.getPoints()) {
        json.object();
        json.key("x").value(point.x);
        json.key("y").value(point.y);
        json.endObject();
      }
      json.endArray();
      json.key("attributes").value(vector.getAttributes());
      json.key("isClosed").value(vector.isClosed());
      json.key("layer").value(vector.getLayer());
      json.key("tileType").value(vector.getTileType());
      json.key("handle").value(vector.getHandle());
      json.endObject();
    }
    json.endArray();

    // Programs.
    json.key("programs").array();
    for (BoardProgram program : board.getPrograms()) {
      json.object();
      json.key("fileName").value(program.getFileName());
      json.key("graphic").value(program.getGraphic());
      json.key("initialVariable").value(program.getInitialVariable());
      json.key("initialValue").value(program.getInitialValue());
      json.key("finalVariable").value(program.getFinalVariable());
      json.key("finalValue").value(program.getFinalValue());
      json.key("activate").value(program.getActivate());
      json.key("activationType").value(program.getActivationType());
      json.key("distanceRepeat").value(program.getDistanceRepeat());
      json.key("layer").value(program.getLayer());

      json.key("points").array();
      for (Point point : program.getVector().getPoints()) {
        json.object();
        json.key("x").value(point.x);
        json.key("y").value(point.y);
        json.endObject();
      }
      json.endArray();

      json.key("isClosed").value(program.getVector().isClosed());
      json.key("handle").value(program.getVector().getHandle());
      json.endObject();
    }
    json.endArray();

    // Sprites.
    json.key("sprites").array();
    for (BoardSprite sprite : board.getSprites()) {
      json.object();
      json.key("fileName").value(sprite.getFile());
      json.key("activationProgram").value(sprite.getActivationProgram());
      json.key("multitaskingProgram").value(sprite.getMultitaskingProgram());
      json.key("initialVariable").value(sprite.getInitialVariable());
      json.key("initialValue").value(sprite.getInitialValue());
      json.key("finalVariable").value(sprite.getFinalVariable());
      json.key("finalValue").value(sprite.getFinalValue());
      json.key("loadingVariable").value(sprite.getLoadingVariable());
      json.key("loadingValue").value(sprite.getLoadingValue());
      json.key("activate").value(sprite.getActivate());
      json.key("activationType").value(sprite.getActivationType());
      json.key("x").value(sprite.getX());
      json.key("y").value(sprite.getY());
      json.key("layer").value(sprite.getLayer());
      json.endObject();
    }
    json.endArray();

    // Layer Titles.
    json.key("layerTitles").array();
    for (String title : board.getLayerTitles()) {
      json.value(title);
    }
    json.endArray();

    // Directional Links.
    json.key("directionalLinks").array();
    for (String link : board.getDirectionalLinks()) {
      json.value(link);
    }
    json.endArray();

    json.key("backgroundMusic").value(board.getBackgroundMusic());
    json.key("firstRunProgram").value(board.getFirstRunProgram());

    // Ambient Effect.
    json.key("ambientEffect").object();
    json.key("red").value(board.getAmbientEffect().getRed());
    json.key("green").value(board.getAmbientEffect().getGreen());
    json.key("blue").value(board.getAmbientEffect().getBlue());
    json.endObject();

    json.key("startingPositionX").value(board.getStartingPositionX());
    json.key("startingPositionY").value(board.getStartingPositionY());
    json.key("startingLayer").value(board.getStartingLayer());
  }

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI());
    return (ext.contains(".brd.json"));
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return serializable(descriptor);
  }

  @Override
  public void deserialize(AssetHandle handle) throws IOException, AssetException {
    JSONObject json = load(handle);
    final Board asset = new Board(new File(handle.getDescriptor().getURI()));
    harvest(json, asset);
 
    handle.setAsset(asset);
  }

  private void harvest(JSONObject json, Board board) {
    board.setWidth(json.optInt("width"));
    board.setHeight(json.optInt("height"));
    board.setLayerCount(json.optInt("layerCount"));
    board.setBoardDimensions(new int[board.getWidth()][board.getHeight()][board.getLayerCount()]);
    board.getTileNameIndex(getStringArrayList(json.optJSONArray("tileNames")));
    board.setCompressedTileIndex(getTileIndices(json.optJSONArray("tileIndex")));
    board.setVectors(getVectors(json.optJSONArray("vectors")));
    board.setPrograms(getPrograms(json.optJSONArray("programs")));
    board.setSprites(getSprites(json.optJSONArray("sprites")));
    board.setLayerTitles(getStringArrayList(json.optJSONArray("layerTitles")));
    board.setDirectionalLinks(getStringArrayList(json.optJSONArray("directionalLinks")));
    board.setBackgroundMusic(json.getString("backgroundMusic"));
    board.setFirstRunProgram(json.getString("firstRunProgram"));
    board.setAmbientEffect(getColor(json.getJSONObject("ambientEffect")));
    board.setStartingPositionX(json.getInt("startingPositionX"));
    board.setStartingPositionY(json.getInt("startingPositionY"));
    board.setStartingLayer(json.getInt("startingLayer"));
    board.updateTileSetCache();
    board.createLayers();
  }

  private ArrayList<String> getStringArrayList(JSONArray array) {
    ArrayList<String> strings = new ArrayList<>();

    int length = array.length();
    for (int i = 0; i < length; i++) {
      strings.add(array.getString(i));
    }

    return strings;
  }

  private ArrayList<Integer> getTileIndices(JSONArray array) {
    ArrayList<Integer> tileIndices = new ArrayList<>();

    int length = array.length();
    for (int i = 0; i < length; i++) {
      tileIndices.add(array.getInt(i));
    }

    return tileIndices;
  }

  private ArrayList<BoardVector> getVectors(JSONArray array) {
    ArrayList<BoardVector> vectors = new ArrayList<>();

    BoardVector vector;
    int length = array.length();
    for (int i = 0; i < length; i++) {
      JSONObject object = array.getJSONObject(i);
      vector = new BoardVector();
      vector.setPoints(getPoints(object.getJSONArray("points")));
      vector.setAttributes(object.getInt("attributes"));
      vector.setClosed(object.getBoolean("isClosed"));
      vector.setLayer(object.getInt("layer"));
      vector.setTileType(object.getInt("tileType"));
      vector.setHandle(object.getString("handle"));

      vectors.add(vector);
    }

    return vectors;
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

  private ArrayList<BoardSprite> getSprites(JSONArray array) {
    ArrayList<BoardSprite> sprites = new ArrayList<>();

    BoardSprite sprite;
    int length = array.length();
    for (int i = 0; i < length; i++) {
      JSONObject object = array.getJSONObject(i);
      sprite = new BoardSprite();
      sprite.setFileName(object.getString("fileName"));
      sprite.setActivationProgram(object.getString("activationProgram"));
      sprite.setMultitaskingProgram(object.getString("multitaskingProgram"));
      sprite.setInitialVariable(object.getString("initialVariable"));
      sprite.setInitialValue(object.getString("initalValue"));
      sprite.setFinalVariable(object.getString("finalVariable"));
      sprite.setFinalValue(object.getString("finalValue"));
      sprite.setLoadingVariable(object.getString("loadingVariable"));
      sprite.setLoadingValue(object.getString("loadingValue"));
      sprite.setActivate(object.getLong("activate"));
      sprite.setActivationType(object.getLong("activationType"));
      sprite.setX(object.getLong("x"));
      sprite.setY(object.getLong("y"));
      sprite.setLayer(object.getLong("layer"));

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

  private Color getColor(JSONObject object) {
    int r = object.getInt("red");
    int g = object.getInt("green");
    int b = object.getInt("blue");

    return new Color(r, g, b);
  }

}
