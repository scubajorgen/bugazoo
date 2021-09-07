package net.studioblueplanet.bugazoo.ui;

import java.awt.*;
import java.net.*;

/**
 * The canvas containing the intro image
 * @author        B.J. van der Velde
 * @version       1.0
 */


public class IntroCanvas extends Canvas
{
    private static final Color  BORDER_COLOR  =new Color(0.2f, 0.5f, 1.0f);
    private static final String INTROIMAGEFILE="bugazoo/image/bugazoo.jpg";

    private final Image         introImage;
    private Canvas              imageCanvas;

    private static Color        borderColor=BORDER_COLOR;

    /**
     * Constructor
     */
    public IntroCanvas()
    {
        URL url = IntroCanvas.class.getResource("/" + INTROIMAGEFILE);
        introImage = Toolkit.getDefaultToolkit().getImage(url);
        repaint();
    }

    /**
     * Sets the border color
     * @param newColor New border color
     */
    public static void setBorderColor(Color newColor)
    {
        borderColor = newColor;
    }

    /**
     * Repaint the canvas
     * @param g Graphics to use for repainting 
     */
    @Override
    public void paint(Graphics g)
    {
        Dimension canvasDim;
        int iImageWidth;
        int iImageHeight;
        int iXBorder;
        int iYBorder;

        canvasDim = getSize();
        /*
        iImageWidth=introImage.getWidth(this);
        iImageHeight=introImage.getHeight(this);
        iXBorder=(canvasDim.width-iImageWidth)/2;
        iYBorder=(canvasDim.height-iImageHeight)/2;
        g.drawImage(introImage,
                    iXBorder,
                    iYBorder,
                    this);
         */
        iXBorder = 5;
        iYBorder = 5;
        iImageWidth = canvasDim.width - 10;
        iImageHeight = canvasDim.height - 10;
        g.drawImage(introImage,
                iXBorder,
                iYBorder,
                iImageWidth,
                iImageHeight,
                this);
        g.setColor(borderColor);
        g.fillRect(0, 0,
                iXBorder, iImageHeight + 2 * iYBorder);
        g.fillRect(iImageWidth + iXBorder, 0,
                iXBorder, iImageHeight + 2 * iYBorder);
        g.fillRect(iXBorder, 0,
                iImageWidth, iYBorder);
        g.fillRect(iXBorder, iImageHeight + iYBorder,
                iImageWidth, iYBorder);
    }
}
