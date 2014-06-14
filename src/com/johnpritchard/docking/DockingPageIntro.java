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

    protected final static ViewPage2DTextGroup Group = new ViewPage2DTextGroup();



    private DockingPageIntro(){
        super(new ViewPage2DComponent[]{
                new ViewPage2DTextMultiline(Group,"Rendezvous in zero gravity using thrusters T10 and T01.")
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

        final Input type = in.type();

        switch(type){
        case Left:
            view.script(Page.intro6);
            break;
        case Right:
            view.script(Page.intro1);
            break;
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
