/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import util.Constants;
import util.GameParameter;
import util.Util;

/**
 *
 * @author ayon2
 */
public class Main extends JFrame implements ActionListener {

    public JMenuBar menuBar;

    public JMenu option;

    public JMenuItem newGame, save, load, exit;

    GamePanel gamePanel;

    public static String ENGINE_PATH = "src"+ File.separator + "engine" + File.separator;

    public Main() {
        gamePanel = new GamePanel(Constants.STARTING_FEN);

        menuBar = new JMenuBar();

        option = new JMenu("Option");

        newGame = new JMenuItem("New Game");
        save = new JMenuItem("Save");
        load = new JMenuItem("Load");
        exit = new JMenuItem("Exit");

        newGame.addActionListener(this);
        save.addActionListener(this);
        load.addActionListener(this);
        exit.addActionListener(this);

        option.add(newGame);
        option.add(save);
        option.add(load);
        option.add(exit);

        menuBar.add(option);

        setDefaultCloseOperation(3);
        this.setJMenuBar(menuBar);
        add(gamePanel);
        pack();
    }

    public static void main(String[] args) {
        String osName = System.getProperty("os.name").toLowerCase();
        if(osName.contains("windows")){
            ENGINE_PATH += Constants.STOCKFISH_WINDOWS;
        }else if(osName.contains("linux")){
            ENGINE_PATH += Constants.STOCKFISH_LINUX;
        }
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
            String path = JOptionPane.showInputDialog(null, "File path :", "FEN.txt");
            if (path != null || !path.isEmpty()) {
                String fen = Util.loadFenFromBoard(gamePanel.board);
                try {
                    File file = new File(path);
                    FileWriter fw = new FileWriter(file);
                    fw.write(fen);
                    fw.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        }else if(src == load){
            String fen = JOptionPane.showInputDialog(null,"FEN :");
            GameParameter.loadGame(gamePanel, fen);
        }else if(src == newGame){
            GameParameter.loadGame(gamePanel,Constants.STARTING_FEN);
        }
    }

}
