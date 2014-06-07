/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * Parameters set by {@link ViewPage} for use from {@link
 * ViewPage2DComponent} in the {@link ViewPage} layout stage processing.
 */
public abstract class ViewDisplay {


    public static int Width, Height;

    public static boolean Portrait, Landscape;


    private ViewDisplay(){
        super();
    }

}
