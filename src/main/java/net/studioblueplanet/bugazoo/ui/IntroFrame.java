package net.studioblueplanet.bugazoo.ui;

import net.studioblueplanet.bugazoo.core.WorldRunner;

import java.awt.*;

/**
 *
 * @author        B.J. van der Velde
 * @version       1.0
 *
 * Class        : IntroFrame
 * Package      : bugazoo.ui
 * Description  : The intro frame
 * Exceptions   :
 *
 */

public class IntroFrame extends BaseFrame
{
    private final IntroCanvas   introCanvas;
    private final WorldRunner   theWorldRunner;
    private WorldFrame          worldFrame;

    public IntroFrame(WorldRunner worldRunner)
    {
        super();
        this.worldFrame     = null;
        this.theWorldRunner = worldRunner;

        addButton("Help");
        addButton("Start");
        addButton("Exit");

        introCanvas = new IntroCanvas();
        showFrame.setLayout(new BorderLayout());
        showFrame.add(introCanvas, BorderLayout.CENTER);
    }

    public static void setBorderColor(Color newColor)
    {
        IntroCanvas.setBorderColor(newColor);
    }

    protected void button1Action()
    {
        addFrame("help", new HelpFrame(theWorldRunner));
        selfDestructFrame();
    }

    protected void button2Action()
    {
        if (worldFrame == null)
        {
            worldFrame = new WorldFrame(theWorldRunner);
        }
        addFrame("world", worldFrame);
        selfDestructFrame();
    }

    protected void button3Action()
    {
        selfDestructFrame();
        System.exit(0);
    }
}

