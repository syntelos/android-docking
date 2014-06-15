/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageNoHistory
    extends DockingPageAbout
{


    public final static DockingPageNoHistory Instance = new DockingPageNoHistory();


    private DockingPageNoHistory(){
        super(new ViewPage2DComponent[]{
                textH
            });
    }


    @Override
    public String name(){
        return Page.nohistory.name();
    }
    @Override
    public Page value(){
        return Page.nohistory;
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
