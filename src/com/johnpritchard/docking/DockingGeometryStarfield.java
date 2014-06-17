/*
 * Copyright (C) 2014 John Pritchard.  All rights reserved.
 */
package com.johnpritchard.docking;

import static android.opengl.GLES10.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 
 */
public class DockingGeometryStarfield
    extends View3DGeometry
{

    private final static float[] XYZ = {
        3.19767f, 48.6191f, 87.3267f,
        2.0531f, 85.851f, 51.2385f,
        2.86488f, -71.6271f, 69.7233f,
        5.57087f, 26.1913f, 96.3482f,
        8.36685f, -15.3398f, 98.4616f,
        2.47381f, -97.5358f, 21.9235f,
        8.46288f, -67.3091f, 73.4701f,
        8.2507f, -69.063f, 71.8486f,
        9.4644f, 80.7958f, 58.1589f,
        14.6584f, 51.2955f, 84.5808f,
        9.6953f, 83.4244f, 54.2804f,
        17.9813f, -30.8796f, 93.3977f,
        11.3242f, 84.634f, 52.0465f,
        11.9797f, 87.2212f, 47.4232f,
        19.1827f, 62.2507f, 75.8741f,
        19.4955f, -72.7995f, 65.7279f,
        29.0189f, -17.6779f, 94.05f,
        24.3533f, 58.2415f, 77.5554f,
        16.7584f, -82.1605f, 54.4867f,
        35.4813f, -14.2341f, 92.4038f,
        18.1576f, 86.8071f, 46.2041f,
        0.789823f, 99.9918f, 1.01264f,
        27.362f, -68.6051f, 67.4141f,
        37.4798f, 26.4645f, 88.8532f,
        25.3998f, -75.5542f, 60.3855f,
        27.407f, 75.0438f, 60.1439f,
        22.3807f, -84.0913f, 49.2723f,
        42.1779f, -27.4589f, 86.412f,
        45.9807f, -17.9403f, 86.9708f,
        21.2311f, 89.6254f, 38.9426f,
        41.1908f, 49.3621f, 76.5944f,
        44.8329f, 35.5238f, 82.0248f,
        30.0981f, -78.379f, 54.3216f,
        15.491f, 95.3303f, 25.9259f,
        46.6564f, -35.9635f, 80.8072f,
        23.5828f, -87.9397f, 41.3577f,
        38.0482f, 67.3396f, 63.3854f,
        48.3297f, 39.8149f, 77.9681f,
        43.8821f, 57.3394f, 69.1849f,
        34.916f, -78.2741f, 51.5174f,
        57.0462f, -5.19438f, 81.968f,
        65.2709f, 5.64459f, 75.5503f,
        38.006f, 82.8017f, 41.224f,
        60.0507f, 45.8038f, 65.5434f,
        41.7032f, 79.6134f, 43.8469f,
        44.3233f, -76.4812f, 46.7554f,
        68.7625f, -15.4677f, 70.9399f,
        53.5145f, -64.6853f, 54.3325f,
        71.2288f, 7.13185f, 69.8255f,
        42.9249f, 80.3923f, 41.1647f,
        56.3062f, 62.7152f, 53.8184f,
        55.271f, 65.5473f, 51.4652f,
        52.1578f, 70.5343f, 48.005f,
        65.0234f, -48.461f, 58.5105f,
        71.021f, -37.0684f, 59.8494f,
        50.1553f, 76.4484f, 40.498f,
        76.9717f, 15.6932f, 61.8795f,
        77.4466f, 16.9053f, 60.9609f,
        79.0181f, -16.433f, 59.0432f,
        55.5247f, 74.0658f, 37.832f,
        70.15f, 53.418f, 47.1751f,
        61.2597f, 67.6601f, 40.8577f,
        81.5218f, -16.9579f, 55.3771f,
        75.8638f, 40.8543f, 50.7505f,
        75.9219f, 41.2592f, 50.3342f,
        76.4418f, 40.841f, 49.8865f,
        35.3104f, -90.4879f, 23.7723f,
        76.8357f, 40.7587f, 49.3458f,
        72.4251f, 52.8195f, 44.3249f,
        22.7313f, -96.2403f, 14.8695f,
        65.9698f, 64.2925f, 38.9161f,
        83.7857f, -23.3592f, 49.3388f,
        84.6974f, 21.6274f, 48.5655f,
        86.8066f, 10.434f, 48.5361f,
        66.1982f, -67.2941f, 33.0046f,
        41.3978f, -88.68f, 20.5444f,
        87.2437f, 26.9382f, 40.7782f,
        74.9885f, -55.6271f, 35.8099f,
        86.9248f, 30.1413f, 39.1864f,
        75.7266f, -55.9438f, 33.7012f,
        87.0397f, 32.8542f, 36.6701f,
        88.5953f, 27.5003f, 37.3444f,
        88.6497f, 27.347f, 37.3279f,
        89.4973f, 28.4169f, 34.3906f,
        93.2479f, -5.84788f, 35.646f,
        80.3278f, -50.8474f, 31.016f,
        53.3064f, -81.9602f, 20.9988f,
        90.7901f, -24.7065f, 33.8638f,
        94.6478f, 12.12f, 29.915f,
        95.072f, 9.76697f, 29.4267f,
        95.8261f, 4.25829f, 28.2706f,
        80.5653f, 54.7068f, 22.7243f,
        69.8473f, 69.2437f, 18.074f,
        73.0221f, 65.7057f, 18.7223f,
        73.1633f, 65.9142f, 17.3912f,
        89.8678f, -38.0604f, 21.7987f,
        97.0386f, -8.86577f, 22.47f,
        94.0086f, -27.9084f, 19.5831f,
        68.2315f, 71.9316f, 13.0498f,
        97.0363f, -14.2658f, 19.505f,
        97.5936f, -11.9174f, 18.2612f,
        98.7147f, -4.18224f, 15.4245f,
        98.2385f, 11.0597f, 15.0626f,
        86.8442f, 47.8807f, 12.866f,
        92.6115f, -35.4445f, 12.9148f,
        99.2536f, -0.522142f, 12.1839f,
        80.7997f, -58.0285f, 10.2032f,
        94.5279f, -30.6065f, 11.3011f,
        97.9217f, 17.2517f, 10.6644f,
        98.8976f, -10.2966f, 10.6418f,
        99.44f, -2.09763f, 10.358f,
        92.8253f, 36.0689f, 9.0832f,
        45.8852f, -88.6928f, 5.30403f,
        99.4678f, -4.5363f, 9.2507f,
        99.5905f, -3.39014f, 8.38108f,
        82.5049f, -56.0266f, 7.34583f,
        92.2101f, -38.185f, 6.26066f,
        96.516f, -25.5816f, 5.49959f,
        98.4386f, -16.7968f, 5.26376f,
        77.4976f, 63.1334f, 2.87896f,
        62.7453f, -77.7875f, 3.48466f,
        93.3664f, -35.6398f, 3.53714f,
        81.0756f, -58.4509f, 3.19964f,
        99.1435f, 12.8916f, 2.08902f,
        58.3756f, 81.1928f, 0.120564f,
        96.9464f, -24.4762f, 1.52084f,
        70.7753f, 70.6459f, 0.145658f,
        79.6397f, 60.4773f, 0.0967193f,
        73.3544f, -67.9637f, 0.273127f,
        92.1889f, 38.2791f, -5.99256f,
        99.1922f, -10.9296f, -6.43838f,
        86.2075f, -50.0957f, -7.66095f,
        91.9157f, 38.2903f, -9.23922f,
        94.6632f, -30.8284f, -9.40693f,
        83.0617f, -55.1011f, -8.03939f,
        60.274f, -79.5429f, -6.3222f,
        94.636f, 28.2328f, -15.7143f,
        93.1988f, -32.9787f, -15.0462f,
        71.9142f, -68.4498f, -11.9574f,
        88.8756f, 42.4691f, -17.2482f,
        95.5807f, 22.3175f, -19.1379f,
        93.9218f, -28.763f, -18.7454f,
        82.3447f, -53.7426f, -18.1957f,
        80.7512f, 55.863f, -18.9357f,
        46.0014f, -88.2467f, -9.81778f,
        61.9529f, -77.2897f, -13.7167f,
        88.6908f, -40.9667f, -21.3467f,
        84.6388f, -48.4386f, -22.1355f,
        85.164f, -46.8465f, -23.5054f,
        89.9851f, 35.1356f, -25.8489f,
        88.0355f, -40.4078f, -24.8389f,
        85.6174f, -44.4531f, -26.3359f,
        31.8918f, -94.2635f, -9.86429f,
        84.5655f, -45.0453f, -28.6286f,
        90.3504f, 28.4689f, -32.0362f,
        75.285f, -60.3173f, -26.3436f,
        87.1206f, 37.4319f, -31.7624f,
        35.4407f, -92.6904f, -12.347f,
        82.343f, 46.6357f, -32.3224f,
        81.3993f, -48.9429f, -31.285f,
        91.8866f, 14.4174f, -36.7287f,
        67.3291f, -68.5836f, -27.624f,
        77.7765f, 52.827f, -34.0606f,
        77.7768f, 52.8265f, -34.0607f,
        90.382f, 9.10671f, -41.811f,
        89.1464f, -16.5927f, -42.1615f,
        81.775f, 41.3074f, -40.0818f,
        79.1162f, 46.9874f, -39.1511f,
        78.6768f, -48.4118f, -38.2918f,
        70.6659f, -61.523f, -34.9464f,
        26.9919f, -95.4272f, -12.8485f,
        80.6105f, -42.0398f, -41.6485f,
        67.0302f, -65.0454f, -35.7219f,
        52.5582f, -79.8449f, -29.3672f,
        65.731f, -64.2832f, -39.3333f,
        77.3849f, -41.1581f, -48.1413f,
        57.2298f, -73.5348f, -36.2958f,
        81.7164f, 15.9632f, -55.386f,
        41.2415f, -86.1715f, -29.5569f,
        80.2869f, -6.81265f, -59.225f,
        38.7691f, 87.2223f, -29.8198f,
        32.5478f, -91.4515f, -24.0263f,
        62.4897f, -57.7976f, -52.4831f,
        52.4656f, -72.7161f, -44.2689f,
        46.1354f, -79.7815f, -38.8129f,
        71.5275f, 31.1575f, -62.5542f,
        63.2589f, -54.7364f, -54.7926f,
        74.2025f, 11.1797f, -66.0985f,
        43.486f, -81.6222f, -38.0367f,
        51.982f, -71.9845f, -46.0012f,
        71.7297f, 10.3583f, -68.9025f,
        47.4398f, 74.3631f, -47.1125f,
        35.4051f, -87.1597f, -33.9068f,
        52.5798f, 66.6308f, -52.8743f,
        47.3165f, 73.3216f, -48.8375f,
        47.2557f, -73.2516f, -49.001f,
        28.0095f, -91.6336f, -28.614f,
        49.5265f, -68.75f, -53.1091f,
        34.6682f, -85.687f, -38.1556f,
        31.195f, -88.5533f, -34.4264f,
        66.0887f, 4.03788f, -74.9398f,
        23.0605f, -93.7993f, -25.8825f,
        51.7768f, 59.9059f, -61.0774f,
        33.3352f, -85.9632f, -38.7181f,
        52.7505f, 56.4859f, -63.4565f,
        36.4522f, -81.926f, -44.2646f,
        61.0049f, -15.0547f, -77.7931f,
        27.3382f, 89.1497f, -36.1242f,
        37.1326f, 78.453f, -49.6618f,
        46.129f, -64.9006f, -60.4982f,
        32.8942f, -83.8998f, -43.3451f,
        57.3974f, -1.99439f, -81.863f,
        56.0993f, 17.1795f, -80.9799f,
        50.5594f, 40.3133f, -76.2796f,
        25.6054f, -88.7074f, -38.4106f,
        27.4542f, 85.7514f, -43.5081f,
        23.0938f, -90.6838f, -35.258f,
        47.3719f, 43.848f, -76.3758f,
        29.6714f, -81.4802f, -49.8055f,
        45.198f, 28.841f, -84.4117f,
        45.7867f, 20.7352f, -86.4502f,
        44.883f, -21.3954f, -86.7626f,
        32.8818f, -67.0711f, -66.485f,
        39.978f, 39.7424f, -82.5972f,
        31.7897f, 68.0906f, -65.978f,
        15.2681f, -93.992f, -30.5352f,
        20.8259f, -87.7416f, -43.2168f,
        39.7635f, 33.9422f, -85.2455f,
        39.7619f, 33.9404f, -85.247f,
        30.9613f, 66.2613f, -68.1971f,
        38.1312f, -28.964f, -87.7901f,
        11.1462f, -96.1414f, -25.1514f,
        20.3016f, -85.4816f, -47.7571f,
        36.6433f, 16.1719f, -91.6283f,
        17.7637f, -88.0356f, -43.9795f,
        23.5207f, -74.5776f, -62.3294f,
        14.2561f, -90.1791f, -40.7983f,
        20.4336f, -75.9498f, -61.7583f,
        29.0273f, -27.8884f, -91.5404f,
        23.7242f, 56.23f, -79.2171f,
        14.7997f, -85.5846f, -49.5605f,
        13.8992f, 83.2752f, -53.5915f,
        11.5048f, 88.0898f, -45.9112f,
        15.5402f, 70.0892f, -69.6133f,
        11.4649f, -85.6942f, -50.2499f,
        18.628f, 35.0593f, -91.7815f,
        19.1193f, 26.6052f, -94.4808f,
        15.0953f, 54.6017f, -82.4063f,
        17.0638f, -25.5085f, -95.1743f,
        9.83465f, -81.4025f, -57.2443f,
        15.4118f, 18.2736f, -97.1007f,
        4.39278f, 93.5636f, -35.0223f,
        9.98255f, -52.7813f, -84.3474f,
        4.7856f, -89.1163f, -45.1153f,
        4.08775f, 74.0564f, -67.0742f,
        2.47962f, -91.8644f, -39.4308f,
        4.61817f, 25.1595f, -96.673f,
        4.05703f, 3.07953f, -99.8702f,
        1.59381f, 80.5874f, -59.1873f,
        -2.30833f, -77.4089f, -63.2656f,
        -4.07671f, -38.4613f, -92.2177f,
        -3.10285f, -79.1955f, -60.979f,
        -3.4258f, -85.4902f, -51.7657f,
        -3.66008f, 83.8979f, -54.2931f,
        -6.57101f, -30.1404f, -95.123f,
        -8.67442f, -1.16401f, -99.6163f,
        -4.59675f, -86.9505f, -49.1781f,
        -5.2392f, -89.1791f, -44.9404f,
        -5.24111f, -89.1793f, -44.9397f,
        -7.80706f, -76.8625f, -63.4918f,
        -12.4581f, -28.4276f, -95.0614f,
        -7.36086f, -83.9746f, -53.7967f,
        -4.33179f, -95.1772f, -30.3734f,
        -13.7187f, -39.7094f, -90.7467f,
        -5.02961f, 93.8423f, -34.1809f,
        -5.7531f, -93.4426f, -35.1481f,
        -10.843f, -74.9431f, -65.3143f,
        -11.8292f, -75.4248f, -64.5846f,
        -18.0719f, -2.52949f, -98.3209f,
        -18.0719f, -2.52949f, -98.3209f,
        -7.47848f, -92.7889f, -36.528f,
        -10.433f, -86.3295f, -49.3798f,
        -13.0744f, 82.8644f, -54.4293f,
        -23.982f, 5.92628f, -96.9007f,
        -18.9901f, 62.003f, -76.1249f,
        -8.49389f, -94.8594f, -30.4885f,
        -26.3094f, 19.0109f, -94.5854f,
        -31.0361f, -39.3487f, -86.5358f,
        -27.614f, -59.7796f, -75.2586f,
        -20.5759f, 81.8403f, -53.6544f,
        -20.5812f, 81.8367f, -53.6579f,
        -35.6354f, -19.3573f, -91.4079f,
        -29.8932f, -63.4832f, -71.248f,
        -40.15f, -1.03991f, -91.58f,
        -25.1315f, -80.3508f, -53.9644f,
        -34.3393f, -66.5071f, -66.3145f,
        -29.48f, 75.8286f, -58.146f,
        -33.9484f, -67.5254f, -65.4815f,
        -32.7659f, -73.4776f, -59.3922f,
        -45.5261f, 31.5612f, -83.2542f,
        -36.6127f, -67.0437f, -64.5341f,
        -35.1218f, -70.4679f, -61.6501f,
        -25.4283f, -86.9263f, -42.3937f,
        -46.8092f, -44.9046f, -76.1083f,
        -42.2763f, -59.2997f, -68.5292f,
        -22.3364f, 90.165f, -37.0318f,
        -52.6988f, 32.8578f, -78.3786f,
        -39.6542f, -72.004f, -56.9471f,
        -53.0667f, 50.5603f, -68.0263f,
        -48.3312f, 61.9893f, -61.8176f,
        -46.5274f, -67.1174f, -57.7101f,
        -31.259f, -87.3222f, -37.3859f,
        -31.2593f, -87.3225f, -37.3851f,
        -27.5433f, -90.6125f, -32.1051f,
        -43.9536f, -73.5959f, -51.4949f,
        -12.7077f, -98.1776f, -14.1307f,
        -51.3104f, -61.2819f, -60.098f,
        -64.9777f, -9.85961f, -75.3704f,
        -58.7055f, 45.5143f, -66.9486f,
        -66.3069f, 3.30292f, -74.7829f,
        -65.1994f, -27.6336f, -70.6075f,
        -18.5072f, 96.2006f, -20.0728f,
        -51.27f, -68.3706f, -51.9312f,
        -52.2699f, -67.0481f, -52.6537f,
        -54.3119f, 64.7994f, -53.3971f,
        -65.0631f, -42.7073f, -62.7923f,
        -48.9983f, -75.1699f, -44.1435f,
        -45.7022f, -78.9075f, -41.0477f,
        -27.7408f, -93.1561f, -23.503f,
        -62.9496f, 54.9238f, -54.9612f,
        -74.7448f, -16.3034f, -64.4004f,
        -58.4131f, -65.1403f, -48.4216f,
        -62.1749f, -59.147f, -51.3412f,
        -54.9913f, -70.3264f, -45.0573f,
        -23.9468f, 95.0157f, -19.9644f,
        -40.1967f, 85.6863f, -32.2814f,
        -68.81f, 48.6424f, -53.8433f,
        -60.7362f, -65.8255f, -44.4759f,
        -78.1039f, -25.5268f, -56.9926f,
        -79.2341f, 18.2879f, -58.2023f,
        -79.2337f, 18.2908f, -58.202f,
        -71.9642f, 44.9549f, -52.917f,
        -71.5734f, -47.1551f, -51.5136f,
        -70.8059f, -49.6637f, -50.1999f,
        -74.0489f, 44.3002f, -50.5396f,
        -82.448f, 11.1912f, -55.4715f,
        -80.4298f, 26.5925f, -53.1403f,
        -84.099f, -5.98339f, -53.7732f,
        -70.4121f, -55.3787f, -44.4447f,
        -84.2721f, 7.80724f, -53.266f,
        -38.253f, -89.4393f, -23.1802f,
        -74.9867f, -48.8075f, -44.6634f,
        -82.6322f, 26.9956f, -49.4283f,
        -77.5361f, -44.0161f, -45.285f,
        -67.8935f, -62.1106f, -39.1503f,
        -80.0068f, -38.4644f, -46.0369f,
        -77.793f, 43.712f, -45.1388f,
        -82.5727f, -33.8829f, -45.0965f,
        -82.3818f, -35.2971f, -44.3549f,
        -39.7955f, -89.6375f, -19.5305f,
        -89.3744f, -6.44355f, -44.3926f,
        -90.0161f, -8.1808f, -42.7805f,
        -81.9353f, -43.1972f, -37.6911f,
        -62.5668f, 72.3128f, -29.2618f,
        -85.9454f, 32.8093f, -39.2038f,
        -17.9f, -98.1283f, -7.10067f,
        -43.5701f, 87.8935f, -19.4005f,
        -82.6411f, -44.5135f, -34.482f,
        -85.9996f, 36.6334f, -35.5255f,
        -92.4843f, 3.46185f, -37.8771f,
        -82.2483f, -47.2799f, -31.6202f,
        -91.9516f, -18.3389f, -34.7647f,
        -80.1958f, 52.4031f, -28.68f,
        -34.0717f, -93.3754f, -10.9614f,
        -73.4385f, 62.8265f, -25.683f,
        -49.0465f, -85.7539f, -15.5149f,
        -78.8103f, -56.343f, -24.7871f,
        -75.2959f, -61.6315f, -23.067f,
        -75.3734f, -61.5902f, -22.9236f,
        -70.9013f, -67.3805f, -20.8058f,
        -53.9393f, -82.8943f, -14.8017f,
        -95.0377f, 16.2895f, -26.5045f,
        -82.8879f, 51.3936f, -22.0974f,
        -94.0101f, -27.1016f, -20.6788f,
        -71.2682f, -68.5045f, -15.0987f,
        -40.1054f, 91.1509f, -9.11418f,
        -94.9722f, 24.8526f, -19.0427f,
        -89.0078f, 42.0073f, -17.692f,
        -78.5283f, 59.9152f, -15.6036f,
        -89.3889f, -42.2609f, -14.9546f,
        -55.95f, -82.4423f, -8.53657f,
        -54.7418f, -83.2704f, -8.32942f,
        -48.574f, -87.1932f, -6.15821f,
        -78.9053f, -60.5931f, -10.1208f,
        -63.9585f, -76.4653f, -7.89797f,
        -79.2261f, -60.3262f, -9.16385f,
        -60.6426f, 79.1238f, -7.86713f,
        -72.7802f, -68.197f, -7.22594f,
        -97.0237f, 21.7462f, -10.6537f,
        -95.9495f, -26.5533f, -9.41357f,
        -77.455f, -62.9727f, -5.92981f,
        -42.6154f, -90.4261f, -2.65624f,
        -69.1792f, 71.9417f, -6.21516f,
        -99.4234f, 7.96287f, -7.18199f,
        -76.3497f, -64.4483f, -4.13996f,
        -88.3682f, 46.516f, -5.22747f,
        -99.7491f, 4.72324f, -5.27418f,
        -79.7399f, -60.2419f, -3.5309f,
        -54.6282f, 83.7459f, -1.543f,
        -79.589f, 60.5302f, -1.30123f,
        -98.5477f, -16.9756f, -0.418532f,
        -87.2473f, 48.8587f, -0.850866f,
        -62.2598f, 78.2487f, -0.9219f,
        -99.8687f, 5.11449f, 0.281065f,
        -64.1293f, -76.7072f, 1.85617f,
        -86.2023f, -50.6398f, 2.18515f,
        -98.5594f, 16.6147f, 3.16193f,
        -87.6147f, 48.118f, 2.88479f,
        -93.1529f, -35.9327f, 5.60093f,
        -79.8764f, -59.8488f, 6.1555f,
        -86.3884f, -49.7399f, 7.93608f,
        -99.4406f, -5.05736f, 9.27297f,
        -82.0678f, -56.4747f, 8.68783f,
        -92.3722f, 37.0877f, 9.58579f,
        -69.0248f, -71.8956f, 8.16147f,
        -89.6455f, -42.9277f, 10.9952f,
        -29.5577f, 95.4931f, 2.72334f,
        -97.8012f, -14.3392f, 15.1433f,
        -76.9414f, 62.6381f, 12.5095f,
        -87.3456f, -45.3848f, 17.6344f,
        -81.5344f, 54.9938f, 18.1056f,
        -87.0573f, -44.3019f, 21.4095f,
        -90.3471f, -36.0105f, 23.2518f,
        -81.3929f, 54.0085f, 21.4074f,
        -83.4912f, -49.8189f, 23.3943f,
        -89.2113f, -37.0422f, 25.8691f,
        -84.8123f, -46.4387f, 25.5014f,
        -93.1596f, 23.9607f, 27.3345f,
        -95.5033f, -8.51126f, 28.402f,
        -89.052f, -35.8753f, 27.9769f,
        -36.1187f, 92.4956f, 11.8325f,
        -56.3216f, 80.2491f, 19.6969f,
        -89.2071f, -30.648f, 33.2083f,
        -70.8804f, -65.0988f, 27.1682f,
        -92.9842f, 5.43354f, 36.3925f,
        -81.4974f, 46.8851f, 34.0584f,
        -57.253f, 78.5098f, 23.6287f,
        -87.9126f, 18.418f, 43.9562f,
        -63.2769f, 70.872f, 31.1961f,
        -84.594f, 31.787f, 42.8188f,
        -87.4842f, 15.4164f, 45.9221f,
        -88.184f, 1.75494f, 47.1222f,
        -30.071f, 94.1281f, 15.3506f,
        -25.4114f, -95.5847f, 14.7589f,
        -87.0597f, 11.1585f, 47.9176f,
        -71.5185f, 57.4767f, 39.7685f,
        -81.8936f, 33.3679f, 46.6908f,
        -34.179f, -91.4832f, 21.5084f,
        -84.0232f, -1.43354f, 54.2038f,
        -57.2063f, 72.8268f, 37.7319f,
        -55.8664f, 73.9801f, 37.4953f,
        -80.4319f, -21.7202f, 55.3077f,
        -78.9587f, -25.5132f, 55.8086f,
        -44.1425f, -83.6143f, 32.5591f,
        -62.0856f, 64.6213f, 44.3786f,
        -52.4185f, -73.4813f, 43.0442f,
        -74.7931f, 25.199f, 61.4085f,
        -73.7667f, 27.416f, 61.6995f,
        -30.3413f, -91.4981f, 26.5989f,
        -53.6183f, 71.0557f, 45.5649f,
        -62.0622f, 55.8763f, 55.0102f,
        -73.3892f, -16.4976f, 65.8927f,
        -35.4439f, 88.1624f, 31.1629f,
        -37.8227f, -85.2222f, 36.1473f,
        -53.8829f, 65.8259f, 52.5698f,
        -49.8193f, 69.3751f, 52.0109f,
        -57.5525f, 50.3426f, 64.4463f,
        -51.9787f, 61.6288f, 59.1617f,
        -65.3888f, 9.1463f, 75.1042f,
        -29.8134f, 88.7699f, 35.0864f,
        -57.3389f, -38.1254f, 72.5169f,
        -60.0503f, -9.70811f, 79.3707f,
        -20.414f, 94.2995f, 26.2844f,
        -12.4066f, -97.5879f, 17.9634f,
        -54.9179f, -28.6729f, 78.4977f,
        -55.0243f, 17.1499f, 81.7203f,
        -52.6576f, -27.7771f, 80.3468f,
        -41.5486f, -60.689f, 67.7536f,
        -47.7982f, -0.558018f, 87.8352f,
        -31.9798f, -73.0891f, 60.2933f,
        -42.7721f, 42.8068f, 79.6124f,
        -45.828f, 10.7961f, 88.2227f,
        -24.1568f, 84.9903f, 46.8305f,
        -21.2583f, -86.8283f, 44.8211f,
        -41.5934f, -2.42092f, 90.9072f,
        -27.9743f, -68.8298f, 66.932f,
        -20.2181f, 85.1867f, 48.3162f,
        -24.1205f, 76.9204f, 59.173f,
        -33.0034f, 18.7919f, 92.5075f,
        -22.6267f, -72.998f, 64.4928f,
        -28.4875f, 50.3343f, 81.5777f,
        -28.8825f, 39.9798f, 86.9909f,
        -19.1699f, -78.0615f, 59.4888f,
        -27.3396f, 41.6307f, 86.7146f,
        -12.1883f, 91.4964f, 38.469f,
        -28.7272f, -13.1906f, 94.8723f,
        -27.0639f, -27.263f, 92.327f,
        -23.3584f, -49.4279f, 83.7333f,
        -18.5357f, 67.335f, 71.5712f,
        -21.428f, 47.0747f, 85.5851f,
        -23.034f, 26.2278f, 93.7099f,
        -20.4027f, -36.1177f, 90.9903f,
        -15.1306f, -71.0144f, 68.7606f,
        -9.72202f, -85.0222f, 51.7369f,
        -18.5512f, 5.72543f, 98.0973f,
        -15.1073f, -34.3669f, 92.686f,
        -6.73281f, 72.487f, 68.5587f,
        -1.92753f, 97.6794f, 21.3312f,

    };

    public final static DockingGeometryStarfield Instance = new DockingGeometryStarfield();

    protected final static void Init(){
    }


    private final int count;

    private final FloatBuffer points;


    private DockingGeometryStarfield(){
        super();

        final int xyz_len = XYZ.length;

        this.count = (xyz_len/3);
        {
            final ByteBuffer ib = ByteBuffer.allocateDirect(xyz_len * bpf);
            ib.order(nativeOrder);
            this.points = ib.asFloatBuffer();
            this.points.put(XYZ);
            this.points.position(0);
        }
    }


    public void draw(){

        glEnableClientState(GL_VERTEX_ARRAY);

        glVertexPointer(3,GL_FLOAT,stride,this.points);

        glDrawArrays(GL_POINTS,0,this.count);

        glDisableClientState(GL_VERTEX_ARRAY);
    }
}