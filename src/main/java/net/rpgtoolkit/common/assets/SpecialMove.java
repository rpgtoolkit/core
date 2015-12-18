/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

public class SpecialMove extends AbstractAsset {

  private String name;
  private String description;
  private AssetDescriptor rpgcodeProgram;
  private AssetDescriptor associatedStatusEffect;
  private AssetDescriptor associatedAnimation;
  private long fightPower;
  private long mpCost;
  private long mpDrainedFromTarget;
  private boolean canUseInBattle;
  private boolean canUseInMenu;

  public SpecialMove(AssetDescriptor descriptor) {
    super(descriptor);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getFightPower() {
    return fightPower;
  }

  public void setFightPower(long fightPower) {
    this.fightPower = fightPower;
  }

  public long getMpCost() {
    return mpCost;
  }

  public void setMpCost(long mpCost) {
    this.mpCost = mpCost;
  }

  public AssetDescriptor getRpgcodeProgram() {
    return rpgcodeProgram;
  }

  public void setRpgcodeProgram(AssetDescriptor rpgcodeProgram) {
    this.rpgcodeProgram = rpgcodeProgram;
  }

  public long getMpDrainedFromTarget() {
    return mpDrainedFromTarget;
  }

  public void setMpDrainedFromTarget(long mpDrainedFromTarget) {
    this.mpDrainedFromTarget = mpDrainedFromTarget;
  }

  public boolean getCanUseInBattle() {
    return canUseInBattle;
  }

  public void setCanUseInBattle(boolean canUseInBattle) {
    this.canUseInBattle = canUseInBattle;
  }

  public boolean getCanUseInMenu() {
    return canUseInMenu;
  }

  public void setCanUseInMenu(boolean canUseInMenu) {
    this.canUseInMenu = canUseInMenu;
  }

  public AssetDescriptor getAssociatedStatusEffect() {
    return associatedStatusEffect;
  }

  public void setAssociatedStatusEffect(AssetDescriptor associatedStatusEffect) {
    this.associatedStatusEffect = associatedStatusEffect;
  }

  public AssetDescriptor getAssociatedAnimation() {
    return associatedAnimation;
  }

  public void setAssociatedAnimation(AssetDescriptor associatedAnimation) {
    this.associatedAnimation = associatedAnimation;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return getName();
  }

  @Override
  public void reset() {
    this.name = "";
    this.description = "";
    this.mpCost = 0;
    this.fightPower = 0;
    this.rpgcodeProgram = null;
    this.mpDrainedFromTarget = 0;
    this.associatedStatusEffect = null;
    this.associatedAnimation = null;
    this.canUseInBattle = false;
    this.canUseInMenu = false;
  }
}
