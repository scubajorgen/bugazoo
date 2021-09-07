package net.studioblueplanet.bugazoo.core;

import net.studioblueplanet.bugazoo.common.Constants;

/**
 * This class performs the iterations on the World. It is responsible for proper
 * scheduling and updating settings.
 *
 * @author B.J. van der Velde
 * @version 1.0
 *
 */

public class WorldRunner implements Runnable
{
    private World theWorld;
    private boolean bPaused;
    private long iTimerPeriod;
    private Thread runnerThread;
    private SimSettingsList worldSettings;
    private SimCreatureList worldCreatures;
    private SimPopulationsList worldPopulations;
    private WorldSettingsChangedListener changeListener;

    /**
     * Constructor, initializes the World runner
     *
     * @param theWorld The World to run
     */
    public WorldRunner(World theWorld)
    {
        super();
        changeListener = null;
        this.theWorld = theWorld;
        worldSettings = new SimSettingsList();
        worldCreatures = new SimCreatureList();
        worldPopulations = new SimPopulationsList(worldCreatures);
        applyNewSettings();
        bPaused = false;
        normalRate();
    }

    /**
     * Starts running the world iteration process
     */
    public void start()
    {

        bPaused = false;
        if (runnerThread == null)
        {
            runnerThread = new Thread(this);
            runnerThread.start();
        }
    }

    /**
     * Stops running the world iteration process
     */
    public void stop()
    {
        runnerThread = null;
    }

    /**
     * Sets the world in the Paused state
     */
    public synchronized void pause()
    {
        bPaused = true;
    }

    /**
     * Resumes running the world, if in the Paused state
     */
    public synchronized void resume()
    {
        bPaused = false;
    }

    /**
     * Sets the world running to slow motion. Decreases the running speed by a
     * factor 3 with respect to the normal rate.
     */
    public synchronized void slowMotion()
    {
        iTimerPeriod = 1000 / Constants.SIMULATION_SPEED * 3;
        iTimerPeriod = Math.max(iTimerPeriod, 10);
    }

    /**
     * Sets the world running to normal speed.
     */
    public synchronized void normalRate()
    {
        iTimerPeriod = 1000 / Constants.SIMULATION_SPEED;
        iTimerPeriod = Math.max(iTimerPeriod, 10);
    }

    /**
     * Sets the world running to fast forward. Increases the running speed by a
     * factor 3 with respect to the normal rate.
     */
    public synchronized void fastForward()
    {
        iTimerPeriod = 1000 / Constants.SIMULATION_SPEED / 3;
        iTimerPeriod = Math.max(iTimerPeriod, 10);
    }

    /**
     * Returns the current World running
     *
     * @return The World
     */
    public World getWorld()
    {
        return theWorld;
    }

    /**
     * Sets a new World to run
     *
     * @param newWorld The new World to simulate
     */
    public synchronized void setWorld(World newWorld)
    {
        theWorld = newWorld;
    }

    /**
     * Resets the selection process of the settings.
     */
    public void resetSettingsSelection()
    {
        this.worldSettings.resetSettings();
    }

    /**
     * Selects the next set of settings
     */
    public void nextWorld()
    {
        this.worldSettings.nextSettings();
    }

    /**
     * Selects the previous set of settings
     */
    public void previousWorld()
    {
        this.worldSettings.previousSettings();
    }

    /**
     * Returns the name of the World of which the settings have currently been
     * selected
     *
     * @return The Name of the current wold
     */
    public String getWorldName()
    {
        return worldSettings.getWorldName();
    }

    /**
     * Returns the name of the World of which the settings have currently been
     * selected
     *
     * @return The description of currently selected World
     */
    public String getWorldDescription()
    {
        return worldSettings.getWorldDescription();
    }

    /**
     * Returns the background texture file name of the World of which the
     * settings have currently been selected
     *
     * @return The background texture file name
     */
    public String getTexture()
    {
        return worldSettings.getTexture();
    }

