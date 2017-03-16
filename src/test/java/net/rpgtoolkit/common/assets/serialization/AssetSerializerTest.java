/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization;

import java.util.Arrays;
import java.util.List;
import net.rpgtoolkit.common.assets.Animation;
import net.rpgtoolkit.common.assets.AnimationFrame;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Some pretty basic checks to ensure the file serializers work.
 * 
 * @author Joshua Michael Daly
 */
public class AssetSerializerTest {
    
    @BeforeClass
    public static void beforeClass() {
        System.setProperty("project.path", "src/test/resources");
    }

    @Test
    public void testAnimationSerializier() throws Exception {
        String path = AssetSerializerTestHelper.getPath(
                "Animations/Hero_world_attack_north.animation");
        JsonAnimationSerializer serializer = new JsonAnimationSerializer();

        // Deserialize original.
        Animation asset = AssetSerializerTestHelper.deserializeFile(path, serializer);
        checkAnimation(asset);
        
        // Serialize a temporary version and deserialize it.
        path = AssetSerializerTestHelper.serialize(asset, serializer);
        asset = AssetSerializerTestHelper.deserializeFile(path, serializer);
        checkAnimation(asset);
    }

    private void checkAnimation(Animation asset) {
        List<String> frames = Arrays.asList(
                "Idle_north.png", 
                "attack1_north.png", 
                "attack2_north.png");
        
        List<AnimationFrame> actualFrames =  asset.getFrames();
        for (int i = 0; i < frames.size(); i++) {
            Assert.assertEquals(frames.get(i), actualFrames.get(i).getFrameName());
        }
        
        Assert.assertEquals(0.2, asset.getFrameRate(), 0);
        Assert.assertEquals(90, asset.getAnimationHeight());
        Assert.assertEquals(55, asset.getAnimationWidth());
        Assert.assertEquals("hit.wav", asset.getSoundEffect());
    }

    @Test
    public void testBoardSerializier() throws Exception {
        
    }

    @Test
    public void testCharacterSerializer() throws Exception {
        
    }

    @Test
    public void testEnemySerialzier() throws Exception {
        
    }

    @Test
    public void testItemSerializer() throws Exception {
        
    }

}
