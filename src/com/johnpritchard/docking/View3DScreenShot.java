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

    private int series = 1;


    public View3DScreenShot(View3D view){
        super();
        if (null != view){

            this.view = view;
            this.renderer = view.renderer;

            this.width = renderer.width;
            this.height = renderer.height;

            this.size = (RGB_565_BYTES * width * height);

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){

                this.dir = Docking.ExternalDirectory3D(Environment.DIRECTORY_PICTURES);

                this.dir.mkdirs();
            }
            else {
                throw new IllegalStateException();
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }


    /**
     * Capture and invert image for normal use
     * @see save(android.graphics.Bitmap)
     */
    public Bitmap screenshot()
        throws InterruptedException
    {

        final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        {
            buffer.order(nativeOrder);
            buffer.position(0);
        }

        if (renderer.screenshot(GL_RGB,GL_UNSIGNED_SHORT_5_6_5,buffer)){

            buffer.position(0);


            final Bitmap image = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);

            image.copyPixelsFromBuffer(buffer);

            return flip(image);
        }
        else {

            throw new IllegalStateException("timing");
        }
    }
    /**
     * Image flip about the horizontal for OGL fb called from {@link
     * #screenshot(android.graphics.Bitmap) screenshot}
     */
    private Bitmap flip(Bitmap image){

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
    public void save(Bitmap raw){
        final File file = next();
        try {
            FileOutputStream fout = new FileOutputStream(file);
            try {
                raw.compress(Bitmap.CompressFormat.PNG,100,fout);

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
    private File next(){

        final String session = DockingCraftStateVector.Instance.identifier;

        String frame = String.format("%04d",this.series++);

        String filename = "Docking-"+session+'-'+frame+".png";

        File file = new File(this.dir,filename);
        {
            while (file.exists()){

                frame = String.format("%04d",this.series++);

                filename = "Docking-"+session+'-'+frame+".png";

                file = new File(this.dir,filename);
            }
        }
        return file;
    }

}
