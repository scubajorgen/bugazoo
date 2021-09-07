package net.studioblueplanet.bugazoo.core;

/**
 * This class implements a predefined creature
 *
 * @author B.J. van der Velde
 * @version 1.0
 */
public class CreatureManureDuck extends Creature
{
    /**
     * Creates new manure duck
     *
     * @param fXPos X coordinate of initial position of the creature
     * @param fYPos Y coordinate of initial position of the creature
     * @param fHeading initial heading of the creature
     */
    public CreatureManureDuck(float fXPos, float fYPos, float fHeading)
    {
        super(fXPos, fYPos, fHeading);
        createManureDuck();
    }

    /**
     * Creates new manure duck
     *
     * @param fXPos X coordinate of initial position of the creature
     * @param fYPos Y coordinate of initial position of the creature
     * @param fHeading Initial heading of the creature
     * @param iGeneration Generation of this instance
     */
    public CreatureManureDuck(float fXPos, float fYPos, float fHeading, int iGeneration)
    {
        super(fXPos, fYPos, fHeading, iGeneration);
        createManureDuck();
    }

    /**
     * Creates the cells and initialized the creature
     */
    private void createManureDuck()
    {
        sName = "Manure Duck";
        addCell(new FunctionalCell(1, 1, FunctionalCell.TYPE_DRAGGER, liInitialEnergy));
        addCell(new FunctionalCell(1, 3, FunctionalCell.TYPE_DRAGGER, liInitialEnergy));
        addCell(new FunctionalCell(1, 2, FunctionalCell.TYPE_MANURESENSOR, liInitialEnergy));
        addCell(new FunctionalCell(2, 2, FunctionalCell.TYPE_MANURECONSUMER, liInitialEnergy));
        createCreature();
    }
}
