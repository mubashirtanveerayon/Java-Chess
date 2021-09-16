/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import board.Move;
import game.GamePanel;
import java.util.ArrayList;

/**
 *
 * @author ayon2
 */
public class GameParameter {
    
    public static boolean whiteToMove;
    public static ArrayList<String> history = new ArrayList<>();
    public static ArrayList<String> moves = new ArrayList<>();
    public static int halfMove = 0;
    public static int fullMove = 1;
    
    public static void loadGame(GamePanel gamePanel,String fen){
        history.clear();
        moves.clear();
        gamePanel.removeComponent();
        gamePanel.board = Util.loadBoardFromFen(fen);
        history.add(fen);
        boolean toMove = fen.split(" ")[1].equals(String.valueOf(Constants.WHITE));
        gamePanel.move = new Move(gamePanel.board,toMove,fen);
        gamePanel.registerComponent();
        gamePanel.renderBoard();
        if(!whiteToMove){
            gamePanel.cpu().start();
        }
    }
    
}
