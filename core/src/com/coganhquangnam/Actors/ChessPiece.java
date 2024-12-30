package com.haminhon.Actors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.haminhon.Engine.AI;
import com.haminhon.Screen.ChessBoard;
import com.haminhon.gesture.Assets;
import com.haminhon.gesture.Behaviour;
import com.haminhon.gesture.BoardMemory;


import java.util.ArrayList;

/**
 * Created by nguyen gon on 2015/10/31.
 */
public class ChessPiece extends Actor {

// Variable 's declaration
//chessboard
    public static ChessBoard board;

// Coordinates and image
    private final Vector2 boardCoord = new Vector2();
    private  final Vector2 screenCoord = new Vector2();
    private Texture texture;
    public int size;
//listener
    public InputListener touchedYou;
    public InputListener justFollowTheRule;
    public InputListener debuggingShow;
// related next positions
    public static ArrayList<NextPosition> listNextPosition = new ArrayList<NextPosition>();
// Selected or Not
    public boolean selected = false;
    public Selected IamHere;
// Methods for defining
    public  void boardToScreenCoord()
    {
        this.screenCoord.x = boardCoord.x * ChessBoard.distance/4 + ChessBoard.gocX;
        this.screenCoord.y = boardCoord.y * ChessBoard.distance/4 + ChessBoard.gocY;
    }
    public Vector2 getBoardCoord()
    {
        return boardCoord;
    }
    public void setBoardCoord(Vector2 otherBoardCoord){
       this.boardCoord.x = (int) otherBoardCoord.x;
        this.boardCoord.y = (int) otherBoardCoord.y;
        boardToScreenCoord();
    }
    public Vector2 getScreenCoord() { return screenCoord;}
    public void resetFaction()
    {
        int i = (int) getBoardCoord().x;
        int j = (int) getBoardCoord().y;
        if(AI.chessBoard[i][j].equals("a"))
            this.texture = Assets.darkSide;
        if(AI.chessBoard[i][j].equals("A"))
            this.texture = Assets.lightSide;

    }
    private void setListener()
    {
        // TRUONG HOP BINH THUONG
        touchedYou = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                board.removeOldNextPosition();
                listPossible();
                board.showNextPosition();
                prepareToMove();
                return super.touchDown(event, x, y, pointer, button);
            }
        };
        this.addListener(touchedYou);
        // TRong truong hop co MO
        justFollowTheRule = new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                ////////// Sao luu trang thai truoc khi move
                new BoardMemory(board);
                board.DontTouchMe();
                //////////
                ChessPiece currentTouchedPiece = (ChessPiece) event.getListenerActor();
                for(ChessPiece cp : ChessBoard.pieceCollection){
                    cp.removeHightLight();
                    //currentTouchedPiece.removeHightLight();
                }

               // board.AO.debugTrap();

                // a thread with Runnable
                Behaviour.moveInScreen(board, currentTouchedPiece, board.AO.getBoardCoord(), Behaviour.HUMAN_FORCED_RESPONSE);

                Behaviour.moveInBoard(currentTouchedPiece,  board.AO.getBoardCoord(), Behaviour.HUMAN_FORCED_RESPONSE);

                return super.touchDown(event, x, y, pointer, button);
            }
        };

        // For debugging
//        debuggingShow = new InputListener(){
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                ChessPiece chessPiece = (ChessPiece) event.getListenerActor();
//                int i = (int) getBoardCoord().x;
//                int j = (int) getBoardCoord().y;
//                //System.out.println();
//                //System.out.println(i + "_" + j + "_" + AI.chessBoard[i][j] + " co so luong Listener la : " + chessPiece.getListeners().size);
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        };
//        this.addListener(debuggingShow);

    }
    private void setMark()
    {

        this.IamHere = new Selected(this);
        IamHere.setVisible(false);
        board.addActor(IamHere);
    }
