package com.squashjam.game.behaviors;

import com.squashjam.game.entities.Entity;
import com.squashjam.game.enums.EntityType;

public class AbominationBehavior extends BaseBehavior{
    @Override
    public int handleDamage(Entity entity, Entity target) {
        if (entity.getEntityType() == EntityType.ABOMINATION && target.getEntityType() == EntityType.SNIPER) {
            return entity.attackDamage * 2;
        }
        return entity.attackDamage;
    }
}