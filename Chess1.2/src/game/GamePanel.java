/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import board.Board;
import board.Move;
import board.Tile;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import piece.Piece;
import stockfish.Stockfish;
import util.Constants;
import util.GameParameter;
import util.Util;

public class GamePanel extends JPanel implements ActionListener {

    public Board board;
    public Tile selectedTile;
    public Move move;
    
    Stockfish ai;

    public GamePanel(String fen){
        super(new GridLayout(Constants.NUM_OF_COLUMNS,Constants.NUM_OF_ROWS));
        setSize(600,500);
        this.board=Util.loadBoardFromFen(fen);
        boolean toMove = fen.split(" ")[1].equals(String.valueOf(Constants.WHITE));
        move = new Move(board,toMove,fen);
        ai = new Stockfish();
        registerComponent();
        renderBoard();
    }
    
    public void registerComponent(){
        for(int i=0;i<Constants.NUM_OF_COLUMNS;i++){
            for(int j=0;j<Constants.NUM_OF_ROWS;j++){
                board.boardTiles[j][i].addActionListener(this);
                add(board.boardTiles[j][i]);
            }
        }
    }

    public void removeComponent(){
        for(int i=0;i<Constants.NUM_OF_COLUMNS;i++){
            for(int j=0;j<Constants.NUM_OF_ROWS;j++){
                remove(board.boardTiles[i][j]);
            }
        }
    }

    public void renderBoard() {
        for (int i = 0; i < Constants.NUM_OF_COLUMNS; i++) {
            for (int j = 0; j < Constants.NUM_OF_ROWS; j++) {
                board.boardTiles[i][j].showPiece();
            }
        }
    }

    public Thread cpu() {
        Thread thread = new Thread() {
            public void run() {
                if (ai.startEngine()) {
                    String fen = Util.loadFenFromBoard(board);
                    String bestMove = null;
                    int tried = 0, waitTime = 100;
                    while (tried < 20) {
                        try {
                            bestMove = ai.getBestMove(fen, waitTime);
                            break;
                        } catch (Exception ex) {
                            waitTime += 100;
                            tried++;
                        }
                    }
                    int[][] bestMovePosition = Util.parseMove(bestMove);
                    Tile from = board.boardTiles[bestMovePosition[0][0]][bestMovePosition[0][1]];
                    Tile to = board.boardTiles[bestMovePosition[1][0]][bestMovePosition[1][1]];
                    if (!move.move(from, to)) {
                        System.out.println("Not a legal move!");
                    }
                    ai.stopEngine();
                    renderBoard();
                }
            }
        };
        return thread;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < Constants.NUM_OF_COLUMNS; i++) {
            for (int j = 0; j < Constants.NUM_OF_ROWS; j++) {
                if (e.getSource() == board.boardTiles[i][j]) {
                    if (selectedTile != null) {
                        if (GameParameter.whiteToMove && move.move(selectedTile, board.boardTiles[i][j])) {
                            cpu().start();
                        } else {
                            System.out.println("Not a legal move!");
                        }
                        selectedTile = null;
                        for (Tile[] col : board.boardTiles) {
                            for (Tile row : col) {
                                row.setBackground(row.c);
                            }
                        }
                    } else {
                        selectedTile = board.boardTiles[i][j];
                        selectedTile.setBackground(new Color(0, 255, 0));
                        if (selectedTile.isOccupied()) {
                            ArrayList<int[]> pos = move.generateMove(selectedTile.piece.pieceChar, selectedTile.position, false); //selectedTile.piece.getLegalMoves(board);
                            for (int[] d : pos) {
                                board.getTile(d).setBackground(new Color(255, 0, 0));
                            }
                        }
                    }
                }
            }
        }
        //System.out.println("evaluation : "+board.evaluateBoard());
        //board.printBoard();
        System.out.println(Util.loadFenFromBoard(board));
        renderBoard();
    }

}
