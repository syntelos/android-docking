/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

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

    public Page currentPage();

    public void onCreate(SharedPreferences state);

    public void onResume();

    public void onPause(SharedPreferences.Editor state);

    public SharedPreferences preferences();

    public void script(Page pageTo);

    public void script(InputScript in);

    public void script(InputScript[] sequence);

    public class Script {

        protected final static InputScript[] Direction(Input dir){

            return new InputScript[]{Input.Skip,dir,Input.Emphasis,Input.Skip,Input.Deemphasis};
        }
        protected final static InputScript[] Enter(){

            return new InputScript[]{Input.Emphasis,Input.Skip,Input.Deemphasis,Input.Enter};
        }


        private Script(){
        }
    }
}
