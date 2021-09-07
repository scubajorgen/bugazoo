package net.studioblueplanet.bugazoo.common;

import java.awt.*;


/**
 * This class represents the globally used constants within the program.
 * @author        B.J. van der Velde
 * @version       1.0
 */
public class Constants extends Object
{
    public static final int         RED_BUTTON                  =1;
    public static final int         GREEN_BUTTON                =2;
    public static final int         YELLOW_BUTTON               =3;
    public static final int         BLUE_BUTTON                 =4;

    /** Number of World time units that advance between two iterations. 
     *  The smaller the number, the slower everything goes by the exacter the simulation */
    public static final float       TIME_UNIT                   =0.1f;

    /** Simulation speed. Iterations per second while in normal speed, assuming
        computational power is sufficient ;-)*/
    public static final int         SIMULATION_SPEED            =9;

    public static final Font        NORMAL_FONT                 =new Font("SansSerif", Font.PLAIN, 10);
    public static final Font        LARGE_FONT                  =new Font("SansSerif", Font.PLAIN, 24);

    public static final int         FUNCTION_OK                 =0;
    public static final int         FUNCTION_ERROR              =1;

    public static final String      SIMSETTINGSXMLFILENAME      ="SimSettings.xml";
    public static final String      SIMCREATURESXMLFILENAME     ="SimCreatures.xml";
    public static final String      SIMPOPULATIONSXMLFILENAME   ="SimPopulations.xml";
    public static final String      SIMSETTINGSSCHEMAFILENAME   ="SimSettingsSchema.xsd";
    public static final String      SIMCREATURESSCHEMAFILENAME  ="SimCreaturesSchema.xsd";
    public static final String      SIMPOPULATIONSSCHEMAFILENAME="SimPopulationsSchema.xsd";

    public static final int         PLAYGROUND_WIDTH            =1024;
    public static final int         PLAYGROUND_HEIGHT           =768;
    /** Creates new Constants */
    public Constants ()
    {
    }

}

