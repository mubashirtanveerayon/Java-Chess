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

    public int depthReached,leastDepthReached = -1;

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

    public float minimax(float alpha,float beta,int depth,boolean maximizing){
        if(engine.checkMate(true)){
            depthReached = depth;
            return Float.POSITIVE_INFINITY;
        }else if(engine.checkMate(false)){
            depthReached = depth;
            return Float.NEGATIVE_INFINITY;
        }
        else if(engine.fullMove>=100){
            return 0.0f;
        }
        else if(depth==0){
            depthReached = depth;
            return engine.evaluateBoard(false);
        }
        float score,bestScore = maximizing?Float.NEGATIVE_INFINITY:Float.POSITIVE_INFINITY;
//        ArrayList<int[]> legalMoves = null;
//        for (int i = 0; i< Constants.COLUMNS; i++){
//            for(int j=0;j<Constants.ROWS;j++){
//                char piece = engine.board[i][j];
//                if(piece != Constants.EMPTY_CHAR && ((engine.whiteToMove&&Util.isUpperCase(piece))||(!engine.whiteToMove&&!Util.isUpperCase(piece)))){
//                    int[] position = new int[]{i,j};
//                    legalMoves = engine.generateMove(piece,position,Util.getOffset(piece));
//                    char[][] prevBoardChars = Util.copyBoard(engine.board);
//                    boolean prevWhiteToMove = engine.whiteToMove;
//                    int prevHalfMove = engine.halfMove;
//                    int prevFullMove = engine.fullMove;
//                    String prevHistory = engine.history;
//                    String prevLastMove = engine.lastMove;
//                    String prevFen = engine.fen;
//                    for (int[] move : legalMoves){
//                        engine.move(new int[]{position[0],position[1],move[0],move[1]});
//                        score = minimax(alpha,beta,depth-1,!maximizing);
//                        if(maximizing){
//                            bestScore = Math.max(score,bestScore);
//                            alpha = Math.max(alpha,score);
//                        }else{
//                            bestScore = Math.min(score,bestScore);
//                            beta = Math.min(beta,score);
//                        }
//                        engine.board = Util.copyBoard(prevBoardChars);
//                        engine.history = prevHistory;
//                        engine.lastMove = prevLastMove;
//                        engine.whiteToMove = prevWhiteToMove;
//                        engine.halfMove = prevHalfMove;
//                        engine.fullMove = prevFullMove;
//                        engine.fen = prevFen;
//                        if(beta<=alpha){
//                            return bestScore;
//                        }
//                    }
//                    legalMoves.clear();
//                }
//            }
//        }

        char[][] prevBoardChars = Util.copyBoard(engine.board);
        boolean prevWhiteToMove = engine.whiteToMove;
        int prevHalfMove = engine.halfMove;
        int prevFullMove = engine.fullMove;
        String prevHistory = engine.history;
        String prevLastMove = engine.lastMove;
        String prevFen = engine.fen;
        int file,rank,i = 0,j = 0; //i, j represents position of piece
        for(char c:engine.fen.split(" ")[0].toCharArray()){
            if(c == '/'){
                j++;
                i = 0;
            }else if(Character.isDigit(c)){
                i += Integer.parseInt(Character.toString(c));
            }else{
                if(((engine.whiteToMove&&Util.isUpperCase(c))||(!engine.whiteToMove&&!Util.isUpperCase(c)))){
                    int[][] direction = Util.getOffset(c);
                    switch(Util.toUpper(c)){
                        case Constants.WHITE_KING:{
                            for(int[] dir:direction){
                                file = i;
                                rank = j;
                                file+=dir[0];
                                rank+=dir[1];
                                if(Util.isValid(file,rank)){
                                    if(engine.board[file][rank] == Constants.EMPTY_CHAR){
                                        engine.board[file][rank] = c;
                                        engine.board[i][j] = Constants.EMPTY_CHAR;
                                        if(!engine.isKingInCheck(engine.whiteToMove)){
                                            engine.board[i][j] = engine.board[file][rank];
                                            engine.board[file][rank] = Constants.EMPTY_CHAR;
                                            engine.move(new int[]{i,j,file,rank});
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
                                        }else {
                                            engine.board[i][j] = engine.board[file][rank];
                                            engine.board[file][rank] = Constants.EMPTY_CHAR;
                                        }
                                    }else{
                                        if(!Util.isAlly(c,engine.board[file][rank])){
                                            char piece = engine.board[file][rank];
                                            engine.board[file][rank] = engine.board[i][j];
                                            engine.board[i][j] = Constants.EMPTY_CHAR;
                                            if(!engine.isKingInCheck(engine.whiteToMove)){
                                                engine.board[i][j] = c;
                                                engine.board[file][rank] = piece;
                                                engine.move(new int[]{i,j,file,rank});
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
                                            }else {
                                                engine.board[i][j] = c;
                                                engine.board[file][rank] = piece;
                                            }
                                        }
                                    }
                                }
                            }
                            rank = engine.whiteToMove ? 7:0;
                            boolean[] castling = engine.generateCastlingMove(engine.whiteToMove);
                            if(castling[0]){
                                engine.move(new int[]{i,j,2,rank});
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
                            if(castling[1]){
                                engine.move(new int[]{i,j,6,rank});
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
                            break;
                        }case Constants.WHITE_KNIGHT:{
                            for(int[] dir:direction){
                                file = i;
                                rank = j;
                                file+=dir[0];
                                rank+=dir[1];
                                if(Util.isValid(file,rank)){
                                    if(engine.board[file][rank] == Constants.EMPTY_CHAR){
                                        engine.board[file][rank] = c;
                                        engine.board[i][j] = Constants.EMPTY_CHAR;
                                        if(!engine.isKingInCheck(engine.whiteToMove)){
                                            engine.board[i][j] = engine.board[file][rank];
                                            engine.board[file][rank] = Constants.EMPTY_CHAR;
                                            engine.move(new int[]{i,j,file,rank});
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
                                        }else {
                                            engine.board[i][j] = engine.board[file][rank];
                                            engine.board[file][rank] = Constants.EMPTY_CHAR;
                                        }
                                    }else{
                                        if(!Util.isAlly(c,engine.board[file][rank])) {
                                            char piece = engine.board[file][rank];
                                            engine.board[file][rank] = c;
                                            engine.board[i][j] = Constants.EMPTY_CHAR;
                                            if (!engine.isKingInCheck(engine.whiteToMove)) {
                                                engine.board[i][j] = engine.board[file][rank];
                                                engine.board[file][rank] = piece;
                                                engine.move(new int[]{i,j,file,rank});
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
                                            }else{
                                                engine.board[i][j] = engine.board[file][rank];
                                                engine.board[file][rank] = piece;
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        } case Constants.WHITE_PAWN:{
                            int limit = (Util.isUpperCase(c) && j == 6) || (!Util.isUpperCase(c) && j == 1) ? 2 : 1;
                            for(int[] dir:direction){
                                file = i;
                                rank = j;
                                file+=dir[0];
                                rank+=dir[1];
                                if(Util.isValid(file,rank)){
                                    if(Math.abs(dir[0]) == 1 && Math.abs(dir[1]) == 1){
                                        if(engine.board[file][rank] != Constants.EMPTY_CHAR && !Util.isAlly(engine.board[file][rank], c)){
                                            char piece = engine.board[file][rank];
                                            engine.board[file][rank] = c;
                                            engine.board[i][j] = Constants.EMPTY_CHAR;
                                            if(!engine.isKingInCheck(engine.whiteToMove)){
                                                engine.board[i][j] = c;
                                                engine.board[file][rank] = piece;
                                                engine.move(new int[]{i,j,file,rank});
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
                                            }else {
                                                engine.board[i][j] = c;
                                                engine.board[file][rank] = piece;
                                            }
                                        }
                                    }else{
                                        for(int k=0;k<limit && Util.isValid(file,rank);k++){
                                            if(engine.board[file][rank] == Constants.EMPTY_CHAR){
                                                engine.board[file][rank] = c;
                                                engine.board[i][j] = Constants.EMPTY_CHAR;
                                                if(!engine.isKingInCheck(engine.whiteToMove)){
                                                    engine.board[i][j] = engine.board[file][rank];
                                                    engine.board[file][rank] = Constants.EMPTY_CHAR;
                                                    engine.move(new int[]{i,j,file,rank});
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
                                                }else {
                                                    engine.board[i][j] = engine.board[file][rank];
                                                    engine.board[file][rank] = Constants.EMPTY_CHAR;
                                                }
                                            }else{
                                                break;
                                            }
                                            file+=dir[0];
                                            rank+=dir[1];
                                        }
                                    }
                                }
                            }
                            String enPassant = engine.fen.split(" ")[3];
                            if(!enPassant.equals("-")){
                                int[] enPassantSquare = Util.cvtPosition(enPassant);
                                if(Math.abs(i-enPassantSquare[0]) == 1 && Math.abs(j-enPassantSquare[1]) == 1){
                                    engine.board[enPassantSquare[0]][enPassantSquare[1]] = c;
                                    engine.board[i][j] = Constants.EMPTY_CHAR;
                                    char piece = engine.board[enPassantSquare[0]][j];
                                    engine.board[enPassantSquare[0]][j] = Constants.EMPTY_CHAR;
                                    if(!engine.isKingInCheck(engine.whiteToMove)){
                                        engine.board[i][j] = engine.board[enPassantSquare[0]][enPassantSquare[1]];
                                        engine.board[enPassantSquare[0]][enPassantSquare[1]] = Constants.EMPTY_CHAR;
                                        engine.board[enPassantSquare[0]][j] = piece;
                                        engine.move(new int[]{i,j,enPassantSquare[0],enPassantSquare[1]});
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
                                    }else {
                                        engine.board[i][j] = engine.board[enPassantSquare[0]][enPassantSquare[1]];
                                        engine.board[enPassantSquare[0]][enPassantSquare[1]] = Constants.EMPTY_CHAR;
                                        engine.board[enPassantSquare[0]][j] = piece;
                                    }
                                }
                            }
                            break;
                        }default:{
                            for(int[] dir:direction){
                                file = i;
                                rank = j;
                                file+=dir[0];
                                rank+=dir[1];
                                while(Util.isValid(file,rank)){
                                    if(engine.board[file][rank] == Constants.EMPTY_CHAR){
                                        engine.board[file][rank] = c;
                                        engine.board[i][j] = Constants.EMPTY_CHAR;
                                        if(!engine.isKingInCheck(engine.whiteToMove)) {
                                            engine.board[i][j] = engine.board[file][rank];
                                            engine.board[file][rank] = Constants.EMPTY_CHAR;
                                            engine.move(new int[]{i,j,file,rank});
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
                                        }else{
                                            engine.board[i][j] = engine.board[file][rank];
                                            engine.board[file][rank] = Constants.EMPTY_CHAR;
                                        }
                                    }else{
                                        if(!Util.isAlly(engine.board[file][rank], c)){
                                            char piece = engine.board[file][rank];
                                            engine.board[file][rank] = c;
                                            engine.board[i][j] = Constants.EMPTY_CHAR;
                                            if(!engine.isKingInCheck(engine.whiteToMove)){
                                                engine.board[i][j] = engine.board[file][rank];
                                                engine.board[file][rank] = piece;
                                                engine.move(new int[]{i,j,file,rank});
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
                                            }else {
                                                engine.board[i][j] = engine.board[file][rank];
                                                engine.board[file][rank] = piece;
                                            }
                                        }
                                        break;
                                    }
                                    file+=dir[0];
                                    rank+=dir[1];
                                }
                            }
                        }
                    }
                }
                i++;
            }
        }




        depthReached = depth;

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
                    if(depthReached>leastDepthReached){
                        leastDepthReached = depthReached;
                    }
                }
            }else{
                if(score>bestScore){
                    bestScore = score;
                    best = move;
                    finalscore = bestScore;
                    if(depthReached>leastDepthReached){
                        leastDepthReached = depthReached;
                    }
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
