package com.silentgames.silent_planet.logic;

import android.content.Context;

import com.silentgames.silent_planet.utils.Converter;

import java.util.Map;

/**
 * Created by gidroshvandel on 09.07.16.
 */
public class Constants {

    public static final int horizontalCountOfCells = 12;
    public static final int verticalCountOfCells = 12;
    public static final float mScaleFactor = 1f;
    public static final float cellSize = 31;
    public static Map<String,String> oldXY;
    private static int viewSize;
    private static float canvasSize;

    public Constants(Context context){
        //в xml разметке позднее пропишем размер вьюхи равный 300dp
        viewSize=(int) Converter.convertDpToPixel(350, context.getResources());
        canvasSize=(int)(viewSize*mScaleFactor);//определяем размер канваса
    }

    public static int getHorizontalCountOfCells() {
        return horizontalCountOfCells;
    }

    public static int getVerticalCountOfCells() {
        return verticalCountOfCells;
    }

    public static float getmScaleFactor() {
        return mScaleFactor;
    }

    public int getViewSize() {
        return viewSize;
    }

    public float getCanvasSize() {
        return canvasSize;
    }
}
