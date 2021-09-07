package net.studioblueplanet.bugazoo.ui;

import net.studioblueplanet.bugazoo.core.WorldRunner;

import java.awt.*;
import java.net.*;
import java.io.*;
import javax.swing.*;

/**
 * The help frame
 *
 * @author B.J. van der Velde
 * @version 1.0
 */
public class HelpFrame extends BaseFrame
{
    private static final String HTMLHELPFILENAME = "/bugazoo/html/help.html";
    private final WorldRunner   theWorldRunner;

    private final JEditorPane   textPane;
    private final JScrollPane   textScrollPane;

    public HelpFrame(WorldRunner worldRunner)
    {
        super();

        this.theWorldRunner = worldRunner;

        addButton("Back");

        textPane = createEditorPane();
        textScrollPane = new JScrollPane(textPane);
        textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        textScrollPane.setPreferredSize(new Dimension(250, 145));
        textScrollPane.setMinimumSize(new Dimension(10, 10));

        showFrame.add(textScrollPane);

    }

    private JEditorPane createEditorPane()
    {
        JEditorPane editorPane;
        String fileSeparator;
        URL helpURL;

        helpURL = null;

        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        try
        {
            helpURL = HelpFrame.class.getResource(HTMLHELPFILENAME);
        } catch (Exception e)
        {
            System.err.println("Couldn't create help URL: " + HTMLHELPFILENAME);
        }

        try
        {
            editorPane.setPage(helpURL);
        } catch (IOException e)
        {
            System.err.println("Attempted to read a bad URL: " + helpURL);
        }

        return editorPane;
    }

    protected void button1Action()
    {
        addFrame("intro", new IntroFrame(theWorldRunner));
        selfDestructFrame();
    }
}
