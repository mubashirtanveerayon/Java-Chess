/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import board.Board;
import javax.swing.JFrame;
import util.Constants;
import util.Util;

/**
 *
 * @author ayon2
 */
public class Main extends JFrame{
    
    public Main(){
        GamePanel gamePanel = new GamePanel(Constants.STARTING_FEN);
        setDefaultCloseOperation(3);
        add(gamePanel);
        pack();
    }
    
    public static void main(String[] args) {
        Thread thread = new Thread(){
            public void run(){
                new Main().setVisible(true);
            }
        };
        thread.start();
    }
    
}
