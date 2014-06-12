/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 */
public class DockingPostFinishPhysics
    extends ObjectLog
    implements Runnable
{


    public DockingPostFinishPhysics(){
        super();
    }

    public void run(){
        try {
            DockingDatabase.GameOver();

            DockingPageGameAbstract.View();

            if (DockingCraftStateVector.Instance.crash()){

                ViewAnimation.Script(Page.gamecrash);
            }
            else {

                ViewAnimation.Script(Page.gameview);
            }
        }
        catch (Exception exc){
            error("post",exc);
        }
    }
}
