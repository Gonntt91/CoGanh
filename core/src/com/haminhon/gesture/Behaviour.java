package com.haminhon.gesture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.haminhon.Actors.ChessPiece;
import com.haminhon.Engine.AI;
import com.haminhon.Screen.ChessBoard;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by nguyen gon on 2016/01/12.
 */
public class Behaviour {

   public static final int HUMAN_FREE_RESPONSE = 0;
   public static final int HUMAN_FORCED_RESPONSE = 1;
   public static final int COMPUTER_RESPONSE = 2;

    public static void moveInScreen(final ChessBoard board, ChessPiece chessPiece, Vector2 destinationBoard, final int type)
    {
        float destinationX = destinationBoard.x * ChessBoard.distance/4 + ChessBoard.gocX - ChessBoard.pieceSize/2;
        float destinationY = destinationBoard.y * ChessBoard.distance/4 + ChessBoard.gocY - ChessBoard.pieceSize/2;


        chessPiece.addAction(
                sequence(
                        parallel(
                            //first
                            Actions.moveTo(destinationX, destinationY, 1f, Interpolation.pow3),
                            sequence(
                                            Actions.delay(0.7f),
                                            run(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //board.DontTouchMe();
                                                    board.AO.removeTrapSign();
                                                }
                                            })
                                    ),
                            //second
                            run(new Runnable() {
                                @Override
                                public void run() {
                                            if(type == HUMAN_FREE_RESPONSE)
                                            {
                                                board.HO.addTrapSign();
                                            }
                                    board.repaint();
                            }
                        })),
                        run(new Runnable() {
                            @Override
                            public void run() {
                                computerResponse();
                            }
                        }),
                        //third
                        run(new Runnable() {
                            @Override
                            public void run() {
                                //Noi lai  Listener Touch me for all chesspieces of the board
                                board.touchMePlease();
                            }
                        })));

    }

    public static void moveInBoard(ChessPiece chessPiece, Vector2 destination, int type)
    {
        int x1 = (int) chessPiece.getBoardCoord().x;
        int y1 = (int) chessPiece.getBoardCoord().y;
        int x2 = (int) destination.x;
        int y2 = (int) destination.y;

        chessPiece.setBoardCoord(destination);

        String move = "" +x1 + y1 + x2 + y2+" ";

//        switch (type) {
//            case HUMAN_FREE_RESPONSE:
//               System.out.print("HUMAN  FREE MOVE:  " + move);
//                break;
//            case HUMAN_FORCED_RESPONSE:
//                System.out.print("HUMAN  FORCED MOVE:  " + move);
//                break;
//
//        }

        AI.makeMove(move);

        playSound();
        //AI.printForDebug();


    }

    public static void computerResponse()
    {

        AI.flipBoard();
        // KIem tra Thang Thua
        if(!AI.isWinLose("human"))
        {
            substitute();
        }

        AI.flipBoard();
        //AI.printForDebug();
    }

    public static void  substitute()
    {
        String computerResponse = AI.alphaBeta(AI.globalDepth, 1000000, -1000000, "", 0);
        int OldRow = 4 - Character.getNumericValue(computerResponse.charAt(0));
        int OldColumn = 4  - Character.getNumericValue(computerResponse.charAt(1));
        int CurRow = 4 - Character.getNumericValue(computerResponse.charAt(2));
        int CurColumn = 4 - Character.getNumericValue(computerResponse.charAt(3));

        float destinationX = CurRow * ChessBoard.distance/4 + ChessBoard.gocX - ChessBoard.pieceSize/2;
        float destinationY = CurColumn * ChessBoard.distance/4 + ChessBoard.gocY - ChessBoard.pieceSize/2;

        ChessPiece currentChessPieceOfComputer = Calculation.getPiece(OldRow, OldColumn);

        if(currentChessPieceOfComputer != null) {
            currentChessPieceOfComputer.addAction(
                    sequence(
                            run(new Runnable() {
                                @Override
                                public void run() {
                                    ChessPiece.board.DontTouchMe();
                                }
                            }),
                            parallel(
                                        //first
                                        Actions.moveTo(destinationX, destinationY, 1f, Interpolation.pow3In),
                                                sequence(
                                                        Actions.delay(0.7f),
                                                        run(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                ChessPiece.board.HO.removeTrapSign();
                                                            }
                                                        })
                                                )
                                        ,
                                        //second
                                        run(new Runnable() {
                                        @Override
                                        public void run() {
                //                            System.out.println(AI.putTrap);
                                            ChessPiece.board.AO.addTrapSign();

                                            ChessPiece.board.repaint();
                                             }})
                            )
                            ));

        }
        //Move in Board
        currentChessPieceOfComputer.setBoardCoord(new Vector2(CurRow, CurColumn));
//        System.out.println();
//        System.out.println();
//        System.out.print("COMPUTER MOVE IS:   " + OldRow + OldColumn + CurRow + CurColumn);
       AI.makeMove(computerResponse);
        playSound();
    }

    public static  void playSound()
    {
        if(AI.bopchit || AI.ganh )
            Assets.playSound(Assets.hitSound);
        else if(AI.putTrap)
            Assets.playSound(Assets.openSound);
        else
            Assets.playSound(Assets.moveSound);
    }
}
