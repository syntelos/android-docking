/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import java.util.Random;

/**
 * A fairly recognizable random string
 */
public abstract class BID {

    private final static int RandomIdentifierOctets = 12;

    /**
     * @return A random string having some fixed properties
     */
    public final static String Identifier(){
        byte[] bits = new byte[RandomIdentifierOctets];
        {
            new Random().nextBytes(bits);
        }
        return RandomIdentifierPathclean(B64.encodeBytes(bits));
    }
    private final static String RandomIdentifierPathclean(String r){
        char[] cary = r.toCharArray();
        final int count = cary.length;
        boolean change = false;
        for (int cc = 0; cc < count; cc++){
            switch (cary[cc]){
            case '_':
                change = true;
                cary[cc] = 'A';
                break;
            case '/':
                change = true;
                cary[cc] = 'B';
                break;
            case '+':
                change = true;
                cary[cc] = 'C';
                break;
            case '\r':
                change = true;
                cary[cc] = 'D';
                break;
            case '\n':
                change = true;
                cary[cc] = 'E';
                break;
            case '=':
                change = true;
                cary[cc] = 'F';
                break;
            default:
                break;
            }
        }
        if (change)
            return new String(cary,0,count);
        else
            return r;
    }


    private BID(){
        super();
    }

}
