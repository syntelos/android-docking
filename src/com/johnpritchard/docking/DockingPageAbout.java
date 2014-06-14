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
    public final static String AboutString = "Docking for Android version "+DockingVersion.Name+'\n'+
                                             "Copyright (C) 2014 John Pritchard."+'\n'+
                                             "All rights reserved.";

    public final static DockingPageAbout Instance = new DockingPageAbout();



    private DockingPageAbout(){
        super(new ViewPage2DComponent[]{
                new ViewPage2DTextMultiline(AboutString)
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
    public void input(InputScript in){

        switch(in.type()){
        case Left:
        case Right:
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
