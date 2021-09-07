package net.studioblueplanet.bugazoo.core;

import net.studioblueplanet.bugazoo.common.Constants;

import java.io.Serializable;
import java.util.ArrayList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;

/**
 * This class contains the adjustable settings for the entire
 * simulations. The settings should be applied before
 * instantiating the World!!
 * @author        B.J. van der Velde
 * @version       1.0
 */
public class SimSettingsList extends ArrayList<SimSettings> implements Serializable
{
    public static final int         NUMBER_OF_WORLDS=8;

    private             int         iSettingsIndex;

    private             Document    dom;

    /**
     *  Constructor. Initialises the list of settings sets and activates the
     *  1st set.
     */
    public SimSettingsList()
    {
        XmlToolkit.validateXmlFile(getClass().getResourceAsStream("/bugazoo/data/"+Constants.SIMSETTINGSXMLFILENAME),
                                   getClass().getResourceAsStream("/bugazoo/data/"+Constants.SIMSETTINGSSCHEMAFILENAME),
                                   Constants.SIMSETTINGSXMLFILENAME);

        // Read the list of world settings from the xml file
        parseSettingsXmlFile();

        // set the pointer to the current set of settings to the 1st in the list
        iSettingsIndex=0;

        // activate the current settings
        this.applySettings();
    }

    /**
     * This method parses the xml file SimSettings.xml defining the settings.
     * Multiple sets of settings can be defined. Each set is added to the list.
     *
     * @return
     */
    private void parseSettingsXmlFile()
    {
        InputStream inStream;
        NodeList nl;
        Element docEle;
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // parse the file
        try
        {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            inStream = getClass().getResourceAsStream("/bugazoo/data/" + Constants.SIMSETTINGSXMLFILENAME);
            dom = db.parse(inStream);
        }  
        catch (ParserConfigurationException | SAXException pce)
        {
            pce.printStackTrace();
        } 
        catch (IOException ioe)
        {
            System.err.println("Cannot find settings file " + "/bugazoo/data/" + Constants.SIMSETTINGSXMLFILENAME);
        }

        //get the root elememt
        docEle = dom.getDocumentElement();

        //get a nodelist of <World> elements
        nl = docEle.getElementsByTagName("World");
        if (nl != null && nl.getLength() > 0)
        {
            for (int i = 0; i < nl.getLength(); i++)
            {

                //get the World element
                Element el = (Element) nl.item(i);

                // Create a new SimSettings instance from it
                SimSettings simSettings = new SimSettings(el);

                //add it to list
                this.add(simSettings);
            }
        }
    }



    /**
     *  This activates the next set of settings in the list, or the
     *  current settings if they are the last in the list
     */
    public void nextSettings()
    {
        if (iSettingsIndex<this.size()-1)
        {
            iSettingsIndex++;
        }
    }

    /**
     *  This activates the previous set of settings in the list, or the
     *  current settings if they are the first in the list
     */
    public void previousSettings()
    {
        if (iSettingsIndex>0)
        {
            iSettingsIndex--;
        }
    }

    /**
     *  This activates the first set of settings in the list
     */
    public void resetSettings()
    {
        iSettingsIndex=0;
    }



    /**
     * This method activates the current Settings. Each setting is made
     * to the appropriate Class
     */

    public final void applySettings()
    {
        SimSettings currentSettings;

        currentSettings=(SimSettings)this.get(iSettingsIndex);

        World.setInitialManureDensity(currentSettings.iInitialManureDensity);
        World.setInitialManureEnergy(currentSettings.liInitialManureEnergy);
        World.setBackgroundTextureFile(currentSettings.sBackgroundTextureFile);
        World.setWorldName(currentSettings.sWorldName);

        Creature.setMinimumCellEnergy(currentSettings.liMinimumCellEnergy);
        Creature.setReproductionCellEnergy(currentSettings.liReproductionCellEnergy);
        Creature.setInitialCellEnergy(currentSettings.liInitialCellEnergy);
        Creature.setMutationChance(currentSettings.fMutationChance);
        Creature.setFriction(currentSettings.fFriction);

        Cell.setEnergyDissipation(currentSettings.fCellEnergyDissipation);
        Cell.setAccelerationEnergyUnit(currentSettings.fCellAccelerationEnergyUnit);
        Cell.setMass(currentSettings.fCellMass);
        Cell.setCellSize(currentSettings.iCellSize);

        FunctionalCell.setSensorRange(currentSettings.fSensorRange);
        FunctionalCell.setConsumerRange(currentSettings.fConsumerRange);
        FunctionalCell.setPreyConsumptionSpeed(currentSettings.fPreyConsumptionSpeed);
        FunctionalCell.setManureConsumptionSpeed(currentSettings.fManureConsumptionSpeed);
        FunctionalCell.setPreySensitivity(currentSettings.fPreySensitivity);
        FunctionalCell.setPredatorSensitivity(currentSettings.fPredatorSensitivity);
        FunctionalCell.setManureSensitivity(currentSettings.fManureSensitivity);
    }

    /**
     * Get the name of the current set of settings defining the world
     * @return The String representing current set of settings
     */
    public String getWorldName()
    {
        SimSettings currentSettings;

        currentSettings=(SimSettings)this.get(iSettingsIndex);
        return currentSettings.sWorldName;
    }

    /**
     * Get the brief description of the current set of settings defining the world
     * @return The String representing current set of settings
     */
    public String getWorldDescription()
    {
        SimSettings currentSettings;

        currentSettings=(SimSettings)this.get(iSettingsIndex);
        return currentSettings.sDescription;
    }

    /**
     * Get the name of the texture file defined by current set
     * @return The String texture file name
     */
    public String getTexture()
    {
        SimSettings currentSettings;

        currentSettings=(SimSettings)this.get(iSettingsIndex);
        return currentSettings.sBackgroundTextureFile;
    }

    public int getPlayingFieldWidth()
    {
        SimSettings currentSettings;

        currentSettings=(SimSettings)this.get(iSettingsIndex);
        return currentSettings.iFrameWidth;
    }

    public int getPlayingFieldHeight()
    {
        SimSettings currentSettings;

        currentSettings=(SimSettings)this.get(iSettingsIndex);
        return currentSettings.iFrameHeight;
    }
}
