/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
//package net.rpgtoolkit.common.assets.serialization.legacy;
//
//import net.rpgtoolkit.common.assets.AssetDescriptor;
//import net.rpgtoolkit.common.assets.AssetSerializer;
//import net.rpgtoolkit.common.assets.StatusEffect;
//import net.rpgtoolkit.common.assets.StatusEffectAttribute;
//import net.rpgtoolkit.common.assets.StatusEffectAttributeKind;
//import net.rpgtoolkit.common.assets.StatusEffectTarget;
//import net.rpgtoolkit.common.assets.serialization.AssetSerializerTestHelper;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.io.File;
//
//
//public class TestStatusEffectSerializer {
//
//  @Test
//  public void testSerialize() throws Exception {
//
//    final AssetSerializer serializer = new LegacyStatusEffectSerializer();
//    final StatusEffect asset = new StatusEffect(
//      AssetDescriptor.parse("file:/sample.ste"));
//
//    asset.setName("Sample");
//    asset.setTarget(StatusEffectTarget.SELF);
//    asset.setProgramEnabled(true);
//    asset.setProgram(AssetDescriptor.parse("file:/sample.prg"));
//
//    asset.getAttributes().add(
//      new StatusEffectAttribute(StatusEffectAttributeKind.SPEED, 1, 10));
//
//    asset.getAttributes().add(
//      new StatusEffectAttribute(StatusEffectAttributeKind.SPEED, 1, -10));
//
//    asset.getAttributes().add(
//      new StatusEffectAttribute(StatusEffectAttributeKind.DISABLE, 1, 1));
//
//    asset.getAttributes().add(
//      new StatusEffectAttribute(StatusEffectAttributeKind.HP, 1, 25));
//
//    asset.getAttributes().add(
//      new StatusEffectAttribute(StatusEffectAttributeKind.SMP, 1, 50));
//
//    final File file = AssetSerializerTestHelper.serialize(asset, serializer);
//
//    Assert.assertTrue(file.exists());
//    Assert.assertEquals(56, file.length());
//
//  }
//
//  @Test
//  public void testDeserialize() throws Exception {
//
//    Assert.fail("not implemented yet");
//
//  }
//
//}
