package com.squashjam.game.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;

public class AnimationUtils {
    public static Animation<TextureRegion> createAnimation(Texture texture, int frameCols, int frameRows, float frameDuration) {
        TextureRegion[][] frameRegions = TextureRegion.split(texture,
                texture.getWidth() / frameCols, texture.getHeight() / frameRows);

        return new Animation<>(frameDuration,
                Arrays.stream(frameRegions)
                        .flatMap(Arrays::stream)
                        .toArray(TextureRegion[]::new));
    }
}
