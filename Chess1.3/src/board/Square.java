package board;

import java.awt.Color;
import javax.swing.JButton;
import piece.Piece;
import util.Constants;

public class Square extends JButton {

    public int[] position;

    public Color bgColor;

    public Piece piece;

    public Square(int[] position){
        this.position = position;
        bgColor = (position[0] + position[1])%2!=0?new Color(203,103,57):new Color(224,170,146);
        setFocusable(false);
        setBackground(bgColor);
    }

    public boolean isOccupied(){
        return this.piece!=null;
    }

    public char getPieceChar(){
        return isOccupied()?piece.pieceChar: Constants.EMPTY_CHAR;
    }

    public void showPiece(){
        if(isOccupied()){
            setIcon(piece.img);
        }else{
            setIcon(null);
        }
    }

    public Square copy(){
        Square square = new Square(position);
        if(isOccupied()){
            square.piece = piece.copy();
        }
        return square;
    }

}
