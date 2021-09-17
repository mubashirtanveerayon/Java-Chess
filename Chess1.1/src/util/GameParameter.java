/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import board.Board;
import board.Move;
import gui.GamePanel;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
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
        history.add(fen);
        if(gamePanel != null){
            gamePanel.removeComponent();
            gamePanel.board = Util.loadBoardFromFen(fen);
            boolean toMove = fen.split(" ")[1].equals(String.valueOf(Constants.WHITE));
            gamePanel.move = new Move(gamePanel.board,toMove,fen);
            gamePanel.registerComponent();
            gamePanel.renderBoard();
        }
    }

    public static void saveGame(Board board){
        String path = JOptionPane.showInputDialog(null, "File path :", "FEN.txt");
        if (path != null || !path.isEmpty()) {
            String fen = Util.loadFenFromBoard(board);
            try {
                File file = new File(path);
                FileWriter fw = new FileWriter(file);
                fw.write(fen);
                fw.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }
    
}
