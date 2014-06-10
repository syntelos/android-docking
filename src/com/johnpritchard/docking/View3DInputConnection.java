/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.text.Editable;

/**
 * 
 */
public class View3DInputConnection
    extends android.view.inputmethod.BaseInputConnection
{


    public View3DInputConnection(View3D view){
        super(view,false);
    }


    public Editable getEditable(){
        return View3DInputEditable.Instance;
    }
}
