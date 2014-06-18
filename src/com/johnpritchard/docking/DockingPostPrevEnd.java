/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * Called from {@link DockingPostGameView} input navigation.
 * 
 * @see DockingPostFinishPhysics
 * @see DockingPostNextEnd
 */
public class DockingPostPrevEnd
    extends ObjectLog
    implements Runnable
{


    public DockingPostPrevEnd(){
        super();
    }

    public void run(){
        try {
            final DockingCraftStateVector sv = DockingCraftStateVector.Instance;

            if (DockingDatabase.HistoryPrev()){

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
