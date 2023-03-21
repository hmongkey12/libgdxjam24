package com.squashjam.game.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
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

    private static Entity createBasicEntity(EntityType entityType, EntityTeam team, int viewportWidth, int viewportHeight, Vector2 startPosition, int startHealth, int maxHealth, float speed, float attackRange, int attackDamage, float attackCooldown, EntityBehavior behavior, String movingTexturePath, String idleTexturePath, String attackTexturePath, int frameCols, int frameRows, float frameDuration) {
        Texture movingTexture = AssetManagerUtil.get().get(movingTexturePath, Texture.class);
        Texture idleTexture = AssetManagerUtil.get().get(idleTexturePath, Texture.class);
        Texture attackTexture = AssetManagerUtil.get().get(attackTexturePath, Texture.class);

        float characterWidth = viewportWidth * 0.1f;
        float characterHeight = viewportHeight * 0.15f;

        return Entity.builder()
                .entityType(entityType)
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
                .viewportWidth(viewportWidth)
                .viewportHeight(viewportHeight)
                .behavior(behavior)
                .healthBar(HealthBarFactory.createHealthBar(characterWidth, characterHeight, startPosition, viewportHeight))
                .build();
    }
    private static Entity createAbomination(EntityTeam team, int viewportWidth, int viewportHeight) {
        return createBasicEntity(EntityType.ABOMINATION, team, viewportWidth, viewportHeight, new Vector2(1000, 0), 800, 800, 20, 50, 50, 2f, new AbominationBehavior(), "abomination_walk.png", "idle.png", "abomination_attack.png", 4, 2, 0.2f);
    }

    private static Entity createDrone(EntityTeam team, int viewportWidth, int viewportHeight) {
        return createBasicEntity(EntityType.DRONE, team, viewportWidth, viewportHeight, new Vector2(1000, 0), 100, 100, 150, 50, 10, 0.5f, new DroneBehavior(), "moveleft.png", "idle.png", "attack1.png", 4, 2, 0.2f);
    }

    private static Entity createGrenadier(EntityTeam team, int viewportWidth, int viewportHeight) {
        return createBasicEntity(EntityType.GRENADIER, team, viewportWidth, viewportHeight, new Vector2(1000, 0), 100, 100, 50, 50, 100, 2f, new GrenadierBehavior(), "moveleft.png", "idle.png", "attack1.png", 4, 2, 0.2f);
    }

    public static Entity createChickenCharacter(EntityTeam team, int viewportWidth, int viewportHeight) {
        return createBasicEntity(EntityType.CHICKEN, team, viewportWidth, viewportHeight, new Vector2(10, viewportHeight * 0.1f), 1000, 1000, 0, viewportWidth * 0.1f, 100, 1f, new ChickenBehavior(), "moveleft.png", "chicken_idle.png", "chicken_attack.png", 4, 2, 0.1f);
    }

    private static Entity createGrunt(EntityTeam team, int viewportWidth, int viewportHeight) {
        return createBasicEntity(EntityType.GRUNT, team, viewportWidth, viewportHeight, new Vector2(0, 0), 100, 100, 50, 50, 10, 1f, new GruntBehavior(), "grunt_walk.png", "idle.png", "grunt_attack.png", 4, 2, 0.1f);
    }

    private static Entity createSniper(EntityTeam team, int viewportWidth, int viewportHeight) {
        return createBasicEntity(EntityType.SNIPER, team, viewportWidth, viewportHeight, new Vector2(0, 0), 100, 100, 50, 300, 10, 1f, new SniperBehavior(), "sniper_walk.png", "idle.png", "sniper_attack.png", 4, 2, 0.1f);
    }

    private static Entity createDemolitionist(EntityTeam team, int viewportWidth, int viewportHeight) {
        return createBasicEntity(EntityType.DEMOLITIONIST, team, viewportWidth, viewportHeight, new Vector2(0, 0), 100, 100, 50, 50, 10, 1f, new DemolitionistBehavior(), "moveright.png", "idle.png", "attack1.png", 4, 2, 0.1f);
    }
}
