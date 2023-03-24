package com.squashjam.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.squashjam.game.utils.GameUI;

import java.util.*;
import java.util.stream.IntStream;


public class RenderScreen extends ScreenAdapter {

    private GrenadierBehavior grenadierBehavior;

    private GameUI gameUI;
    private Texture foggyCircleTexture;
    private final PixelWars game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture backgroundTexture;
    private List<Entity> characters;

    private Map<String, Integer> gold;
    BitmapFont font;
    private InputHandler inputHandler;

    private Texture blackTexture;
    private AssetManager assetManager;

    public RenderScreen(PixelWars game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Initialize camera, viewport, and background
        this.assetManager = game.assetManager;
        initCameraAndViewport();
        initBackground();
        initCharacters();
        scheduleCustomEnemySpawning();
        gold = new HashMap<>();
        gold.put("gold", 10000);
        foggyCircleTexture = assetManager.get("radial_circle.png");
        scheduleGoldIncrement();
        inputHandler = new InputHandler(characters, assetManager);
        gameUI = new GameUI(camera, assetManager);
        blackTexture = assetManager.get("black.jpg");
    }

    private void initCameraAndViewport() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(1200, 800, camera);
        camera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
        viewport.apply();
        Gdx.gl.glClearColor(1, 1, 1, 1);
    }

    private void initBackground() {
        backgroundTexture = new Texture("background.png");
        font = new BitmapFont();
    }

    private void initCharacters() {
        characters = new ArrayList<>();
        Entity chicken = EntityFactory.createEntity(EntityType.CHICKEN, EntityTeam.PLAYER, (int) viewport.getWorldWidth(), (int) viewport.getWorldHeight(), game.assetManager);
        characters.add(chicken);

        Entity bunny = EntityFactory.createEntity(EntityType.BUNNY, EntityTeam.ENEMY, (int) viewport.getWorldWidth(), (int) viewport.getWorldHeight(), game.assetManager);
        characters.add(bunny);
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (characters.stream().filter(character -> character.getEntityType().equals(EntityType.CHICKEN)).count() < 1) {
            game.setScreen(new DefeatOutroScreen(game));
        }

        if (characters.stream().filter(character -> character.getEntityType().equals(EntityType.BUNNY)).count() < 1) {
            game.setScreen(new VictoryOutroScreen(game));
        }

        // Handle input
        inputHandler.handleInput(camera, delta, (int) viewport.getWorldWidth(), (int) viewport.getWorldHeight(), gold);
        gameUI.updatePositions(camera);

        // Update characters
        for (Entity character : characters) {
            character.update(delta, characters);
        }

        // Update grenades
        if (grenadierBehavior != null) {
            characters.addAll(grenadierBehavior.getPendingGrenades());
            grenadierBehavior.getPendingGrenades().clear();
        }

        // Remove dead characters
        removeDeadCharacters();

        // Set the batch projection matrix
        game.batch.setProjectionMatrix(camera.combined);

        // Begin the batch
        game.batch.begin();
//         Draw the background
        drawTiledBackground(game.batch);
        game.batch.end();

        // Draw the foggy circles around each player entity
        game.batch.begin();
        drawDarkenedRectangle(game.batch);
        drawFoggyCircles(game.batch);
        game.batch.end();

        game.batch.begin();
        game.batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);
        drawTiledBackground(game.batch);
        game.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        drawVisibleEntities(game.batch);
        gameUI.draw(game.batch);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        font.dispose();
        gameUI.dispose();
        characters.forEach(Entity::dispose);
    }

    @Override
    public void hide() {
        dispose();
    }

    private void scheduleCustomEnemySpawning() {
//        scheduleEnemySpawning(EntityType.ABOMINATION, 20);
        scheduleEnemySpawning(EntityType.DRONE, 10);
//        scheduleEnemySpawning(EntityType.GRENADIER, 10);
    }

    private void scheduleEnemySpawning(final EntityType entityType, float interval) {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                Entity enemy = EntityFactory.createEntity(entityType, EntityTeam.ENEMY, (int) viewport.getWorldWidth(), (int) viewport.getWorldHeight(), game.assetManager);
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
        int horizontalTiles = (int) Math.ceil(viewport.getWorldWidth() / backgroundWidth) + 1;
        int verticalTiles = (int) Math.ceil(viewport.getWorldHeight() / backgroundHeight) + 1;

        float startX = camera.position.x - camera.viewportWidth / 2;
        float startY = camera.position.y - camera.viewportHeight / 2;

        IntStream.range(0, horizontalTiles)
                .forEach(i -> IntStream.range(0, verticalTiles)
                        .forEach(j -> {
                            float x = startX + i * backgroundWidth - startX % backgroundWidth;
                            float y = startY + j * backgroundHeight - startY % backgroundHeight;
                            batch.draw(backgroundTexture, x, y);
                        }));
    }

    private boolean isEntityInPlayerVisibility(Entity player, Entity other, float visibilityRadius) {
        float distance = player.getPosition().dst(other.getPosition());
        return distance <= visibilityRadius;
    }

    private void drawFoggyCircles(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        characters.stream()
                .filter(c -> c.getTeam() == EntityTeam.PLAYER)
                .forEach(c -> {
                    float squareWidth = c.getEntitySight() + c.getEntityWidth();
                    float squareHeight = c.getEntitySight() + c.getEntityHeight();
                    float squareXPos = c.getPosition().x;
                    float squareYPos = c.getPosition().y;
                    batch.draw(foggyCircleTexture, squareXPos, squareYPos, squareWidth, squareHeight * .5f);
                });
    }

    private void drawDarkenedRectangle(SpriteBatch batch) {
        // Draw a semi-transparent black texture over the entire screen
        batch.setColor(.1f, .1f, .15f, 0.9f);
        batch.draw(blackTexture, 0, 0, viewport.getWorldWidth() + 2000, viewport.getWorldHeight());
        batch.setColor(Color.WHITE); // Reset the color
    }

    private void removeDeadCharacters() {
        Iterator<Entity> iterator = characters.iterator();
        while (iterator.hasNext()) {
            Entity character = iterator.next();
            if (character.isToBeRemoved()) {
                character.dispose();
                iterator.remove();
            }
        }
    }

    private boolean isVisible(Entity enemy) {
        return enemy.getEntityType() == EntityType.BUNNY || enemy.getTeam() == EntityTeam.PLAYER ||
                characters.stream()
                        .filter(character -> character.getTeam() == EntityTeam.PLAYER)
                        .anyMatch(character -> isEntityInPlayerVisibility(character, enemy, character.getEntitySight()));
    }

    private void drawVisibleEntities(SpriteBatch batch) {
        for (Entity character : characters) {
            if (isVisible(character)) {
                character.draw(batch);
            }
        }
    }
}



