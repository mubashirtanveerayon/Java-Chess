/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import util.Constants;
import util.GameParameter;
import util.Util;

/**
 *
 * @author ayon2
 */
public class Main extends JFrame implements ActionListener {

    GamePanel gamePanel;

    public JMenuBar menuBar;

    public JMenu option;

    public JMenuItem save, load, exit;

    public JPanel gameParent;

    public Main() {
        gamePanel = new GamePanel(Constants.STARTING_FEN);
        gameParent = new JPanel(new BorderLayout());

        gameParent.add(gamePanel,BorderLayout.CENTER);

        menuBar = new JMenuBar();
        menuBar.setBackground(new Color(100,100,100));

        option = new JMenu("Option");
        option.setForeground(Color.white);

        save = new JMenuItem("Save");
        load = new JMenuItem("Load");
        exit = new JMenuItem("Exit");

        save.addActionListener(this);
        load.addActionListener(this);
        exit.addActionListener(this);

        option.add(save);
        option.add(load);
        option.add(exit);

        menuBar.add(option);

        setDefaultCloseOperation(3);
        setJMenuBar(menuBar);
        
        add(gameParent);
        pack();
    }

    public static void main(String[] args) {
        Thread thread = new Thread() {
            public void run() {
                new Main().setVisible(true);
            }
        };
        thread.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == exit) {
            dispose();
            System.exit(0);
        } else if (src == save) {
            GameParameter.saveGame(gamePanel.board);
        }else if(src == load){
            String fen = JOptionPane.showInputDialog(null,"FEN :");
            GameParameter.loadGame(gamePanel, fen);
        }
    }
    
}