//Constructor
    public ChessPiece( Vector2 boardCoord, int size, final ChessBoard board){
        this.board = board;
        this.boardCoord.set(boardCoord);
        this.size = size;

        boardToScreenCoord();
        resetFaction();

        setBounds(screenCoord.x - ChessBoard.squaredistance / 2, screenCoord.y - ChessBoard.squaredistance / 2, ChessBoard.squaredistance, ChessBoard.squaredistance);

        setListener();
        setMark();


    }
// Copy Constructor
    public ChessPiece(ChessPiece existingChessPiece)
    {
        this.boardCoord.set(existingChessPiece.boardCoord);
        this.screenCoord.set(existingChessPiece.screenCoord);
        resetFaction();

        for(EventListener inputListener : existingChessPiece.getListeners())
        {
            this.addListener(inputListener);
        }
        // phai trich xuat AI.board truoc khi trich xuat ChessPiece tu stack
        //Neu khong thi dung truc tiep: this.texture = existingChessPiece.texture

        this.selected = existingChessPiece.IamHere.isVisible();

        this.setBounds(existingChessPiece.getX(), existingChessPiece.getY(), existingChessPiece.getWidth(), existingChessPiece.getHeight());

    }

 //specific methods
    public  void listPossible()
    {
         // VI toa do trong mang 2 chieu va Vi tri tren bang co KHac nhau
        listNextPosition.clear();

        int i = (int) this.boardCoord.x;
        int j = (int) this.boardCoord.y;

        if("A".equals( AI.chessBoard[i][j]) )
        {
            if((i+j) % 2 ==0)
            {
                for(int r=-1; r<=1; r++)
                    for(int c=-1; c<=1; c++)
                    {
                        if(r!=0 || c!=0)
                        {
                            try
                            {
                                if(" ".equals(AI.chessBoard[i+r][j+c]))
                                {
                                    //list = list + i+j+(i+r)+(j+c)+"X";
                                    listNextPosition.add(new NextPosition(new Vector2(i + r, j + c), board));
                                }
                            }
                            catch(Exception e) {}
                        }
                    }
            }
            else
            {
                for(int k=-1; k<=1; k+=2)
                {
                    try
                    {
                        if(" ".equals(AI.chessBoard[i][j+k]))
                        {
                            //list = list+i+j+i+(j+k)+" ";
                            listNextPosition.add(new NextPosition(new Vector2(i , j + k), board));
                        }
                    }
                    catch(Exception e) {}
                    try
                    {
                        if(" ".equals(AI.chessBoard[i+k][j]))
                        {
                            //list = list + i+j+(i+k)+j+" ";
                            listNextPosition.add(new NextPosition(new Vector2(i + k, j), board));
                        }
                    }
                    catch(Exception e) {}
                }
            }
        }


    }
    public void prepareToMove()
    {
        //NextPosition.originChessPiece = this;
        for(NextPosition currentPosition : listNextPosition)
        {

            currentPosition.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    board.removeOldNextPosition();
                    NextPosition currentTouchedPosition = (NextPosition) event.getListenerActor();

                    // PLaying between computer and Human

                    humanMoveToPosition(currentTouchedPosition);
                    // Core of the Game End

                    return super.touchDown(event, x, y, pointer, button);
                }
            });
        }
    }

    public void humanMoveToPosition(NextPosition nextPosition)
    {
        // Sao luu trang thai ban co
        new BoardMemory(board);
             ////////////////////
        board.DontTouchMe();
        //This take a long time
        Behaviour.moveInScreen(board, this, nextPosition.getBoardCoord(), Behaviour.HUMAN_FREE_RESPONSE);
        //Thuc chat la duoc thuc hien truoc roi
        Behaviour.moveInBoard(this, nextPosition.getBoardCoord(), Behaviour.HUMAN_FREE_RESPONSE);

    }

    public void hightLight()
    {
        //this.selected = true;
        this.IamHere.setVisible(true);
    }
    public void removeHightLight()
    {
        //this.selected = false;
        this.IamHere.setVisible(false);
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {

        super.draw(batch, parentAlpha);
        batch.setColor(getColor());
        batch.draw(texture, screenCoord.x - size / 2, screenCoord.y - size/ 2, size, size);
    }
}
