package net.studioblueplanet.bugazoo.ui;

import java.awt.*;
import java.awt.event.*;

/**
 * BaseFrame Package : bugazoo.ui Description : The applet/xlet. This
 * class implements the iteration thread. 
 * @author B.J. van der Velde
 * @version 1.0
 */
public class BaseFrame extends Container implements KeyListener, ActionListener
{
    protected int           MAX_BUTTONS = 8;

    protected Container     rootContainer;

    protected Container     showFrame;
    protected Panel         buttonFrame;

    private final Button[]  buttons;
    private int             nButtons;

    /**
     * Constructor: adds command bar and show frame
     */
    public BaseFrame()
    {
        nButtons = 0;
        buttons = new Button[MAX_BUTTONS];

        buttonFrame = new Panel();
        buttonFrame.setVisible(true);
        buttonFrame.setLayout(new GridLayout());

        showFrame = new Container();                 // create container for use
        showFrame.setVisible(true);
        showFrame.setLayout(new BorderLayout());

        this.setLayout(new BorderLayout());

        add(showFrame, BorderLayout.CENTER);       // add the components to frame
        add(buttonFrame, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    /**
     * Adds the new BaseFrame mentioned to the root container (the container in
     * which this BaseFrame is placed) and request attention for the new Base
     * frame
     *
     * @param sName Name of the new BaseFrame
     * @param theFrame The new BaseFrame
     * @param rootContainer Container to add the base frame to
     */
    public static void addFirstFrame(String sName, BaseFrame theFrame, Container rootContainer)
    {
        if (rootContainer != null)
        {
            rootContainer.add(sName, theFrame);
            theFrame.show(sName);
        }
    }

    /**
     * Adds the new BaseFrame mentioned to the root container (the container in
     * which this BaseFrame is placed) and request attention for the new Base
     * frame
     *
     * @param sName Name of the new BaseFrame
     * @param theFrame The new BaseFrame
     */
    protected void addFrame(String sName, BaseFrame theFrame)
    {
        rootContainer = getParent();
        if (rootContainer != null)
        {
            rootContainer.add(sName, theFrame);
            theFrame.show(sName);

        }
    }

    /**
     * Shows the frame
     * @param sName Name to show
     */
    protected void show(String sName)
    {
        rootContainer = getParent();
        if (rootContainer != null)
        {
            ((CardLayout) rootContainer.getLayout()).show(rootContainer, sName);
        }

    }

    /**
     * Removes this BaseFrame from the root container
     */
    protected void selfDestructFrame()
    {
        cleanUpButtons();
        rootContainer = getParent();
        if (rootContainer != null)
        {
            rootContainer.remove(this);
        }
    }

    /**
     * Add button
     * @param text Text on the button 
     */
    protected final void addButton(String text)
    {
        if (nButtons < MAX_BUTTONS)
        {
            buttons[nButtons] = new Button(text);

            buttons[nButtons].addActionListener(this);
//    		buttons[nButtons].setMnemonic(KeyEvent.VK_F1+nButtons);
            buttonFrame.add(buttons[nButtons]);
            nButtons++;
        }
    }

    /**
     * Remove the buttons
     */
    protected void cleanUpButtons()
    {
        int i;

        i = 0;
        while (i < nButtons)
        {
            buttons[i].removeActionListener(this);
            buttonFrame.remove(buttons[i]);
            buttons[i] = null;
            i++;
        }
        nButtons = 0;

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int iKeyPressed;

        iKeyPressed = e.getKeyCode();

        switch (iKeyPressed)
        {
            case KeyEvent.VK_F1:
                button1Action();
                break;
            case KeyEvent.VK_F2:
                button2Action();
                break;
            case KeyEvent.VK_F3:
                button3Action();
                break;
            case KeyEvent.VK_F4:
                button4Action();
                break;
            case KeyEvent.VK_F5:
                button5Action();
                break;
            case KeyEvent.VK_F6:
                button6Action();
                break;
            case KeyEvent.VK_F7:
                button7Action();
                break;
            case KeyEvent.VK_F8:
                button8Action();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Button button;

        button = (Button) e.getSource();

        if (button.equals(buttons[0]))
        {
            button1Action();
        } else if (button.equals(buttons[1]))
        {
            button2Action();
        } else if (button.equals(buttons[2]))
        {
            button3Action();
        } else if (button.equals(buttons[3]))
        {
            button4Action();
        } else if (button.equals(buttons[4]))
        {
            button5Action();
        } else if (button.equals(buttons[5]))
        {
            button6Action();
        } else if (button.equals(buttons[6]))
        {
            button7Action();
        } else if (button.equals(buttons[7]))
        {
            button8Action();
        }

    }

    protected void button1Action()
    {
    }

    protected void button2Action()
    {
    }

    protected void button3Action()
    {
    }

    protected void button4Action()
    {
    }

    protected void button5Action()
    {
    }

    protected void button6Action()
    {
    }

    protected void button7Action()
    {
    }

    protected void button8Action()
    {
    }
}
