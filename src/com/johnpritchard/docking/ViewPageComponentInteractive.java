/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * @see ViewPage2D#input(com.johnpritchard.docking.InputScript)
 * @see ViewPage3D#input(com.johnpritchard.docking.InputScript)
 */
public interface ViewPageComponentInteractive
    extends ViewPageComponent
{
    /**
     * @return Component state is interactive, captures input
     */
    public boolean interactive();
    /**
     * @return Component state remains interactive, capturing input
     */
    public boolean input(InputScript in);

}
