package com.squashjam.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.squashjam.game.entities.Entity;
import com.squashjam.game.enums.EntityState;
import com.squashjam.game.enums.EntityTeam;
import com.squashjam.game.enums.EntityType;

import java.util.List;

public class EntityUtils {
    public static boolean overlap(Vector2 position1, float width1, float height1, Vector2 position2, float width2, float height2) {
        return position1.x < position2.x + width2 &&
                position1.x + width1 > position2.x &&
                position1.y < position2.y + height2 &&
                position1.y + height1 > position2.y;
    }

    public static boolean isInAttackRange(Entity entity, List<Entity> otherEntities) {
        return otherEntities.stream()
                .filter(other -> entity.getTeam() != other.getTeam() && !other.isToBeRemoved() && entity.behavior.canAttack(entity, other))
                .anyMatch(other -> entity.getPosition().dst(other.getPosition()) <= entity.attackRange);
    }

    public static void updateAttack(Entity entity, float delta, List<Entity> otherEntities) {
        updateAttackTimer(entity, delta);
        if (isAttackCooldownFinished(entity)) {
            resetAttackTimer(entity);
            boolean attackedOneTarget = performAttackOnTargetsInRange(entity, otherEntities);
            updateEntityStateAfterAttack(entity, attackedOneTarget);
        }
    }

    private static void updateAttackTimer(Entity entity, float delta) {
        float attackTimer = entity.getAttackTimer();
        entity.setAttackTimer(attackTimer + delta);
    }

    private static boolean isAttackCooldownFinished(Entity entity) {
        return entity.getAttackTimer() >= entity.attackCooldown;
    }

    private static void resetAttackTimer(Entity entity) {
        entity.setAttackTimer(0);
    }

    private static boolean performAttackOnTargetsInRange(Entity entity, List<Entity> otherEntities) {
        return otherEntities.stream()
                .filter(other -> entity.getTeam() != other.getTeam() && !other.isToBeRemoved() && entity.behavior.canAttack(entity, other))
                .filter(other -> entity.getPosition().dst(other.getPosition()) <= entity.attackRange)
                .findFirst()
                .map(target -> {
                    int modifiedDamage = entity.behavior.handleDamage(entity, target);
                    target.takeDamage(modifiedDamage);
                    target.setLastAttack(entity.getEntityType());
                    if (entity.getEntityType().equals(EntityType.DEMOLITIONIST)) {
                        target.setFreezeTimer(3f);
                    }
                    return true;
                })
                .orElse(false);
    }

    private static void updateEntityStateAfterAttack(Entity entity, boolean attackedOneTarget) {
        if (attackedOneTarget) {
            entity.state = EntityState.ATTACKING;
            entity.getAttackSound().play();
        } else if (entity.getEntityType() == EntityType.CHICKEN || entity.getEntityType() == EntityType.BUNNY) {
            entity.state = EntityState.IDLE;
        } else {
            entity.state = EntityState.MOVING;
        }
    }

    public static void updateMovement(Entity entity, float delta) {
        if (entity.team == EntityTeam.ENEMY) {

            float dx;
            if (entity.getFreezeTimer() > 0) {
                float freezeTime = entity.getFreezeTimer();
                entity.setFreezeTimer(freezeTime - delta);
                dx = -1 * (entity.speed * .5f) * delta;
            } else {
                entity.setFreezeTimer(0);
                dx = -1 * entity.speed * delta;
            }
            entity.position.x += dx;
            entity.state = EntityState.MOVING;
        } else {
            entity.state = EntityState.IDLE;
        }
    }

}



