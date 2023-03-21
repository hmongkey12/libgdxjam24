package com.squashjam.game.utils;

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
    private BitmapFont font;
    private Map<String, Integer> gold;
    private float offsetX;
    private float offsetY;

    private float viewportHeight;


    public GameUI(OrthographicCamera camera) {
        offsetX = camera.position.x - camera.viewportWidth / 2;
        offsetY = camera.position.y - camera.viewportHeight / 2;
        viewportHeight = camera.viewportHeight;
        initUiSquares();
        font = new BitmapFont();
        gold = new HashMap<>();
        gold.put("gold", 0);
    }

    private void initUiSquares() {
        uiSquares = new ArrayList<>();
        uiSquares.add(new UiSquare("uiSquare.png", 10, 10, 50, 50, "A"));
        uiSquares.add(new UiSquare("uiSquare.png", 70, 10, 50, 50, "S"));
        uiSquares.add(new UiSquare("uiSquare.png", 130, 10, 50, 50, "D"));
        uiSquares.add(new UiSquare("uiSquare.png", 190, 10, 50, 50, "left"));
        uiSquares.add(new UiSquare("uiSquare.png", 250, 10, 50, 50, "right"));
    }

    public void updatePositions(OrthographicCamera camera) {
        offsetX = camera.position.x - camera.viewportWidth / 2;
        offsetY = camera.position.y - camera.viewportHeight / 2;
        viewportHeight = camera.viewportHeight;

        uiSquares.get(0).setPosition(offsetX + 10, offsetY + viewportHeight - 60);
        uiSquares.get(1).setPosition(offsetX + 70, offsetY + viewportHeight - 60);
        uiSquares.get(2).setPosition(offsetX + 130, offsetY + viewportHeight - 60);
        uiSquares.get(3).setPosition(offsetX + 190, offsetY + viewportHeight - 60);
        uiSquares.get(4).setPosition(offsetX + 250, offsetY + viewportHeight - 60);
    }

    public void draw(SpriteBatch batch) {
        for (UiSquare uiSquare : uiSquares) {
            uiSquare.draw(batch, font);
        }

        float goldX = offsetX + 380;
        float goldY = offsetY + viewportHeight - 45;

        font.setColor(Color.BLACK);
        font.draw(batch, "Gold: " + gold.get("gold"), goldX, goldY);
    }

    public void setGold(int amount) {
        gold.put("gold", amount);
    }

    public void dispose() {
        font.dispose();
    }
}
