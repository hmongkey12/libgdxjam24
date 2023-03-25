package com.squashjam.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.squashjam.game.PixelWars;
import com.squashjam.game.entities.Entity;
import com.squashjam.game.enums.EntityTeam;
import com.squashjam.game.enums.EntityType;
import com.squashjam.game.factories.EntityFactory;
import com.squashjam.game.handlers.InputHandler;
import com.squashjam.game.utils.GameUI;
import com.squashjam.game.utils.UiSquare;

import java.util.*;
import java.util.stream.IntStream;


public class RenderScreen extends ScreenAdapter {
    private static final float VICTORY_TIME = 180;
    private float remainingVictoryTime;


    private static int GOLD_INCREMENT_AMOUNT = 8;
    private static int MAX_LEVEL = 4;

    private static int GOLD_START_AMOUNT = 1500;
    private static int DRONE_GOLD_REWARD = 100;
    private static int ABOMINATION_GOLD_REWARD = 150;

    private static int GRENADIER_GOLD_REWARD = 300;

    private static int ABOMINATION_SPAWN_INTERVAL = 10;
    private static int DRONE_SPAWN_INTERVAL = 5;
    private static int GRENADIER_SPAWN_INTERVAL = 15;

    private GameUI gameUI;
    private Texture foggyCircleTexture;
    private final PixelWars game;

    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture backgroundTexture;
    private List<Entity> entityList;

