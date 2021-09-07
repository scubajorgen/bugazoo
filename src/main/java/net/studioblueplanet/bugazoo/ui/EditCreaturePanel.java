package net.studioblueplanet.bugazoo.ui;

import net.studioblueplanet.bugazoo.common.Constants;
import net.studioblueplanet.bugazoo.core.Cell;
import net.studioblueplanet.bugazoo.core.FunctionalCell;
import net.studioblueplanet.bugazoo.core.Creature;
import net.studioblueplanet.bugazoo.core.CreatureUserDefined;
import net.studioblueplanet.bugazoo.core.WorldRunner;
import net.studioblueplanet.bugazoo.core.SimCreatureList;

import java.awt.*;
import java.awt.event.*;


/**
 * Title:        Bugazoo
 * Description:  Simulation of artifical life
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author       B.J. van der Velde
 * @version      1.0
 */

public class EditCreaturePanel extends Canvas implements MouseListener
{
    private static final Color  TEXT_COLOR = new Color(0, 0, 255);
    private static final Color  BACKGROUND_COLOR = new Color(0, 200, 255);
    private static final int    GRID_SIZE = 10;
    private static final int    CELL_SIZE = 40;
    private static final int    HALF_CELL_SIZE = CELL_SIZE / 2;
    private static final int    SQ_HALF_CELL_SIZE = HALF_CELL_SIZE * HALF_CELL_SIZE;
    private static final int    OFFSET_X = 200;
    private static final int    OFFSET_Y = 150;

    private final WorldRunner   theWorldRunner;
    private final Cell[][]      cellGrid;
    private final int[][]       iXPos;
    private final int[][]       iYPos;

    private CreatureUserDefined userDefinedCreature;
    private Creature            prefabCreature;
    private int                 iPrefabCreatureIndex;

    private boolean             bModified;

    /**
     * Constructor. Initializes variables and arrays.
     * @param worldRunner Worldrunner to use
     */
    public EditCreaturePanel(WorldRunner worldRunner)
    {

        theWorldRunner = worldRunner;

        userDefinedCreature = new CreatureUserDefined(0.0f, 0.0f, 0.0f, "UserDefined");
        prefabCreature = userDefinedCreature;
        iPrefabCreatureIndex = worldRunner.getAvailableCreatures().size();

        bModified = false;

        cellGrid = new Cell[GRID_SIZE][GRID_SIZE];
        iXPos = new int[GRID_SIZE][GRID_SIZE];
        iYPos = new int[GRID_SIZE][GRID_SIZE];

        clearGrid();
    }

    /**
     * This routine clears the grid
     */
    private void clearGrid()
    {
        int iX, iY;

        iY = 0;
        while (iY < GRID_SIZE)
        {
            iX = 0;
            while (iX < GRID_SIZE)
            {
                cellGrid[iX][iY] = new Cell(iX, iY, Creature.getInitialCellEnergy());
                if (iY % 2 == 0)
                {
                    iXPos[iX][iY] = iX * CELL_SIZE + OFFSET_X;
                } else
                {
                    iXPos[iX][iY] = iX * CELL_SIZE - CELL_SIZE / 2 + OFFSET_X;
                }
                iYPos[iX][iY] = (int) ((double) iY * (double) CELL_SIZE * Math.cos(Math.PI / 6.0)) + OFFSET_Y;
                iX++;
            }
            iY++;
        }

    }

    /**
     * This method returns the creature as edited.
     *
     * @return The creature as edited so far. Null if no cells have been added
     * to the creature.
     */
    public CreatureUserDefined getCreature()
    {
        CreatureUserDefined creature;
        Cell cell;
        int iX, iY;
        int iType;

        creature = new CreatureUserDefined(100.0f, 100.0f, 0.0f, "UserDefined");
        iY = 0;
        while (iY < GRID_SIZE)
        {
            iX = 0;
            while (iX < GRID_SIZE)
            {
                cell = cellGrid[iX][iY];
                if (cell instanceof FunctionalCell)
                {
                    creature.addCell(cell);
                }
                iX++;
            }
            iY++;
        }

        if (creature.getNumberOfCells() > 0)
        {
            creature.createCreature();
        }

        return creature;
    }

    /**
     * This method paints the panel and draws the cells
     *
     * @param g Graphics of the panel
     */
    @Override
    public void paint(Graphics g)
    {
        int iX;
        int iY;
        Cell cell;
        FontMetrics fontMetrics;
        String sCreatureType;

        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(TEXT_COLOR);
        g.setFont(Constants.LARGE_FONT);

        fontMetrics = g.getFontMetrics();
        sCreatureType = prefabCreature.getType();
        g.drawString(sCreatureType,
                (getWidth() - fontMetrics.stringWidth(sCreatureType)) / 2,
                50);

        iY = 0;
        while (iY < GRID_SIZE)
        {
            iX = 0;
            while (iX < GRID_SIZE)
            {
                cell = cellGrid[iX][iY];
                cell.paintLarge(g, (float) iXPos[iX][iY], (float) iYPos[iX][iY], CELL_SIZE);

                iX++;
            }
            iY++;
        }

    }

