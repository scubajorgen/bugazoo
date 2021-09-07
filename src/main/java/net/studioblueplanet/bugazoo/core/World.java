package net.studioblueplanet.bugazoo.core;

import java.util.Random;
import java.awt.*;
import java.net.URL;
import java.util.Date;
import java.io.Serializable;
import net.studioblueplanet.bugazoo.common.Constants;


/**
 * This class represents the playground for the Creatures and Manure. It is a
 * rectangular area where Creatures can move around and Manure grows. The class
 * is responsible for drawing the world and implementing the iteration process
 * of the Creatures and Manure. Copyright: Copyright (c) 2003
 *
 * @author B.J. van der Velde
 * @version 1.0
 */
public class World extends Canvas implements Serializable
{

    public static  final int     BORDER_WIDTH           =5;
    public static  final String  BACKGROUNDTEXTUREFILE  ="bugazoo/image/water2.jpg";

    private static final int    INITIAL_MANURE_DENSITY  =100;
    private static final long   INITIAL_MANUREENERGY    =(long)1000000;

    private static final Color  BORDER_COLOR            =new Color(0.2f, 0.5f, 1.0f);

    private static final int    MAX_INFO_LEVELS         =3;

    /** The Manures in the World */
    private Manure              manureChain;                            // the manures

    /** The Creatures in the World */
    private Creature            creatureChain;                          // the creatures

    private int                 iPlayingFieldWidth;                     // width of playground
    private int                 iPlayingFieldHeight;                    // height of playground

    private long                liEnergyDissipatedByCreatures;
    private int                 nManures;                               // current number of manures
    private long                liWorldEnergy;                          // entire energy in the universe
    private long                liWorldManureEnergy;                    // energy in manure
    private long                liWorldCreatureEnergy;                  // energy in creatures
    private int                 iMaxManures;                            // max allowed manures

    private int                 iCreaturesBorn=0;
    private int                 iCreaturesDied=0;
    private int                 iCreaturesAlive=0;

    private long                iIterations;
    private float               fWorldTime;
    private int                 iInfoLevel;

    private int                 iTextureHeight;
    private int                 iTextureWidth;

    private final Date          time=new Date();
    private long                liFrameStartTime=-1;
    private long                liFrameEndTime;
    private long                liFrameRate=0;
    private long                liCalcStartTime=-1;
    private long                liCalcEndTime;
    private long                liCalcRate=0;
    private long                liFrames=0;
    private long                liCalculations=0;

    private static Image        offscreenImage;        // static because Image is not serializable
    private static Image        backgroundImage;
    private static Image        backgroundTexture;
    private static Graphics     offscreenGraphics;

    // adjustable settings
    private static int          iInitialManureDensity   =INITIAL_MANURE_DENSITY;
    private static long         liInitialManureEnergy   =INITIAL_MANUREENERGY;
    private static Color        borderColor             =BORDER_COLOR;
    private static String       sBackgroundTextureFile  =BACKGROUNDTEXTUREFILE;
    private static String       sWorldName              ="Default World";


    /** 
     * Creates the world and adds manure and some creature to it
     */
    public World (int iPlayingFieldWidth, int iPlayingFieldHeight)
    {
        iInfoLevel=0;

        this.iPlayingFieldWidth =iPlayingFieldWidth-2*BORDER_WIDTH;
        this.iPlayingFieldHeight=iPlayingFieldHeight-2*BORDER_WIDTH;

        Creature.setPlayingFieldDimension(iPlayingFieldWidth, iPlayingFieldHeight);

        initializeWorld();
        initializeManures();
    }

    public void setNewPlayingFieldDimensions(int iPlayingFieldWidth, int iPlayingFieldHeight)
    {
        this.iPlayingFieldWidth=iPlayingFieldWidth-2*BORDER_WIDTH;
        this.iPlayingFieldHeight=iPlayingFieldHeight-2*BORDER_WIDTH;

        Creature.setPlayingFieldDimension(iPlayingFieldWidth, iPlayingFieldHeight);
    }

    /** 
     * Initializes the world. Loads the background texture file.
     */
    public void initializeWorld()
    {
        MediaTracker tracker;

        URL url = World.class.getResource("/" + sBackgroundTextureFile);
        backgroundTexture = Toolkit.getDefaultToolkit().getImage(url);
        tracker = new MediaTracker(this);						// wait till image loaded
        tracker.addImage(backgroundTexture, 0);

        try
        {
            tracker.waitForID(0);
        } 
        catch (InterruptedException e)
        {
        }

        iTextureWidth   = backgroundTexture.getWidth(this);
        iTextureHeight  = backgroundTexture.getHeight(this);

        iIterations = 0;
        fWorldTime  = 0.0f;
    }

