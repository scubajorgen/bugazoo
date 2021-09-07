/*----------------------------------------------------------------------------*\
 *																			  *
 *	File		:                                                             *
 *																			  *
 *	Author		: B.J. van der Velde										  *
 *																			  *
 *	Date		:															  *
 *																			  *
 *	Description :                               							  *
 *																			  *
 *																			  *
\*----------------------------------------------------------------------------*/

package net.studioblueplanet.bugazoo.ui;


import net.studioblueplanet.bugazoo.core.World;
import net.studioblueplanet.bugazoo.core.WorldRunner;
import net.studioblueplanet.bugazoo.common.Constants;

import java.awt.*;


/**
 *
 * @author        B.J. van der Velde
 * @version       1.0
 *
 * Class        : EditWorldFrame
 * Package      : bugazoo.ui
 * Description  : The edit world frame
 * Exceptions   :
 *
 */

public class EditWorldFrame extends BaseFrame
{
    private World                   theWorld;
    private final WorldRunner       theWorldRunner;

    private final int               iWorldIndex;

    private final EditWorldPanel    editWorldPanel;

    public EditWorldFrame(WorldRunner worldRunner)
    {
        super();

        this.theWorldRunner = worldRunner;

        theWorldRunner.resetSettingsSelection();

        addButton("Previous");
        addButton("Next");
        addButton("Cancel");
        addButton("Back");

        iWorldIndex = 0;

        editWorldPanel = new EditWorldPanel(theWorldRunner.getWorldName(),
                theWorldRunner.getWorldDescription(),
                theWorldRunner.getTexture());
        showFrame.add(editWorldPanel);
    }

    @Override
    protected void button1Action()
    {
        theWorldRunner.previousWorld();
        editWorldPanel.setContents(theWorldRunner.getWorldName(),
                theWorldRunner.getWorldDescription(),
                theWorldRunner.getTexture());
    }

    @Override
    protected void button2Action()
    {
        theWorldRunner.nextWorld();
        editWorldPanel.setContents(theWorldRunner.getWorldName(),
                theWorldRunner.getWorldDescription(),
                theWorldRunner.getTexture());
    }

    @Override
    protected void button3Action()
    {
        selfDestructFrame();
    }

    @Override
    protected void button4Action()
    {
        theWorldRunner.applyNewSettings();
        selfDestructFrame();
    }
}

