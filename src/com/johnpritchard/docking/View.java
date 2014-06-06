/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

import android.content.SharedPreferences;
import android.view.SurfaceHolder;

/**
 * @see View2D
 * @see View3D
 */
public interface View
    extends android.view.SurfaceHolder.Callback
{

    public boolean is2D();

    public boolean is3D();

    public SurfaceHolder getHolder();
    /**
     * Called from {@link ViewAnimator}
     */
    public void pageTo(Page page);

    public void onCreate(SharedPreferences state);

    public void onResume();

    public void onPause(SharedPreferences.Editor state);

    public SharedPreferences preferences();

}
