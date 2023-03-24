package com.squashjam.game.behaviors;

import com.squashjam.game.entities.Entity;
import com.squashjam.game.enums.EntityType;

public class SniperBehavior extends BaseBehavior {
    @Override
    public int handleDamage(Entity entity, Entity target) {
        if (entity.getEntityType() == EntityType.SNIPER && target.getEntityType() == EntityType.DRONE) {
            return entity.attackDamage * 4;
        }
        return entity.attackDamage;
    }
}

