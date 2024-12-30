package com.haminhon.Screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.haminhon.Actors.ChessPiece;
import com.haminhon.Actors.OpeningSign;
import com.haminhon.Actors.PlayBackButton;
import com.haminhon.Engine.AI;



import java.util.ArrayList;

/**
 * Created by NGUYENGON on 2015/09/12.
 */
public class ChessBoard extends Stage {

    //Ve ban co
    public ShapeRenderer shapeRenderer;
    public static final int gocX = 40, gocY = 100, menuAreaHeight = 150, thickness =3;
    public static final int distance = 400;
    public static final int squaredistance = distance / 4;
    public static int pieceSize = 40;

    //actors
    public static ArrayList<ChessPiece> pieceCollection;
    public final OpeningSign HO, AO;

    //Constructor
    public ChessBoard(){
        super(new FitViewport(distance + 2*gocX, distance + gocY  + menuAreaHeight));  // chua biet truoc duoc
        //super(new ExtendViewport(distance, distance));

        //ChessBoard.distance = distance;

        shapeRenderer = new ShapeRenderer();
        pieceCollection = new ArrayList<ChessPiece>();
        initializePieces();
        // Opening Symbol
        AO = new OpeningSign(OpeningSign.COMPUTER_SIGN, this);
        HO = new OpeningSign(OpeningSign.HUMAN_SIGN, this);

        // Nut Undo
        PlayBackButton playBackButton = new PlayBackButton(this);
        this.addActor(playBackButton);
/*        for(ChessPiece cp : pieceCollection)
        {
            System.out.println(cp.getBoardCoord().x + "___" + cp.getBoardCoord().y);
        }*/
    }


    @Override
    public void draw() {
        drawBoard();
         super.draw();
    }

    public void drawBoard()
    {


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        for(int row=0; row<5; row++)
        {
            shapeRenderer.rectLine(gocX, row * squaredistance + gocY, distance + gocX, row * squaredistance + gocY, thickness);
        }
        for(int column=0; column<5; column++)
        {
            shapeRenderer.rectLine(column * squaredistance + gocX, gocY, column * squaredistance + gocX, distance + gocY, thickness);
        }
        shapeRenderer.rectLine(2 * squaredistance + gocX, gocY, gocX, 2 * squaredistance + gocY, thickness);
        shapeRenderer.rectLine(distance + gocX, gocY, gocX, distance + gocY, thickness);
        shapeRenderer.rectLine(distance + gocX, 2 * squaredistance + gocY, 2 * squaredistance + gocX, distance + gocY, thickness);
        shapeRenderer.rectLine(gocX, gocY, distance + gocX, distance + gocY, thickness);
        shapeRenderer.rectLine(2 * squaredistance + gocX, gocY, distance + gocX, 2 * squaredistance + gocY, thickness);
        shapeRenderer.rectLine(gocX, 2 * squaredistance + gocY, 2 * squaredistance + gocX, distance + gocY, thickness);

        shapeRenderer.end();

    }

    public void repaint()
    {
        //Gdx.gl.glClearColor(0,0,0,1);
        for(ChessPiece p : pieceCollection)
        {
            p.resetFaction();
        }

    }
    public void initializePieces()
    {
        ChessPiece chessPiece = null;

        for(int i=0; i<5; i++)
            for(int j=0; j<5; j++)
            {
                //                        if (AI.chessBoard[i][j].equals("a")) {
//                            chessPiece = new ChessPiece(new Vector2(i, j), pieceSize, ChessKind.DARK, this);
//                        }
//                        if (AI.chessBoard[i][j].equals("A")) {
//                            chessPiece = new ChessPiece(new Vector2(i, j), pieceSize, ChessKind.LIGHT, this);
//                        }
                if(AI.chessBoard[i][j].equals("a") || AI.chessBoard[i][j].equals("A")) {
                    chessPiece = new ChessPiece(new Vector2(i, j), pieceSize, this);
                    pieceCollection.add(chessPiece);

                }
                if (chessPiece != null) addActor(chessPiece);

            }
        }

    public  void showNextPosition()
    {

        if(ChessPiece.listNextPosition != null) {
            for (int m = 0; m < ChessPiece.listNextPosition.size(); m++) {
                addActor(ChessPiece.listNextPosition.get(m));
            }
        }
    }
    public void removeOldNextPosition()
    {
        for (int m = 0; m < ChessPiece.listNextPosition.size(); m++) {
            ChessPiece.listNextPosition.get(m).remove();
        }
    }

    // Xoa het cac Listener
    public void DontTouchMe()
    {
        for(ChessPiece cp : pieceCollection)
        {
            for(EventListener e : cp.getListeners())
            {
                // Can than loi co the xay ra tai day !!!
                if(e != cp.debuggingShow )
                                cp.removeListener(e);
            }

        }
    }
    // Tao lai cac Listener cho cac ChessPieces
    public void touchMePlease()
    {
        for(int k=0; k<pieceCollection.size(); k++)
        {
            ChessPiece cp = pieceCollection.get(k);
            cp.addListener(cp.touchedYou);
            //cp.setTouchable(Touchable.enabled);
        }
    }


}
