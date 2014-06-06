/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

/**
 * Floating point utilities
 */
public abstract class Epsilon {

    protected final static float EPSILON = 1.0e-7f;
    protected final static double EPSILON_D = 1.0e-7;
    /**
     * A delta-epsilon operator performs a first approximation to
     * floating point numeric error for a difference from zero
     */
    protected final static float Z(float value){
        if (EPSILON < Math.abs(0.0f-value))
            return value;
        else
            return 0.0f;
    }
    /**
     * A delta-epsilon operator performs a first approximation to
     * floating point numeric error for the difference between
     * arbitrary values
     */
    protected final static float DE(float constant, float variable){
        if (1.0f < constant){
            final double c = constant;
            final double v = variable;
            final double e = c * EPSILON_D;

            if (e < Math.abs(c-v)){

                return variable;
            }
            else {
                return constant;
            }
        }
        else if (EPSILON < Math.abs(constant-variable)){

            return variable;
        }
        else
            return constant;
    }

}
