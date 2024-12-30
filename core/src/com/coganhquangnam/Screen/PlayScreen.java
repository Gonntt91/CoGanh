package com.coganhquangnam.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.coganhquangnam.Engine.AI;
import com.coganhquangnam.gesture.Assets;


/**
 * Created by NGUYENGON on 2015/09/12.
 */
public class PlayScreen extends  BaseGameScreen {

    private ChessBoard chessBoard;
    //private Controller controller;
    //private AI engine;

    public PlayScreen(final Game game){
        super(game);
        Assets.load();
        chessBoard = new ChessBoard();
        //controller = new Controller(chessBoard);
        //chessBoard.addActor(controller);

        inputMultiplexer.addProcessor(chessBoard);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        chessBoard.act(delta);
        chessBoard.draw();
        //chessBoard.repaint();

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {

        chessBoard.getCamera().update();
        chessBoard.shapeRenderer.setProjectionMatrix(chessBoard.getCamera().combined);
        chessBoard.getViewport().update(width, height, false);

    }
}
