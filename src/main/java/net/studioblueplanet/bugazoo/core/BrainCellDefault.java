package net.studioblueplanet.bugazoo.core;

import java.awt.Graphics;
import java.awt.Color;


/**
 * This Brain Cell represents some basic intelligence
 * UNDER CONSTRUCTION
 * @author B.J. van der Velde
 * @version 1.0
 */
public class BrainCellDefault extends BrainCell
{

    /**
     * Creates a new default brain cell
     *
     * @param iXGridPos column of relative position in grid of cell
     * @param iYGridPos row of relative position in grid of cell
     * @param liEnergy initial energy of cell
     */
    public BrainCellDefault(int iXGridPos, int iYGridPos, long liEnergy)
    {
        super(iXGridPos, iYGridPos, liEnergy);
    }

    @Override
    public Cell copy()
    {
        BrainCellDefault cell;

        cell = new BrainCellDefault(getXGridPos(), getYGridPos(), getEnergy());
        return cell;
    }

    /**
     * Performs the brain action.
     * @param creature The brain cell's owning creature
     * @return false if not performed, true if action performed
     */
    @Override
    public boolean performBrainAction(Creature creature)
    {
        Cell currCell;
        Cell otherCell;
        Cell chain;
        Vector manureSense;
        Vector predatorSense;
        Vector preySense;
        Vector force;

        chain = (Cell) creature.getCellChain();
        currCell = chain;
        force = new Vector(0.0f, 0.0f);
        while (currCell != null)
        {
            manureSense = currCell.getManureSense();
            force.add(manureSense);
            currCell = (Cell) currCell.getNextChainItem();
        }

        currCell = chain;
        while (currCell != null)
        {
            currCell.setDragForce(force);
            currCell = (Cell) currCell.getNextChainItem();
        }
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
