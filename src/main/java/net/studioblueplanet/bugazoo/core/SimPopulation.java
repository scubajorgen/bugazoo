/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.bugazoo.core;

import java.io.Serializable;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * This class represents a population of creatures. The population can be
 * inserted into a Word instance.
 *
 * @author Jorgen
 */
public class SimPopulation extends ArrayList<Creature> implements Serializable
{

    SimCreatureList prefabCreatures;

    /**
     * The constructor. The only way to create a Population is from an XML
     * document element specifying the Population
     *
     * @param element The Element defining the Population of Creatures
     * @param prefabCreatures The available Creatures
     */
    public SimPopulation(Element element, SimCreatureList prefabCreatures)
    {
        NodeList nl;

        this.prefabCreatures = prefabCreatures;

        try
        {
            //get a nodelist of <World> elements
            nl = element.getElementsByTagName("creature");
            if (nl != null && nl.getLength() > 0)
            {
                for (int i = 0; i < nl.getLength(); i++)
                {

                    //get the employee element
                    Element creatureElement = (Element) nl.item(i);

                    //get the Employee object
                    addCreaturesFromElement(creatureElement);

                }
            }

        } 
        catch (java.lang.Exception e)
        {
            System.err.println("Error parsing xml file 'World' element");
        }

        System.out.println("break");

    }

    /**
     * Helper method adding the requested amount of Creatures defined by the
     * type to the Population.
     *
     * @param element The Element defining the Creature to add.
     */
    private void addCreaturesFromElement(Element element)
    {
        Creature    creature;
        Creature    newCreature;
        String      sType;
        int         i;
        boolean     bFound;
        int         iNumber;

        // Get the type
        sType = getTextValue(element, "type");

        // Look for a Creature instance in the creature list with the same type
        i = 0;
        bFound = false;
        creature = null;
        while (i < this.prefabCreatures.size() && !bFound)
        {
            creature = (Creature) prefabCreatures.get(i);
            if (sType.equalsIgnoreCase(creature.getType()))
            {
                bFound = true;
            }
            i++;
        }

        // When the creature has been identified, add the requested number to
        // the population
        if (bFound)
        {
            iNumber = this.getIntValue(element, "number");
            if (iNumber > 0)
            {
                i = 0;
                while (i < iNumber)
                {
                    newCreature = creature.copy();
                    this.add(newCreature);
                    i++;
                }
            }
        } 
        else
        {
            System.err.println("The creature of type " + sType + " requested in the population file was not found.");
        }
    }

    /**
     * I take a xml element and the tag name, look for the tag and get the text
     * content i.e for <employee><name>John</name></employee> xml snippet if the
     * Element points to employee node and tagName is name I will return John
     *
     * @param ele
     * @param tagName
     * @return
     */
    private String getTextValue(Element element, String tagName)
    {
        String textVal = null;
        NodeList nl = element.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0)
        {
            Element el = (Element) nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }

        return textVal;
    }

    /**
     * Calls getTextValue and returns a int value
     *
     * @param element
     * @param tagName
     * @return
     */
    private int getIntValue(Element element, String tagName)
    {
        //in production application you would catch the exception
        return Integer.parseInt(getTextValue(element, tagName));
    }

    /**
     * Calls getTextValue and returns a long value
     *
     * @param element
     * @param tagName
     * @return
     */
    private long getLongValue(Element element, String tagName)
    {
        //in production application you would catch the exception
        return Long.parseLong(getTextValue(element, tagName));
    }

    /**
     * Calls getTextValue and returns a double value
     *
     * @param element
     * @param tagName
     * @return
     */
    private float getFloatValue(Element element, String tagName)
    {
        //in production application you would catch the exception
        return Float.parseFloat(getTextValue(element, tagName));
    }

}
