package engine;

import util.Constants;
import util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AI {

    /*
    * to reduce difficulty level, lower the value of SEARCH_DEPTH in Constants.java file
    * */

    Engine engine;

//     FileWriter fw;
//     File dst = new File("src/learning/eval.txt");


    public AI(Engine engine)
    {
//        try{
//            fw = new FileWriter(dst);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        this.engine = engine;
    }

    public int[] BestMove(){
        ArrayList<int[]> moves = engine.copy().getLegalMoves();
        int size = moves.size();
        int movesperthread = size/(Constants.SEARCH_DEPTH);
        BestMove[] bestMoves = new BestMove[Constants.SEARCH_DEPTH];
        for(int i=0;i<bestMoves.length;i++){
            if(i==bestMoves.length-1){
                bestMoves[i] = new BestMove(engine.copy(),moves,moves.size());
            }else{
                bestMoves[i] = new BestMove(engine.copy(),moves,movesperthread);
            }
            bestMoves[i].start();
        }
        boolean complete = false;
        while(!complete){
            int count = 0;
            for(int i=0;i<bestMoves.length;i++){
                if(!bestMoves[i].isAlive()){
                    count++;
                }
            }
            complete = count == bestMoves.length;
            System.out.print("");
        }
//        String text = "";
        int[] best=null;
        int leastDepth = -1;
        float bestScore = engine.whiteToMove?Float.POSITIVE_INFINITY:Float.NEGATIVE_INFINITY;
        for(int i=0;i<bestMoves.length;i++){
           //text+=bestMoves[i].eval_text;
            if(engine.whiteToMove){
                if(bestMoves[i].finalscore<bestScore){
                    bestScore = bestMoves[i].finalscore;
                    best = bestMoves[i].bestMove;
                }else if(bestMoves[i].finalscore == bestScore){
                    if(bestMoves[i].leastDepthReached>leastDepth){
                        bestScore = bestMoves[i].finalscore;
                        best = bestMoves[i].bestMove;
                        leastDepth = bestMoves[i].leastDepthReached;
                    }
                }
            }else{
                if(bestMoves[i].finalscore>bestScore){
                    bestScore = bestMoves[i].finalscore;
                    best = bestMoves[i].bestMove;
                }else if(bestMoves[i].finalscore == bestScore){
                    if(bestMoves[i].leastDepthReached>leastDepth){
                        bestScore = bestMoves[i].finalscore;
                        best = bestMoves[i].bestMove;
                        leastDepth = bestMoves[i].leastDepthReached;
                    }
                }
            }
        }
        //append(text);
        if(best == null){
            ArrayList<int[]> legalmoves = engine.getLegalMoves();
            if(legalmoves.isEmpty()){
                return null;
            }else{
                int index = (int)(Math.random()*legalmoves.size());
                return legalmoves.get(index);
            }
        }else{
            return best;
        }
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
                            }
                        }else{
                            if(score>bestScore){
                                bestScore = score;
                                bestMove = Util.toString(position)+Util.toString(move);
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
        return bestMove == null? "":bestMove;
    }

    public int[] best(){
        ArrayList<int[]> moves = engine.copy().getLegalMoves();
        int size = moves.size();
        int movesperthread = size/(Constants.SEARCH_DEPTH+2);
        BestMove[] bestMoves = new BestMove[Constants.SEARCH_DEPTH+2];
        for(int i=0;i<bestMoves.length;i++){
            if(i==bestMoves.length-1){
                bestMoves[i] = new BestMove(engine.copy(),moves,moves.size());
            }else{
                bestMoves[i] = new BestMove(engine.copy(),moves,movesperthread);
            }
            bestMoves[i].start();
        }
        boolean complete = false;
        while(!complete){
            int count = 0;
            for(int i=0;i<bestMoves.length;i++){
                if(!bestMoves[i].isAlive()){
                    count++;
                }
            }
            complete = count == bestMoves.length;
            System.out.print("");
        }
        for(int k = 0;k<bestMoves.length;k++){
            for(int l=k+1;l<bestMoves.length;l++){
                if(engine.whiteToMove){
                    if(bestMoves[k].finalscore>bestMoves[l].finalscore){
                        BestMove temp = bestMoves[k];
                        bestMoves[k] = bestMoves[l];
                        bestMoves[l] = temp;
                    }else if(bestMoves[k].finalscore == bestMoves[l].finalscore){
                        if(bestMoves[k].leastDepthReached<bestMoves[l].leastDepthReached){
                            BestMove temp = bestMoves[k];
                            bestMoves[k] = bestMoves[l];
                            bestMoves[l] = temp;
                        }
                    }
                }else{
                    if(bestMoves[k].finalscore<bestMoves[l].finalscore){
                        BestMove temp = bestMoves[k];
                        bestMoves[k] = bestMoves[l];
                        bestMoves[l] = temp;
                    }else if(bestMoves[k].finalscore == bestMoves[l].finalscore){
                        if(bestMoves[k].leastDepthReached<bestMoves[l].leastDepthReached){
                            BestMove temp = bestMoves[k];
                            bestMoves[k] = bestMoves[l];
                            bestMoves[l] = temp;
                        }
                    }
                }
            }
        }
        ArrayList<int[]> bestMovesList = new ArrayList<>();
        for(int i=0;i<bestMoves.length;i++){
            bestMovesList.add(bestMoves[i].bestMove);
        }
        System.out.println("1 : "+Util.parseMove(bestMovesList.get(0)));
        BestMove[] bestMoves2 = new BestMove[Constants.SEARCH_DEPTH];
        Constants.SEARCH_DEPTH++;
        for(int i=0;i<bestMoves2.length;i++){
            bestMoves2[i] = new BestMove(engine.copy(),bestMovesList,1);
            bestMoves2[i].start();
        }
        complete = false;
        while(!complete){
            int count = 0;
            for(int i=0;i<bestMoves2.length;i++){
                if(!bestMoves2[i].isAlive()){
                    count++;
                }
            }
            complete = count == bestMoves2.length;
            System.out.print("");
        }
        Constants.SEARCH_DEPTH--;
        int[] best=null;
        int leastDepth = -1;
        float bestScore = engine.whiteToMove?Float.POSITIVE_INFINITY:Float.NEGATIVE_INFINITY;
        for(int i=0;i<bestMoves2.length;i++){
            if(engine.whiteToMove){
                if(bestMoves2[i].finalscore<bestScore){
                    bestScore = bestMoves2[i].finalscore;
                    best = bestMoves2[i].bestMove;
                }else if(bestMoves2[i].finalscore == bestScore){
                    if(bestMoves2[i].leastDepthReached>leastDepth){
                        bestScore = bestMoves2[i].finalscore;
                        best = bestMoves2[i].bestMove;
                        leastDepth = bestMoves2[i].leastDepthReached;
                        System.out.println(leastDepth);
                    }
                }
            }else{
                if(bestMoves2[i].finalscore>bestScore){
                    bestScore = bestMoves2[i].finalscore;
                    best = bestMoves2[i].bestMove;
                }else if(bestMoves2[i].finalscore == bestScore){
                    if(bestMoves2[i].leastDepthReached>leastDepth){
                        bestScore = bestMoves2[i].finalscore;
                        best = bestMoves2[i].bestMove;
                        leastDepth = bestMoves2[i].leastDepthReached;
                        System.out.println(leastDepth);
                    }
                }
            }
        }
        if(best == null){
            ArrayList<int[]> legalmoves = engine.getLegalMoves();
            if(legalmoves.isEmpty()){
                return null;
            }else{
                int index = (int)(Math.random()*legalmoves.size());
                return legalmoves.get(index);
            }
        }else{
            System.out.println("2 : "+Util.parseMove(best));
            return best;
        }
    }


    public float minimax(float alpha,float beta,int depth,boolean maximizing){
        if(engine.checkMate(true)){
            return Float.POSITIVE_INFINITY;
        }else if(engine.checkMate(false)){
            return Float.NEGATIVE_INFINITY;
        }else if(engine.isDraw()){
            return 0.0f;
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

    public String getOutput(){
        String moves="";

        ArrayList<int[]> legalmoves = engine.getLegalMoves();
        for(int[] move:legalmoves){
            moves+=Util.parseMove(move)+" ";
        }


        return moves;
    }
//
//    private void append(String text){
//        try {
//            fw.write(readFileContent(dst) + text);
//            fw.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    public static String readFileContent(File file) throws Exception{
//        FileInputStream fileInputStream;
//        String content;
//        fileInputStream = new FileInputStream(file);
//        byte[] value = new byte[(int) file.length()];
//        fileInputStream.read(value);
//        fileInputStream.close();
//        content = new String(value, StandardCharsets.UTF_8);
//        return content;
//    }


}
