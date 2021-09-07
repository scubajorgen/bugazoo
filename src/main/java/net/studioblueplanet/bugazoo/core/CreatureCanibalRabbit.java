package net.studioblueplanet.bugazoo.core;

/**
 *
 * @author B.J. van der Velde
 * @version 1.0
 *
 * Class : CreatureCanibalRabbit Package : Beestjes Description : This class
 * implements a predefined creature Exceptions :
 *
 */
public class CreatureCanibalRabbit extends Creature
{
    /**
     * Creates new CanibalRabbit
     *
     * @param fXPos X coordinate of initial position of the creature
     * @param fYPos Y coordinate of initial position of the creature
     * @param fHeading initial heading of the creature
     */
    public CreatureCanibalRabbit(float fXPos, float fYPos, float fHeading)
    {
        super(fXPos, fYPos, fHeading);
        createCanibalRabbit();
    }

    /**
     * Creates new CanibalRabbit creature
     *
     * @param fXPos X coordinate of initial position of the creature
     * @param fYPos Y coordinate of initial position of the creature
     * @param fHeading Initial heading of the creature
     * @param iGeneration Generation of this instance
     */
    public CreatureCanibalRabbit(float fXPos, float fYPos, float fHeading, int iGeneration)
    {
        super(fXPos, fYPos, fHeading, iGeneration);
        createCanibalRabbit();
    }

    /**
     * Creates the cells and initialized the creature
     */
    private void createCanibalRabbit()
    {
        sName = "Canibal Rabbit";
        addCell(new FunctionalCell(1, 1, FunctionalCell.TYPE_PREYSENSOR, liInitialEnergy));
        addCell(new FunctionalCell(0, 2, FunctionalCell.TYPE_DRAGGER, liInitialEnergy));
        addCell(new FunctionalCell(1, 2, FunctionalCell.TYPE_DRAGGER, liInitialEnergy));
        addCell(new FunctionalCell(0, 0, FunctionalCell.TYPE_PREYCONSUMER, liInitialEnergy));
        addCell(new FunctionalCell(1, 0, FunctionalCell.TYPE_PREYCONSUMER, liInitialEnergy));

        createCreature();
    }
}
