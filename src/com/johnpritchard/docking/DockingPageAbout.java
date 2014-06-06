/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageAbout
    extends ViewPage2D
{

    public final static DockingPageAbout Instance = new DockingPageAbout();



    private DockingPageAbout(){
        super(new ViewPageComponent[]{
                new TextMultiline("Lunar Lander version G for Android\nCopyright (C) 2014 John Pritchard.\nAll rights reserved.")
            });
    }


    @Override
    public String name(){
        return Page.about.name();
    }
    @Override
    public Page value(){
        return Page.about;
    }
    @Override
    public void input(Input in){

        switch(in){

        case Enter:

            if (-1 < enter()){

                ((View2D)view).script(Page.start);
            }
            return;

        case Back:
        case Up:
        case Down:
            ((View2D)view).script(Page.start);
            return;

        default:
            return;
        }
    }
}