    private Map<String, Integer> gold;
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
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        remainingVictoryTime = VICTORY_TIME;
        initCameraAndViewport();
        initBackground();
        initEntities();
        scheduleCustomEnemySpawning();
        gold = new HashMap<>();
        gold.put("gold", GOLD_START_AMOUNT);
        foggyCircleTexture = assetManager.get("radial_circle.png");
        scheduleGoldIncrement();
        inputHandler = new InputHandler(entityList, assetManager);
        gameUI = new GameUI(camera, assetManager);
        blackTexture = assetManager.get("black.jpg");
        scheduleVictoryTimer();
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    // Convert screen coordinates to game world coordinates
                    Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));

                    for (Entity entity : entityList) {
                        if (entity.getTeam() == EntityTeam.PLAYER) {
                            for (UiSquare uiSquare : entity.getUiSquares()) {
                                if (uiSquare.checkClick(worldCoords.x, worldCoords.y)) {
                                    if (uiSquare.getLabel1().equalsIgnoreCase("upgrade")){
                                        // decrement it because entity level starts at 0, but level starts at 1
                                        int entityLevel = entity.getCurrentLevel() - 1;
                                        Integer[] upgradeCost = entity.getUpgradeCost();
                                        // Max level is the max of the levels, but the upgradeCost array starts at 0
                                        if (entityLevel < MAX_LEVEL - 1 &&  gold.get("gold") >= upgradeCost[entityLevel]) {
                                            int currentGold = gold.get("gold");
                                            gold.put("gold", currentGold - upgradeCost[entityLevel]);
                                            // increment by 2, to offset the decrement and to also increment by 1
                                            entity.setCurrentLevel(entityLevel + 2);
                                            int oldAttackDamage = entity.getAttackDamage();
                                            float oldAttackRange = entity.getAttackRange();
                                            float oldAttackCooldown = entity.getAttackCooldown();
                                            if (entity.getEntityType().equals(EntityType.SNIPER)) {
                                                entity.setAttackRange(oldAttackRange + (oldAttackRange * 10f));
                                                entity.setAttackDamage((int) (oldAttackDamage + (oldAttackDamage * 1.2f)));
                                            } else if (entity.getEntityType().equals(EntityType.GRUNT)) {
                                                entity.setAttackDamage(oldAttackDamage * 2);
                                                entity.setAttackCooldown(oldAttackCooldown * .9f);
                                            } else if (entity.getEntityType().equals(EntityType.DEMOLITIONIST)) {
                                                entity.setAttackCooldown(oldAttackCooldown * .8f);
                                                entity.setAttackDamage((int) (oldAttackDamage + (oldAttackDamage * 1.2f)));
                                            }
                                        }
                                    } else if (uiSquare.getLabel1().equalsIgnoreCase("sell")) {
                                        int currentGold = gold.get("gold");
                                        int sellPrice = entity.getSellPrice();
                                        entity.setToBeRemoved(true);
                                        gold.put("gold", currentGold + sellPrice);
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
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

    private void initBackground() {
        backgroundTexture = new Texture("background.png");
        font = new BitmapFont();
    }

    private void initEntities() {
        entityList = new ArrayList<>();
        Entity chicken = EntityFactory.createEntity(EntityType.CHICKEN, EntityTeam.PLAYER, (int) viewport.getWorldWidth(), (int) viewport.getWorldHeight(), game.assetManager);
        entityList.add(chicken);
    }

    private void scheduleGoldIncrement() {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                incrementGold(GOLD_INCREMENT_AMOUNT);
            }
        }, 1, 1);
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

        // the game over time
        remainingVictoryTime -= delta;
        // UI has a game over time to display
        gameUI.setTimeRemaining(remainingVictoryTime);

        // Verifies if there is a Chicken left, if not, its game over
        if (entityList.stream().filter(entity -> entity.getEntityType().equals(EntityType.CHICKEN)).count() < 1) {
            game.setScreen(new OutroScreen(game, EntityType.CHICKEN));
        }

        // Handle input, handles the input for entity creation
        inputHandler.handleInput(camera, delta, (int) viewport.getWorldWidth(), (int) viewport.getWorldHeight(), gold);
        // keeps the camera moving with the UI
        gameUI.updatePositions(camera);

        // mousePosition is needed to move the camera left or right in the game world
        Vector3 mousePosition = new Vector3(Gdx.input.getX(), 0, 0);
        camera.unproject(mousePosition);

        // update entities
        for (Entity entity : entityList) {
            entity.update(delta, entityList, mousePosition);
        }

        // Remove dead entities if they are marked for removal
        removeDeadEntities();

        game.batch.setProjectionMatrix(camera.combined);

        // draw the background
        game.batch.begin();
        drawTiledBackground(game.batch);
        game.batch.end();

        // draw the foggy circles around each player entity
        game.batch.begin();
        drawDarkenedRectangle(game.batch);
        drawFoggyCircles(game.batch);
        game.batch.end();


        // draw the entity and the tiled background after it has been covered by the black background and the foggy mask
        // needed for the fog of war effect
        game.batch.begin();
        game.batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);
        drawTiledBackground(game.batch);
        game.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        drawVisibleEntities(game.batch);
        entityList.stream()
                .filter(entity -> entity.getTeam() == EntityTeam.PLAYER)
                .flatMap(entity -> entity.getUiSquares().stream())
                .forEach(uiSquare -> uiSquare.draw(game.batch, font));
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
        entityList.forEach(Entity::dispose);
    }

    @Override
    public void hide() {
        dispose();
    }

    private void scheduleCustomEnemySpawning() {
        scheduleEnemySpawning(EntityType.ABOMINATION, 0, ABOMINATION_SPAWN_INTERVAL);
        scheduleEnemySpawning(EntityType.DRONE, ABOMINATION_SPAWN_INTERVAL, DRONE_SPAWN_INTERVAL);
        scheduleEnemySpawning(EntityType.GRENADIER, ABOMINATION_SPAWN_INTERVAL + DRONE_SPAWN_INTERVAL, GRENADIER_SPAWN_INTERVAL);
    }

    private void scheduleEnemySpawning(final EntityType entityType, float initialDelay, float interval) {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                Entity enemy = EntityFactory.createEntity(entityType, EntityTeam.ENEMY, (int) viewport.getWorldWidth(), (int) viewport.getWorldHeight(), game.assetManager);
                entityList.add(enemy);
            }
        }, initialDelay, interval);
    }


    private void drawTiledBackground(Batch batch) {
        float backgroundWidth = backgroundTexture.getWidth();
        float backgroundHeight = backgroundTexture.getHeight();
        int horizontalTiles = (int) Math.ceil(viewport.getWorldWidth() / backgroundWidth) + 1;
        int verticalTiles = (int) Math.ceil(viewport.getWorldHeight() / backgroundHeight) + 1;

        float startX = camera.position.x - camera.viewportWidth / 2;
        float startY = camera.position.y - camera.viewportHeight / 2;

        IntStream.range(0, horizontalTiles)
                .boxed()
                .flatMap(horizontalIndex -> IntStream.range(0, verticalTiles)
                        .mapToObj(verticalIndex -> new int[]{horizontalIndex, verticalIndex}))
                .forEach(coords -> {
                    int horizontalIndex = coords[0];
                    int verticalIndex = coords[1];
                    float x = startX + horizontalIndex * backgroundWidth - startX % backgroundWidth;
                    float y = startY + verticalIndex * backgroundHeight - startY % backgroundHeight;
                    batch.draw(backgroundTexture, x, y);
                });
    }

    private boolean isEntityInPlayerVisibility(Entity player, Entity other, float visibilityRadius) {
        float distance = player.getPosition().dst(other.getPosition());
        return distance <= visibilityRadius;
    }

    private void drawFoggyCircles(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        entityList.stream()
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

    private void removeDeadEntities() {
        Iterator<Entity> iterator = entityList.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity.isToBeRemoved()) {
                entity.dispose();
                iterator.remove();
                if (entity.getTeam() == EntityTeam.ENEMY && !isKilledByChicken(entity)) {
                    incrementGold(getGoldReward(entity));
                }
            }
        }
    }

    private int getGoldReward(Entity enemy) {
        switch (enemy.getEntityType()) {
            case ABOMINATION:
                return ABOMINATION_GOLD_REWARD;
            case DRONE:
                return DRONE_GOLD_REWARD;
            case GRENADIER:
                return GRENADIER_GOLD_REWARD;
            default:
                return 0;
        }
    }

    private boolean isVisible(Entity enemy) {
        return enemy.getEntityType() == EntityType.BUNNY || enemy.getTeam() == EntityTeam.PLAYER ||
                entityList.stream()
                        .filter(entity -> entity.getTeam() == EntityTeam.PLAYER)
                        .anyMatch(entity -> isEntityInPlayerVisibility(entity, enemy, entity.getEntitySight()));
    }

    private void drawVisibleEntities(SpriteBatch batch) {
        for (Entity entity : entityList) {
            if (isVisible(entity)) {
                entity.draw(batch);
            }
        }
    }

    private void scheduleVictoryTimer() {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                game.setScreen(new OutroScreen(game, null));
            }
        }, VICTORY_TIME);
    }

    private boolean isKilledByChicken(Entity enemy) {
        return enemy.getLastAttack() != null && enemy.getLastAttack().equals(EntityType.CHICKEN);
    }
}



