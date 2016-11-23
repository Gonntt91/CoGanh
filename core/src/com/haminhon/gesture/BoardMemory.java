package com.haminhon.gesture;


import com.haminhon.Actors.ChessPiece;
import com.haminhon.Actors.OpeningSign;
import com.haminhon.Engine.AI;
import com.haminhon.Engine.Point;
import com.haminhon.Screen.ChessBoard;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * Created by nguyen gon on 2016/02/21.
 */
public class BoardMemory {

    private ChessBoard board;
    public static Deque<BoardMemory> stackStatusInStage = new ArrayDeque<BoardMemory>();
    //public static Deque<BoardStatus>   aA_StatusInBoard     = new ArrayDeque<BoardStatus>();

    private ArrayList<ChessPiece> pieceCollectionMemory ;
    private String[][] arrayBoard = new String[5][5];
    private OpeningSign HO, AO;
    private ArrayList<Point> sheeps;

    public BoardMemory(final ChessBoard board)
    {
//        System.out.println("GGGGGGGGGGGGGGGGGGGGGG");
        this.board = board;
        pieceCollectionMemory = new ArrayList<ChessPiece>();
        sheeps = new ArrayList<Point>();

        saveArrayBoard(AI.chessBoard);
        copyAllPiecesIntoThis();
        saveOpeningSign(board.HO, board.AO);
        saveListSheeps();

        BoardMemory.stackStatusInStage.push(this);


    }

    private void saveOpeningSign(OpeningSign otherHO, OpeningSign otherAO)
    {
        this.HO = new OpeningSign(otherHO);
        this.AO = new OpeningSign(otherAO);
    }
    public OpeningSign getHO()
    {
        return HO;
    }
    public OpeningSign getAO()
    {
        return  AO;
    }

    private  void copyAllPiecesIntoThis()
    {
        for(ChessPiece c : ChessBoard.pieceCollection)
        {
            // Hy vong la theo dung thu tu.
            ChessPiece copiedC = new ChessPiece(c);
            pieceCollectionMemory.add(copiedC);
        }
    }
    public ArrayList<ChessPiece> getScreenChessBoard()
    {
        return  this.pieceCollectionMemory;
    }

    private void saveArrayBoard(String[][] chessBoard)
    {

        for(int i=0; i<5; i++)
            for(int j=0; j<5; j++)
            {
                arrayBoard[i][j] = chessBoard[i][j];
            }

    }

    public String[][] getArrayBoard()
    {
        return arrayBoard;
    }
    private void saveListSheeps()
    {
/*        if(AI.sheeps != null) {
            for (Point p : AI.sheeps) {
                Point pCopy = new Point(p);
                sheeps.add(pCopy);
            }
        }*/
    }
    public ArrayList<Point> getSheeps()
    {
        return this.sheeps;
    }




}
