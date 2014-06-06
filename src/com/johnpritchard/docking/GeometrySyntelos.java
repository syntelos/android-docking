/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.llg;

import static android.opengl.GLES10.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 
 */
public final class GeometrySyntelos
    extends Geometry
{
    public final static GeometrySyntelos Instance = new GeometrySyntelos();

    public final float[] glyph_s0 = {
        -0.5965f, -0.4274f,  0.6794f,
        -0.5921f, -0.4471f,  0.6705f,
        -0.5747f, -0.4671f,  0.6720f,
        -0.5747f, -0.4671f,  0.6720f,
        -0.5551f, -0.4766f,  0.6817f,
        -0.5458f, -0.4693f,  0.6941f,
        -0.5458f, -0.4693f,  0.6941f,
        -0.5510f, -0.4552f,  0.6994f,
        -0.5756f, -0.4260f,  0.6980f,
        -0.5756f, -0.4260f,  0.6980f,
        -0.6004f, -0.3914f,  0.6974f,
        -0.6108f, -0.3659f,  0.7022f,
        -0.6099f, -0.3437f,  0.7141f,
        -0.6099f, -0.3437f,  0.7141f,
        -0.6014f, -0.3261f,  0.7294f,
        -0.5873f, -0.3150f,  0.7455f,
        -0.5689f, -0.3112f,  0.7613f,
        -0.5475f, -0.3152f,  0.7752f,
        -0.5248f, -0.3276f,  0.7856f,
        -0.5248f, -0.3276f,  0.7856f,
        -0.5004f, -0.3510f,  0.7915f,
        -0.4825f, -0.3828f,  0.7879f,
        -0.4758f, -0.4147f,  0.7756f,
        -0.4758f, -0.4147f,  0.7756f,
        -0.4772f, -0.4296f,  0.7666f,
        -0.4772f, -0.4296f,  0.7666f,
        -0.5274f, -0.3999f,  0.7496f,
        -0.5274f, -0.3999f,  0.7496f,
        -0.5411f, -0.3722f,  0.7541f,
        -0.5411f, -0.3722f,  0.7541f,
        -0.5555f, -0.3682f,  0.7455f,
        -0.5636f, -0.3776f,  0.7347f,
        -0.5636f, -0.3776f,  0.7347f,
        -0.5579f, -0.3936f,  0.7306f,
        -0.5579f, -0.3936f,  0.7306f,
        -0.5363f, -0.4185f,  0.7330f,
        -0.5363f, -0.4185f,  0.7330f,
        -0.5073f, -0.4491f,  0.7354f,
        -0.4914f, -0.4733f,  0.7310f,
        -0.4864f, -0.4954f,  0.7197f,
        -0.4864f, -0.4954f,  0.7197f,
        -0.4921f, -0.5142f,  0.7024f,
        -0.5081f, -0.5228f,  0.6845f,
        -0.5315f, -0.5214f,  0.6676f,
        -0.5595f, -0.5103f,  0.6530f,
        -0.5902f, -0.4898f,  0.6418f,
        -0.5902f, -0.4898f,  0.6418f,
        -0.6120f, -0.4679f,  0.6377f,
        -0.6286f, -0.4461f,  0.6371f,
        -0.6400f, -0.4253f,  0.6400f,
        -0.6459f, -0.4064f,  0.6463f,
        -0.6458f, -0.3905f,  0.6561f,
        -0.6458f, -0.3905f,  0.6561f,
        -0.6427f, -0.3809f,  0.6647f,
        -0.6427f, -0.3809f,  0.6647f
    };
    public final int glyph_s0_count = (glyph_s0.length/3);

    public final float[] glyph_y0 = {
        -0.4027f, -0.5659f,  0.7195f,
        -0.4527f, -0.3742f,  0.8094f,
        -0.4527f, -0.3742f,  0.8094f,
        -0.3949f, -0.3945f,  0.8297f,
        -0.3949f, -0.3945f,  0.8297f,
        -0.3653f, -0.5208f,  0.7716f,
        -0.3653f, -0.5208f,  0.7716f,
        -0.2843f, -0.4111f,  0.8661f,
        -0.2843f, -0.4111f,  0.8661f,
        -0.2157f, -0.4086f,  0.8869f,
        -0.2157f, -0.4086f,  0.8869f,
        -0.3736f, -0.6559f,  0.6559f,
        -0.4529f, -0.6219f,  0.6389f,
        -0.4027f, -0.5659f,  0.7195f
    };
    public final int glyph_y0_count = (glyph_y0.length/3);

    public final float[] glyph_n0 = {
        -0.1881f, -0.4046f,  0.8949f,
        -0.1326f, -0.3916f,  0.9105f,
        -0.1234f, -0.4203f,  0.8990f,
        -0.1080f, -0.3912f,  0.9140f,
        -0.0900f, -0.3710f,  0.9243f,
        -0.0667f, -0.3572f,  0.9316f,
        -0.0667f, -0.3572f,  0.9316f,
        -0.0177f, -0.3441f,  0.9388f,
         0.0307f, -0.3557f,  0.9341f,
         0.0750f, -0.3914f,  0.9172f,
         0.0750f, -0.3914f,  0.9172f,
         0.1079f, -0.4393f,  0.8918f,
         0.1079f, -0.4393f,  0.8918f,
         0.1689f, -0.5609f,  0.8105f,
         0.0851f, -0.5991f,  0.7962f,
         0.0429f, -0.4894f,  0.8710f,
         0.0194f, -0.4482f,  0.8937f,
         0.0194f, -0.4482f,  0.8937f,
        -0.0112f, -0.4254f,  0.9049f,
        -0.0491f, -0.4291f,  0.9019f,
        -0.0491f, -0.4291f,  0.9019f,
        -0.0747f, -0.4456f,  0.8921f,
        -0.0888f, -0.4712f,  0.8776f,
        -0.0888f, -0.4712f,  0.8776f,
        -0.0906f, -0.4973f,  0.8628f,
        -0.0819f, -0.5359f,  0.8403f,
        -0.0819f, -0.5359f,  0.8403f,
        -0.0581f, -0.6369f,  0.7688f,
        -0.0581f, -0.6369f,  0.7688f,
        -0.1423f, -0.6441f,  0.7516f,
        -0.1423f, -0.6441f,  0.7516f
    };
    public final int glyph_n0_count = (glyph_n0.length/3);

    public final float[] glyph_t0 = {
         0.1292f, -0.3275f,  0.9360f,
         0.0998f, -0.3516f,  0.9308f,
         0.0998f, -0.3516f,  0.9308f,
         0.0584f, -0.2892f,  0.9555f,
         0.0584f, -0.2892f,  0.9555f,
         0.0833f, -0.2674f,  0.9600f,
         0.0048f, -0.1696f,  0.9855f,
         0.0408f, -0.1281f,  0.9909f,
         0.1310f, -0.2172f,  0.9673f,
         0.1579f, -0.1829f,  0.9704f,
         0.2137f, -0.2369f,  0.9477f,
         0.2137f, -0.2369f,  0.9477f,
         0.1832f, -0.2739f,  0.9441f,
         0.1832f, -0.2739f,  0.9441f,
         0.3210f, -0.4523f,  0.8321f,
         0.2473f, -0.5122f,  0.8225f,
         0.2473f, -0.5122f,  0.8225f,
         0.1292f, -0.3275f,  0.9360f
    };
    public final int glyph_t0_count = (glyph_t0.length/3);

    public final float[] glyph_e0 = {
         0.3699f, -0.2233f,  0.9018f,
         0.4013f, -0.2271f,  0.8873f,
         0.4328f, -0.2076f,  0.8773f,
         0.4594f, -0.1676f,  0.8723f,
         0.4594f, -0.1676f,  0.8723f,
         0.4718f, -0.1163f,  0.8740f,
         0.4654f, -0.0688f,  0.8824f,
         0.4654f, -0.0688f,  0.8824f,
         0.4856f,  0.0278f,  0.8737f,
         0.5128f, -0.0208f,  0.8583f,
         0.5252f, -0.0779f,  0.8474f,
         0.5226f, -0.1403f,  0.8409f,
         0.5044f, -0.2054f,  0.8387f,
         0.5044f, -0.2054f,  0.8387f,
         0.4719f, -0.2566f,  0.8435f,
         0.4345f, -0.2924f,  0.8519f,
         0.3944f, -0.3125f,  0.8642f,
         0.3538f, -0.3158f,  0.8804f,
         0.3152f, -0.3006f,  0.9002f,
         0.3152f, -0.3006f,  0.9002f,
         0.2727f, -0.2597f,  0.9264f,
         0.2411f, -0.2048f,  0.9487f,
         0.2227f, -0.1413f,  0.9646f,
         0.2200f, -0.0757f,  0.9726f,
         0.2346f, -0.0154f,  0.9720f,
         0.2346f, -0.0154f,  0.9720f,
         0.2584f,  0.0316f,  0.9655f,
         0.2961f,  0.0639f,  0.9530f,
         0.3436f,  0.0794f,  0.9358f,
         0.3965f,  0.0763f,  0.9149f,
         0.3965f,  0.0763f,  0.9149f,
         0.4609f,  0.0478f,  0.8862f,
         0.4609f,  0.0478f,  0.8862f
    };
    public final int glyph_e0_count = (glyph_e0.length/3);

    public final float[] glyph_e1 = {
         0.3869f, -0.0001f,  0.9221f,
         0.3521f,  0.0002f,  0.9360f,
         0.3246f, -0.0189f,  0.9457f,
         0.3047f, -0.0574f,  0.9507f,
         0.3047f, -0.0574f,  0.9507f,
         0.2958f, -0.1231f,  0.9473f,
         0.3177f, -0.1807f,  0.9308f,
         0.3177f, -0.1807f,  0.9308f
    };
    public final int glyph_e1_count = (glyph_e1.length/3);

    public final float[] glyph_l0 = {
         0.1561f,  0.1988f,  0.9675f,
         0.1527f,  0.2542f,  0.9550f,
         0.1527f,  0.2542f,  0.9550f,
         0.5631f,  0.1492f,  0.8128f,
         0.5631f,  0.1492f,  0.8128f,
         0.5629f,  0.0564f,  0.8246f
    };
    public final int glyph_l0_count = (glyph_l0.length/3);

    public final float[] glyph_o0 = {
         0.2635f,  0.5391f,  0.7999f,
         0.2897f,  0.5444f,  0.7872f,
         0.3183f,  0.5433f,  0.7768f,
         0.3482f,  0.5364f,  0.7688f,
         0.3786f,  0.5241f,  0.7629f,
         0.4086f,  0.5067f,  0.7591f,
         0.4375f,  0.4847f,  0.7574f,
         0.4645f,  0.4582f,  0.7578f,
         0.4888f,  0.4275f,  0.7605f,
         0.5095f,  0.3925f,  0.7657f,
         0.5095f,  0.3925f,  0.7657f,
         0.5215f,  0.3526f,  0.7769f,
         0.5246f,  0.3169f,  0.7902f,
         0.5191f,  0.2867f,  0.8052f,
         0.5055f,  0.2634f,  0.8216f,
         0.4842f,  0.2488f,  0.8389f,
         0.4554f,  0.2447f,  0.8560f,
         0.4554f,  0.2447f,  0.8560f,
         0.4090f,  0.2531f,  0.8767f,
         0.3606f,  0.2721f,  0.8922f,
         0.3133f,  0.2997f,  0.9011f,
         0.2706f,  0.3335f,  0.9031f,
         0.2363f,  0.3708f,  0.8981f,
         0.2134f,  0.4092f,  0.8871f,
         0.2134f,  0.4092f,  0.8871f,
         0.2019f,  0.4366f,  0.8767f,
         0.1992f,  0.4630f,  0.8637f,
         0.2047f,  0.4874f,  0.8489f,
         0.2176f,  0.5088f,  0.8329f,
         0.2375f,  0.5263f,  0.8165f,
         0.2635f,  0.5391f,  0.7999f
    };
    public final int glyph_o0_count = (glyph_o0.length/3);

    public final float[] glyph_o1 = {
         0.4245f,  0.3270f,  0.8443f,
         0.4528f,  0.3384f,  0.8249f,
         0.4633f,  0.3660f,  0.8071f,
         0.4549f,  0.4040f,  0.7937f,
         0.4549f,  0.4040f,  0.7937f,
         0.4341f,  0.4345f,  0.7891f,
         0.4071f,  0.4592f,  0.7895f,
         0.3765f,  0.4769f,  0.7943f,
         0.3450f,  0.4861f,  0.8029f,
         0.3158f,  0.4851f,  0.8155f,
         0.3158f,  0.4851f,  0.8155f,
         0.2887f,  0.4702f,  0.8340f,
         0.2776f,  0.4450f,  0.8514f,
         0.2855f,  0.4132f,  0.8647f,
         0.2855f,  0.4132f,  0.8647f,
         0.3091f,  0.3803f,  0.8717f,
         0.3444f,  0.3522f,  0.8703f,
         0.3849f,  0.3332f,  0.8607f,
         0.4245f,  0.3270f,  0.8443f
    };
    public final int glyph_o1_count = (glyph_o1.length/3);

    public final float[] glyph_s1 = {
         0.2649f,  0.6134f,  0.7441f,
         0.2742f,  0.6258f,  0.7302f,
         0.2601f,  0.6432f,  0.7202f,
         0.2601f,  0.6432f,  0.7202f,
         0.2325f,  0.6561f,  0.7180f,
         0.2098f,  0.6554f,  0.7255f,
         0.2098f,  0.6554f,  0.7255f,
         0.2056f,  0.6502f,  0.7314f,
         0.2101f,  0.6401f,  0.7390f,
         0.2249f,  0.6212f,  0.7507f,
         0.2249f,  0.6212f,  0.7507f,
         0.2393f,  0.5976f,  0.7653f,
         0.2438f,  0.5800f,  0.7773f,
         0.2380f,  0.5672f,  0.7884f,
         0.2219f,  0.5576f,  0.7999f,
         0.2219f,  0.5576f,  0.7999f,
         0.1844f,  0.5491f,  0.8152f,
         0.1417f,  0.5499f,  0.8231f,
         0.1003f,  0.5594f,  0.8228f,
         0.0668f,  0.5764f,  0.8144f,
         0.0668f,  0.5764f,  0.8144f,
         0.0465f,  0.5980f,  0.8002f,
         0.0438f,  0.6214f,  0.7823f,
         0.0580f,  0.6416f,  0.7648f,
         0.0580f,  0.6416f,  0.7648f,
         0.0710f,  0.6501f,  0.7565f,
         0.0710f,  0.6501f,  0.7565f,
         0.1221f,  0.6217f,  0.7737f,
         0.1151f,  0.6115f,  0.7829f,
         0.1228f,  0.6011f,  0.7897f,
         0.1228f,  0.6011f,  0.7897f,
         0.1437f,  0.5941f,  0.7915f,
         0.1642f,  0.5974f,  0.7849f,
         0.1642f,  0.5974f,  0.7849f,
         0.1677f,  0.6085f,  0.7756f,
         0.1677f,  0.6085f,  0.7756f,
         0.1641f,  0.6147f,  0.7715f,
         0.1515f,  0.6297f,  0.7619f,
         0.1515f,  0.6297f,  0.7619f,
         0.1343f,  0.6494f,  0.7485f,
         0.1267f,  0.6639f,  0.7370f,
         0.1281f,  0.6752f,  0.7265f,
         0.1380f,  0.6846f,  0.7158f,
         0.1380f,  0.6846f,  0.7158f,
         0.1585f,  0.6922f,  0.7041f,
         0.1848f,  0.6941f,  0.6957f,
         0.2148f,  0.6906f,  0.6906f,
         0.2467f,  0.6818f,  0.6887f,
         0.2788f,  0.6679f,  0.6901f,
         0.3096f,  0.6487f,  0.6952f,
         0.3096f,  0.6487f,  0.6952f,
         0.3364f,  0.6219f,  0.7072f,
         0.3498f,  0.5976f,  0.7215f,
         0.3490f,  0.5783f,  0.7374f,
         0.3329f,  0.5664f,  0.7539f,
         0.3329f,  0.5664f,  0.7539f,
         0.3181f,  0.5629f,  0.7628f,
         0.3181f,  0.5629f,  0.7628f,
         0.2649f,  0.6134f,  0.7441f
    };
    public final int glyph_s1_count = (glyph_s1.length/3);


    private final FloatBuffer b_glyph_s0;

    private final FloatBuffer b_glyph_y0;

    private final FloatBuffer b_glyph_n0;

    private final FloatBuffer b_glyph_t0;

    private final FloatBuffer b_glyph_e0;

    private final FloatBuffer b_glyph_e1;

    private final FloatBuffer b_glyph_l0;

    private final FloatBuffer b_glyph_o0;

    private final FloatBuffer b_glyph_o1;

    private final FloatBuffer b_glyph_s1;



    private GeometrySyntelos(){
        super();
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.glyph_s0.length * bpf);
            ib.order(nativeOrder);
            this.b_glyph_s0 = ib.asFloatBuffer();
            this.b_glyph_s0.put(this.glyph_s0);
            this.b_glyph_s0.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.glyph_y0.length * bpf);
            ib.order(nativeOrder);
            this.b_glyph_y0 = ib.asFloatBuffer();
            this.b_glyph_y0.put(this.glyph_y0);
            this.b_glyph_y0.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.glyph_n0.length * bpf);
            ib.order(nativeOrder);
            this.b_glyph_n0 = ib.asFloatBuffer();
            this.b_glyph_n0.put(this.glyph_n0);
            this.b_glyph_n0.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.glyph_t0.length * bpf);
            ib.order(nativeOrder);
            this.b_glyph_t0 = ib.asFloatBuffer();
            this.b_glyph_t0.put(this.glyph_t0);
            this.b_glyph_t0.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.glyph_e0.length * bpf);
            ib.order(nativeOrder);
            this.b_glyph_e0 = ib.asFloatBuffer();
            this.b_glyph_e0.put(this.glyph_e0);
            this.b_glyph_e0.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.glyph_e1.length * bpf);
            ib.order(nativeOrder);
            this.b_glyph_e1 = ib.asFloatBuffer();
            this.b_glyph_e1.put(this.glyph_e1);
            this.b_glyph_e1.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.glyph_l0.length * bpf);
            ib.order(nativeOrder);
            this.b_glyph_l0 = ib.asFloatBuffer();
            this.b_glyph_l0.put(this.glyph_l0);
            this.b_glyph_l0.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.glyph_o0.length * bpf);
            ib.order(nativeOrder);
            this.b_glyph_o0 = ib.asFloatBuffer();
            this.b_glyph_o0.put(this.glyph_o0);
            this.b_glyph_o0.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.glyph_o1.length * bpf);
            ib.order(nativeOrder);
            this.b_glyph_o1 = ib.asFloatBuffer();
            this.b_glyph_o1.put(this.glyph_o1);
            this.b_glyph_o1.position(0);
        }
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(this.glyph_s1.length * bpf);
            ib.order(nativeOrder);
            this.b_glyph_s1 = ib.asFloatBuffer();
            this.b_glyph_s1.put(this.glyph_s1);
            this.b_glyph_s1.position(0);
        }
    }


    public void draw(){

        glPushMatrix();

        glRotatef(Rotation(),0.0f,1.0f,0.0f);

        glEnableClientState(GL_VERTEX_ARRAY);

        glVertexPointer(3,GL_FLOAT,stride,this.b_glyph_s0);
        glDrawArrays(GL_LINE_LOOP,0,this.glyph_s0_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_glyph_y0);
        glDrawArrays(GL_LINE_LOOP,0,this.glyph_y0_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_glyph_n0);
        glDrawArrays(GL_LINE_LOOP,0,this.glyph_n0_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_glyph_t0);
        glDrawArrays(GL_LINE_LOOP,0,this.glyph_t0_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_glyph_e0);
        glDrawArrays(GL_LINE_LOOP,0,this.glyph_e0_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_glyph_e1);
        glDrawArrays(GL_LINE_LOOP,0,this.glyph_e1_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_glyph_l0);
        glDrawArrays(GL_LINE_LOOP,0,this.glyph_l0_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_glyph_o0);
        glDrawArrays(GL_LINE_LOOP,0,this.glyph_o0_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_glyph_o1);
        glDrawArrays(GL_LINE_LOOP,0,this.glyph_o1_count);

        glVertexPointer(3,GL_FLOAT,stride,this.b_glyph_s1);
        glDrawArrays(GL_LINE_LOOP,0,this.glyph_s1_count);

        glPopMatrix();
    }

    public final static float Rotation(){
        /*
         * wall clock data visualization
         */
        double time = (System.currentTimeMillis() & 0xFFFL);
        time /= 4095.0;
        time *= 360.0;
        return (float)time;
    }
}
