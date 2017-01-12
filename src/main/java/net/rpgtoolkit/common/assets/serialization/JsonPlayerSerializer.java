/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import java.util.ArrayList;
import java.util.List;
import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.Player;
import net.rpgtoolkit.common.assets.PlayerSpecialMove;
import net.rpgtoolkit.common.io.Paths;
import net.rpgtoolkit.common.utilities.CoreProperties;
import org.json.JSONArray;

import org.json.JSONObject;

/**
 * @author Joshua Michael Daly
 */
public class JsonPlayerSerializer extends AbstractJsonSerializer {

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI());
    return (ext.endsWith(CoreProperties.getFullExtension("toolkit.character.extension.json")));
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return serializable(descriptor);
  }

  @Override
  protected void load(AssetHandle handle, JSONObject json) throws AssetException {
    final Player player = new Player(handle.getDescriptor());

    player.setName(json.getString("name"));
    player.setExpVariableName(json.getString("expVariableName"));
    player.setDpVariableName(json.getString("dpVariableName"));
    player.setFpVariableName(json.getString("fpVariableName"));
    player.setHpVariableName(json.getString("hpVariableName"));
    player.setMaxMPVariableName(json.getString("maxMPVariableName"));
    player.setNameVariableName(json.getString("nameVariableName"));
    player.setMpVariableName(json.getString("mpVariableName"));
    player.setMaxMPVariableName(json.getString("maxMPVariableName"));
    player.setLvlVariableName(json.getString("lvlVariableName"));
    player.setInitialExperience(json.optInt("initialExperience"));
    player.setInitialHP(json.optInt("initialHP"));
    player.setInitialMaxHP(json.optInt("initialMaxHP"));
    player.setInitialDP(json.optInt("initialDP"));
    player.setInitialFP(json.optInt("initialFP"));
    player.setInitialMP(json.optInt("initialMP"));
    player.setInitialMaxMP(json.optInt("initialMaxMP"));
    player.setInitialLevel(json.optInt("initialLevel"));
    player.setProfilePicture(json.getString("profilePicture"));
    player.setSpecialMoveList(deserializePlayerSpecialMoves(json.getJSONArray("specialMoves")));
    player.setSpecialMovesName(json.getString("specialMovesName"));
    player.setHasSpecialMoves(json.getBoolean("hasSpecialMoves"));
    player.setAccessoryNames(getStringArrayList(json.getJSONArray("accessoryNames")));
    player.setArmourTypes(getBooleanArray(json.getJSONArray("armourTypes")));
    player.setLevelType(json.optInt("levelType"));
    player.setExpIncreaseFactor(json.optInt("expIncreaseFactor"));
    player.setMaxLevel(json.optInt("maxLevel"));
    player.setPercentHPIncrease(json.optInt("percentHPIncrease"));
    player.setPercentDPIncrease(json.optInt("percentDPIncrease"));
    player.setPercentFPIncrease(json.optInt("percentFPIncrease"));
    player.setPercentMPIncrease(json.optInt("percentMPIncrease"));
    player.setProgramOnLevelUp(json.getString("programOnLevelUp"));
    player.setLevelUpType((byte) json.optInt("levelUpType"));
    player.setCharacterSize((byte) json.optInt("characterSize"));
    player.setStandardGraphics(getStringArrayList(json.getJSONArray("standardGraphics")));
    player.setStandingGraphics(getStringArrayList(json.getJSONArray("standingGraphics")));
    player.setCustomGraphics(getStringArrayList(json.getJSONArray("customGraphics")));
    player.setCustomGraphicNames(getStringArrayList(json.getJSONArray("customGraphicsNames")));
    player.setIdleTimeBeforeStanding(json.optDouble("idleTimeBeforeStanding"));
    player.setFrameRate(json.optDouble("frameRate"));
    player.setBaseVector(deserializeBoardVector(json.optJSONObject("baseVector")));
    player.setActivationVector(deserializeBoardVector(json.optJSONObject("activationVector")));
    player.setBaseVectorOffset(deserializePoint(json.optJSONObject("baseVectorOffset")));
    player.setActivationVectorOffset(deserializePoint(json.optJSONObject("activationOffset")));

    handle.setAsset(player);
  }

  @Override
  protected void store(AssetHandle handle, JSONObject json)
          throws AssetException {
    final Player player = (Player) handle.getAsset();
    
    json.put("name", player.getName());
    json.put("expVariableName", player.getExpVariableName());
    json.put("dpVariableName", player.getDpVariableName());
    json.put("fpVariableName", player.getFpVariableName());
    json.put("hpVariableName", player.getHpVariableName());
    json.put("maxMPVariableName", player.getMaxMPVariableName());
    json.put("nameVariableName", player.getNameVariableName());
    json.put("mpVariableName", player.getMpVariableName());
    json.put("maxMPVariableName", player.getMaxMPVariableName());
    json.put("lvlVariableName", player.getLvlVariableName());
    json.put("initialExperience", player.getInitialExperience());
    json.put("initialHP", player.getInitialHP());
    json.put("initialMaxHP", player.getInitialMaxHP());
    json.put("initialDP", player.getInitialDP());
    json.put("initialFP", player.getInitialFP());
    json.put("initialMP", player.getInitialMP());
    json.put("initialMaxMP", player.getInitialMaxMP());
    json.put("initialLevel", player.getInitialLevel());
    json.put("profilePicture", player.getProfilePicture());
    json.put("specialMoves", serializePlayerSpecialMoves(player.getSpecialMoveList()));
    json.put("specialMovesName", player.getSpecialMovesName());
    json.put("hasSpecialMoves", player.getHasSpecialMoves());
    json.put("accessoryNames", player.getAccessoryNames());
    json.put("armourTypes", player.getArmourTypes());
    json.put("levelType", player.getLevelType());
    json.put("expIncreaseFactor", player.getExpIncreaseFactor());
    json.put("maxLevel", player.getMaxLevel());
    json.put("percentHPIncrease", player.getPercentHPIncrease());
    json.put("percentDPIncrease", player.getPercentDPIncrease());
    json.put("percentFPIncrease", player.getPercentFPIncrease());
    json.put("percentMPIncrease", player.getPercentMPIncrease());
    json.put("programOnLevelUp", player.getProgramOnLevelUp());
    json.put("characterSize", player.getCharacterSize());
    json.put("standardGraphics", player.getStandardGraphics());
    json.put("standingGraphics", player.getStandingGraphics());
    json.put("customGraphics", player.getCustomGraphics());
    json.put("customGraphicsNames", player.getCustomGraphicsNames());
    json.put("idleTimeBeforeStanding", player.getIdleTimeBeforeStanding());
    json.put("frameRate", player.getFrameRate());
    json.put("baseVector", serializeBoardVector(player.getBaseVector()));
    json.put("activationVector", serializeBoardVector(player.getActivationVector()));
    json.put("baseVectorOffset", serializePoint(player.getBaseVectorOffset()));
    json.put("activationOffset", serializePoint(player.getActivationVectorOffset()));
  }

  private List<JSONObject> serializePlayerSpecialMoves(List<PlayerSpecialMove> moves) {
    List<JSONObject> objects = new ArrayList(moves.size());
    
    for (PlayerSpecialMove move : moves) {
      JSONObject object = new JSONObject();
      object.put("name", move.getName());
      object.put("minExperience", move.getMinExperience());
      object.put("minLevel", move.getMinLevel());
      object.put("conditionVariable", move.getConditionVariable());
      object.put("conditionVariableTest", move.getConditionVariableTest());
      
      objects.add(object);
    }
    
    return objects;
  }
  
  private ArrayList<PlayerSpecialMove> deserializePlayerSpecialMoves(JSONArray array) {
    ArrayList<PlayerSpecialMove> moves = new ArrayList();
    
    PlayerSpecialMove move;
    int length = array.length();
    for (int i = 0; i < length; i++) {
      JSONObject object = array.getJSONObject(i);
      move = new PlayerSpecialMove(
              object.getString("name"),
              object.getInt("minExperience"),
              object.getInt("minLevel"),
              object.getString("conditionVariable"),
              object.getString("conditionVariableTest")
      );
      moves.add(move);
    }
    
    return moves;
  }

}
