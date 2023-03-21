package com.squashjam.game.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class AssetManagerUtil {
    private static AssetManager assetManager;

    public static AssetManager get() {
        if (assetManager == null) {
            assetManager = new AssetManager();
            assetManager.load("abomination_attack.png", Texture.class);
            assetManager.load("abomination_walk.png", Texture.class);
            assetManager.load("attack1.png", Texture.class);
            assetManager.load("background.png", Texture.class);
            assetManager.load("idle.png", Texture.class);
            assetManager.load("moveleft.png", Texture.class);
            assetManager.load("moveright.png", Texture.class);
            assetManager.load("sniper_attack.png", Texture.class);
            assetManager.load("sniper_walk.png", Texture.class);
            assetManager.load("uiSquare.png", Texture.class);
            assetManager.load("grunt_walk.png", Texture.class);
            assetManager.load("grunt_attack.png", Texture.class);
            assetManager.load("chicken_attack.png", Texture.class);
            assetManager.load("chicken_idle.png", Texture.class);

            // Load other assets as needed
            assetManager.finishLoading(); // Blocks until all assets are loaded
        }
        return assetManager;
    }

    public static void dispose() {
        if (assetManager != null) {
            assetManager.dispose();
            assetManager = null;
        }
    }
}

