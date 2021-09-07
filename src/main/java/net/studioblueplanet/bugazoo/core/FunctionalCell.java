package net.studioblueplanet.bugazoo.core;
import java.awt.*;

import net.studioblueplanet.bugazoo.common.Constants;

public class FunctionalCell extends Cell
{

    // public defs
    public static final int   NUMBER_OF_TYPES       =6; // number of predefined types
    public static final int   NUMBER_OF_PERFORMANCES=5;
    public static final int   TYPE_DRAGGER          =0; // actual types
    public static final int   TYPE_MANURESENSOR     =1;
    public static final int   TYPE_PREYSENSOR       =2;
    public static final int   TYPE_MANURECONSUMER   =3;
    public static final int   TYPE_PREYCONSUMER     =4;
    public static final int   TYPE_MULTIFUNCTIONAL  =5;


    public static final int   TYPE_NOTYPE           =6; // special types
    public static final int   TYPE_RANDOM           =7;

    public static final float CELLFORCE_MAX                     =50.0f;
    public static final float PREYSENSE_TO_FORCE_SCALING        =50.0f;
    public static final float PREDATORSENSE_TO_FORCE_SCALING    =80f;
    public static final float MANURESENSE_TO_FORCE_SCALING      =70.0f;
    public static final float MUTATION_INCREMENT                =0.4f;

    // private defs
    private static final float SENSOR_RANGE                     =20.0f;
    private static final float CONSUMER_RANGE                   =3.0f;          // relative: number of cellsizes
    private static final float PREYCONSUMPTION_SPEED            =10000.0f;      // max amount of energy per cell per second
    private static final float MANURECONSUMPTION_SPEED          =100000.0f;     // max amount of energy per manure per sec

    private static final float REPRODUCTION_ENERGY              =1000000.0f;

    // variables
    private float fManureSensorPerformance;
    private float fPreySensorPerformance;
    private float fManureConsumerPerformance;
    private float fPreyConsumerPerformance;
    private float fDraggerPerformance;

    private Vector manureSense;
    private Vector preySense;
    private Vector predatorSense;
    private Vector nearestManure;
    private Vector nearestCell;
    private float  fNearestManureDistance;
    private float  fNearestCellDistance;


    private Vector relPos;
    private Vector repulseStrength;
    private Vector senseStrength;

    private Vector preyForce;
    private Vector manureForce;
    private Vector dragForce;
    private Vector predatorForce;

    private Color  rimColor;
    private Color  interiorColor;

	private int    iInitialType;

    // adjustable settings
    private static float    fSensorRange            =SENSOR_RANGE;
    private static float    fSqSensorRange          =SENSOR_RANGE*SENSOR_RANGE;
    private static float    fConsumerRange          =CONSUMER_RANGE;
    private static float    fSqConsumerRange        =CONSUMER_RANGE*CONSUMER_RANGE;
    private static float    fPreyConsumptionSpeed   =PREYCONSUMPTION_SPEED;
    private static float    fManureConsumptionSpeed =MANURECONSUMPTION_SPEED;

	private static float	fPreySensitivity        =PREYSENSE_TO_FORCE_SCALING;
	private static float	fPredatorSensitivity    =PREDATORSENSE_TO_FORCE_SCALING;
	private static float	fManureSensitivity      =MANURESENSE_TO_FORCE_SCALING;

    /** The energy at which the Cell reproduces */
    private static float    fReproductionEnergy     =REPRODUCTION_ENERGY;
    /** Normalised Prey sensitivity */
    private static float    fRelPreySensitivity     =fPreySensitivity/REPRODUCTION_ENERGY;
    /** Normalised Predator Sensitivity */
    private static float    fRelPredatorSensitivity =fPredatorSensitivity/1.0f;
    /** Normalised Manure Sensitivity */
    private static float    fRelManureSensitivity   =fManureSensitivity/REPRODUCTION_ENERGY;