    /**
     * Choose the next of the prefab creatures and display it in the editor. If
     * any modification has been made to the creature present in the editor,
     * this is saved in the user defined creature. The user defined creature is
     * treated as one of the prefabs. In this case the last modified creature is
     * saved.
     */
    public void nextPrefab()
    {
        SimCreatureList creatureList;
        Creature creature;

        creatureList = this.theWorldRunner.getAvailableCreatures();

        if (bModified)
        {
            userDefinedCreature = getCreature();
            prefabCreature = userDefinedCreature;
            bModified = false;
            iPrefabCreatureIndex = creatureList.size();
        }

        if (iPrefabCreatureIndex == creatureList.size())
        {
            iPrefabCreatureIndex = 0;
        } else
        {
            iPrefabCreatureIndex++;
        }

        if (iPrefabCreatureIndex == creatureList.size())
        {
            // if the index points to the 1st creature outside the list
            // the user defined creature is shown
            prefabCreature = userDefinedCreature;
        } else
        {
            // otherwise the prefab creature is retrieved from the list
            prefabCreature = (Creature) creatureList.get(iPrefabCreatureIndex);
        }

        getCellsFromCreature(prefabCreature);
    }

    /**
     * This routines copies the cells from the indicated creature and shows them
     * in the editor.
     */
    private void getCellsFromCreature(Creature creature)
    {
        Cell cell;
        int iX;
        int iY;

        clearGrid();
        cell = creature.getCellChain();
        while (cell != null)
        {
            iX = cell.getXGridPos();
            iY = cell.getYGridPos();
            if ((iX >= 0) && (iY >= 0) && (iX < GRID_SIZE) && (iY < GRID_SIZE))
            {
                cellGrid[iX][iY] = cell.copy();
            }
            cell = (Cell) cell.getNextChainItem();
        }
        repaint();
    }

    /**
     * MouseListener method. Checks if a cell has been clicked. If so, this cell
     * is replaced by a next type.
     *
     * @param e Mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        int iX;
        int iY;
        int iCellX;
        int iCellY;
        Cell currentCell;
        Cell newCell;
        boolean bFound;

        bFound = false;
        newCell = null;
        iY = 0;
        iX = 0;
        iCellX = 0;
        iCellY = 0;
        while ((iY < GRID_SIZE) && !bFound)
        {
            iX = 0;
            while ((iX < GRID_SIZE) && !bFound)
            {
                if (((e.getX() - iXPos[iX][iY]) * (e.getX() - iXPos[iX][iY])
                        + (e.getY() - iYPos[iX][iY]) * (e.getY() - iYPos[iX][iY]))
                        < SQ_HALF_CELL_SIZE)
                {
                    iCellX = iX;
                    iCellY = iY;
                    bFound = true;
                }
                iX++;
            }
            iY++;
        }

        if (bFound)
        {

            bModified = true;

            currentCell = cellGrid[iCellX][iCellY];
            if (currentCell instanceof FunctionalCell)
            {
                switch (((FunctionalCell) currentCell).getInitialType())
                {
                    case FunctionalCell.TYPE_DRAGGER:
                        newCell = new FunctionalCell(iCellX, iCellY,
                                FunctionalCell.TYPE_MANURESENSOR,
                                Creature.getInitialCellEnergy());
                        break;
                    case FunctionalCell.TYPE_MANURESENSOR:
                        newCell = new FunctionalCell(iCellX, iCellY,
                                FunctionalCell.TYPE_MANURECONSUMER,
                                Creature.getInitialCellEnergy());
                        break;
                    case FunctionalCell.TYPE_MANURECONSUMER:
                        newCell = new FunctionalCell(iCellX, iCellY,
                                FunctionalCell.TYPE_PREYSENSOR,
                                Creature.getInitialCellEnergy());
                        break;
                    case FunctionalCell.TYPE_PREYSENSOR:
                        newCell = new FunctionalCell(iCellX, iCellY,
                                FunctionalCell.TYPE_PREYCONSUMER,
                                Creature.getInitialCellEnergy());
                        break;
                    case FunctionalCell.TYPE_PREYCONSUMER:
                        newCell = new FunctionalCell(iCellX, iCellY,
                                FunctionalCell.TYPE_MULTIFUNCTIONAL,
                                Creature.getInitialCellEnergy());
                        break;
                    case FunctionalCell.TYPE_MULTIFUNCTIONAL:
                        newCell = new Cell(iCellX, iCellY,
                                Creature.getInitialCellEnergy());
                        break;
                }
            } else
            {
                newCell = new FunctionalCell(iCellX, iCellY,
                        FunctionalCell.TYPE_DRAGGER,
                        Creature.getInitialCellEnergy());
            }

            if (newCell != null)
            {
                cellGrid[iCellX][iCellY] = newCell;
                repaint();
            }
        }
    }

    /**
     * MouseListener method.
     *
     * @param e Mouse event
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
    }

    /**
     * MouseListener method.
     *
     * @param e Mouse event
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    /**
     * MouseListener method.
     *
     * @param e Mouse event
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    /**
     * MouseListener method.
     *
     * @param e Mouse event
     */
    @Override
    public void mouseExited(MouseEvent e)
    {
    }
}
