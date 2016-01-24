/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.common.assets;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import net.rpgtoolkit.common.CorruptAssetException;
import net.rpgtoolkit.common.utilities.PropertiesSingleton;

public class Player extends AbstractAsset
{
    // Constants

    //TODO: can these be changed to enums or something?
    public static final int DIRECTION_NORTH = 1;
    public static final int DIRECTION_EAST = 2;
    public static final int DIRECTION_SOUTH = 0;
    public static final int DIRECTION_WEST = 3;

    private String name;
    private String expVariableName;
    private String dpVariableName;
    private String fpVariableName;
    private String hpVariableName;
    private String maxHPVariableName;
    private String nameVariableName;
    private String mpVariableName;
    private String maxMPVariableName;
    private String lvlVariableName;
    private long initialExperience;
    private long initialHP;
    private long initialMaxHP;
    private long initialDP;
    private long initialFP;
    private long initialMP;
    private long initialMaxMP;
    private long initialLevel;
    private String profilePicture;
    private ArrayList<PlayerSpecialMove> specialMoveList;
    private String specialMovesName;
    private boolean hasSpecialMoves;
    private ArrayList<String> accessoryNames;
    private boolean armourTypes[] = new boolean[7];
    private long levelType;
    private int expIncreaseFactor;
    private long maxLevel;
    private int percentHPIncrease;
    private int percentMPIncrease;
    private int percentDPIncrease;
    private int percentFPIncrease;
    private String programOnLevelUp;
    private byte levelUpType;
    private byte characterSize;

    // Graphics Variables
    private ArrayList<String> standardGraphics; // 13 Values, S,N,E,W,NW,NE,SW,SE,Att,Def,Spec,Die,Rst
    private ArrayList<Animation> standardGraphicsAnimations;

    // Active animation for the engine
    private Animation activeAnimation;
    private int frameNumber;
    private int frameCount;
    private Timer animationTimer;

    private ArrayList<String> customGraphics;
    private ArrayList<String> customGraphicNames;
    private ArrayList<String> standingGraphics;
    private double idleTimeBeforeStanding;
    private double frameRate; //Seconds between each step
    private long loopSpeed;
    private BoardVector baseVector;
    private BoardVector activationVector;
    private Area baseArea;

    // Engine Variables
    private int currentXLocation;
    private int currentYLocation;
    private int vectorCorrectionX;
    private int vectorCorrectionY;

    /**
     * Opens a player from an existing file
     *
     * @param descriptor Character (.tem) file to open
     */
    public Player(AssetDescriptor descriptor) {
        super(descriptor);
        // Prepare the ArrayLists
        specialMoveList = new ArrayList<>();
        accessoryNames = new ArrayList<>();
        standardGraphics = new ArrayList<>();
        customGraphics = new ArrayList<>();
        customGraphicNames = new ArrayList<>();
        standingGraphics = new ArrayList<>();
        baseVector = new BoardVector();
        activationVector = new BoardVector();
    }

    /**
     * Opens a player from an existing file
     *
     * @param file Character (.tem) file to open
     */
//    public Player(File file)
//    {
//        super(file);
//        System.out.println("Loading Player: " + this.file);
//        this.open();
//    }

    /**
     * Creates a new player (for editor use)
     */
//    public Player()
//    {
//
//    }

    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the expVariableName
     */
    public String getExpVariableName() {
        return expVariableName;
    }

    /**
     * @param expVariableName the expVariableName to set
     */
    public void setExpVariableName(String expVariableName) {
        this.expVariableName = expVariableName;
    }

    /**
     * @return the dpVariableName
     */
    public String getDpVariableName() {
        return dpVariableName;
    }

    /**
     * @param dpVariableName the dpVariableName to set
     */
    public void setDpVariableName(String dpVariableName) {
        this.dpVariableName = dpVariableName;
    }

    /**
     * @return the fpVariableName
     */
    public String getFpVariableName() {
        return fpVariableName;
    }

    /**
     * @param fpVariableName the fpVariableName to set
     */
    public void setFpVariableName(String fpVariableName) {
        this.fpVariableName = fpVariableName;
    }

    /**
     * @return the hpVariableName
     */
    public String getHpVariableName() {
        return hpVariableName;
    }

    /**
     * @param hpVariableName the hpVariableName to set
     */
    public void setHpVariableName(String hpVariableName) {
        this.hpVariableName = hpVariableName;
    }

    /**
     * @return the maxHPVariableName
     */
    public String getMaxHPVariableName() {
        return maxHPVariableName;
    }

