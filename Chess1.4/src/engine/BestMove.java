package engine;

import util.Constants;
import util.Util;

import java.util.ArrayList;

public class BestMove extends Thread{

    public Engine engine;
    public ArrayList<int[]> legalMoves;
    public int size;

    public float finalscore;

    public int[] bestMove;

    public BestMove(Engine engine_,ArrayList<int[]>legalMoves_,int num2Search){
        engine = engine_;
        size = num2Search;
        legalMoves = splice(legalMoves_);
    }

    public ArrayList<int[]> splice(ArrayList<int[]> moves){
        ArrayList<int[]> legalmoves = new ArrayList<>();
        try{
            for(int i=0;i<size;i++){
                legalmoves.add(moves.get(i));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        moves.removeAll(legalmoves);
        return legalmoves;
    }

    public float evaluateCaptures(boolean white){
        char[][] prevBoardChars = Util.copyBoard(engine.board);
        boolean prevWhiteToMove = engine.whiteToMove;
        int prevHalfMove = engine.halfMove;
        int prevFullMove = engine.fullMove;
        String prevHistory = engine.history;
        String prevLastMove = engine.lastMove;
        String prevFen = engine.fen;
        float score,bestScore = white?Float.POSITIVE_INFINITY:Float.NEGATIVE_INFINITY;
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                if(engine.board[i][j] != Constants.EMPTY_CHAR){
                    ArrayList<int[]> captureMoves = engine.generateCaptureMove(engine.board[i][j],new int[]{i,j},Util.getOffset(engine.board[i][j]));
                    for(int[] move:captureMoves){
                        engine.move(new int[]{i,j,move[0],move[1]});
                        score = engine.evaluateBoard(white);
                        bestScore = white?Math.min(score,bestScore):Math.max(score,bestScore);
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
        return bestScore;
    }

    public float minimax(float alpha,float beta,int depth,boolean maximizing){
        if(engine.checkMate(true)){
            return Float.POSITIVE_INFINITY;
        }else if(engine.checkMate(false)){
            return Float.NEGATIVE_INFINITY;
        }
//        else if(engine.isDraw()){
//            return 0.0f;
//        }
        else if(depth==0){
            return engine.evaluate(false);
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
                        score = minimax(alpha,beta,depth-1,!maximizing);
                        if(maximizing){
                            bestScore = Math.max(score,bestScore);
                            alpha = Math.max(alpha,score);
                        }else{
                            bestScore = Math.min(score,bestScore);
                            beta = Math.min(beta,score);
                        }
                        engine.board = Util.copyBoard(prevBoardChars);
                        engine.history = prevHistory;
                        engine.lastMove = prevLastMove;
                        engine.whiteToMove = prevWhiteToMove;
                        engine.halfMove = prevHalfMove;
                        engine.fullMove = prevFullMove;
                        engine.fen = prevFen;
                        if(beta<=alpha){
                            return bestScore;
                        }
                    }
                    legalMoves.clear();
                }
            }
        }
        return bestScore;
    }

    private int[] getBestMove(){
        char[][] prevBoardChars = Util.copyBoard(engine.board);
        boolean prevWhiteToMove = engine.whiteToMove;
        int prevHalfMove = engine.halfMove;
        int prevFullMove = engine.fullMove;
        String prevHistory = engine.history;
        String prevLastMove = engine.lastMove;
        String prevFen = engine.fen;
        float score,bestScore = prevWhiteToMove?Float.POSITIVE_INFINITY:Float.NEGATIVE_INFINITY;
        int[] best=null;
        for(int[] move:legalMoves){
            engine.move(move);
            score = minimax(Float.NEGATIVE_INFINITY,Float.POSITIVE_INFINITY, Constants.SEARCH_DEPTH,prevWhiteToMove);
            if(prevWhiteToMove){
                if(score<bestScore){
                    bestScore = score;
                    best = move;
                    finalscore = bestScore;
                }
            }else{
                if(score>bestScore){
                    bestScore = score;
                    best = move;
                    finalscore = bestScore;
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
        return best;
    }

    public void run(){
        bestMove = getBestMove();
    }
//    public static void main(String[] args) {
//        Engine engine = new Engine(Constants.STARTING_FEN);
//
//        ArrayList<int[]> moves = engine.getLegalMoves();
//        BestMove array[] = new BestMove[3];
//        BestMove bm = new BestMove(engine,moves,moves.size()/2);
//        BestMove bm2 = new BestMove(engine.copy(),moves,moves.size()/2);
//        BestMove bm3 = new BestMove(engine.copy(),moves,moves.size());
//        array[0] = bm;
//        array[1] = bm2;
//        array[2] = bm3;
//        bm.start();
//        bm2.start();
//        bm3.start();
//        long start = System.nanoTime();
//        while(bm.isAlive()||bm2.isAlive()||bm3.isAlive()){
//            System.out.print("");
//        }
//        System.out.println("Time took to search depth "+Constants.SEARCH_DEPTH+" : "+ (System.nanoTime()/1000000-start/1000000)+" ms");
//        float bestscore = Math.min(Math.min(bm.finalscore,bm2.finalscore),bm2.finalscore);
//        for(int i=0;i<3;i++)
//        {
//            if(array[i].finalscore==bestscore){
//                System.out.println(Util.parseMove(array[i].bestMove));
//                break;
//            }
//        }
//
//    }
}