    /** Creates a new cell of the given type at given position with given intial energy;
     *  @param          iXGridPos           column of relative position in grid of cell
     *  @param          iYGridPos           row of relative position in grid of cell
     *  @param          iType               type of cell: TYPE_DRAGGER, TYPE_MANURESENSOR,
     *                                      TYPE_MANURECONSUMER, TYPE_PREYSENSOR, TYPE_PREYCONSUMER,
     *                                      TYPE_MULTIFUNCTIONAL
     *  @param          liEnery             initial energy of cell
     */
    public FunctionalCell (int iXGridPos, int iYGridPos, int iType, long liEnergy)
    {
        super(iXGridPos, iYGridPos, liEnergy);

		iInitialType=iType;

        if (iType==TYPE_RANDOM)
        {
            iType=(int)(Math.random()*NUMBER_OF_TYPES);
        }

        switch (iType)
        {
            case TYPE_NOTYPE:
                fManureSensorPerformance  =0f;
                fPreySensorPerformance    =0f;
                fManureConsumerPerformance=0f;
                fPreyConsumerPerformance  =0f;
                fDraggerPerformance       =0f;
                break;
            case TYPE_DRAGGER:
                fManureSensorPerformance  =0f;
                fPreySensorPerformance    =0f;
                fManureConsumerPerformance=0f;
                fPreyConsumerPerformance  =0f;
                fDraggerPerformance       =1.0f;
                break;
            case TYPE_MANURESENSOR:
                fManureSensorPerformance  =1.0f;
                fPreySensorPerformance    =0f;
                fManureConsumerPerformance=0f;
                fPreyConsumerPerformance  =0f;
                fDraggerPerformance       =0f;
                break;
            case TYPE_PREYSENSOR:
                fManureSensorPerformance  =0f;
                fPreySensorPerformance    =1.0f;
                fManureConsumerPerformance=0f;
                fPreyConsumerPerformance  =0f;
                fDraggerPerformance       =0f;
                break;
            case TYPE_MANURECONSUMER:
                fManureSensorPerformance  =0f;
                fPreySensorPerformance    =0f;
                fManureConsumerPerformance=1.0f;
                fPreyConsumerPerformance  =0f;
                fDraggerPerformance       =0f;
                break;
            case TYPE_PREYCONSUMER:
                fManureSensorPerformance  =0f;
                fPreySensorPerformance    =0f;
                fManureConsumerPerformance=0f;
                fPreyConsumerPerformance  =1.0f;
                fDraggerPerformance       =0f;
                break;
            case TYPE_MULTIFUNCTIONAL:
                fManureSensorPerformance  =0.20f;
                fPreySensorPerformance    =0.20f;
                fManureConsumerPerformance=0.20f;
                fPreyConsumerPerformance  =0.20f;
                fDraggerPerformance       =0.20f;
                break;
        }
        manureSense     =new Vector(0.0f, 0.0f);
        preySense       =new Vector(0.0f, 0.0f);
        predatorSense   =new Vector(0.0f, 0.0f);
        dragForce       =new Vector(0.0f, 0.0f);
        preyForce       =new Vector(0.0f, 0.0f);
        manureForce     =new Vector(0.0f, 0.0f);
        predatorForce   =new Vector(0.0f, 0.0f);
        relPos          =new Vector(0.0f, 0.0f);
        senseStrength   =new Vector(0.0f, 0.0f);
        repulseStrength =new Vector(0.0f, 0.0f);
        calculateColors();
    }

    /** Reset the sensing strength and forces of the cell
     */
    public void updateReset()
    {
        super.updateReset();
        manureSense.setVector   (0.0f, 0.0f);
        preySense.setVector     (0.0f, 0.0f);
        predatorSense.setVector (0.0f, 0.0f);
        dragForce.setVector     (0.0f, 0.0f);
        preyForce.setVector     (0.0f, 0.0f);
        manureForce.setVector   (0.0f, 0.0f);
        predatorForce.setVector (0.0f, 0.0f);
        nearestManure=null;
        nearestCell=null;
    }



