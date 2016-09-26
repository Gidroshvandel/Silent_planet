package com.silentgames.silent_planet.model.entities.space.fractions;

import android.content.res.Resources;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.logic.Fractions;
import com.silentgames.silent_planet.model.entities.space.SpaceShip;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by gidroshvandel on 24.09.16.
 */
public class AlienShip extends SpaceShip {
    public AlienShip(Resources res) {
        super.setBitmap(BitmapEditor.getEntityBitmap(R.drawable.aliens_space_ship,res));
        super.setCanFly(true);
        super.setFraction(Fractions.Aliens);
    }
}