package net.studioblueplanet.bugazoo.ui;

import net.studioblueplanet.bugazoo.common.Constants;


import java.awt.*;
import java.awt.event.*;
import net.studioblueplanet.bugazoo.core.World;
import net.studioblueplanet.bugazoo.core.WorldRunner;
import net.studioblueplanet.bugazoo.core.SimSettingsList;
import net.studioblueplanet.bugazoo.core.WorldSettingsChangedListener;


/**
 * The Bugazoo application. It can be started as Applet or as Java application.
 * The class is responsible for creating the main frame (Applet or java.awt.Frame).
 * It creates the intro frame.
 * @author        B.J. van der Velde
 * @version       1.0
 */
public class Bugazoo extends java.applet.Applet implements WorldSettingsChangedListener
{

    private World               theWorld;
    private WorldRunner         theWorldRunner;
    private SimSettingsList     theSettings;
    private int                 iPanelWidth;
    private int                 iPanelHeight;

    private IntroFrame          introFrame;

    private Container           rootContainer;

    private static Bugazoo      theApplication;
    private static Frame        theFrame;
    private static boolean      isApplet=true;

    /**
     * Main function. Creates a Bugazoo instance and a frame for showing the
     * Applet.
     *
     * @param args
     */
    public static void main(String[] args)
    {
        int iFrameInteriorWidth = Constants.PLAYGROUND_WIDTH;
        int iFrameInteriorHeight = Constants.PLAYGROUND_HEIGHT;

        Insets insets;

        theApplication = new Bugazoo();
        theFrame = new Frame("Bugazoo");
        isApplet = false;

        if (args.length == 2)
        {
            System.out.println("Trying to start Bugazoo with user defined width " + args[0] + " and height " + args[1]);
            iFrameInteriorWidth = Integer.parseInt(args[0]);
            iFrameInteriorHeight = Integer.parseInt(args[1]);
            if ((iFrameInteriorWidth < 100) || (iFrameInteriorWidth > 4092))
            {
                iFrameInteriorWidth = 800;
                System.out.println("Error: width should be 100<=width<=4092");
            }
            if ((iFrameInteriorHeight < 100) || (iFrameInteriorHeight > 4092))
            {
                iFrameInteriorHeight = 600;
                System.out.println("Error: height should be 100<=width<=4092");
            }
        }

        theFrame.pack();

        theFrame.setResizable(false);
        insets = theFrame.getInsets();

        // set the frame size, account for the window insets
        theFrame.setSize(iFrameInteriorWidth + insets.left + insets.right,
                iFrameInteriorHeight + insets.top + insets.bottom);
        theFrame.add(theApplication, BorderLayout.CENTER);

        theFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }

            @Override
            public void windowDeiconified(WindowEvent e)
            {

            }

            @Override
            public void windowIconified(WindowEvent e)
            {

            }
        });

        theApplication.init();
        theApplication.start();

        theFrame.setVisible(true);

        theFrame.validate();
        theFrame.repaint();

    }

    /**
     * Initializes the Applet. Basically the root container is created and the
     * intro frame is created and shown
     */
    @Override
    public void init()
    {
        Dimension dim;

        dim = getSize();                                  // initialize size of environment
        iPanelWidth = dim.width;
        iPanelHeight = dim.height;

        initializeWorld();

        rootContainer = new Container();
        rootContainer.setLayout(new CardLayout());
        rootContainer.setVisible(true);

        setLayout(new BorderLayout());
        add(rootContainer, BorderLayout.CENTER);
        setVisible(true);

        introFrame = new IntroFrame(theWorldRunner);
        rootContainer.add("intro", introFrame);
//        BaseFrame.addFirstFrame("intro", introFrame, rootContainer);
    }

    /**
     * Creates the World. It also creates the WorldRunner that controls and
     * animates the World.
     */
    private void initializeWorld()
    {
        // Create 'Da Wold'
        theWorld = new World(iPanelWidth, iPanelHeight - 25);  // create the world panel
        theWorld.setVisible(true);

        // Make the World turn around!
        theWorldRunner = new WorldRunner(theWorld);       // the world runner

        // If this is not an Applet, we can resize. Hence add a settings
        // changed listener, apply new settings and make the Frame resize
        if (!isApplet)
        {
            // This adds the listerener for changing the world settings
            theWorldRunner.setSettingsChangedListener(this);

            // Apply new settings, this calls the change listener and
            // resizes the frame
            theWorldRunner.applyNewSettings();
        }
    }

    /**
     * Applet method: Starts the Applet
     */
    @Override
    public void start()
    {
    }

    /**
     * Applet method: Stops the Applet
     */
    @Override
    public void stop()
    {
    }

    /**
     * Destroys the Applet
     */
    @Override
    public void destroy()
    {
        theWorldRunner.stop();                          // stop simulating the world
    }

    @Override
    public void worldSettingsChanged()
    {
        int width;
        int height;

        width = theWorldRunner.getPlayingfieldWidth();
        height = theWorldRunner.getPlayingfieldHeight();
        theWorld.setNewPlayingFieldDimensions(width, height - 25);
        Insets insets = theFrame.getInsets();

        // set the frame size, account for the window insets
        theFrame.setSize(width + insets.left + insets.right,
                height + insets.top + insets.bottom);
    }
}