    /** Updates the cell (consumption, sensing strength) for the presense
     *  of another cell (belonging to another creature). The reason to combine
     *  both actions in one method is that for both actions the same
     *  performance intensive calculations have to be made.
     *  @param          otherCreatureCell The other creature's cell
     */
    public void updateToEnvironment(Cell otherCreatureCell)
    {
        float fSqDistance;
        float fRelSqDistance;
        float fRelDistance;
        float fOtherXPos;
        float fOtherYPos;

        // Position of the other cell
        fOtherXPos=otherCreatureCell.getXPos();
        fOtherYPos=otherCreatureCell.getYPos();


        // Square of the distance between me and the other cell
        fSqDistance=(fXPos-fOtherXPos)*(fXPos-fOtherXPos)+
                    (fYPos-fOtherYPos)*(fYPos-fOtherYPos);


        // Square of relative distance (distance/cellsize)
        fRelSqDistance=fSqDistance/fSqCellSize;
        if (fRelSqDistance<1.0f)
             fRelSqDistance=1.0f;

        // If cell within consuming range, consume!
        if (fRelSqDistance<fSqConsumerRange)
        {
            // this cell consumption
            this.updateConsuming(otherCreatureCell, fRelSqDistance);

            if (otherCreatureCell instanceof FunctionalCell)
            // other cell consumption
            ((FunctionalCell)otherCreatureCell).updateConsuming(this, fRelSqDistance);
        }

        // If other cell withing sensing range, update sense strengths
        if (fRelSqDistance<fSqSensorRange)
        {
            relPos.setVector(fXPos, fYPos, fOtherXPos, fOtherYPos);
            fRelDistance=(float)Math.sqrt(fRelSqDistance);
            this.updatePreySensing(otherCreatureCell, relPos, fRelDistance);
            this.updatePredatorSensing(otherCreatureCell, relPos, fRelSqDistance);
            relPos.negative();
            otherCreatureCell.updatePreySensing(this, relPos, fRelDistance);
            otherCreatureCell.updatePredatorSensing(this, relPos, fRelSqDistance);
        }
    }

    /** Consumes a bit of the energy of another cell (belonging to another
     *  creature). The amount of energy is added to this cell's energy, whereas
     *  the energy of the other cell is reduced by the same amount. The amount
     *  depends on the square of the distance between the cells, this cell's
     *  consumption ability and a factor called fPreyConsumptionSpeed.
     *  @param          otherCell The other cell
     *  @param          fRelSqDistance Square of the distance between the cells
     */
    public void updateConsuming(Cell otherCell, float fRelSqDistance)
    {
        long  iChunk;
        float fConsumeStrength;

        fConsumeStrength=fPreyConsumptionSpeed*Constants.TIME_UNIT*
                         fPreyConsumerPerformance/
                         fRelSqDistance;

        iChunk=(long)fConsumeStrength;
        iChunk=otherCell.decreaseUpdatedEnergy(iChunk);
        this.increaseUpdatedEnergy(iChunk);
    }

    /** Update the sensing strength of this cell for the presence of the other
     *  cell.
     *  @param          otherCell The other cell
     *  @param          relativePosition Relative position vector of the other
     *                  cell with respect to this cell
     *  @param          fRelDistance Distance between the cells
     */
    public void updatePreySensing(Cell otherCell, Vector relativePosition,
                                  float fRelDistance)
    {
        senseStrength.setVector(relativePosition.getAngle(),        // strength of sensing other creature's...
              fPreySensorPerformance/fRelDistance*                  // ...cell energy
              (float)otherCell.getEnergy());

        preySense.add(senseStrength);

        if (nearestCell==null)
        {
            nearestCell=new Vector(relativePosition);
            fNearestCellDistance=fRelDistance;
        }
        else
        {
            if (fRelDistance<fNearestCellDistance)
            {
                nearestCell=new Vector(relativePosition);
                fNearestCellDistance=fRelDistance;
            }
        }
    }

