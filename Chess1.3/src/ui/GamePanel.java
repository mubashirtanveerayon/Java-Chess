package ui;

import board.Board;
import board.Square;
import engine.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import main.Main;
import util.*;

import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class GamePanel extends JPanel implements ActionListener {

    public Board board;
    public Engine engine;
    public static Square selected;

    public AI ai;

    public JMenuItem newGame,load,saveGame,setDepth;
    public JMenu file;

    public boolean human;
    
    public GamePanel(String fen){
        super(new GridLayout(Constants.COLUMNS,Constants.ROWS));
        setSize(600,500);
        board = new Board(fen);
        engine = board.engine;
        human = engine.whiteToMove;
        ai = new AI(engine);
        initComponents();
        registerComponents();
        if(!engine.whiteToMove){
            cpu().start();
        }
//        play().start();
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
        setDepth = new JMenuItem("Search Depth");
        file = new JMenu("File");
        file.add(newGame);
        file.add(load);
        file.add(saveGame);
        file.add(setDepth);
    }

    public Thread cpu(){
        return new Thread() {
            @Override
            public void run() {
                String side = engine.whiteToMove?"White":"Black";
                System.out.println(side+" is thinking...");
                String bestMove = ai.getBestMove();
                try{
                    int[][] cpu = Util.parseMove(bestMove);
                    System.out.println("Found best move "+bestMove);
                    System.out.println(engine.move(cpu[0],cpu[1]));
                    System.out.println(Util.printBoard(engine.board));
                    board.refactor();
                }catch(Exception e){
                    System.out.println("Couldnt find best move! As the engine is not yet optimized at all and has not been checked properly, there might be a lot of bugs lying around, I will fix them as I play around with the AI.");
                }
                human = true;
            }
        };
    }

    public void registerComponents(){
        newGame.addActionListener(this);
        load.addActionListener(this);
        saveGame.addActionListener(this);
        setDepth.addActionListener(this);
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
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                if(actionEvent.getSource() == board.boardSquares[i][j]){
                    //System.out.println((selected == null && human) +" and "+(selected != board.boardSquares[i][j]&&human));
                    if(selected == null && human){
                        selected = board.boardSquares[i][j];
                        selected.setBackground(Color.green);
                        ArrayList<int[]> legalMoves = selected.isOccupied()?engine.generateMove(selected.getPieceChar(),selected.position,Util.getOffset(selected.getPieceChar())):new ArrayList<>();
                        for(int[] squarePos:legalMoves){
                            board.boardSquares[squarePos[0]][squarePos[1]].setBackground(Color.red);
                        }
                    }else{
                        if(selected != board.boardSquares[i][j]&&human){
                            ArrayList<int[]> legalMoves = selected.isOccupied()?engine.generateMove(selected.getPieceChar(),selected.position,Util.getOffset(selected.getPieceChar())):new ArrayList<>();
                            for(int[] move:legalMoves){
                                if(Util.samePosition(move,board.boardSquares[i][j].position)){
                                    System.out.println(engine.move(selected.position,move));
                                    System.out.println("white : "+engine.evaluateBoard(true));
                                    System.out.println(Util.printBoard(engine.board));
                                    selected = null;
                                    board.refactor();
                                    human = false;
                                    cpu().start();
                                    break;
                                }
                            }

                        }
                        if(selected!=null){
                            selected = null;
                        }
                    }
                    break;
                }
            }
        }
        //System.out.println(Util.printBoard(engine.board));
        //System.out.println(engine.count());


        if(actionEvent.getSource() == newGame){
            Parameters.loadGame(this,Constants.STARTING_FEN);
        }else if(actionEvent.getSource() == load){
            Parameters.loadGame(this,JOptionPane.showInputDialog(null,"Fen position :","Enter Fen String",JOptionPane.QUESTION_MESSAGE));
        }else if(actionEvent.getSource() == setDepth){
            try{
                Constants.SEARCH_DEPTH = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter search depth : (not recommended to enter higher than 3)","Set Search Depth",JOptionPane.QUESTION_MESSAGE));
            }catch(NumberFormatException nfe){
                JOptionPane.showMessageDialog(null,"Digits only!","Error",JOptionPane.ERROR_MESSAGE);
            }
        }else if(actionEvent.getSource() == saveGame){
            try {
                Parameters.saveGame(this);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,"Something went wrong!","Error",JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
