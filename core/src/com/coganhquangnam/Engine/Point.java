package com.coganhquangnam.Engine;

import java.util.ArrayList;

/**
 * Created by nguyen gon on 2015/11/07.
 */
public class Point {
    private int row;
    private int column;
    private String value = AI.chessBoard[row][column];
    public boolean possible = false; // tiep xuc voi cung phe
    public boolean sure = false;  //tiep xuc voi khoang trong
    public boolean isTrapOpenable = false;
    public ArrayList<Point> relatedPoint ;
    public ArrayList<Point> carnivorePoints;

    public Point(int row, int column)
    {
        this.relatedPoint = new ArrayList<Point>();
        this.row = row;
        this.column = column;
        this.value  = AI.chessBoard[row][column];
        if(checkSure())
        {
            this.sure = true;
            //this.possible = true;
        }


    }
// Copy Constructor
    public Point(Point otherPoint)
    {
        this.row = otherPoint.row;
        this.column = otherPoint.column;

    }

    public String viewString()
    {
        return " " + row + "___" +column;
    }

    public void checkPossible()
    {

        if((row+column)%2 == 0)
        {
            //Quet xem co cho thoat khong
            for(int i=-1; i<=1; i++)
                for(int j=-1; j<=1; j++)
                {
                    if(i!=0 || j!=0)
                    {
                        try
                        {
                            if(this.value.equals(AI.chessBoard[row+i][column+j]))
                            {
                                relatedPoint.add(new Point(row+i, column+j));
                                //carnivorePoints.add(new Point(row+i, column+j));
                                this.possible = true;
                            }

                        }
                        catch(Exception e) {}
                        try
                        {
                            if("A".equals(AI.chessBoard[row+i][column+j]) && "A".equals(AI.chessBoard[row-i][column-j]))
                            {
                                this.isTrapOpenable = true;
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
                    if(this.value.equals(AI.chessBoard[row+k][column])  )
                    {
                        relatedPoint.add(new Point(row+k, column));
                        //carnivorePoints.add(new Point(row+k, column));
                        this.possible = true;
                    }
                }
                catch(Exception e) {}
                try
                {
                    if("A".equals(AI.chessBoard[row+k][column]) && "A".equals(AI.chessBoard[row-k][column]))
                    {
                        this.isTrapOpenable = true;
                    }
                }
                catch(Exception e) {}
                try
                {
                    if(this.value.equals(AI.chessBoard[row][column+k]))
                    {
                        relatedPoint.add(new Point(row, column+k));
                        this.possible = true;
                    }
                }
                catch(Exception e) {}
                try
                {
                    if("A".equals(AI.chessBoard[row][column+k]) && "A".equals(AI.chessBoard[row][column-k]))
                    {
                        this.isTrapOpenable = true;
                    }
                }
                catch(Exception e) {}
            }
        }

    }
    public boolean checkSure()
    {
        if((row+column)%2 == 0)
        {
            //Quet xem co cho thoat khong
            for(int i=-1; i<=1; i++)
                for(int j=-1; j<=1; j++)
                {
                    if(i!=0 || j!=0)
                    {
                        try
                        {
                            if(" ".equals(AI.chessBoard[row+i][column+j]))
                            {
                                //this.sure = true;
                                return true;
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
                    if(" ".equals(AI.chessBoard[row+k][column]) )
                        //this.sure = true;
                        return true;
                }
                catch(Exception e) {}
                try
                {
                    if(" ".equals(AI.chessBoard[row][column+k]))
                        //this.sure = true;
                        return true;
                }
                catch(Exception e) {}
            }
        }
        return false;
    }

    public int getRow()
    {
        return row;
    }
    public int getColumn()
    {
        return column;
    }
    public String getValue()
    {
        return this.value;
    }
    public String getEnemy()
    {
        String enemy="" ;
        if(this.value.equals("A"))	enemy = "a";
        if(this.value.equals("a"))	enemy = "A";
        return enemy;
    }

    public void setValue(String string)
    {
        this.value = string;
    }
}

