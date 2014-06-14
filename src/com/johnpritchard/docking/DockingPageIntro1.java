/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageIntro1
    extends ViewPage2D
{

    public final static DockingPageIntro1 Instance = new DockingPageIntro1();

    protected final static ViewPage2DTextGroup Group = DockingPageIntro.Group;



    private DockingPageIntro1(){
        super(new ViewPage2DComponent[]{
                new ViewPage2DTextMultiline(Group,"Use up, down, left, right and enter actions to select, modify, and execute thruster control.")
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
            view.script(Page.intro);
            break;
        case Right:
            view.script(Page.intro2);
            break;
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
