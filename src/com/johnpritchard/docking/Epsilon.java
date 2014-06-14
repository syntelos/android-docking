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

    protected final static float Seconds(double ms){

        return (float)(ms/1000.0);
    }
    protected final static float ClampRP(double value, double precis){

        return (float)(Math.floor(value*precis)/precis);
    }
    protected final static float AddClampRP(double a, double b, double precis){

        return ClampRP((a+b),precis);
    }
    protected final static float SubClampRP(double a, double b, double precis){

        return ClampRP((a-b),precis);
    }
    protected final static float MulClampRP(double a, double b, double precis){

        return ClampRP((a*b),precis);
    }
    protected final static float DivClampRP(double a, double b, double precis){

        return ClampRP((a/b),precis);
    }

    protected final static long ClampZP(long value){

        if (0L < value){

            return value;
        }
        else {
            return 0L;
        }
    }
    protected final static long AddClampZP(long a, long b){

        return ClampZP(a+b);
    }
    protected final static long SubClampZP(long a, long b){

        return ClampZP(a-b);
    }
    protected final static long MulClampZP(long a, long b){

        return ClampZP(a*b);
    }
    protected final static long DivClampZP(long a, long b){

        return ClampZP(a/b);
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
        String string = String.format("%3.1f",value);
        char[] sary = string.toCharArray();
        int strlen = sary.length;
        if (5 <= strlen){
            if (5 == strlen){

                return string;
            }
            else {
                int idx = string.lastIndexOf('.');
                if (0 < idx){
                    string = string.substring(0,idx);
                    sary = string.toCharArray();
                    strlen = sary.length;
                    if (5 <= strlen){
                        return string;
                    }
                }
                else {
                    return string;
                }
            }
        }
        //
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
    protected final static String Format5(double value){
        final String string = String.format("%3.0f",value);
        final char[] sary = string.toCharArray();
        final int strlen = sary.length;
        if (5 <= strlen)
            return string;
        else {
            char[] cary = new char[5];
            {
                for (int cc = 0, sp = (5-strlen); cc < 5; cc++){
                    if (cc < sp){
                        cary[cc] = ' ';
                    }
                    else {
                        cary[cc] = sary[cc-sp];
                    }
                }
            }
            return new String(cary,0,5);
        }
    }
    protected final static String Format8(double value){
        final String string = String.format("%3.4f",value);
        final char[] sary = string.toCharArray();
        final int strlen = sary.length;
        if (8 <= strlen)
            return string;
        else {
            char[] cary = new char[8];
            {
                for (int cc = 0, sp = (8-strlen); cc < 8; cc++){
                    if (cc < sp){
                        cary[cc] = ' ';
                    }
                    else {
                        cary[cc] = sary[cc-sp];
                    }
                }
            }
            return new String(cary,0,8);
        }
    }
}
