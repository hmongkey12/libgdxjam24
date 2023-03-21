package com.squashjam.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.squashjam.game.enums.EntityState;
import com.squashjam.game.enums.EntityTeam;
import com.squashjam.game.enums.EntityType;
import com.squashjam.game.utils.AnimationUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Entity {
    public Circle collisionCircle;
    public EntityType entityType;
    public EntityBehavior behavior;

    private HealthBar healthBar;
    private int maxHealth;
    public int viewportWidth;
    public int viewportHeight;
    public EntityTeam team;
    public EntityState state;

    private final Animation<TextureRegion> movingAnimation;
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> attackAnimation;
    private float animationTime = 0;
    public Vector2 position;

    private boolean toBeRemoved;

    private int health;
    public float speed;
    public float attackRange;
    public int attackDamage;
    public float attackCooldown;
    public float attackTimer;

    float characterWidth;
    float characterHeight;

    public void update(float delta, List<Entity> otherEntities) {
        if (toBeRemoved) {
            return;
        }

        behavior.update(this, delta, otherEntities);
        animationTime += delta;

        // health
        healthBar.update((float) health / maxHealth, new Vector2(position.x, position.y + characterHeight));
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            toBeRemoved = true;
        }
    }

    public void draw(Batch batch) {
        Animation<TextureRegion> currentAnimation;
        switch (state) {
            case MOVING:
                currentAnimation = movingAnimation;
                break;
            case ATTACKING:
                currentAnimation = attackAnimation;
                break;
            case IDLE:
            default:
                currentAnimation = idleAnimation;
                break;
        }

        // Calculate the proportional width and height
        float width = viewportWidth * 0.1f;
        float height = viewportHeight * 0.15f;

        batch.draw(currentAnimation.getKeyFrame(animationTime, true), position.x, position.y, width, height);

        // Draw the health bar
        System.out.println(health/maxHealth);
        healthBar.draw(batch, (float) health / maxHealth);
    }

    public static Animation<TextureRegion> createMovingAnimation(Texture movingTexture, int frameCols, int frameRows, float frameDuration) {
        return AnimationUtils.createAnimation(movingTexture, frameCols, frameRows, frameDuration);
    }

    public static Animation<TextureRegion> createIdleAnimation(Texture idleTexture, int frameCols, int frameRows, float frameDuration) {
        return AnimationUtils.createAnimation(idleTexture, frameCols, frameRows, frameDuration);
    }

    public static Animation<TextureRegion> createAttackAnimation(Texture attackTexture, int frameCols, int frameRows, float frameDuration) {
        return AnimationUtils.createAnimation(attackTexture, frameCols, frameRows, frameDuration);
    }

    public boolean isToBeRemoved() {
        return toBeRemoved;
    }
}
