package com.coganhquangnam.Engine;

/**
 * Created by nguyen gon on 2015/11/07.
 */
public class Rating {
    public static int rate(int depth)
    {
        int sum=0;
        for(int i =0; i < 5; i++)
            for(int j=0; j<5; j++)
            {
                if("A".equals(AI.chessBoard[i][j]))
                    sum += 10;
                if("a".equals(AI.chessBoard[i][j]))
                    sum-= 10;
            }

        return  -(sum );
    }


}
