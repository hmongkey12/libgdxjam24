package com.squashjam.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.squashjam.game.entities.HealthBar;
import com.squashjam.game.screens.IntroScreen;
import com.squashjam.game.screens.RenderScreen;

public class PixelWars extends Game {
	public SpriteBatch batch;
	public AssetManager assetManager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		loadAssets();
		setScreen(new IntroScreen(this));
//		setScreen(new RenderScreen(this));
	}

	@Override
	public void dispose() {
		batch.dispose();
		assetManager.dispose();
		HealthBar.disposePixmap();
	}

	private void loadAssets() {
		assetManager.load("abomination_attack.png", Texture.class);
		assetManager.load("abomination_move.png", Texture.class);
		assetManager.load("abomination_idle.png", Texture.class);
		assetManager.load("attack1.png", Texture.class);
		assetManager.load("background.png", Texture.class);
		assetManager.load("idle.png", Texture.class);
		assetManager.load("moveleft.png", Texture.class);
		assetManager.load("moveright.png", Texture.class);
		assetManager.load("sniper_attack.png", Texture.class);
		assetManager.load("sniper_idle.png", Texture.class);
		assetManager.load("sniper_move.png", Texture.class);
		assetManager.load("uiSquare.png", Texture.class);
		assetManager.load("grunt_move.png", Texture.class);
		assetManager.load("grunt_attack.png", Texture.class);
		assetManager.load("grunt_idle.png", Texture.class);
		assetManager.load("chicken_attack.png", Texture.class);
		assetManager.load("chicken_idle.png", Texture.class);
		assetManager.load("radial_circle.png", Texture.class);
		assetManager.load("demolition_idle.png", Texture.class);
		assetManager.load("demolition_move.png", Texture.class);
		assetManager.load("demolition_attack.png", Texture.class);
		assetManager.load("drone_idle.png", Texture.class);
		assetManager.load("drone_move.png", Texture.class);
		assetManager.load("drone_attack.png", Texture.class);
		assetManager.load("grenadier_attack.png", Texture.class);
		assetManager.load("grenadier_idle.png", Texture.class);
		assetManager.load("grenadier_move.png", Texture.class);
		assetManager.load("grenade_attack.png", Texture.class);
		assetManager.load("grenade_idle.png", Texture.class);
		assetManager.load("grenade_move.png", Texture.class);
		assetManager.load("intro.mp3", Music.class);
		assetManager.load("defeat.mp3", Music.class);
		assetManager.load("victory.mp3", Music.class);
		assetManager.load("war1.png", Texture.class);
		assetManager.load("war2.png", Texture.class);
//		assetManager.load("war3.png", Texture.class);
		assetManager.load("pixelwars.png", Texture.class);

		// Load other assets as needed
		assetManager.finishLoading(); // Blocks until all assets are loaded
	}
}

