/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import java.io.File;
import net.rpgtoolkit.common.assets.Animation;
import net.rpgtoolkit.common.assets.AnimationFrame;
import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.io.Paths;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Joshua Michael Daly
 */
public class JsonAnimationSerializer extends AbstractJsonSerializer {

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI());
    return (ext.endsWith(".anm.json"));
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return serializable(descriptor);
  }

  @Override
  protected void load(AssetHandle handle, JSONObject json) throws AssetException {
    final Animation animation = new Animation(new File(handle.getDescriptor().getURI()));
    
    animation.setAnimationWidth(json.optLong("animationWidth"));
    animation.setAnimationHeight(json.optLong("animationHeight"));
    animation.setSoundEffect(json.getString("soundEffect"));
    animation.setFramRate(json.getDouble("frameRate"));
    
    JSONArray frames = json.getJSONArray("frames");
    int length = frames.length();
    for (int i = 0; i < length; i++) {
      animation.addFrame(new AnimationFrame(frames.getString(i), 0, ""));
    }
    
    handle.setAsset(animation);
  }

  @Override
  public void store(AssetHandle handle, JSONObject json) {
    Animation animation = (Animation) handle.getAsset();

    json.put("animationWidth", animation.getAnimationWidth());
    json.put("animationHeight", animation.getAnimationHeight());
    json.put("soundEffect", animation.getSoundEffect());
    json.put("frameRate", animation.getFrameRate());

    final JSONArray frames = new JSONArray();
    for (AnimationFrame frame : animation.getFrames()) {
      frames.put(frame.getFrameName());
    }
    json.put("frames", frames);
  }

}
