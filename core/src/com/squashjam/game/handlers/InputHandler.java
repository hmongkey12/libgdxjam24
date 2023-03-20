package com.squashjam.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    private List<Entity> characters;
    private Map<Integer, Float> lastKeyPressTimes;

    private Texture movingTexture;
    private Texture idleTexture;
    private Texture attackTexture;
    private static final float DEBOUNCE_TIME = 0.2f;

    public InputHandler(List<Entity> characters) {
        this.characters = characters;

        // Initialize last key press times to current time
        lastKeyPressTimes = new HashMap<Integer, Float>();
    }

    public void handleInput(OrthographicCamera camera, float delta, int viewportWidth, int viewportHeight, Map<String, Integer> gold) {
        float currentTime = TimeUtils.nanoTime() / 1000000000f;
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
        camera.update();
    }

    private void updateCameraPosition(OrthographicCamera camera, float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.position.x -= 100 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.position.x += 100 * delta;
        }
        float halfViewportWidth = camera.viewportWidth / 2f;
        float maxX = 2000 - halfViewportWidth;
        float minX = halfViewportWidth;
        camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX);
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
        int localGold = gold.get("gold");
        if (localGold >= 100) {
            gold.put("gold", localGold - 100);
            Entity newCharacter = EntityFactory.createEntity(EntityType.GRUNT, EntityTeam.PLAYER, viewportWidth, viewportHeight);
            characters.add(newCharacter);
        }
    }

    private void summonSniper(Map<String, Integer> gold, int viewportWidth, int viewportHeight) {
        int localGold = gold.get("gold");
        int sniperCost = 200; // Twice the gold cost of a Grunt Entity
        if (localGold >= sniperCost) {
            gold.put("gold", localGold - sniperCost);
            Entity newCharacter = EntityFactory.createEntity(EntityType.SNIPER, EntityTeam.PLAYER, viewportWidth, viewportHeight);
            characters.add(newCharacter);
        }
    }

    private void summonDemolitionist(Map<String, Integer> gold, int viewportWidth, int viewportHeight) {
        int localGold = gold.get("gold");
        int demolitionistCost = 300; // Three times the gold cost of a Grunt Entity
        if (localGold >= demolitionistCost) {
            gold.put("gold", localGold - demolitionistCost);

//            EntityBehavior demolitionistBehavior = new DemolitionistBehavior();
//            movingTexture = AssetManagerUtil.get().get("moveleft.png", Texture.class);
//            idleTexture = AssetManagerUtil.get().get("idle.png", Texture.class);
//            attackTexture = AssetManagerUtil.get().get("attack1.png", Texture.class);

            Entity newCharacter = EntityFactory.createEntity(EntityType.DEMOLITIONIST,EntityTeam.PLAYER, viewportWidth, viewportHeight);
//            Entity newCharacter = new Entity(EntityType.DEMOLITIONIST,
//                    movingTexture, idleTexture, attackTexture,
//                    4, 2, 0.1f,
//                    new Vector2(0, 0), 100,
//                    EntityTeam.PLAYER, 50, 50, 10, 1, viewportWidth, viewportHeight, demolitionistBehavior);
            characters.add(newCharacter);
        }
    }
}



