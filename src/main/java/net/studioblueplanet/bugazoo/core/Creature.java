package net.studioblueplanet.bugazoo.core;

import java.awt.*;

import net.studioblueplanet.bugazoo.common.Constants;

/**
 * This class represents a Creature. A creature is a collection of Functional
 * Cells and an optional Brain Cell. A Functional cell is an actuator or a
 * sensor or a combination of both. A Brain Cell translates sensor input into
 * actuator output.
 *
 * @author B.J. van der Velde
 * @version 1.0
 */

public class Creature extends ChainItem
{
    public static final int     POSSIBLE_MUTATIONS          =3;
    public static final int     MAX_CELLS                   =100;

    private static final float  FRICTION                    =0.95f;
    private static final long   REPRODUCTION_CELLENERGY     =1500000;
    private static final long   INITIAL_CELLENERGY          =900000;
    private static final long   MINIMUM_CELLENERGY          =250000;
    private static final float  MUTATION_CHANCE             =0.2f;

    protected String            sName="Creature";
    protected String            sType="type";

    private Cell                cellChain;
    private Cell                cellPointer;
    private Cell                firstBrainCell;

    private float               fSize;
    private int                 nCells;
    private int                 iGeneration;
    private static int          nCreaturesMutated=0;


    private static float        fPlayingFieldHeight;
    private static float        fPlayingFieldWidth;

    private float               fXCenterOfMass;         // x coordinate of center of mass rel. to fXPos
    private float               fYCenterOfMass;         // y coordinate of center of mass rel. to fYPos
    private float               fXPos;                  // x coordinate of creature
    private float               fYPos;                  // y coordinate of creature
    private Vector              speed;                  // speed (vector) of creature
    private Vector              force;                  // force acting on creature
    private Vector              acceleration;           // acceleration due to force

    private float               fHeading;               // heading of creature
    private float               fAngularSpeed;
    private float               fAngularAcceleration;
    private float               fTorque;
    private float               fMomentOfInertia;

    private float               fEnergyDissipated;
    private long                liEnergy;
    private long                liEnergyPerCell;
    private long                liEnergyRemaining;
    private long                liEnergyDissipated;

    private Vector              v;
    private Vector              cellForce;
    private Vector              parallelForce;
    private Vector              perpendicularForce;


    // adjustable settings
    private static float        fFriction           =FRICTION;
    private static long         liMinimumEnergy     =MINIMUM_CELLENERGY;
    protected static long       liInitialEnergy     =INITIAL_CELLENERGY;
    private static long         liReproductionEnergy=REPRODUCTION_CELLENERGY;
    private static float        fMutationChance     =MUTATION_CHANCE;
    private static boolean      bAllowCellMutation  =true;
    private static boolean      bAllowCellCreation  =true;
    private static boolean      bAllowCellDeletion  =true;


    /** Creates new creature. Position and orientation are defined.
     *  @param          fXPos X coordinate of initial position of the creature
     *  @param          fYPos Y coordinate of initial position of the creature
     *  @param          fHeading initial heading of the creature
     */
    public Creature (float fXPos, float fYPos, float fHeading)
    {
        startCreature(fXPos, fYPos, fHeading, 1);
    }


    /** Creates new creature. Position, orientation and generation are defined.
     *  @param          fXPos X coordinate of initial position of the creature
     *  @param          fYPos Y coordinate of initial position of the creature
     *  @param          fHeading initial heading of the creature
     *  @param          iGeneration Initial generation of this creature
     */
    public Creature (float fXPos, float fYPos, float fHeading,
                     int iGeneration)
    {
        startCreature(fXPos, fYPos, fHeading, iGeneration);
    }


    /** Starts the creation process of a new creature
     *  @param          fXPos X coordinate of initial position of the creature
     *  @param          fYPos Y coordinate of initial position of the creature
     *  @param          fHeading initial heading of the creature
     */
    private void startCreature(float fXPos, float fYPos, float fHeading,
                               int iGeneration)
    {
        this.fXPos          =fXPos;
        this.fYPos          =fYPos;
        this.fHeading       =fHeading;
        this.iGeneration    =iGeneration;
        fXCenterOfMass      =0.0f;
        fYCenterOfMass      =0.0f;
        cellChain           =null;
        fSize               =0.0f;
        nCells              =0;
        speed               =new Vector(0.0f, 0.0f);
        force               =new Vector(0.0f, 0.0f);
        acceleration        =new Vector(0.0f, 0.0f);
        v                   =new Vector();
        cellForce           =new Vector();
        parallelForce       =new Vector();
        perpendicularForce  =new Vector();
    }

