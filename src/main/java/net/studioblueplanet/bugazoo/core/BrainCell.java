package net.studioblueplanet.bugazoo.core;

import java.awt.*;

/**
 * This Cell represents a Brain Cell. It adds intelligence to the Creature.
 * UNDER CONSTRUCTION
 * @author        B.J. van der Velde
 */
public class BrainCell extends Cell
{
    /**
     * Creates a new brain cell
     *
     * @param iXGridPos column of relative position in grid of cell
     * @param iYGridPos row of relative position in grid of cell
     * @param liEnergy initial energy of cell
     */
    public BrainCell(int iXGridPos, int iYGridPos, long liEnergy)
    {
        super(iXGridPos, iYGridPos, liEnergy);
    }

    @Override
    public void mutate()
    {
    }

    @Override
    public Cell copy()
    {
        BrainCell cell;

        cell = new BrainCell(getXGridPos(), getYGridPos(), getEnergy());
        return cell;
    }

    /**
     * Performs the brain action.
     *
     * @param creature The brain cell's owning creature
     * @return false if not performed, true if action performed
     */
    public boolean performBrainAction(Creature creature)
    {
        return true;
    }

    @Override
    public void paint(Graphics g)
    {
        g.setColor(Color.white);
        g.fillOval((int) fXPos - iCellSize / 2,
                (int) fYPos - iCellSize / 2,
                iCellSize,
                iCellSize);
    }
}