    /**
     * Initializes the initial amount of Manure and distributes it over the
     * World.
     */
    private void initializeManures()
    {
        int i;
        int x;
        int y;
        Manure manure;
        int nNumberOfManures;

        manureChain = null;                                       // erase existing manures
        nNumberOfManures = iInitialManureDensity * iPlayingFieldWidth * iPlayingFieldHeight / 1000000;
        i = 0;
        while (i < nNumberOfManures)                               // process manures
        {
            x = (int) (Math.random() * iPlayingFieldWidth);         // generate random position
            y = (int) (Math.random() * iPlayingFieldHeight);
            manure = new Manure(x, y, liInitialManureEnergy);    // create manure
            if (manureChain == null)                             // append to chain
            {
                manureChain = manure;
            } else
            {
                manureChain.appendItemToChain(manure);
            }
            i++;
        }
        this.nManures = nNumberOfManures;
        iMaxManures = nNumberOfManures;
    }

    /**
     * Updates the World and its contents when static settings have changed.
     */
    public void updateForNewSettings()
    {
        Creature creature;
        creature = creatureChain;                                   // start with 1st creature in the chain
        while (creature != null)                                    // process all creatures
        {
            creature.updateForNewSettings();                        // reset the sensor information of the cell
            creature = (Creature) creature.getNextChainItem();
        }
        initializeWorld();
        initializeManures();
        offscreenImage = null;
    }

    /**
     * Get rid of all the Creatures in this world.
     */
    public void killAllCreatures()
    {
        creatureChain=null;
    }

    /**
     * Insert a brand new Creature in this world
     */
    public void insertCreature(Creature creature, boolean bRandomPosition)
    {
        int iFrameWidth;
        int iFrameHeight;
        Random rand;
        float fNewXPos;
        float fNewYPos;

        // If requested, update creatures coordinates to random position
        if (bRandomPosition)
        {
            rand = new Random();
            // Set each creature to a random position
            fNewXPos = rand.nextFloat() * iPlayingFieldWidth;
            fNewYPos = rand.nextFloat() * iPlayingFieldHeight;
            creature.setPosition(fNewXPos, fNewYPos);
            creature.updateForNewSettings();
        }

        if (creatureChain == null)
        {
            creatureChain = creature;
        } else
        {
            creatureChain.appendItemToChain(creature);
        }
        iCreaturesAlive++;
        iCreaturesBorn++;
        repaint();
    }

    /**
     * Updates the world for one time unit (iteration). Creatures and manure are
     * updated. World energy is calcualted. The iteration rate is calculated.
     */
    public void iterate()
    {
        // let the creature take a bite and update sensory inputs
        updateToEnvironment();

        // calculate position and speed of the creatures
        updateCreatures();

        // update creature birth and death
        updateCreatureCreation();

        // remove manure that has been eaten entirely
        updateCleanupManure();

        // the energy dissipated by creatures is added to manure or occurs
        // as new manure
        updateManureEnergy();

        // add all manure and creature energy: this should be constant
        updateWorldEnergy();

        // calculate the number of iterations per second
        updateTime();

        fWorldTime += Constants.TIME_UNIT;
        iIterations++;

        // repaint
        repaint();
    }

    /**
     * Updates all creatures with respect to their environment (other creatures
     * and manure). The sensing strength and consumption of the creatures is
     * updated.
     */
    private void updateToEnvironment()
    {
        Creature currCreature;
        Creature otherCreature;
        Manure manure;

        manure = manureChain;
        while (manure != null)
        {
            manure.updateReset();
            manure = (Manure) manure.getNextChainItem();
        }

        currCreature = creatureChain;                                             // start with 1st creature in the chain
        while (currCreature != null)                                              // process all creatures
        {
            currCreature.updateReset();                                         // reset the sensor information of the cell
            currCreature = (Creature) currCreature.getNextChainItem();
        }

        currCreature = creatureChain;                                             // start with 1st creature in the chain
        while (currCreature != null)                                              // process all creatures
        {
            otherCreature = (Creature) currCreature.getNextChainItem();            // get creature next to current
            while (otherCreature != null)                                         // process all previous creatures
            {                                                                   // check if creature in range
                currCreature.updateToEnvironment(otherCreature);                // update creature for environment
                otherCreature = (Creature) otherCreature.getNextChainItem();       // get next cell
            }

            manure = manureChain;
            while (manure != null)
            {
                currCreature.updateToEnvironment(manure);
                manure = (Manure) manure.getNextChainItem();
            }
            currCreature = (Creature) currCreature.getNextChainItem();
        }

    }

