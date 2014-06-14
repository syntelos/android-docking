/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

/**
 * An 80 bit random (string)
 */
public final class BID
    extends java.security.SecureRandom
{
    /*
     * 80 = (16 * 5)
     */
    private final static int RandomIdentifierOctets = 16;

    private final static int RandomOctetBits = 5;

    private final static char[] IdentifierMap = {
        'A', 'B', 'C', 'D',
        'E', 'F', 'G', 'H',
        'J', 'K', 'L', 'M',
        'N', 'P', 'Q', 'R',

        'S', 'T', 'U', 'V',
        'W', 'X', 'Y', 'Z',
        '2', '3', '4', '5',
        '6', '7', '8', '9'
    };

    private final static BID prng = new BID();

    /**
     * @return A random string
     */
    public final static String Identifier(){
        char[] cary = new char[RandomIdentifierOctets];
        {
            for (int cc = 0; cc < RandomIdentifierOctets; cc++){

                cary[cc] = prng.next();
            }
        }
        return new String(cary,0,RandomIdentifierOctets);
    }

    private final static byte[] Seed(){
        byte[] s = new byte[128];
        (new java.util.Random()).nextBytes(s);
        return s;
    }


    private BID(){
        super(Seed());
    }


    protected char next(){

        int x = nextInt(RandomOctetBits);

        return IdentifierMap[x];
    }
}
