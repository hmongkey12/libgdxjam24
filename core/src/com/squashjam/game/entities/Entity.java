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

import java.util.List;

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


    public Entity(EntityType entityType, Texture movingTexture, Texture idleTexture, Texture attackTexture,
                  int frameCols, int frameRows, float frameDuration, Vector2 startPosition, int startHealth,
                  EntityTeam team, float speed, float attackRange, int attackDamage, float attackCooldown, int viewportWidth, int viewportHeight, EntityBehavior behavior) {
        // Initialize the position and health
        this.position = startPosition;
        this.health = startHealth;
        this.entityType = entityType;
        this.team = team; // assuming the constructor parameter is now of type CharacterTeam
        this.state = EntityState.IDLE;
        this.speed = speed;
        this.attackRange = attackRange;
        this.attackDamage = attackDamage;
        this.attackCooldown = attackCooldown;
        this.attackTimer = 0;
        this.behavior = behavior;
        // Set the viewport dimensions
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;

        // health
        this.maxHealth = startHealth;
        float characterWidth = viewportWidth * 0.1f;
        float characterHeight = viewportHeight * 0.15f;
        this.healthBar = new HealthBar(characterWidth, viewportHeight * 0.015f, new Vector2(position.x, position.y + characterHeight));


        // Create animations
        this.movingAnimation = AnimationUtils.createAnimation(movingTexture, frameCols, frameRows, frameDuration);
        this.idleAnimation = AnimationUtils.createAnimation(idleTexture, frameCols, frameRows, frameDuration);
        this.attackAnimation = AnimationUtils.createAnimation(attackTexture, frameCols, frameRows, frameDuration);


        // Calculate the proportional radius
        float radius = viewportWidth * 0.1f;
        this.collisionCircle = new Circle(position.x, position.y, radius);
    }

    public void update(float delta, List<Entity> otherEntities) {
        if (toBeRemoved) {
            return;
        }

        behavior.update(this, delta, otherEntities);
        animationTime += delta;

        // health
        healthBar.update((float) health / maxHealth, position);
    }

    public EntityTeam getTeam() {
        return team;
    }

    public EntityType getEntityType() {
        return entityType;
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
        healthBar.draw(batch, (float) health / maxHealth);
    }

    public EntityBehavior getBehavior() {
        return behavior;
    }

    public boolean isToBeRemoved() {
        return toBeRemoved;
    }
}