    /** Updates the creature when the static settings have changed.
     */
    public void updateForNewSettings()
    {
        Cell cell;

        // Set the initial cell energy
        cell=cellChain;                              
        while (cell!=null)
        {
            cell.setEnergy(liInitialEnergy);
            cell=(Cell)cell.getNextChainItem();
        }

        // create the updated creature
        createCreature();
    }

    /** Sets the playing field dimensions
     *  @param          iPlayingFieldWidth Width of playing field
     *  @param          iPlayingFieldHeight Height of playing field
     */
    public static void setPlayingFieldDimension(int iPlayingFieldWidth,
                                                int iPlayingFieldHeight)
    {
        fPlayingFieldWidth =(float)iPlayingFieldWidth;
        fPlayingFieldHeight=(float)iPlayingFieldHeight;
    }


    /** Just adds a cell to the creature. No updates are made to the creature
     *  itself (like center of mass, etc). Functional Cells are inserted at the
     *  beginning of the Cell chain, Brain Cells at the end.
     *  @param          cell The cell to be added.
     */
    public void addCell(Cell cell)
    {
        if (cell instanceof BrainCell)
        {
            if (cellChain==null)
            {
                cellChain=cell;
                firstBrainCell=(BrainCell)cell;
            }
            else
            {
                cellChain.appendItemToChain(cell);
            }
        }
        else
        {
            if (cellChain==null)
                cellChain=cell;
            else
                cellChain=(Cell)cellChain.insertItemAtBeginOfChain(cell);

        }
        nCells++;
    }


     /** This routine finishes the creature creation process. This routine
     *   calculates the center of mass position, the angular momentum and the
     *   size of the creature.
     */
    public void createCreature()
    {
        Cell  cell;
        float fXSum;
        float fYSum;
        int   iCount;
        float fDistance;

        iCount=0;
        fXSum=0.0f;
        fYSum=0.0f;
        liEnergy=0;

        cell=cellChain;                              // calculate center of mass
        while (cell!=null)
        {
            cell.resetRelativePosition();
            fXSum+=cell.getXRelPos();
            fYSum+=cell.getYRelPos();
            liEnergy+=cell.getEnergy();
            iCount++;
            cell=(Cell)cell.getNextChainItem();
        }
        nCells=iCount;

        if (iCount>0)
        {
            fXCenterOfMass=fXSum/(float)iCount;             // calculate center of mass position
            fYCenterOfMass=fYSum/(float)iCount;
            fMomentOfInertia=0.0f;                          // initialize angular momentum

            cell=cellChain;                                 // start with 1st cell in chain
            while (cell!=null)                              // process all cells
            {
                cell.translateRelativePosition(-fXCenterOfMass, -fYCenterOfMass);
                cell.iterationUpdatePosition(fXPos, fYPos, fHeading);

                fDistance=(float)Math.sqrt(Math.pow(cell.getXRelPos(), 2.0)+
                                           Math.pow(cell.getYRelPos(), 2.0));
                if (fDistance>fSize)                                // calculate size of creature
                    fSize=fDistance;

                fMomentOfInertia+=Cell.getMass()*fDistance*fDistance;    // update the moment of inertia

                cell=(Cell)cell.getNextChainItem();                 // proceed to next cell
            }
            liEnergyPerCell=liEnergy/(long)nCells;
            liEnergyRemaining=liEnergy%(long)nCells;
        }
        else
        {
            liEnergyPerCell=0;
            liEnergyRemaining=0;
        }
    }

    /** Kills the creature
     *  @return         Manure Chain of manure which remains after the killing
     */

    public Manure killCreature()
    {
        Manure  manureChain=null;
        Manure  manure;
        Cell    cell;

        cell=cellChain;
        while (cell!=null)
        {
            manure=new Manure(cell.getXPos(), cell.getYPos(), cell.getEnergy());
            if (manureChain==null)
                manureChain=manure;
            else
                manureChain.appendItemToChain(manure);
            cell=(Cell)cell.getNextChainItem();
        }
        return manureChain;
    }

    /**
     * Resets the iterative updates
     */
    public void updateReset()
    {
        Cell cell;

        cell=(Cell)cellChain;
        while (cell!=null)
        {
            cell.updateReset();
            cell=(Cell)cell.getNextChainItem();
        }

    }

