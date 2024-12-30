package com.haminhon.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.haminhon.Screen.ChessBoard;


/**
 * Created by NGUYENGON on 2015/10/25.
 */
public class NextPosition extends Actor {

    public static ChessBoard board;
    private final Texture texture = new Texture(Gdx.files.internal("data/next.png"));
    private Vector2  boardCoord;
    private Vector2  screenCoord;
    public static int nextPositionSize = 30;
    public static ChessPiece originChessPiece;

    public Vector2 getBoardCoord()
    {
        return boardCoord;
    }
    public Vector2 getScreenCoord()
    {
        return screenCoord;
    }

    public  void boardToScreenCoord(Vector2 boardPosition)
    {
        this.screenCoord.x = boardPosition.x * ChessBoard.distance/4 + ChessBoard.gocX;
        this.screenCoord.y = boardPosition.y * ChessBoard.distance/4 + ChessBoard.gocY;
    }

    public NextPosition(Vector2 boardCoord, final ChessBoard board){
        NextPosition.board = board;
        this.boardCoord = boardCoord;

        screenCoord = new Vector2();
        boardToScreenCoord(boardCoord);

        setBounds(screenCoord.x - ChessBoard.squaredistance / 2, screenCoord.y - ChessBoard.squaredistance / 2, ChessBoard.squaredistance, ChessBoard.squaredistance);

    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor());
        batch.draw(texture, screenCoord.x - nextPositionSize / 2, screenCoord.y - nextPositionSize / 2, nextPositionSize, nextPositionSize);
    }
}
