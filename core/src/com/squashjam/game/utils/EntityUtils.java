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
        for (Entity other : otherEntities) {
            if (entity.getTeam() != other.getTeam() && !other.isToBeRemoved() && entity.behavior.canAttack(entity, other)) {
                if (overlap(entity.position, entity.getEntityWidth(), entity.getEntityHeight(), other.position, other.getEntityWidth(), other.getEntityHeight())) {
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
                    if (overlap(entity.position, entity.getEntityWidth(), entity.getEntityHeight(), other.position, other.getEntityWidth(), other.getEntityHeight())) {
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
        entity.state = EntityState.MOVING;
    }
}



