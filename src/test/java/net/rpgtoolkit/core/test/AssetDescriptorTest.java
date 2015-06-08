/*
 * Copyright (c) 2015, rpgtoolkit.net & Contributors
 *
 * See LICENSE.md in the distribution for the full license text including,
 * but not limited to, a notice of warranty and distribution rights.
 */
package net.rpgtoolkit.core.test;

import net.rpgtoolkit.core.assets.AssetDescriptor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for asset descriptors.
 *
 * @see AssetDescriptor
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public class AssetDescriptorTest {

    @Test
    public void testDescriptorEquality() {

        final String path = "file:///config.xml";
        final AssetDescriptor x = new AssetDescriptor(path);
        final AssetDescriptor y = new AssetDescriptor(path);

        Assert.assertEquals(x, y);
        Assert.assertEquals(x.hashCode(), y.hashCode());

    }

}
