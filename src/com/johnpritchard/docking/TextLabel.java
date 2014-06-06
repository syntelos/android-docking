/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

import path.Op;
import path.Operand;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * 
 */
public class TextLabel
    extends ViewPageComponentPath
    implements ViewPageComponentGroup
{


    public TextLabel(){
        super();
    }
    public TextLabel(String label){
        super(label);
    }


    public void draw(Canvas c){
        c.save();

        c.clipPath(this.clip,Region.Op.REPLACE);

        c.drawPath(this.path,this.fill);

        c.restore();
    }

}
