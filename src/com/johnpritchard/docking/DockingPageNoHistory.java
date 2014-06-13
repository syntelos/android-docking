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
    extends ViewPage2D
{

    public final static DockingPageNoHistory Instance = new DockingPageNoHistory();





    private DockingPageNoHistory(){
        super(new ViewPage2DComponent[]{
                new ViewPage2DTextLabel("Record not found")
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

        if (-1 < enter()){

            view.script(Page.start);
        }
    }
}
