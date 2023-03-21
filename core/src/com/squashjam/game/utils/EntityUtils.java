package com.squashjam.game.utils;

import com.squashjam.game.entities.Entity;
import com.squashjam.game.enums.EntityState;
import com.squashjam.game.enums.EntityTeam;
import com.squashjam.game.enums.EntityType;

import java.util.List;

public class EntityUtils {

    public static boolean isInAttackRange(Entity entity, List<Entity> otherEntities) {
        for (Entity other : otherEntities) {
            if (entity.getTeam() != other.getTeam() && !other.isToBeRemoved() && entity.behavior.canAttack(entity, other)) {
                float distance = entity.position.dst(other.position);
                if (distance <= entity.attackRange) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void updateAttack(Entity entity, float delta, List<Entity> otherEntities) {
        entity.attackTimer += delta;
        if (entity.attackTimer >= entity.attackCooldown) {
            entity.attackTimer = 0;
            boolean attackedOneTarget = false;
            for (Entity other : otherEntities) {
                if (entity.getTeam() != other.getTeam() && !other.isToBeRemoved() && entity.behavior.canAttack(entity, other)) {
                    float distance = entity.position.dst(other.position);
                    if (distance <= entity.attackRange) {
                        other.takeDamage(entity.attackDamage);
                        // Check if the entity is not DEMOLITIONIST and has already attacked one target
                        if (entity.getEntityType() != EntityType.DEMOLITIONIST && attackedOneTarget) {
                            break;
                        }
                        attackedOneTarget = true;
                    }
                }
            }
            // Update entity state to ATTACKING if an attack was performed
            if (attackedOneTarget) {
                entity.state = EntityState.ATTACKING;
            } else {
                // If no target was attacked, set state back to IDLE or MOVING based on the situation
                entity.state = EntityState.IDLE; // or EntityState.MOVING
            }
        }
    }

    public static void updateMovement(Entity entity, float delta) {
        float dx = (entity.team == EntityTeam.PLAYER ? 1 : -1) * entity.speed * delta;
        entity.position.x += dx;
        entity.collisionCircle.setPosition(entity.position.x, entity.position.y);
        entity.state = EntityState.MOVING;
    }
}