    /**
     * @param maxHPVariableName the maxHPVariableName to set
     */
    public void setMaxHPVariableName(String maxHPVariableName) {
        this.maxHPVariableName = maxHPVariableName;
    }

    /**
     * @return the nameVariableName
     */
    public String getNameVariableName() {
        return nameVariableName;
    }

    /**
     * @param nameVariableName the nameVariableName to set
     */
    public void setNameVariableName(String nameVariableName) {
        this.nameVariableName = nameVariableName;
    }

    /**
     * @return the mpVariableName
     */
    public String getMpVariableName() {
        return mpVariableName;
    }

    /**
     * @param mpVariableName the mpVariableName to set
     */
    public void setMpVariableName(String mpVariableName) {
        this.mpVariableName = mpVariableName;
    }

    /**
     * @return the maxMPVariableName
     */
    public String getMaxMPVariableName() {
        return maxMPVariableName;
    }

    /**
     * @param maxMPVariableName the maxMPVariableName to set
     */
    public void setMaxMPVariableName(String maxMPVariableName) {
        this.maxMPVariableName = maxMPVariableName;
    }

    /**
     * @return the lvlVariableName
     */
    public String getLvlVariableName() {
        return lvlVariableName;
    }

    /**
     * @param lvlVariableName the lvlVariableName to set
     */
    public void setLvlVariableName(String lvlVariableName) {
        this.lvlVariableName = lvlVariableName;
    }

    /**
     * @return the initialExperience
     */
    public long getInitialExperience() {
        return initialExperience;
    }

    /**
     * @param initialExperience the initialExperience to set
     */
    public void setInitialExperience(long initialExperience) {
        this.initialExperience = initialExperience;
    }

    /**
     * @return the initialHP
     */
    public long getInitialHP() {
        return initialHP;
    }

    /**
     * @param initialHP the initialHP to set
     */
    public void setInitialHP(long initialHP) {
        this.initialHP = initialHP;
    }

    /**
     * @return the initialMaxHP
     */
    public long getInitialMaxHP() {
        return initialMaxHP;
    }

    /**
     * @param initialMaxHP the initialMaxHP to set
     */
    public void setInitialMaxHP(long initialMaxHP) {
        this.initialMaxHP = initialMaxHP;
    }

    /**
     * @return the initialDP
     */
    public long getInitialDP() {
        return initialDP;
    }

    /**
     * @param initialDP the initialDP to set
     */
    public void setInitialDP(long initialDP) {
        this.initialDP = initialDP;
    }

    /**
     * @return the initialFP
     */
    public long getInitialFP() {
        return initialFP;
    }

    /**
     * @param initialFP the initialFP to set
     */
    public void setInitialFP(long initialFP) {
        this.initialFP = initialFP;
    }

    /**
     * @return the initialMP
     */
    public long getInitialMP() {
        return initialMP;
    }

    /**
     * @param initialMP the initialMP to set
     */
    public void setInitialMP(long initialMP) {
        this.initialMP = initialMP;
    }

    /**
     * @return the initialMaxMP
     */
    public long getInitialMaxMP() {
        return initialMaxMP;
    }

    /**
     * @param initialMaxMP the initialMaxMP to set
     */
    public void setInitialMaxMP(long initialMaxMP) {
        this.initialMaxMP = initialMaxMP;
    }

    /**
     * @return the initialLevel
     */
    public long getInitialLevel() {
        return initialLevel;
    }

    /**
     * @param initialLevel the initialLevel to set
     */
    public void setInitialLevel(long initialLevel) {
        this.initialLevel = initialLevel;
    }

    /**
     * @return the profilePicture
     */
    public String getProfilePicture() {
        return profilePicture;
    }

    /**
     * @param profilePicture the profilePicture to set
     */
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * @return the specialMoveList
     */
    public ArrayList<PlayerSpecialMove> getSpecialMoveList() {
        return specialMoveList;
    }

    /**
     * @return the specialMoveName
     */
    public String getSpecialMovesName() {
        return specialMovesName;
    }

    /**
     * @param specialMoveName the specialMoveName to set
     */
    public void setSpecialMovesName(String specialMoveName) {
        this.specialMovesName = specialMoveName;
    }

    /**
     * @return the hasSepcialMoves
     */
    public boolean getHasSpecialMoves() {
        return hasSpecialMoves;
    }

    /**
     * @param hasSpecialMoves the hasSepcialMoves to set
     */
    public void setHasSpecialMoves(boolean hasSpecialMoves) {
        this.hasSpecialMoves = hasSpecialMoves;
    }

    /**
     * @return the accessoryName
     */
    public ArrayList<String> getAccessoryNames() {
        return accessoryNames;
    }

