/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.util.ArrayList;

public class Enemy extends AbstractSprite {
  
  private long hitPoints;
  private long magicPoints;
  private long fightPower;
  private long defencePower;
  private boolean canRunAway;
  private int sneakChance;
  private int surpriseChance;
  private ArrayList<String> specialMoves;
  private ArrayList<String> weaknesses;
  private ArrayList<String> strengths;
  //TODO: replace aiLevel with something more semantically appropriate than a byte
  private byte aiLevel;  //0-3, inclusive
  private boolean useRPGCodeTatics;
  private String tacticsFile;
  private long experienceAwarded;
  private long goldAwarded;
  private String beatEnemyProgram;
  private String runAwayProgram;
  
  private long maxHitPoints;
  private long maxMagicPoints;
  private ArrayList<String> statusEffects;

  public Enemy(AssetDescriptor descriptor) {
    super(descriptor);
    specialMoves = new ArrayList<>();
    weaknesses = new ArrayList<>();
    strengths = new ArrayList<>();
    standardGraphics = new ArrayList<>();
    customGraphics = new ArrayList<>();
    customGraphicsNames = new ArrayList<>();
  }

  /**
   * @return the hitPoints
   */
  public long getHitPoints() {
    return hitPoints;
  }

  /**
   * @param hitPoints the hitPoints to set
   */
  public void setHitPoints(long hitPoints) {
    this.hitPoints = hitPoints;
  }

  /**
   * @return the magicPoints
   */
  public long getMagicPoints() {
    return magicPoints;
  }

  /**
   * @param magicPoints the magicPoints to set
   */
  public void setMagicPoints(long magicPoints) {
    this.magicPoints = magicPoints;
  }

  /**
   * @return the fightPower
   */
  public long getFightPower() {
    return fightPower;
  }

  /**
   * @param fightPower the fightPower to set
   */
  public void setFightPower(long fightPower) {
    this.fightPower = fightPower;
  }

  /**
   * @return the defencePower
   */
  public long getDefencePower() {
    return defencePower;
  }

  /**
   * @param defencePower the defencePower to set
   */
  public void setDefencePower(long defencePower) {
    this.defencePower = defencePower;
  }

  /**
   * @return the canRunAway
   */
  public boolean canRunAway() {
    return canRunAway;
  }

  /**
   * @param canRunAway the canRunAway to set
   */
  public void canRunAway(boolean canRunAway) {
    this.canRunAway = canRunAway;
  }

  /**
   * @return the sneakChance
   */
  public int getSneakChance() {
    return sneakChance;
  }

  /**
   * @param sneakChance the sneakChance to set
   */
  public void setSneakChance(int sneakChance) {
    this.sneakChance = sneakChance;
  }

  /**
   * @return the surpriseChance
   */
  public int getSurpriseChance() {
    return surpriseChance;
  }

  /**
   * @param surpriseChance the surpriseChance to set
   */
  public void setSurpriseChance(int surpriseChance) {
    this.surpriseChance = surpriseChance;
  }

  /**
   * @return the specialMoves
   */
  public ArrayList<String> getSpecialMoves() {
    return specialMoves;
  }

  /**
   * @return the weaknesses
   */
  public ArrayList<String> getWeaknesses() {
    return weaknesses;
  }

  /**
   * @return the strengths
   */
  public ArrayList<String> getStrengths() {
    return strengths;
  }

  /**
   * @return the aiLevel
   */
  public byte getAiLevel() {
    return aiLevel;
  }

  /**
   * @param aiLevel the aiLevel to set
   */
  public void setAiLevel(byte aiLevel) {
    if (aiLevel > 3) {
      throw new IllegalArgumentException("AI level must be < 4.");
    }
    this.aiLevel = aiLevel;
  }

  /**
   * @return the useRPGCodeTatics
   */
  public boolean useRPGCodeTatics() {
    return useRPGCodeTatics;
  }

  /**
   * @param useRPGCodeTatics the useRPGCodeTatics to set
   */
  public void useRPGCodeTactics(boolean useRPGCodeTatics) {
    this.useRPGCodeTatics = useRPGCodeTatics;
  }

  /**
   * @return the tacticsFile
   */
  public String getTacticsFile() {
    return tacticsFile;
  }

  /**
   * @param tacticsFile the tacticsFile to set
   */
  public void setTacticsFile(String tacticsFile) {
    this.tacticsFile = tacticsFile;
  }

  /**
   * @return the experienceAwarded
   */
  public long getExperienceAwarded() {
    return experienceAwarded;
  }

  /**
   * @param experienceAwarded the experienceAwarded to set
   */
  public void setExperienceAwarded(long experienceAwarded) {
    this.experienceAwarded = experienceAwarded;
  }

  /**
   * @return the goldAwarded
   */
  public long getGoldAwarded() {
    return goldAwarded;
  }

  /**
   * @param goldAwarded the goldAwarded to set
   */
  public void setGoldAwarded(long goldAwarded) {
    this.goldAwarded = goldAwarded;
  }

  /**
   * @return the beatEnemyProgram
   */
  public String getBeatEnemyProgram() {
    return beatEnemyProgram;
  }

  /**
   * @param beatEnemyProgram the beatEnemyProgram to set
   */
  public void setBeatEnemyProgram(String beatEnemyProgram) {
    this.beatEnemyProgram = beatEnemyProgram;
  }

  /**
   * @return the runAwayProgram
   */
  public String getRunAwayProgram() {
    return runAwayProgram;
  }

  /**
   * @param runAwayProgram the runAwayProgram to set
   */
  public void setRunAwayProgram(String runAwayProgram) {
    this.runAwayProgram = runAwayProgram;
  }

  /**
   * @return the maxHitPoints
   */
  public long getMaxHitPoints() {
    return maxHitPoints;
  }

  /**
   * @param maxHitPoints the maxHitPoints to set
   */
  public void setMaxHitPoints(long maxHitPoints) {
    this.maxHitPoints = maxHitPoints;
  }

  /**
   * @return the maxMagicPoints
   */
  public long getMaxMagicPoints() {
    return maxMagicPoints;
  }

  /**
   * @param maxMagicPoints the maxMagicPoints to set
   */
  public void setMaxMagicPoints(long maxMagicPoints) {
    this.maxMagicPoints = maxMagicPoints;
  }

  /**
   * @return the statusEffects
   */
  public ArrayList<String> getStatusEffects() {
    return statusEffects;
  }

  @Override
  public String toString() {
    return getName();
  }

  public void setCanRunAway(boolean canRunAway) {
    this.canRunAway = canRunAway;
  }

  public void setSpecialMoves(ArrayList<String> specialMoves) {
    this.specialMoves = specialMoves;
  }

  public void setWeaknesses(ArrayList<String> weaknesses) {
    this.weaknesses = weaknesses;
  }

  public void setStrengths(ArrayList<String> strengths) {
    this.strengths = strengths;
  }

  public void setUseRPGCodeTatics(boolean useRPGCodeTatics) {
    this.useRPGCodeTatics = useRPGCodeTatics;
  }

  public void setStatusEffects(ArrayList<String> statusEffects) {
    this.statusEffects = statusEffects;
  }
  
}
