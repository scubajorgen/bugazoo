package net.studioblueplanet.bugazoo.core;

/**
 * The 'Blind Lobster' Creature heads for manure and eats it. It is also capable
 * of eating other Creatures. However, it cannot see other Creatures.
 *
 * @author B.J. van der Velde
 * @version 1.0
 */
public class CreatureBlindLobster extends Creature
{

    /**
     * Creates new Blind Lobster
     *
     * @param fXPos X coordinate of initial position of the creature
     * @param fYPos Y coordinate of initial position of the creature
     * @param fHeading initial heading of the creature
     */
    public CreatureBlindLobster(float fXPos, float fYPos, float fHeading)
    {
        super(fXPos, fYPos, fHeading);
        createBlindLobster();
    }

    /**
     * Creates new Blind Lobster
     *
     * @param fXPos X coordinate of initial position of the creature
     * @param fYPos Y coordinate of initial position of the creature
     * @param fHeading Initial heading of the creature
     * @param iGeneration Generation of this instance
     */
    public CreatureBlindLobster(float fXPos, float fYPos, float fHeading, int iGeneration)
    {
        super(fXPos, fYPos, fHeading, iGeneration);
        createBlindLobster();
    }

    /**
     * Creates the cells and initialized the creature
     */
    private void createBlindLobster()
    {
        sName = "Blind Lobster";
        addCell(new FunctionalCell(1, 0, FunctionalCell.TYPE_PREYCONSUMER, liInitialEnergy));
        addCell(new FunctionalCell(1, 4, FunctionalCell.TYPE_PREYCONSUMER, liInitialEnergy));
        addCell(new FunctionalCell(1, 1, FunctionalCell.TYPE_DRAGGER, liInitialEnergy));
        addCell(new FunctionalCell(1, 3, FunctionalCell.TYPE_DRAGGER, liInitialEnergy));
        addCell(new FunctionalCell(1, 2, FunctionalCell.TYPE_MANURESENSOR, liInitialEnergy));
        addCell(new FunctionalCell(2, 2, FunctionalCell.TYPE_MANURECONSUMER, liInitialEnergy));
        createCreature();
    }
}
