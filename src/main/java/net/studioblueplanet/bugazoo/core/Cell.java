package net.studioblueplanet.bugazoo.core;

import java.awt.*;

/**
 * The class represents the Cell. It stores the Cell's state like energy,
 * position and a number of cell parameters.
 * @author        B.J. van der Velde
 * @version       1.0
 */
public class Cell extends EnergyCarrier
{
    private static final int   DEFAULT_SIZE                      =12;
    private static final float MASS                              =1.0f;
    private static final float ENERGY_DISSIPATION_PER_TIME       =5000.0f;
    private static final float ACCELERATION_ENERGY_UNIT          =1.0f;


    /** Absolute x position on the screen, expressed in pixel units */
    protected           float   fXPos;                    
    /** Absolute y position on the screen, expressed in pixel units */
    protected           float   fYPos;
    /** normalized relative x position on the grid, expressed in units of Cell size */
    private             float   fXRelPos;                 
    /** normalized relative y position on the grid, expressed in units of Cell size */
    private             float   fYRelPos;
    /** normalized relative x position on the grid, expressed in units of Cell size */
    private final       int     iXGridPos;
    /** normalized relative y position on the grid, expressed in units of Cell size */
    private final       int     iYGridPos;


    private final       Vector  zeroVector=new Vector(0.0f, 0.0f);
    protected static    float   fSqCellSize=(float)(DEFAULT_SIZE*DEFAULT_SIZE);

    // adjustable settings
    private static      float   fEnergyDissipation=ENERGY_DISSIPATION_PER_TIME;
    private static      float   fAccelerationEnergyUnit=ACCELERATION_ENERGY_UNIT;
    private static      float   fMass=MASS;
    protected static    int     iCellSize=DEFAULT_SIZE;
    protected static    float   fCellSize=(float)DEFAULT_SIZE;


    /** Creates a new cell at a position relative to (0, 0). The position
     *  indicated is a normalized position, which means it is expressed
     *  in cell diameters.
     *  @param          iXGridPos  Column of relative position in grid of cell
     *  @param          iYGridPos  Row of relative position in grid of cell
     *  @param          liEnergy    Initial energy of cell
     */
    public Cell (int iXGridPos, int iYGridPos, long liEnergy)
    {
        setEnergy(liEnergy);

        this.iXGridPos=iXGridPos;
        this.iYGridPos=iYGridPos;

        resetRelativePosition();
    }

    /** Translate the relative position (relative with respect to position
     *  of the creature) of the cell.
     *  @param          fXIncrement Increment of relative x coordinate
     *  @param          fYIncrement Increment of relative y coordinate
     */
    public void translateRelativePosition(float fXIncrement, float fYIncrement)
    {
        fXRelPos+=fXIncrement;
        fYRelPos+=fYIncrement;
    }

    /** Resets the relative position of the cell to some calibrated
     *  position (with respect to the x,y coordinate of the
     *  creature).
     *  This method calculates the non-normalized relative position.
     *  After calling (fXRelPos, fYRelPos) is expressed in pixels i.s.o.
     *  cell diameters. Also the Cells are translated into a 'hexagonal close
     *  packing' structure i.s.o. a grid.
     */

    public final void resetRelativePosition()
    {
        if (iYGridPos%2==0)
            this.fXRelPos=iXGridPos*iCellSize;
        else
            this.fXRelPos=iXGridPos*iCellSize-iCellSize/2;
        this.fYRelPos=iYGridPos*iCellSize*(float)Math.cos(Math.PI/6.0);

    }


    /** Gets the distance from this cell to the cell specified
     *  @param          cell Cell to which the distance is returned
     *  @return         The distance to the cell specified
     */

    public float getDistance(Cell cell)
    {
        float fDx;
        float fDy;

        fDx=fXPos-cell.fXPos;
        fDy=fYPos-cell.fYPos;
        return (float)Math.sqrt(fDx*fDx+fDy*fDy);
    }

    /** Gets the relative X coordinate of the cell with respect to the position
     *  of the creature (the center of mass), expressed in pixels
     *  @return         The X coordinate
     */
    public float getXRelPos()
    {
        return fXRelPos;
    }

    /** Gets the relative Y coordinate of the cell with respect to the position
     *  of the creature (the center of mass), expressed in pixels
     *  @return         The Y coordinate
     */
    public float getYRelPos()
    {
        return fYRelPos;
    }


    /** Gets the absolute X coordinate of the cell expressed in pixels
     *  @return         The X coordinate
     */
    public float getXPos()
    {
        return fXPos;
    }

    /** Gets the absolute Y coordinate of the cell expressed in pixels
     *  @return         The Y coordinate
     */
    public float getYPos()
    {
        return fYPos;
    }

    /** Gets the relative grid X coordinate of the cell with respect to some
     *  arbitrary point. Specified in cell diameters
     *  @return         The X coordinate
     */
    public int getXGridPos()
    {
        return iXGridPos;
    }

    /** Gets the relative grid Y coordinate of the cell with respect to some
     *  arbitrary point. Specified in Cell diameter units
     *  @return         The Y coordinate
     */
    public int getYGridPos()
    {
        return iYGridPos;
    }

