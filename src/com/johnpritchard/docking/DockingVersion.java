/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * Information derived from the android manifest.
 */
public final class DockingVersion {

    public final static int Code = 2;

    public final static String Name = "2";

    public final static String Type = "beta";

    public final static String Desc = Name+' '+Type;

    private DockingVersion(){
        super();
    }
}
