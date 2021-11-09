package engine;

import util.Constants;
import util.Util;

import java.util.ArrayList;

public class AI {

    /*
    * to reduce difficulty level, lower the value of SEARCH_DEPTH in Constants.java file
    * */

    Engine engine;

    public AI(Engine engine){
        this.engine = engine;
    }

    public String getBestMove(){
        long start = System.nanoTime();
        String bestMove=null;
        boolean white = engine.whiteToMove;
        float score,bestScore = white?Float.MAX_VALUE:-Float.MAX_VALUE;
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                char piece = engine.board[i][j];
                if(piece != Constants.EMPTY_CHAR &&( (white&&Util.isUpperCase(piece))||(!white&&!Util.isUpperCase(piece)))){
                    int[] position = new int[]{i,j};
                    ArrayList<int[]> legalMoves = engine.generateMove(piece,position,Util.getOffset(piece));
                    char[][] prevBoardChars = Util.copyBoard(engine.board);
                    boolean prevWhiteToMove = engine.whiteToMove;
                    int prevHalfMove = engine.halfMove;
                    int prevFullMove = engine.fullMove;
                    String prevHistory = engine.history;
                    String prevLastMove = engine.lastMove;
                    String prevFen = engine.fen;
                    for (int[] move : legalMoves){
                        engine.move(position,move);
                        score = minimax(-Float.MAX_VALUE,Float.MAX_VALUE,Constants.SEARCH_DEPTH,white);
                        if(white){
                            if(score<bestScore){
                                bestScore = score;
                                bestMove = Util.toString(position)+Util.toString(move);
                                System.out.println(bestScore+" "+Util.parseMove(position,move));
                            }
                        }else{
                            if(score>bestScore){
                                bestScore = score;
                                bestMove = Util.toString(position)+Util.toString(move);
                                System.out.println(bestScore+" "+Util.parseMove(position,move));
                            }
                        }
                        engine.board = Util.copyBoard(prevBoardChars);
                        engine.history = prevHistory;
                        engine.lastMove = prevLastMove;
                        engine.whiteToMove = prevWhiteToMove;
                        engine.halfMove = prevHalfMove;
                        engine.fullMove = prevFullMove;
                        engine.fen = prevFen;
                    }
                }
            }
        }
        System.out.println("Time took to search depth "+Constants.SEARCH_DEPTH+" : "+ (System.nanoTime()/1000000-start/1000000)+" ms");
        return bestMove == null? "":bestMove;
    }


    public float minimax(float alpha,float beta,int depth,boolean maximizing){
        if(engine.checkMate(true)){
            return Float.MAX_VALUE;
        }else if(engine.checkMate(false)){
            return -Float.MAX_VALUE;
        }else if(depth==0){
            return engine.evaluateBoard(false);
        }
        float score,bestScore = maximizing?-Float.MAX_VALUE:Float.MAX_VALUE;
        for (int i = 0; i< Constants.COLUMNS; i++){
            for(int j=0;j<Constants.ROWS;j++){
                char piece = engine.board[i][j];
                if(piece != Constants.EMPTY_CHAR && ((engine.whiteToMove&&Util.isUpperCase(piece))||(!engine.whiteToMove&&!Util.isUpperCase(piece)))){
                    int[] position = new int[]{i,j};
                    ArrayList<int[]> legalMoves = engine.generateMove(piece,position,Util.getOffset(piece));
                    char[][] prevBoardChars = Util.copyBoard(engine.board);
                    boolean prevWhiteToMove = engine.whiteToMove;
                    int prevHalfMove = engine.halfMove;
                    int prevFullMove = engine.fullMove;
                    String prevHistory = engine.history;
                    String prevLastMove = engine.lastMove;
                    String prevFen = engine.fen;
                    for (int[] move : legalMoves){
                        engine.move(position,move);
                        score =minimax(alpha,beta,depth-1,!maximizing);
                        boolean prune = false;
                        if(maximizing){
                            bestScore = Math.max(score,bestScore);
                            alpha = Math.max(alpha,bestScore);
                        }else{
                            bestScore = Math.min(score,bestScore);
                            beta = Math.min(beta,bestScore);
                        }
                        if(beta<=alpha){
                            prune = true;
                        }
                        engine.board = Util.copyBoard(prevBoardChars);
                        engine.history = prevHistory;
                        engine.lastMove = prevLastMove;
                        engine.whiteToMove = prevWhiteToMove;
                        engine.halfMove = prevHalfMove;
                        engine.fullMove = prevFullMove;
                        engine.fen = prevFen;
                        if(prune){
                            return bestScore;
                        }
                    }
                }
            }
        }
        return bestScore;
    }

    public String getOutput(boolean white){
        String moves="";
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                if(engine.board[i][j] != Constants.EMPTY_CHAR && ((!white&&!Util.isUpperCase(engine.board[i][j])))||(white&&Util.isUpperCase(engine.board[i][j]))){
                    char piece = engine.board[i][j];
                    int[] position = new int[]{i,j};
                    ArrayList<int[]> legalMoves = engine.generateMove(piece,position,Util.getOffset(piece));
                    for(int[] positions:legalMoves){
                        moves+=(Util.toString(position)+Util.toString(positions))+"\n";
                    }
                }
            }
        }
        return moves;
    }
}