    /** Updates the absolute cell coordinates. The position and heading of the
     *  creature to which the cell belongs is specified
     *  @param          fCreatureXPos X coordinate of creature
     *  @param          fCreatureYPos Y coordinate of creature
     *  @param          fCreatureHeading Heading of creature in radians
     */
    public void iterationUpdatePosition(float fCreatureXPos , float fCreatureYPos,
                                        float fCreatureHeading)
    {
        float fSin;
        float fCos;

        fSin=(float)Math.sin(fCreatureHeading);
        fCos=(float)Math.cos(fCreatureHeading);

        fXPos=fCreatureXPos + fCos*fXRelPos + fSin*fYRelPos;
        fYPos=fCreatureYPos - fSin*fXRelPos + fCos*fYRelPos;
    }

    /** Sets a new value for the Energy per unit of time. This value holds
     *  for all Cells, since it is a static parameter
     *  @param          fNewEnergyDissipation New value for the Energy dissipation
     */
    public static final void setEnergyDissipation(float fNewEnergyDissipation)
    {
        fEnergyDissipation=fNewEnergyDissipation;
    }

    /** Returns the value for the Energy dissipation per unit of time. This
     *  is a static parameter of the Cell.
     *  @return         The value for Energy dissipation
     */
    public static float getEnergyDissipation()
    {
        return fEnergyDissipation;
    }

    /** Sets a new value for the Energy dissipation per unit of acceleration (static value).
     *  @param          fNewEnergyUnit Energy dissipation
     */
    public static void setAccelerationEnergyUnit(float fNewEnergyUnit)
    {
        fAccelerationEnergyUnit=fNewEnergyUnit;
    }

    /** This method returns the Energy dissipation per acceleration unit (static value).
     *  @return         The Energy per unit of acceleration
     */
    public static float getAccelerationEnergyUnit()
    {
        return fAccelerationEnergyUnit;
    }

    /** This method sets the Cell size (diameter) expressed in pixels (static value).
     *  It should be minimum 4, maximum 50
     *  @param    iNewCellSize The new Cell size
     */
    public static void setCellSize(int iNewCellSize)
    {
        iCellSize=iNewCellSize;
        iCellSize=Math.max(iCellSize, 4);
        iCellSize=Math.min(iCellSize, 50);
        fCellSize=(float)iCellSize;
        fSqCellSize=(float)(fCellSize*fCellSize);
    }

    /** Returns the Cell size (diameter) expressed in pixels (static value).
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public static int getCellSize()
    {
        return iCellSize;
    }


    /** This method sets the Cell mass (static value)
     *  @param  fNewMass The new mass value
     */
    public static void setMass(float fNewMass)
    {
        fMass=fNewMass;
    }

    /** Returns the mass of the Cell (static value)
     *  @return   The mass of a Cell
     */
    public static float getMass()
    {
        return fMass;
    }


    /** Draws the cell
     *  @param          g Graphics
     */
    public void paint(Graphics g)
    {
        g.setColor(Color.black);
        g.fillOval ((int)fXPos-iCellSize/2,
                    (int)fYPos-iCellSize/2,
                    iCellSize,
                    iCellSize);
    }


    /**
     * This method paints a large version of the Cell.
     * @param g Graphics environment to use
     * @param fXPos Absolute X position
     * @param fYPos Absolute Y position
     * @param iCellSize The diameter of the Cell to draw
     */
    public void paintLarge(Graphics g, float fXPos, float fYPos, int iCellSize)
    {
        g.setColor(Color.black);
        g.drawOval ((int)fXPos-iCellSize/2,
                    (int)fYPos-iCellSize/2,
                    iCellSize,
                    iCellSize);
    }


    /**
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void updateReset()
    {
        initializeUpdatedEnergy();
    }

    /**
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void updateToEnvironment(Manure manure)
    {
    }


    /**
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void updateToEnvironment(Cell otherCreatureCell)
    {
    }

    /**
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void updateConsuming(Cell otherCell, float fRelSqDistance)
    {
    }

    /**
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void updatePreySensing(Cell otherCell, Vector relativePosition,
                                  float fRelDistance)
    {
    }

    /**
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void updatePredatorSensing(Cell otherCell, Vector relativePosition,
                                      float fRelSqDistance)
    {
    }

    /**
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public float getPreyConsumerPerformance()
    {
        return 0.0f;
    }

    /**
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public Vector getPreySense()
    {
        return zeroVector;
    }

    /**
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public Vector getManureSense()
    {
        return zeroVector;
    }

    /**
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public Vector getPredatorSense()
    {
        return zeroVector;
    }



    /**
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void accumulateDragForce(Cell otherCell)
    {
    }

    /**
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public Vector getDragForce()
    {
        return zeroVector;
    }

    /**
     * 
     * @param force 
     */
    public void setDragForce(Vector force)
    {
        // TO DO?
    }

    /**
     *  Mutates this cell
     */
    public void mutate()
    {
        
    }

    /**
     * Copies this cell to a new instance
     * @return Newly created, identical Cell
     */
    public Cell copy()
    {
        Cell cell;

        cell = new Cell(getXGridPos(), getYGridPos(), 0);
        return cell;
    }
}
