package net.studioblueplanet.bugazoo.core;

/**
 *
 * @author B.J. van der Velde
 * @version 1.0
 *
 * Class : CreatureUserDefined Package : bugazoo.core Description : This class
 * implements a user defined creature Exceptions :
 *
 */
public class CreatureUserDefined extends Creature
{

    /**
     * Creates new amoebe creature
     *
     * @param fXPos X coordinate of initial position of the creature
     * @param fYPos Y coordinate of initial position of the creature
     * @param fHeading initial heading of the creature
     * @param sType
     */
    public CreatureUserDefined(float fXPos, float fYPos, float fHeading, String sType)
    {
        super(fXPos, fYPos, fHeading);
        this.sType = sType;
        sName = "User Defined Creature";
    }

    /**
     * Creates new amoebe creature
     *
     * @param fXPos X coordinate of initial position of the creature
     * @param fYPos Y coordinate of initial position of the creature
     * @param fHeading Initial heading of the creature
     * @param iGeneration Generation of this instance
     */
    public CreatureUserDefined(float fXPos, float fYPos, float fHeading, int iGeneration)
    {
        super(fXPos, fYPos, fHeading, iGeneration);
        sName = "User Defined Creature";
    }

}
