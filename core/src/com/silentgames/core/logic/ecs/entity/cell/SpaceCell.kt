package com.silentgames.core.logic.ecs.entity.cell


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.MovingMode
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.Texture

/**
 * Created by gidroshvandel on 07.07.16.
 */
class SpaceCell(
        position: Axis,
        imageName: String = "space_texture.jpg"
) : CellEcs(
        Position(position),
        Description(Strings.space_cell_name.getString(), Strings.space_cell_description.getString()),
        Texture(imageName)
) {
    init {
        addComponent(MovingMode.FLY)
    }
}