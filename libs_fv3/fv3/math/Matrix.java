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
 * A transformation matrix in geometric three- space.
 * 
 * This class employs the Row Major Notation.  The matrix notation is
 * an addressing scheme.  In using Row Major Notation, this class
 * adopts an address mapping to Open GL's Row Minor storage format.
 * 
 * The Row Major Notation used exposed by this class expresses the
 * elements of the matrix as illustrated in the following:
 * 
 * <pre>
 *          | 0,0  0,1  0,2  0,3 |
 *          |                    |
 *          | 1,0  1,1  1,2  1,3 |
 *      M = |                    |
 *          | 2,0  2,1  2,2  2,3 |
 *          |                    |
 *          | 3,0  3,1  3,2  3,3 |
 * </pre>
 * 
 * This class has been written with Row Major Notation for its
 * familiarity with C and Java syntax, as in:
 * 
 * <pre>
 * float[][] M = new float[4][4];
 * for (int I = 0, J; I &lt; 4; I++){
 *     for (J = 0; J &lt; 4; J++){
 * 
 *         if ( I == J)
 *             M[I][J] = 1.0;
 *         else
 *             M[I][J] = 0.0;
 *     }
 * }
 * </pre>
 * 
 * From Row Major notation, this class maps storage location for Open
 * GL.  GL's matrix notation and storage, as in the red book, is Row
 * Minor (Column Major).  
 * 
 * Row Minor Notation can be confusing from the perspective of the
 * programming language, while it's natural from the relative
 * perspective of hardware internals.  The Row Minor storage order is
 * defined as follows.
 * 
 * <pre>
 *          |  0    4    8   12  |
 *          |                    |
 *          |  1    5    9   13  |
 *      N = |                    |
 *          |  2    6   10   14  |
 *          |                    |
 *          |  3    7   11   15  |
 * </pre>
 * 
 * 
 * @see Abstract
 * @author jdp
 */
