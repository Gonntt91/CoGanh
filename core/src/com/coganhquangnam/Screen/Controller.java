package com.haminhon.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.haminhon.Actors.ChessPiece;
import com.haminhon.Actors.OpeningSign;
import com.haminhon.Engine.AI;
import com.haminhon.gesture.BoardMemory;


import java.util.ArrayList;

/**
 * Created by nguyen gon on 2016/01/04.
 */
public class Controller extends Table {

    Skin skin;
    TextButton playBackButton;
    ChessBoard chessBoard;
    //public static   boolean isButtonPressed = false;

    public Controller(final ChessBoard board) {
        chessBoard = board;
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        playBackButton = new TextButton("BACK", skin);
        playBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //System.out.println("Nut Back BBBBBBBBBBBBBBBBBBBBBBBBB   : " + BoardMemory.stackStatusInStage.size());
                board.removeOldNextPosition();
                if(!BoardMemory.stackStatusInStage.isEmpty())
                {
                    //System.out.println("STACK IS NOT EMPTY ");
                    BoardMemory bm = BoardMemory.stackStatusInStage.pop();
                    //first
                    recoverArrayBoard(bm);
                    //second
                    recoverAllChessPieces(bm);
                    //third
                    recoverOpeningSign(bm);
                }
            }
        });

        this.add(playBackButton);

        this.setTransform(true);
        this.setPosition(ChessBoard.distance / 2 + ChessBoard.gocX, ChessBoard.distance + 2 * ChessBoard.gocY);
        //this.debug();
    }

    public void recoverArrayBoard(BoardMemory currentStatus)
    {
        for(int i=0; i<5; i++)
            for(int j=0; j<5; j++)
            {
                AI.chessBoard[i][j] = currentStatus.getArrayBoard()[i][j];

            }
    }

    public void recoverAllChessPieces(BoardMemory currentStatus)
    {
        //ArrayList<ChessPiece>  pieces =  currentStatus.getScreenChessBoard();

        for(int i = 0; i < ChessBoard.pieceCollection.size(); i++)
        {
            ChessPiece realP            =  ChessBoard.pieceCollection.get(i);
            ChessPiece memoryP      =  currentStatus.getScreenChessBoard().get(i);

            realP.setBoardCoord(memoryP.getBoardCoord());
            realP.boardToScreenCoord();
            realP.setBounds(memoryP.getX(), memoryP.getY(), memoryP.getWidth(), memoryP.getHeight());
            realP.resetFaction();

            realP.clearListeners();

            for(EventListener e : memoryP.getListeners())
            {
                realP.addListener(e);
            }
            realP.IamHere.setVisible(memoryP.selected);

            //System.out.print(" " + ChessBoard.pieceCollection.get(i).getListeners().size);
        }


    }

    public void recoverOpeningSign(BoardMemory currentStatus)
    {
        OpeningSign a = currentStatus.getAO();
        chessBoard.AO.setBoardCoordOfTrap(a.getBoardCoord());
        chessBoard.AO.setBounds(a.getX(), a.getY(), a.getWidth(), a.getHeight());
        chessBoard.AO.setVisible(a.isVisible());

        OpeningSign h = currentStatus.getHO();
        chessBoard.HO.setBoardCoordOfTrap(h.getBoardCoord());
        chessBoard.HO.setBounds(h.getX(), h.getY(), h.getWidth(), h.getHeight());
        chessBoard.HO.setVisible(h.isVisible());

    }


}
