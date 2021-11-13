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

    public int[] BestMove(){
        ArrayList<int[]> moves = engine.getLegalMoves();
        int size = moves.size();
        int movesperthread = size/Constants.SEARCH_DEPTH;
        BestMove[] bestMoves = new BestMove[Constants.SEARCH_DEPTH];
        for(int i=0;i<bestMoves.length;i++){
            bestMoves[i] = new BestMove(engine.copy(),moves,movesperthread);
            bestMoves[i].start();
        }
        boolean complete = false;
        long start = System.nanoTime();
        while(!complete){
            int count = 0;
            for(int i=0;i<bestMoves.length;i++){
                if(!bestMoves[i].isAlive()){
                    count++;
                }
            }
            if(count == bestMoves.length){
                complete = true;
            }
            System.out.print("");
        }
        System.out.println("Time took to search depth "+Constants.SEARCH_DEPTH+" : "+ (System.nanoTime()/1000000-start/1000000)+" ms");
        int[] best=null;
        float bestScore = engine.whiteToMove?Float.POSITIVE_INFINITY:Float.NEGATIVE_INFINITY;
        for(int i=0;i<bestMoves.length;i++){
            if(engine.whiteToMove){
                if(bestMoves[i].finalscore<bestScore){
                    bestScore = bestMoves[i].finalscore;
                    best = bestMoves[i].bestMove;
                }
            }else{
                if(bestMoves[i].finalscore>bestScore){
                    bestScore = bestMoves[i].finalscore;
                    best = bestMoves[i].bestMove;
                }
            }
        }
        return best;
    }

    public String getBestMove(){
        long start = System.nanoTime();
        String bestMove=null;
        boolean white = engine.whiteToMove;
        float score,bestScore = white?Float.POSITIVE_INFINITY:Float.NEGATIVE_INFINITY;
        ArrayList<int[]> legalMoves = null;
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                char piece = engine.board[i][j];
                if(piece != Constants.EMPTY_CHAR &&( (white&&Util.isUpperCase(piece))||(!white&&!Util.isUpperCase(piece)))){
                    int[] position = new int[]{i,j};
                    legalMoves = engine.generateMove(piece,position,Util.getOffset(piece));
                    char[][] prevBoardChars = Util.copyBoard(engine.board);
                    boolean prevWhiteToMove = engine.whiteToMove;
                    int prevHalfMove = engine.halfMove;
                    int prevFullMove = engine.fullMove;
                    String prevHistory = engine.history;
                    String prevLastMove = engine.lastMove;
                    String prevFen = engine.fen;
                    for (int[] move : legalMoves){
                        engine.move(new int[]{position[0],position[1],move[0],move[1]});
                        score = minimax(Float.NEGATIVE_INFINITY,Float.POSITIVE_INFINITY,Constants.SEARCH_DEPTH,white);
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
                    legalMoves.clear();
                }
            }
        }
        System.out.println("Time took to search depth "+Constants.SEARCH_DEPTH+" : "+ (System.nanoTime()/1000000-start/1000000)+" ms");
        return bestMove == null? "":bestMove;
    }


    public float minimax(float alpha,float beta,int depth,boolean maximizing){
        if(engine.checkMate(true)){
            return Float.POSITIVE_INFINITY;
        }else if(engine.checkMate(false)){
            return Float.NEGATIVE_INFINITY;
        }else if(depth==0){
            return engine.evaluateBoard(false);
        }
        float score,bestScore = maximizing?Float.NEGATIVE_INFINITY:Float.POSITIVE_INFINITY;
        ArrayList<int[]> legalMoves = null;
        for (int i = 0; i< Constants.COLUMNS; i++){
            for(int j=0;j<Constants.ROWS;j++){
                char piece = engine.board[i][j];
                if(piece != Constants.EMPTY_CHAR && ((engine.whiteToMove&&Util.isUpperCase(piece))||(!engine.whiteToMove&&!Util.isUpperCase(piece)))){
                    int[] position = new int[]{i,j};
                    legalMoves = engine.generateMove(piece,position,Util.getOffset(piece));
                    char[][] prevBoardChars = Util.copyBoard(engine.board);
                    boolean prevWhiteToMove = engine.whiteToMove;
                    int prevHalfMove = engine.halfMove;
                    int prevFullMove = engine.fullMove;
                    String prevHistory = engine.history;
                    String prevLastMove = engine.lastMove;
                    String prevFen = engine.fen;
                    for (int[] move : legalMoves){
                        engine.move(new int[]{position[0],position[1],move[0],move[1]});
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
                    legalMoves.clear();
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
                        moves+=(Util.parseMove(position,positions))+"\n";
                    }
                }
            }
        }
        return moves;
    }

}
