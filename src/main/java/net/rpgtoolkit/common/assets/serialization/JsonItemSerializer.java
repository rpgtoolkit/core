package net.rpgtoolkit.common.assets.serialization;

import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetException;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.assets.Item;
import net.rpgtoolkit.common.io.Paths;
import net.rpgtoolkit.common.utilities.CoreProperties;

import org.json.JSONObject;

/**
 * @author Joshua Michael Daly
 */
public class JsonItemSerializer extends AbstractJsonSerializer {

  @Override
  public boolean serializable(AssetDescriptor descriptor) {
    final String ext = Paths.extension(descriptor.getURI());
    return (ext.endsWith(CoreProperties.getFullExtension("toolkit.item.extension.json")));
  }

  @Override
  public boolean deserializable(AssetDescriptor descriptor) {
    return serializable(descriptor);
  }

  @Override
  protected void load(AssetHandle handle, JSONObject json) throws AssetException {
    final Item item = new Item(handle.getDescriptor());

    item.setName(json.getString("name"));
    item.setDescription(json.getString("description"));
    item.setIsEquippable(json.optBoolean("isEquippable"));
    item.setIsMenuDriven(json.optBoolean("isMenuDriven"));
    item.setUsersSpecified(json.optBoolean("usersSpecified"));
    item.setUserChar(getStringArrayList(json.getJSONArray("userChar")));
    item.setBuyPrice(json.optInt("buyPrice"));
    item.setSellPrice(json.optInt("sellPrice"));
    item.setIsKeyItem(json.optBoolean("isKeyItem"));
    item.setEquipLocation(getBooleanArrayList(json.getJSONArray("equipLocation")));
    item.setAccessory(json.getString("accessory"));
    item.setEquipHP(json.optInt("equipHP"));
    item.setEquipDP(json.optInt("equipDP"));
    item.setEquipFP(json.optInt("equipFP"));
    item.setEquipSMP(json.optInt("equipSMP"));
    item.setEquipProgram(json.getString("equipProgram"));
    item.setRemoveProgram(json.getString("removeProgram"));
    item.setMenuHP(json.getInt("menuHP"));
    item.setMenuSMP(json.optInt("menuSMP"));
    item.setMenuProgram(json.getString("menuProgram"));
    item.setFightHP(json.getInt("fightHP"));
    item.setFightSMP(json.getInt("fightSMP"));
    item.setFightProgram(json.getString("fightProgram"));
    item.setFightAnimation(json.getString("fightAnimation"));
    item.setBoardMultitaskProgram(json.getString("boardMultitaskProgram"));
    item.setBoardPickUpProgram(json.getString("boardPickUpProgram"));
    item.setIsWide(json.getBoolean("isWide"));
    item.setStandardGraphics(getStringArrayList(json.getJSONArray("standardGraphics")));
    item.setStandingGraphics(getStringArrayList(json.getJSONArray("standingGraphics")));
    item.setIdleTimeBeforeStanding(json.optDouble("idleTimeBeforeStanding"));
    item.setFrameRate(json.optDouble("frameRate"));
    item.setCustomGraphics(getStringArrayList(json.getJSONArray("customGraphics")));
    item.setCustomGraphicNames(getStringArrayList(json.getJSONArray("customGraphicsNames")));
    item.setBaseVector(deserializeBoardVector(json.optJSONObject("baseVector")));
    item.setActivationVector(deserializeBoardVector(json.optJSONObject("activationVector")));
    item.setBaseVectorOffset(deserializePoint(json.optJSONObject("baseVectorOffset")));
    item.setActivationVectorOffset(deserializePoint(json.optJSONObject("activationOffset")));

    handle.setAsset(item);
  }

  @Override
  protected void store(AssetHandle handle, JSONObject json)
          throws AssetException {
    final Item item = (Item) handle.getAsset();

    json.put("name", item.getName());
    json.put("description", item.getName());
    json.put("isEquippable", item.isIsEquippable());
    json.put("isMenuDriven", item.isIsMenuDriven());
    json.put("usersSpecified", item.getUsersSpecified());
    json.put("userChar", item.getUserChar());
    json.put("buyPrice", item.getBuyPrice());
    json.put("sellPrice", item.getSellPrice());
    json.put("isKeyItem", item.getIsKeyItem());
    json.put("equipLocation", item.getEquipLocation());
    json.put("accessory", item.getAccessory());
    json.put("equipHP", item.getEquipHP());
    json.put("equipDP", item.getEquipDP());
    json.put("equipFP", item.getEquipFP());
    json.put("equipSMP", item.getEquipSMP());
    json.put("equipProgram", item.getEquipProgram());
    json.put("removeProgram", item.getRemoveProgram());
    json.put("menuHP", item.getMenuHP());
    json.put("menuSMP", item.getMenuSMP());
    json.put("menuProgram", item.getMenuProgram());
    json.put("fightHP", item.getFightHP());
    json.put("fightSMP", item.getFightSMP());
    json.put("fightProgram", item.getFightProgram());
    json.put("fightAnimation", item.getFightAnimation());
    json.put("boardMultitaskProgram", item.getBoardMultitaskProgram());
    json.put("boardPickUpProgram", item.getBoardPickUpProgram());
    json.put("isWide", item.isIsWide());
    json.put("standardGraphics", item.getStandardGraphics());
    json.put("standingGraphics", item.getStandingGraphics());
    json.put("idleTimeBeforeStanding", item.getIdleTimeBeforeStanding());
    json.put("frameRate", item.getFrameRate());
    json.put("customGraphics", item.getCustomGraphics());
    json.put("customGraphicsNames", item.getCustomGraphicsNames());
    json.put("baseVector", serializeBoardVector(item.getBaseVector()));
    json.put("activationVector", serializeBoardVector(item.getActivationVector()));
    json.put("baseVectorOffset", serializePoint(item.getBaseVectorOffset()));
    json.put("activationOffset", serializePoint(item.getActivationVectorOffset()));
  }

}