/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import java.io.File;
import java.io.IOException;

import net.rpgtoolkit.common.CorruptAssetException;
import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.SpecialMove;
import net.rpgtoolkit.common.io.Paths;

import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author Joel Moore
 */
public class JsonSMoveSerializer extends AbstractJsonSerializer {

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
  public void deserialize(AssetHandle handle)
    throws IOException, AssetException {

    final JSONObject json = this.load(handle);
    final SpecialMove asset = new SpecialMove(
      new File(handle.getDescriptor().getURI()));
    this.harvest(json, asset);

    handle.setAsset(asset);

  }

  @Override
  public void populate(JSONStringer json, AssetHandle handle) {
    final SpecialMove smove = (SpecialMove) handle.getAsset();
    json.key("name").value(smove.getName());
    json.key("description").value(smove.getDescription());
    json.key("mpCost").value(smove.getMpCost());
    json.key("fightPower").value(smove.getFightPower());
    json.key("rpgcodeProgram").value(smove.getRpgcodeProgram());
    json.key("mpDrainedFromTarget").value(smove.getMpDrainedFromTarget());
    json.key("associatedStatusEffect").value(smove.getAssociatedStatusEffect());
    json.key("associatedAnimation").value(smove.getAssociatedAnimation());
    json.key("canUseInBattle").value(smove.getCanUseInBattle());
    json.key("canUseInMenu").value(smove.getCanUseInMenu());
  }

  //separate from deserialize() to make it easier for inheritance
  public void harvest(JSONObject json, SpecialMove smove) {
    smove.setName(json.optString("name"));
    smove.setDescription(json.optString("description"));
    smove.setMpCost(json.optLong("mpCost"));
    smove.setFightPower(json.optLong("fightPower"));
    smove.setRpgcodeProgram(json.optString("rpgcodeProgram"));
    smove.setMpDrainedFromTarget(json.optLong("mpDrainedFromTarget"));
    smove.setAssociatedStatusEffect(json.optString("associatedStatusEffect"));
    smove.setAssociatedAnimation(json.optString("associatedAnimation"));
    smove.setCanUseInBattle(json.optBoolean("canUseInBattle"));
    smove.setCanUseInMenu(json.optBoolean("canUseInMenu"));
  }

}
