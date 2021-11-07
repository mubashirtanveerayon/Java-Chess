package main;

import engine.Engine;
import ui.GamePanel;
import util.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class Main extends JFrame{
	public static void main(String[] args){
		new Main().show();
	}

	public JMenuBar menuBar;

	GamePanel gamePanel;

	public Main(){
		super("Chess1.3");
		setDefaultCloseOperation(3);
		initComponents();
		pack();
	}

	public void initComponents(){
		gamePanel = new GamePanel(Constants.STARTING_FEN);
		menuBar = new JMenuBar();
        menuBar.add(gamePanel.file);
        setJMenuBar(menuBar);
		add(gamePanel);
	}


	
}
