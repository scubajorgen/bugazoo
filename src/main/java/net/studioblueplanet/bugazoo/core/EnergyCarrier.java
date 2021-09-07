package net.studioblueplanet.bugazoo.core;

/**
 * This class represents an entity that is capable of carrying Energy.
 * @author        B.J. van der Velde
 * @version       1.0
 *
 */

class EnergyCarrier extends ChainItem
{
    /** The amount of Energy the EnergyCarrier carries */
    private long            liEnergy;
    /** Intermediate Energy value used for calculation. If all calculations
     *  have taken place, this amount will be the new amount of Energy for the
     *  EnergyCarrier.
     */
    private long            liUpdatedEnergy;

    /**
     * Constructor, creates and initialises.
     */
    public EnergyCarrier()
    {
        liEnergy=0;
        liUpdatedEnergy=0;
    }

    /**
     * Increases the Energy with given increment
     * @param liIncrement Increment of Energy
     */
    public void increaseEnergy(long liIncrement)
    {
        liEnergy+=liIncrement;
    }

    /**
     * Decreases teh Energy of the Energy Carrier with given decrement. The
     * Energy value cannot be decreased below 0. If there is not sufficient
     * Energy for the decrement, the Energy is set to 0. The actual decrement is
     * returned by the method.
     * @param liDecrement Decrement of Energy
     * @return The actual decrease in Energy
     */
    public long decreaseEnergy(long liDecrement)
    {
        long liActualDecrement;

        if (liEnergy>liDecrement)
        {
            liEnergy-=liDecrement;
            liActualDecrement=liDecrement;
        }
        else
        {
            liActualDecrement=liEnergy;
            liEnergy=0;
        }
        return liActualDecrement;
    }

    /**
     * Returns the Energy the EnergyCarrier is carrying.
     * @return The Energy value
     */
    public long getEnergy()
    {
        return liEnergy;
    }

    /**
     * Sets the Energy value for the EnergyCarrier, as well as the updated
     * Energy value.
     * @param liEnergy The new Energy value
     */
    public void setEnergy(long liEnergy)
    {
        this.liEnergy=liEnergy;
        this.liUpdatedEnergy=liEnergy;
    }

    /**
     * Increases the updated Energy value (intermediate value for calculation)
     * with given increment
     * @param liIncrement Increment of Energy
     */
    public void increaseUpdatedEnergy(long liIncrement)
    {
        liUpdatedEnergy+=liIncrement;
    }

    /**
     * Decreases the updated Energy (intermediate value for calculation)
     * with given decrement. The Energy value cannot be decreased below 0.
     * If there is not sufficient
     * Energy for the decrement, the Energy is set to 0. The actual decrement is
     * returned by the method.
     * @param liDecrement Decrement of Energy
     * @return The actual decrease in Energy
     */
    public long decreaseUpdatedEnergy(long liDecrement)
    {
        long liActualDecrement;

        if (liUpdatedEnergy>liDecrement)
        {
            liUpdatedEnergy-=liDecrement;
            liActualDecrement=liDecrement;
        }
        else
        {
            liActualDecrement=liUpdatedEnergy;
            liUpdatedEnergy=0;
        }
        return liActualDecrement;
    }

    /**
     * Returns the updated Energy (intermediate Energy value for calculation)
     * value
     * @return The value of the updated Energy
     */
    public long getUpdatedEnergy()
    {
        return liUpdatedEnergy;
    }

    /**
     * Sets a new value for the updated Energy (intermediate Energy value used
     * for calculation).
     * @param liUpdatedEnergy New Energy value
     */
    public void setUpdatedEnergy(long liUpdatedEnergy)
    {
        this.liUpdatedEnergy=liUpdatedEnergy;
    }

    /**
     * Updates the Energy after calculation: the intermediate value is set
     * to the actual value of Energy carried by the EnergyCarrier
     */
    public void updateEnergy()
    {
        liEnergy=liUpdatedEnergy;
    }

    /**
     * Initialised the updated Energy value before calculations. It is set
     * to the current value of the Energy the EnergyCarrier is carrying.
     */
    public void initializeUpdatedEnergy()
    {
        liUpdatedEnergy=liEnergy;
    }

}