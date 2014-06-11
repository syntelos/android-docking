/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * Floating point utilities
 */
public abstract class Epsilon
    extends ObjectLog
{

    protected final static float EPSILON_F = 1.0e-7f;
    protected final static double EPSILON_D = 1.0e-15;
    /**
     * A delta-epsilon operator performs a first approximation to
     * floating point numeric error for a difference from zero
     */
    protected final static float Z(float value){
        if (EPSILON_F < Math.abs(0.0f-value))
            return value;
        else
            return 0.0f;
    }
    protected final static double Z(double value){
        if (EPSILON_D < Math.abs(0.0-value))
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
            final double e = c * EPSILON_F;

            if (e < Math.abs(c-v)){

                return variable;
            }
            else {
                return constant;
            }
        }
        else if (EPSILON_F < Math.abs(constant-variable)){

            return variable;
        }
        else
            return constant;
    }

    protected final static double DEG = 180.0/Math.PI;

    protected final static float PAD_RATIO = 0.05f;

    protected final static String Format7(float value){
        final String string = String.format("%3.4f",value);
        final char[] sary = string.toCharArray();
        final int strlen = sary.length;
        if (7 <= strlen)
            return string;
        else {
            char[] cary = new char[7];
            {
                for (int cc = 0, sp = (7-strlen); cc < 7; cc++){
                    if (cc < sp){
                        cary[cc] = ' ';
                    }
                    else {
                        cary[cc] = sary[cc-sp];
                    }
                }
            }
            return new String(cary,0,7);
        }
    }
    protected final static String FormatT(float value){
        final String string = String.format("%3.1f",value);
        final char[] sary = string.toCharArray();
        final int strlen = sary.length;
        {
            char[] cary = new char[5];
            {
                for (int cc = 0, sp = (5-strlen); cc < 5; cc++){
                    if (cc < sp){
                        cary[cc] = '0';
                    }
                    else {
                        cary[cc] = sary[cc-sp];
                    }
                }
            }
            return new String(cary,0,5);
        }
    }
    protected final static String Format4(double value){
        final String string = String.format("%3.0f",value);
        final char[] sary = string.toCharArray();
        final int strlen = sary.length;
        if (4 <= strlen)
            return string;
        else {
            char[] cary = new char[4];
            {
                for (int cc = 0, sp = (4-strlen); cc < 4; cc++){
                    if (cc < sp){
                        cary[cc] = ' ';
                    }
                    else {
                        cary[cc] = sary[cc-sp];
                    }
                }
            }
            return new String(cary,0,4);
        }
    }
}
