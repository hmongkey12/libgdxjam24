package com.squashjam.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.squashjam.game.PixelWars;


public class IntroScreen extends ScreenAdapter {

    private final PixelWars game;
    private BitmapFont font;

    private Texture backgroundImage;
    private float elapsedTime;
    private final float INTRO_DURATION = 12f;
    private OrthographicCamera camera;
    private Viewport viewport;

    public IntroScreen(PixelWars game) {
        this.game = game;
    }

    @Override
    public void show() {
        font = new BitmapFont();
        font.getData().setScale(2f);
        backgroundImage = game.assetManager.get("pixelwars.png");
        elapsedTime = 0;

        initCameraAndViewport();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                skipIntro();
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                skipIntro();
                return true;
            }
        });
    }

    private void initCameraAndViewport() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(1200, 800, camera);
        camera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
        viewport.apply();
        Gdx.gl.glClearColor(1, 1, 1, 1);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        elapsedTime += delta;
        if (elapsedTime >= INTRO_DURATION) {
            skipIntro();
        }

        game.batch.begin();
        game.batch.draw(backgroundImage, 0, 0, camera.viewportWidth, camera.viewportHeight);
        font.draw(game.batch, "Presented by Hmongkey12", camera.viewportWidth * 0.25f, camera.viewportHeight * 0.5f);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        font.dispose();
    }

    private void skipIntro() {
        Gdx.input.setInputProcessor(null);
        game.setScreen(new RenderScreen(game));
        dispose();
    }
}

