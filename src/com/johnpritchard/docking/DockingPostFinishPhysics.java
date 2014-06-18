/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * 
 * @see DockingPostNextEnd
 * @see DockingPostPrevEnd
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
            final DockingCraftStateVector sv = DockingCraftStateVector.Instance;

            if (sv.inGame()){

                DockingDatabase.GameOver();

                DockingPageGameAbstract.View();

                if (sv.dock()){

                    ViewAnimation.Script(Page.gamedock);
                }
                else if (sv.stall()){

                    ViewAnimation.Script(Page.gamestall);
                }
                else if (sv.free()){

                    ViewAnimation.Script(Page.gamelost);
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
