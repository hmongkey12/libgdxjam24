package com.squashjam.game.factories;

import com.badlogic.gdx.math.Vector2;
import com.squashjam.game.entities.HealthBar;

public class HealthBarFactory {
    public static final float ENTITY_HEIGHT_MULTIPLIER = 0.015f;

    public static HealthBar createHealthBar(float entityWidth, float entityHeight, Vector2 position) {
        return new HealthBar(entityWidth, entityHeight * ENTITY_HEIGHT_MULTIPLIER, new Vector2(position.x, position.y + entityHeight));
    }
}

