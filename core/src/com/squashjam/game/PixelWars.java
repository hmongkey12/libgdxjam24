package com.squashjam.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.squashjam.game.screens.RenderScreen;

public class PixelWars extends Game {
	public SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
		setScreen(new RenderScreen(this));
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
