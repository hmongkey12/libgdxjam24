package com.squashjam.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthBar {
    private float width;
    private float height;
    private Vector2 position;

    private Texture backgroundTexture;
    private Texture foregroundTexture;
    private static Pixmap pixmap;

    static {
        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    }

    public HealthBar(float width, float height, Vector2 position) {
        this.width = width;
        this.height = height;
        this.position = position;

        this.backgroundTexture = createTexture(Color.BLACK);
        this.foregroundTexture = createTexture(Color.RED);
    }

    private Texture createTexture(Color color) {
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        return texture;
    }

    public void update(Vector2 entityPosition) {
        this.position.set(entityPosition.x, entityPosition.y + height);
    }

    public void draw(Batch batch, float healthPercentage) {
        batch.draw(backgroundTexture, position.x, position.y, width, height);
        batch.draw(foregroundTexture, position.x, position.y, width * healthPercentage, height);
    }


    public void dispose() {
        backgroundTexture.dispose();
        foregroundTexture.dispose();
    }

    public static void disposePixmap() {
        if (pixmap != null) {
            pixmap.dispose();
            pixmap = null;
        }
    }
}

