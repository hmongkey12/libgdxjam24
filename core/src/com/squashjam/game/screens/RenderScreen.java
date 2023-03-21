package com.squashjam.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.squashjam.game.PixelWars;
import com.squashjam.game.behaviors.GrenadierBehavior;
import com.squashjam.game.entities.Entity;
import com.squashjam.game.enums.EntityTeam;
import com.squashjam.game.enums.EntityType;
import com.squashjam.game.factories.EntityFactory;
import com.squashjam.game.handlers.InputHandler;
import com.squashjam.game.utils.AssetManagerUtil;
import com.squashjam.game.utils.GameUI;
import com.squashjam.game.utils.UiSquare;

import java.util.*;


public class RenderScreen extends ScreenAdapter {

    private GrenadierBehavior grenadierBehavior;

    private GameUI gameUI;

    private final PixelWars game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture backgroundTexture;
    private List<Entity> characters;

    private Map<String, Integer> gold;
    BitmapFont font;
    private InputHandler inputHandler;

    private List<UiSquare> uiSquares;

    public RenderScreen(PixelWars game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Initialize camera, viewport, and background
        initCameraAndViewport();
        initBackground();
        initCharacters();
        initUiSquares();
        scheduleCustomEnemySpawning();
        gold = new HashMap<>();
        gold.put("gold", 10000);
        scheduleGoldIncrement();
        inputHandler = new InputHandler(characters);
        gameUI = new GameUI(camera);
        inputHandler = new InputHandler(characters);
    }

    private void initCameraAndViewport() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();
        Gdx.gl.glClearColor(1, 1, 1, 1);
    }

    private void initBackground() {
        backgroundTexture = new Texture("background.png");
        font = new BitmapFont();
    }

    private void initCharacters() {
        characters = new ArrayList<>();
        Entity chicken = EntityFactory.createEntity(EntityType.CHICKEN, EntityTeam.PLAYER, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        characters.add(chicken);
    }

    private void initUiSquares() {
        uiSquares = new ArrayList<>();
        uiSquares.add(new UiSquare("uiSquare.png", 10, 10, 50, 50, "A"));
        uiSquares.add(new UiSquare("uiSquare.png", 70, 10, 50, 50, "S"));
        uiSquares.add(new UiSquare("uiSquare.png", 130, 10, 50, 50, "D"));
        uiSquares.add(new UiSquare("uiSquare.png", 190, 10, 50, 50, "left"));
        uiSquares.add(new UiSquare("uiSquare.png", 250, 10, 50, 50, "right"));
    }

    private void scheduleGoldIncrement() {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                incrementGold(10); // Increment the gold by 10 every second
            }
        }, 1, 1); // 1 initial delay, 1 second interval between increments
    }

    public void incrementGold(int amount) {
        int localGold = gold.get("gold");
        gold.put("gold", localGold + amount);
        gameUI.setGold(localGold + amount);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle input
        inputHandler.handleInput(camera, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), gold);
        gameUI.updatePositions(camera);

        // Update characters
        for (Entity character : characters) {
            character.update(delta, characters);
        }

        if (grenadierBehavior != null) {
            characters.addAll(grenadierBehavior.getPendingGrenades());
            grenadierBehavior.getPendingGrenades().clear();
        }

        // Remove dead characters
        Iterator<Entity> iterator = characters.iterator();
        while (iterator.hasNext()) {
            Entity character = iterator.next();
            if (character.isToBeRemoved()) {
                character.dispose();
                iterator.remove();
            }
        }

        // Set the batch projection matrix
        game.batch.setProjectionMatrix(camera.combined);

        // Begin the batch
        game.batch.begin();

        // Draw the background
        drawTiledBackground(game.batch);

        // Draw the UI squares
        gameUI.draw(game.batch);

        // Draw characters
        for (Entity character : characters) {
            character.draw(game.batch);
        }

        // End the batch
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Update the viewport
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        font.dispose();

        // Dispose of GameUI resources
        gameUI.dispose();

        // Dispose of the characters
        for (Entity character : characters) {
            character.dispose();
        }

        // Dispose of AssetManager resources
        AssetManagerUtil.dispose();
    }

    private void scheduleCustomEnemySpawning() {
//        scheduleEnemySpawning(EntityType.ABOMINATION, 15); // Spawn Abomination every 5 seconds
//        scheduleEnemySpawning(EntityType.DRONE, 5); // Spawn Grunt every 10 seconds
        scheduleEnemySpawning(EntityType.GRENADIER, 12);
    }

    private void scheduleEnemySpawning(final EntityType entityType, float interval) {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                int viewportWidth = Gdx.graphics.getWidth();
                int viewportHeight = Gdx.graphics.getHeight();
                Entity enemy = EntityFactory.createEntity(entityType, EntityTeam.ENEMY, viewportWidth, viewportHeight);
                if (entityType == EntityType.GRENADIER) {
                    grenadierBehavior = (GrenadierBehavior) enemy.getBehavior();
                }
                characters.add(enemy);
            }
        }, 0, interval);
    }

    private void drawTiledBackground(Batch batch) {
        float backgroundWidth = backgroundTexture.getWidth();
        float backgroundHeight = backgroundTexture.getHeight();
        int horizontalTiles = (int) Math.ceil(Gdx.graphics.getWidth() / backgroundWidth) + 1;
        int verticalTiles = (int) Math.ceil(Gdx.graphics.getHeight() / backgroundHeight) + 1;

        float startX = camera.position.x - camera.viewportWidth / 2;
        float startY = camera.position.y - camera.viewportHeight / 2;

        for (int i = 0; i < horizontalTiles; i++) {
            for (int j = 0; j < verticalTiles; j++) {
                float x = startX + i * backgroundWidth - startX % backgroundWidth;
                float y = startY + j * backgroundHeight - startY % backgroundHeight;
                batch.draw(backgroundTexture, x, y);
            }
        }
    }


}