    /** Updates the creature to the presence of the other creature: sensor
     *  output is calculated and the other creature is consumed
     *  @param          otherCreature The other creature
     */
    public void updateToEnvironment(Creature otherCreature)
    {
        Cell currCell;
        Cell otherCell;


        currCell=(Cell)cellChain;
        while (currCell!=null)
        {
            otherCell=(Cell)otherCreature.getCellChain();
            while (otherCell!=null)
            {
                currCell.updateToEnvironment(otherCell);
                otherCell=(Cell)otherCell.getNextChainItem();
            }
            currCell=(Cell)currCell.getNextChainItem();
        }

    }

    /** 
     * Updates the creature to the presence of a particular manure: sensor
     * output is calculated and the manure is consumed
     * @param manure Manure to update the creature for
     */
    public void updateToEnvironment(Manure manure)
    {
        Cell cell;

        cell=(Cell)cellChain;
        while (cell!=null)
        {
            cell.updateToEnvironment(manure);
            cell=(Cell)cell.getNextChainItem();
        }

    }


    /**
     * Iteration update of the creature
     */
    public void iterationUpdate()
    {
        boolean bBrainPresent;

        fEnergyDissipated=0.0f;                     // reset the dissipated energy

        if (firstBrainCell!=null)
        {
            updateCellForcesByBrain();              // optional brain action
        }
        else
        {
            updateCellForces();                     // default cell force update
        }
        updateCreatureForces();     // update the force acting on the creature
        updateSpeed();              // update speed and angular speed of creature
        updateHeading();            // update creature heading
        updatePosition();           // update creature speed
        updateEnergy();             // update creature energy
        updateCellPositions();      // update position of creatures cells
    }


    /**
     * Calculates the forces that each cell applies to the creature. This is
     * the default process which can be overruled by a braincell (if present).
     */
    private void updateCellForces()
    {
        FunctionalCell currCell;
        FunctionalCell otherCell;

        currCell=(FunctionalCell)cellChain;
        while (currCell!=firstBrainCell)
        {
            otherCell=(FunctionalCell)cellChain;
            while (otherCell!=firstBrainCell)
            {
                currCell.accumulateDragForce(otherCell);
                otherCell=(FunctionalCell)otherCell.getNextChainItem();
            }

            currCell=(FunctionalCell)currCell.getNextChainItem();
        }
    }

    /**
     * Transforms sensory input to some action by the Brain Cells
     *
     * @return true if at least one brain cell performed some action, false if
     * no brain cell present
     */
    private boolean updateCellForcesByBrain()
    {
        BrainCell cell;
        boolean bActionPerformed;

        bActionPerformed=false;
        cell=(BrainCell)firstBrainCell;
        while (cell!=null)
        {
            bActionPerformed |= ((BrainCell)cell).performBrainAction(this);
            cell=(BrainCell)cell.getNextChainItem();
        }
        return bActionPerformed;
    }


    /**
     * Updates the forces on the creature
     */
    private void updateCreatureForces()
    {
        Cell   cell;

        force.setVector(0.0f, 0.0f);
        fTorque=0.0f;
        cell=(Cell)cellChain;
        while (cell!=null)
        {
            v.setVector(0.0f, 0.0f, cell.getXRelPos(), cell.getYRelPos());
            cellForce.setVector(cell.getDragForce());

            parallelForce=Vector.getParallelComponent(cellForce, v);
            perpendicularForce=Vector.getPerpendicularComponent(cellForce, v);

            force.add(/*parallelForce*/cellForce);
            fTorque-=perpendicularForce.getLength()*v.getLength();      // Torque: N=f.r

            cell=(Cell)cell.getNextChainItem();
        }
    }

    /** 
     * Updates the speed and angular speed of the creature
     */
    private void updateSpeed()
    {
        float a;
        float v;
        float fMass;

        fMass=Cell.getMass();

        speed.multiply(fFriction);                              // not quite right in a physical sense

        acceleration.setVector(force);                          // acc=force/mass
        acceleration.multiply(1.0f/((float)nCells*fMass));
        acceleration.multiply(Constants.TIME_UNIT);             // per time unit!

        a=acceleration.getLength();
        v=speed.getLength();
        fEnergyDissipated+=nCells*fMass*(a*a+2.0f*a*v)*Cell.getAccelerationEnergyUnit();

        speed.add(acceleration);
    }


