/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.RectF;
import android.util.Log;

/**
 * 
 */
public final class DockingFieldIOGroup
    extends ViewPage3DTextGroup
{

    protected final static int SEL_O = DockingFieldIO.SEL_O;
    protected final static double SEL_Z = DockingPageGameAbstract.Z;

    protected final static float RIGHT = 245.8f;

    protected final static DockingFieldIOGroup SEL_XP0 = new DockingFieldIOGroup(SEL_O,SEL_Z);
    protected final static DockingFieldIOGroup SEL_XM0 = new DockingFieldIOGroup(SEL_O,SEL_Z);
    protected final static DockingFieldIOGroup SEL_XP1 = new DockingFieldIOGroup(SEL_O,SEL_Z);
    protected final static DockingFieldIOGroup SEL_XM1 = new DockingFieldIOGroup(SEL_O,SEL_Z);



    public DockingFieldIOGroup(int ofs, double z){
        super(ofs,z);
    }


    @Override
    protected RectF close(RectF outline){

        outline.right = RIGHT;

        return outline;
    }
}
