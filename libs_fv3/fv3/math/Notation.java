/*
 * fv3.math
 * Copyright (C) 2012, John Pritchard, all rights reserved.
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package fv3.math;

/**
 * Array notation for {@link Vector} and {@link Matrix}
 * 
 */
public interface Notation
{
    /**
     * Common Math constants, epsilon for values 0.0 to 10.0.
     * 
     * @see java.lang.Math#ulp
     */
    public final static float EPSILON = 1.0e-7f;
    public final static float EPS = EPSILON;
    public final static float EPS_M2 = (EPSILON*2.0f);
    public final static float EPS_D2 = (EPSILON/2.0f);
    public final static float EPS_1D2 = (1.0f - EPS_D2);

    public final static double D_EPSILON = 1.0e-13;
    public final static double D_EPS = D_EPSILON;
    public final static double D_EPS_M2 = (D_EPSILON*2.0);
    public final static double D_EPS_D2 = (D_EPSILON/2.0);
    public final static double D_EPS_1D2 = (1.0 - D_EPS_D2);

    public final static float PI = (float)Math.PI;
    public final static float PI_D2 = (PI / 2.0f);
    public final static float PI_M2 = (PI * 2.0f);
    public final static float PI_D3 = (PI / 3.0f);
    /**
     * Radians per degree.  Multiply by degrees for radians.
     */
    public final static float Degrees = (float)(Math.PI / 180.0);
    /**
     * Simple zero float
     */
    public final static float ZERO = 0.0f;

    /**
     * Vector array notation
     */
    public final static int X = 0;
    public final static int Y = 1;
    public final static int Z = 2;

    /**
     * Matrix array notation
     */
    public final static int M00 =  0;
    public final static int M01 =  4;
    public final static int M02 =  8;
    public final static int M03 =  12;
    public final static int M10 =  1;
    public final static int M11 =  5;
    public final static int M12 =  9;
    public final static int M13 =  13;
    public final static int M20 =  2;
    public final static int M21 =  6;
    public final static int M22 =  10;
    public final static int M23 =  14;
    public final static int M30 =  3;
    public final static int M31 =  7;
    public final static int M32 =  11;
    public final static int M33 =  15;

    /**
     * Color array notation
     */
    public final static int R = 0;
    public final static int G = 1;
    public final static int B = 2;
    public final static int A = 3;
}
