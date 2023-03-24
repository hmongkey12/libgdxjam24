package com.squashjam.game.behaviors;

import com.squashjam.game.entities.Entity;
import com.squashjam.game.entities.EntityBehavior;
import com.squashjam.game.utils.EntityUtils;

import java.util.List;

public class TowerBehavior implements EntityBehavior {

    @Override
    public void update(Entity entity, float delta, List<Entity> otherEntities) {
        EntityUtils.updateAttack(entity, delta, otherEntities);
    }

    @Override
    public boolean canAttack(Entity attacker, Entity target) {
        return attacker.getTeam() != target.getTeam() && !target.isToBeRemoved();
    }

    @Override
    public int handleDamage(Entity entity, Entity target) {
        return entity.attackDamage;
    }
}