/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageHistory
    extends ViewPage2D
{

    public final static DockingPageHistory Instance = new DockingPageHistory();





    private DockingPageHistory(){
        super(new ViewPage2DComponent[]{
                new ViewPage2DTextLabel("History")
            });
    }


    @Override
    public String name(){
        return Page.history.name();
    }
    @Override
    public Page value(){
        return Page.history;
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
