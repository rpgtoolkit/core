/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

import net.rpgtoolkit.common.CorruptAssetException;
import net.rpgtoolkit.common.assets.AbstractAssetSerializer;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.utilities.JSON;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * @author Joel Moore
 */
public abstract class AbstractJsonSerializer
  extends AbstractAssetSerializer {

  public static final String DEFAULT_CHARSET = "UTF-8";

  @Override
  public void serialize(AssetHandle handle)
    throws IOException, AssetException {

    try (final WritableByteChannel channel = handle.write()) {
      final String source = this.toJSONString(handle);
      final ByteBuffer buffer = ByteBuffer.wrap(
        source.getBytes(DEFAULT_CHARSET));
      channel.write(buffer);
    }

  }

  /**
   * Loads a JSON object from disk using the handle provided.
   *
   * @return the JSONObject that was loaded, or null if load failed
   */
  public static JSONObject load(AssetHandle handle)
    throws IOException, AssetException {

    try (final ReadableByteChannel channel = handle.read()) {

      final ByteBuffer buffer = ByteBuffer.allocateDirect((int) handle.size());

      channel.read(buffer);
      buffer.position(0);

      final CharBuffer source =
        Charset.forName(DEFAULT_CHARSET).decode(buffer);

      return new JSONObject(source.toString());

    }
    catch (JSONException ex) {

      throw new CorruptAssetException(ex.getMessage());

    }

  }

  /**
   * Creates and populates a JSON object using the asset handle provided.
   */
  public String toJSONString(AssetHandle handle) {
    final JSONStringer builder = new JSONStringer();
    builder.object();
    this.populate(builder, handle);
    builder.endObject();
    return JSON.toPrettyJSON(builder);
  }


  /**
   * Given a JSONStringer that has started making a JSON object and an AssetHandle for an asset this
   * serializer can handle, add the asset's data to that object without ending the object.
   * Subclasses designed to handle a subclass of the asset type should override toJSONString() and
   * populateJSON(), calling both super.populateJSON() and populateJSON() from toJSONString() to add
   * the asset's parent class's info to the JSON object alongside the asset subclass's info.
   * 
   * @param json
   * @param handle
   */
  public abstract void populate(JSONStringer json, AssetHandle handle);

}
