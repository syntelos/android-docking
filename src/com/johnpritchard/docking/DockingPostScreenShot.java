/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Bitmap;
import static android.opengl.GLES10.*;
import android.os.Environment;
import android.view.Surface;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * 
 */
public class DockingPostScreenShot
    extends ObjectLog
    implements Runnable
{
    protected final static java.nio.ByteOrder nativeOrder = java.nio.ByteOrder.nativeOrder();

    protected final static int RGB_565_BYTES_CHANNELS = 3;
    protected final static int RGB_565_BYTES_FORMAT = 2;
    protected final static int RGB_565_BYTES = (RGB_565_BYTES_CHANNELS * RGB_565_BYTES_FORMAT);
    /*
     * I can't find these files from my desktop, so I can't expect
     * anyone else to -- perhaps MediaScannerConnection is the
     * solution set, but looks incomplete from here. 
     */
    protected final static boolean UseInternal = false;


    private final View3D view;
    private final View3DRenderer renderer;
    private final int width, height, size;
    private final String session, filename;
    private final File externalDir, externalFile;
    private final boolean external;
    private final boolean valid;

    public DockingPostScreenShot(View3D view){
        super();
        if (null != view){

            this.view = view;
            this.renderer = view.renderer;

            this.width = renderer.width;
            this.height = renderer.height;

            this.size = (RGB_565_BYTES * width * height);

            this.session = DockingCraftStateVector.Instance.identifier;

            this.valid = (null != this.session && 0 < this.session.length());

            this.external = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

            if (valid){

                if (external){

                    this.externalDir = Docking.ExternalDirectory3D(Environment.DIRECTORY_PICTURES);

                    int num = 1;

                    String frame = String.format("%03d",num++);

                    String filename = "Docking-"+session+'-'+frame+".png";

                    if (externalDir.mkdirs()){
                        this.filename = filename;
                        this.externalFile = new File(externalDir,filename);
                    }
                    else {

                        File file = new File(externalDir,filename);
                        {

                            while (file.exists()){

                                frame = String.format("%03d",num++);

                                filename = "Docking-"+session+'-'+frame+".png";

                                file = new File(externalDir,filename);
                            }
                        }
                        this.filename = filename;
                        this.externalFile = file;
                    }
                }
                else {
                    this.filename = "Docking-"+session+".png";
                    this.externalDir = null;
                    this.externalFile = null;
                }
            }
            else {
                this.filename = null;
                this.externalDir = null;
                this.externalFile = null;
            }

        }
        else {
            throw new IllegalArgumentException();
        }
    }
    /*
     * image flip about the horizontal (because ogl fb origin is lower-left)
     */
    protected Bitmap flip(Bitmap image){

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
    protected Bitmap screenshot()
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

    public void run(){

        if (valid){
            try {
                /*
                 * Take the screenshot if there's a stream to write
                 */
                FileOutputStream fout = null;

                if (external){

                    fout = new FileOutputStream(this.externalFile);
                }
                else if (UseInternal){

                    fout = Docking.InternalFile3D(this.filename);
                }
                else {
                    throw new FileNotFoundException();
                }

                final Bitmap raw = screenshot();

                try {
                    raw.compress(Bitmap.CompressFormat.PNG,100,fout);

                    fout.flush();
                }
                finally {
                    fout.close();
                }
                /*
                 * Report
                 */
                Docking.Toast3D("Screenshot "+this.filename);
            }
            catch (FileNotFoundException exc){

                Docking.Toast3D("Screenshot storage error");

                error("post",exc);
            }
            catch (Throwable t){

                Docking.Toast3D("Screenshot internal error");

                error("post",t);
            }
        }
        else {
            error("missing identifier");
        }
    }

}
