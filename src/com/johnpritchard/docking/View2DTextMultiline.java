/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import java.util.StringTokenizer;

/**
 * 
 */
public class View2DTextMultiline
    extends View2DTextLabel
{
    public final static int DefaultLineWidth = 20;


    public final int line_width;


    public View2DTextMultiline(){
        this(null,DefaultLineWidth);
    }
    public View2DTextMultiline(String text){
        this(text,DefaultLineWidth);
    }
    public View2DTextMultiline(String text, int lw){
        super();

        if (0 < lw){
            this.line_width = lw;
        }
        else {
            this.line_width = DefaultLineWidth;
        }
        this.setText(text);
    }


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

                        if (line.length()+tok_len > this.line_width){

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

            float x = 0.0f, y = TextSize;

            for (int cc = 0; cc < count; cc++){

                String tl = lines[cc];

                this.fill.getTextPath(tl,0,tl.length(),x,y,line);

                y += TextSize;

                this.path.add(line);

                line.reset();
            }
        }
        return this;
    }


    public final static String[] Add(String[] list, String item){

        if (null == item){

            return list;
        }
        else if (null == list){

            return new String[]{item};
        }
        else {
            int len = list.length;
            String[] copier = new String[len+1];
            System.arraycopy(list,0,copier,0,len);
            copier[len] = item;
            return copier;
        }
    }
}
