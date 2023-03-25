package com.squashjam.game.entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.squashjam.game.enums.EntityState;
import com.squashjam.game.enums.EntityTeam;
import com.squashjam.game.enums.EntityType;
import com.squashjam.game.utils.AnimationUtils;
import com.squashjam.game.utils.UiSquare;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Entity {
    public float entitySight;
    private Sound attackSound;
    private int[] upgradeCost;

    private boolean isFollowingMouse;
    private float freezeTimer;
    private List<UiSquare> uiSquares;

    private Array<Texture> textures;
    public EntityType entityType;
    public EntityBehavior behavior;
    private HealthBar healthBar;
    private int maxHealth;
    private float entityWidth;
    private float entityHeight;
    public EntityTeam team;
    public EntityState state;

    private final Animation<TextureRegion> movingAnimation;
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> attackAnimation;
    private float animationTime;
    public Vector2 position;

    private boolean toBeRemoved;

    private int health;
    public float speed;
    public float attackRange;
    public int attackDamage;
    public float attackCooldown;
    private float attackTimer;
    private Integer currentLevel;
    private EntityType lastAttack;

    public void update(float delta, List<Entity> otherEntities, Vector3 mousePosition) {
        if (toBeRemoved) {
            return;
        }

        if (isFollowingMouse) {
            this.position.x = mousePosition.x - (entityWidth / 2);
        }

        if (team == EntityTeam.PLAYER) {
            float squareX = position.x;
            for (UiSquare uiSquare : uiSquares) {
                uiSquare.setPosition(squareX, position.y + entityHeight);
                squareX += 70; // Update this value based on the width of your UiSquares
                if (uiSquare.getLabel1().equalsIgnoreCase("level")) {
                    uiSquare.setLabel2(currentLevel.toString());
                }
            }
        }

        behavior.update(this, delta, otherEntities);
        animationTime += delta;

        if (healthBar != null) {
            healthBar.update(new Vector2(position.x, position.y + entityHeight));
        }
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        toBeRemoved = (this.health <= 0);
    }

    public void heal(int healAmount) {
       this.health += healAmount;
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

        batch.draw(currentAnimation.getKeyFrame(animationTime, true), position.x, position.y, entityWidth, entityHeight);

        // Draw the health bar
        if (healthBar != null) {
            healthBar.draw(batch, (float) health / maxHealth);
        }
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

    public List<UiSquare> getUiSquares() {
        return uiSquares;
    }



    public boolean isToBeRemoved() {
        return toBeRemoved;
    }

    public void dispose() {
        if (healthBar != null) {
            healthBar.dispose();
        }
    }

    public void setFollowingMouse(boolean isFollowingMouse) {
        this.isFollowingMouse = isFollowingMouse;
    }
}
