package com.squashjam.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class HealthBar {
    private float width;
    private float height;
    private Vector2 position;

    private Texture backgroundTexture;
    private Texture foregroundTexture;

    public HealthBar(float width, float height, Vector2 position) {
        this.width = width;
        this.height = height;
        this.position = position;

        this.backgroundTexture = createTexture(Color.BLACK);
        this.foregroundTexture = createTexture(Color.RED);
    }

    private Texture createTexture(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public void update(float healthPercentage, Vector2 characterPosition) {
        this.position.set(characterPosition.x, characterPosition.y + height);
    }

    public void draw(Batch batch, float healthPercentage) {
        batch.draw(backgroundTexture, position.x, position.y, width, height);
        batch.draw(foregroundTexture, position.x, position.y, width * healthPercentage, height);
    }


    public void dispose() {
        backgroundTexture.dispose();
        foregroundTexture.dispose();
    }
}

