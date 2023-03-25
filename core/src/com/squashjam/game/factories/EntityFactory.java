package com.squashjam.game.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.squashjam.game.behaviors.*;
import com.squashjam.game.entities.Entity;
import com.squashjam.game.entities.EntityBehavior;
import com.squashjam.game.entities.HealthBar;
import com.squashjam.game.enums.EntityState;
import com.squashjam.game.enums.EntityTeam;
import com.squashjam.game.enums.EntityType;
import com.squashjam.game.utils.UiSquare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityFactory {
    private static final int PLAYER_ENTITY_YPOS = 200;

    public static Entity createEntity(EntityType entityType, EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        switch (entityType) {
            case ABOMINATION:
                return createAbomination(team, viewportWidth, viewportHeight, assetManager);
            case DRONE:
                return createDrone(team, viewportWidth, viewportHeight, assetManager);
            case GRENADIER:
                return createGrenadier(team, viewportWidth, viewportHeight, assetManager);
            case CHICKEN:
                return createChickenCharacter(team, viewportWidth, viewportHeight, assetManager);
            case GRUNT:
                return createGrunt(team, viewportWidth, viewportHeight, assetManager);
            case SNIPER:
                return createSniper(team, viewportWidth, viewportHeight, assetManager);
            case DEMOLITIONIST:
                return createDemolitionist(team, viewportWidth, viewportHeight, assetManager);
            case BUNNY:
                return createBunny(team, viewportWidth, viewportHeight, assetManager);
            default:
                throw new IllegalArgumentException("Unsupported enemy type: " + entityType);
        }
    }

    private static Entity createBasicEntity(EntityType entityType, EntityTeam team, int viewportWidth, int viewportHeight, Vector2 startPosition, int startHealth, int maxHealth, float speed, float attackRange, int attackDamage, float attackCooldown, EntityBehavior behavior, Array<Texture> textures, int frameCols, int frameRows, float frameDuration, float entitySight, Sound attackSound, AssetManager assetManager, int[] upgradeCost) {
        Texture movingTexture = textures.get(0);
        Texture idleTexture = textures.get(1);
        Texture attackTexture = textures.get(2);
        Integer currentLevel = 1;
        float entityWidth = viewportWidth * 0.3f;
        float entityHeight = viewportHeight * 0.35f;
        HealthBar healthBar;
        List<UiSquare> uiSquares = new ArrayList<>();
        if (team == EntityTeam.PLAYER && !entityType.equals(EntityType.CHICKEN)) {
            UiSquare uiSquare1 = new UiSquare("black.jpg", startPosition.x, startPosition.y + entityHeight * 1.1f, 60, 60, "Upgrade", "Up", assetManager);
            UiSquare uiSquare2 = new UiSquare("black.jpg", startPosition.x + entityWidth + 70, startPosition.y + entityHeight * 1.1f, 60, 60, "Sell", "Price", assetManager);
            UiSquare uiSquare3 = new UiSquare("black.jpg", startPosition.x + entityWidth + 140, startPosition.y + entityHeight * 1.1f, 60, 60, "Level", currentLevel.toString() , assetManager);
            uiSquares.add(uiSquare1);
            uiSquares.add(uiSquare2);
            uiSquares.add(uiSquare3);
            healthBar = null;
        } else {
           healthBar = HealthBarFactory.createHealthBar(entityWidth, entityHeight, startPosition);
        }

        return Entity.builder()
                .entityType(entityType)
                .textures(textures)
                .uiSquares(uiSquares)
                .movingAnimation(Entity.createMovingAnimation(movingTexture, frameCols, frameRows, frameDuration))
                .idleAnimation(Entity.createIdleAnimation(idleTexture, 1, 1, frameDuration))
                .attackAnimation(Entity.createAttackAnimation(attackTexture, frameCols, frameRows, frameDuration))
                .position(startPosition)
                .health(startHealth)
                .maxHealth(maxHealth)
                .upgradeCost(upgradeCost)
                .team(team)
                .currentLevel(currentLevel)
                .attackSound(attackSound)
                .state(EntityState.IDLE)
                .speed(speed)
                .entitySight(entitySight)
                .attackRange(attackRange)
                .attackDamage(attackDamage)
                .attackCooldown(attackCooldown)
                .entityWidth(entityWidth)
                .entityHeight(entityHeight)
                .behavior(behavior)
                .healthBar(healthBar)
                .build();
    }

    private static Entity createAbomination(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"abomination_move.png", "abomination_idle.png", "abomination_attack.png"};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        Sound attackSound = assetManager.get("punch.mp3");
        return createBasicEntity(EntityType.ABOMINATION, team, viewportWidth, viewportHeight, new Vector2(2000, 0), 600, 600, 100, 100, 100, 2f, new AbominationBehavior(), textures, 3, 2, 0.2f, 100, attackSound, assetManager, null);
    }

    private static Entity createDrone(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"drone_move.png", "drone_idle.png", "drone_attack.png"};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        Sound attackSound = assetManager.get("punch.mp3");
        return createBasicEntity(EntityType.DRONE, team, viewportWidth, viewportHeight, new Vector2(2000, 0), 1000, 1000, 300, 300, 100, 2f, new DroneBehavior(), textures, 3, 2, 0.2f, 100, attackSound, assetManager, null);
    }

    private static Entity createGrenadier(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"grenadier_move.png", "grenadier_idle.png", "grenade_attack.png",};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        Sound attackSound = assetManager.get("plant.mp3");
        return createBasicEntity(EntityType.GRENADIER, team, viewportWidth, viewportHeight, new Vector2(2000, 0), 2000, 2000, 50, 300, 500, 2f, new GrenadierBehavior(), textures, 4, 2, 0.2f, 100, attackSound, assetManager, null);
    }

    public static Entity createChickenCharacter(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"chicken_idle.png", "chicken_idle.png", "chicken_attack.png"};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        Sound attackSound = assetManager.get("punch.mp3");
        return createBasicEntity(EntityType.CHICKEN, team, viewportWidth, viewportHeight, new Vector2(10, 0), 1000, 1000, 0, 300, 500, 1f, new TowerBehavior(), textures, 4, 2, 0.1f, 800, attackSound, assetManager, null);
    }

    private static Entity createBunny(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"bunny_idle.png", "bunny_idle.png", "bunny_attack.png"};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        Sound attackSound = assetManager.get("punch.mp3");
        return createBasicEntity(EntityType.BUNNY, team, viewportWidth, viewportHeight, new Vector2(2000, 0), 1000, 1000, 0, 300, 50, 1f, new TowerBehavior(), textures, 3, 2, 0.1f, 800, attackSound, assetManager, null);
    }

    private static Entity createGrunt(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"grunt_move.png", "grunt_idle.png", "grunt_attack.png"};
        int[] upgradeCost = {500, 800, 1000, 1500};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        Sound attackSound = assetManager.get("slash.mp3");
        return createBasicEntity(EntityType.GRUNT, team, viewportWidth, viewportHeight, new Vector2(0, PLAYER_ENTITY_YPOS), 150, 150, 50, 300, 100, 1f, new GruntBehavior(), textures, 3, 2, 0.1f, 300, attackSound, assetManager, upgradeCost);
    }

    private static Entity createSniper(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"sniper_move.png", "sniper_idle.png", "sniper_attack.png"};
        int[] upgradeCost = {500, 800, 1000, 1500};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        Sound attackSound = assetManager.get("shoot.mp3");
        return createBasicEntity(EntityType.SNIPER, team, viewportWidth, viewportHeight, new Vector2(0, PLAYER_ENTITY_YPOS), 100, 100, 50, 800, 30, 1f, new SniperBehavior(), textures, 3, 2, 0.1f, 800, attackSound, assetManager, upgradeCost);
    }

    private static Entity createDemolitionist(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"demolition_move.png", "demolition_idle.png", "demolition_attack.png",};
        int[] upgradeCost = {500, 800, 1000, 1500};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        Sound attackSound = assetManager.get("plant.mp3");
        return createBasicEntity(EntityType.DEMOLITIONIST, team, viewportWidth, viewportHeight, new Vector2(0, PLAYER_ENTITY_YPOS), 150, 150, 50, 300, 30, 1f, new DemolitionistBehavior(), textures, 3, 2, 0.1f, 300, attackSound, assetManager, upgradeCost);
    }

    private static Texture[] loadTextures(String[] texturePaths, AssetManager assetManager) {
        return Arrays.stream(texturePaths)
                .map(assetManager::get)
                .toArray(Texture[]::new);
    }
}
