package com.squashjam.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.squashjam.game.PixelWars;
import com.squashjam.game.enums.EntityType;

public class OutroScreen extends ScreenAdapter {

    private final PixelWars game;
    private Texture background;
    private EntityType entityType;

    private Music outroMusic;
    private boolean played;

    public OutroScreen(PixelWars game, EntityType entityType) {
        this.game = game;
        this.entityType = entityType;
    }

    @Override
    public void show() {
        if (entityType == null) {
            outroMusic = game.assetManager.get("panamahat.mp3"); // switch to new music
            outroMusic.setLooping(false);
            background = game.assetManager.get("war2.png"); // switch to new background
        } else {
            outroMusic = game.assetManager.get("panamahat.mp3"); // switch to new music
            outroMusic.setLooping(false);
            background = game.assetManager.get("war1.png"); // switch to new background
        }

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
        Gdx.input.setInputProcessor(null);
        game.setScreen(new RenderScreen(game));
    }

    @Override
    public void hide(){
        outroMusic.stop();
        dispose();
    }

    @Override
    public void dispose() {
    }
}
