/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 
 */
public final class DockingPageIntro4
    extends ViewPage2D
{

    public final static DockingPageIntro4 Instance = new DockingPageIntro4();

    protected final static ViewPage2DTextGroup Group = DockingPageIntro.Group;



    private DockingPageIntro4(){
        super(new ViewPage2DComponent[]{
                new ViewPage2DTextMultiline(Group,"Physical units are 'kms': kilograms, meters, and seconds.")
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
            view.script(Page.intro3);
            break;
        case Right:
            view.script(Page.intro5);
            break;
        case Enter:
            view.script(Page.start);
            break;
        }
    }
}
