package com.qin;

public class Constants {

    public static final int vertexColor = 0xFFFFB400;
    public static final int selectedVertexColor = 0xFFFF9500;
    public static final int fixedVertexColor = 0xFFFF6900;
    public static final int animationVertexColor = 0xFFFF3E00;
    public static final int edgeColor = 0xff000000;
    public static final int selectedEdgeColor = 0xFFFF9500;

    public static final int animationInterval = 1000; //画图间隔1s

    public static final int maxX = 800;
    public static final int maxY = 800;
    public static final int minX = 0;
    public static final int minY = 0;

    public static final float edgeWidth = 3;
    public static final float selectedEdgeWidth = 5;

    public static boolean isNumber(String s)
    {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c < '0' || c > '9') && c != '.')
                return false;
        }
        return true;
    }


}