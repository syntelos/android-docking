/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingPostPrevHistory
    extends ObjectLog
    implements Runnable
{


    public DockingPostPrevHistory(){
        super();
    }

    public void run(){
        try {
            if (DockingDatabase.HistoryPrev()){

                DockingPageGameAbstract.View();
            }
        }
        catch (Exception exc){
            error("post",exc);
        }
    }

}
