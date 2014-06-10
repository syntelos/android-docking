/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import path.Op;
import path.Operand;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * 
 */
public class ViewPage2DTextLabel
    extends ViewPage2DComponentPath
    implements ViewPageComponentGroup
{


    public ViewPage2DTextLabel(){
        super();
    }
    public ViewPage2DTextLabel(String label){
        super(label);
    }


    public void draw(Canvas c){
        c.save();

        c.clipPath(this.clip,Region.Op.REPLACE);

        c.drawPath(this.path,this.fill);

        c.restore();
    }

}
