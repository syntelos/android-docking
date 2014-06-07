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
public class View2DTextLabel
    extends ViewPage2DComponentPath
    implements ViewPage2DComponentGroup
{


    public View2DTextLabel(){
        super();
    }
    public View2DTextLabel(String label){
        super(label);
    }


    public void draw(Canvas c){
        c.save();

        c.clipPath(this.clip,Region.Op.REPLACE);

        c.drawPath(this.path,this.fill);

        c.restore();
    }

}