    /**
     * Updates the creatures state. Creature position, speed, energy, etc. is
     * updated.
     */
    private void updateCreatures()
    {
        Creature creature;

        liEnergyDissipatedByCreatures = 0;
        creature = creatureChain;
        while (creature != null)
        {
            creature.iterationUpdate();
            liEnergyDissipatedByCreatures += creature.getDissipatedEnergy();
            creature = (Creature) creature.getNextChainItem();
        }
    }

    /**
     * Updates the creature creation and dying process.
     */
    private void updateCreatureCreation()
    {
        Creature creature;
        Creature returnCreature;
        Creature removeCreature;
        Creature newCreature;
        Manure manure;

        creature = creatureChain;
        while (creature != null)
        {
            if (creature.getEnergyPerCell() > Creature.getReproductionCellEnergy())
            {
                newCreature = creature.splitCreature();
                iCreaturesAlive++;
                iCreaturesBorn++;
                creatureChain.appendItemToChain(newCreature);
            }

            if (creature.getEnergyPerCell() < Creature.getMinimumCellEnergy())
            {
                manure = creature.killCreature();                                 // kill creature, returns menure subchain
                manureChain.appendItemToChain(manure);                          // append resulting subchain to chain
                while (manure != null)                                            // count number of manures
                {
                    manure = (Manure) manure.getNextChainItem();
                    nManures++;
                }
                removeCreature = creature;                                        // remember killed creature
                creature = (Creature) creature.getNextChainItem();                 // get next creature
                creatureChain = (Creature) removeCreature.removeItemFromChain();   // remove killed creature from chain
                iCreaturesAlive--;
                iCreaturesDied++;
            } else
            {
                creature = (Creature) creature.getNextChainItem();
            }
        }

    }

    /**
     * Cleans up Manure which energy has depleted (=0). The Manure is removed
     * from the chain of Manures
     */
    private void updateCleanupManure()
    {
        Manure manure;                  // manure pointer
        Manure removeManure;            // manure to be removed

        manure = manureChain;             // get 1st manure in chain
        while (manure != null)            // process all manures
        {
            manure.updateEnergy();
            if (manure.getEnergy() == 0)  // if manure energy zero...
            {
                removeManure = manure;    // remove it
                manure = (Manure) manure.getNextChainItem();
                manureChain = (Manure) removeManure.removeItemFromChain();
                nManures--;
            } else
            {
                manure = (Manure) manure.getNextChainItem();
            }

        }
    }

    /**
     * Updates the manure energy. Energy dissipated by creatures is added to the
     * Manures. This energy is added to a newly created Manure or is distributed
     * amongst all existing Manure.
     */
    public void updateManureEnergy()
    {
        Manure manure;
        int x;
        int y;
        long liEnergyIncrement;
        long liEnergyRemaining;

        if (manureChain == null)
        {
            x = (int) (Math.random() * iPlayingFieldWidth);
            y = (int) (Math.random() * iPlayingFieldHeight);
            manureChain = new Manure(x, y, liEnergyDissipatedByCreatures);
            nManures++;
        } else
        {
            if ((Math.random() < 10.0 * (double) Constants.TIME_UNIT / (double) nManures)
                    && nManures < iMaxManures)
            {
                x = (int) (Math.random() * iPlayingFieldWidth);
                y = (int) (Math.random() * iPlayingFieldHeight);
                manure = new Manure(x, y, liEnergyDissipatedByCreatures);
                manureChain.appendItemToChain(manure);
                nManures++;
            } else
            {
                liEnergyIncrement = liEnergyDissipatedByCreatures / (long) nManures;
                liEnergyRemaining = liEnergyDissipatedByCreatures % (long) nManures;

                manure = manureChain;
                while (manure != null)
                {
                    manure.increaseUpdatedEnergy(liEnergyIncrement);
                    if (liEnergyRemaining > 0)
                    {
                        manure.increaseUpdatedEnergy((long) 1);
                        liEnergyRemaining--;
                    }
                    manure.updateEnergy();
                    manure = (Manure) manure.getNextChainItem();
                }
            }
        }

    }

