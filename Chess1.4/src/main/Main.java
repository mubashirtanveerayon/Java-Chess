package main;

import ui.GamePanel;
import util.*;

import javax.swing.*;
import java.io.File;

public class Main extends JFrame{
	public static void main(String[] args){
		int response = JOptionPane.showConfirmDialog(null,"Welcome "+System.getProperty("user.name")+" to a game of chess! Would you like to play as white? (selecting 'No' means you'll play as black)","Welcome",JOptionPane.YES_NO_OPTION);
		Parameters.HUMAN_CHOSE_WHITE = response == JOptionPane.YES_OPTION;
		new Main().show();
	}

	public JMenuBar menuBar;

	GamePanel gamePanel;

	public Main(){
		super("Chess 1.4");
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
