package com.coganhquangnam.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by NGUYENGON on 2015/09/12.
 */
public abstract class BaseGameScreen implements Screen {

    final Game game;
    Vector2 screenSize;
    BitmapFont font;
    InputMultiplexer inputMultiplexer;

    public BaseGameScreen(final Game game){
        this.game = game;
        screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font = new BitmapFont();
        inputMultiplexer = new InputMultiplexer();


        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
