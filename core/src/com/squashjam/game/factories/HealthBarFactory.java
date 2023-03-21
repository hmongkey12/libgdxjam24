package com.squashjam.game.factories;

import com.badlogic.gdx.math.Vector2;
import com.squashjam.game.entities.HealthBar;

public class HealthBarFactory {
    public static HealthBar createHealthBar(float characterWidth, float characterHeight, Vector2 position, int viewportHeight) {
        return new HealthBar(characterWidth, viewportHeight * 0.015f, new Vector2(position.x, position.y + characterHeight));
    }
}

