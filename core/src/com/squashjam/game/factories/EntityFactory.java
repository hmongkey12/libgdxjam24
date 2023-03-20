package com.squashjam.game.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.squashjam.game.behaviors.AbominationBehavior;
import com.squashjam.game.behaviors.ChickenBehavior;
import com.squashjam.game.behaviors.DroneBehavior;
import com.squashjam.game.behaviors.GrenadierBehavior;
import com.squashjam.game.entities.Entity;
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
            default:
                throw new IllegalArgumentException("Unsupported enemy type: " + entityType);
        }
    }

    private static Entity createAbomination(EntityTeam team, int viewportWidth, int viewportHeight) {
        EntityType entityType = EntityType.ABOMINATION;
        Texture movingTexture = AssetManagerUtil.get().get("abomination_walk.png", Texture.class);
        Texture idleTexture = AssetManagerUtil.get().get("idle.png", Texture.class);
        Texture attackTexture = AssetManagerUtil.get().get("abomination_attack.png", Texture.class);
        int frameCols = 4;
        int frameRows = 2;
        float frameDuration = 0.2f;
        Vector2 startPosition = new Vector2(1000, 0); // Change the y-value if needed
        int startHealth = 800;
        float speed = 20;
        float attackRange = 50;
        int attackDamage = 50;
        float attackCooldown = 2f;
        AbominationBehavior behavior = new AbominationBehavior();

        return new Entity(entityType, movingTexture, idleTexture, attackTexture, frameCols, frameRows, frameDuration,
                startPosition, startHealth, team, speed, attackRange, attackDamage, attackCooldown, viewportWidth, viewportHeight, behavior);
    }

    private static Entity createDrone(EntityTeam team, int viewportWidth, int viewportHeight) {
        EntityType entityType = EntityType.DRONE;
        Texture movingTexture = AssetManagerUtil.get().get("moveleft.png", Texture.class);
        Texture idleTexture = AssetManagerUtil.get().get("idle.png", Texture.class);
        Texture attackTexture = AssetManagerUtil.get().get("attack1.png", Texture.class);
        int frameCols = 4;
        int frameRows = 2;
        float frameDuration = 0.2f;
        Vector2 startPosition = new Vector2(1000, 0); // Change the y-value if needed
        int startHealth = 100;
        float speed = 150;
        float attackRange = 50;
        int attackDamage = 10;
        float attackCooldown = 0.5f;
        DroneBehavior behavior = new DroneBehavior();

        return new Entity(entityType, movingTexture, idleTexture, attackTexture, frameCols, frameRows, frameDuration,
                startPosition, startHealth, team, speed, attackRange, attackDamage, attackCooldown, viewportWidth, viewportHeight, behavior);
    }

    private static Entity createGrenadier(EntityTeam team, int viewportWidth, int viewportHeight) {
        EntityType entityType = EntityType.GRENADIER;
        Texture movingTexture = AssetManagerUtil.get().get("moveleft.png", Texture.class);
        Texture idleTexture = AssetManagerUtil.get().get("idle.png", Texture.class);
        Texture attackTexture = AssetManagerUtil.get().get("attack1.png", Texture.class);
        int frameCols = 4;
        int frameRows = 2;
        float frameDuration = 0.2f;
        Vector2 startPosition = new Vector2(1000, 0); // Change the y-value if needed
        int startHealth = 100;
        float speed = 50; // Adjust Grenadier's speed as needed
        float attackRange = 50;
        int attackDamage = 100;
        float attackCooldown = 2f;
        GrenadierBehavior behavior = new GrenadierBehavior();
        return new Entity(entityType, movingTexture, idleTexture, attackTexture, frameCols, frameRows, frameDuration, startPosition, startHealth, team, speed, attackRange, attackDamage, attackCooldown, viewportWidth, viewportHeight, behavior);
    }

    public static Entity createChickenCharacter(EntityTeam team, int viewportWidth, int viewportHeight) {
        EntityType entityType = EntityType.CHICKEN;
        Texture movingTexture = AssetManagerUtil.get().get("moveleft.png", Texture.class);
        Texture idleTexture = AssetManagerUtil.get().get("chicken_idle.png", Texture.class);
        Texture attackTexture = AssetManagerUtil.get().get("chicken_attack.png", Texture.class);
        int frameCols = 4;
        int frameRows = 2;
        float frameDuration = 0.1f;
        Vector2 startPosition = new Vector2(10, viewportHeight * 0.1f);
        int startHealth = 1000;
        float speed = 0; // Chicken doesn't move
        float attackRange = viewportWidth * 0.1f;
        int attackDamage = 100;
        float attackCooldown = 1f;
        ChickenBehavior behavior = new ChickenBehavior();
        return new Entity(entityType, movingTexture, idleTexture, attackTexture, frameCols, frameRows, frameDuration, startPosition, startHealth, team, speed, attackRange, attackDamage, attackCooldown, viewportWidth, viewportHeight, behavior);
    }
}
