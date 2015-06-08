/*
 * Copyright (c) 2015, rpgtoolkit.net & Contributors
 *
 * See LICENSE.md in the distribution for the full license text including,
 * but not limited to, a notice of warranty and distribution rights.
 */
package net.rpgtoolkit.core.assets;

/**
 *
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public class Game implements Asset {

    private static final String DEFAULT_NAME = "New Game";

    protected String name;

    public Game() {
        this.name = DEFAULT_NAME;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        this.name = name;
    }

}
