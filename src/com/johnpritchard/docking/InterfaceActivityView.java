/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

import android.hardware.Camera;

/**
 * Interface from activity to view.
 */
public interface InterfaceActivityView {

    public void onPause();

    public void onResume();

    public void attach(Camera c) throws java.io.IOException;

}
