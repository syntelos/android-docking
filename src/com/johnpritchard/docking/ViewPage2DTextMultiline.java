/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import android.graphics.RectF;

import java.util.StringTokenizer;

/**
 * 
 */
public class ViewPage2DTextMultiline
    extends ViewPage2DTextLabel
{
    public final static int DefaultLineWidth = 20;


    public final int line_width;


    public ViewPage2DTextMultiline(){
        this(null,DefaultLineWidth);
    }
    public ViewPage2DTextMultiline(String text){
        this(text,DefaultLineWidth);
    }
    public ViewPage2DTextMultiline(String text, int lw){
        super();

        if (0 < lw){
            this.line_width = lw;
        }
        else {
            this.line_width = DefaultLineWidth;
        }
        this.setText(text);
    }
    public ViewPage2DTextMultiline(ViewPageOperatorSelection sel, String text){
        this(sel,text,DefaultLineWidth);
    }
    public ViewPage2DTextMultiline(ViewPageOperatorSelection sel, String text, int lw){
        super(sel);

        if (0 < lw){
            this.line_width = lw;
        }
        else {
            this.line_width = DefaultLineWidth;
        }
        this.setText(text);
    }


    // public boolean pageMeasureByGroup(){

    //     return hasSelectionGroup();
    // }
    public ViewPage2DComponentPath setText(String text){
        if (null != text){
            reset();

            String[] lines = null;

            int nl = text.indexOf('\n');
            if (0 < nl){
                final StringTokenizer strtok = new StringTokenizer(text,"\n");
                final int count = strtok.countTokens();
                lines = new String[count];
                for (int cc = 0; cc < count; cc++){
                    lines[cc] = strtok.nextToken();
                }
            }
            else {
                final StringTokenizer strtok = new StringTokenizer(text," ");
                final int count = strtok.countTokens();
                final int end = (count+1);
                final int term = (count-1);

                StringBuilder line = new StringBuilder();

                for (int cc = 0; cc < end; cc++){

                    if (cc < count){

                        final String tok = strtok.nextToken();

                        final int tok_len = tok.length();

                        if (0 != line.length()){

                            line.append(' ');
                        }

                        line.append(tok);

                        if (line.length() > this.line_width){

                            lines = Add(lines,line.toString());

                            line.setLength(0);
                        }
                    }
                    else {

                        if (0 != line.length()){

                            lines = Add(lines,line.toString());
                        }
                    }
                }
            }

            final ViewPage2DPath line = new ViewPage2DPath();

            final int count = lines.length;

            /*
             */
            final ViewPageOperatorSelection selection = this.selection;

            RectF selection_update = null;

            if (null != selection){

                selection_update = new RectF();

                selection.open(1);
            }


            float x = 0.0f, y = TextSize;

            for (int cc = 0; cc < count; cc++){

                String tl = lines[cc];

                this.fill.getTextPath(tl,0,tl.length(),x,y,line);

                y += TextSize;

                this.path.add(line);

                line.reset();
            }
            /*
             */
            if (null != selection){

                selection.update(0,this.bounds());

                selection.close();
            }
        }
        return this;
    }
}
