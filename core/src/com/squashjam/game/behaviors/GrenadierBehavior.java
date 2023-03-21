package com.squashjam.game.behaviors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.squashjam.game.entities.Entity;
import com.squashjam.game.entities.EntityBehavior;
import com.squashjam.game.enums.EntityState;
import com.squashjam.game.enums.EntityType;
import com.squashjam.game.factories.HealthBarFactory;
import com.squashjam.game.utils.AssetManagerUtil;
import com.squashjam.game.utils.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class GrenadierBehavior implements EntityBehavior {
    private List<Entity> pendingGrenades = new ArrayList<>();
    private float grenadeCooldownTimer = 0;
    private float grenadeCooldown = 3.0f; // set the grenade cooldown to 3 seconds

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
        Texture idleTexture = AssetManagerUtil.get().get("idle.png", Texture.class);
        Texture movingTexture = AssetManagerUtil.get().get("moveleft.png", Texture.class);
        Texture attackTexture = AssetManagerUtil.get().get("attack1.png", Texture.class);

        int frameCols = 4;
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
                .movingAnimation(Entity.createMovingAnimation(movingTexture, frameCols, frameRows, frameDuration))
                .idleAnimation(Entity.createIdleAnimation(idleTexture, frameCols, frameRows, frameDuration))
                .attackAnimation(Entity.createAttackAnimation(attackTexture, frameCols, frameRows, frameDuration))
                .position(grenadePosition)
                .health(grenadeHealth)
                .maxHealth(grenadier.getMaxHealth())
                .team(grenadier.getTeam())
                .state(EntityState.IDLE)
                .speed(0)
                .collisionCircle(new Circle())
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