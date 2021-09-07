package net.studioblueplanet.bugazoo.core;

/**
 * The CreatureAmoebe is a simple Creature made up of one single hybrid
 * Functional Cell
 *
 * @author B.J. van der Velde
 * @version 1.0
 */
public class CreatureAmoebe extends Creature
{
    /**
     * Creates new amoebe creature
     *
     * @param fXPos X coordinate of initial position of the creature
     * @param fYPos Y coordinate of initial position of the creature
     * @param fHeading initial heading of the creature
     */
    public CreatureAmoebe(float fXPos, float fYPos, float fHeading)
    {
        super(fXPos, fYPos, fHeading);
        createAmoebe();
    }

    /**
     * Creates new amoebe creature
     *
     * @param fXPos X coordinate of initial position of the creature
     * @param fYPos Y coordinate of initial position of the creature
     * @param fHeading Initial heading of the creature
     * @param iGeneration Generation of this instance
     */
    public CreatureAmoebe(float fXPos, float fYPos, float fHeading, int iGeneration)
    {
        super(fXPos, fYPos, fHeading, iGeneration);
        createAmoebe();
    }

    /**
     * Creates the cells and initialized the creature
     *
     * @param -
     * @return -
     * @exception -
     */
    private void createAmoebe()
    {
        sName = "Amoebe";
        sType = "Amoebe";
        addCell(new FunctionalCell(0, 0, FunctionalCell.TYPE_MULTIFUNCTIONAL, liInitialEnergy));
        createCreature();
    }
}
