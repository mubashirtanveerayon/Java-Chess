package main;

import engine.AI;
import engine.Engine;
import util.Constants;
import util.Util;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Engine engine = new Engine("N1bk1b1r/pp1p1ppp/2n1pn2/3q4/8/4PNPP/PPPP1P2/R1BQKB1R w KQ - 1 9");
        AI ai = new AI(engine);
        System.out.println("q to exit");
        while(!sc.next().equals("q")){
            System.out.println(Util.printBoard(engine.board));
            System.out.println("Search depth : "+Constants.SEARCH_DEPTH);
            if(engine.whiteToMove) {
                System.out.println("Make move :");
            }
            String movestr = sc.next();
            try{
                int[][] move = Util.parseMove(movestr);
                System.out.println(engine.move(move[0],move[1]));
            }catch(Exception ex){
                try{
                    Constants.SEARCH_DEPTH = Integer.parseInt(movestr);
                }catch(NumberFormatException nx){

                }
            }
           if(!engine.whiteToMove){
               try{
                   int[][] aimove = Util.parseMove(ai.getBestMove());
                   System.out.println(engine.move(aimove[0],aimove[1]));
               }catch(Exception ex){

               }
           }
            System.out.println("q to exit");
        }
    }

}
