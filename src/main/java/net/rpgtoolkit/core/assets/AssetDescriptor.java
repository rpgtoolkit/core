/*
 * Copyright (c) 2015, rpgtoolkit.net & Contributors
 *
 * See LICENSE.md in the distribution for the full license text including,
 * but not limited to, a notice of warranty and distribution rights.
 */
package net.rpgtoolkit.core.assets;

import java.net.URI;

/**
 * Describes the location and type of a game asset. Asset descriptors are used
 * to classify and track game assets.
 *
 * @see Asset
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public class AssetDescriptor {

    public static final String DEFAULT_TYPE = "application/octet-stream";

    protected URI uri;
    protected String type;

    public AssetDescriptor(URI uri, String type) {
        if (uri == null) {
            throw new NullPointerException();
        }
        if (type == null) {
            type = DEFAULT_TYPE;
        }
        this.uri = uri;
        this.type = type;
    }

    public AssetDescriptor(String uri) {
        this(URI.create(uri), DEFAULT_TYPE);
    }

    public URI getURI() {
        return this.uri;
    }

    public String getScheme() {
        return this.uri.getScheme();
    }

    public String getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs == this) {
            return true;
        }
        if (rhs == null) {
            return false;
        }
        if (rhs.getClass() != this.getClass()) {
            return false;
        }
        final AssetDescriptor other = (AssetDescriptor) rhs;
        return this.uri.equals(other.uri);
    }

    @Override
    public int hashCode() {
        return this.uri.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.type, this.uri.toString());
    }

}
