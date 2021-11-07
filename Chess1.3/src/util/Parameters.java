package util;

import board.Board;
import engine.AI;
import engine.Engine;
import ui.GamePanel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Parameters{

	public static boolean STOP_AI = false;

	public static void loadGame(GamePanel gp,String fen){
		gp.board.engine = new Engine(fen);
		gp.engine = gp.board.engine;
		gp.ai = new AI(gp.engine);
		gp.board.refactor();
		if(!gp.engine.whiteToMove){
			gp.cpu().start();
		}
	}

	public static void saveGame(GamePanel gp) throws IOException {
		FileWriter fw = new FileWriter(new File("save.txt"));
		fw.write(gp.engine.fen);
		fw.close();
	}

}
