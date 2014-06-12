/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingPostNextHistory
    extends ObjectLog
    implements Runnable
{


    public DockingPostNextHistory(){
        super();
    }

    public void run(){
        try {
            if (DockingDatabase.HistoryNext()){

                DockingPageGameAbstract.View();
            }
        }
        catch (Exception exc){
            error("post",exc);
        }
    }

}
