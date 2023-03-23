package com.squashjam.game.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.squashjam.game.behaviors.*;
import com.squashjam.game.entities.Entity;
import com.squashjam.game.entities.EntityBehavior;
import com.squashjam.game.enums.EntityState;
import com.squashjam.game.enums.EntityTeam;
import com.squashjam.game.enums.EntityType;

import java.util.Arrays;

public class EntityFactory {

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
            default:
                throw new IllegalArgumentException("Unsupported enemy type: " + entityType);
        }
    }

    private static Entity createBasicEntity(EntityType entityType, EntityTeam team, int viewportWidth, int viewportHeight, Vector2 startPosition, int startHealth, int maxHealth, float speed, float attackRange, int attackDamage, float attackCooldown, EntityBehavior behavior, Array<Texture> textures, int frameCols, int frameRows, float frameDuration, float entitySight) {
        Texture movingTexture = textures.get(0);
        Texture idleTexture = textures.get(1);
        Texture attackTexture = textures.get(2);
        float entityWidth = viewportWidth * 0.3f;
        float entityHeight = viewportHeight * 0.35f;


        return Entity.builder()
                .entityType(entityType)
                .textures(textures)
                .movingAnimation(Entity.createMovingAnimation(movingTexture, frameCols, frameRows, frameDuration))
                .idleAnimation(Entity.createIdleAnimation(idleTexture, 1, 1, frameDuration))
                .attackAnimation(Entity.createAttackAnimation(attackTexture, frameCols, frameRows, frameDuration))
                .position(startPosition)
                .health(startHealth)
                .maxHealth(maxHealth)
                .team(team)
                .state(EntityState.IDLE)
                .speed(speed)
                .entitySight(entitySight)
                .attackRange(attackRange)
                .attackDamage(attackDamage)
                .attackCooldown(attackCooldown)
                .entityWidth(entityWidth)
                .entityHeight(entityHeight)
                .behavior(behavior)
                .healthBar(HealthBarFactory.createHealthBar(entityWidth, entityHeight, startPosition))
                .build();
    }

    private static Entity createAbomination(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"abomination_move.png", "abomination_idle.png", "abomination_attack.png"};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        return createBasicEntity(EntityType.ABOMINATION, team, viewportWidth, viewportHeight, new Vector2(1600, 0), 800, 800, 80, 50, 50, 2f, new AbominationBehavior(), textures, 3, 2, 0.2f, 100);
    }

    private static Entity createDrone(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"drone_move.png", "drone_idle.png", "drone_attack.png"};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        return createBasicEntity(EntityType.DRONE, team, viewportWidth, viewportHeight, new Vector2(1600, 0), 100, 100, 150, 50, 10, 0.5f, new DroneBehavior(), textures, 3, 2, 0.2f, 100);
    }

    private static Entity createGrenadier(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"grenadier_move.png", "grenadier_idle.png", "grenadier_attack.png",};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        return createBasicEntity(EntityType.GRENADIER, team, viewportWidth, viewportHeight, new Vector2(1600, 0), 100, 100, 50, 50, 100, 2f, new GrenadierBehavior(assetManager), textures, 4, 2, 0.2f, 100);
    }

    public static Entity createChickenCharacter(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"chicken_idle.png", "chicken_idle.png", "chicken_attack.png"};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        return createBasicEntity(EntityType.CHICKEN, team, viewportWidth, viewportHeight, new Vector2(10, 0), 100, 100, 0, 50, 100, 1f, new ChickenBehavior(), textures, 4, 2, 0.1f, 300);
    }

    private static Entity createGrunt(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"grunt_move.png", "grunt_idle.png", "grunt_attack.png"};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        return createBasicEntity(EntityType.GRUNT, team, viewportWidth, viewportHeight, new Vector2(0, 0), 100, 100, 50, 50, 10, 1f, new GruntBehavior(), textures, 3, 2, 0.1f, 200);
    }

    private static Entity createSniper(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"sniper_move.png", "sniper_idle.png", "sniper_attack.png"};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        return createBasicEntity(EntityType.SNIPER, team, viewportWidth, viewportHeight, new Vector2(0, 0), 100, 100, 50, 300, 10, 1f, new SniperBehavior(), textures, 3, 2, 0.1f, 400);
    }

    private static Entity createDemolitionist(EntityTeam team, int viewportWidth, int viewportHeight, AssetManager assetManager) {
        String[] texturePaths = {"demolition_move.png", "demolition_idle.png", "demolition_attack.png",};
        Array<Texture> textures = Array.with(loadTextures(texturePaths, assetManager));
        return createBasicEntity(EntityType.DEMOLITIONIST, team, viewportWidth, viewportHeight, new Vector2(0, 0), 100, 100, 50, 50, 10, 1f, new DemolitionistBehavior(), textures, 3, 2, 0.1f, 200);
    }

    private static Texture[] loadTextures(String[] texturePaths, AssetManager assetManager) {
        return Arrays.stream(texturePaths)
                .map(assetManager::get)
                .toArray(Texture[]::new);
    }
}
