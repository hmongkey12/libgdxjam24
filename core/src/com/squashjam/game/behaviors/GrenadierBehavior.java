package com.squashjam.game.behaviors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.squashjam.game.entities.Entity;
import com.squashjam.game.entities.EntityBehavior;
import com.squashjam.game.enums.EntityState;
import com.squashjam.game.enums.EntityType;
import com.squashjam.game.factories.HealthBarFactory;
import com.squashjam.game.utils.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class GrenadierBehavior implements EntityBehavior {
    private List<Entity> pendingGrenades = new ArrayList<>();
    private float grenadeCooldownTimer = 0;
    private float grenadeCooldown = 3.0f; // set the grenade cooldown to 3 seconds
    private AssetManager assetManager;

    public GrenadierBehavior(AssetManager assetManager) {
       this.assetManager = assetManager;
    }

    @Override
    public void update(Entity entity, float delta, List<Entity> otherEntities) {
        grenadeCooldownTimer -= delta;
        if (grenadeCooldownTimer <= 0) {
            placeGrenade(entity);
            grenadeCooldownTimer = grenadeCooldown;
        } else {
            EntityUtils.updateMovement(entity, delta);
        }
    }


    public List<Entity> getPendingGrenades() {
        return pendingGrenades;
    }

    @Override
    public boolean canAttack(Entity attacker, Entity target) {
        return false;
    }

    private void placeGrenade(Entity grenadier) {
        Texture idleTexture = assetManager.get("grenade_idle.png", Texture.class);
        Texture movingTexture = assetManager.get("grenade_move.png", Texture.class);
        Texture attackTexture = assetManager.get("grenade_attack.png", Texture.class);

        int frameCols = 3;
        int frameRows = 2;
        float frameDuration = 0.1f;

        Vector2 grenadePosition = new Vector2(grenadier.getPosition().x, grenadier.getPosition().y);
        int grenadeHealth = grenadier.getMaxHealth();
        float attackRange = grenadier.getAttackRange();
        int attackDamage = grenadier.getAttackDamage();
        float attackCooldown = 0;

        float entityWidth = grenadier.getEntityWidth() * 0.5f;
        float entityHeight = grenadier.getEntityHeight() * 0.5f;

        Entity grenade = Entity.builder()
                .entityType(EntityType.GRENADE)
                .movingAnimation(Entity.createMovingAnimation(movingTexture, 1, 1, frameDuration))
                .idleAnimation(Entity.createIdleAnimation(idleTexture, 1, 1, frameDuration))
                .attackAnimation(Entity.createAttackAnimation(attackTexture, frameCols, frameRows, frameDuration))
                .position(grenadePosition)
                .health(grenadeHealth)
                .maxHealth(grenadier.getMaxHealth())
                .team(grenadier.getTeam())
                .state(EntityState.IDLE)
                .speed(0)
                .attackRange(attackRange)
                .attackDamage(attackDamage)
                .attackCooldown(attackCooldown)
                .entityWidth(entityWidth)
                .entityHeight(entityHeight)
                .behavior(new GrenadeBehavior())
                .healthBar(HealthBarFactory.createHealthBar(entityWidth, entityHeight, grenadePosition))
                .build();
        pendingGrenades.add(grenade);
    }
}