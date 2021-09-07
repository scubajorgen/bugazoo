package net.studioblueplanet.bugazoo.ui;

import net.studioblueplanet.bugazoo.core.World;
import net.studioblueplanet.bugazoo.core.WorldRunner;
import net.studioblueplanet.bugazoo.core.SimSettingsList;
import net.studioblueplanet.bugazoo.core.SimCreatureList;
import net.studioblueplanet.bugazoo.core.SimPopulationsList;

import java.awt.*;
import java.io.*;
import java.util.Date;


/**
 * This class implements the file panel and functions to save and retrieve
 * Worlds with everything in it
 * @author jorgen
 */
public class FileFrame extends BaseFrame
{
    private final WorldRunner       theWorldRunner;
    private World                   theWorld;
    private SimSettingsList         theSettings;
    private SimCreatureList         theCreatures;
    private SimPopulationsList      thePopulations;
    private final FilePanel         filePanel;

    public FileFrame(WorldRunner worldRunner)
    {
        super();

        this.theWorldRunner = worldRunner;

        addButton("Up");
        addButton("Down");
        addButton("Save");
        addButton("Retrieve");
        addButton("Back");

        filePanel = new FilePanel();
        showFrame.add(filePanel);
        /* to do: file panel */

    }

    public static void setBorderColor(Color newColor)
    {

    }

    @Override
    protected void button1Action()
    {
        filePanel.indexUp();
    }

    @Override
    protected void button2Action()
    {
        filePanel.indexDown();
    }

    @Override
    protected void button3Action()
    {
        saveWorld();

        addFrame("world", new WorldFrame(theWorldRunner));
        selfDestructFrame();
    }

    @Override
    protected void button4Action()
    {
        retrieveWorld();

        addFrame("world", new WorldFrame(theWorldRunner));
        selfDestructFrame();
    }

    @Override
    protected void button5Action()
    {
        addFrame("world", new WorldFrame(theWorldRunner));
        selfDestructFrame();
    }


    private void saveWorld()
    {
        FileOutputStream    fileStream;
        ObjectOutputStream  objectStream;
        String				fileName;

        fileName=filePanel.getFileName();
        theWorld=theWorldRunner.getWorld();                                 // get the current World with everything in it
        theSettings=theWorldRunner.getAvailableSettings();                  // get the collection of world settings
        theCreatures=theWorldRunner.getAvailableCreatures();                // get the collection of available creatures
        thePopulations=theWorldRunner.getAvailablePopulations();            // get the collection of available populations
        theWorldRunner.stop();
        try
        {
            fileStream   = new FileOutputStream(fileName, false);           // open file
            objectStream = new ObjectOutputStream(fileStream);              // create object stream
            objectStream.writeObject(new Date(System.currentTimeMillis()));
            objectStream.writeObject(theSettings);                          // write object to stream (file)
            objectStream.writeObject(theCreatures);                         // write object to stream (file)
            objectStream.writeObject(thePopulations);                       // write object to stream (file)
            objectStream.writeObject(theWorld);                             // write object to stream (file)
            objectStream.flush();
            objectStream.close();                                           // closing...
            fileStream.close();
            System.out.println("Saved");
        }
        catch (IOException e)
        {
              System.err.println("Unable to write file "+fileName);
        }
        theWorldRunner.start();


    }


    private boolean retrieveWorld()
    {
        File                worldFile;
        FileInputStream     fileStream;
        ObjectInputStream   objectStream;
        boolean             bFileLoaded;
        String              fileName;
        Date                date;


        bFileLoaded=false;
        fileName=filePanel.getFileName();
        worldFile=new File(fileName);
        if (worldFile.exists())                                                 // check if file exists
        {
            try
            {
                fileStream      =new FileInputStream(fileName);			// open file
                objectStream    = new ObjectInputStream(fileStream);            // create object stream
                date            =(Date)objectStream.readObject();
                theSettings     =(SimSettingsList)objectStream.readObject();    // read object
                theSettings.applySettings();                                    // set static settings.
                theCreatures    =(SimCreatureList)objectStream.readObject();    // read collection of available creatures
                thePopulations  =(SimPopulationsList)objectStream.readObject(); // read collection of available populations
                theWorld        =(World)objectStream.readObject();              // read object
                theWorld.initializeWorld();
                objectStream.close();                                           // close object stream 
                fileStream.close();                                             // close file
                bFileLoaded     =true;
                theWorldRunner.setWorld(theWorld);                              // set world
                theWorldRunner.setAvailableCreatures(theCreatures);
                theWorldRunner.setAvailablePopulations(thePopulations);
                theWorldRunner.setAvailableSettings(theSettings);

                System.out.println("Retrieved");
            }
            catch (ClassNotFoundException e)
            {
                System.err.println("Unable to read file "+fileName+": class not found");
                bFileLoaded=false;
            }
            catch (InvalidClassException e)
            {
                System.err.println("Unable to read file "+fileName+": invalid class");
            }
            catch (StreamCorruptedException e)
            {
                System.err.println("Unable to read file "+fileName+": stream corrupt");
            }
            catch (OptionalDataException e)
            {
                System.err.println("Unable to read file "+fileName+": optional data");
            }
            catch (IOException e)
            {
                System.err.println("Unable to read file "+fileName+": io error");
            }
        }
        return bFileLoaded;
    }

}

