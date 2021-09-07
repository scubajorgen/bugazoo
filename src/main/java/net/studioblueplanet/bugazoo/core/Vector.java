package net.studioblueplanet.bugazoo.core;

import java.io.Serializable;

import java.awt.*;

/**
 *
 * @author B.J. van der Velde
 * @version 1.0
 *
 * Class : Vector Package : Beestjes Description : This class implements a
 * Vector. A Vector is relative and is described by a length and an angle.
 * Exceptions :
 *
 */
public class Vector implements Serializable
{
    private float fAngle;
    private float fLength;
    private float fDX;
    private float fDY;

    /**
     * Constructor
     * @param fAngle Angle value
     * @param fLength Length value
     */
    public Vector(float fAngle, float fLength)
    {
        setVector(fAngle, fLength);
    }

    /**
     * Constructor: creates a vector with angle and length equal to 0
     */
    public Vector()
    {
        this.fAngle = 0.0f;
        this.fLength = 0.0f;
        this.fDX = 0.0f;
        this.fDY = 0.0f;
    }

    /**
     * Constructor: constructs vector from point (fX1, fY1) to (fX2, fY2)
     * @param fX1 
     * @param fY1
     * @param fX2
     * @param FY2
     */
    public Vector(float fX1, float fY1, float fX2, float fY2)
    {
        setVector(fX1, fY1, fX2, fY2);
    }

    /**
     * Constructor: creates a new vector, equal to v
     * @param v Vector to be copied
     */
    public Vector(Vector v)
    {
        setVector(v);
    }

    /**
     * Adds a vector v to the vector
     *
     * @param v Vector to be added
     * @return -
     * @exception -
     */
    public void add(Vector v)
    {
        fDX += v.getDX();
        fDY += v.getDY();
        this.fLength = (float) Math.sqrt(fDX * fDX + fDY * fDY);
        /*
        if (fDX!=0.0f)
        {
            if (fDX<0.0f)
            {
                if (fDY<0.0f)
                {
                    this.fAngle=(float)(Math.atan(fDY/fDX)+Math.PI);
                }
                else
                {
                    this.fAngle=(float)(Math.atan(fDY/fDX)+Math.PI);
                }
            }
            else
            {
                if (fDY<0.0f)
                {
                    this.fAngle=(float)(Math.atan(fDY/fDX)+2.0*Math.PI);
                }
                else
                {
                    this.fAngle=(float)(Math.atan(fDY/fDX));
                }
            }
        }
        else
        {
            this.fAngle=0.0f;
        }
         */
    }

    /**
     * Multiplies the vector (length) with the value defined (fValue)
     * @param fValue Multiplication factor
     */
    public void multiply(float fValue)
    {
        fLength *= fValue;
        fDX *= fValue;
        fDY *= fValue;
    }

    /**
     * Mirrors the vector with respect to the x-axis
     */
    public void xMirror()
    {
        fAngle = (float) Math.PI - fAngle;
        fDX = -fDX;
    }

    /**
     * Mirrors the vector with respect to the x-axis
     */
    public void yMirror()
    {
        fAngle = -fAngle;
        fDY = -fDY;
    }

    /**
     * Inverts the vector
     */
    public void negative()
    {
        fAngle += (float) Math.PI;
        while (fAngle > 2.0f * (float) Math.PI)
        {
            fAngle -= 2.0f * (float) Math.PI;
        }
        fDX = -fDX;
        fDY = -fDY;
    }

    /**
     * Sets the length of the vector
     * @param fLength The new length of the vector
     */
    public void setLength(float fLength)
    {
        float fFactor;
        fFactor = fLength / this.fLength;
        this.fLength = fLength;
        this.fDX *= fFactor;
        this.fDY *= fFactor;
    }

    /**
     * Sets the angle of the vector
     * @param fAngle The new angle of the vector
     */
    public void setAngle(float fAngle)
    {
        this.fAngle = fAngle;
        this.fDX = fLength * (float) Math.cos(fAngle);
        this.fDY = fLength * (float) Math.sin(fAngle);
    }

    /**
     * Returns the length of the vector
     * @return The length of the vector
     */
    public float getLength()
    {
        return fLength;
    }

    /**
     * Returns the angle of the vector with the x-axis
     * @return The angle in radians
     */
    public float getAngle()
    {

        float fReturnAngle;

        if (fDX != 0.0f)
        {
            if (fDX < 0.0f)
            {
                if (fDY < 0.0f)
                {
                    fReturnAngle = (float) (Math.atan(fDY / fDX) + Math.PI);
                } else
                {
                    fReturnAngle = (float) (Math.atan(fDY / fDX) + Math.PI);
                }
            } else
            {
                if (fDY < 0.0f)
                {
                    fReturnAngle = (float) (Math.atan(fDY / fDX) + 2.0 * Math.PI);
                } else
                {
                    fReturnAngle = (float) (Math.atan(fDY / fDX));
                }
            }
        } else
        {
            fReturnAngle = 0.0f;
        }
        return fReturnAngle;
    }

