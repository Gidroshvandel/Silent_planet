package com.silentgames.silent_planet.logic.ecs.entity.unit

import android.content.Context
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.MovingMode
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.logic.ecs.entity.Entity
import com.silentgames.silent_planet.model.fractions.FractionsType

abstract class Unit(
        val context: Context,
        position: Position,
        texture: Texture,
        movingMode: MovingMode,
        fractionsType: FractionsType,
        description: Description
) : Entity() {

    init {
        addComponent(position)
        addComponent(texture)
        addComponent(movingMode)
        addComponent(fractionsType)
        addComponent(description)
    }

}