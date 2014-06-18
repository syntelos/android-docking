/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import static android.opengl.GLES10.*;

import android.util.Log;

/**
 * GL version details intended for use from the GL thread.
 */
public abstract class View3DVersion
    extends ObjectLog
{

    private static boolean INIT = true;

    private static String VendorString;

    private static String ExtensionsString;

    private static String[] ExtensionsList;

    private static int ExtensionsCount;

    private static String LongString;

    private static String NameString;

    private static String NumberString;

    private static int Major, Minor;

    public final static void Init(){
        if (INIT){
            INIT = false;

            VendorString = glGetString(GL_VENDOR);

            ExtensionsString = glGetString(GL_EXTENSIONS);
            {
                String[] extensions = null;
                {
                    int start = 0, end = 0;
                    do {
                        end = ExtensionsString.indexOf(' ',start);
                        if (start < end){

                            String item = ExtensionsString.substring(start,end);

                            extensions = Add(extensions,item);

                            start = (end+1);
                        }
                        else {
                            break;
                        }
                    }
                    while (true);
                }

                if (null != extensions){
                    ExtensionsCount = extensions.length;
                    ExtensionsList = extensions;
                }
                else {
                    ExtensionsCount = 0;
                }
            }
            LongString = glGetString(GL_VERSION);

            Log.i(TAG,LongString);

            final int sp0 = LongString.indexOf(' ');

            final int sp1 = LongString.indexOf(' ',sp0+1);

            NameString = LongString.substring(sp0+1,sp1);

            NumberString = LongString.substring(sp1+1);

            final int dt = NumberString.indexOf('.');
            final String majorString = NumberString.substring(0,dt);

            final String minorString = NumberString.substring(dt+1);

            Major = Integer.parseInt(majorString);
            Minor = Integer.parseInt(minorString);
        }
    }
    public final static String GetVendorString(){
        return VendorString;
    }
    public final static String GetExtensionsString(){

        return ExtensionsString;
    }
    public final static String[] GetExtensions(){

        if (0 != ExtensionsCount){

            return ExtensionsList.clone();
        }
        else {
            return null;
        }
    }
    public final static String GetLongString(){
        return LongString;
    }
    public final static String GetNameString(){
        return NameString;
    }
    public final static String GetNumberString(){
        return NumberString;
    }
    public final static int GetMajor(){
        return Major;
    }
    public final static int GetMinor(){
        return Minor;
    }
    public final static boolean EQ(int major, int minor){
        return (major == Major && minor == Minor);
    }
    public final static boolean GE(int major, int minor){
        return (major >= Major && minor >= Minor);
    }
    public final static boolean GT(int major, int minor){
        if (major > Major){

            return true;
        }
        else if (major == Major){

            return (minor > Minor);
        }
        else {
            return false;
        }
    }
    public final static boolean LE(int major, int minor){
        return (major <= Major && minor <= Minor);
    }
    public final static boolean LT(int major, int minor){
        if (major < Major){

            return true;
        }
        else if (major == Major){

            return (minor < Minor);
        }
        else {
            return false;
        }
    }


    private View3DVersion(){
        super();
    }
}
