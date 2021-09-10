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
 * @author jorgen.van.der.velde
 */
public class ChainItemTest
{
    ChainItem instance;
    ChainItem chainedItem;
    
    public ChainItemTest()
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
        instance=new ChainItem();
        chainedItem=new ChainItem();
        instance.appendItemToChain(chainedItem);
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getNextChainItem method, of class ChainItem.
     */
    @Test
    public void testGetNextChainItem()
    {
        System.out.println("getNextChainItem");
        ChainItem expResult = chainedItem;
        ChainItem result = instance.getNextChainItem();
        assertEquals(expResult, result);
        result = chainedItem.getNextChainItem();
        assertEquals(null, result);
    }

    /**
     * Test of getPreviousChainItem method, of class ChainItem.
     */
    @Test
    public void testGetPreviousChainItem()
    {
        System.out.println("getPreviousChainItem");
        ChainItem result = instance.getPreviousChainItem();
        assertEquals(null, result);
        result = chainedItem.getPreviousChainItem();
        assertEquals(instance, result);
    }

    /**
     * Test of removeItemFromChain method, of class ChainItem.
     */
    @Test
    public void testRemoveItemFromChain()
    {
        System.out.println("removeItemFromChain");

        ChainItem result = chainedItem.removeItemFromChain();
        assertEquals(instance, result);
        result = instance.removeItemFromChain();
        assertEquals(null, result);
    }

    /**
     * Test of appendItemToChain method, of class ChainItem.
     */
    @Test
    public void testAppendItemToChain()
    {
        System.out.println("appendItemToChain");
        ChainItem newItem = new ChainItem();

        instance.appendItemToChain(newItem);
        
        assertEquals(newItem, chainedItem.getNextChainItem());
    }

    /**
     * Test of insertItemAtBeginOfChain method, of class ChainItem.
     */
    @Test
    public void testInsertItemAtBeginOfChain()
    {
        System.out.println("insertItemAtBeginOfChain");
        ChainItem newItem = new ChainItem();
        ChainItem expResult = null;
        ChainItem result = instance.insertItemAtBeginOfChain(newItem);
        assertEquals(newItem, instance.getPreviousChainItem());
    }
    
}
