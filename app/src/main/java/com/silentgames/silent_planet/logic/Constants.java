package com.silentgames.silent_planet.logic;

import android.content.Context;

import com.silentgames.silent_planet.model.entities.ground.Player;
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
    public static final float entitySize = 20;
    public static final int countArrowCells = 3;

    public static int getHorizontalCountOfCells() {
        return horizontalCountOfCells;
    }

    public static int getVerticalCountOfCells() {
        return verticalCountOfCells;
    }

    public static float getmScaleFactor() {
        return mScaleFactor;
    }

    public static int getViewSize(Context context) {
        return (int) Converter.convertDpToPixel(350, context.getResources());
    }

    public static float getCanvasSize(Context context) {
        return (int)(getViewSize(context)*mScaleFactor);
    }

}