    /**
     * Updates the heading of the creature according to the angular speed. The
     * angular speed is updated for angular acceleration and friction.
     */
    private void updateHeading()
    {
        if (fMomentOfInertia>0.0f)
        {
            fAngularAcceleration=fTorque/fMomentOfInertia;
        }
        else
        {
            fAngularAcceleration=0.0f;
        }

        fAngularSpeed*=fFriction;
        fAngularSpeed+=fAngularAcceleration*Constants.TIME_UNIT;

        fHeading+=fAngularSpeed;
        while (fHeading>(float)Math.PI*2.0f)
            fHeading-=(float)Math.PI*2.0f;
        while (fHeading<0.0f)
            fHeading+=(float)Math.PI*2.0f;
    }


    /** Updates the position of the creature
     */
    private void updatePosition()
    {
        fXPos+=Constants.TIME_UNIT*speed.getLength()*(float)Math.cos(speed.getAngle());
        fYPos+=Constants.TIME_UNIT*speed.getLength()*(float)Math.sin(speed.getAngle());

        if (fXPos-fSize<0)
        {
            fXPos+=fSize-fXPos;
            speed.xMirror();
        }
        if (fXPos+fSize>fPlayingFieldWidth)
        {
            fXPos-=fXPos+fSize-fPlayingFieldWidth;
            speed.xMirror();
        }
        if (fYPos-fSize<0)
        {
            fYPos+=fSize-fYPos;
            speed.yMirror();
        }
        if (fYPos+fSize>fPlayingFieldHeight)
        {
            fYPos-=fYPos+fSize-fPlayingFieldHeight;
            speed.yMirror();
        }
    }

    /**
     * Updates the energy for the energy dissipated. Energy is distributed
     * equally amongst the cells.
     */
    private void updateEnergy()
    {
        Cell cell;

        liEnergy=0;
        cell=cellChain;
        while (cell!=null)
        {
            liEnergy+=cell.getUpdatedEnergy();
            cell=(Cell)cell.getNextChainItem();
        }

        fEnergyDissipated+=Constants.TIME_UNIT*nCells*Cell.getEnergyDissipation();
        liEnergyDissipated=(long)fEnergyDissipated;
        if (liEnergyDissipated<=liEnergy)
            liEnergy-=liEnergyDissipated;
        else
        {
            liEnergyDissipated=liEnergy;
            liEnergy=0;
        }
        liEnergyPerCell=liEnergy/(long)nCells;
        liEnergyRemaining=liEnergy%(long)nCells;


        cell=cellChain;
        while (cell!=null)
        {
            cell.setEnergy(liEnergyPerCell);
            if (liEnergyRemaining>0)
            {
                cell.increaseEnergy((long)1);
                liEnergyRemaining--;
            }
            cell=(Cell)cell.getNextChainItem();
        }
    }

    /**
     * Update the position of the individual cells for the new situation.
     */
    private void updateCellPositions()
    {
        Cell cell;

        cell=cellChain;
        while (cell!=null)
        {
            cell.iterationUpdatePosition(fXPos, fYPos, fHeading);
            cell=(Cell)cell.getNextChainItem();
        }
    }


    /**
     * This method returns the x coordinate of the position of the creature.
     * @return         The x coordinate
     */
    public float getXPos()
    {
        return fXPos;
    }


    /**
     * This method returns the y coordinate of the position of the creature.
     * @return         The y coordinate
     */
    public float getYPos()
    {
        return fYPos;
    }


    /**This method returns the number of cells in the creature.
     * @return The number of cells in the creature
     */
    public int getNumberOfCells()
    {
        return nCells;
    }

    public float getSize()
    {
        return fSize;
    }

    public float getXCenterOfMass()
    {
        return fXCenterOfMass;
    }

    public float getYCenterOfMass()
    {
        return fYCenterOfMass;
    }

    public float getDistance(Creature creature)
    {
        return (float)Math.sqrt(Math.pow(fXPos-creature.getXPos(), 2)+
                                Math.pow(fYPos-creature.getYPos(), 2));
    }

    public float getDistance(Manure manure)
    {
        return (float)Math.sqrt(Math.pow(fXPos-manure.getXPos(), 2)+
                                Math.pow(fYPos-manure.getYPos(), 2));
    }


