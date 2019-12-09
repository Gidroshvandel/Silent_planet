package com.silentgames.silent_planet.engine

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.silentgames.silent_planet.engine.base.Sprite
import com.silentgames.silent_planet.logic.Constants.horizontalCountOfCells
import com.silentgames.silent_planet.logic.Constants.verticalCountOfCells
import com.silentgames.silent_planet.model.Axis

class Background(
        axis: Axis,
        bmp: Bitmap
) : Sprite(axis, bmp) {
    override fun update() {}

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(
                bmp,
                axis.x.toFloat() * canvas.width / horizontalCountOfCells,
                axis.y.toFloat() * canvas.height / verticalCountOfCells,
                paint
        )
    }


}