package com.squashjam.game.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.squashjam.game.behaviors.AbominationBehavior;
import com.squashjam.game.behaviors.DroneBehavior;
import com.squashjam.game.behaviors.GrenadierBehavior;
import com.squashjam.game.entities.Entity;
import com.squashjam.game.entities.EntityBehavior;
import com.squashjam.game.enums.EntityTeam;
import com.squashjam.game.enums.EntityType;
import com.squashjam.game.utils.AssetManagerUtil;

public class EntityFactory {

    public static Entity createEnemyCharacter(EntityType entityType, int viewportWidth, int viewportHeight) {
        Texture movingTexture;
        Texture idleTexture;
        Texture attackTexture;
        int startHealth;
        float speed;
        float attackRange;
        int attackDamage;
        float attackCooldown;
        EntityBehavior behavior;

        switch (entityType) {
            case ABOMINATION:
                movingTexture = AssetManagerUtil.get().get("abomination_walk.png", Texture.class);
                idleTexture = AssetManagerUtil.get().get("idle.png", Texture.class);
                attackTexture = AssetManagerUtil.get().get("abomination_attack.png", Texture.class);
                startHealth = 800;
                speed = 20;
                attackRange = 50;
                attackDamage = 50;
                attackCooldown = 2f;
                behavior = new AbominationBehavior();
                break;
            case DRONE:
                movingTexture = AssetManagerUtil.get().get("moveleft.png", Texture.class);
                idleTexture = AssetManagerUtil.get().get("idle.png", Texture.class);
                attackTexture = AssetManagerUtil.get().get("attack1.png", Texture.class);
                startHealth = 100;
                speed = 150;
                attackRange = 50;
                attackDamage = 10;
                attackCooldown = 0.5f;
                behavior = new DroneBehavior();
                break;
            case GRENADIER:
                movingTexture = AssetManagerUtil.get().get("moveleft.png", Texture.class);
                idleTexture = AssetManagerUtil.get().get("idle.png", Texture.class);
                attackTexture = AssetManagerUtil.get().get("attack1.png", Texture.class);
                startHealth = 100;
                speed = 50; // Adjust Grenadier's speed as needed
                attackRange = 50;
                attackDamage = 100;
                attackCooldown = 2f;
                behavior = new GrenadierBehavior();
                break;
            default:
                throw new IllegalArgumentException("Unsupported enemy type: " + entityType);
        }

        int frameCols = 4;
        int frameRows = 2;
        float frameDuration = 0.2f;
        Vector2 startPosition = new Vector2(1000, 0); // Change the y-value if needed
        EntityTeam team = EntityTeam.ENEMY;

        return new Entity(entityType, movingTexture, idleTexture, attackTexture, frameCols, frameRows, frameDuration,
                startPosition, startHealth, team, speed, attackRange, attackDamage, attackCooldown, viewportWidth, viewportHeight, behavior);
    }
}



