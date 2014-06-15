/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageIntro6
    extends DockingPageIntro
{

    public final static DockingPageIntro6 Instance = new DockingPageIntro6();


    private DockingPageIntro6(){
        super(new ViewPage2DComponent[]{
                text6
            });
    }


    @Override
    public String name(){
        return Page.intro6.name();
    }
    @Override
    public Page value(){
        return Page.intro6;
    }
    @Override
    public void input(InputScript in){
        final Input type = in.type();

        switch(type){
        case Left:
            view.script(Page.intro5);
            break;
        case Right:
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