    /**
     * @return the armourType
     */
    public boolean[] getArmourTypes() {
        return armourTypes;
    }

    /**
     * @return the levelType
     */
    public long getLevelType() {
        return levelType;
    }

    /**
     * @param levelType the levelType to set
     */
    public void setLevelType(long levelType) {
        this.levelType = levelType;
    }

    /**
     * @return the expIncreaseFactor
     */
    public int getExpIncreaseFactor() {
        return expIncreaseFactor;
    }

    /**
     * @param expIncreaseFactor the expIncreaseFactor to set
     */
    public void setExpIncreaseFactor(int expIncreaseFactor) {
        this.expIncreaseFactor = expIncreaseFactor;
    }

    /**
     * @return the maxLevel
     */
    public long getMaxLevel() {
        return maxLevel;
    }

    /**
     * @param maxLevel the maxLevel to set
     */
    public void setMaxLevel(long maxLevel) {
        this.maxLevel = maxLevel;
    }

    /**
     * @return the percentHPIncrease
     */
    public int getPercentHPIncrease() {
        return percentHPIncrease;
    }

    /**
     * @param percentHPIncrease the percentHPIncrease to set
     */
    public void setPercentHPIncrease(int percentHPIncrease) {
        this.percentHPIncrease = percentHPIncrease;
    }

    /**
     * @return the percentMPIncrease
     */
    public int getPercentMPIncrease() {
        return percentMPIncrease;
    }

    /**
     * @param percentMPIncrease the percentMPIncrease to set
     */
    public void setPercentMPIncrease(int percentMPIncrease) {
        this.percentMPIncrease = percentMPIncrease;
    }

    /**
     * @return the percentDPIncrease
     */
    public int getPercentDPIncrease() {
        return percentDPIncrease;
    }

    /**
     * @param percentDPIncrease the percentDPIncrease to set
     */
    public void setPercentDPIncrease(int percentDPIncrease) {
        this.percentDPIncrease = percentDPIncrease;
    }

    /**
     * @return the percentFPIncrease
     */
    public int getPercentFPIncrease() {
        return percentFPIncrease;
    }

    /**
     * @param percentFPIncrease the percentFPIncrease to set
     */
    public void setPercentFPIncrease(int percentFPIncrease) {
        this.percentFPIncrease = percentFPIncrease;
    }

    /**
     * @return the programOnLevelUp
     */
    public String getProgramOnLevelUp() {
        return programOnLevelUp;
    }

    /**
     * @param programOnLevelUp the programOnLevelUp to set
     */
    public void setProgramOnLevelUp(String programOnLevelUp) {
        this.programOnLevelUp = programOnLevelUp;
    }

    /**
     * @return the levelUpType
     */
    public byte getLevelUpType() {
        return levelUpType;
    }

    /**
     * @param levelUpType the levelUpType to set
     */
    public void setLevelUpType(byte levelUpType) {
        this.levelUpType = levelUpType;
    }

    /**
     * @return the characterSize
     */
    public byte getCharacterSize() {
        return characterSize;
    }

    /**
     * @param characterSize the characterSize to set
     */
    public void setCharacterSize(byte characterSize) {
        this.characterSize = characterSize;
    }

    /**
     * @return the standardGraphics
     */
    public ArrayList<String> getStandardGraphics() {
        return standardGraphics;
    }

    /**
     * @return the standardGraphicsAnimations
     */
    public ArrayList<Animation> getStandardGraphicsAnimations() {
        return standardGraphicsAnimations;
    }

    /**
     * @return the customGraphics
     */
    public ArrayList<String> getCustomGraphics() {
        return customGraphics;
    }

    /**
     * @return the customGraphicNames
     */
    public ArrayList<String> getCustomGraphicNames() {
        return customGraphicNames;
    }

    /**
     * @return the standingGraphics
     */
    public ArrayList<String> getStandingGraphics() {
        return standingGraphics;
    }

    /**
     * @return the idleTimeBeforeStanding
     */
    public double getIdleTimeBeforeStanding() {
        return idleTimeBeforeStanding;
    }

    /**
     * @param idleTimeBeforeStanding the idleTimeBeforeStanding to set
     */
    public void setIdleTimeBeforeStanding(double idleTimeBeforeStanding) {
        this.idleTimeBeforeStanding = idleTimeBeforeStanding;
    }

    /**
     * @return the frameRate (seconds between each step)
     */
    public double getFrameRate() {
        return frameRate;
    }

    /**
     * @param frameRate the frameRate to set (seconds between each step)
     */
    public void setFrameRate(double frameRate) {
        this.frameRate = frameRate;
    }

