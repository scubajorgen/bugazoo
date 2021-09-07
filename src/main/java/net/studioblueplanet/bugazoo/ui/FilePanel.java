package net.studioblueplanet.bugazoo.ui;

import net.studioblueplanet.bugazoo.common.Constants;

import java.awt.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;



/**
 *
 * @author        B.J. van der Velde
 * @version       1.0
 *
 * Class        : FilePanel
 * Package      : nl.ict.alife.ui
 * Description  : The file panel showing the 10 locations which can be occupied
 *                by files
 * Exceptions   :
 *
 */


public class FilePanel extends Container
{
    private static final Color      BACKGROUND_COLOR  =new Color(0.2f, 0.5f, 1.0f, 0.5f);
    private static final Color      TEXT_COLOR        =new Color(0.2f, 1.0f, 0.8f, 1.0f);
    private static final Color      LIST_COLOR        =new Color(1.0f, 1.0f, 1.0f, 1.0f);
    private static final int        LISTSIZE          =10;
    private static final int        LISTITEMWIDTH     =300;
    private static final int        LISTITEMHEIGHT    =40;

    private static List             list;
    private static Label            title;

    private int                     iListIndex;

    private String[]                theElements;

    public FilePanel()
    {
        super();
        initComponents();
    }

    public void initComponents()
    {
        int i;

        theElements = new String[LISTSIZE];

        /*
        i=0;                                                                    // create empty elements
        while (i<LISTSIZE)
        {
            theElements[i]=new String("Empty");
            i++;
        }
         */
        list = new List();      									// put them in a list

        updateFileList();                                                       // give elements real content

        list.setFont(Constants.LARGE_FONT);                                  	// some list properties...
        list.setForeground(LIST_COLOR);
        list.setBackground(BACKGROUND_COLOR);
//        list.setNumVisibleElements(LISTSIZE);

        iListIndex = 0;                                                           // the cursor position
        list.select(iListIndex);
//        theElements[iListIndex].setBackground(listColor);
//        theElements[iListIndex].setForeground(backgroundColor);
//        theList.setVisible(true);

        Label title = new Label("Choose file location and save, delete or retrieve");
//        title.setFont(Constants.NORMALFONT);
//        title.setBackground(backgroundColor);
//        title.setForeground(textColor);

        setLayout(new BorderLayout());
        add(title, BorderLayout.SOUTH);
        add(list, BorderLayout.CENTER);

        setVisible(true);
    }

    public void indexUp()
    {
        iListIndex = list.getSelectedIndex();
        if (iListIndex < LISTSIZE - 1)
        {
            iListIndex++;
            list.select(iListIndex);
        }
    }

    public void indexDown()
    {
        iListIndex = list.getSelectedIndex();
        if (iListIndex > 0)
        {
            iListIndex--;
            list.select(iListIndex);
        }
    }

    public void updateFileList()
    {
        int i;

        i = 0;
        while (i < LISTSIZE)
        {
            if (worldFileExists(i))                                                  // check if file exists
            {
                getFileLabel(i);
            } else
            {
                getDefaultFileLabel(i);
            }

            i++;
        }
        i = 0;
        while (i < LISTSIZE)
        {
            list.add(theElements[i], i);
            i++;
        }
    }

    private boolean worldFileExists(int iIndex)
    {
        boolean bExists;
        File worldFile;

        worldFile = new File(getFileName(iIndex));
        if (worldFile.exists())
        {
            bExists = true;
        } else
        {
            bExists = false;
        }
        return bExists;
    }

    private void getFileLabel(int iIndex)
    {
        FileInputStream fileStream;
        ObjectInputStream objectStream;
        boolean bFileLoaded;
        Date fileDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy, HH:mm:ss");
        String sFileName;
        String sFileDate;

        sFileName = getFileName(iIndex);
        try
        {
            fileStream = new FileInputStream(sFileName);                          // open file
            objectStream = new ObjectInputStream(fileStream);                   // create object stream                            // make object stream
            fileDate = (Date) objectStream.readObject();                           // read object
            objectStream.close();                                               // close object stream                            // close stream and file
            fileStream.close();                                                 // close file

            sFileDate = dateFormat.format(fileDate);                              // create date string
            sFileDate = "World " + (iIndex + 1) + ": " + sFileDate;
            theElements[iIndex] = new String(sFileDate);   						// create new list element
        } catch (ClassNotFoundException e)
        {
            System.err.println("Unable to read file " + sFileName + ": class not found");
            bFileLoaded = false;
        } catch (InvalidClassException e)
        {
            System.err.println("Unable to read file " + sFileName + ": invalid class");
        } catch (StreamCorruptedException e)
        {
            System.err.println("Unable to read file " + sFileName + ": stream corrupt");
        } catch (OptionalDataException e)
        {
            System.err.println("Unable to read file " + sFileName + ": optional data");
        } catch (IOException e)
        {
            System.err.println("Unable to read file " + sFileName + ": io error");
        }

    }

    private void getDefaultFileLabel(int iIndex)
    {

        theElements[iIndex] = new String("World " + (iIndex + 1) + ": vacant ");

    }

    public String getFileName()
    {
        int iIndex;

        iIndex = list.getSelectedIndex();

        return ("world" + (iIndex) + ".dat");
    }

    protected String getFileName(int iIndex)
    {
        return ("world" + (iIndex) + ".dat");
    }
}