public class Matrix
    extends AbstractFloat
{


    private final float[] m;


    public Matrix(){
        super();
        this.m = Identity.clone();
    }
    public Matrix(float[] m){
        super();
        if (null != m && 16 == m.length)
            this.m = m;
        else
            throw new IllegalArgumentException();
    }
    public Matrix(Matrix m){
        super();
        this.m = m.array().clone();
    }


    public final Matrix identity(){
        System.arraycopy(Identity,0,this.m,0,16);
        return this;
    }
    public final float[] array(){
        return this.m;
    }
    public final Matrix copyFrom(Matrix m){
        return this.copyFrom(m.m);
    }
    public final Matrix copyFrom(float[] m){
        if (16 == m.length){
            System.arraycopy(m,0,this.m,0,16);
            return this;
        }
        else
            throw new IllegalArgumentException();
    }
    public final Matrix copyTo(Matrix m){
        this.copyTo(m.m);
        return m;
    }
    public final float[] copyTo(float[] m){
        if (16 == m.length){
            System.arraycopy(this.m,0,m,0,16);
            return m;
        }
        else
            throw new IllegalArgumentException();
    }
    public final Matrix mul(Matrix m){
        return this.mul(m.m);
    }
    public final Matrix mul(float[] b){
        float[] m = this.m;
        float[] a = m.clone();
        int i, j, k;
        float ab;
        for (j = 0; j < 4; j++) {
            for (i = 0; i < 4; i++) {
                ab = ZERO;
                for (k = 0; k < 4; k++){
                    ab += (a[I(k,i)] * b[I(j,k)]);
                }
                m[I(j,i)] = Z1(ab);
            }
        }
        return this;
    }
    public final Matrix translate(float x, float y, float z){
        float[] m = this.m;

        for (int i = 0; i < 3; i++) {
            m[I(i,3)] += m[I(i,0)] * x + m[I(i,1)] * y + m[I(i,2)] * z;
        }
        return this;
    }
    public final Matrix translateX(float x){
        return this.translate(x,0,0);
    }
    public final Matrix translateY(float y){
        return this.translate(0,y,0);
    }
    public final Matrix translateZ(float z){
        return this.translate(0,0,z);
    }
    public final Matrix translate(Vector v){
        float[] vv = v.array();
        return this.translate(vv[X],vv[Y],vv[Z]);
    }
    public final Vector getTranslation(){
        return new Vector(this.m[M03],this.m[M13],this.m[M23]);
    }
    public final Matrix translate(int axis, float delta){
        switch(axis){
        case X:
            return this.translateX(delta);
        case Y:
            return this.translateY(delta);
        case Z:
            return this.translateZ(delta);
        default:
            throw new IllegalArgumentException(String.valueOf(axis));
        }
    }
    public final Matrix scale(float s){
        if (1.0f != s){
            float[] m = this.m;

            for (int i = 0; i < 4; i++) {
                m[I(i,0)] *= s;
                m[I(i,1)] *= s;
                m[I(i,2)] *= s;
            }
        }
        return this;
    }
    public final Matrix scale(float x, float y, float z){

        float[] m = this.m;

        for (int i = 0; i < 4; i++) {
            m[I(i,0)] *= x;
            m[I(i,1)] *= y;
            m[I(i,2)] *= z;
        }
        return this;
    }
    public final Vector getScale(){
        return new Vector(this.m[M00],this.m[M11],this.m[M22]);
    }
    public final Matrix rotate(AxisAngle a){
        float[] axis = a.normalize().array();
        float[] R = Identity.clone();
        
        float x = axis[Vector.X];
        float y = axis[Vector.Y];
        float z = axis[Vector.Z];

        float angle = a.angle();
        float s = (float)Math.sin(angle);
        float c = (float)Math.cos(angle);
        float c1 = (1.0f - c);

        float xx = (x * x);
        float yy = (y * y);
        float zz = (z * z);
        float xy = (x * y);
        float xz = (x * z);
        float yz = (y * z);
        float xs = (x * s);
        float ys = (y * s);
        float zs = (z * s);

        R[M00] = ((xx * c1) +  c);
        R[M01] = ((xy * c1) - zs);
        R[M02] = ((xz * c1) + ys);
        R[M10] = ((xy * c1) + zs);
        R[M11] = ((yy * c1) +  c);
        R[M12] = ((yz * c1) - xs);
        R[M20] = ((xz * c1) - ys);
        R[M21] = ((yz * c1) + xs);
        R[M22] = ((zz * c1) +  c);

        return this.mul(R);
    }
    public final Matrix rotateX(float rax){
        float[] R = Identity.clone();

        float c = (float)Math.cos(rax);
        float s = (float)Math.sin(rax);

        R[M11] = c;
        R[M21] = -s;

        R[M12] = s;
        R[M22] = c;

        return this.mul(R);
    }
    public final Matrix rotateY(float ray){
        float[] R = Identity.clone();

        float c = (float)Math.cos(ray);
        float s = (float)Math.sin(ray);

        R[M00] = c;
        R[M20] = -s;

        R[M02] = s;
        R[M22] = c;

        return this.mul(R);
    }
    public final Matrix rotateZ(float raz){
        float[] R = Identity.clone();

        float c = (float)Math.cos(raz);
        float s = (float)Math.sin(raz);

        R[M00] = c;
        R[M10] = -s;

        R[M01] = s;
        R[M11] = c;

        return this.mul(R);
    }
    public final Matrix rotate(float rax, float ray, float raz){
        float[] R = Identity.clone();

        float cx = (float)Math.cos(rax);
        float sx = (float)Math.sin(rax);

        float cy = (float)Math.cos(ray);
        float sy = (float)Math.sin(ray);

        float cz = (float)Math.cos(raz);
        float sz = (float)Math.sin(raz);

        R[M00] =  cy * cz;
        R[M01] = -cy * sz;
        R[M02] =  sy;

        R[M10] =  (sx * sy * cz)+(cx * sz);
        R[M11] = -(sx * sy * sz) + (cx * cz);
        R[M12] = -sx * cy;

        R[M20] = -(cx * sy * cz) + (sx * sz);
        R[M21] =  (cx * sy * sz) + (sx * cz);
        R[M22] =  cx * cy;

        return this.mul(R);
    }
    public final Matrix rotate(int axis, float radians){
        switch(axis){
        case X:
            return this.rotateX(radians);
        case Y:
            return this.rotateY(radians);
        case Z:
            return this.rotateZ(radians);
        default:
            throw new IllegalArgumentException(String.valueOf(axis));
        }
    }
    public final Vector transform(Vector v){
        float[] c = v.array();
        float[] mm = this.m;
        float[] a = c.clone();

        c[X] = mm[M00] * a[X] + mm[M01] * a[Y] + mm[M02] * a[Z] + mm[M03];
        c[Y] = mm[M10] * a[X] + mm[M11] * a[Y] + mm[M12] * a[Z] + mm[M13];
        c[Z] = mm[M20] * a[X] + mm[M21] * a[Y] + mm[M22] * a[Z] + mm[M23];

        return v;
    }
    public final float[] transform(float[] v){

        float[] mm = this.m;

        if (3 < v.length){

            for (int index = 0, count = v.length; index < count; index += 3){

                final float vX = v[index+X];
                final float vY = v[index+Y];
                final float vZ = v[index+Z];

                v[index+X] = mm[M00] * vX + mm[M01] * vY + mm[M02] * vZ + mm[M03];
                v[index+Y] = mm[M10] * vX + mm[M11] * vY + mm[M12] * vZ + mm[M13];
                v[index+Z] = mm[M20] * vX + mm[M21] * vY + mm[M22] * vZ + mm[M23];
            }
        }
        else {
            final float vX = v[X];
            final float vY = v[Y];
            final float vZ = v[Z];

            v[X] = mm[M00] * vX + mm[M01] * vY + mm[M02] * vZ + mm[M03];
            v[Y] = mm[M10] * vX + mm[M11] * vY + mm[M12] * vZ + mm[M13];
            v[Z] = mm[M20] * vX + mm[M21] * vY + mm[M22] * vZ + mm[M23];
        }
        return v;
    }
    public final float[] transform(float[] v, final int ofs){

        final float[] mm = this.m;

        final float vX = v[ofs+X];
        final float vY = v[ofs+Y];
        final float vZ = v[ofs+Z];

        v[ofs+X] = mm[M00] * vX + mm[M01] * vY + mm[M02] * vZ + mm[M03];
        v[ofs+Y] = mm[M10] * vX + mm[M11] * vY + mm[M12] * vZ + mm[M13];
        v[ofs+Z] = mm[M20] * vX + mm[M21] * vY + mm[M22] * vZ + mm[M23];

        return v;
    }
    public final float m00(){
        return this.m[M00];
    }
    public final float getM00(){
        return this.m[M00];
    }
    public final Matrix m00(float m00){
        this.m[M00] = Z(m00);
        return this;
    }
    public final Matrix setM00(float m00){
        this.m[M00] = Z(m00);
        return this;
    }
    public final float m01(){
        return this.m[M01];
    }
    public final float getM01(){
        return this.m[M01];
    }
    public final Matrix m01(float m01){
        this.m[M01] = Z(m01);
        return this;
    }
    public final Matrix setM01(float m01){
        this.m[M01] = Z(m01);
        return this;
    }
    public final float m02(){
        return this.m[M02];
    }
    public final float getM02(){
        return this.m[M02];
    }
    public final Matrix m02(float m02){
        this.m[M02] = Z(m02);
        return this;
    }
    public final Matrix setM02(float m02){
        this.m[M02] = Z(m02);
        return this;
    }
    public final float m03(){
        return this.m[M03];
    }
    public final float getM03(){
        return this.m[M03];
    }
    public final Matrix m03(float m03){
        this.m[M03] = Z(m03);
        return this;
    }
    public final Matrix setM03(float m03){
        this.m[M03] = Z(m03);
        return this;
    }
    public final float m10(){
        return this.m[M10];
    }
    public final float getM10(){
        return this.m[M10];
    }
    public final Matrix m10(float m10){
        this.m[M10] = Z(m10);
        return this;
    }
    public final Matrix setM10(float m10){
        this.m[M10] = Z(m10);
        return this;
    }
    public final float m11(){
        return this.m[M11];
    }
    public final float getM11(){
        return this.m[M11];
    }
    public final Matrix m11(float m11){
        this.m[M11] = Z(m11);
        return this;
    }
    public final Matrix setM11(float m11){
        this.m[M11] = Z(m11);
        return this;
    }
    public final float m12(){
        return this.m[M12];
    }
    public final float getM12(){
        return this.m[M12];
    }
    public final Matrix m12(float m12){
        this.m[M12] = Z(m12);
        return this;
    }
    public final Matrix setM12(float m12){
        this.m[M12] = Z(m12);
        return this;
    }
    public final float m13(){
        return this.m[M13];
    }
    public final float getM13(){
        return this.m[M13];
    }
    public final Matrix m13(float m13){
        this.m[M13] = Z(m13);
        return this;
    }
    public final Matrix setM13(float m13){
        this.m[M13] = Z(m13);
        return this;
    }
    public final float m20(){
        return this.m[M20];
    }
    public final float getM20(){
        return this.m[M20];
    }
    public final Matrix m20(float m20){
        this.m[M20] = Z(m20);
        return this;
    }
    public final Matrix setM20(float m20){
        this.m[M20] = Z(m20);
        return this;
    }
    public final float m21(){
        return this.m[M21];
    }
    public final float getM21(){
        return this.m[M21];
    }
    public final Matrix m21(float m21){
        this.m[M21] = Z(m21);
        return this;
    }
    public final Matrix setM21(float m21){
        this.m[M21] = Z(m21);
        return this;
    }
    public final float m22(){
        return this.m[M22];
    }
    public final float getM22(){
        return this.m[M22];
    }
    public final Matrix m22(float m22){
        this.m[M22] = Z(m22);
        return this;
    }
    public final Matrix setM22(float m22){
        this.m[M22] = Z(m22);
        return this;
    }
    public final float m23(){
        return this.m[M23];
    }
    public final float getM23(){
        return this.m[M23];
    }
    public final Matrix m23(float m23){
        this.m[M23] = Z(m23);
        return this;
    }
    public final Matrix setM23(float m23){
        this.m[M23] = Z(m23);
        return this;
    }
    public final float m30(){
        return this.m[M30];
    }
    public final float getM30(){
        return this.m[M30];
    }
    public final Matrix m30(float m30){
        this.m[M30] = Z(m30);
        return this;
    }
    public final Matrix setM30(float m30){
        this.m[M30] = Z(m30);
        return this;
    }
    public final float m31(){
        return this.m[M31];
    }
    public final float getM31(){
        return this.m[M31];
    }
    public final Matrix m31(float m31){
        this.m[M31] = Z(m31);
        return this;
    }
    public final Matrix setM31(float m31){
        this.m[M31] = Z(m31);
        return this;
    }
    public final float m32(){
        return this.m[M32];
    }
    public final float getM32(){
        return this.m[M32];
    }
    public final Matrix m32(float m32){
        this.m[M32] = Z(m32);
        return this;
    }
    public final Matrix setM32(float m32){
        this.m[M32] = Z(m32);
        return this;
    }
    public final float m33(){
        return this.m[M33];
    }
    public final float getM33(){
        return this.m[M33];
    }
    public final Matrix m33(float m33){
        this.m[M33] = Z(m33);
        return this;
    }
    public final Matrix setM33(float m33){
        this.m[M33] = Z(m33);
        return this;
    }

    public String toString(){
        return this.toString("");
    }
    public String toString(String pr){
        return this.toString(pr,"\n");
    }
    public String toString(String pr, String in){
        if (null == pr)
            pr = "";
        if (null == in)
            in = "";
        float[] m = this.m;
        return String.format("%s%30.26f %30.26f %30.26f %30.26f%s%s%30.26f %30.26f %30.26f %30.26f%s%s%30.26f %30.26f %30.26f %30.26f%s%s%30.26f %30.26f %30.26f %30.26f", pr, m[M00], m[M01], m[M02], m[M03], in, pr, m[M10], m[M11], m[M12], m[M13], in, pr, m[M20], m[M21], m[M22], m[M23], in, pr, m[M30], m[M31], m[M32], m[M33]);
    }


    /** 
     * @param m Row Major Row Index
     * @param n Row Major Column Index
     * @return GL Array Index
     */
    public final static int I(int m, int n){
        switch (m){
        case 0:
            return (4*n);
        case 1:
            return (1+(4*n));
        case 2:
            return (2+(4*n));
        case 3:
            return (3+(4*n));
        default:
            throw new IllegalArgumentException(String.valueOf(n));
        }
    }


    private final static float[] Identity = {1.0f, ZERO, ZERO, ZERO, 
                                             ZERO, 1.0f, ZERO, ZERO, 
                                             ZERO, ZERO, 1.0f, ZERO, 
                                             ZERO, ZERO, ZERO, 1.0f};

    public final static float[] Identity(){
        return Identity.clone();
    }
    public final static void Identity(float[] m){
        System.arraycopy(Identity,0,m,0,16);
    }

    private final static double[] IVIdentity = {1.0, 0.0, 0.0, 0.0, 
                                                0.0, 1.0, 0.0, 0.0, 
                                                0.0, 0.0, 1.0, 0.0, 
                                                0.0, 0.0, 0.0, 1.0};

    public final static double[] IVIdentity(){
        return IVIdentity.clone();
    }
    public final static void IVIdentity(double[] m){
        System.arraycopy(IVIdentity,0,m,0,16);
    }

    /**
     * Axis-angle rotation for normalized axis A, stored into R.
     */
    public final static void Rotate(float angle, float ax, float ay, float az, 
                                    float[] R)
    {
        Matrix.Identity(R);

        float s = (float)Math.sin(angle);
        float c = (float)Math.cos(angle);
        float c1 = (1.0f - c);

        float xx = (ax * ax);
        float yy = (ay * ay);
        float zz = (az * az);
        float xy = (ax * ay);
        float xz = (ax * az);
        float yz = (ay * az);
        float xs = (ax * s);
        float ys = (ay * s);
        float zs = (az * s);

        R[M00] = ((xx * c1) +  c);
        R[M01] = ((xy * c1) - zs);
        R[M02] = ((xz * c1) + ys);
        R[M10] = ((xy * c1) + zs);
        R[M11] = ((yy * c1) +  c);
        R[M12] = ((yz * c1) - xs);
        R[M20] = ((xz * c1) - ys);
        R[M21] = ((yz * c1) + xs);
        R[M22] = ((zz * c1) +  c);
    }
    public final static void Rotate(double angle, double ax, double ay, double az, 
                                    double[] R)
    {
        Matrix.IVIdentity(R);

        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double c1 = (1.0f - c);

        double xx = (ax * ax);
        double yy = (ay * ay);
        double zz = (az * az);
        double xy = (ax * ay);
        double xz = (ax * az);
        double yz = (ay * az);
        double xs = (ax * s);
        double ys = (ay * s);
        double zs = (az * s);

        R[M00] = ((xx * c1) +  c);
        R[M01] = ((xy * c1) - zs);
        R[M02] = ((xz * c1) + ys);
        R[M10] = ((xy * c1) + zs);
        R[M11] = ((yy * c1) +  c);
        R[M12] = ((yz * c1) - xs);
        R[M20] = ((xz * c1) - ys);
        R[M21] = ((yz * c1) + xs);
        R[M22] = ((zz * c1) +  c);
    }
    public final static void Rotate(float rax, float ray, float raz, float[] R){

        Matrix.Identity(R);

        float cx = (float)Math.cos(rax);
        float sx = (float)Math.sin(rax);

        float cy = (float)Math.cos(ray);
        float sy = (float)Math.sin(ray);

        float cz = (float)Math.cos(raz);
        float sz = (float)Math.sin(raz);

        R[M00] = ( cy * cz);
        R[M01] = (-cy * sz);
        R[M02] = ( sy);

        R[M10] = ( (sx * sy * cz)+(cx * sz));
        R[M11] = (-(sx * sy * sz) + (cx * cz));
        R[M12] = (-sx * cy);

        R[M20] = (-(cx * sy * cz) + (sx * sz));
        R[M21] = ( (cx * sy * sz) + (sx * cz));
        R[M22] = ( cx * cy);

    }
    public final static void Rotate(double rax, double ray, double raz, double[] R){

        Matrix.IVIdentity(R);

        double cx = Math.cos(rax);
        double sx = Math.sin(rax);

        double cy = Math.cos(ray);
        double sy = Math.sin(ray);

        double cz = Math.cos(raz);
        double sz = Math.sin(raz);

        R[M00] = ( cy * cz);
        R[M01] = (-cy * sz);
        R[M02] = ( sy);

        R[M10] = ( (sx * sy * cz)+(cx * sz));
        R[M11] = (-(sx * sy * sz) + (cx * cz));
        R[M12] = (-sx * cy);

        R[M20] = (-(cx * sy * cz) + (sx * sz));
        R[M21] = ( (cx * sy * sz) + (sx * cz));
        R[M22] = ( cx * cy);

    }
    public final static float[] Mul(float[] a, float[] b){
        float[] c = Identity.clone();

        int i, j, k;
        float ab;
        for (j = 0; j < 4; j++) {
            for (i = 0; i < 4; i++) {
                ab = 0.0f;
                for (k = 0; k < 4; k++){
                    ab += (a[I(k,i)] * b[I(j,k)]);
                }
                c[I(j,i)] = Z1(ab);
            }
        }
        return c;
    }
    public final static double[] Mul(double[] a, double[] b){
        double[] c = IVIdentity.clone();

        int i, j, k;
        double ab;
        for (j = 0; j < 4; j++) {
            for (i = 0; i < 4; i++) {
                ab = 0.0f;
                for (k = 0; k < 4; k++){
                    ab += (a[I(k,i)] * b[I(j,k)]);
                }
                c[I(j,i)] = Z1(ab);
            }
        }
        return c;
    }
    public final static float[] Copy(double[] d){
        float[] f = new float[16];
        for (int cc = 0; cc < 16; cc++){
            f[cc] = (float)d[cc];
        }
        return f;
    }
    public final static void Copy(double[] d, float[] f){

        for (int cc = 0; cc < 16; cc++){
            f[cc] = (float)d[cc];
        }
    }
    public final static void Scale(double[] m, double s){
        if (1.0 != s){

            for (int i = 0; i < 4; i++) {
                m[I(i,0)] *= s;
                m[I(i,1)] *= s;
                m[I(i,2)] *= s;
            }
        }
    }
    public final static void Scale(float[] m, float s){
        if (1.0 != s){

            for (int i = 0; i < 4; i++) {
                m[I(i,0)] *= s;
                m[I(i,1)] *= s;
                m[I(i,2)] *= s;
            }
        }
    }
    public final static double[] Translate(double[] m, double x, double y, double z){

        for (int i = 0; i < 3; i++) {

            m[I(i,3)] += m[I(i,0)] * x + m[I(i,1)] * y + m[I(i,2)] * z;
        }
        return m;
    }
    public final static float[] Translate(float[] m, float x, float y, float z){

        for (int i = 0; i < 3; i++) {

            m[I(i,3)] += m[I(i,0)] * x + m[I(i,1)] * y + m[I(i,2)] * z;
        }
        return m;
    }
    public final static float[] Translate(float x, float y, float z){

        float[] m = Identity();

        for (int i = 0; i < 3; i++) {

            m[I(i,3)] += m[I(i,0)] * x + m[I(i,1)] * y + m[I(i,2)] * z;
        }
        return m;
    }
    public final static void Transform(double[] m, float[] v){

        if (3 < v.length){

            for (int index = 0, count = v.length; index < count; index += 3){

                final double vX = v[index+X];
                final double vY = v[index+Y];
                final double vZ = v[index+Z];

                v[index+X] = (float)(m[M00] * vX + m[M01] * vY + m[M02] * vZ + m[M03]);
                v[index+Y] = (float)(m[M10] * vX + m[M11] * vY + m[M12] * vZ + m[M13]);
                v[index+Z] = (float)(m[M20] * vX + m[M21] * vY + m[M22] * vZ + m[M23]);
            }
        }
        else {
            final double vX = v[X];
            final double vY = v[Y];
            final double vZ = v[Z];

            v[X] = (float)(m[M00] * vX + m[M01] * vY + m[M02] * vZ + m[M03]);
            v[Y] = (float)(m[M10] * vX + m[M11] * vY + m[M12] * vZ + m[M13]);
            v[Z] = (float)(m[M20] * vX + m[M21] * vY + m[M22] * vZ + m[M23]);
        }
    }

    public final static float[] RotateX(float rax){
        float[] R = Identity.clone();

        float c = (float)Math.cos(rax);
        float s = (float)Math.sin(rax);

        R[M11] = c;
        R[M21] = -s;

        R[M12] = s;
        R[M22] = c;

        return R;
    }
    public final static float[] RotateY(float ray){
        float[] R = Identity.clone();

        float c = (float)Math.cos(ray);
        float s = (float)Math.sin(ray);

        R[M00] = c;
        R[M20] = -s;

        R[M02] = s;
        R[M22] = c;

        return R;
    }
    public final static float[] RotateZ(float raz){
        float[] R = Identity.clone();

        float c = (float)Math.cos(raz);
        float s = (float)Math.sin(raz);

        R[M00] = c;
        R[M10] = -s;

        R[M01] = s;
        R[M11] = c;

        return R;
    }
    public final static float[] Rotate(float rax, float ray, float raz){
        float[] R = Identity.clone();

        float cx = (float)Math.cos(rax);
        float sx = (float)Math.sin(rax);

        float cy = (float)Math.cos(ray);
        float sy = (float)Math.sin(ray);

        float cz = (float)Math.cos(raz);
        float sz = (float)Math.sin(raz);

        R[M00] =  cy * cz;
        R[M01] = -cy * sz;
        R[M02] =  sy;

        R[M10] =  (sx * sy * cz)+(cx * sz);
        R[M11] = -(sx * sy * sz) + (cx * cz);
        R[M12] = -sx * cy;

        R[M20] = -(cx * sy * cz) + (sx * sz);
        R[M21] =  (cx * sy * sz) + (sx * cz);
        R[M22] =  cx * cy;

        return R;
    }

    private final static double Sign(double x){
        if (x >= 0.0)
            return +1.0;
        else
            return -1.0;
    }
    /**
     * Interesting test subject for review
     * 
     * @see http://www.cg.info.hiroshima-cu.ac.jp/~miyazaki/knowledge/teche52.html
     */
    public final static double[] Quaternion(double[] m){
        double r11 = m[M00];
        double r12 = m[M01];
        double r13 = m[M02];

        double r21 = m[M10];
        double r22 = m[M11];
        double r23 = m[M12];

        double r31 = m[M20];
        double r32 = m[M21];
        double r33 = m[M22];

        double q0 = ( r11 + r22 + r33 + 1.0) / 4.0;
        double q1 = ( r11 - r22 - r33 + 1.0) / 4.0;
        double q2 = (-r11 + r22 - r33 + 1.0) / 4.0;
        double q3 = (-r11 - r22 + r33 + 1.0) / 4.0;

        if (q0 < 0.0) q0 = 0.0;
        if (q1 < 0.0) q1 = 0.0;
        if (q2 < 0.0) q2 = 0.0;
        if (q3 < 0.0) q3 = 0.0;

        q0 = Math.sqrt(q0);
        q1 = Math.sqrt(q1);
        q2 = Math.sqrt(q2);
        q3 = Math.sqrt(q3);

        if (q0 >= q1 && q0 >= q2 && q0 >= q3) {
            q0 *= +1.0;
            q1 *= Sign(r32 - r23);
            q2 *= Sign(r13 - r31);
            q3 *= Sign(r21 - r12);
        }
        else if (q1 >= q0 && q1 >= q2 && q1 >= q3) {
            q0 *= Sign(r32 - r23);
            q1 *= +1.0;
            q2 *= Sign(r21 + r12);
            q3 *= Sign(r13 + r31);
        }
        else if (q2 >= q0 && q2 >= q1 && q2 >= q3) {
            q0 *= Sign(r13 - r31);
            q1 *= Sign(r21 + r12);
            q2 *= +1.0;
            q3 *= Sign(r32 + r23);
        }
        else if (q3 >= q0 && q3 >= q1 && q3 >= q2) {
            q0 *= Sign(r21 - r12);
            q1 *= Sign(r31 + r13);
            q2 *= Sign(r32 + r23);
            q3 *= +1.0;
        }
        else {
            throw new IllegalStateException();
        }
        double r = Vector.Length(q0, q1, q2, q3);
        q0 /= r;
        q1 /= r;
        q2 /= r;
        q3 /= r;

        return new double[]{
            q0, q1, q2, q3
        };
    }
}
