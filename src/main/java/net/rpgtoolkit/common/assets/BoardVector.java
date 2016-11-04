/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import net.rpgtoolkit.common.Selectable;

/**
 *
 * TT_NULL = -1 'To denote empty slot in editor. TT_NORMAL = 0 'See TILE_TYPE enumeration, board
 * conversion.h TT_SOLID = 1 TT_UNDER = 2 TT_UNIDIRECTIONAL = 4 'Incomplete / unnecessary. TT_STAIRS
 * = 8 TT_WAYPOINT = 16
 *
 * @author Geoff Wilson
 * @author Joshua Michael Daly
 */
public class BoardVector implements Cloneable, Selectable {

  // Appears in the TableModel.
  private int layer;                  //layer the vector is on
  private int attributes;             //???
  private boolean isClosed;           //whether the vector is closed
  private String handle;              //vector's handle
  private TileType tileType;

  private ArrayList<Point> points;    //the points in the vector
  private Polygon polygon;
  private boolean selected;

  /**
   *
   */
  public BoardVector() {
    layer = 0;
    attributes = 0;
    isClosed = false;
    points = new ArrayList<>();
    handle = "";
    tileType = TileType.SOLID;
    polygon = new Polygon();
    selected = false;
  }

  /**
   *
   * @return
   */
  public ArrayList<Point> getPoints() {
    return points;
  }

  /**
   *
   * @return
   */
  public TileType getTileType() {
    return tileType;
  }

  /**
   *
   * @return
   */
  public int getPointCount() {
    return points.size();
  }

  /**
   *
   * @param index
   * @return
   */
  public int getPointX(int index) {
    return (int) points.get(index).getX();
  }

  /**
   *
   * @param index
   * @return
   */
  public int getPointY(int index) {
    return (int) points.get(index).getY();
  }

  /**
   *
   * @return
   */
  public int getLayer() {
    return (layer);
  }

  /**
   *
   * @return
   */
  public int getAttributes() {
    return (attributes);
  }

  /**
   *
   * @return
   */
  public String getHandle() {
    return (handle);
  }
  
  /**
   * 
   * @return 
   */
  public double getWidth() {
    return polygon.getBounds().getWidth();
  }
  
  public double getHeight() {
    return polygon.getBounds().getHeight();
  }

  /**
   *
   * @return
   */
  public boolean isClosed() {
    return (isClosed);
  }

  /**
   *
   * @param xVal
   * @param yVal
   */
  public void addPoint(long xVal, long yVal) {
    points.add(new Point((int) xVal, (int) yVal));
    polygon.addPoint((int) xVal, (int) yVal);
  }

  /**
   *
   * @param layer
   */
  public void setLayer(int layer) {
    this.layer = layer;
  }

  /**
   *
   * @param attributes
   */
  public void setAttributes(int attributes) {
    this.attributes = attributes;
  }

  /**
   *
   * @param closed
   */
  public void setClosed(boolean closed) {
    isClosed = closed;
  }

  /**
   *
   * @param handle
   */
  public void setHandle(String handle) {
    this.handle = handle;
  }

  /**
   *
   * @param tileType
   */
  public void setTileType(TileType tileType) {
    this.tileType = tileType;
  }

  /**
   *
   * @return
   */
  public Polygon getPolygon() {
    return polygon;
  }

  /**
   *
   * @param isClosed
   */
  public void setIsClosed(boolean isClosed) {
    this.isClosed = isClosed;
  }

  /**
   *
   * @param points
   */
  public void setPoints(ArrayList<Point> points) {
    this.points = points;
  }

  /**
   *
   * @param polygon
   */
  public void setPolygon(Polygon polygon) {
    this.polygon = polygon;
  }

  @Override
  public boolean isSelected() {
    return selected;
  }

  @Override
  public void setSelectedState(boolean state) {
    selected = state;
  }

  /**
   *
   * @return @throws CloneNotSupportedException
   */
  @Override
  public Object clone() throws CloneNotSupportedException {
    super.clone();

    BoardVector clone = new BoardVector();
    clone.layer = layer;
    clone.attributes = attributes;
    clone.handle = handle;
    clone.isClosed = isClosed;
    clone.points = (ArrayList<Point>) points.clone();
    clone.polygon = polygon;
    clone.tileType = tileType;

    return clone;
  }
}
