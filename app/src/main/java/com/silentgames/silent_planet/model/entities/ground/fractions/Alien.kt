package com.silentgames.silent_planet.model.entities.ground.fractions

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.fractions.factionType.Aliens
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class Alien(
        context: Context,
        override var name: String,
        override var bitmap: Bitmap = BitmapEditor.getEntityBitmap(context, R.drawable.alien),
        override var description: String = context.getString(R.string.alien_player_description)
) : Player(context, Aliens) {

    override fun copy(): EntityType = Alien(
            context,
            name
    ).also {
        it.crystals = crystals
        it.isCanFly = isCanFly
        it.isCanMove = isCanMove
        it.isDead = isDead
        it.effects = effects
        it.goal = goal
    }

}
