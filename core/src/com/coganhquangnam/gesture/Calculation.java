package com.coganhquangnam.gesture;

import com.badlogic.gdx.math.Vector2;
import com.coganhquangnam.Actors.ChessPiece;
import com.coganhquangnam.Engine.Point;
import com.coganhquangnam.Screen.ChessBoard;


import java.util.ArrayList;

/**
 * Created by nguyen gon on 2016/02/01.
 */
public class Calculation {

    public static int[] analysize(String move)
    {
        int[] result;
        result = new int[4];

        int flipingOldRow =  Character.getNumericValue(move.charAt(0));
        int flipingOldColumn = Character.getNumericValue(move.charAt(1));
        int flipingCurRow = Character.getNumericValue(move.charAt(2));
        int flipingCurColumn = Character.getNumericValue(move.charAt(3));

        result[0] =  4 - flipingOldRow;
        result[1] = 4 - flipingOldColumn;
        result[2] = 4 - flipingCurRow;
        result[3] = 4 - flipingCurColumn;


        return result;
    }
    /**
    * Tim dung chessPiece
    * Tai toa do boardCoord
    *
     */
    public static ChessPiece getPiece(int x, int y)
    {
        ChessPiece resultChessPiece = null;
        int count = 0;


        for(ChessPiece cp : ChessBoard.pieceCollection)
        {

            if((cp.getBoardCoord().x == x ) && (cp.getBoardCoord().y == y))
            {
                count++;
                resultChessPiece = cp;
            }
        }

        if(count == 0)
        {
            System.out.println("There are no ChessPiece at that coordinate " + x + " " + y);
        }
        else if(count == 1 )
        {
            return  resultChessPiece;
        }
        else
        {
            System.out.println("There are" + count +"Chesspiece  like that " + x + " " + y);
        }

        return null;
    }

    /**
     * Find the indexes of some pieces which is determined in a list
     * @param boardIsFlipping define the status of chessBoard
     */
    public static ArrayList<Integer> findCommonPoints(ArrayList<Point> aList, boolean boardIsFlipping )
    {
        ArrayList<Integer> commonPoints = new ArrayList<Integer>();

        for(Point p : aList)
            for(ChessPiece chessPiece : ChessBoard.pieceCollection)
            {
                if(boardIsFlipping == false){
                    if(((int)chessPiece.getBoardCoord().x == p.getRow()) && ((int)chessPiece.getBoardCoord().y == p.getColumn()))
                    {
                       commonPoints.add(new Integer(ChessBoard.pieceCollection.indexOf(chessPiece)));
                    }
                }
                else
                {
                    if(((int)chessPiece.getBoardCoord().x == 4 - p.getRow()) && ((int)chessPiece.getBoardCoord().y == 4 - p.getColumn()))
                    {
                        commonPoints.add(new Integer(ChessBoard.pieceCollection.indexOf(chessPiece)));
                    }
                }
            }

        return commonPoints;
    }

}