    /**
     * Calculates the energy stored in Manure and in Creatures.
     */
    private void updateWorldEnergy()
    {
        Manure manure;                  // manure pointer
        Creature creature;

        liWorldManureEnergy = 0;
        manure = manureChain;             // get 1st manure in chain
        while (manure != null)            // process all manures
        {
            liWorldManureEnergy += manure.getEnergy();
            manure = (Manure) manure.getNextChainItem();
        }

        liWorldCreatureEnergy = 0;
        creature = creatureChain;
        while (creature != null)
        {
            liWorldCreatureEnergy += creature.getEnergy();
            creature = (Creature) creature.getNextChainItem();
        }

        liWorldEnergy = liWorldManureEnergy + liWorldCreatureEnergy;

    }

    /**
     * Calculates the iteration rate (number of iterations per second)
     */
    private void updateTime()
    {
        liCalculations++;
        if ((liCalculations % 64) == 0)
        {
            liCalcEndTime = System.currentTimeMillis();
            if (liCalcStartTime > 0)
            {
                liCalcRate = (long) 64000 / (liCalcEndTime - liCalcStartTime);
            }
            liCalcStartTime = liCalcEndTime;
        }
    }

    /**
     * Sets the border color
     *
     * @param newColor The new border color
     */
    public static void setBorderColor(Color newColor)
    {
        borderColor = newColor;
    }

    /**
     * The paint method.
     *
     * @param g Graphics instance used for painting
     */
    public void paint(Graphics g)
    {
        update(g);
    }

    /**
     * The update method. Draws the playing field with manure and creatures.
     *
     * @param g Graphics instance used for painting
     */
    public void update(Graphics g)
    {
        Manure   manure;
        Creature creature;
        int      i;
        int      j;

        g.setColor(borderColor);									// paint the border
        g.fillRect(0, 0,
                   BORDER_WIDTH, iPlayingFieldHeight+2*BORDER_WIDTH);
        g.fillRect(iPlayingFieldWidth+BORDER_WIDTH, 0,
                   BORDER_WIDTH, iPlayingFieldHeight+2*BORDER_WIDTH);
        g.fillRect(BORDER_WIDTH,0,
                   iPlayingFieldWidth, BORDER_WIDTH);
        g.fillRect(BORDER_WIDTH, iPlayingFieldHeight+BORDER_WIDTH,
                   iPlayingFieldWidth, BORDER_WIDTH);

        if (offscreenImage==null)                                   // if offscreen image does not exist, create it
        {
            offscreenImage=createImage(iPlayingFieldWidth, iPlayingFieldHeight);
            offscreenGraphics=offscreenImage.getGraphics();
        }

        if ((iTextureWidth>0) && (iTextureHeight>0))                // fill backgound with background texture (if loaded)
        {
            i=0;
            while (i<iPlayingFieldHeight)
            {
                j=0;
                while (j<iPlayingFieldWidth)
                {
                    offscreenGraphics.drawImage(backgroundTexture, j, i, this);
                    j+=iTextureWidth;
                }
                i+=iTextureHeight;
            }
        }
        else                                                        // else, erase ima
        {
            offscreenGraphics.clearRect(0, 0, iPlayingFieldWidth, iPlayingFieldHeight);    // erase image
            iTextureWidth =backgroundTexture.getWidth (this);
            iTextureHeight=backgroundTexture.getHeight(this);
        }

        liFrames++;                                                 // calculate framerate
        if ((liFrames%64)==0)
        {
            liFrameEndTime=System.currentTimeMillis();
            if (liFrameStartTime>0)
            {
                liFrameRate=(long)64000/(liFrameEndTime-liFrameStartTime);
            }
            liFrameStartTime=liFrameEndTime;
        }

        manure=manureChain;                                         // draw the menures
        while (manure!=null)
        {
            manure.paint(offscreenGraphics);
            manure=(Manure)manure.getNextChainItem();
        }

        creature=creatureChain;                                     // draw the creatures
        while (creature!=null)
        {
            creature.paint(offscreenGraphics, (iInfoLevel>1));
            creature=(Creature)creature.getNextChainItem();
        }

        if (iInfoLevel>0)
        {
            offscreenGraphics.setColor(Color.black);                    // draw status
            offscreenGraphics.drawString("ENERGY: Total "   +String.valueOf(liWorldEnergy)                 , 0   , iPlayingFieldHeight);
            offscreenGraphics.drawString("Manure "          +String.valueOf(liWorldManureEnergy)           , 160 , iPlayingFieldHeight);
            offscreenGraphics.drawString("Cell "            +String.valueOf(liWorldCreatureEnergy)         , 280 , iPlayingFieldHeight);
            offscreenGraphics.drawString("CREATURES: Alive "+String.valueOf(iCreaturesAlive)               , 380 , iPlayingFieldHeight);
            offscreenGraphics.drawString("Born "            +String.valueOf(iCreaturesBorn)                , 530 , iPlayingFieldHeight);
            offscreenGraphics.drawString("Died "            +String.valueOf(iCreaturesDied)                , 600 , iPlayingFieldHeight);
            offscreenGraphics.drawString("Mutated "         +String.valueOf(Creature.getMutatedCreatures()), 670 , iPlayingFieldHeight);

            offscreenGraphics.drawString("Iterations "      +String.valueOf(iIterations)                   , 0   , iPlayingFieldHeight-20);
            offscreenGraphics.drawString("WorldTime "       +String.valueOf((int)fWorldTime)               , 150   , iPlayingFieldHeight-20);
            offscreenGraphics.drawString("Calc rate "       +String.valueOf(liCalcRate)                    , 300 , iPlayingFieldHeight-20);
            offscreenGraphics.drawString("Frame rate "      +String.valueOf(liFrameRate)                   , 400 , iPlayingFieldHeight-20);

            offscreenGraphics.drawString("WORLD: " + sWorldName                                            , 10  , 20                    );
        }
        g.drawImage(offscreenImage, BORDER_WIDTH, BORDER_WIDTH, null);

    }

