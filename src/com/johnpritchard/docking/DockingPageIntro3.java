/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageIntro3
    extends ViewPage2D
{

    public final static DockingPageIntro3 Instance = new DockingPageIntro3();

    protected final static ViewPage2DTextGroup Group = DockingPageIntro.Group;



    private DockingPageIntro3(){
        super(new ViewPage2DComponent[]{
                new ViewPage2DTextMultiline(Group,"Use up, down, left, right and enter actions to select, modify, and execute a thruster burn.")
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
            view.script(Page.intro2);
            break;
        case Right:
            view.script(Page.intro4);
            break;
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
