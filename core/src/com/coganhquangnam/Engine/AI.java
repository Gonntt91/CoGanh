package com.haminhon.Engine;

import com.badlogic.gdx.math.Vector2;


import javax.swing.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class AI {
	//////////////////////////////////////////////////////////
	public static Deque<BoardStatus> stack = new ArrayDeque<BoardStatus>();
	public static int globalDepth=3;

	public static String haveToMoveList = "";
	public static String[][] chessBoard=
		{
				{"A","A","A","a","a"},
				{"A"," "," ", " ","a"},
				{"A"," "," ", " ","a"},
				{"A"," "," "," ","a"},
				{"A","A","a","a","a"}
				
		};
    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	//Trang thai cua nuoc di
	public static boolean ganh = false, bopchit=false, putTrap = false;
	//Vi tri dang mo
	public static Point currentTrappingPositon;
	//Cac quan co ngay ngo
	public static ArrayList<Point> sheeps;

    //////////////////////////////////////////////////////////////////////
	public static String alphaBeta(int depth, int beta, int alpha, String move, int player)
	{

		String list=posibleMoves();
		if (depth==0 || list.length()==0) //khong con duong nao de di, hoac chieu bi
		{
			return move+( Rating.rate(depth)   *      (player*2-1) );  //tan cung cua de quy
		}
		list=sortMoves(list);
		player=1-player;//either 1 or 0   . 1 is human. 0 is Computer
		for (int i=0;i<list.length();i+=5)
		{
			makeMove(list.substring(i,i+5));
			flipBoard();
			String returnString=alphaBeta(depth-1, beta, alpha, list.substring(i,i+5), player);
			int value=Integer.valueOf(returnString.substring(5));  // diem so cua nuoc di nay
			flipBoard();
			undoMove();
			if (player==0)
			{
				if (value<=beta)
				{
					beta=value;
					if (depth==globalDepth)
					{
						move=returnString.substring(0,5);

					}
				}
			}
			else
			{
				if (value>alpha)
				{
					alpha=value;
					if (depth==globalDepth)
					{
						move=returnString.substring(0,5);

					}
				}
			}
			//////////////////////////////////
			//if(depth==4) System.out.println("move: "+move+" ,value: "+value+", alpha: "+alpha+", beta: "+beta+", depth: "+depth+", player: "+player);

			if (alpha>=beta)
			{
				if (player==0) {return move+beta;} else {return move+alpha;}
			}
		}
		if (player==0) {return move+beta;} else {return move+alpha;}

	}

	public static void flipBoard()
	{
		String temp;
		for(int step=0; step<13; step++)
		{
			int row = step/5;
			int column= step%5;

			temp = chessBoard[row][column];
			chessBoard[row][column] =  chessBoard[4-row][4-column];
			chessBoard[4-row][4-column] = temp;

		}
		for(int step=0; step<25; step++)
		{
			int row = step/5;
			int column= step%5;

			if("A".equals(chessBoard[row][column]))
				chessBoard[row][column] = "a";
			else if("a".equals(chessBoard[row][column]))
				chessBoard[row][column] = "A";

		}


	}
	public static String posibleMoves()
	{
		String list = "";
		if(putTrap)
		{
			list = haveToMoveList;

		}
		else
		{
			for(int i=0; i<5; i++)
				for(int j=0; j<5; j++)
				{
					if("A".equals( chessBoard[i][j]) )
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
											if(" ".equals(chessBoard[i+r][j+c]))
											{
												list = list + i+j+(i+r)+(j+c)+"X";
												//list = list + i+j+(i+r)+(j+c)+'O';
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
									if(" ".equals(chessBoard[i][j+k]))
									{
										list = list+i+j+i+(j+k)+"X";
										//list = list+i+j+i+(j+k)+'O';
									}
								}
								catch(Exception e) {}
								try
								{
									if(" ".equals(chessBoard[i+k][j]))
									{
										list = list + i+j+(i+k)+j+"X";
										//list = list + i+j+(i+k)+j+'O';
									}
								}
								catch(Exception e) {}
							}
						}
					}
				}
		}
		return list;
	}

	public static void makeMove(String move)
	{

		//Sao Luu Ban Co truoc khi di
		BoardStatus   b = new BoardStatus();
		b.saveBoard(chessBoard);
		stack.push(b);

		int oldRow = Character.getNumericValue(move.charAt(0));
		int oldColumn = Character.getNumericValue(move.charAt(1));
		int curRow = Character.getNumericValue(move.charAt(2));
		int curColumn = Character.getNumericValue(move.charAt(3));
		// Nham phan biet la nen ganh-bopchit hay mo thi co loi hon cho AI
		char clarify = move.charAt(4);
/*		int oppositeRow = 2*oldRow - curRow;
		int oppositeColumn = 2*oldColumn - curColumn;*/
		//String faction = chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];

		//System.out.println(Character.getNumericValue(move.charAt(0)) + "\t" + Character.getNumericValue(move.charAt(1)) + "\t" + Character.getNumericValue(move.charAt(2)) + "\t" + Character.getNumericValue(move.charAt(3)));
		chessBoard[curRow][curColumn] = chessBoard[oldRow][oldColumn];
		chessBoard[oldRow][oldColumn] = " ";

		// Quan di vao cho ganh
		/*String faction = chessBoard[curRow][curColumn];
		String enemy = (faction=="A")? "a":"A";*/
		String faction ="A";
		String enemy = "a";

		// Code cu khi chua co Button

		// Neu ban mo thi ban khong ganh-bopchit, neu ban ganh-bopchit thi ban khong mo;
		ganh = Shoulder(oldRow, oldColumn, curRow, curColumn, faction, enemy) ;
		bopchit = staleMate(faction, enemy);
        putTrap = false;
        //reset isOpening and haveToMoveList

        haveToMoveList = "";
		//Uu tien cho nuoc Ganh va Bopchit hon nuoc MO RA
		//Bay gio co button OPEN TRAP de xet do uu tien khac.
		if(!(ganh || bopchit))
		{

			// Kiem tra xem co phai dang MO RA hay khong
			putTrap = vesicotomy(new Point(oldRow, oldColumn));
		}
		// Code moi khi da co Button
		/*if(isHumanMoving)
		{
			if(Controller.isButtonPressed)
			{
				putTrap = vesicotomy(new Point(oldRow, oldColumn));
			}
			else
			{
				ganh = Shoulder(oldRow, oldColumn, curRow, curColumn, faction, enemy) ;
				bopchit = staleMate(faction, enemy);
			}
		}
		else
		{
			ganh = Shoulder(oldRow, oldColumn, curRow, curColumn, faction, enemy) ;
			bopchit = staleMate(faction, enemy);

			//Uu tien cho nuoc Ganh va Bopchit hon nuoc MO RA
			//Bay gio co button OPEN TRAP de xet do uu tien khac.
			if(!(ganh || bopchit))
			{
				// Kiem tra xem co phai dang MO RA hay khong
				putTrap = vesicotomy(new Point(oldRow, oldColumn));
			}
		}*/

	}
	public static void undoMove()
	{
		if(!stack.isEmpty())
		{
			BoardStatus b = stack.pop();
			for(int i=0; i<5; i++)
				for(int j=0; j<5; j++)
				{
					chessBoard[i][j] = b.getBoard()[i][j];
				}
		}
	}
	private static boolean Shoulder(int oldRow, int oldColumn, int curRow, int curColumn, String faction,
									String enemy)
	{
		boolean ganh = false;
		// TODO Auto-generated method stub
		if((curRow+curColumn)%2==0)
		{
			try
			{
				if((chessBoard[curRow][curColumn+1] == enemy) && (chessBoard[curRow][curColumn-1] == enemy))
				{
					chessBoard[curRow][curColumn+1] = faction;
					chessBoard[curRow][curColumn-1] = faction;
					ganh = true;
				}
			}
			catch(Exception e) {}
			try
			{
				if((chessBoard[curRow+1][curColumn] == enemy) && (chessBoard[curRow-1][curColumn] == enemy))
				{
					chessBoard[curRow+1][curColumn] = faction;
					chessBoard[curRow-1][curColumn] = faction;
					ganh = true;
				}
			}
			catch(Exception e ) {}
			try
			{
				if((chessBoard[curRow+1][curColumn+1] == enemy) && (chessBoard[curRow-1][curColumn-1] == enemy))
				{
					chessBoard[curRow+1][curColumn+1] = faction;
					chessBoard[curRow-1][curColumn-1] = faction;
					ganh = true;
				}
			}
			catch(Exception e) {}
			try
			{
				if((chessBoard[curRow-1][curColumn+1] == enemy) && (chessBoard[curRow+1][curColumn-1] == enemy))
				{
					chessBoard[curRow+1][curColumn-1] = faction;
					chessBoard[curRow-1][curColumn+1] = faction;
					ganh = true;
				}
			}
			catch(Exception e) {}
		}
		else
		{

			if(oldColumn == curColumn)
			{
				try
				{
					if((chessBoard[curRow][curColumn-1] == enemy) && (chessBoard[curRow][curColumn+1] == enemy))
					{
						chessBoard[curRow][curColumn-1] = faction;
						chessBoard[curRow][curColumn+1] = faction;
						ganh = true;
					}
				}
				catch(Exception e) {}
			}
			else
			{
				try
				{
					if((chessBoard[curRow-1][curColumn] == enemy) && (chessBoard[curRow+1][curColumn] == enemy))
					{
						chessBoard[curRow-1][curColumn] = faction;
						chessBoard[curRow+1][curColumn] = faction;
						ganh = true;
					}
				}
				catch(Exception e) {}
			}

		}

		return ganh;
	}

	public static boolean staleMate(String faction, String enemy)
	{
		boolean bopchit = false;

		for(int row=0; row<5; row++)
			for(int column=0; column<5; column++)
			{
				if(enemy.equals(chessBoard[row][column]))
				{

					ArrayList<Point> flock = new ArrayList<Point>();
					Point curPoint ;
					Point eachPoint;
					boolean standStill = true;

					curPoint = new Point(row, column);
					curPoint.checkPossible();

					if(curPoint.sure == true)
						continue;
					if(curPoint.possible == true)
					{
						flock.add(curPoint);
						//flock.addAll(curPoint.relatedPoint);
						flock = heavenOrHell(flock, curPoint);
						if(flock != null)
						{
							for(int w=0; w<flock.size(); w++)
							{
								eachPoint = flock.get(w);
								if(eachPoint.sure == true)
								{
									standStill = false;
									break;
								}
							}
							if(standStill)
							{
								for(int w=0; w<flock.size(); w++)
								{
									chessBoard[flock.get(w).getRow()][flock.get(w).getColumn()] = faction;
								}
								bopchit = true;
							}

						}
					}
					else
					{
						chessBoard[row][column] = faction;
						bopchit = true;
					}
				}
			}
		return bopchit;
	}
	public static ArrayList<Point> heavenOrHell(ArrayList<Point> group, Point originalPoint)
	{
		originalPoint.checkPossible();

		ArrayList<Point> branch = originalPoint.relatedPoint;

/*		for(int w=0; w<group.size(); w++)
			{
				Point p = group.get(w);
				if(p.sure == true)
					return null;
			}*/
		for(int w=0; w<branch.size(); w++)
		{
			Point nextPoint = branch.get(w);
			// Neu nhu nextPOint khac het tat ca cac diem trong group thi them no vao group

			boolean isNewOne = true;
			for(int q=0; q<group.size(); q++)
			{
				if(nextPoint.getRow() == group.get(q).getRow() && nextPoint.getColumn() == group.get(q).getColumn())
				{
					isNewOne = false;
				}
			}
			if(isNewOne)
			{

				group.add(nextPoint);
				group = heavenOrHell(group, nextPoint);
			}
		}

		// Xem thu phai Diem Cut hay KHong?


		return group;

	}
	public static boolean vesicotomy(Point currentOpeningPosition)
	{
		boolean openSuccessful = false;
		currentOpeningPosition.setValue("a");
		currentOpeningPosition.checkPossible();

		if(currentOpeningPosition.isTrapOpenable)
		{
			if(currentOpeningPosition.possible)
			{
				//isOpening = true;
				if(!currentOpeningPosition.relatedPoint.isEmpty())
				{
					openSuccessful = true;
					// Tim ra duoc vi tri dang mo
					currentTrappingPositon = currentOpeningPosition;
					sheeps = currentOpeningPosition.relatedPoint;
					//////////
					for(Point p : currentOpeningPosition.relatedPoint)
					{
						// them actor
						//the Chosen Points and the trapping Position

						//Vi moi khi chuyen luot di tu Computer -Human or Human-Computer thi
						// toa do cua 1 diem dieu phai qua phep FlipBoard (doi xung tam)
						haveToMoveList = haveToMoveList+ (4-p.getRow())+(4-p.getColumn())+
								(4-currentOpeningPosition.getRow())+(4-currentOpeningPosition.getColumn())+"W";
						//AIBoard.addActor(new OpeningSign(new Vector2(4-currentOpeningPosition.getRow(), 4-currentOpeningPosition.getColumn())));
					}
				}
			}
		}

		return  openSuccessful;
	}
	public static boolean isWinLose(String faction)
	{

		boolean lose = true;
		for(short i=0; i<5; i++)
			for(short j=0; j<5; j++ )
			{
				if("A".equals(chessBoard[i][j]))
					lose= false;
			}
		if(lose)
		{

			if(faction.equals("human"))
			{
				//System.out.println("HUMAN PLAYER WINS");
				JOptionPane.showMessageDialog(null, "HUMAN PLAYER WINS");
			}

			if(faction.equals("computer")) JOptionPane.showMessageDialog(null, "COMPUTER'S ARTIFICAL INTELLIGENT WINS");
		}
		return lose;
	}
	public static String sortMoves(String list)
	{
		int[] score=new int [list.length()/5];
		for (int i=0;i<list.length();i+=5) {
			makeMove(list.substring(i, i+5));
			score[i/5]=-Rating.rate(0);
			undoMove();
		}
		String newListA="", newListB=list;
		for (int i=0;i<Math.min(6, list.length()/5);i++) {//first few moves only
			int max=-1000000, maxLocation=0; // gia tri khoi tao cua max la -1000000 tuong duong voi am vo cung
			for (int j=0;j<list.length()/5;j++)
			{
				if (score[j]>max) {max=score[j]; maxLocation=j;} // tim gia tri lon nhat
			}
			score[maxLocation]=-1000000;
			newListA+=list.substring(maxLocation*5,maxLocation*5+5); // nuoc di duoc diem cao nhat
			newListB=newListB.replace(list.substring(maxLocation*5,maxLocation*5+5), "");
		}
		return newListA+newListB;   // chuyen toi da 6 nuoc di co diem cao nhat ra dau tien cua chuoi
	}

	// Phai tim ra error
	public static void printForDebug()
	{

		for(short i=4; i>=0; i--)
		{
			System.out.println();
			for(short j=0; j<5; j++ )
			{
				if(chessBoard[j][i].equals("a") || chessBoard[j][i].equals("A"))
				System.out.print(chessBoard[j][i] + " ");
				else
					System.out.print("\u25A1" + " ");
			}

		}
		System.out.println();
	}
}
