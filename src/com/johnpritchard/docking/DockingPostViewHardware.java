/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingPostViewHardware
    extends ObjectLog
    implements Runnable
{


    public DockingPostViewHardware(){
        super();
    }


    public void run(){
        try {
            DockingDatabase.Hardware();

            ViewAnimation.Script(Page.gamehardware);
        }
        catch (Exception exc){
            error("post",exc);
        }
    }

}
