package com.haminhon.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.haminhon.Engine.AI;
import com.haminhon.Engine.Point;
import com.haminhon.Screen.ChessBoard;
import com.haminhon.gesture.Calculation;


import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by nguyen gon on 2015/12/26.
 */
public class OpeningSign extends Actor {

    private ChessBoard board;
    private final Texture texture = new Texture(Gdx.files.internal("data/openSign.png"));
    private Vector2 boardCoordOfTrap = new Vector2();
    private Vector2 screenCoordOfTrap = new Vector2();
    public static int trapSize = ChessBoard.pieceSize;

    public static final short COMPUTER_SIGN = 1;
    public static final short HUMAN_SIGN = 0;
    public  int whatFaction;

    public Vector2 getBoardCoord()
    {
        return boardCoordOfTrap;
    }
    public Vector2 getScreenCoord()
    {
        return screenCoordOfTrap;
    }

    public  void boardToScreenCoord()
    {
        this.screenCoordOfTrap.x = boardCoordOfTrap.x * ChessBoard.distance/4 + ChessBoard.gocX;
        this.screenCoordOfTrap.y = boardCoordOfTrap.y * ChessBoard.distance/4 + ChessBoard.gocY;
    }
    public void setBoardCoordOfTrap(Vector2 newCoord)
    {
        this.boardCoordOfTrap.set(newCoord);
        boardToScreenCoord();
    }
    // Main Constructor
    public OpeningSign(int faction, final ChessBoard board){
        //Stage contains group that contains this Sign
        this.board = board;

        //Phan biet phe nao dang mo
        this.whatFaction = faction;

        board.addActor(this);
        this.setVisible(false);
    }

    // Copy Constructor
    public OpeningSign(OpeningSign existingOpeningSign){

        this.whatFaction = existingOpeningSign.whatFaction;
        this.boardCoordOfTrap.set(existingOpeningSign.boardCoordOfTrap);
        this.screenCoordOfTrap.set(existingOpeningSign.screenCoordOfTrap);
        this.setVisible(existingOpeningSign.isVisible());

        this.setBounds(existingOpeningSign.getX(), existingOpeningSign.getY(), existingOpeningSign.getWidth(), existingOpeningSign.getHeight());
    }

    public void addTrapSign()
    {
        if(AI.putTrap)
        {
            switch (whatFaction)
            {
                case HUMAN_SIGN:
                    boardCoordOfTrap.x = AI.currentTrappingPositon.getRow();
                    boardCoordOfTrap.y = AI.currentTrappingPositon.getColumn();
                    boardToScreenCoord();

                    //board.addActor(this);
                    this.setVisible(true);
                    //Assets.playSound(Assets.openSound);

                    break;
                case COMPUTER_SIGN:
                    boardCoordOfTrap.x = 4 - AI.currentTrappingPositon.getRow();
                    boardCoordOfTrap.y = 4 - AI.currentTrappingPositon.getColumn();
                    boardToScreenCoord();
                    //board.addActor(this);
                    this.setVisible(true);
                    //Assets.playSound(Assets.openSound);
                    commiserateWithHuman();

                    break;
            }
        }
    }

    public void commiserateWithHuman()
    {
        // discard move Touchme Listener
        board.DontTouchMe();

        final ArrayList<Integer> definedSheeps = Calculation.findCommonPoints(AI.sheeps, true);

        for(Integer index : definedSheeps)
        {
            final ChessPiece chessPiece = ChessBoard.pieceCollection.get(index);

            chessPiece.hightLight();

            chessPiece.addListener(chessPiece.justFollowTheRule);

//            System.out.println("HOW MANY LISTENER AT :    "+
//                    chessPiece.getBoardCoord().x + "_"+ chessPiece.getBoardCoord().y+ " IS  "+ chessPiece.getListeners().size);
        }



    }

    public void debugTrap()
    {
//        System.out.println("Vi tri dang mo : "+ AI.currentTrappingPositon.viewString());
//        System.out.println("Danh sach con cuu ung mang : " + AI.sheeps.toString());
       for(Point p : AI.sheeps)
        {
            System.out.println("Openning Sheep: " + p.getRow() + "__"+p.getColumn());
        }
    }

    public void removeTrapSign()
    {
        //if(this.hasParent())   this.remove();
        this.setVisible(false);
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(texture, screenCoordOfTrap.x - trapSize / 2, screenCoordOfTrap.y - trapSize / 2, trapSize, trapSize);
    }


}
