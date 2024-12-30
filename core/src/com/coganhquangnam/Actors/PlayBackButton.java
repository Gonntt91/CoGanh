package com.coganhquangnam.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.coganhquangnam.Engine.AI;
import com.coganhquangnam.Screen.ChessBoard;
import com.coganhquangnam.gesture.Assets;
import com.coganhquangnam.gesture.BoardMemory;

/**
 * Created by nguyen gon on 2016/04/24.
 */
public class PlayBackButton extends Actor {

    private static final Texture texture  = Assets.playBackImage;

    private final ChessBoard chessBoard;
    private static final float centerX = (ChessBoard.gocX * 2 + ChessBoard.distance) / 2;
    private static final float centerY = (ChessBoard.gocY + ChessBoard.distance) + (ChessBoard.menuAreaHeight) * 2 /3;
    private static final float squareSize = 70f;

    public PlayBackButton(final  ChessBoard board){
        this.chessBoard = board;

        setBounds(centerX - squareSize / 2, centerY - squareSize / 2, squareSize, squareSize);

        this.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("Nut Back BBBBBBBBBBBBBBBBBBBBBBBBB   : " + BoardMemory.stackStatusInStage.size());
                chessBoard.removeOldNextPosition();
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

                return super.touchDown(event, x, y, pointer, button);
            }
        });

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


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor());
        batch.draw(texture, centerX - squareSize / 2, centerY - squareSize / 2, squareSize, squareSize);
    }







}
