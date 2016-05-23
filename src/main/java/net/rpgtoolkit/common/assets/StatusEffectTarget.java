package net.rpgtoolkit.common.assets;

/**
 * Defines the target of a status effect invocation.
 */
public enum StatusEffectTarget {

    /**
     * Effect can target any entity.
     */
    ANY,

    /**
     * Effect targets a player.
     */
    SELF,

    /**
     * Effect targets an enemy.
     */
    ENEMY

}
