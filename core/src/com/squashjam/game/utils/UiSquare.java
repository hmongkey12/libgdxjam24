package com.squashjam.game.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UiSquare {
    private Texture texture;
    private float x;
    private float y;
    private float width;
    private float height;
    private String label1;
    private String label2;

    public UiSquare(String texturePath, float x, float y, float width, float height, String label1, String label2, AssetManager assetManager) {
        this.texture = assetManager.get(texturePath, Texture.class);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label1 = label1;
        this.label2 = label2;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Batch batch, BitmapFont font) {
        batch.draw(texture, x, y, width, height);
        font.setColor(Color.WHITE);
        float labelX = x + width / 2 - font.getXHeight() / 2;
        float labelY = y + height / 2 + font.getXHeight() / 2;
        font.draw(batch, label1, labelX, labelY);
        font.draw(batch, label2, labelX, labelY - 20);
    }

    public boolean checkClick(float screenX, float screenY) {
        if (screenX >= x && screenX <= x + width &&
                screenY >= y && screenY <= y + height) {
            // The click is inside the UiSquare
            return true;
        }
        return false;
    }
}
