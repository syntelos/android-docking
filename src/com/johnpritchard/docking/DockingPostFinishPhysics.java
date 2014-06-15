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
            if (DockingCraftStateVector.Instance.inGame()){

                DockingDatabase.GameOver();

                DockingPageGameAbstract.View();

                if (DockingCraftStateVector.Instance.dock() ||
                    DockingCraftStateVector.Instance.stall())
                    {
                        ViewAnimation.Script(Page.gameview);
                    }
                else {
                    ViewAnimation.Script(Page.gamecrash);
                }
            }
        }
        catch (Exception exc){
            error("post",exc);
        }
    }
}
