/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageIntro
    extends ViewPage2D
{

    public final static DockingPageIntro Instance = new DockingPageIntro();





    private DockingPageIntro(){
        super(new ViewPage2DComponent[]{
                new View2DTextMultiline("Intro")
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

        switch(in.type()){

        case Enter:

            if (-1 < enter()){

                view.script(Page.start);
            }
            return;

        case Back:
        case Up:
        case Down:
            view.script(Page.start);
            return;

        default:
            return;
        }
    }
}
