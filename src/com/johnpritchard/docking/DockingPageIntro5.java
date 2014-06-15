/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageIntro5
    extends DockingPageIntro
{

    public final static DockingPageIntro5 Instance = new DockingPageIntro5();


    private DockingPageIntro5(){
        super(new ViewPage2DComponent[]{
                text5
            });
    }


    @Override
    public String name(){
        return Page.intro5.name();
    }
    @Override
    public Page value(){
        return Page.intro5;
    }
    @Override
    public void input(InputScript in){
        final Input type = in.type();

        switch(type){
        case Left:
            view.script(Page.intro4);
            break;
        case Right:
            view.script(Page.intro6);
            break;
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
