package ui;

import board.Board;
import board.Square;
import engine.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
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

    public ArrayList<String> prevFen;

    InputStream in;
    AudioStream as;

    public GamePanel(String fen){
        super(new GridLayout(Constants.COLUMNS,Constants.ROWS));
        setSize(600,500);
        human = Parameters.HUMAN_CHOSE_WHITE;
        prevFen = new ArrayList<>();
        prevFen.add(fen);
        board = new Board(fen);
        engine = board.engine;
        ai = new AI(engine);
        initComponents();
        registerComponents();
        if(! Parameters.HUMAN_CHOSE_WHITE){
            cpu().start();
        }
    }

    public void checkGameStage(){
        if(engine.checkMate(false)){
            controlGame(false);
            JOptionPane.showMessageDialog(null,"White won!");
        }else if(engine.checkMate(true)){
            controlGame(false);
            JOptionPane.showMessageDialog(null,"Black won!");
        }else if(engine.isDraw()){
            controlGame(false);
            JOptionPane.showMessageDialog(null,"Draw!");
        }else{
            controlGame(true);
        }
    }

    public void controlGame(boolean enable){
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                board.boardSquares[j][i].setEnabled(enable);
            }
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
                    if(engine.board[bestMove[2]][bestMove[3]]!=Constants.EMPTY_CHAR){
                        playAudio(1);
                    }else{
                        playAudio(0);
                    }
                    System.out.println(engine.move(bestMove));
                    board.refactor();
                    human = !human;
                    checkGameStage();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                System.out.println("Time took to search depth "+Constants.SEARCH_DEPTH+" : "+ (System.nanoTime()/1000000-start/1000000)+" ms");
                System.out.println(Util.printBoard(engine.board,false));
                System.out.println(side+" : "+engine.evaluateBoard(!engine.whiteToMove));
            }
        };
    }

    public void playAudio(int type){
        try{
            in = type==1?new FileInputStream(Constants.CAPTURE_AUDIO_PATH): new FileInputStream(Constants.AUDIO_PATH);
            as = new AudioStream(in);
            AudioPlayer.player.start(as);
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

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(Parameters.FLIP){
            for(int i=Constants.COLUMNS-1;i>=0;i--) {
                for (int j = Constants.ROWS - 1; j >= 0; j--) {
                    if(actionEvent.getSource() == board.boardSquares[i][j]){
                        int[] position = new int[]{Constants.COLUMNS-1-i,Constants.ROWS-1-j};
                        if(selected == null && human ){
                            selected = board.boardSquares[i][j];
                            selected.setBackground(new Color(255,112,0));;
                            ArrayList<int[]> legalMoves = selected.isOccupied()?engine.generateMove(selected.getPieceChar(),position,Util.getOffset(selected.getPieceChar())):new ArrayList<>();
                            for(int[] squarePos:legalMoves){
                                board.boardSquares[Constants.COLUMNS-1-squarePos[0]][Constants.COLUMNS-1-squarePos[1]].setBackground(new Color(100,200,0));
                            }
                        }else{
                            if(selected != board.boardSquares[i][j]&& selected.isOccupied()&&human&&((Util.isUpperCase(selected.getPieceChar())&&Parameters.HUMAN_CHOSE_WHITE)||(!Util.isUpperCase(selected.getPieceChar())&&!Parameters.HUMAN_CHOSE_WHITE))){
                                int[] from = new int[]{Constants.COLUMNS-1-selected.position[0],Constants.ROWS-1-selected.position[1]};
                                ArrayList<int[]> legalMoves = engine.generateMove(selected.getPieceChar(),from,Util.getOffset(selected.getPieceChar()));
                                if(Util.toUpper(selected.getPieceChar()) == Constants.WHITE_PAWN && (engine.whiteToMove&&position[1] == 0||!engine.whiteToMove&&position[1] == 7)){
                                    String choice = JOptionPane.showInputDialog(null,"Enter \"1\" to promote to a knight, \"2\" to promote to a bishop,\"3\" to promote to a rook, by default the pawn will be promoted to a queen.","Pawn Promotion",JOptionPane.QUESTION_MESSAGE);
                                    if(choice.length()==1){
                                        try {
                                            position = new int[]{position[0], position[1], Integer.parseInt(choice)};
                                        }catch(Exception ex){
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                                for(int[] move:legalMoves){
                                    if(Util.samePosition(move,position)){
                                        prevFen.add(engine.fen);
                                        if(engine.board[move[0]][move[1]]!=Constants.EMPTY_CHAR){
                                            playAudio(1);
                                        }else{
                                            playAudio(0);
                                        }
                                        String mFen = position.length==2?engine.move(new int[]{Constants.COLUMNS-1-selected.position[0],Constants.ROWS-1-selected.position[1],move[0],move[1]}):engine.move(new int[]{Constants.COLUMNS-1-selected.position[0],Constants.ROWS-1-selected.position[1],move[0],move[1],position[2]});
                                        System.out.println(mFen);
                                        checkGameStage();
                                        String side = Parameters.HUMAN_CHOSE_WHITE?"white : ":"black : ";
                                        System.out.println(side+engine.evaluateBoard(Parameters.HUMAN_CHOSE_WHITE));
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
                        int[] position = new int[]{i,j};
                        if(selected == null &&  human ){
                            selected = board.boardSquares[i][j];
                            selected.setBackground(new Color(255,112,0));//yellow rgb
                            ArrayList<int[]> legalMoves = selected.isOccupied()?engine.generateMove(selected.getPieceChar(),selected.position,Util.getOffset(selected.getPieceChar())):new ArrayList<>();
                            for(int[] squarePos:legalMoves){
                                board.boardSquares[squarePos[0]][squarePos[1]].setBackground(new Color(100,200,0));
                            }
                        }else{
                            if(selected != board.boardSquares[i][j]&& selected.isOccupied()&& human &&((Util.isUpperCase(selected.getPieceChar())&&Parameters.HUMAN_CHOSE_WHITE)||(!Util.isUpperCase(selected.getPieceChar())&&!Parameters.HUMAN_CHOSE_WHITE))){
                                ArrayList<int[]> legalMoves = selected.isOccupied()?engine.generateMove(selected.getPieceChar(),selected.position,Util.getOffset(selected.getPieceChar())):new ArrayList<>();
                                if(Util.toUpper(selected.getPieceChar()) == Constants.WHITE_PAWN && (engine.whiteToMove&&position[1] == 0||!engine.whiteToMove&&position[1] == 7)){
                                    String choice = JOptionPane.showInputDialog(null,"Enter \"1\" to promote to a knight, \"2\" to promote to a bishop,\"3\" to promote to a rook, by default the pawn will be promoted to a queen.","Pawn Promotion",JOptionPane.QUESTION_MESSAGE);
                                    if(choice.length()==1){
                                        try {
                                            position = new int[]{position[0], position[1], Integer.parseInt(choice)};
                                        }catch(Exception ex){
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                                for(int[] move:legalMoves){
                                    if(Util.samePosition(move,position)){
                                        prevFen.add(engine.fen);
                                        if(engine.board[move[0]][move[1]]!=Constants.EMPTY_CHAR){
                                            playAudio(1);
                                        }else{
                                            playAudio(0);
                                        }
                                        String mFen = position.length==2?engine.move(new int[]{selected.position[0],selected.position[1],move[0],move[1]}):engine.move(new int[]{selected.position[0],selected.position[1],move[0],move[1],position[2]});
                                        System.out.println(mFen);
                                        String side = Parameters.HUMAN_CHOSE_WHITE?"white : ":"black : ";
                                        System.out.println(side+engine.evaluateBoard(Parameters.HUMAN_CHOSE_WHITE));
                                        System.out.println(Util.printBoard(engine.board,Parameters.FLIP));
                                        human  = !human;
                                        cpu().start();
                                        break;
                                    }
                                }
                            }
                            selected = null;
                            board.refactor();
                            checkGameStage();
                        }
                        break;
                    }
                }
            }
        }

        //System.out.println(engine.count());


        if(actionEvent.getSource() == newGame){
            Parameters.loadGame(this,Constants.STARTING_FEN);
            checkGameStage();
        }else if(actionEvent.getSource() == load){
            String fen = JOptionPane.showInputDialog(null,"Fen position :","Enter Fen String",JOptionPane.QUESTION_MESSAGE);
            if(fen!=null&&!fen.isEmpty()&&Util.FENValidator(fen)){
                Parameters.loadGame(this,fen);
                checkGameStage();
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
            if(!prevFen.isEmpty()){
                Parameters.loadGame(this,prevFen.get(prevFen.size()-1));
                prevFen.remove(prevFen.size()-1);
                checkGameStage();
            }
        }

    }
}
