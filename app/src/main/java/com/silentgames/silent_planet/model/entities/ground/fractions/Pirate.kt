package com.silentgames.silent_planet.model.entities.ground.fractions

import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.fractions.factionType.Pirates
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class Pirate(
        override var name: String,
        override var bitmap: Bitmap = BitmapEditor.getEntityBitmap(R.drawable.pirate),
        override var description: String = ""
) : Player(Pirates.getInstance())