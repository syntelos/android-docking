/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingPostStartModel
    extends ObjectLog
    implements Runnable
{


    public DockingPostStartModel(){
        super();
    }


    public void run(){
        try {
            DockingDatabase.Model();

            DockingPageGameAbstract.Range();

            ViewAnimation.Script(Page.gamemodel);
        }
        catch (Exception exc){
            error("post",exc);
        }
    }

}
