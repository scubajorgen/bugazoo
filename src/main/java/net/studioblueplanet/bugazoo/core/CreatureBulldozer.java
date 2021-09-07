package net.studioblueplanet.bugazoo.core;

/**
 *
 * @author B.J. van der Velde
 * @version 1.0
 *
 * Class : BuldozerCreature Package : Beestjes Description : This class
 * implements a predefined creature Exceptions :
 *
 */
public class CreatureBulldozer extends Creature
{
    /**
     * Creates new bulldozer
     *
     * @param fXPos X coordinate of initial position of the creature
     * @param fYPos Y coordinate of initial position of the creature
     * @param fHeading initial heading of the creature
     */
    public CreatureBulldozer(float fXPos, float fYPos, float fHeading)
    {
        super(fXPos, fYPos, fHeading);
        createBulldozer();
    }

    /**
     * Creates new buldozer creature
     *
     * @param fXPos X coordinate of initial position of the creature
     * @param fYPos Y coordinate of initial position of the creature
     * @param fHeading Initial heading of the creature
     * @param iGeneration Generation of this instance
     */
    public CreatureBulldozer(float fXPos, float fYPos, float fHeading, int iGeneration)
    {
        super(fXPos, fYPos, fHeading, iGeneration);
        createBulldozer();
    }

    /**
     * Creates the cells and initialized the creature
     */
    private void createBulldozer()
    {
        sName = "Bulldozer";
        addCell(new FunctionalCell(0, 0, FunctionalCell.TYPE_PREYSENSOR, liInitialEnergy));
        addCell(new FunctionalCell(1, 0, FunctionalCell.TYPE_PREYSENSOR, liInitialEnergy));
        addCell(new FunctionalCell(0, 1, FunctionalCell.TYPE_DRAGGER, liInitialEnergy));
        addCell(new FunctionalCell(2, 1, FunctionalCell.TYPE_DRAGGER, liInitialEnergy));
        addCell(new FunctionalCell(0, 3, FunctionalCell.TYPE_DRAGGER, liInitialEnergy));
        addCell(new FunctionalCell(2, 3, FunctionalCell.TYPE_DRAGGER, liInitialEnergy));

        addCell(new FunctionalCell(0, 4, FunctionalCell.TYPE_MANURESENSOR, liInitialEnergy));
        addCell(new FunctionalCell(1, 4, FunctionalCell.TYPE_MANURESENSOR, liInitialEnergy));
        addCell(new FunctionalCell(1, 1, FunctionalCell.TYPE_PREYCONSUMER, liInitialEnergy));
        addCell(new FunctionalCell(1, 2, FunctionalCell.TYPE_PREYCONSUMER, liInitialEnergy));
        addCell(new FunctionalCell(0, 2, FunctionalCell.TYPE_MANURECONSUMER, liInitialEnergy));
        addCell(new FunctionalCell(1, 3, FunctionalCell.TYPE_MANURECONSUMER, liInitialEnergy));
        createCreature();
    }
}
