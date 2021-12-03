package main;

import engine.AI;
import engine.Engine;
import util.Constants;
import util.Util;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Engine engine = new Engine("r2q1b1r/1pk2ppp/p1b2n2/2p2Q2/4PP2/8/PP1P2PP/R1B1KB1R b KQ e3 0 18");
        AI ai = new AI(engine);
        FileWriter fw = null;
        System.out.println("q to exit");
        boolean run = true;
        boolean flipped=false;
        while(run){
            String input = sc.nextLine();
            String[] contents = input.split(";");
            if(contents.length == 3&&contents[0].equalsIgnoreCase("position")&&contents[1].equalsIgnoreCase("fen")){
                if(Util.FENValidator(contents[2])){
                    engine = new Engine(contents[2]);
                    ai = new AI(engine);
                }else{
                    System.out.println("Invalid fen");
                }
            }else if(contents.length==1) {
                if(Character.isDigit(contents[0].charAt(0))){
                    Constants.SEARCH_DEPTH = Integer.parseInt(String.valueOf(contents[0].charAt(0)));
                    if(Constants.SEARCH_DEPTH<=0){
                        Constants.SEARCH_DEPTH = 1;
                    }
                }else if(contents[0].length()==1){
                    if(contents[0].toLowerCase().charAt(0) == 'd'){
                        System.out.println(Util.printBoard(engine.board,flipped));
                    }else if(contents[0].toLowerCase().charAt(0) == 'q') {
                        run = false;
                    }else if(contents[0].toLowerCase().charAt(0) == 's') {
                        System.out.println("Search depth : "+Constants.SEARCH_DEPTH);
                    }else if(contents[0].toLowerCase().charAt(0) == 'c') {
                        System.out.println(engine.count());
                    }else if(contents[0].toLowerCase().charAt(0) == 'v') {
                        System.out.println(Constants.VERSION);
                    }
                }else if(contents[0].equalsIgnoreCase("flip")) {
                    flipped = !flipped;
                }else if(contents[0].equalsIgnoreCase("fen")) {
                    System.out.println(engine.fen);
                }else if(contents[0].equalsIgnoreCase("go")){
                    long start = System.nanoTime();
                    int[] bestMove = ai.BestMove();
                    System.out.println("Best move : "+Util.parseMove(bestMove));
                    System.out.println("Time taken : "+(System.nanoTime()-start)/1000000+"ms");
                }else if(contents[0].equalsIgnoreCase("play")){
                    long start = System.nanoTime();
                    int[] bestMove = ai.BestMove();
                    try {
                        System.out.println("Best move : "+Util.parseMove(bestMove));
                        System.out.println("Time taken : "+(System.nanoTime()-start)/1000000+"ms");
                        System.out.println(engine.move(bestMove));
                        String side = !engine.whiteToMove?"white : ":"black : ";
                        System.out.println(side+engine.evaluateBoard(!engine.whiteToMove));
                    } catch (Exception ex) {
                        System.out.println("Couldn't find any best move!");
                    }
                }else{
                    try {
                        int[][] move = Util.parseMove(contents[0].toLowerCase());
                        boolean legal = (engine.whiteToMove&&Util.isUpperCase(engine.board[move[0][0]][move[0][1]]))||(!engine.whiteToMove&&!Util.isUpperCase(engine.board[move[0][0]][move[0][1]]));
                        if (legal) {
                            ArrayList<int[]> legalMoves = engine.generateMove(engine.board[move[0][0]][move[0][1]],move[0],Util.getOffset(engine.board[move[0][0]][move[0][1]]));
                            for(int[] legalmove:legalMoves){
                                if(Util.samePosition(legalmove,move[1])){
                                    System.out.println(engine.move(new int[]{move[0][0],move[0][1],move[1][0],move[1][1]}));
                                    String side = !engine.whiteToMove?"white : ":"black : ";
                                    System.out.println(side+engine.evaluateBoard(!engine.whiteToMove));
                                }
                            }
                        }
                    } catch (Exception ex) {

                    }
                }
            }else if(contents.length==2){
                if(contents[0].equalsIgnoreCase("moves")){
                    boolean white = contents[1].toLowerCase().charAt(0) == Constants.WHITE;
                    String moves = ai.getOutput(white);
                    System.out.println(moves);
                    System.out.println(moves.split(" ").length);
                }else if(contents[0].equalsIgnoreCase("evaluate")){
                    boolean white = contents[1].toLowerCase().charAt(0) == Constants.WHITE;
                    System.out.println(engine.evaluateBoard(white));
                }else if(contents[0].equalsIgnoreCase("go")){
                    try{
                        System.out.println(engine.moveGeneration(Integer.parseInt(contents[1])));
                    }catch(NumberFormatException nx){

                    }
                }else if(contents[0].equalsIgnoreCase("export")){
                    try{
                        fw = new FileWriter(new File(contents[1]));
                        fw.write(engine.fen);
                        fw.close();
                    }catch(Exception ex){
                        System.out.println(ex);
                    }
                }
            }
            System.out.println("q to exit");
        }
        sc.close();
    }

}
