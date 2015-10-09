/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets.serialization.legacy;

import net.rpgtoolkit.common.CorruptAssetException;
import net.rpgtoolkit.common.assets.AbstractAssetSerializer;
import net.rpgtoolkit.common.assets.AnimatedTile;
import net.rpgtoolkit.common.assets.AssetDescriptor;
import net.rpgtoolkit.common.assets.AssetHandle;
import net.rpgtoolkit.common.io.BinaryReader;
import net.rpgtoolkit.common.io.Paths;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Path;

public class LegacyAnimatedTileSerializer extends AbstractAssetSerializer {

    private final String HEADER_MAGIC = "RPGTLKIT TILEANIM";
    private final int HEADER_VERSION_MAJOR = 2;
    private final int HEADER_VERSION_MINOR = 0;

    @Override
    public boolean canSerialize(AssetDescriptor descriptor) {
        final String ext = Paths.getExtension(descriptor.getURI().toString());
        return ext.contains(".tan");
    }

    @Override
    public boolean canDeserialize(AssetDescriptor descriptor) {
        return this.canSerialize(descriptor);
    }

    @Override
    public void serialize(AssetHandle handle)
            throws IOException, CorruptAssetException {

    }

    @Override
    public void deserialize(AssetHandle handle)
            throws IOException, CorruptAssetException {

        final BinaryReader reader =
                this.getBinaryReader(ByteOrder.LITTLE_ENDIAN, handle);

        // Read asset header

        final String header = reader.readTerminatedString();
        final int versionMajor = reader.readInt16();
        final int versionMinor = reader.readInt16();

        // Ensure header magic is correct

        if (!header.equals(HEADER_MAGIC)) {
            throw new CorruptAssetException("Invalid asset header");
        }

        if (versionMajor < HEADER_VERSION_MAJOR || versionMinor < HEADER_VERSION_MINOR) {
            throw new CorruptAssetException("Unsupported asset version");
        }

        final AnimatedTile tile = new AnimatedTile(handle.getDescriptor());

        final int fps = reader.readInt32();     // frame duration (ms)
        final int count = reader.readInt32();   // frame count

        for (int i = 0; i < count; ++i) {
            final String frameTarget = reader.readTerminatedString();
            if (frameTarget != null && frameTarget.length() > 0) {
                final AnimatedTile.Frame frame = new AnimatedTile.Frame(
                        new AssetDescriptor("file:///" + frameTarget));
                frame.setDuration(fps);
                tile.getFrames().add(frame);
            }
        }

        handle.setAsset(tile);

    }
}
