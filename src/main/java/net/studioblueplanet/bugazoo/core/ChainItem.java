package net.studioblueplanet.bugazoo.core;

import java.io.Serializable;

/**
 * The ChainItem is an list element in a double linked list. It contains
 * functionality to parse the list and update the list.
 * @author        B.J. van der Velde
 * @version       1.0
 */
class ChainItem implements Serializable
{
    private ChainItem nextChainItem;
    private ChainItem previousChainItem;

    /** 
     * Constructor. Creates a new empty Chain
     */
    public ChainItem ()
    {
        nextChainItem=null;
        previousChainItem=null;
    }

    /**
     * This method sets the pointer to the next item in the Chain
     * @param nextChainItem Pointer to the next Chain item
     */
    private void setNextChainItem(ChainItem nextChainItem)
    {
        this.nextChainItem=nextChainItem;
    }

    /**
     * This method sets the pointer to the previous ChainItem
     * @param previousChainItem Previous ChainItem
     */
    private void setPreviousChainItem(ChainItem previousChainItem)
    {
        this.previousChainItem=previousChainItem;
    }

    /**
     * This method returns reference to the previous item in the Chain
     * @return Reference to the previous item. Null if there is no previous.
     */
    public ChainItem getNextChainItem()
    {
        return nextChainItem;
    }

    /**
     * This method returns reference to the next item in the Chain
     * @return Reference to the next item. Null if there is no next.
     */
    public ChainItem getPreviousChainItem()
    {
        return previousChainItem;
    }

    /**
     * This method removes this item from the Chain. Pointers of previous and
     * next items in the chain are updated.
     * @return
     */
    public ChainItem removeItemFromChain()
    {
        ChainItem startOfChain=null;

        if (previousChainItem!=null)
        {
            previousChainItem.setNextChainItem(nextChainItem);
            startOfChain=previousChainItem;
            while (startOfChain.getPreviousChainItem()!=null)
                startOfChain=startOfChain.getPreviousChainItem();
        }
        else
            startOfChain=nextChainItem;

        if (nextChainItem!=null)
        {
            nextChainItem.setPreviousChainItem(previousChainItem);
        }


        return startOfChain;
    }

    /**
     * Appends a new item to the Chain
     * @param newItem The new item to append
     */
    public void appendItemToChain(ChainItem newItem)
    {
        ChainItem pointer;
        pointer=this;

        pointer=this;
        while (pointer.getNextChainItem()!=null)
            pointer=pointer.getNextChainItem();

        pointer.setNextChainItem(newItem);
        newItem.setPreviousChainItem(pointer);
    }

    /**
     * Inserts a new item at the beginning of the Chain
     * @param newItem The new item to insert.
     * @return Reference to the start of the chain.
     */
    public ChainItem insertItemAtBeginOfChain(ChainItem newItem)
    {
        newItem.nextChainItem=this;
        this.previousChainItem=newItem;
        return newItem;
    }
}