    public void updateSpeed(Vector deltaV)
    {
        speed.add(deltaV);
    }


    /**
     * This method draws the Creatgure
     * @param g Graphics environment to use for drawing
     * @param bShowInfo Indicates whether or not information must be shown
     */
    public void paint(Graphics g, boolean bShowInfo)
    {
        Cell cell;
        CellCapabilities capabilities;
        cell=(Cell)cellChain;

        // draw Cells
        while (cell!=null)
        {
            cell.paint(g);
            cell=(Cell)cell.getNextChainItem();
        }



        if (bShowInfo)
        {
            // draw Center of Mass
            g.setColor(Color.blue);
            g.drawLine((int)fXPos-5, (int)fYPos-5,
                       (int)fXPos+5, (int)fYPos+5);
            g.drawLine((int)fXPos+5, (int)fYPos-5,
                       (int)fXPos-5, (int)fYPos+5);

            // show generation
            g.setColor(Color.red);
            g.drawString(String.valueOf(iGeneration), (int)(fXPos-fSize-20), (int)fYPos);

            // show the small bar graphs
            capabilities=getCapabilityPerCell();
//            g.setColor(Color.gray);
//            g.fillRect((int)(fXPos+fSize), (int)fYPos-7, 40  , 16);
            g.setColor(Color.red);
            g.drawRect((int)(fXPos+fSize), (int)fYPos-6, (int)(40.0f*capabilities.PreySensorPerformance)  , 1);
            g.drawRect((int)(fXPos+fSize), (int)fYPos-3, (int)(40.0f*capabilities.PreyConsumerPerformance), 1);
            g.setColor(Color.blue);
            g.drawRect((int)(fXPos+fSize), (int)fYPos  , (int)(40.0f*capabilities.ManureSensorPerformance), 1);
            g.drawRect((int)(fXPos+fSize), (int)fYPos+3, (int)(40.0f*capabilities.ManureConsumerPerformance), 1);
            g.setColor(Color.green);
            g.drawRect((int)(fXPos+fSize), (int)fYPos+6, (int)(40.0f*capabilities.DraggerPerformance), 1);


        }
    }

    public Cell getCellChain()
    {
        return cellChain;
    }

    /**
     * This method creates an exact copy of this Creature
     * @return The copy of the Creature
     */
    public Creature copy()
    {
        Cell            cell;
        Cell            newCell;
        long            liCellEnergy;
        Creature        creature;
        Vector          deltaV;

        creature=new Creature(fXPos, fYPos, fHeading);

        cell=(Cell)cellChain;
        while (cell!=null)
        {
            liCellEnergy=cell.getEnergy();
            newCell=cell.copy();
            newCell.setEnergy(liCellEnergy);
            creature.addCell(newCell);
            cell=(Cell)cell.getNextChainItem();
        }
        creature.createCreature();


        return creature;
    }

    public Creature splitCreature()
    {
        Cell            cell;
        Cell            newCell;
        long            liCellEnergy;
        Creature        creature;
        Vector          deltaV;

        creature=new Creature(fXPos, fYPos, fHeading, iGeneration+1);

        cell=(Cell)cellChain;
        while (cell!=null)
        {
            liCellEnergy=cell.getEnergy();
            cell.decreaseEnergy(liCellEnergy/(long)2);
            newCell=cell.copy();
            newCell.setEnergy(liCellEnergy/(long)2);
            creature.addCell(newCell);
            cell=(Cell)cell.getNextChainItem();
        }
        creature.createCreature();

        creature.updateSpeed(speed);

        deltaV=new Vector((float)(Math.random()*Math.PI), 2+(int)(20.0*Math.random()));
        updateSpeed(deltaV);
        deltaV.negative();
        creature.updateSpeed(deltaV);

        creature.mutate();

        return creature;
    }

    /**
     * This method randomly mutates the Creature. If allowed, a Cell is
     * added, deleted or mutated.
     */
    private void mutate()
    {
        int             iMutation;
        Cell            cell;
        float           fIncrement;

        if (Math.random()<fMutationChance)
        {
            // Get a random number 0, 1 or 2
            iMutation=(int)(Math.random()*POSSIBLE_MUTATIONS);

            // If 0 -> create cell
            if (iMutation==0)
            {
                if (bAllowCellCreation)
                {
                    mutationAddCell();
                }

            }
            if (iMutation==1)
            {
                if (bAllowCellDeletion)
                {
                    mutationRemoveCell();
                }
            }
            if (iMutation==2)
            {
                if (bAllowCellMutation)
                {
                    cell=getRandomCell();
                    if (cell!=null)
                    {
                        System.out.println("Mutatation: mutate cell");
                        cell.mutate();
                        nCreaturesMutated++;
                    }
                }
            }
        }
    }