    /** Update the sensing strength of this cell for the presence of
     *  other cell's prey consumption ability (it is repulsed).
     *  @param          otherCell The other cell
     *  @param          relativePosition Relative position vector of the other
     *                  cell with respect to this cell
     *  @param          fRelSqDistance Distance between the cells
     */
    public void updatePredatorSensing(Cell otherCell, Vector relativePosition,
                                      float fRelSqDistance)
    {
        repulseStrength.setVector(relativePosition.getAngle(),                  // strength of sensing other creature's...
              (fPreySensorPerformance+fManureSensorPerformance)/fRelSqDistance* // ... prey consumption ability
              otherCell.getPreyConsumerPerformance());                          
        predatorSense.add(repulseStrength);

    }



    /** This method updates the cell for the environment as it comes to
     *  Manure sensing and consuming.
     *  @param          Manure Manure pile
     */
    public void updateToEnvironment(Manure manure)
    {
        float fConsumeStrength;
        float fSqDistance;
        float fRelSqDistance;
        float fRelDistance;
        float fOtherXPos;
        float fOtherYPos;
        long  liChunk;

        // Position of the manure
        fOtherXPos=manure.getXPos();
        fOtherYPos=manure.getYPos();

        // Square of distance from me to the Manure
        fSqDistance=(fXPos-fOtherXPos)*(fXPos-fOtherXPos)+
                    (fYPos-fOtherYPos)*(fYPos-fOtherYPos);

        // Square of relative distance
        fRelSqDistance=fSqDistance/fSqCellSize;

        // Minimize it to 1.0f
        if (fRelSqDistance<1.0f)
            fRelSqDistance=1.0f;

        // If Manure within consuming range, consume!!
        if (fRelSqDistance<fSqConsumerRange)
        {
            fConsumeStrength=fManureConsumptionSpeed*Constants.TIME_UNIT*
                            fManureConsumerPerformance/fRelSqDistance;    // 0 ..100

            liChunk=(long)fConsumeStrength;
            liChunk=manure.decreaseUpdatedEnergy(liChunk);
            this.increaseUpdatedEnergy(liChunk);
        }

        // If Manure withing sensing range, update sensing strength
        if (fRelSqDistance<fSqSensorRange)
        {
            relPos.setVector(fXPos, fYPos, fOtherXPos, fOtherYPos);
            fRelDistance=(float)Math.sqrt(fRelSqDistance);

            senseStrength.setVector(relPos.getAngle(),
                       fManureSensorPerformance/fRelDistance*(float)manure.getEnergy());

            manureSense.add(senseStrength);

            // Update nearest Manure pile
            if (nearestManure==null)
            {
                nearestManure=new Vector(relPos);
                fNearestManureDistance=fRelDistance;
            }
            else
            {
                if (fRelDistance<fNearestManureDistance)
                {
                    nearestManure=new Vector(relPos);
                    fNearestManureDistance=fRelDistance;
                }
            }
        }
    }

