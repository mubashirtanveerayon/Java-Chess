/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import board.Board;
import board.Move;
import board.Tile;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.JPanel;
import piece.Piece;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import util.Constants;
import util.Util;

public class GamePanel extends JPanel implements ActionListener{
    
    public Board board;
    public Tile selectedTile;
    public Move move;

    InputStream in;
    AudioStream as;

    public GamePanel(String fen){
        super(new GridLayout(Constants.NUM_OF_COLUMNS,Constants.NUM_OF_ROWS));
        setSize(600,500);
        this.board=Util.loadBoardFromFen(fen);
        boolean toMove = fen.split(" ")[1].equals(String.valueOf(Constants.WHITE));
        move = new Move(board,toMove,fen);
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
    
    public void renderBoard(){
        for(int i=0;i<Constants.NUM_OF_COLUMNS;i++){
            for(int j=0;j<Constants.NUM_OF_ROWS;j++){
                board.boardTiles[i][j].showPiece();
            }
        }
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

    @Override
    public void actionPerformed(ActionEvent e) {
         for(int i=0;i<Constants.NUM_OF_COLUMNS;i++){
            for(int j=0;j<Constants.NUM_OF_ROWS;j++){
                if(e.getSource()==board.boardTiles[i][j]){
                    if(selectedTile!=null){
                        if(selectedTile.isOccupied()){
                            Piece piece = selectedTile.piece;
                            int type = board.boardTiles[i][j].isOccupied()?1:0;
                            if(!move.move(board.boardTiles[i][j], piece)){
                                System.out.println("Not a legal move!");
                            }else{
                                playAudio(type);
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
                        selectedTile.setBackground(new Color(0,255,0));
                        if(selectedTile.isOccupied()){
                            ArrayList<int[]> pos =move.generateMove(selectedTile.piece.pieceChar,selectedTile.position,false); //selectedTile.piece.getLegalMoves(board);
                            for(int[] d:pos){
                                board.getTile(d).setBackground(new Color(255,0,0));
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
