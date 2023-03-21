package com.squashjam.game.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.squashjam.game.behaviors.*;
import com.squashjam.game.entities.Entity;
import com.squashjam.game.entities.EntityBehavior;
import com.squashjam.game.enums.EntityState;
import com.squashjam.game.enums.EntityTeam;
import com.squashjam.game.enums.EntityType;
import com.squashjam.game.utils.AssetManagerUtil;

public class EntityFactory {

    public static Entity createEntity(EntityType entityType, EntityTeam team, int viewportWidth, int viewportHeight) {
        switch (entityType) {
            case ABOMINATION:
                return createAbomination(team, viewportWidth, viewportHeight);
            case DRONE:
                return createDrone(team, viewportWidth, viewportHeight);
            case GRENADIER:
                return createGrenadier(team, viewportWidth, viewportHeight);
            case CHICKEN:
                return createChickenCharacter(team, viewportWidth, viewportHeight);
            case GRUNT:
                return createGrunt(team, viewportWidth, viewportHeight);
            case SNIPER:
                return createSniper(team, viewportWidth, viewportHeight);
            case DEMOLITIONIST:
                return createDemolitionist(team, viewportWidth, viewportHeight);
            default:
                throw new IllegalArgumentException("Unsupported enemy type: " + entityType);
        }
    }

    private static Entity createBasicEntity(EntityType entityType, EntityTeam team, int viewportWidth, int viewportHeight, Vector2 startPosition, int startHealth, int maxHealth, float speed, float attackRange, int attackDamage, float attackCooldown, EntityBehavior behavior, Array<Texture> textures, int frameCols, int frameRows, float frameDuration) {
        Texture movingTexture = textures.get(0);
        Texture idleTexture = textures.get(1);
        Texture attackTexture = textures.get(2);
        float entityWidth = viewportWidth * 0.1f;
        float entityHeight = viewportHeight * 0.15f;

        return Entity.builder()
                .entityType(entityType)
                .textures(textures)
                .movingAnimation(Entity.createMovingAnimation(movingTexture, frameCols, frameRows, frameDuration))
                .idleAnimation(Entity.createIdleAnimation(idleTexture, frameCols, frameRows, frameDuration))
                .attackAnimation(Entity.createAttackAnimation(attackTexture, frameCols, frameRows, frameDuration))
                .position(startPosition)
                .health(startHealth)
                .maxHealth(maxHealth)
                .team(team)
                .state(EntityState.IDLE)
                .speed(speed)
                .collisionCircle(new Circle())
                .attackRange(attackRange)
                .attackDamage(attackDamage)
                .attackCooldown(attackCooldown)
                .entityWidth(entityWidth)
                .entityHeight(entityHeight)
                .behavior(behavior)
                .healthBar(HealthBarFactory.createHealthBar(entityWidth, entityHeight, startPosition))
                .build();
    }

    private static Entity createAbomination(EntityTeam team, int viewportWidth, int viewportHeight) {
        String[] texturePaths = {"abomination_walk.png", "idle.png", "abomination_attack.png"};
        Array<Texture> textures = Array.with(loadTextures(texturePaths));
        return createBasicEntity(EntityType.ABOMINATION, team, viewportWidth, viewportHeight, new Vector2(1600, 0), 800, 800, 20, 50, 50, 2f, new AbominationBehavior(), textures, 4, 2, 0.2f);
    }

    private static Entity createDrone(EntityTeam team, int viewportWidth, int viewportHeight) {
        String[] texturePaths = {"moveleft.png", "idle.png", "attack1.png"};
        Array<Texture> textures = Array.with(loadTextures(texturePaths));
        return createBasicEntity(EntityType.DRONE, team, viewportWidth, viewportHeight, new Vector2(1600, 0), 100, 100, 150, 50, 10, 0.5f, new DroneBehavior(), textures, 4, 2, 0.2f);
    }

    private static Entity createGrenadier(EntityTeam team, int viewportWidth, int viewportHeight) {
        String[] texturePaths = {"moveleft.png", "idle.png", "attack1.png",};
        Array<Texture> textures = Array.with(loadTextures(texturePaths));
        return createBasicEntity(EntityType.GRENADIER, team, viewportWidth, viewportHeight, new Vector2(1600, 0), 100, 100, 50, 50, 100, 2f, new GrenadierBehavior(), textures, 4, 2, 0.2f);
    }

    public static Entity createChickenCharacter(EntityTeam team, int viewportWidth, int viewportHeight) {
        String[] texturePaths = {"moveleft.png", "chicken_idle.png", "chicken_attack.png"};
        Array<Texture> textures = Array.with(loadTextures(texturePaths));
        return createBasicEntity(EntityType.CHICKEN, team, viewportWidth, viewportHeight, new Vector2(10, 0), 1000, 1000, 0, 50, 100, 1f, new ChickenBehavior(), textures, 4, 2, 0.1f);
    }

    private static Entity createGrunt(EntityTeam team, int viewportWidth, int viewportHeight) {
        String[] texturePaths = {"grunt_walk.png", "idle.png", "grunt_attack.png"};
        Array<Texture> textures = Array.with(loadTextures(texturePaths));
        return createBasicEntity(EntityType.GRUNT, team, viewportWidth, viewportHeight, new Vector2(0, 0), 100, 100, 50, 50, 10, 1f, new GruntBehavior(), textures, 4, 2, 0.1f);
    }

    private static Entity createSniper(EntityTeam team, int viewportWidth, int viewportHeight) {
        String[] texturePaths = {"sniper_walk.png", "idle.png", "sniper_attack.png"};
        Array<Texture> textures = Array.with(loadTextures(texturePaths));
        return createBasicEntity(EntityType.SNIPER, team, viewportWidth, viewportHeight, new Vector2(0, 0), 100, 100, 50, 300, 10, 1f, new SniperBehavior(), textures, 4, 2, 0.1f);
    }

    private static Entity createDemolitionist(EntityTeam team, int viewportWidth, int viewportHeight) {
        String[] texturePaths = {"moveright.png", "idle.png", "attack1.png",};
        Array<Texture> textures = Array.with(loadTextures(texturePaths));
        return createBasicEntity(EntityType.DEMOLITIONIST, team, viewportWidth, viewportHeight, new Vector2(0, 0), 100, 100, 50, 50, 10, 1f, new DemolitionistBehavior(), textures, 4, 2, 0.1f);
    }

    private static Texture[] loadTextures(String[] texturePaths) {
        AssetManager assetManager = AssetManagerUtil.get();
        Texture[] textures = new Texture[texturePaths.length];
        for (int i = 0; i < texturePaths.length; i++) {
            textures[i] = assetManager.get(texturePaths[i], Texture.class);
        }
        return textures;
    }
}
