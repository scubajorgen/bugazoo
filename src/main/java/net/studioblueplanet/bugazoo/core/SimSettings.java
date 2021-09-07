/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.studioblueplanet.bugazoo.core;

import java.io.Serializable;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jorgen
 */
public class SimSettings implements Serializable
{
    public String  sWorldName;
    public String  sDescription;
    public float   fCellEnergyDissipation;
    public float   fCellAccelerationEnergyUnit;
    public long    liMinimumCellEnergy;
    public long    liReproductionCellEnergy;
    public long    liInitialCellEnergy;
    public float   fMutationChance;
    public float   fCellMass;
    public float   fFriction;
    public int     iInitialManureDensity;
    public long    liInitialManureEnergy;
    public float   fSensorRange;
    public float   fConsumerRange;
    public int     iCellSize;
    public float   fPreyConsumptionSpeed;
    public float   fManureConsumptionSpeed;
    public String  sBackgroundTextureFile;
    public float   fPreySensitivity;
    public float   fPredatorSensitivity;
    public float   fManureSensitivity;
    public boolean bAllowCellMutation;
    public boolean bAllowCellCreation;
    public boolean bAllowCellDeletion;

    public int     iFrameWidth;
    public int     iFrameHeight;

    /**
     * Constructor. Initialises the class from the XML element
     *
     * @param element
     */
    public SimSettings(Element element)
    {
        try
        {
            this.sWorldName                 =XmlToolkit.getTextValue(element, "WorldName");
            this.sDescription               =XmlToolkit.getTextValue(element, "Description");
            this.fCellEnergyDissipation     =XmlToolkit.getFloatValue(element, "CellEnergyDissipation");
            this.fCellAccelerationEnergyUnit=XmlToolkit.getFloatValue(element, "CellAccelerationEnergy");
            this.liMinimumCellEnergy        =XmlToolkit.getLongValue(element, "MinimumCellEnergy");
            this.liReproductionCellEnergy   =XmlToolkit.getLongValue(element, "ReproductionCellEnergy");
            this.liInitialCellEnergy        =XmlToolkit.getLongValue(element, "InitialCellEnergy");
            this.fMutationChance            =XmlToolkit.getFloatValue(element, "MutationChance");
            this.fCellMass                  =XmlToolkit.getFloatValue(element, "CellMass");
            this.fFriction                  =XmlToolkit.getFloatValue(element, "Friction");
            this.iInitialManureDensity      =XmlToolkit.getIntValue(element, "InitialManureDensity");
            this.liInitialManureEnergy      =XmlToolkit.getLongValue(element, "InitialManureEnergy");
            this.fSensorRange               =XmlToolkit.getFloatValue(element, "SensorRange");
            this.fConsumerRange             =XmlToolkit.getFloatValue(element, "ConsumerRange");
            this.iCellSize                  =XmlToolkit.getIntValue(element, "CellSize");
            this.fPreyConsumptionSpeed      =XmlToolkit.getFloatValue(element, "PreyConsumptionSpeed");
            this.fManureConsumptionSpeed    =XmlToolkit.getFloatValue(element, "ManureConsumptionSpeed");
            this.sBackgroundTextureFile     =XmlToolkit.getTextValue(element, "BackgroundTextureFile");
            this.fPreySensitivity           =XmlToolkit.getFloatValue(element, "PreySensitivity");
            this.fPredatorSensitivity       =XmlToolkit.getFloatValue(element, "PredatorSensitivity");
            this.fManureSensitivity         =XmlToolkit.getFloatValue(element, "ManureSensitivity");
            this.iFrameWidth                =XmlToolkit.getIntValue(element, "FrameWidth");
            this.iFrameHeight               =XmlToolkit.getIntValue(element, "FrameHeight");
            this.bAllowCellCreation         =XmlToolkit.getBooleanValue(element, "AllowCellCreation");
            this.bAllowCellDeletion         =XmlToolkit.getBooleanValue(element, "AllowCellDeletion");
            this.bAllowCellMutation         =XmlToolkit.getBooleanValue(element, "AllowCellMutation");
        }
        catch (java.lang.Exception e)
        {
            System.err.println("Error parsing xml file 'World' element");
        }
    }
}
