/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import net.rpgtoolkit.common.assets.Animation;
import net.rpgtoolkit.common.assets.AnimationFrame;
import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.io.Paths;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author Joshua Michael Daly
 */
public class JsonAnimationSerializer extends AbstractJsonSerializer {

  @Override
  public void populate(JSONStringer json, AssetHandle handle) {
    Animation animation = (Animation) handle.getAsset();

    json.key("animationWidth").value(animation.getAnimationWidth());
    json.key("animationHeight").value(animation.getAnimationHeight());
    json.key("soundEffect").value(animation.getSoundEffect());
    json.key("frameRate").value(animation.getFrameRate());

    json.key("frames").array();
    ArrayList<AnimationFrame> frames = animation.getFrames();
    for (AnimationFrame frame : frames) {
      json.value(frame.getFrameName());
    }
    json.endArray();
  }

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI().getPath());
    return (ext.contains(".anm.json"));
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return serializable(descriptor);
  }

  @Override
  public void deserialize(AssetHandle handle) throws IOException, AssetException {
    JSONObject json = load(handle);
    final Animation asset = new Animation(new File(handle.getDescriptor().getURI()));
    harvest(json, asset);
    handle.setAsset(asset);
  }

  private void harvest(JSONObject json, Animation animation) {
    animation.setAnimationWidth(json.optLong("animationWidth"));
    animation.setAnimationHeight(json.optLong("animationHeight"));
    animation.setSoundEffect(json.getString("soundEffect"));
    animation.setFramRate(json.getDouble("frameRate"));
    
    JSONArray frames = json.getJSONArray("frames");
    int length = frames.length();
    for (int i = 0; i < length; i++) {
      animation.addFrame(new AnimationFrame(frames.getString(i), 0, ""));
    }
  }

}
