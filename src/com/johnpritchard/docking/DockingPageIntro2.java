/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageIntro2
    extends DockingPageIntro
{

    public final static DockingPageIntro2 Instance = new DockingPageIntro2();



    private DockingPageIntro2(){
        super(new ViewPage2DComponent[]{
                text2
            });
    }


    @Override
    public String name(){
        return Page.intro2.name();
    }
    @Override
    public Page value(){
        return Page.intro2;
    }
    @Override
    public void input(InputScript in){
        final Input type = in.type();

        switch(type){
        case Left:
            view.script(Page.intro1);
            break;
        case Right:
            view.script(Page.intro3);
            break;
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
