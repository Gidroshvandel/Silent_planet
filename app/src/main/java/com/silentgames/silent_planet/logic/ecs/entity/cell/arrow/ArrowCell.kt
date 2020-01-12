package com.silentgames.silent_planet.logic.ecs.entity.cell.arrow

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.logic.ecs.entity.cell.GroundCell

/**
 * Created by gidroshvandel on 09.12.16.
 */
abstract class ArrowCell(
        context: Context,
        position: Axis,
        rotateAngle: RotateAngle,
        arrowMode: ArrowMode,
        distance: Int,
        texture: Int,
        description: String = ""
) : GroundCell(
        context,
        Position(position),
        Hide(
                texture,
                Description(context.getString(R.string.arrow_cell_name), description)
        )
) {

    init {
        addComponent(Arrow(distance, arrowMode, rotateAngle))
    }

}
