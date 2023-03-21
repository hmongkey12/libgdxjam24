package com.squashjam.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.squashjam.game.screens.RenderScreen;

public class PixelWars extends Game {
	public SpriteBatch batch;
	public AssetManager assetManager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		loadAssets();
		setScreen(new RenderScreen(this));
	}

	@Override
	public void dispose() {
		batch.dispose();
		assetManager.dispose();
	}

	private void loadAssets() {
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
}

