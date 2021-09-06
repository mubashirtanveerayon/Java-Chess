/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package board;

import java.awt.Color;
import javax.swing.JButton;
import piece.Piece;
import util.Constants;
import util.Util;

/**
 *
 * @author ayon2
 */
public class Tile extends JButton{
    
    public int[] position;
    public Color c;
    public Piece piece;
    
    public Tile(int file,int rank){
        position=new int[]{file,rank};
        c=(file+rank)%2!=0?new Color(203,103,57):new Color(224,170,146);
        setFocusable(false);
        setBackground(c);
    }
    
    public Tile(int[] positoion_){
        position=Util.copyPosition(positoion_);
        c=(position[0]+position[1])%2!=0?new Color(203,103,57):new Color(224,170,146);
        setFocusable(false);
        setBackground(c);
    }
    
    public boolean isOccupied(){
        return this.piece!=null;
    }
    
    public char getPieceChar(){
        return isOccupied()?piece.pieceChar:Constants.EMPTY_CHAR;
    }
    
    public Tile copy(){
        Tile tile = new Tile(this.position[0],this.position[1]);
        if(isOccupied()){
            tile.piece=piece.copy();
        }
        return tile;
    }
    
    public void showPiece(){
        if(isOccupied()){
            setIcon(piece.img);
        }else{
            setIcon(null);
        }
    }
    
}
