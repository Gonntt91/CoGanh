package com.coganhquangnam.Engine;

/**
 * Created by nguyen gon on 2015/11/07.
 */
public class BoardStatus {

    private String[][] board = new String[5][5];

    public void saveBoard(String[][] chessBoard)
    {
/*		for(int i=0; i<chessBoard.length; i++)
			for(int j=0; j<chessBoard[i].length; j++)
				{
					board[i][j] = chessBoard[i][j];
				}*/
        for(int i=0; i<5; i++)
            for(int j=0; j<5; j++)
            {
                board[i][j] = chessBoard[i][j];
            }

    }
    public String[][] getBoard()
    {
        return board;
    }


}
