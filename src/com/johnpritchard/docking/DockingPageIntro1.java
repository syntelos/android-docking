/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageIntro1
    extends DockingPageIntro
{

    public final static DockingPageIntro1 Instance = new DockingPageIntro1();



    private DockingPageIntro1(){
        super(new ViewPage2DComponent[]{
                text1, text1pg
            });
    }


    @Override
    public String name(){
        return Page.intro1.name();
    }
    @Override
    public Page value(){
        return Page.intro1;
    }
    @Override
    public void input(InputScript in){
        final Input type = in.type();

        switch(type){
        case Left:
            view.script(Page.intro);
            break;
        case Right:
            view.script(Page.intro2);
            break;
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
