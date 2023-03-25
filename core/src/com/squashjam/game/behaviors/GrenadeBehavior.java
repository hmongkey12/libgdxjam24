package com.squashjam.game.behaviors;

import com.squashjam.game.entities.Entity;
import com.squashjam.game.utils.EntityUtils;

import java.util.ArrayList;
import java.util.List;



public class GrenadeBehavior extends BaseBehavior {

    private final List<Entity> targetsInRange = new ArrayList<>();

    @Override
    public void update(Entity entity, float delta, List<Entity> otherEntities) {
        int damageDealt = entity.attackDamage;
        boolean exploded = false;

        // Check for new targets within range
        for (Entity other : otherEntities) {
            if (entity.getTeam().equals(other.getTeam()) && !other.isToBeRemoved() && EntityUtils.overlap(entity.position, entity.getEntityWidth(), entity.getEntityHeight(), other.position, other.getEntityWidth(), other.getEntityHeight())) {
                if (!targetsInRange.contains(other)) {
                    targetsInRange.add(other);
                }
            }
        }

        // Attack all targets within range
        for (Entity target : targetsInRange) {
            target.heal(damageDealt);
            exploded = true;
        }
        targetsInRange.clear();

        // Damage the grenade if it exploded
        if (exploded) {
            entity.setToBeRemoved(true);
        }
    }

    @Override
    public boolean canAttack(Entity attacker, Entity target) {
        return EntityUtils.overlap(attacker.position, attacker.getEntityWidth(), attacker.getEntityHeight(), target.position, target.getEntityWidth(), target.getEntityHeight());
    }
}




