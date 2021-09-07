/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
 * This class represents the list with predefined Creatures. The creatures
 * are read from an XML file SimCreatures.xml
 * @author Jorgen
 */
public class SimCreatureList extends ArrayList<Creature> implements Serializable
{
    private Document        dom;

    /**
     * The constructor. Reads the available predefined creatures from
     * xml file.
     *
     */

    public SimCreatureList()
    {
        XmlToolkit.validateXmlFile(getClass().getResourceAsStream("/bugazoo/data/"+Constants.SIMCREATURESXMLFILENAME),
                                   getClass().getResourceAsStream("/bugazoo/data/"+Constants.SIMCREATURESSCHEMAFILENAME),
                                   Constants.SIMCREATURESXMLFILENAME);
        this.parseCreaturesXmlFile();
    }

    /**
     *  This method parses the xml file SimCreatures.xml defining the
     *  creatures.
     *  @return
     */
    private void parseCreaturesXmlFile()
    {
        InputStream inStream;
        NodeList nl;
        Element docElement;
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // parse the file and generate the document
        try
        {
            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            inStream = getClass().getResourceAsStream("/bugazoo/data/" + Constants.SIMCREATURESXMLFILENAME);
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
            System.err.println("Cannot find settings file " + Constants.SIMCREATURESXMLFILENAME);
        }

        //get the root elememt
        docElement = dom.getDocumentElement();

        //get a nodelist of <World> elements
        nl = docElement.getElementsByTagName("creature");
        if (nl != null && nl.getLength() > 0)
        {
            for (int i = 0; i < nl.getLength(); i++)
            {

                //get the employee element
                Element creatureElement = (Element) nl.item(i);

                //get the creature object
                Creature creature = createCreatureFromElement(creatureElement);

                //add it to list
                if (creature != null)
                {
                    this.add(creature);
                }
            }
        }
    }

    /**
     * Given an document element defining the creature, this method
     * creates a Creature instance.
     * @param element The document element defining the creature
     * @return The instantiated creature
     */
    private Creature createCreatureFromElement(Element element)
    {
        CreatureUserDefined creature;
        NodeList            theCellsNode;
        NodeList            cellNodes;
        int                 iCellCount;
        String              sCreatureType;
        int                 i;


        iCellCount      =0;
        sCreatureType   ="";
        creature        =null;

        try
        {
            // get the creature type
            sCreatureType=this.getTextValue(element, "type");

            //get the <cells> element
            theCellsNode = element.getElementsByTagName("cells");
            if(theCellsNode != null && theCellsNode.getLength() == 1)
            {
                //get the Cell element
                Element cellsElement = (Element)theCellsNode.item(0);

                cellNodes = cellsElement.getElementsByTagName("cell");

                if(cellNodes != null && cellNodes.getLength() > 0)
                {
                    creature=new CreatureUserDefined(0, 0, 0, sCreatureType);
                    i=0;
                    while (i<cellNodes.getLength())
                    {
                        Element cellElement=(Element)cellNodes.item(i);

                        //get the Employee object
                        Cell cell = createCellFromElement(cellElement);
                        if (cell!=null)
                        {
                            creature.addCell(cell);
                            iCellCount++;
                        }
                        i++;
                    }
                }
            }
            else
            {
                System.err.println("Error parsing SimCreatures.xml: no or more than one <cells> section");
            }
        }
        catch (java.lang.Exception e)
        {
            creature=null;
            System.err.println("Error parsing xml file 'World' element");
        }

        // If there are no cells in the creature, it is not sensible to return it.
        if (iCellCount==0)
        {
            System.err.println("No cells defined for Creature with type "+sCreatureType);
        }

        return creature;
    }


    private Cell createCellFromElement(Element element)
    {
        Cell        cell;
        String      sType;
        int         iX;
        int         iY;

        sType=getTextValue(element, "type");
        iX=this.getIntValue(element, "xpos");
        iY=this.getIntValue(element, "ypos");

        if (sType.equalsIgnoreCase("Dragger"))
        {
            cell=new FunctionalCell(iX, iY, FunctionalCell.TYPE_DRAGGER, Creature.getInitialCellEnergy());
        }
        else if (sType.equalsIgnoreCase("PreySensor"))
        {
            cell=new FunctionalCell(iX, iY, FunctionalCell.TYPE_PREYSENSOR, Creature.getInitialCellEnergy());
        }
        else if (sType.equalsIgnoreCase("ManureSensor"))
        {
            cell=new FunctionalCell(iX, iY, FunctionalCell.TYPE_MANURESENSOR, Creature.getInitialCellEnergy());
        }
        else if (sType.equalsIgnoreCase("PreyConsumer"))
        {
            cell=new FunctionalCell(iX, iY, FunctionalCell.TYPE_PREYCONSUMER, Creature.getInitialCellEnergy());
        }
        else if (sType.equalsIgnoreCase("ManureConsumer"))
        {
            cell=new FunctionalCell(iX, iY, FunctionalCell.TYPE_MANURECONSUMER, Creature.getInitialCellEnergy());
        }
        else if (sType.equalsIgnoreCase("Hybrid"))
        {
            cell=new FunctionalCell(iX, iY, FunctionalCell.TYPE_MULTIFUNCTIONAL, Creature.getInitialCellEnergy());
        }
        else
        {
            cell=null;
        }

        return cell;
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
