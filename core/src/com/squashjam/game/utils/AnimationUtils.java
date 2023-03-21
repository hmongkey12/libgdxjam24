package com.squashjam.game.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationUtils {
    public static Animation<TextureRegion> createAnimation(Texture texture, int frameCols, int frameRows, float frameDuration) {
        TextureRegion[][] frameRegions = TextureRegion.split(texture,
                texture.getWidth() / frameCols, texture.getHeight() / frameRows);

        TextureRegion[] frames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                frames[index++] = frameRegions[i][j];
            }
        }
        return new Animation<>(frameDuration, frames);
    }
}
