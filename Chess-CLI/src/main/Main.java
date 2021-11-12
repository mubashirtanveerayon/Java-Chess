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
        Engine engine = new Engine(Constants.STARTING_FEN);
        AI ai = new AI(engine);
        FileWriter fw = null;
        System.out.println("q to exit");
        boolean run = true;
        while(run){
            String input = sc.nextLine();
            String[] contents = input.split(";");
            if(contents.length == 3&&contents[0].equalsIgnoreCase("position")&&contents[1].equalsIgnoreCase("fen")){
                engine = new Engine(contents[2]);
                ai = new AI(engine);
            }else if(contents.length==1) {
                if(Character.isDigit(contents[0].charAt(0))){
                    Constants.SEARCH_DEPTH = Integer.parseInt(String.valueOf(contents[0].charAt(0)));
                }else if(contents[0].length()==1){
                    if(contents[0].toLowerCase().charAt(0) == 'd'){
                        System.out.println(Util.printBoard(engine.board));
                    }else if(contents[0].toLowerCase().charAt(0) == 'q') {
                        run = false;
                    }else if(contents[0].toLowerCase().charAt(0) == 's') {
                        System.out.println("Search depth : "+Constants.SEARCH_DEPTH);
                    }else if(contents[0].toLowerCase().charAt(0) == 'c') {
                        System.out.println(engine.count());
                    }else if(contents[0].toLowerCase().charAt(0) == 'v') {
                        System.out.println(Constants.VERSION);
                    }
                }else if(contents[0].equalsIgnoreCase("fen")) {
                    System.out.println(engine.fen);
                }else if(contents[0].equalsIgnoreCase("go")){
                    String movestr = ai.getBestMove();
                    System.out.println("Best move : "+movestr);
                }else if(contents[0].equalsIgnoreCase("play")){
                    String movestr = ai.getBestMove();
                    try {
                        int[][] move = Util.parseMove(movestr);
                        System.out.println(engine.move(move[0],move[1]));
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
                                    System.out.println(engine.move(move[0],move[1]));
                                }
                            }
                        }
                    } catch (Exception ex) {

                    }
                }
            }else if(contents.length==2){
                if(contents[0].equalsIgnoreCase("moves")){
                    boolean white = contents[1].toLowerCase().charAt(0) == Constants.WHITE;
                    System.out.println(ai.getOutput(white));
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
