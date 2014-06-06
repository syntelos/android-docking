/*
 * Copyright (C) 2014, John Pritchard
 */
package com.johnpritchard.llg;

import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.view.SurfaceHolder;

/**
 * 
 */
public final class SensorOrientation
    extends ObjectLog
    implements android.hardware.SensorEventListener,
               InterfaceRotation
{

    private static SensorOrientation Instance;

    public final static SensorOrientation Instance(ObjectActivity context){
        if (null == Instance){
            Instance = new SensorOrientation(context);
        }
        return Instance;
    }
    public final static SensorOrientation Instance(){
        if (null == Instance){
            throw new RuntimeException();
        }
        else {
            return Instance;
        }
    }



    private Sensor accelerometer, magnetometer;

    private double[] gravity = {0,0,0};

    private double[] magnetic = {0,0,0};

    private boolean updateGravity, updateMagnetic;
    /**
     * 3 DOF Earth - centric orientation rotation matrix
     */
    private float[] rotation = fv3.math.Matrix.Identity();

    private double rotationX, rotationY, rotationZ, rotationEL, rotationAZ;

    private int failure;

    private boolean down = false;

    private boolean plumb = false;

    private Listener[] listeners;


    private SensorOrientation(ObjectActivity context){
        super(context);
    }


    /**
     * The argument must not be a member of the list of registered
     * listeners.
     */
    public void register(Listener li){
        if (null == li)
            return;
        else if (null == listeners)
            listeners = new Listener[]{li};
        else {
            Listener[] source = listeners;
            int len = source.length;
            Listener[] copier = new Listener[len+1];
            System.arraycopy(source,0,copier,0,len);
            copier[len] = li;
            listeners = copier;
        }
        if (plumb){
            li.update(this);
        }
    }
    /**
     * The argument must be a member of the list of registered
     * listeners.
     */
    public void unregister(Listener li){
        if (null == li)
            return;
        else if (null == listeners)
            return;
        else {
            Listener[] source = listeners;
            int len = source.length;
            if (1 == len){

                listeners = null;
            }
            else {
                Listener[] copier = new Listener[len-1];
                for (int sourcex = 0, copierx = 0; sourcex < len; sourcex++){
                    if (li != source[sourcex]){

                        copier[copierx++] = source[sourcex];
                    }
                }
                listeners = copier;
            }
        }
    }
    public float[] rotationMatrix(){

        return rotation;
    }
    public double rotationX(){

        return rotationX;
    }
    public double rotationY(){

        return rotationY;
    }
    public double rotationZ(){

        return rotationZ;
    }
    public double rotationEL(){

        return rotationEL;
    }
    public double rotationAZ(){

        return rotationAZ;
    }
    /**
     * Is up, or is expected to be up
     */
    public boolean isOpen(){

        return (null != accelerometer && null != magnetometer);
    }
    /**
     * Is not expected to be up
     */
    public boolean isClosed(){

        return down;
    }
    /**
     * Is expected to be operating normally
     */
    public boolean isUp(){

        return plumb;
    }
    /**
     * 
     */
    public void open(){

        down = false;

        final SensorManager sm = sensorManager();

        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (null != accelerometer && null != magnetometer){

            sm.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_UI);
            sm.registerListener(this,magnetometer,SensorManager.SENSOR_DELAY_UI);

            info("SensorOrientation [A,M]");
        }
        else {
            down = true;
            accelerometer = null;
            magnetometer = null;

            error("SensorOrientation failed to open required sensor devices.");
        }
    }
    public void close(){

        if (null != accelerometer || null != magnetometer){
            try {
                sensorUnregister(this);
            }
            finally {
                accelerometer = null;
                magnetometer = null;
            }
        }
    }
    public void onSensorChanged(SensorEvent event){

        if (accelerometer == event.sensor){

            gravity[0] = event.values[0];
            gravity[1] = event.values[1];
            gravity[2] = event.values[2];

            updateGravity = true;
        }
        else if (magnetometer == event.sensor){

            magnetic[0] = event.values[0];
            magnetic[1] = event.values[1];
            magnetic[2] = event.values[2];

            updateMagnetic = true;
        }
        else {
            warn("Unrecognized event from sensor "+event.sensor.getName());
        }

        if (updateGravity && updateMagnetic){

            if (updateRotation()){

                failure = 0;

                dispatchRotation();

                //log('R',rX,rY,rZ);

                //log('R',rotation);
            }
            else if (failure < 20){

                if (0 == failure){

                    warn("SensorOrientation problem updating orientation basis from gravity and magnetic fields.");
                }
                failure += 1;
            }
            else {
                error("SensorOrientation failed to update orientation basis from gravity and magnetic fields.");

                down = true;

                close();
            }
        }
        else if (updateGravity){
            log('A',gravity[0],gravity[1],gravity[2]);
        }
        else if (updateMagnetic){
            log('M',magnetic[0],magnetic[1],magnetic[2]);
        }
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }
    /**
     * This function is derived from a number of days study into
     * rotations and the work represented in {@link
     * android.hardware.SensorManager} and {@link
     * android.hardware.SensorEvent}.
     */
    private boolean updateRotation(){
        /*
         * Acceleration (due to gravity) measured in m/s in three
         * dimensions.
         */
        double[] A = this.gravity.clone();
        /*
         * Local magnetic field measured in micro-tesla in three
         * dimensions.
         */
        double[] M = this.magnetic.clone();
        /*
         * Construct an orthogonal from A and M.
         */
        double[] B = new double[]{
            (A[1] * M[2] - A[2] * M[1]),
            (A[2] * M[0] - A[0] * M[2]),
            (A[0] * M[1] - A[1] * M[0])
        };

        double magB = Math.sqrt(B[0]*B[0] + B[1]*B[1] + B[2]*B[2]);

        if (0.1 < magB){
            /*
             * Normalize A
             */
            double magA = Math.sqrt(A[0]*A[0] + A[1]*A[1] + A[2]*A[2]);
            A[0] /= magA;
            A[1] /= magA;
            A[2] /= magA;
            /*
             * Normalize B
             */
            B[0] /= magB;
            B[1] /= magB;
            B[2] /= magB;

            /*
             * C is a third vector orthogonal to A and B
             */
            double[] C = new double[]{
                (A[1] * B[2] - A[2] * B[1]),
                (A[2] * B[0] - A[0] * B[2]),
                (A[0] * B[1] - A[1] * B[0])
            };

            this.rotation = visualizeBasisTransposed(A,B,C);

            this.rotationX = Math.asin(A[1]);
            this.rotationY = Math.atan2(A[0], A[2]);
            this.rotationZ = Math.atan2(+B[1], C[1]);

            this.rotationEL = this.rotationY - PI_D2;
            this.rotationAZ = this.rotationZ - PI_D2;

            return true;
        }
        else {
            return false;
        }
    }
    private final static double PI_D2 = Math.PI / 2.0;

    private final static float[] visualizeBasis(double[] A, double[] B, double[] C){
        /*
         *   M 00 [0] = Bx   M 01 [1] = By   M 02 [2] = Bz
         *   M 10 [4] = Cx   M 11 [5] = Cy   M 12 [6] = Cz
         *   M 20 [8] = Ax   M 21 [9] = Ay   M 22[10] = Az
         */
        float[] rotation = fv3.math.Matrix.Identity();
        {
            rotation[ 0] = (float)B[0];
            rotation[ 1] = (float)B[1];
            rotation[ 2] = (float)B[2];

            rotation[ 4] = (float)C[0];
            rotation[ 5] = (float)C[1];
            rotation[ 6] = (float)C[2];

            rotation[ 8] = (float)A[0];
            rotation[ 9] = (float)A[1];
            rotation[10] = (float)A[2];
        }
        return rotation;
    }
    private final static float[] visualizeBasisTransposed(double[] A, double[] B, double[] C){
        /*
         *   M 00 [0] = Bx   M 01 [1] = Cx   M 02 [2] = Ax
         *   M 10 [4] = By   M 11 [5] = Cy   M 12 [6] = Ay
         *   M 20 [8] = Bz   M 21 [9] = Cz   M 22[10] = Az
         */
        float[] rotation = fv3.math.Matrix.Identity();
        {
            rotation[ 0] = (float)B[0];
            rotation[ 1] = (float)C[0];
            rotation[ 2] = (float)A[0];

            rotation[ 4] = (float)B[1];
            rotation[ 5] = (float)C[1];
            rotation[ 6] = (float)A[1];

            rotation[ 8] = (float)B[2];
            rotation[ 9] = (float)C[2];
            rotation[10] = (float)A[2];
        }
        return rotation;
    }
    private final static float[] visualizeAngle(double[] A, double[] B, double[] C){
        /*
         * angle axis = acos ( axis X projection)
         *            = acos ( 1.0 * X + 0.0 * Y + 0.0 * Z)
         *            = acos ( 0.0 * X + 1.0 * Y + 0.0 * Z)
         *            = acos ( 0.0 * X + 0.0 * Y + 1.0 * Z)
         */
        double rX = Math.acos(B[0]);
        double rY = Math.acos(C[1]);
        double rZ = Math.acos(A[2]);

        float[] rotation = fv3.math.Matrix.Identity();
        {
            final double cx = Math.cos(rX);
            final double sx = Math.sin(rX);

            final double cy = Math.cos(rY);
            final double sy = Math.sin(rY);

            final double cz = Math.cos(rZ);
            final double sz = Math.sin(rZ);

            rotation[ 0] = (float)( cy * cz);
            rotation[ 1] = (float)(-cy * sz);
            rotation[ 2] = (float)( sy);

            rotation[ 4] = (float)( (sx * sy * cz)+(cx * sz));
            rotation[ 5] = (float)(-(sx * sy * sz) + (cx * cz));
            rotation[ 6] = (float)(-sx * cy);

            rotation[ 8] = (float)(-(cx * sy * cz) + (sx * sz));
            rotation[ 9] = (float)( (cx * sy * sz) + (sx * cz));
            rotation[10] = (float)( cx * cy);
        }
        return rotation;
    }
    private final static float[] visualizeAngle(double rX, double rY, double rZ){

        float[] rotation = fv3.math.Matrix.Identity();
        {
            final double cx = Math.cos(rX);
            final double sx = Math.sin(rX);

            final double cy = Math.cos(rY);
            final double sy = Math.sin(rY);

            final double cz = Math.cos(rZ);
            final double sz = Math.sin(rZ);

            rotation[ 0] = (float)( cy * cz);
            rotation[ 1] = (float)(-cy * sz);
            rotation[ 2] = (float)( sy);

            rotation[ 4] = (float)( (sx * sy * cz)+(cx * sz));
            rotation[ 5] = (float)(-(sx * sy * sz) + (cx * cz));
            rotation[ 6] = (float)(-sx * cy);

            rotation[ 8] = (float)(-(cx * sy * cz) + (sx * sz));
            rotation[ 9] = (float)( (cx * sy * sz) + (sx * cz));
            rotation[10] = (float)( cx * cy);
        }
        return rotation;
    }
    private void dispatchRotation(){
        plumb = true;

        Listener[] li = this.listeners;
        if (null != li){
            for (int cc = 0, count = li.length; cc < count; cc++){

                li[cc].update(this);
            }
        }
    }
    private void log(char z, double a, double b, double c)
    {
        StringBuilder msg = new StringBuilder();

        msg.append("SensorOrientation ");
        msg.append(z);
        msg.append(' ');
        msg.append(String.format("% 2.7f",a));
        msg.append(' ');
        msg.append(String.format("% 2.7f",b));
        msg.append(' ');
        msg.append(String.format("% 2.7f",c));

        info(msg.toString());
    }
    private void log(char z, double a, double b, double c, double d)
    {
        StringBuilder msg = new StringBuilder();

        msg.append("SensorOrientation ");
        msg.append(z);
        msg.append(' ');
        msg.append(String.format("% 2.7f",a));
        msg.append(' ');
        msg.append(String.format("% 2.7f",b));
        msg.append(' ');
        msg.append(String.format("% 2.7f",c));
        msg.append(' ');
        msg.append(String.format("% 2.7f",d));

        info(msg.toString());
    }
    private void log(char z, double a, double b, double c, double d, double e)
    {
        StringBuilder msg = new StringBuilder();

        msg.append("SensorOrientation ");
        msg.append(z);
        msg.append(' ');
        msg.append(String.format("% 2.7f",a));
        msg.append(' ');
        msg.append(String.format("% 2.7f",b));
        msg.append(' ');
        msg.append(String.format("% 2.7f",c));
        msg.append(' ');
        msg.append(String.format("% 2.7f",d));
        msg.append(' ');
        msg.append(String.format("% 2.7f",e));

        info(msg.toString());
    }
    private void log(char z, double a, double b, double c, 
                     double d, double e, double f)
    {
        StringBuilder msg = new StringBuilder();

        msg.append("SensorOrientation ");
        msg.append(z);
        msg.append(' ');
        msg.append(String.format("% 2.7f",a));
        msg.append(' ');
        msg.append(String.format("% 2.7f",b));
        msg.append(' ');
        msg.append(String.format("% 2.7f",c));
        msg.append(' ');
        msg.append(String.format("% 2.7f",d));
        msg.append(' ');
        msg.append(String.format("% 2.7f",e));
        msg.append(' ');
        msg.append(String.format("% 2.7f",f));

        info(msg.toString());
    }
    private void log(char z, double a, double b, double c, 
                     double d, double e, double f, double g)
    {
        StringBuilder msg = new StringBuilder();

        msg.append("SensorOrientation ");
        msg.append(z);
        msg.append(' ');
        msg.append(String.format("% 2.7f",a));
        msg.append(' ');
        msg.append(String.format("% 2.7f",b));
        msg.append(' ');
        msg.append(String.format("% 2.7f",c));
        msg.append(' ');
        msg.append(String.format("% 2.7f",d));
        msg.append(' ');
        msg.append(String.format("% 2.7f",e));
        msg.append(' ');
        msg.append(String.format("% 2.7f",f));
        msg.append(' ');
        msg.append(String.format("% 2.7f",g));

        info(msg.toString());
    }
    private void log(char z, double a, double b, double c, 
                     double d, double e, double f, double g, double h)
    {
        StringBuilder msg = new StringBuilder();

        msg.append("SensorOrientation ");
        msg.append(z);
        msg.append(' ');
        msg.append(String.format("% 2.4f",a));
        msg.append(' ');
        msg.append(String.format("% 2.4f",b));
        msg.append(' ');
        msg.append(String.format("% 2.4f",c));
        msg.append(' ');
        msg.append(String.format("% 2.4f",d));
        msg.append(' ');
        msg.append(String.format("% 2.4f",e));
        msg.append(' ');
        msg.append(String.format("% 2.4f",f));
        msg.append(' ');
        msg.append(String.format("% 2.4f",g));
        msg.append(' ');
        msg.append(String.format("% 2.4f",h));

        info(msg.toString());
    }
    private void log(char x, double a, double b, double c, 
                     char y, double d, double e, double f, 
                     char z, double g, double h, double i)
    {
        StringBuilder msg = new StringBuilder();

        msg.append("SensorOrientation ");
        msg.append(x);
        msg.append(' ');
        msg.append(String.format("% 2.4f",a));
        msg.append(' ');
        msg.append(String.format("% 2.4f",b));
        msg.append(' ');
        msg.append(String.format("% 2.4f",c));
        msg.append(' ');
        msg.append(y);
        msg.append(' ');
        msg.append(String.format("% 2.4f",d));
        msg.append(' ');
        msg.append(String.format("% 2.4f",e));
        msg.append(' ');
        msg.append(String.format("% 2.4f",f));
        msg.append(' ');
        msg.append(z);
        msg.append(' ');
        msg.append(String.format("% 2.4f",g));
        msg.append(' ');
        msg.append(String.format("% 2.4f",h));
        msg.append(' ');
        msg.append(String.format("% 2.4f",i));

        info(msg.toString());
    }
    private void log(char d, double[] m)
    {
        StringBuilder msg = new StringBuilder();

        msg.append("SensorOrientation ");
        msg.append(d);
        msg.append(' ');
        msg.append(String.format("% 2.4f",m[0]));
        msg.append(' ');
        msg.append(String.format("% 2.4f",m[1]));
        msg.append(' ');
        msg.append(String.format("% 2.4f",m[2]));
        msg.append(' ');
        msg.append(String.format("% 2.4f",m[4]));
        msg.append(' ');
        msg.append(String.format("% 2.4f",m[5]));
        msg.append(' ');
        msg.append(String.format("% 2.4f",m[6]));
        msg.append(' ');
        msg.append(String.format("% 2.4f",m[8]));
        msg.append(' ');
        msg.append(String.format("% 2.4f",m[9]));
        msg.append(' ');
        msg.append(String.format("% 2.4f",m[10]));

        info(msg.toString());
    }

    /*
     * Multiply by nano seconds for seconds
     */
    private static final double NS2S = (1.0f / 1000000000.0f);

    private final static double PI_PLUS = Math.PI;
    private final static double PI_MINUS = -Math.PI;
    /**
     * Clamp the difference between two orientation values for a
     * correct radial difference value.
     * 
     * For example if value A is PI minus 0.1, and value B is the
     * product of a positive rotation of 0.2 radians, then the
     * difference of 3.0414 and -3.2414 should be 0.2 radians.  This
     * correct value can be obtained by clamping the subtraction of B
     * from A using this function.
     * <pre>
     * C = ClampR(A - B);
     * </pre>
     */
    public final static double ClampR(double r){
        if (r <= PI_MINUS){
            do {
                r += PI_PLUS;
            }
            while (r <= PI_MINUS);

            return -(PI_PLUS + r);
        }
        else if (r >= PI_PLUS){
            do {
                r -= PI_PLUS;
            }
            while (r >= PI_PLUS);

            return (PI_PLUS - r);
        }
        else {
            return r;
        }
    }
}
