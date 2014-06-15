/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageIntro3
    extends DockingPageIntro
{

    public final static DockingPageIntro3 Instance = new DockingPageIntro3();



    private DockingPageIntro3(){
        super(new ViewPage2DComponent[]{
                text3, text3pg
            });
    }


    @Override
    public String name(){
        return Page.intro3.name();
    }
    @Override
    public Page value(){
        return Page.intro3;
    }
    @Override
    public void input(InputScript in){
        final Input type = in.type();

        switch(type){
        case Left:
            view.script(Page.intro2);
            break;
        case Right:
            view.script(Page.intro4);
            break;
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
