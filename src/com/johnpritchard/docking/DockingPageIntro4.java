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
    extends DockingPageIntro
{

    public final static DockingPageIntro4 Instance = new DockingPageIntro4();



    private DockingPageIntro4(){
        super(new ViewPage2DComponent[]{
                text4
            });
    }


    @Override
    public String name(){
        return Page.intro4.name();
    }
    @Override
    public Page value(){
        return Page.intro4;
    }
    @Override
    public void input(InputScript in){
        final Input type = in.type();

        switch(type){
        case Left:
            view.script(Page.intro3);
            break;
        case Right:
            view.script(Page.intro5);
            break;
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
