package com.squashjam.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.squashjam.game.PixelWars;

public class DefeatOutroScreen extends ScreenAdapter {

    private final PixelWars game;
    private Music outroMusic;
    private Texture background;
    private boolean played;

    public DefeatOutroScreen(PixelWars game) {
        this.game = game;
    }

    @Override
    public void show() {
        outroMusic = game.assetManager.get("defeat.mp3");
        outroMusic.setLooping(false);
        background = game.assetManager.get("war1.png");
        played = false;

        // Set up input handling
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                goToRenderScreen();
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                goToRenderScreen();
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!played) {
            outroMusic.play();
            played = true;
        }

        if (!outroMusic.isPlaying()) {
            goToRenderScreen();
        }

        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();
    }

    private void goToRenderScreen() {
        outroMusic.stop();
        Gdx.input.setInputProcessor(null);
        game.setScreen(new RenderScreen(game));
        dispose();
    }

    @Override
    public void dispose() {
    }
}
