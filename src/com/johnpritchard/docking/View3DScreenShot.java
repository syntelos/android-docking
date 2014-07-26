/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Bitmap;
import static android.opengl.GLES10.*;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * External storage processor
 */
public class View3DScreenShot
    extends ObjectLog
{
    protected final static java.nio.ByteOrder nativeOrder = java.nio.ByteOrder.nativeOrder();

    protected final static int RGB_565_BYTES_CHANNELS = 3;
    protected final static int RGB_565_BYTES_FORMAT = 2;
    protected final static int RGB_565_BYTES = (RGB_565_BYTES_CHANNELS * RGB_565_BYTES_FORMAT);


    protected final View3D view;

    protected final View3DRenderer renderer;

    protected final int width, height, size;

    protected final File dir;

    protected final ByteBuffer buffer;

    protected final Bitmap image;

    private int series = 1;

    private long local = 0L;


    public View3DScreenShot(View3D view){
        super();
        if (null != view){

            this.view = view;
            this.renderer = view.renderer;

            this.width = renderer.width;
            this.height = renderer.height;

            this.size = (RGB_565_BYTES * width * height);

            this.dir = Docking.ExternalDirectory3D(Environment.DIRECTORY_PICTURES);

            if (null != dir){

                this.dir.mkdirs();

                this.buffer = ByteBuffer.allocateDirect(size);
                {
                    buffer.order(nativeOrder);
                }
                this.image = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }


    /**
     * Capture and invert OGL FB from non-OGL thread
     * @see save()
     */
    public final Bitmap screenshot_ext()
        throws InterruptedException
    {
        buffer.position(0);

        if (renderer.screenshot_ext(GL_RGB,GL_UNSIGNED_SHORT_5_6_5,buffer)){

            buffer.position(0);

            image.copyPixelsFromBuffer(buffer);

            return flip();
        }
        else {

            throw new IllegalStateException("timing");
        }
    }
    /**
     * Capture and invert OGL FB from OGL thread
     * @see save()
     */
    public final Bitmap screenshot_loc()
    {
        buffer.position(0);

        glReadPixels(0,0,width,height,GL_RGB,GL_UNSIGNED_SHORT_5_6_5,buffer);

        buffer.position(0);

        image.copyPixelsFromBuffer(buffer);

        return flip();
    }
    /**
     * Movie snap with interframe period
     */
    public final boolean frame(long period)
    {
        final long next = (local + period);
        final long time = DockingGameClock.uptimeMillis();

        if (next <= time){
            local = time;

            screenshot_loc();
            save();

            return true;
        }
        else {
            return false;
        }
    }
    /**
     * Image flip about the horizontal for OGL fb called from {@link
     * #screenshot(android.graphics.Bitmap) screenshot}
     */
    protected final Bitmap flip(){

        final int count = width*height;
        
        final int[] pix = new int[count];
        {
            image.getPixels(pix,0,width,0,0,width,height);
        }
        {
            final int[] swap = new int[width];
            final int term = (count>>1);
            int head = 0, tail = (count-width);

            for (; term < tail; head += width, tail -= width)
            {
                System.arraycopy(pix,head,swap,0,width);  // copy head to swap
                System.arraycopy(pix,tail,pix,head,width);// copy tail to head
                System.arraycopy(swap,0,pix,tail,width);  // copy swap to tail
            }
        }
        {
            image.setPixels(pix,0,width,0,0,width,height);
        }
        return image;
    }
    public final void save(){
        final File file = next();
        try {
            FileOutputStream fout = new FileOutputStream(file);
            try {
                image.compress(Bitmap.CompressFormat.PNG,100,fout);

                fout.flush();
            }
            finally {
                fout.close();
            }
        }
        catch (IOException exc){

            error(file.getPath(),exc);
        }
    }
    protected final File next(){

        final String session = DockingCraftStateVector.Instance.identifier;

        String filename = String.format("Docking-%s-%04d.png",session,this.series++);

        File file = new File(this.dir,filename);
        {
            while (file.exists()){

                filename = String.format("Docking-%s-%04d.png",session,this.series++);

                file = new File(this.dir,filename);
            }
        }
        return file;
    }

}
