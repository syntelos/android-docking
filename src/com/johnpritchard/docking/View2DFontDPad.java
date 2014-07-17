/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * Circle, triangle, square glyphs for DPad navigational indicators
 */
public class View2DFontDPad {

    public final static float Em = 10.0f;
    public final static float Ascent = 10.0f;
    public final static float Descent = 0.0f;
    public final static float Leading = 1.5f;

    private final static float Z = 0.0f;
    private final static float C = (Em/2.0f);

    public enum Char {
        Circle,
        Left,
        Top,
        Right,
        Bottom,
        Square;
    }

    public final static Path Apply(Char c, Path p){
        p.reset();
        switch(c){
        case Circle:
            {
                p.addCircle(C,C,C,Path.Direction.CCW);
            }
            break;
        case Left:
            {
                p.moveTo(Z,C);
                p.lineTo(Em,Em);
                p.lineTo(Em,Z);
                p.close();
            }
            break;
        case Top:
            {
                p.moveTo(C,Z);
                p.lineTo(Z,Em);
                p.lineTo(Em,Em);
                p.close();
            }
            break;
        case Right:
            {
                p.moveTo(Z,Z);
                p.lineTo(Z,Em);
                p.lineTo(Em,C);
                p.close();
            }
            break;
        case Bottom:
            {
                p.moveTo(Z,Z);
                p.lineTo(C,Em);
                p.lineTo(Em,Z);
                p.close();
            }
            break;
        case Square:
            {
                p.addRect(Z,Z,Em,Em,Path.Direction.CCW);
            }
            break;
        default:
            throw new IllegalArgumentException(c.name());
        }
        return p;
    }



    private View2DFontDPad(){
        super();
    }

}
