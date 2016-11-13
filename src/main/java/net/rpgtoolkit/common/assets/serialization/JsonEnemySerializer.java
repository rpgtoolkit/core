/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.Enemy;
import net.rpgtoolkit.common.io.Paths;

import org.json.JSONObject;

/**
 * @author Joshua Michael Daly
 */
public class JsonEnemySerializer extends AbstractJsonSerializer {

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI());
    return (ext.endsWith(".ene.json"));
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return serializable(descriptor);
  }

  @Override
  protected void load(AssetHandle handle, JSONObject json) throws AssetException {
    final Enemy enemy = new Enemy(handle.getDescriptor());

    enemy.setName(json.getString("name"));
    enemy.setHitPoints(json.optInt("hitPoints"));
    enemy.setMagicPoints(json.optInt("magicPoints"));
    enemy.setFightPower(json.optInt("fightPower"));
    enemy.setDefencePower(json.optInt("defencePower")); 
    enemy.canRunAway(json.optBoolean("canRunAway"));
    enemy.setSneakChance(json.optInt("sneakChance"));
    enemy.setSurpriseChance(json.optInt("surpriseChance"));
    enemy.setSpecialMoves(getStringArrayList(json.optJSONArray("specialMoves")));
    enemy.setWeaknesses(getStringArrayList(json.optJSONArray("weaknesses")));
    enemy.setStrengths(getStringArrayList(json.optJSONArray("strengths")));
    enemy.setAiLevel((byte) json.optInt("aiLevel"));
    enemy.useRPGCodeTactics(json.optBoolean("useRPGCodeTactics"));
    enemy.setTacticsFile(json.optString("tacticsFile"));
    enemy.setExperienceAwarded(json.optInt("experienceAwarded"));
    enemy.setGoldAwarded(json.optInt("goldAwarded"));
    enemy.setBeatEnemyProgram(json.optString("beatEnemyProgram"));
    enemy.setRunAwayProgram(json.optString("runAwayProgram"));
    enemy.setStandardGraphics(getStringArrayList(json.optJSONArray("standardGraphics")));
    enemy.setCustomGraphics(getStringArrayList(json.optJSONArray("customizedGraphics")));
    enemy.setCustomGraphicNames(getStringArrayList(json.optJSONArray("customizedGraphicsNames")));
    enemy.setMaxHitPoints(json.optInt("maxHitPoints"));
    enemy.setMagicPoints(json.optInt("maxMagicPoints"));
    enemy.setStatusEffects(getStringArrayList(json.optJSONArray("statusEffects")));

    handle.setAsset(enemy);
  }

  @Override
  protected void store(AssetHandle handle, JSONObject json)
          throws AssetException {
    final Enemy enemy = (Enemy) handle.getAsset();

    json.put("name", enemy.getName());
    json.put("hitPoints", enemy.getHitPoints());
    json.put("magicPoints", enemy.getMagicPoints());
    json.put("fightPower", enemy.getFightPower());
    json.put("defencePower", enemy.getDefencePower());
    json.put("canRunAway", enemy.canRunAway());
    json.put("sneakChance", enemy.getSneakChance());
    json.put("surpriseChance", enemy.getSurpriseChance());
    json.put("specialMoves", enemy.getSpecialMoves());
    json.put("weaknesses", enemy.getWeaknesses());
    json.put("strengths", enemy.getStrengths());
    json.put("aiLevel", enemy.getAiLevel());
    json.put("useRPGCodeTactics", enemy.useRPGCodeTatics());
    json.put("tacticsFile", enemy.getTacticsFile());
    json.put("experienceAwarded", enemy.getExperienceAwarded());
    json.put("goldAwarded", enemy.getGoldAwarded());
    json.put("beatEnemyProgram", enemy.getBeatEnemyProgram());
    json.put("runAwayProgram", enemy.getRunAwayProgram());
    json.put("standardGraphics", enemy.getStandardGraphics());
    json.put("customizedGraphics", enemy.getCustomGraphics());
    json.put("customizedGraphicsNames", enemy.getCustomGraphicsNames());
    json.put("maxHitPoints", enemy.getMaxHitPoints());
    json.put("maxMagicPoints", enemy.getMaxMagicPoints());
    json.put("statusEffects", enemy.getStatusEffects());
  }

}
