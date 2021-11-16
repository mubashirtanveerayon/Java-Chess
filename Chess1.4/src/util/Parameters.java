package util;

import engine.AI;
import engine.Engine;
import ui.GamePanel;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Parameters{

	public static boolean HUMAN_CHOSE_WHITE = false;

	public static boolean STOP_AI = false;

	public static boolean FLIP=false;

	public static void loadGame(GamePanel gp,String fen){
		gp.board.engine = new Engine(fen);
		gp.engine = gp.board.engine;
		gp.ai = new AI(gp.engine);
		gp.board.refactor();
		int response = JOptionPane.showConfirmDialog(gp, "Would you like to play as white? (selecting 'No' means you'll play as black)", "Choose side", JOptionPane.YES_NO_OPTION);
		HUMAN_CHOSE_WHITE = response == JOptionPane.YES_OPTION;
		gp.human = (HUMAN_CHOSE_WHITE&&gp.engine.whiteToMove)||(!gp.engine.whiteToMove&&!Parameters.HUMAN_CHOSE_WHITE);
		if(!gp.human){
			gp.cpu().start();
		}
	}

	public static void saveGame(GamePanel gp) throws IOException {
		FileWriter fw = new FileWriter(new File("save.txt"));
		fw.write(gp.engine.fen);
		fw.close();
	}

}