    /**
     * Apply the selected settings and makes them active. Mind you, with the
     * settings the population and manures are reset!
     */
    public synchronized void applyNewSettings()
    {
        boolean bPausedState;

        bPausedState = bPaused;
        bPaused = true;
        worldSettings.applySettings();
        if (changeListener != null)
        {
            changeListener.worldSettingsChanged();
        }
        theWorld.updateForNewSettings();
        worldPopulations.insertPopulation(theWorld);
        bPaused = bPausedState;
    }

    /**
     * The Thread function. Iterates the world
     */
    public void run()
    {
        long iStartTime;
        long iEndTime;
        long iSleepTime;
        boolean bPaused;
        World theWorld;
        long iTimerPeriod;

        while (Thread.currentThread() == runnerThread)
        {
            iStartTime = System.currentTimeMillis();
            synchronized (this)
            {
                bPaused = this.bPaused;
                theWorld = this.theWorld;
                iTimerPeriod = this.iTimerPeriod;
            }

            if (!bPaused)               // if not paused....
            {
                theWorld.iterate();     // perform an iteration step on the world
            }
            iEndTime = System.currentTimeMillis();

            iSleepTime = Math.max(iTimerPeriod - (iEndTime - iStartTime), 10);
            try
            {
                Thread.sleep(iSleepTime);
            } catch (Exception e)
            {
            }
        }
    }

    /**
     * Returns whether the thread is running
     *
     * @return True when running, false when not
     */
    public boolean getRunningState()
    {
        boolean bRunning;
        if (runnerThread != null)
        {
            bRunning = true;
        } else
        {
            bRunning = false;
        }
        return bRunning;
    }

    /**
     * Returns whether iteration has been paused
     *
     * @return True when paused
     */
    public boolean getPausedState()
    {
        return bPaused;
    }

    /**
     * This method returns the list with available creatures
     *
     * @return The list with available creatures
     */
    public SimCreatureList getAvailableCreatures()
    {
        return this.worldCreatures;
    }

    /**
     * This method defines a new list with available creatures.
     *
     * @param newCreatureList The new list
     */
    public void setAvailableCreatures(SimCreatureList newCreatureList)
    {
        this.worldCreatures = newCreatureList;
    }

    /**
     * This method returns the list with available populations
     *
     * @return The list with available creatures
     */
    public SimPopulationsList getAvailablePopulations()
    {
        return this.worldPopulations;
    }

    /**
     * This method sets defines a new list of populations. The old list is
     * replaces
     *
     * @param newPopulationsList The new list
     */
    public void setAvailablePopulations(SimPopulationsList newPopulationsList)
    {
        this.worldPopulations = newPopulationsList;
    }

    /**
     * This method toggles showing of info.
     */
    public void toggleShowInfo()
    {
        theWorld.toggleShowInfo();
        theWorld.repaint();
    }

    /**
     * This method returns the list with available creatures
     *
     * @return The list with available creatures
     */
    public SimSettingsList getAvailableSettings()
    {
        return this.worldSettings;
    }

    /**
     * This method sets defines a new list of settings. The old list is replaces
     *
     * @param newSettingsList The new list
     */
    public void setAvailableSettings(SimSettingsList newSettingsList)
    {
        this.worldSettings = newSettingsList;
    }

    /**
     * Adds a listener that listens for changes in the settings of the World.
     *
     * @param listener The new listener
     */
    public void setSettingsChangedListener(WorldSettingsChangedListener listener)
    {
        this.changeListener = listener;
    }

    /**
     * This method returns the width of the PlayingField as defined in current
     * settings.
     *
     * @return The PlayingFieldWidth
     */
    public int getPlayingfieldWidth()
    {
        return this.worldSettings.getPlayingFieldWidth();
    }

    /**
     * This method returns the height of the PlayingField as defined in current
     * settings.
     *
     * @return The PlayingField height
     */
    public int getPlayingfieldHeight()
    {
        return this.worldSettings.getPlayingFieldHeight();
    }

}
