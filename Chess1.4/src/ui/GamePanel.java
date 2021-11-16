package ui;

import board.Board;
import board.Square;
import engine.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class GamePanel extends JPanel implements ActionListener {

    public Board board;
    public Engine engine;
    public static Square selected;

    public boolean human;

    public AI ai;

    public JMenuItem newGame,load,saveGame,undo,setDepth,flipBoard;
    public JMenu file;

    AudioInputStream audioStream;

    Clip clip;

    public String prevFen;

    public GamePanel(String fen){
        super(new GridLayout(Constants.COLUMNS,Constants.ROWS));
        setSize(600,500);
        human = Parameters.HUMAN_CHOSE_WHITE;
        prevFen = fen;
        board = new Board(fen);
        engine = board.engine;
        ai = new AI(engine);
        initComponents();
        registerComponents();
        if(! Parameters.HUMAN_CHOSE_WHITE){
            cpu().start();
        }
    }

    public Thread play(){
        return new Thread(){
            public void run(){
                while(true){
                    Thread cpu = cpu();
                    cpu.start();
                    try{
                        cpu.join();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    boolean over = engine.checkMate(false)||engine.checkMate(true);
                    if(over){
                        break;
                    }
                    System.out.print("");
                }
            }
        };
    }


    public void initComponents(){
        try{
            audioStream = AudioSystem.getAudioInputStream(ResourceLoader.load(Constants.AUDIO_PATH));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        }catch(Exception ex){
            System.out.println(ex);
        }
        newGame = new JMenuItem("New Game");
        load = new JMenuItem("Load Game");
        saveGame = new JMenuItem("Save Game");
        undo = new JMenuItem("Undo");
        setDepth = new JMenuItem("Search Depth");
        flipBoard = new JMenuItem("Flip");
        file = new JMenu("File");
        file.add(newGame);
        file.add(load);
        file.add(saveGame);
        file.add(undo);
        file.add(setDepth);
        file.add(flipBoard);
    }

    public Thread cpu(){
        return new Thread() {
            @Override
            public void run() {
                String side = engine.whiteToMove?"White":"Black";
                System.out.println(side+" is thinking...");
                long start = System.nanoTime();
                try{
                    int[] bestMove = ai.BestMove();
                    System.out.println("Best Move "+Util.parseMove(bestMove));
                    System.out.println(engine.move(bestMove));
                    human = !human;
                    playAudio();
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null,"There are no moves left to be played or the A.I. player can't look ahead enough for any legal move!");
                    ex.printStackTrace();
                }
                System.out.println("Time took to search depth "+Constants.SEARCH_DEPTH+" : "+ (System.nanoTime()/1000000-start/1000000)+" ms");
                System.out.println(Util.printBoard(engine.board,false));
                board.refactor();
            }
        };
    }

    public void playAudio(){
        try{
            clip.stop();
            clip.setMicrosecondPosition(0);
            clip.start();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void registerComponents(){
        newGame.addActionListener(this);
        load.addActionListener(this);
        saveGame.addActionListener(this);
        undo.addActionListener(this);
        setDepth.addActionListener(this);
        flipBoard.addActionListener(this);
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                board.boardSquares[j][i].addActionListener(this);
                add(board.boardSquares[j][i]);
            }
        }
    }

    public void removeComponents(){
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                remove(board.boardSquares[j][i]);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(Parameters.FLIP){
            for(int i=Constants.COLUMNS-1;i>=0;i--) {
                for (int j = Constants.ROWS - 1; j >= 0; j--) {
                    if(actionEvent.getSource() == board.boardSquares[i][j]){
                        int[] position = new int[]{Constants.COLUMNS-1-i,Constants.ROWS-1-j};
                        if(selected == null && human ){
                            selected = board.boardSquares[i][j];
                            selected.setBackground(Color.green);
                            ArrayList<int[]> legalMoves = selected.isOccupied()?engine.generateMove(selected.getPieceChar(),position,Util.getOffset(selected.getPieceChar())):new ArrayList<>();
                            for(int[] squarePos:legalMoves){
                                board.boardSquares[Constants.COLUMNS-1-squarePos[0]][Constants.COLUMNS-1-squarePos[1]].setBackground(Color.red);
                            }
                        }else{
                            if(selected != board.boardSquares[i][j]&& selected.isOccupied()&&human&&((Util.isUpperCase(selected.getPieceChar())&&Parameters.HUMAN_CHOSE_WHITE)||(!Util.isUpperCase(selected.getPieceChar())&&!Parameters.HUMAN_CHOSE_WHITE))){
                                int[] from = new int[]{Constants.COLUMNS-1-selected.position[0],Constants.ROWS-1-selected.position[1]};
                                ArrayList<int[]> legalMoves = engine.generateMove(selected.getPieceChar(),from,Util.getOffset(selected.getPieceChar()));
                                for(int[] move:legalMoves){
                                    if(Util.samePosition(move,position)){
                                        prevFen = engine.fen;
                                        System.out.println(engine.move(new int[]{Constants.COLUMNS-1-selected.position[0],Constants.ROWS-1-selected.position[1],move[0],move[1]}));
                                        playAudio();
                                        System.out.println("white : "+engine.evaluateBoard(true));
                                        System.out.println(Util.printBoard(engine.board,Parameters.FLIP));
                                        human  = !human;
                                        cpu().start();
                                        break;
                                    }
                                }
                            }
                            selected = null;
                            board.refactor();
                        }
                        break;
                    }
                }
            }
        }else{
            for(int i=0;i<Constants.COLUMNS;i++){
                for(int j=0;j<Constants.ROWS;j++){
                    if(actionEvent.getSource() == board.boardSquares[i][j]){
                        if(selected == null &&  human ){
                            selected = board.boardSquares[i][j];
                            selected.setBackground(Color.green);
                            ArrayList<int[]> legalMoves = selected.isOccupied()?engine.generateMove(selected.getPieceChar(),selected.position,Util.getOffset(selected.getPieceChar())):new ArrayList<>();
                            for(int[] squarePos:legalMoves){
                                board.boardSquares[squarePos[0]][squarePos[1]].setBackground(Color.red);
                            }
                        }else{
                            if(selected != board.boardSquares[i][j]&& selected.isOccupied()&& human &&((Util.isUpperCase(selected.getPieceChar())&&Parameters.HUMAN_CHOSE_WHITE)||(!Util.isUpperCase(selected.getPieceChar())&&!Parameters.HUMAN_CHOSE_WHITE))){
                                ArrayList<int[]> legalMoves = selected.isOccupied()?engine.generateMove(selected.getPieceChar(),selected.position,Util.getOffset(selected.getPieceChar())):new ArrayList<>();
                                for(int[] move:legalMoves){
                                    if(Util.samePosition(move,board.boardSquares[i][j].position)){
                                        prevFen = engine.fen;
                                        System.out.println(engine.move(new int[]{selected.position[0],selected.position[1],move[0],move[1]}));
                                        playAudio();
                                        System.out.println("white : "+engine.evaluateBoard(true));
                                        System.out.println(Util.printBoard(engine.board,Parameters.FLIP));
                                        human  = !human;
                                        cpu().start();
                                        break;
                                    }
                                }
                            }
                            selected = null;
                            board.refactor();
                        }
                        break;
                    }
                }
            }
        }
        //System.out.println(Util.printBoard(engine.board));
        //System.out.println(engine.count());


        if(actionEvent.getSource() == newGame){
            Parameters.loadGame(this,Constants.STARTING_FEN);
        }else if(actionEvent.getSource() == load){
            String fen = JOptionPane.showInputDialog(null,"Fen position :","Enter Fen String",JOptionPane.QUESTION_MESSAGE);
            if(fen!=null&&!fen.isEmpty()&&Util.FENValidator(fen)){
                Parameters.loadGame(this,fen);
            }else{
                JOptionPane.showMessageDialog(null,"Invalid FEN String","Error",JOptionPane.ERROR_MESSAGE);
            }
        }else if(actionEvent.getSource() == setDepth){
            try{
                Constants.SEARCH_DEPTH = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter search depth : (not recommended to enter higher than 4)","Set Search Depth",JOptionPane.QUESTION_MESSAGE));
            }catch(NumberFormatException nfe){
                JOptionPane.showMessageDialog(null,"Digits only!","Error",JOptionPane.ERROR_MESSAGE);
            }
        }else if(actionEvent.getSource() == saveGame){
            try {
                Parameters.saveGame(this);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,"Something went wrong!","Error",JOptionPane.ERROR_MESSAGE);
            }
        }else if(actionEvent.getSource() == flipBoard){
            Parameters.FLIP = !Parameters.FLIP;
            board.refactor();
        }else if(actionEvent.getSource() == undo){
            Parameters.loadGame(this,prevFen);
        }

    }
}
