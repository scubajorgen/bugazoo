package net.studioblueplanet.bugazoo.core;

import java.awt.*;

/**
 * This class implements the manure object. Manure is a gray matter
 * that serves as food (energy) for the Creatures.
 * @author        B.J. van der Velde
 * @version       1.0
 *
 */
public class Manure extends EnergyCarrier
{
    public static final int     ENERGY_SCALEING         =60;
    public static final Color   MANURE_COLOR            =new Color(0.5f, 0.4f, 0.5f);

    private final float         fXPos;
    private final float         fYPos;

    /** Constructor: creates a pile of manure at given position and asigns
     *  the amount of energy to it as defined
     *  @param          fXPos
     *  @param          fYPos
     *  @param          liEnergy
     */

    public Manure (float fXPos, float fYPos, long liEnergy)
    {
        this.fXPos=fXPos;
        this.fYPos=fYPos;
        setEnergy(liEnergy);
    }

    /**
     * Returns the x coordinate of the location
     * @return X Coordinate
     */
    public float getXPos()
    {
        return fXPos;
    }

    /**
     * Returns the y coordinate of the location
     * @return Y Coordinate
     */
    public float getYPos()
    {
        return fYPos;
    }

    /**
     * 
     */
    public void updateReset()
    {
        initializeUpdatedEnergy();
    }

    /** 
     * Paint the manure
     * @param g Graphics to use for repainting the manure
     */
    public void paint(Graphics g)
    {
        int iSize;

        iSize=(int)Math.sqrt(getEnergy())/ENERGY_SCALEING;

        g.setColor(MANURE_COLOR);
        g.fillOval((int)fXPos-iSize/2, (int)fYPos-iSize/2,
                    iSize, iSize);

    }
}
