package com.squashjam.game.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameUI {
    private List<UiSquare> uiSquares;
    private final BitmapFont font;
    private final Map<String, Integer> gold;
    private final Map<String, Float> timeRemaining;
    private float offsetX;
    private float offsetY;

    private float viewportHeight;
    private AssetManager assetManager;


    public GameUI(OrthographicCamera camera, AssetManager assetManager) {
        this.assetManager = assetManager;
        offsetX = camera.position.x - camera.viewportWidth / 2;
        offsetY = camera.position.y - camera.viewportHeight / 2;
        viewportHeight = camera.viewportHeight;
        initUiSquares();
        font = new BitmapFont();
        gold = new HashMap<>();
        timeRemaining = new HashMap<>();
        timeRemaining.put("timeRemaining", 0f);
        gold.put("gold", 0);
    }

    private void initUiSquares() {
        uiSquares = new ArrayList<>();
        uiSquares.add(new UiSquare("grunt_idle.png", 10, 10, 70, 50, "", "200", assetManager));
        uiSquares.add(new UiSquare("sniper_idle.png", 110, 10, 70, 50, "", "500", assetManager));
        uiSquares.add(new UiSquare("demolition_idle.png", 210, 10, 70, 50, "", "300", assetManager));
        uiSquares.add(new UiSquare("black.jpg", 10, 60, 70, 50, "A", "Grunt", assetManager));
        uiSquares.add(new UiSquare("black.jpg", 110, 60, 70, 50, "S", "Sniper", assetManager));
        uiSquares.add(new UiSquare("black.jpg", 210, 60, 70, 50, "D", "IceMan", assetManager));
        uiSquares.add(new UiSquare("black.jpg", 310, 60, 70, 50, "F", "Cancel", assetManager));
    }

    public void updatePositions(OrthographicCamera camera) {
        offsetX = camera.position.x - camera.viewportWidth / 2;
        offsetY = camera.position.y - camera.viewportHeight / 2;
        viewportHeight = camera.viewportHeight;

        uiSquares.get(0).setPosition(offsetX + 10, offsetY + viewportHeight - 60);
        uiSquares.get(1).setPosition(offsetX + 110, offsetY + viewportHeight - 60);
        uiSquares.get(2).setPosition(offsetX + 210, offsetY + viewportHeight - 60);
        uiSquares.get(3).setPosition(offsetX + 10, offsetY + viewportHeight - 120);
        uiSquares.get(4).setPosition(offsetX + 110, offsetY + viewportHeight - 120);
        uiSquares.get(5).setPosition(offsetX + 210, offsetY + viewportHeight - 120);
        uiSquares.get(6).setPosition(offsetX + 310, offsetY + viewportHeight - 120);
    }

    public void draw(SpriteBatch batch) {
        for (UiSquare uiSquare : uiSquares) {
            uiSquare.draw(batch, font);
        }

        float goldX = offsetX + 610;
        float goldY = offsetY + viewportHeight - 45;
        float timeX = goldX + 200;
        float timeY = goldY;

        font.setColor(Color.WHITE); // Set the font color to white
        font.draw(batch, "Gold: " + gold.get("gold"), goldX, goldY);
        font.draw(batch, "Time Remaining: " + Math.round(timeRemaining.get("timeRemaining")), timeX, timeY);
    }

    public void setGold(int amount) {
        gold.put("gold", amount);
    }

    public void setTimeRemaining(float timeRemaining) {
        this.timeRemaining.put("timeRemaining", timeRemaining);
    }

    public void dispose() {
        font.dispose();
    }
}
