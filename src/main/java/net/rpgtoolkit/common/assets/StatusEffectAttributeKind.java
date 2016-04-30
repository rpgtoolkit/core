package net.rpgtoolkit.common.assets;

/**
 * Defines the types of attributes that can be applied to an entity
 * from a status effect.
 */
public enum StatusEffectAttributeKind {

  /**
   * A custom attribute with behavior specific to the game.
   */
  CUSTOM("Custom"),

  /**
   * Affects the target's speed.
   */
  SPEED("Speed"),

  /**
   * Disables the target for a period of time.
   */
  DISABLE("Disable"),

  /**
   * Modifies the target's health power (HP).
   */
  HP("HP"),

  /**
   * Modifies the target's special move power (SMP).
   */
  SMP("SMP");

  private final String name;

  StatusEffectAttributeKind(String name) {
    this.name = name;
  }

}
