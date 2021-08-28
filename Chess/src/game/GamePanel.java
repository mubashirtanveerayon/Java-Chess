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
import util.Constants;
import util.Util;

public class GamePanel extends JPanel implements ActionListener{
    
    Board board;
    Tile selectedTile;
    Move move;

    public GamePanel(Board board){
        super(new GridLayout(Constants.NUM_OF_COLUMNS,Constants.NUM_OF_ROWS));
        setSize(600,500);
        this.board=board;
        move = new Move(board);
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
                            if(!move.move(board.boardTiles[i][j], piece)){
                                System.out.println("Not a legal move!");
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
                        selectedTile.setBackground(Color.green);
                        if(selectedTile.isOccupied()){
                            ArrayList<int[]> pos =move.generateMove(selectedTile.piece.pieceChar,selectedTile.position,false); //selectedTile.piece.getLegalMoves(board);
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