    /**
     * This method returns the number of mutations that have taken place.
     * @return The number of mutations.
     */
    public static int getMutatedCreatures()
    {
        return nCreaturesMutated;
    }

    /**
     * This method picks a Cell from the chain at random.
     * @return Reference to the randomly picked Cell
     */
    public Cell getRandomCell()
    {
        int iCell;
        Cell cell;

        iCell=(int)(Math.random()*nCells);      // choose random cell
        cell=cellChain;                         // proceed chain to reach this one
        while (iCell>0)
        {
            cell=(Cell)cell.getNextChainItem();
            iCell--;
        }
        return cell;
    }


    public boolean cellExists(int iXGridPos, int iYGridPos)
    {
        Cell cell;
        boolean exists;

        exists=false;

        cell=cellChain;
        while ((cell!=null) && (!exists))
        {
            if ((cell.getXGridPos()==iXGridPos) &&
                (cell.getYGridPos()==iYGridPos))
                exists=true;
            else
                cell=(Cell)cell.getNextChainItem();
        }
        return exists;
    }

    public void mutationAddCell()
    {
        Cell cell;
        int  iDirection;
        int iX;
        int iY;

        iDirection=(int)(Math.random()*4.0);        // get random direction
        cell=getRandomCell();                       // get random cell in chain
        iX=cell.getXGridPos();                      // get its grid position
        iY=cell.getYGridPos();

        switch (iDirection)                         // find 1st empty place in given direction
        {
            case 0:
                do
                    iX++;
                while(cellExists(iX, iY));
                break;
            case 1:
                do
                    iX--;
                while(cellExists(iX, iY));
                break;
            case 2:
                do
                    iY++;
                while(cellExists(iX, iY));
                break;
            case 3:
                do
                    iY--;
                while(cellExists(iX, iY));
                break;
        }

        liEnergy=getEnergy();                                           // get creatures energy

        addCell(new FunctionalCell(iX, iY, FunctionalCell.TYPE_RANDOM, 0)); // create random type cell, append it to chain

        createCreature();                                               // re-create creature

        liEnergyPerCell=liEnergy/(long)nCells;                          // assign&equalize energy to creature
        liEnergyRemaining=liEnergy%(long)nCells;

        cell=(Cell)cellChain;
        while (cell!=null)
        {
            cell.setEnergy(liEnergyPerCell);
            if (liEnergyRemaining>0)
            {
                cell.increaseEnergy((long)1);
                liEnergyRemaining--;
            }
            cell=(Cell)cell.getNextChainItem();
        }
        System.out.println("Mutatation: add cell");
        nCreaturesMutated++;
    }

    public void mutationRemoveCell()
    {
        Cell cell;
        long liCellEnergy;
        long liCellEnergyIncrement;
        long liCellEnergyRemaining;


        if (nCells>1)                                   // there should be at least 2 cells
        {
            cell=getRandomCell();                       // get one of the creatures cells
            liCellEnergy=cell.getEnergy();              // remember it's energy
            cellChain=(Cell)cell.removeItemFromChain(); // remove the cell from the chain
            createCreature();                           // recreate the creature

            liCellEnergyIncrement=liCellEnergy/(long)nCells;    // add cell's energy to...
            liCellEnergyRemaining=liCellEnergy%(long)nCells;    // .. the rest of the cells
            cell=cellChain;
            while (cell!=null)
            {
              cell.increaseEnergy(liCellEnergyIncrement);
              while (liCellEnergyRemaining>0)
              {
                  cell.increaseEnergy((long)1);
                  liCellEnergyRemaining--;
              }
              cell=(Cell)cell.getNextChainItem();
            }
            System.out.println("Mutatation: remove cell");
            nCreaturesMutated++;
        }
    }

    /**
     * This method returns the name of the Creature.
     *
     * @return The name of the Creature
     */
    public String getName()
    {
        return sName;
    }

    /**
     * This method returns the type of the Creature.
     *
     * @return The type of the Creature
     */
    public String getType()
    {
        return sType;
    }

    public long getEnergyPerCell()
    {
        return liEnergyPerCell;
    }

