/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * 
 */
public class DockingPageIntro
    extends DockingPageAbout
{

    public final static DockingPageIntro Instance = new DockingPageIntro();


    private DockingPageIntro(){
        super(new ViewPage2DComponent[]{
                text0
            });
    }
    protected DockingPageIntro(ViewPage2DComponent[] c){
        super(c);
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
