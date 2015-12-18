/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.SpecialMove;
import net.rpgtoolkit.common.io.Paths;

import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author Joel Moore
 * @author Chris Hutchinson
 */
public class JsonSpecialMoveSerializer extends AbstractJsonSerializer {

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI());
    return (ext.contains("spc.json"));
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return this.serializable(descriptor);
  }

  @Override
  public void load(AssetHandle handle, JSONObject json) {

    final SpecialMove smove = new SpecialMove(handle.getDescriptor());

    smove.setName(json.optString("name"));
    smove.setDescription(json.optString("description"));
    smove.setFightPower(json.optLong("fightPower"));
    smove.setMpCost(json.optLong("mpCost"));
    smove.setMpDrainedFromTarget(json.optLong("mpDrainedFromTarget"));
    smove.setCanUseInBattle(json.optBoolean("canUseInBattle"));
    smove.setCanUseInMenu(json.optBoolean("canUseInMenu"));

    smove.setRpgcodeProgram(AssetDescriptor.parse(json.optString("script")));
    smove.setAssociatedStatusEffect(AssetDescriptor.parse(json.optString("statusEffect")));
    smove.setAssociatedAnimation(AssetDescriptor.parse(json.optString("animation")));

    handle.setAsset(smove);

  }

  @Override
  public void store(AssetHandle handle, JSONObject json) {

    final SpecialMove smove = (SpecialMove) handle.getAsset();

    json.put("name", smove.getName());
    json.put("description", smove.getDescription());
    json.put("fightPower", smove.getFightPower());
    json.put("mpCost", smove.getMpCost());
    json.put("mpDrainedFromTarget", smove.getMpDrainedFromTarget());
    json.put("canUseInBattle", smove.getCanUseInBattle());
    json.put("canUseInMenu", smove.getCanUseInMenu());

    json.put("script", smove.getRpgcodeProgram());
    json.put("statusEffect", smove.getAssociatedStatusEffect());
    json.put("animation", smove.getAssociatedAnimation());

  }

}
