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

package net.studioblueplanet.bugazoo.core;

import net.studioblueplanet.bugazoo.common.Constants;

import java.io.Serializable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class represents a list of populations that can be inserted into the
 * World. The populations are read from the SimPopulations.xml file.
 * A population can consist of creatures defined by the SimCreatureList.
 * @author B.J. van der Velde
 * @version 1.0
 */

public class SimPopulationsList extends ArrayList implements Serializable
{
    private Document        dom;
    private int             iPopulationIndex;
    private World           theWorld;
    private SimCreatureList prefabCreatures;

    public SimPopulationsList(SimCreatureList prefabCreatures)
    {
        this.prefabCreatures = prefabCreatures;

        // Read the list of world settings from the xml file
        XmlToolkit.validateXmlFile(getClass().getResourceAsStream("/bugazoo/data/" + Constants.SIMPOPULATIONSXMLFILENAME),
                getClass().getResourceAsStream("/bugazoo/data/" + Constants.SIMPOPULATIONSSCHEMAFILENAME),
                Constants.SIMPOPULATIONSXMLFILENAME);
        parseSettingsXmlFile();

        iPopulationIndex = 0;
    }

    /**
     * This method inserts the current active population into the world. Any
     * existing population is killed.
     *
     * @param theWorld Reference to the world.
     */
    public void insertPopulation(World theWorld)
    {
        int i;
        SimPopulation population;
        Creature creature;
        float fXPos;
        float fYPos;
        float fHeading;

        population = (SimPopulation) this.get(this.iPopulationIndex);

        // Kill existing population in the world. RIP
        theWorld.killAllCreatures();

        // Parse the population
        i = 0;
        while (i < population.size())
        {
            creature = (Creature) population.get(i);

            creature = creature.copy();               // create a copy in order not to destroy the original

            theWorld.insertCreature(creature, true);
            i++;
        }
    }

    /**
     * This method selects the next population in the array or current
     * population if current population is the last in the array
     */
    public void nextPopulation()
    {
        if (this.iPopulationIndex < this.size() - 1)
        {
            this.iPopulationIndex++;
        }
    }

    /**
     * This method selects the previous population in the array or current
     * population if current population is the first in the array
     */
    public void previousPopulation()
    {
        if (this.iPopulationIndex > 0)
        {
            this.iPopulationIndex--;
        }
    }

    /**
     * This method selects the 1st population of creatures in the list.
     */
    public void resetPopulation()
    {
        this.iPopulationIndex = 0;
    }

    /**
     * This method parses the xml file SimPopulations.xml defining the settings.
     * Multiple sets of settings can be defined. Each set is added to the list.
     *
     * @return
     */
    private void parseSettingsXmlFile()
    {
        InputStream inStream;
        NodeList nl;
        Element docElement;
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // parse the file
        try
        {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            inStream = getClass().getResourceAsStream("/bugazoo/data/" + Constants.SIMPOPULATIONSXMLFILENAME);
            dom = db.parse(inStream);
        } 
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        } 
        catch (SAXException se)
        {
            se.printStackTrace();
        } 
        catch (IOException ioe)
        {
            System.err.println("Cannot find settings file " + Constants.SIMPOPULATIONSXMLFILENAME);
        }

        //get the root elememt
        docElement = dom.getDocumentElement();

        //get a nodelist of <World> elements
        nl = docElement.getElementsByTagName("population");
        if (nl != null && nl.getLength() > 0)
        {
            for (int i = 0; i < nl.getLength(); i++)
            {

                //get the employee element
                Element element = (Element) nl.item(i);

                //get the Employee object
                SimPopulation population = new SimPopulation(element, this.prefabCreatures);

                //add it to list
                this.add(population);
            }
        }
    }


}