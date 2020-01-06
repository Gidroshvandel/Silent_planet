package com.silentgames.silent_planet.model.fractions

import com.silentgames.silent_planet.logic.ecs.component.Component

/**
 * Created by gidroshvandel on 26.09.16.
 */
enum class FractionsType : Component {
    ALIEN,
    HUMAN,
    PIRATE,
    ROBOT;

    operator fun next(): FractionsType {
        return vals[(this.ordinal + 1) % vals.size]
    }

    companion object {

        private val vals = values()
    }
}

