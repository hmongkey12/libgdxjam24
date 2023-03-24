package com.squashjam.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.squashjam.game.entities.Entity;
import com.squashjam.game.enums.EntityState;
import com.squashjam.game.enums.EntityTeam;
import com.squashjam.game.enums.EntityType;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
        float attackTimer = entity.getAttackTimer();
        entity.setAttackTimer(attackTimer + delta);
        if (entity.getAttackTimer() >= entity.attackCooldown) {
            entity.setAttackTimer(0);
            boolean attackedOneTarget = otherEntities.stream()
                    .filter(other -> entity.getTeam() != other.getTeam() && !other.isToBeRemoved() && entity.behavior.canAttack(entity, other))
                    .filter(other -> entity.getPosition().dst(other.getPosition()) <= entity.attackRange)
                    .peek(other -> {
                        int modifiedDamage = entity.behavior.handleDamage(entity, other);
                        other.takeDamage(modifiedDamage);
                    })
                    .map(Entity::getEntityType)
                    .anyMatch(type -> type != EntityType.DEMOLITIONIST);

            // Update entity state to ATTACKING if an attack was performed
            if (attackedOneTarget) {
                entity.state = EntityState.ATTACKING;
                entity.getAttackSound().play();
            } else if (entity.getEntityType() == EntityType.CHICKEN || entity.getEntityType() == EntityType.BUNNY) {
                entity.state = EntityState.IDLE;
            } else {
                entity.state = EntityState.MOVING;
            }
        }
    }

    public static void updateMovement(Entity entity, float delta) {
        float dx = (entity.team == EntityTeam.PLAYER ? 1 : -1) * entity.speed * delta;
        entity.position.x += dx;
        entity.state = EntityState.MOVING;
    }
}