    /** This method calculates the net drag force on the Cell by
     *  summing the sensed strengths of the sensor cells.
     *  If the relative distance between this cell and the sensor cell is
     *  shorter, the sensed strength influences the drag force more.
     *  The net drag force also depends on the dragger performance of the cell
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void accumulateDragForce(Cell sensorCell)
    {
        float  fDistance;

        fDistance=this.getDistance(sensorCell)/fCellSize;
        if (fDistance<1.0f)
            fDistance=1.0f;

        preyForce.setVector(sensorCell.getPreySense());
        preyForce.multiply(fDraggerPerformance*fRelPreySensitivity/fDistance);

        manureForce.setVector(sensorCell.getManureSense());
        manureForce.multiply(fDraggerPerformance*fRelManureSensitivity/fDistance);

        predatorForce.setVector(sensorCell.getPredatorSense());
        predatorForce.negative();
        predatorForce.multiply(fDraggerPerformance*fRelPredatorSensitivity/fDistance);


        dragForce.add(preyForce);
        dragForce.add(manureForce);
        dragForce.add(predatorForce);

        // TO DO: PERFORMANCE OPTIMISATION: FIRST SUMM FORCES, THEN MULTIPLY BY PEFORMANCE/DISTANCE

    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void setDragForce(Vector force)
    {
        dragForce=force;
        dragForce.multiply(fDraggerPerformance);
    }


    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public Vector getPreySense()
    {
        return preySense;
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public Vector getManureSense()
    {
        return manureSense;
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public Vector getPredatorSense()
    {
        return predatorSense;
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public Vector getDragForce()
    {
        Vector returnForce;

        if (dragForce.getLength()>CELLFORCE_MAX)
        {
            returnForce=new Vector(dragForce);
            returnForce.setLength(CELLFORCE_MAX);
            return returnForce;
        }
        else
            return dragForce;
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public float getPreyConsumerPerformance()
    {
        return fPreyConsumerPerformance;
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public float getPreySensorPerformance()
    {
        return fPreySensorPerformance;
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public float getManureConsumerPerformance()
    {
        return fManureConsumerPerformance;
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public float getManureSensorPerformance()
    {
        return fManureSensorPerformance;
    }



    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public float getDraggerPerformance()
    {
        return fDraggerPerformance;
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void increasePreyConsumerPerformance(float fIncrement)
    {
        fPreyConsumerPerformance+=fIncrement;
        fPreyConsumerPerformance=(float)Math.min(fPreyConsumerPerformance, 1.0f);
        fPreyConsumerPerformance=(float)Math.max(fPreyConsumerPerformance, 0.0f);
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void increasePreySensorPerformance(float fIncrement)
    {
        fPreySensorPerformance+=fIncrement;
        fPreySensorPerformance=(float)Math.min(fPreySensorPerformance, 1.0f);
        fPreySensorPerformance=(float)Math.max(fPreySensorPerformance, 0.0f);
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void increaseManureConsumerPerformance(float fIncrement)
    {
        fManureConsumerPerformance+=fIncrement;
        fManureConsumerPerformance=(float)Math.min(fManureConsumerPerformance, 1.0f);
        fManureConsumerPerformance=(float)Math.max(fManureConsumerPerformance, 0.0f);
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void increaseManureSensorPerformance(float fIncrement)
    {
        fManureSensorPerformance+=fIncrement;
        fManureSensorPerformance=(float)Math.min(fManureSensorPerformance, 1.0f);
        fManureSensorPerformance=(float)Math.max(fManureSensorPerformance, 0.0f);
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void increaseDraggerPerformance(float fIncrement)
    {
        fDraggerPerformance+=fIncrement;
        fDraggerPerformance=(float)Math.min(fDraggerPerformance, 1.0f);
        fDraggerPerformance=(float)Math.max(fDraggerPerformance, 0.0f);
    }



    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public Cell copy()
    {
        FunctionalCell copy;

        copy=new FunctionalCell(getXGridPos(), getYGridPos(), TYPE_NOTYPE, 0);
        copy.setEnergy(getEnergy());
        copy.copyType(this);
        return copy;
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void mutate()
    {
        int iPerformance;
        float fIncrement;
        float fFactor;
        float fSum;

        iPerformance=(int)(Math.random()*NUMBER_OF_PERFORMANCES);
        fIncrement=(float)Math.random()*MUTATION_INCREMENT*2.0f-MUTATION_INCREMENT;

        switch (iPerformance)
        {
            case 0:
                fDraggerPerformance+=fIncrement;
                if (fDraggerPerformance<0.0f)
                {
                    fIncrement-=fDraggerPerformance;
                    fDraggerPerformance=0.0f;
                }
                if (fDraggerPerformance>1.0f)
                {
                    fIncrement-=(fDraggerPerformance-1.0f);
                    fDraggerPerformance=1.0f;
                }

                fSum=fPreySensorPerformance+fPreyConsumerPerformance+
                     fManureSensorPerformance+fManureConsumerPerformance;
                if (fSum>0.0f)
                {
                    fFactor=(fSum-fIncrement)/fSum;
                }
                else
                {
                    fFactor=0.0f;
                }
                fPreySensorPerformance      *=fFactor;
                fPreyConsumerPerformance    *=fFactor;
                fManureSensorPerformance    *=fFactor;
                fManureConsumerPerformance  *=fFactor;
                break;
            case 1:
                fPreySensorPerformance+=fIncrement;
                if (fPreySensorPerformance<0.0f)
                {
                    fIncrement-=fPreySensorPerformance;
                    fPreySensorPerformance=0.0f;
                }
                if (fPreySensorPerformance>1.0f)
                {
                    fIncrement-=(fPreySensorPerformance-1.0f);
                    fPreySensorPerformance=1.0f;
                }

                fSum=fDraggerPerformance+fPreyConsumerPerformance+
                     fManureConsumerPerformance+fManureSensorPerformance;
                if (fSum>0.0f)
                {
                    fFactor=(fSum-fIncrement)/fSum;
                }
                else
                {
                    fFactor=0.0f;
                }
                fDraggerPerformance*=fFactor;
                fPreyConsumerPerformance*=fFactor;
                fManureSensorPerformance*=fFactor;
                fManureConsumerPerformance*=fFactor;
                break;
            case 2:
                fPreyConsumerPerformance+=fIncrement;
                if (fPreyConsumerPerformance<0.0f)
                {
                    fIncrement-=fPreyConsumerPerformance;
                    fPreyConsumerPerformance=0.0f;
                }
                if (fPreyConsumerPerformance>1.0f)
                {
                    fIncrement-=(fPreyConsumerPerformance-1.0f);
                    fPreyConsumerPerformance=1.0f;
                }

                fSum=fPreySensorPerformance+fDraggerPerformance+
                     fManureConsumerPerformance+fManureSensorPerformance;
                if (fSum>0.0f)
                {
                    fFactor=(fSum-fIncrement)/fSum;
                }
                else
                {
                    fFactor=0.0f;
                }
                fPreySensorPerformance*=fFactor;
                fDraggerPerformance*=fFactor;
                fManureSensorPerformance*=fFactor;
                fManureConsumerPerformance*=fFactor;
                break;
            case 3:
                fManureSensorPerformance+=fIncrement;
                if (fManureSensorPerformance<0.0f)
                {
                    fIncrement-=fManureSensorPerformance;
                    fManureSensorPerformance=0.0f;
                }
                if (fManureSensorPerformance>1.0f)
                {
                    fIncrement-=(fManureSensorPerformance-1.0f);
                    fManureSensorPerformance=1.0f;
                }

                fSum=fPreySensorPerformance+fPreyConsumerPerformance+
                     fManureConsumerPerformance+fDraggerPerformance;
                if (fSum>0.0f)
                {
                    fFactor=(fSum-fIncrement)/fSum;
                }
                else
                {
                    fFactor=0.0f;
                }
                fPreySensorPerformance*=fFactor;
                fPreyConsumerPerformance*=fFactor;
                fDraggerPerformance*=fFactor;
                fManureConsumerPerformance*=fFactor;
                break;
            case 4:
                fManureConsumerPerformance+=fIncrement;
                if (fManureConsumerPerformance<0.0f)
                {
                    fIncrement-=fManureConsumerPerformance;
                    fManureConsumerPerformance=0.0f;
                }
                if (fManureConsumerPerformance>1.0f)
                {
                    fIncrement-=(fManureConsumerPerformance-1.0f);
                    fManureConsumerPerformance=1.0f;
                }

                fSum=fPreySensorPerformance+fPreyConsumerPerformance+
                     fDraggerPerformance+fManureSensorPerformance;
                if (fSum>0.0f)
                {
                    fFactor=(fSum-fIncrement)/fSum;
                }
                else
                {
                    fFactor=0.0f;
                }
                fPreySensorPerformance      *=fFactor;
                fPreyConsumerPerformance    *=fFactor;
                fManureSensorPerformance    *=fFactor;
                fDraggerPerformance         *=fFactor;
                break;
        }
        fDraggerPerformance       =Math.min(fDraggerPerformance, 1.0f);
        fDraggerPerformance       =Math.max(fDraggerPerformance, 0.0f);
        fManureSensorPerformance  =Math.min(fManureSensorPerformance, 1.0f);
        fManureSensorPerformance  =Math.max(fManureSensorPerformance, 0.0f);
        fManureConsumerPerformance=Math.min(fManureConsumerPerformance, 1.0f);
        fManureConsumerPerformance=Math.max(fManureConsumerPerformance, 0.0f);
        fPreySensorPerformance    =Math.min(fPreySensorPerformance, 1.0f);
        fPreySensorPerformance    =Math.max(fPreySensorPerformance, 0.0f);
        fPreyConsumerPerformance  =Math.min(fPreyConsumerPerformance, 1.0f);
        fPreyConsumerPerformance  =Math.max(fPreyConsumerPerformance, 0.0f);

        calculateColors();

System.out.print(fPreyConsumerPerformance+fPreySensorPerformance+fManureConsumerPerformance+
                   fManureSensorPerformance+fDraggerPerformance);
System.out.println(" PC"+fPreyConsumerPerformance+
                   " PS"+fPreySensorPerformance+
                   " MC"+fManureConsumerPerformance+
                   " MS"+fManureSensorPerformance+
                   " DR"+fDraggerPerformance);

    }

    private void calculateColors()
    {
        float fRimBlue;
        float fRimGreen;
        float fRimRed;
        float fIntBlue;
        float fIntGreen;
        float fIntRed;

        fRimRed=2*fPreySensorPerformance-
                fPreySensorPerformance*fPreySensorPerformance;
        fRimBlue=2*fManureSensorPerformance-
                 fManureSensorPerformance*fManureSensorPerformance;
        fRimGreen=0.0f;
        fIntRed=2*fPreyConsumerPerformance-
                fPreyConsumerPerformance*fPreyConsumerPerformance;
        fIntGreen=2*fDraggerPerformance-
                  fDraggerPerformance*fDraggerPerformance;
        fIntBlue=2*fManureConsumerPerformance-
                 fManureConsumerPerformance*fManureConsumerPerformance;

        fRimRed=Math.min(fRimRed,1.0f);
        fRimRed=Math.max(fRimRed,0.0f);
        fRimGreen=Math.min(fRimGreen,1.0f);
        fRimGreen=Math.max(fRimGreen,0.0f);
/*
        rimColor=new Color(fPreySensorPerformance, 0, fManureSensorPerformance);
        interiorColor=new Color(fPreyConsumerPerformance, fDraggerPerformance, fManureConsumerPerformance);
*/
        rimColor=new Color(fRimRed, fRimGreen, fRimBlue);
        interiorColor=new Color(fIntRed, fIntGreen, fIntBlue);
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void paint(Graphics g)
    {
        g.setColor(rimColor);
        g.fillOval ((int)fXPos-iCellSize/2,
                    (int)fYPos-iCellSize/2,
                    iCellSize,
                    iCellSize);
        g.setColor(interiorColor);
        g.fillOval ((int)fXPos-iCellSize/4,
                    (int)fYPos-iCellSize/4,
                    iCellSize/2,
                    iCellSize/2);
/*
        g.setColor(Color.black);
        preySense.draw(g, (int)fXPos, (int)fYPos, fPreySensitivity*5);
        g.setColor(Color.green);
        predatorSense.draw(g, (int)fXPos, (int)fYPos, fPredatorSensitivity*5);
        g.setColor(Color.red);
        manureSense.draw(g, (int)fXPos, (int)fYPos, fManureSensitivity);
*/
    }


