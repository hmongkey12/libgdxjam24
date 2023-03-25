package com.squashjam.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.squashjam.game.entities.Entity;
import com.squashjam.game.enums.EntityTeam;
import com.squashjam.game.enums.EntityType;
import com.squashjam.game.factories.EntityFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputHandler {
    private final List<Entity> entityList;
    private Map<Integer, Float> lastKeyPressTimes;

    private static final float DEBOUNCE_TIME = 0.2f;
    private static final float NANO_TO_SECONDS = 1000000000f;

    private static final float CAMERA_SPEED = 0.8f;
    private static final int GAME_WORLD_WIDTH = 2200;
    private static final int GRUNT_COST = 200;
    private static final int SNIPER_COST = 500;
    private static final int DEMOLITIONIST_COST = 300;
    private static final int GRUNT_REFUND = 50;
    private static final int SNIPER_REFUND = 250;
    private static final int DEMOLITIONIST_REFUND = 100;
    private static final float ENTITY_WIDTH_SCALE = 0.30f;
    private Entity followingMouseEntity;

    AssetManager assetManager;

    public InputHandler(List<Entity> entityList, AssetManager assetManager) {
        this.assetManager = assetManager;
        this.entityList = entityList;
        // Initialize last key press times to current time
        lastKeyPressTimes = new HashMap<Integer, Float>();
    }

    public void handleInput(OrthographicCamera camera, float delta, int viewportWidth, int viewportHeight, Map<String, Integer> gold) {
        float currentTime = TimeUtils.nanoTime() / NANO_TO_SECONDS;
        updateCameraPosition(camera, delta);

        int[] keys = {Input.Keys.A, Input.Keys.S, Input.Keys.D};
        for (int key : keys) {
            if (Gdx.input.isKeyPressed(key)) {
                Float lastKeyPressTime = lastKeyPressTimes.containsKey(key) ? lastKeyPressTimes.get(key) : 0f;
                if (currentTime - lastKeyPressTime >= DEBOUNCE_TIME) {
                    handleKeyPress(key, gold, viewportWidth, viewportHeight);
                    lastKeyPressTimes.put(key, currentTime);
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (followingMouseEntity != null) {
                followingMouseEntity.setToBeRemoved(true);
                refundGold(followingMouseEntity, gold);
                followingMouseEntity = null;
            }
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (followingMouseEntity != null) {
                if (!isConflictingPosition(followingMouseEntity, (followingMouseEntity.getEntityWidth() * ENTITY_WIDTH_SCALE))) {
                    followingMouseEntity.setFollowingMouse(false);
                    followingMouseEntity = null;
                } else {
                    // TODO: show entity can't be placed
                }
            }
        }
        camera.update();
    }

    private void updateCameraPosition(OrthographicCamera camera, float delta) {
        float mouseX = Gdx.input.getX() * 2200f / Gdx.graphics.getWidth(); // convert mouse X to game world coordinates
        float halfViewportWidth = camera.viewportWidth / 2f;
        float maxX = GAME_WORLD_WIDTH - halfViewportWidth;
        float minX = halfViewportWidth;
        float targetX = MathUtils.clamp(mouseX, minX, maxX);
        camera.position.x += (targetX - camera.position.x) * delta * CAMERA_SPEED;
        camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX); // clamp camera position, 0 is the min and 2200 is max
    }


    private void handleKeyPress(int key, Map<String, Integer> gold, int viewportWidth, int viewportHeight) {
        switch (key) {
            case Input.Keys.A:
                summonGrunt(gold, viewportWidth, viewportHeight);
                break;
            case Input.Keys.S:
                summonSniper(gold, viewportWidth, viewportHeight);
                break;
            case Input.Keys.D:
                summonDemolitionist(gold, viewportWidth, viewportHeight);
                break;
        }
    }

    private void summonGrunt(Map<String, Integer> gold, int viewportWidth, int viewportHeight) {
        if (followingMouseEntity == null) {
            int localGold = gold.get("gold");
            int gruntCost = GRUNT_COST;
            if (localGold >= gruntCost) {
                gold.put("gold", localGold - gruntCost);
                followingMouseEntity = EntityFactory.createEntity(EntityType.GRUNT, EntityTeam.PLAYER, viewportWidth, viewportHeight, assetManager);
                followingMouseEntity.setFollowingMouse(true);
                entityList.add(followingMouseEntity);
            }
        }
    }

    private void summonSniper(Map<String, Integer> gold, int viewportWidth, int viewportHeight) {
        if (followingMouseEntity == null) {
            int localGold = gold.get("gold");
            int sniperCost = SNIPER_COST;
            if (localGold >= sniperCost) {
                gold.put("gold", localGold - sniperCost);
                followingMouseEntity = EntityFactory.createEntity(EntityType.SNIPER, EntityTeam.PLAYER, viewportWidth, viewportHeight, assetManager);
                followingMouseEntity.setFollowingMouse(true);
                entityList.add(followingMouseEntity);
            }
        }
    }

    private void summonDemolitionist(Map<String, Integer> gold, int viewportWidth, int viewportHeight) {
        if (followingMouseEntity == null) {
            int localGold = gold.get("gold");
            int demolitionistCost = DEMOLITIONIST_COST;
            if (localGold >= demolitionistCost) {
                gold.put("gold", localGold - demolitionistCost);
                followingMouseEntity = EntityFactory.createEntity(EntityType.DEMOLITIONIST, EntityTeam.PLAYER, viewportWidth, viewportHeight, assetManager);
                followingMouseEntity.setFollowingMouse(true);
                entityList.add(followingMouseEntity);
            }
        }
    }

    private boolean isConflictingPosition(Entity newEntity, float minDistance) {
        for (Entity existingEntity : entityList) {
            if (existingEntity == newEntity) continue; // don't check self
            float distance = Math.abs(newEntity.position.x - existingEntity.position.x);
            if (distance < minDistance) {
                return true;
            }
        }
        return false;
    }

    private void refundGold(Entity entity, Map<String, Integer> gold) {
        int cost = 0;
        switch (entity.getEntityType()) {
            case GRUNT:
                cost = GRUNT_REFUND;
                break;
            case SNIPER:
                cost = SNIPER_REFUND;
                break;
            case DEMOLITIONIST:
                cost = DEMOLITIONIST_REFUND;
                break;
        }
        gold.put("gold", gold.get("gold") + cost);
    }
}



