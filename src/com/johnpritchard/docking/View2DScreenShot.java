/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * External storage processor
 */
public class View2DScreenShot
    extends ObjectLog
{

    protected final static int RGB_565_BYTES_CHANNELS = 3;
    protected final static int RGB_565_BYTES_FORMAT = 2;
    protected final static int RGB_565_BYTES = (RGB_565_BYTES_CHANNELS * RGB_565_BYTES_FORMAT);


    protected final View2D view;

    protected final int width, height, size;

    protected final File dir;

    private int series = 1;


    public View2DScreenShot(View2D view){
        super();
        if (null != view){

            this.view = view;

            this.width = view.width;
            this.height = view.height;

            this.size = (RGB_565_BYTES * width * height);

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){

                this.dir = Docking.ExternalDirectory2D(Environment.DIRECTORY_PICTURES);

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


    public Bitmap create(){

        return Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
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