    /**	Paint the Cell at indicated location at indicated size
     *  @param          g Graphics of the component to draw on
     *  @param          fXPos X coordinate of the location to draw the Cell
     *  @param          fYPos Y coordinate of the location to draw the Cell
     *  @param          iCellSize Size of the cell to be drawn
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void paintLarge(Graphics g, float fXPos, float fYPos, int iCellSize)
    {
        g.setColor(rimColor);
        g.fillOval ((int)fXPos-iCellSize/2,
                    (int)fYPos-iCellSize/2,
                    iCellSize,
                    iCellSize);
        g.setColor(interiorColor);
        g.fillOval ((int)fXPos-iCellSize/4,
                    (int)fYPos-iCellSize/4,
                    iCellSize/2,
                    iCellSize/2);
    }


    /**	This method returns the initial type of the cell. The current type might be
	 *  different due to mutation.
     *  @param          -
     *  @return         The intial type at moment of creation.
     *  @exception      -
     */
	public int getInitialType()
	{
		return iInitialType;
	}

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public void copyType(FunctionalCell cell)
    {
        fManureSensorPerformance  =cell.getManureSensorPerformance();
        fManureConsumerPerformance=cell.getManureConsumerPerformance();
        fPreySensorPerformance    =cell.getPreySensorPerformance();
        fPreyConsumerPerformance  =cell.getPreyConsumerPerformance();
        fDraggerPerformance       =cell.getDraggerPerformance();
        calculateColors();
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public static void setSensorRange(float fNewSensorRange)
    {
        fSensorRange=fNewSensorRange;
        fSqSensorRange=fSensorRange*fSensorRange;
        fSensorRange=Math.max(fSensorRange, 0.0f);
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public static float getSensorRange()
    {
        return fSensorRange;
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public static void setConsumerRange(float fNewConsumerRange)
    {
        fConsumerRange=fNewConsumerRange;
        fSqConsumerRange=fConsumerRange*fConsumerRange;
        fConsumerRange=Math.max(fConsumerRange, 0.0f);
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public static float getConsumerRange()
    {
        return fConsumerRange;
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public static void setPreyConsumptionSpeed(float fNewSpeed)
    {
        fPreyConsumptionSpeed=fNewSpeed;
        fPreyConsumptionSpeed=Math.max(fPreyConsumptionSpeed, 0.0f);
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public static float getPreyConsumptionSpeed()
    {
        return fPreyConsumptionSpeed;
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public static void setManureConsumptionSpeed(float fNewSpeed)
    {
        fManureConsumptionSpeed=fNewSpeed;
        fManureConsumptionSpeed=Math.max(fManureConsumptionSpeed, 0.0f);
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public static float getManureConsumptionSpeed()
    {
        return fManureConsumptionSpeed;
    }


    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public static void setPreySensitivity(float fSensitivity)
    {
        fPreySensitivity=fSensitivity;
        fRelPreySensitivity=fPreySensitivity/fReproductionEnergy;
    }

    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public static float getPreySensitivity()
    {
        return fPreySensitivity;
    }


    /** -
     *  @param          -
     *  @return         -
     *  @exception      -
     */
    public static void setPredatorSensitivity(float fSensitivity)
    {
        fPredatorSensitivity=fSensitivity;
        fRelPredatorSensitivity=fPredatorSensitivity/1.0f;
    }


    public static float getPredatorSensitivity()
    {
        return fPredatorSensitivity;
    }

    /**
     * Sets the sensitivity for manure
     * @param fSensitivity Sensitivity level
     */
    public static void setManureSensitivity(float fSensitivity)
    {
        fManureSensitivity=fSensitivity;
        fRelManureSensitivity=fManureSensitivity/fReproductionEnergy;
    }

    /**
     * Returns the sensitivity for manure
     * @return Sensitivity level for manure
     */
    public static float getManureSensitivity()
    {
        return fManureSensitivity;
    }

    /**
     * This method sets the Energy at which the Cell reproduces.
     * It basically is the max. Energy value of the Cell. It is
     * used to scale the sensitivities.
     * @param liEnergy
     */

    public static void setReproductionEnergy(long liEnergy)
    {
        fReproductionEnergy=(float)liEnergy;
        fRelManureSensitivity=fManureSensitivity/fReproductionEnergy;
        fRelPreySensitivity=fPreySensitivity/fReproductionEnergy;
    }

}

