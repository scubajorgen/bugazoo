package net.studioblueplanet.bugazoo.ui;


import net.studioblueplanet.bugazoo.core.World;
import net.studioblueplanet.bugazoo.core.Creature;
import net.studioblueplanet.bugazoo.core.WorldRunner;
import net.studioblueplanet.bugazoo.common.Constants;

import java.awt.*;

/**
 * The edit creature frame
 * @author        B.J. van der Velde
 * @version       1.0
 */

public class EditCreatureFrame extends BaseFrame
{
    private IntroCanvas 		introCanvas;
    private final World       		theWorld;
    private final WorldRunner 		theWorldRunner;
    private final EditCreaturePanel	editCreaturePanel;

    public EditCreatureFrame(World world, WorldRunner worldRunner)
    {
        super();

        this.theWorld = world;
        this.theWorldRunner = worldRunner;

        addButton("Insert");
        addButton("Prefab Creature");
        addButton("Clear World");
        addButton("Edit World");
        addButton("Back");

        editCreaturePanel = new EditCreaturePanel(worldRunner);
        editCreaturePanel.addMouseListener(editCreaturePanel);

        showFrame.add(editCreaturePanel, BorderLayout.CENTER);
    }

    @Override
    protected void button1Action()
    {
        Creature creature;

        creature = editCreaturePanel.getCreature();
        if (creature.getNumberOfCells() > 0)
        {
            theWorld.insertCreature(creature, true);
        }

        editCreaturePanel.removeMouseListener(editCreaturePanel);
        addFrame("world", new WorldFrame(theWorldRunner));
        selfDestructFrame();
    }

    @Override
    protected void button2Action()
    {
        editCreaturePanel.nextPrefab();
    }

    @Override
    protected void button3Action()
    {
        theWorld.killAllCreatures();
    }

    @Override
    protected void button4Action()
    {
        addFrame("worldsettings", new EditWorldFrame(theWorldRunner));
    }

    @Override
    protected void button5Action()
    {
        addFrame("world", new WorldFrame(theWorldRunner));
        selfDestructFrame();
    }
}

