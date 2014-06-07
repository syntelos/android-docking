/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageAbout
    extends ViewPage2D
{
    public final static String AboutString = "Docking for Android version "+DockingVersion.String+'\n'+
                                             "Copyright (C) 2014 John Pritchard."+'\n'+
                                             "All rights reserved.";

    public final static DockingPageAbout Instance = new DockingPageAbout();



    private DockingPageAbout(){
        super(new ViewPage2DComponent[]{
                new TextMultiline(AboutString)
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