    public long getDissipatedEnergy()
    {
        return liEnergyDissipated;
    }

    /**
     * This method returns the total Energy of the Creature. This is the
     * sum of the Energy level of the individual Cells of the Creature.
     * @return
     */
    public long getEnergy()
    {
        long liEnergy;
        Cell cell;

        liEnergy=0;
        cell=cellChain;
        while (cell!=null)
        {
            liEnergy+=cell.getEnergy();
            cell=(Cell)cell.getNextChainItem();
        }
        return liEnergy;
    }


    public static void setFriction(float fNewFriction)
    {
        fFriction=fNewFriction;
        fFriction=Math.max(fFriction, 0.0f);
        fFriction=Math.min(fFriction, 1.0f);
    }

    public static float getFriction()
    {
        return fFriction;
    }

    public static void setMutationChance(float fNewMutationChance)
    {
        fMutationChance=fNewMutationChance;
        fMutationChance=Math.max(fMutationChance, 0.0f);
        fMutationChance=Math.min(fMutationChance, 1.0f);
    }

    public static float getMutationChance()
    {
        return fMutationChance;
    }

    public static void setMinimumCellEnergy(long liNewMinimumEnergy)
    {
        liMinimumEnergy=liNewMinimumEnergy;
        liMinimumEnergy=Math.max(liMinimumEnergy, 0);
    }

    public static long getMinimumCellEnergy()
    {
        return liMinimumEnergy;
    }

    public static void setReproductionCellEnergy(long liNewReproductionEnergy)
    {
        liReproductionEnergy=liNewReproductionEnergy;
        liReproductionEnergy=Math.max(liReproductionEnergy, 0);
        FunctionalCell.setReproductionEnergy(liReproductionEnergy);
    }

    public static long getReproductionCellEnergy()
    {
        return liReproductionEnergy;
    }


    public static void setInitialCellEnergy(long liNewInitialEnergy)
    {
        liInitialEnergy=liNewInitialEnergy;
        liInitialEnergy=Math.max(liInitialEnergy, 0);
    }

    public static long getInitialCellEnergy()
    {
        return liInitialEnergy;
    }

    public void setPosition(float fXPos, float fYPos)
    {
        if ((fXPos>0) && (fXPos<fPlayingFieldWidth))
        {
            this.fXPos=fXPos;
        }
        else
        {
            this.fXPos=fPlayingFieldWidth/2.0f;
        }
        if ((fYPos)>0 && (fYPos<fPlayingFieldHeight))
        {
            this.fYPos=fYPos;
        }
        else
        {
            this.fYPos=fPlayingFieldHeight/2.0f;
        }
    }

    /**
     * This method returns the average capabilities per Cell of the Creature
     * @return Structure containing the performance values.
     */
    public CellCapabilities getCapabilityPerCell()
    {
        Cell cell;
        FunctionalCell functionalCell;
        CellCapabilities capabilities;
        int iCellCount;

        capabilities=new CellCapabilities();

        capabilities.DraggerPerformance=0;
        capabilities.ManureConsumerPerformance=0;
        capabilities.ManureSensorPerformance=0;
        capabilities.PreyConsumerPerformance=0;
        capabilities.PreySensorPerformance=0;
        iCellCount=0;

        cell=this.cellChain;
        while (cell!=null)
        {
            if (cell instanceof FunctionalCell)
            {
                functionalCell=(FunctionalCell)cell;
                capabilities.DraggerPerformance         +=functionalCell.getDraggerPerformance();
                capabilities.ManureConsumerPerformance  +=functionalCell.getManureConsumerPerformance();
                capabilities.ManureSensorPerformance    +=functionalCell.getManureSensorPerformance();
                capabilities.PreyConsumerPerformance    +=functionalCell.getPreyConsumerPerformance();
                capabilities.PreySensorPerformance      +=functionalCell.getPreySensorPerformance();
                iCellCount++;
            }
            cell=(Cell)cell.getNextChainItem();
        }

        if (iCellCount>0)
        {
            capabilities.DraggerPerformance         /=iCellCount;
            capabilities.ManureConsumerPerformance  /=iCellCount;
            capabilities.ManureSensorPerformance    /=iCellCount;
            capabilities.PreyConsumerPerformance    /=iCellCount;
            capabilities.PreySensorPerformance      /=iCellCount;
        }
        return capabilities;
    }
}

