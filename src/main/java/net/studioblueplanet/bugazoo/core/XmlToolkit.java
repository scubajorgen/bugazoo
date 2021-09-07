package net.studioblueplanet.bugazoo.core;

import org.xml.sax.SAXException;

import java.io.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jorgen
 */
public class XmlToolkit
{
    /**
     * Validates XML against schema/XSD 
     * @param xmlFileInputStream XML input stream
     * @param schemaInputStream
     * @param xmlFileName printable file name
     */
    public static void validateXmlFile(InputStream xmlFileInputStream, InputStream schemaInputStream, String xmlFileName)
    {
        // 1. Lookup a factory for the W3C XML Schema language
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

        // 2. Compile the schema.
        // Here the schema is loaded from a java.io.File, but you could use
        // a java.net.URL or a javax.xml.transform.Source instead.
        try
        {
            Source schemaSource = new StreamSource(schemaInputStream);
            Schema schema = factory.newSchema(schemaSource);

            // 3. Get a validator from the schema.
            Validator validator = schema.newValidator();

            // 4. Parse the document you want to check.
            Source source = new StreamSource(xmlFileInputStream);

            // 5. Check the document
            validator.validate(source);
            System.out.println(xmlFileName + " is valid.");
        } 
        catch (SAXException ex)
        {
            System.err.println("Error validating " + xmlFileName);
            System.err.println(ex.getMessage());
            java.lang.System.exit(0);
        } 
        catch (java.io.IOException ex)
        {
            System.err.println("Error validating file " + xmlFileName);
            java.lang.System.exit(0);
        }
    }

    /**
     * I take a xml element and the tag name, look for the tag and get the text
     * content i.e for \<employee\>\<name\>John\</name\>\</employee\> xml snippet if the
     * Element points to employee node and tagName is name I will return John
     *
     * @param element The element
     * @param tagName The tag to find
     * @return The tag value as String
     */
    public static String getTextValue(Element element, String tagName)
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
     * @param element The element
     * @param tagName The tag to find
     * @return The tag value as booleanm
     */
    public static boolean getBooleanValue(Element element, String tagName)
    {
        //in production application you would catch the exception
        return Boolean.parseBoolean(getTextValue(element, tagName));
    }

    /**
     * Calls getTextValue and returns a int value
     * @param element The element
     * @param tagName The tag to find
     * @return The tag value as int
     */
    public static int getIntValue(Element element, String tagName)
    {
        //in production application you would catch the exception
        return Integer.parseInt(getTextValue(element, tagName));
    }

    /**
     * Calls getTextValue and returns a long value
     * @param element The element
     * @param tagName The tag to find
     * @return The tag value as long
     */
    public static long getLongValue(Element element, String tagName)
    {
        //in production application you would catch the exception
        return Long.parseLong(getTextValue(element, tagName));
    }

    /**
     * Calls getTextValue and returns a double value
     * @param element The element
     * @param tagName The tag to find
     * @return The tag value as float
     */
    public static float getFloatValue(Element element, String tagName)
    {
        //in production application you would catch the exception
        return Float.parseFloat(getTextValue(element, tagName));
    }

}
