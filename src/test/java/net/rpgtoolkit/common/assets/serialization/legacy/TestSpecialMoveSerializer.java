package net.rpgtoolkit.common.assets.serialization.legacy;

import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetSerializer;
import net.rpgtoolkit.common.assets.SpecialMove;
import net.rpgtoolkit.common.assets.serialization.AssetSerializerTestHelper;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class TestSpecialMoveSerializer {

  @Test
  public void testSerialize() throws Exception {

    final AssetSerializer serializer = new LegacySpecialMoveSerializer();
    final SpecialMove asset = new SpecialMove(
        AssetDescriptor.parse("file:/sample.spc"));

    asset.setName("Sample");
    asset.setFightPower(10);
    asset.setMovePowerDrainedFromTarget(20);
    asset.setMovePowerCost(30);
    asset.setDescription("Sample");
    asset.setProgram(AssetDescriptor.parse("file:/sample.prg"));
    asset.setStatusEffect(AssetDescriptor.parse("file:/sample.ste"));
    asset.setAnimation(AssetDescriptor.parse("file:/sample.anm"));
    asset.isUsableInBattle(true);
    asset.isUsableInMenu(true);

    final File file = AssetSerializerTestHelper.serialize(asset, serializer);

    Assert.assertTrue(file.exists());
    Assert.assertEquals(82, file.length());

  }

  @Test
  public void testDeserialize() throws Exception {

    final AssetSerializer serializer = new LegacySpecialMoveSerializer();
    final SpecialMove asset = AssetSerializerTestHelper.deserializeResource(
        "assets/sample.spc", serializer);

    Assert.assertNotNull(asset);
    Assert.assertEquals("Sample", asset.getName());
    Assert.assertEquals(10, asset.getFightPower());
    Assert.assertEquals(20, asset.getMovePowerDrainedFromTarget());
    Assert.assertEquals(30, asset.getMovePowerCost());
    Assert.assertEquals("Sample", asset.getDescription());
    Assert.assertEquals("file:/sample.prg", asset.getProgram().getURI().toString());
    Assert.assertEquals("file:/sample.ste", asset.getStatusEffect().getURI().toString());
    Assert.assertEquals("file:/sample.anm", asset.getAnimation().getURI().toString());
    Assert.assertEquals(true, asset.isUsableInBattle());
    Assert.assertEquals(false, asset.isUsableInMenu());

  }

}
