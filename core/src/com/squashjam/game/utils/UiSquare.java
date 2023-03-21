package com.squashjam.game.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class UiSquare {
    private Texture texture;
    private float x;
    private float y;
    private float width;
    private float height;
    private String label;

    public UiSquare(String texturePath, float x, float y, float width, float height, String label, AssetManager assetManager) {
        this.texture = assetManager.get(texturePath, Texture.class);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
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
        font.draw(batch, label, labelX, labelY);
    }
}
