/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingPostStartGame
    extends ObjectLog
    implements Runnable
{


    public DockingPostStartGame(){
        super();
    }


    public void run(){
        try {
            DockingDatabase.Game();

            DockingPageGameAbstract.Play();

            ViewAnimation.Script(Page.gameplay);
        }
        catch (Exception exc){
            error("post",exc);
        }
    }

}
