/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.bugazoo.core;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jorgen
 */
public class EnergyCarrierTest
{
    
    public EnergyCarrierTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of increaseEnergy method, of class EnergyCarrier.
     */
    @Test
    public void testIncreaseEnergy()
    {
        System.out.println("increaseEnergy");
        long liIncrement = 100L;
        EnergyCarrier instance = new EnergyCarrier();

        assertEquals(0, instance.getEnergy());
        instance.increaseEnergy(liIncrement);
        assertEquals(liIncrement, instance.getEnergy());
    }

    /**
     * Test of decreaseEnergy method, of class EnergyCarrier.
     */
    @Test
    public void testDecreaseEnergy()
    {
        System.out.println("decreaseEnergy");

        EnergyCarrier instance = new EnergyCarrier();
        instance.increaseEnergy(100L);
        assertEquals(100, instance.getEnergy());

        long result=instance.decreaseEnergy(40L);
        assertEquals(40L, result);
        assertEquals(60L, instance.getEnergy());
        
        result=instance.decreaseEnergy(70L);
        assertEquals(60L, result);
        assertEquals(0L, instance.getEnergy());
    }

    /**
     * Test of getEnergy en setEnergy method, of class EnergyCarrier.
     */
    @Test
    public void testGetSetEnergy()
    {
        System.out.println("getSetEnergy");
        EnergyCarrier instance = new EnergyCarrier();
        assertEquals(0, instance.getEnergy());
        
        long expResult = 1234L;
        instance.setEnergy(expResult);
        long result = instance.getEnergy();
        assertEquals(expResult, result);
        result = instance.getUpdatedEnergy();
        assertEquals(expResult, result);
    }


    /**
     * Test of increaseUpdatedEnergy method, of class EnergyCarrier.
     */
    @Test
    public void testIncreaseUpdatedEnergy()
    {
        System.out.println("increaseUpdatedEnergy");
        long liIncrement = 4234L;
        EnergyCarrier instance = new EnergyCarrier();
        instance.increaseUpdatedEnergy(liIncrement);
        assertEquals(liIncrement, instance.getUpdatedEnergy());
    }

    /**
     * Test of decreaseUpdatedEnergy method, of class EnergyCarrier.
     */
    @Test
    public void testDecreaseUpdatedEnergy()
    {
        System.out.println("decreaseUpdatedEnergy");
        long liDecrement = 0L;
        EnergyCarrier instance = new EnergyCarrier();
        assertEquals(0, instance.getUpdatedEnergy());
        instance.increaseUpdatedEnergy(101L);
        assertEquals(101L, instance.getUpdatedEnergy());
        long result = instance.decreaseUpdatedEnergy(20);
        assertEquals(20, result);
        assertEquals(81, instance.getUpdatedEnergy());
        
        result = instance.decreaseUpdatedEnergy(100);
        assertEquals(81, result);
        assertEquals(0, instance.getUpdatedEnergy());

    }

    /**
     * Test of getUpdatedEnergy and setUpdatedEnergy method, of class EnergyCarrier.
     */
    @Test
    public void testGetSetUpdatedEnergy()
    {
        System.out.println("getSetUpdatedEnergy");
        EnergyCarrier instance = new EnergyCarrier();
        assertEquals(0, instance.getUpdatedEnergy());
        instance.setUpdatedEnergy(1234L);
        assertEquals(1234L, instance.getUpdatedEnergy());
    }

    /**
     * Test of updateEnergy method, of class EnergyCarrier.
     */
    @Test
    public void testUpdateEnergy()
    {
        System.out.println("updateEnergy");
        EnergyCarrier instance = new EnergyCarrier();
        
        instance.setEnergy(123L);
        instance.setUpdatedEnergy(12);
        instance.updateEnergy();
        assertEquals(12L, instance.getEnergy());
        assertEquals(12L, instance.getUpdatedEnergy());
    }

    /**
     * Test of initializeUpdatedEnergy method, of class EnergyCarrier.
     */
    @Test
    public void testInitializeUpdatedEnergy()
    {
        System.out.println("initializeUpdatedEnergy");
        EnergyCarrier instance = new EnergyCarrier();
        assertEquals(0L, instance.getUpdatedEnergy());
        
        instance.setEnergy(100);
        instance.decreaseEnergy(5);
        assertEquals(95L, instance.getEnergy());
        assertEquals(100L, instance.getUpdatedEnergy());
        
        instance.initializeUpdatedEnergy();
        assertEquals(95L, instance.getUpdatedEnergy());
    }
    
}
