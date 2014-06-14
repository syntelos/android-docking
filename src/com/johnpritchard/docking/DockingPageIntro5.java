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
    extends ViewPage2D
{

    public final static DockingPageIntro5 Instance = new DockingPageIntro5();

    protected final static ViewPage2DTextGroup Group = DockingPageIntro.Group;



    private DockingPageIntro5(){
        super(new ViewPage2DComponent[]{
                new ViewPage2DTextMultiline(Group,"Velocity is meters per second, and thrust control is time seconds.")
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
            view.script(Page.intro4);
            break;
        case Right:
        case Enter:
            view.script(Page.intro6);
            break;
        }
    }
}
