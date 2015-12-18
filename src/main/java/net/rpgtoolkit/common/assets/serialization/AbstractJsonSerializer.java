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
 * Abstract base class for implementing asset serializers that load or store
 * their contents using JSON encoding.
 *
 * @author Joel Moore
 * @author Chris Hutchinson
 */
public abstract class AbstractJsonSerializer
  extends AbstractAssetSerializer {

  public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

  @Override
  public void serialize(AssetHandle handle)
    throws IOException, AssetException {

    try (final WritableByteChannel channel = handle.write()) {

      // Store the asset contents into a JSON representation

      final JSONObject obj = new JSONObject();
      store(handle, obj);

      // Encode JSON representation with the specified character set encoding

      final String contents = obj.toString();
      final ByteBuffer encodedContents = DEFAULT_CHARSET.encode(contents);

      // Write encoded buffer to the channel

      channel.write(encodedContents);

    }

  }

  @Override
  public void deserialize(AssetHandle handle)
    throws IOException, AssetException {

    try (final ReadableByteChannel channel = handle.read()) {

      // Read the asset contents into a buffer

      final ByteBuffer buffer = ByteBuffer.allocateDirect((int) handle.size());
      channel.read(buffer);
      buffer.position(0);

      // Decode and parse the contents as JSON using the specified
      // character set encoding

      final CharBuffer source = DEFAULT_CHARSET.decode(buffer);
      final JSONObject obj = new JSONObject(source.toString());

      // Load asset from the decoded JSON

      load(handle, obj);

    }

  }

  protected abstract void load(AssetHandle handle, JSONObject json)
    throws AssetException;

  protected abstract void store(AssetHandle handle, JSONObject json)
    throws AssetException;

}