    public BoardVector getBaseVector()
    {
        return baseVector;
    }

    /**
     * @param baseVector the baseVector to set
     */
    public void setBaseVector(BoardVector baseVector) {
        this.baseVector = baseVector;
    }

    public BoardVector getActivationVector()
    {
        return activationVector;
    }

    /**
     * @param activationVector the activationVector to set
     */
    public void setActivationVector(BoardVector activationVector) {
        this.activationVector = activationVector;
    }

    /**
     * @deprecated 
     * @return 
     */
    public boolean saveBinary()
    {
        try
        {
            outputStream = new FileOutputStream(this.file);
            binaryIO.setOutputStream(outputStream);

            binaryIO.writeBinaryString(FILE_HEADER);
            binaryIO.writeBinaryInteger(MAJOR_VERSION);
            binaryIO.writeBinaryInteger(MINOR_VERSION);
            binaryIO.writeBinaryString(name);
            binaryIO.writeBinaryString(expVariableName);
            binaryIO.writeBinaryString(dpVariableName);
            binaryIO.writeBinaryString(fpVariableName);
            binaryIO.writeBinaryString(hpVariableName);
            binaryIO.writeBinaryString(maxHPVariableName);
            binaryIO.writeBinaryString(nameVariableName);
            binaryIO.writeBinaryString(mpVariableName);
            binaryIO.writeBinaryString(maxMPVariableName);
            binaryIO.writeBinaryString(lvlVariableName);
            binaryIO.writeBinaryLong(initialExperience);
            binaryIO.writeBinaryLong(initialHP);
            binaryIO.writeBinaryLong(initialMaxHP);
            binaryIO.writeBinaryLong(initialDP);
            binaryIO.writeBinaryLong(initialFP);
            binaryIO.writeBinaryLong(initialMP);
            binaryIO.writeBinaryLong(initialMaxMP);
            binaryIO.writeBinaryLong(initialLevel);
            binaryIO.writeBinaryString(profilePicture);
            for (int i = 0; i < 201; i++)
            {
                PlayerSpecialMove specialMove = specialMoveList.get(i);
                binaryIO.writeBinaryString(specialMove.getName());
                binaryIO.writeBinaryLong(specialMove.getMinExperience());
                binaryIO.writeBinaryLong(specialMove.getMinLevel());
                binaryIO.writeBinaryString(specialMove.getConditionVariable());
                binaryIO.writeBinaryString(specialMove.getConditionVariableTest());
            }
            binaryIO.writeBinaryString(specialMovesName);
            binaryIO.writeBinaryByte(hasSpecialMoves);
            for (int i = 0; i < 11; i++)
            {
                binaryIO.writeBinaryString(accessoryNames.get(i));
            }
            for (int i = 0; i < 7; i++)
            {
                binaryIO.writeBinaryByte(armourTypes[i] == true ? (byte)1 : (byte)0);
            }
            binaryIO.writeBinaryLong(levelType);
            binaryIO.writeBinaryInteger(expIncreaseFactor);
            binaryIO.writeBinaryLong(maxLevel);
            binaryIO.writeBinaryInteger(percentHPIncrease);
            binaryIO.writeBinaryInteger(percentDPIncrease);
            binaryIO.writeBinaryInteger(percentFPIncrease);
            binaryIO.writeBinaryInteger(percentMPIncrease);
            binaryIO.writeBinaryString(programOnLevelUp);
            binaryIO.writeBinaryByte(levelUpType);
            binaryIO.writeBinaryByte(characterSize);
            for (String standardGraphic : standardGraphics)
            {
                binaryIO.writeBinaryString(standardGraphic);
            }
            for (String standingGraphic : standingGraphics)
            {
                binaryIO.writeBinaryString(standingGraphic);
            }
            binaryIO.writeBinaryDouble(idleTimeBeforeStanding);
            binaryIO.writeBinaryDouble(frameRate);
            binaryIO.writeBinaryLong(customGraphicNames.size() - 1);
            for (int i = 0; i < customGraphicNames.size(); i++)
            {
                binaryIO.writeBinaryString(customGraphics.get(i));
                binaryIO.writeBinaryString(customGraphicNames.get(i));
            }
            binaryIO.writeBinaryInteger(1);
            binaryIO.writeBinaryInteger(baseVector.getPointCount() - 1);
            for (int i = 0; i < baseVector.getPointCount(); i++)
            {
                binaryIO.writeBinaryLong(baseVector.getPointX(i));
                binaryIO.writeBinaryLong(baseVector.getPointY(i));
            }
            binaryIO.writeBinaryInteger(activationVector.getPointCount() - 1);
            for (int i = 0; i < activationVector.getPointCount(); i++)
            {
                binaryIO.writeBinaryLong(activationVector.getPointX(i));
                binaryIO.writeBinaryLong(activationVector.getPointY(i));
            }

            outputStream.close();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveAs(File fileName)
    {
        this.file = fileName;
        return this.saveBinary();
    }

    /**
     * Loads the animations into memory, used in the engine, this is not called
     * during load as it would use too much memory to load every animation into memory for
     * all players/enemies/items at once.
     * <p/>
     * This should be called before rendering/game play takes place to avoid hindering performance
     */
    public void loadAnimations()
    {
        System.out.println("Loading Animations for " + this.name);
        standardGraphicsAnimations = new ArrayList<>();
        for (String anmFile : standardGraphics)
        {
            if (!anmFile.equals(""))
            {
                Animation a = new Animation(new File(System.getProperty("project.path") 
                        + "/"
                        + PropertiesSingleton.getProperty("toolkit.directory.misc")
                        + "/" + anmFile));
                standardGraphicsAnimations.add(a);
            }
        }
        animationTimer = new Timer();
        animationTimer.schedule(new AnimationTimer(), 0, 1000 / 6);
    }

    public void setActiveAnimation(int id)
    {
        activeAnimation = standardGraphicsAnimations.get(id);
        frameNumber = 0;
        frameCount = (int) activeAnimation.getFrameCount();
    }

    public BufferedImage getAnimationFrame()
    {
        return activeAnimation.getFrame(frameNumber).getFrameImage();
    }

//    public void loadTextures(TextureLoader loader)
//    {
//        for (Animation a : standardGraphicsAnimations)
//        {
//            for (int i = 0; i < a.getFrameCount(); i++)
//            {
//                a.getFrame(i).loadTexture(loader);
//            }
//        }
//    }

//    public void render()
//    {
//        glPushMatrix();
//
//        getFrameAsGLTexture().bind();
//        float width = getFrameAsGLTexture().getImageWidth();
//        float height = getFrameAsGLTexture().getImageHeight();
//        glTranslatef(currentXLocation, currentYLocation, 0);
//
//        // draw a quad textured to match the sprite
//        glBegin(GL_QUADS);
//        {
//            glTexCoord2f(0, 0);
//            glVertex2f(0, 0);
//
//            glTexCoord2f(0, getFrameAsGLTexture().getHeight());
//            glVertex2f(0, height);
//
//            glTexCoord2f(getFrameAsGLTexture().getWidth(), getFrameAsGLTexture().getHeight());
//            glVertex2f(width, height);
//
//            glTexCoord2f(getFrameAsGLTexture().getWidth(), 0);
//            glVertex2f(width, 0);
//        }
//        glEnd();
//
//        glPopMatrix();
//    }
//
//    public Texture getFrameAsGLTexture()
//    {
//        return activeAnimation.getFrame(frameNumber).getGlTexture();
//    }

    private class AnimationTimer extends TimerTask
    {
        public AnimationTimer()
        {
            super();
        }

        @Override
        public void run()
        {
            if (frameNumber == (frameCount - 1))
            {
                frameNumber = 0;
            }
            else
            {
                frameNumber++;
            }
        }
    }

    public void preparePhysics(int width, int height)
    {
        vectorCorrectionX = width / 2;
        vectorCorrectionY = height;

        currentXLocation = 1;
        currentYLocation = 1;
    }

    public int getVectorCorrectionX()
    {
        return vectorCorrectionX;
    }

    public int getVectorCorrectionY()
    {
        return vectorCorrectionY;
    }

    public Area getCollisionArea(int correctX, int correctY, int shiftX, int shiftY)
    {
        Polygon collisionPoly = new Polygon();

        for (Point point : baseVector.getPoints())
        {
            collisionPoly.addPoint(point.x + correctX + (currentXLocation + shiftX), point.y + correctY + (currentYLocation + shiftY));
        }
        baseArea = new Area(collisionPoly);
        return baseArea;
    }

    public int getXLocation()
    {
        return currentXLocation;
    }

    public int getYLocation()
    {
        return currentYLocation;
    }

    public void setXLocation(int newXLocation)
    {
        this.currentXLocation = newXLocation;
    }

    public void setYLocation(int newYLocation)
    {
        this.currentYLocation = newYLocation;
    }

    public void adjustX(int adjustment)
    {
        currentXLocation += adjustment;
    }

    public void adjustY(int adjustment)
    {
        currentYLocation += adjustment;
    }
}