    /**
     * Return the length in x-direction
     * @return The length
     */
    public float getDX()
    {
        return fDX;
    }

    /**
     * Return the length in y-direction
     * @return The length
     */
    public float getDY()
    {
        return fDY;
    }


    /**
     * Copy this vector from the vector passed
     * @param v Vector to copy from
     */
    public void setVector(Vector v)
    {
        fAngle = v.getAngle();
        fLength = v.getLength();
        this.fDX = v.getDX();
        this.fDY = v.getDY();
    }

    /**
     * Defines this vector based on length and angle
     * @param fAngle Angle to use, in radians
     * @param fLength Length to use
     */
    public void setVector(float fAngle, float fLength)
    {
        this.fAngle = fAngle;
        this.fLength = fLength;
        this.fDX = fLength * (float) Math.cos(fAngle);
        this.fDY = fLength * (float) Math.sin(fAngle);
    }


    /**
     * Defines this vector based on two coordinates
     * @param fX1 First coordinate, x
     * @param fY1 First coordinate, y
     * @param fX2 Second coordinate, x
     * @param fY2 Second coordinate, y
     */
    public void setVector(float fX1, float fY1, float fX2, float fY2)
    {
        fDX = fX2 - fX1;
        fDY = fY2 - fY1;
        this.fLength = (float) Math.sqrt(fDX * fDX + fDY * fDY);

        if (fDX != 0.0f)
        {
            if (fDX < 0.0f)
            {
                if (fDY < 0.0f)
                {
                    this.fAngle = (float) (Math.atan(fDY / fDX) + Math.PI);
                } else
                {
                    this.fAngle = (float) (Math.atan(fDY / fDX) + Math.PI);
                }
            } else
            {
                if (fDY < 0.0f)
                {
                    this.fAngle = (float) (Math.atan(fDY / fDX) + 2.0 * Math.PI);
                } else
                {
                    this.fAngle = (float) (Math.atan(fDY / fDX));
                }
            }
        } else
        {
            this.fAngle = 0.0f;
        }

    }

    /**
     * Returns a new vector representing the parallel component of the target
     * vector with respect to the direction vector
     * @param target Target vector. Vector to be decomposed
     * @param direction Direction vector.
     * @return The parallel component vector
     */
    public static Vector getParallelComponent(Vector target, Vector direction)
    {
        float fNewAngle;
        float fNewLength;

        fNewLength = target.getLength()
                * (float) Math.cos(target.getAngle() - direction.getAngle());

        if (fNewLength < 0)
        {
            fNewLength = -fNewLength;
            fNewAngle = -direction.getAngle();
        } else
        {
            fNewAngle = direction.getAngle();
        }

        return new Vector(fNewAngle, fNewLength);
    }

    /**
     * Returns a new vector representing the perpendicular component of the
     * target vector with respect to the direction vector
     *
     * @param target Target vector. Vector to be decomposed
     * @param direction Direction vector.
     * @return The perpendicular component vector
     */
    public static Vector getPerpendicularComponent(Vector target, Vector direction)
    {
        float fNewAngle;
        float fNewLength;

        fNewLength = target.getLength()
                * (float) Math.sin(target.getAngle() - direction.getAngle());

        /*
        if (fNewLength<0)
        {
            fNewLength=-fNewLength;
            fNewAngle=direction.getAngle()-(float)Math.PI/2.0f;
        }
        else
            fNewAngle=direction.getAngle()+(float)Math.PI/2.0f;
         */
        fNewAngle = direction.getAngle() + (float) Math.PI / 2.0f;

        return new Vector(fNewAngle, fNewLength);
    }

    /**
     * Returns the inproduct of the two vectors
     *
     * @param v1 First vector
     * @param v2 Second vector
     * @return The inproduct
     */
    public static float inproduct(Vector v1, Vector v2)
    {
        return (v1.getDX() * v2.getDX() + v1.getDY() * v2.getDY());
    }

    /**
     * Draws the vector using the graphics and given coordinate and scaling
     * @param g Graphics to use
     * @param iX Location to draw the vector, x
     * @param iY Location to draw the vector, y
     * @param fScaling Scaling to apply to the length
     */
    public void draw(Graphics g, int iX, int iY, float fScaling)
    {
        g.drawLine(iX, iY, iX + (int) (fDX * fScaling), iY + (int) (fDY * fScaling));
    }
}
