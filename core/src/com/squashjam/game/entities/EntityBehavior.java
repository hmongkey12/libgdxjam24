package com.squashjam.game.entities;

import java.util.List;

public interface EntityBehavior {
    void update(Entity entity, float delta, List<Entity> otherEntities);
    boolean canAttack(Entity attacker, Entity target);
}

