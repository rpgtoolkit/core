/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.Board;
import net.rpgtoolkit.common.assets.BoardProgram;
import net.rpgtoolkit.common.assets.BoardSprite;
import net.rpgtoolkit.common.io.Paths;
import net.rpgtoolkit.common.utilities.CoreProperties;

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

    board.setWidth(json.optInt("width"));
    board.setHeight(json.optInt("height"));
    board.setLayerCount(json.optInt("layerCount"));
    board.setBoardDimensions(new int[board.getWidth()][board.getHeight()][board.getLayerCount()]);
    board.getTileNameIndex(getStringArrayList(json.optJSONArray("tileNames")));
    board.setCompressedTileIndex(getTileIndices(json.optJSONArray("tileIndex")));
    board.setVectors(deserializeBoardVectors(json.optJSONArray("vectors")));
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
    
    handle.setAsset(board);

  }

  @Override
  protected void store(AssetHandle handle, JSONObject json) throws AssetException {
    final Board board = (Board) handle.getAsset();

    board.updateBoardIO();

    json.put("width", board.getWidth());
    json.put("height", board.getHeight());
    json.put("layerCount", board.getLayerCount());

    // Tile names e.g. default.tst1 etc.
    json.put("tileNames", board.getTileNameIndex());
    json.put("tileIndex", board.getCompressedTileIndex());

    final JSONArray vectors = serializeBoardVectors(board.getVectors());
    final JSONArray programs = new JSONArray();
    final JSONArray sprites = new JSONArray();

    // Serialize board programs
    for (final BoardProgram program : board.getPrograms()) {
      final JSONObject p = new JSONObject();

      p.put("fileName", program.getFileName());
      p.put("graphic", program.getGraphic());
      p.put("initialVariable", program.getInitialVariable());
      p.put("initialValue", program.getInitialValue());
      p.put("finalVariable", program.getFinalVariable());
      p.put("finalValue", program.getFinalValue());
      p.put("activate", program.getActivate());
      p.put("activationType", program.getActivationType());
      p.put("distanceRepeat", program.getDistanceRepeat());
      p.put("layer", program.getLayer());

      final JSONArray points = new JSONArray();

      for (final Point point : program.getVector().getPoints()) {
        final JSONObject pt = new JSONObject();
        pt.put("x", point.x);
        pt.put("y", point.y);
        points.put(pt);
      }

      p.put("points", points);
      p.put("isClosed", program.getVector().isClosed());
      p.put("handle", program.getVector().getHandle());

      programs.put(p);
    }

    // Serialize sprites
    for (final BoardSprite sprite : board.getSprites()) {
      final JSONObject s = new JSONObject();
      s.put("fileName", sprite.getFileName());
      s.put("activationProgram", sprite.getActivationProgram());
      s.put("multitaskingProgram", sprite.getMultitaskingProgram());
      s.put("initialVariable", sprite.getInitialVariable());
      s.put("initialValue", sprite.getInitialValue());
      s.put("finalVariable", sprite.getFinalVariable());
      s.put("finalValue", sprite.getFinalValue());
      s.put("loadingVariable", sprite.getLoadingVariable());
      s.put("loadingValue", sprite.getLoadingValue());
      s.put("activate", sprite.getActivate());
      s.put("activationType", sprite.getActivationType());
      s.put("x", sprite.getX());
      s.put("y", sprite.getY());
      s.put("layer", sprite.getLayer());
      sprites.put(s);
    }

    json.put("vectors", vectors);
    json.put("programs", programs);
    json.put("sprites", sprites);
    json.put("layerTitles", board.getLayerTitles());
    json.put("directionalLinks", board.getDirectionalLinks());
    
    json.put("backgroundMusic", board.getBackgroundMusic());
    json.put("firstRunProgram", board.getFirstRunProgram());

    final JSONObject ambientEffect = new JSONObject();
    ambientEffect.put("red", board.getAmbientEffect().getRed());
    ambientEffect.put("green", board.getAmbientEffect().getGreen());
    ambientEffect.put("blue", board.getAmbientEffect().getBlue());

    json.put("ambientEffect", ambientEffect);
    json.put("startingPositionX", board.getStartingPositionX());
    json.put("startingPositionY", board.getStartingPositionY());
    json.put("startingLayer", board.getStartingLayer());

    handle.setAsset(board);
  }

  private ArrayList<Integer> getTileIndices(JSONArray array) {
    ArrayList<Integer> tileIndices = new ArrayList<>();

    int length = array.length();
    for (int i = 0; i < length; i++) {
      tileIndices.add(array.getInt(i));
    }

    return tileIndices;
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
      sprite.setInitialValue(object.getString("initialValue"));
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
