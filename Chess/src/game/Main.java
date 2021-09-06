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
        GamePanel gamePanel = new GamePanel("r1bqk2r/pppppp1p/2n2npb/8/8/BPNP4/P1PQPPPP/R3KBNR b KQkq - 0 1");//r1bqk2r/pppppp1p/2n2npb/8/8/BPNP4/P1PQPPPP/R3KBNR b Constants.STARTING_FEN
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