    /**
     * This method returns the width of the creature playing field
     *
     * @return The width of the creature playing field
     */
    public int getPlayingFieldWidth()
    {
        return iPlayingFieldWidth;
    }

    /**
     * This method returns the height of the creature playing field
     *
     * @return The height of the creature playing field
     */
    public int getPlayingFieldHeight()
    {
        return iPlayingFieldHeight;
    }

    /**
     * Sets the initial Manuredenisty in micro Manures per square pixel.
     *
     * @param nNewInitialManures The Manure density at instantiation of the
     * world
     */
    public static void setInitialManureDensity(int nNewInitialManures)
    {
        iInitialManureDensity = nNewInitialManures;
        iInitialManureDensity = Math.max(iInitialManureDensity, 2);
    }

    /**
     * Gets the initial Manure Density on instantiation of the World
     *
     * @return The Manure density in micro Manures per square inch
     */
    public static int getInitialManureDensity()
    {
        return iInitialManureDensity;
    }

    /**
     * Defines the filename of the texture to be used for background
     *
     * @param sFileName Filename to be used
     */
    public static void setBackgroundTextureFile(String sFileName)
    {
        sBackgroundTextureFile = sFileName;
    }

    /**
     * This method returns the name of the file used for background texture
     *
     * @return Texture file name
     */
    public static String getBackgroundTextureFile()
    {
        return sBackgroundTextureFile;
    }

    /**
     * Sets the amount of energy to be stored as Manure at the start of the
     * World
     *
     * @param liNewInitialManureEnergy Initial amount of energy for Manure
     */
    public static void setInitialManureEnergy(long liNewInitialManureEnergy)
    {
        liInitialManureEnergy = liNewInitialManureEnergy;
        liInitialManureEnergy = Math.max(liInitialManureEnergy, 1);
    }

    /**
     * Gets the initial energy stored as Manure
     *
     * @return The initial Manure energy
     */
    public static long getInitialManureEnergy()
    {
        return liInitialManureEnergy;
    }

    /**
     * This method sets the name of the World
     *
     * @param sNewWorldName Name of the World
     */
    public static void setWorldName(String sNewWorldName)
    {
        sWorldName = sNewWorldName;
    }

    /**
     * Returns the name of the World
     *
     * @return The name of this World
     */
    public static String getWorldName()
    {
        return sWorldName;
    }

    /**
     * Cycle through the info levels
     */
    public void toggleShowInfo()
    {
        iInfoLevel++;
        if (iInfoLevel == MAX_INFO_LEVELS)
        {
            iInfoLevel = 0;
        }
    }
}
