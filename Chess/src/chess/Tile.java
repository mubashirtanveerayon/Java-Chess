package chess;

import java.awt.Color;
import javax.swing.JButton;

public class Tile extends JButton{
    
    int[] position;
    Color c;
    public boolean isSelected;
    Piece piece;
    
    public Tile(int i,int j){
        position=new int[]{i,j};
        c=(i+j)%2!=0?new Color(200,150,0):new Color(180,180,180);
        isSelected=false;
        setBackground(c);
        setFocusable(false);
    }
    
    public int[] getPosition(){
        return position;
    }
    
    public Piece getPiece(){
        return piece;
    }
    
    public boolean isEmpty(){
        return piece==null;
    }
    
    public char getPieceChar(){
        return isEmpty()?' ':piece.getPieceChar();
    }
    
    public void place(Piece piece){
        this.piece=piece;
    }
    
    public void showPiece(){
        if(!isEmpty()){
            setIcon(piece.getPieceImage());
        }else{
            setIcon(null);
        }
    }
    
    public void emptyTile(){
        piece=null;
    }
   
}
