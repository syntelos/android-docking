/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageIntro4
    extends ViewPage2D
{

    public final static DockingPageIntro4 Instance = new DockingPageIntro4();





    private DockingPageIntro4(){
        super(new ViewPage2DComponent[]{
                new ViewPage2DTextMultiline("Rendezvous in zero gravity using T10 and T01 thrusters.  Velocity in the X+ direction is slowed by thrust designated X-.  Success at distance zero requires velocity of one centimeter per second (0.010).")
            });
    }


    @Override
    public String name(){
        return Page.intro.name();
    }
    @Override
    public Page value(){
        return Page.intro;
    }
    @Override
    public void input(InputScript in){

        if (-1 < enter()){

            view.script(Page.start);
        }
    }
}