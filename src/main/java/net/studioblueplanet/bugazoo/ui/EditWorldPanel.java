package net.studioblueplanet.bugazoo.ui;

import net.studioblueplanet.bugazoo.common.Constants;

import java.awt.*;
import java.net.*;


/**
 * The panel used to select a World
 * @author B.J. van der Velde
 * @version 1.0
 */

public class EditWorldPanel extends Canvas
{
    private static final Color  TEXT_COLOR=new Color(0, 0, 255);
    private static final Color  BACKGROUND_COLOR=new Color(0, 0, 50);

    private String              sWorldSettingsName;
    private String              sDescription;
    private String              sTextureFileName;
    private Image               backgroundTexture;
    private int                 iTextureWidth;
    private int                 iTextureHeight;
    private FontMetrics         fontMetrics;

    public EditWorldPanel(String sWorldSettingsName, String sDescription, String sTextureFileName)
    {
        this.sWorldSettingsName = sWorldSettingsName;
        this.sDescription = sDescription;
        this.sTextureFileName = sTextureFileName;
        initialize();
    }

    public void setContents(String sWorldSettingsName, String sDescription, String sTextureFileName)
    {
        this.sWorldSettingsName = sWorldSettingsName;
        this.sDescription = sDescription;
        this.sTextureFileName = sTextureFileName;
        initialize();
    }

    /**
     * Initializes the panel
     */
    public final void initialize()
    {
        MediaTracker tracker;

        try
        {
            URL url = EditWorldPanel.class.getResource("/" + sTextureFileName);
            backgroundTexture = Toolkit.getDefaultToolkit().getImage(url);

            //        backgroundTexture=Toolkit.getDefaultToolkit().getImage(sTextureFileName);
            tracker = new MediaTracker(this);						// wait till image loaded
            tracker.addImage(backgroundTexture, 0);
            tracker.waitForID(0);
            iTextureWidth = backgroundTexture.getWidth(this);
            iTextureHeight = backgroundTexture.getHeight(this);
        } 
        catch (InterruptedException e)
        {
            iTextureWidth = 0;
            iTextureHeight = 0;
            System.err.println("Error loading background texture " + sTextureFileName);
        }

        repaint();
    }

    /**
     * Repaint panel
     * @param g Graphics to use for repaint
     */
    @Override
    public void paint(Graphics g)
    {
        int i, j;
        int iHeight;
        int iWidth;

        iHeight = getHeight();
        iWidth = getWidth();

        if ((iTextureWidth > 0) && (iTextureHeight > 0))                // fill backgound with background texture (if loaded)
        {
            i = 0;
            while (i < iHeight)
            {
                j = 0;
                while (j < iWidth)
                {
                    g.drawImage(backgroundTexture, j, i, this);
                    j += iTextureWidth;
                }
                i += iTextureHeight;
            }
        } else                                                        // else, erase ima
        {
            g.setColor(BACKGROUND_COLOR);
            g.clearRect(0, 0, iWidth, iHeight);             // erase image
        }
        g.setColor(TEXT_COLOR);
        g.setFont(Constants.LARGE_FONT);
        fontMetrics = g.getFontMetrics();
        g.drawString(sWorldSettingsName,
                (iWidth - fontMetrics.stringWidth(sWorldSettingsName)) / 2,
                iHeight / 2);
        g.setFont(Constants.NORMAL_FONT);
        fontMetrics = g.getFontMetrics();
        g.drawString(sDescription,
                (iWidth - fontMetrics.stringWidth(sDescription)) / 2,
                iHeight / 2 + 50);

    }
}