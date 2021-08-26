/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import board.Board;
import board.Tile;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import piece.Piece;
import util.Constants;
import util.Util;

/**
 *
 * @author ayon2
 */
public class GamePanel extends JPanel implements ActionListener{
    
    Board board;
    Tile selectedTile;

    public GamePanel(Board board){
        super(new GridLayout(Constants.NUM_OF_COLUMNS,Constants.NUM_OF_ROWS));
        setSize(600,500);
        this.board=board;
        for(int i=0;i<Constants.NUM_OF_COLUMNS;i++){
            for(int j=0;j<Constants.NUM_OF_ROWS;j++){
                board.boardTiles[j][i].addActionListener(this);
                add(board.boardTiles[j][i]);
            }
        }
        renderBoard();
    }
    
    public void renderBoard(){
        for(int i=0;i<Constants.NUM_OF_COLUMNS;i++){
            for(int j=0;j<Constants.NUM_OF_ROWS;j++){
                board.boardTiles[i][j].showPiece();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         for(int i=0;i<Constants.NUM_OF_COLUMNS;i++){
            for(int j=0;j<Constants.NUM_OF_ROWS;j++){
                if(e.getSource()==board.boardTiles[i][j]){
                    if(selectedTile!=null){
                        if(selectedTile.isOccupied()){
                            Piece piece = selectedTile.piece;
                            ArrayList<int[]> legalMoves=piece.getLegalMoves(board);
                            for(int[] p:legalMoves){
                                if(Util.samePosition(p,board.boardTiles[i][j].position)){
                                    board.move(board.boardTiles[i][j], piece);
                                    break;
                                }
                            }
                        }
                        selectedTile=null;
                        for(Tile[] col:board.boardTiles){
                            for(Tile row:col){
                                row.setBackground(row.c);
                            }
                        }
                    }else{
                        selectedTile=board.boardTiles[i][j];
                        if(selectedTile.isOccupied()){
                            selectedTile.piece.generatePseudoLegalMoves(selectedTile.piece.offset, board);
                            ArrayList<int[]> pos=selectedTile.piece.pseudoLegalMoves;
                            for(int[] d:pos){
                                board.getTile(d).setBackground(Color.red);
                            }
                        }
                    }
                }
            }
        }
         renderBoard();
    }
    
}
