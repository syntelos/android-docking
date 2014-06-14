/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageIntro6
    extends ViewPage2D
{

    public final static DockingPageIntro6 Instance = new DockingPageIntro6();

    protected final static ViewPage2DTextGroup Group = DockingPageIntro.Group;



    private DockingPageIntro6(){
        super(new ViewPage2DComponent[]{
                new ViewPage2DTextMultiline(Group,"Vehicle mass is a constant 1000 kg, T10 is 100 N and T01 is 10 N.")
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
            view.script(Page.intro5);
            break;
        case Right:
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
