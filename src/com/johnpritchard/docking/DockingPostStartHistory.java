/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingPostStartHistory
    extends ObjectLog
    implements Runnable
{


    public DockingPostStartHistory(){
        super();
    }

    public void run(){
        try {
            if (DockingDatabase.History()){

                DockingPageGameAbstract.View();

                ViewAnimation.Script(Page.gameview);
            }
            else {

                ViewAnimation.Script(Page.nohistory);
            }
        }
        catch (Exception exc){
            error("post",exc);
        }
    }

}
