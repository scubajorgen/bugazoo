package net.studioblueplanet.bugazoo.ui;

import java.awt.*;

import net.studioblueplanet.bugazoo.core.World;
import net.studioblueplanet.bugazoo.core.WorldRunner;

/**
 * This frame shows the simulation presented on the world panel
 * @author        B.J. van der Velde
 * @version       1.0
 */

public class WorldFrame extends BaseFrame
{
    private final World         theWorld;
    private final WorldRunner   theWorldRunner;
    private final boolean       bPaused;


    /**
     * Constructor
     * @param worldRunner WorldRunner to use
     */
    public WorldFrame(WorldRunner worldRunner)
    {
        super();
        bPaused = false;

        addButton("File");
        addButton("Edit");
        addButton("Pause");
        addButton("Slow");
        addButton("Normal");
        addButton("Fast");
        addButton("Info");
        addButton("Back");

        this.theWorldRunner = worldRunner;
        this.theWorld = theWorldRunner.getWorld();
        showFrame.add(theWorld, BorderLayout.CENTER);
    }

    /**
     * 1st button action: switch to file frame
     */
    @Override
    protected void button1Action()
    {
        addFrame("file", new FileFrame(theWorldRunner));
        selfDestructFrame();
    }

    /**
     * 2nd button action: switch to edit frame
     */
    @Override
    protected void button2Action()
    {
        addFrame("edit", new EditCreatureFrame(theWorld, theWorldRunner));
        selfDestructFrame();
    }

    /**
     * 3rd button action: stop the simulation
     */
    @Override
    protected void button3Action()
    {
        theWorldRunner.stop();
    }

    /**
     * 4th button action: play at slow speed
     */
    @Override
    protected void button4Action()
    {
        theWorldRunner.slowMotion();
        theWorldRunner.start();
    }

    /**
     * 5th button action: play at normal speed
     */
    @Override
    protected void button5Action()
    {
        theWorldRunner.normalRate();
        theWorldRunner.start();
    }

    /**
     * 6th button action: play at fast speed
     */
    @Override
    protected void button6Action()
    {
        theWorldRunner.fastForward();
        theWorldRunner.start();
    }

    /**
     * 7th button action: toggle Creature info
     */
    @Override
    protected void button7Action()
    {
        theWorldRunner.toggleShowInfo();
    }

    /**
     * 8th button action: exit the playground screen
     */
    @Override
    protected void button8Action()
    {
        addFrame("intro", new IntroFrame(theWorldRunner));
        selfDestructFrame();
    }
}
