package piece;

import board.Tile;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import util.Constants;
import util.ResourceLoader;
import util.Util;

public class Piece {
    
    public double value;
    public ImageIcon img;
    public int[] position;
    public boolean white;
    public String name;
    public char pieceChar;
    public int[][] offset;
    public ArrayList<int[]> pseudoLegalMoves;
    public boolean moved;
    
    public Piece(String name,int[] position){
        this.name=name;
        pieceChar=name.charAt(0);
        white=Util.isUpperCase(name.charAt(0));
        img=white?new ImageIcon(ResourceLoader.load(Constants.WHITE_IMG_PATH+name.toLowerCase()+".png")):new ImageIcon(ResourceLoader.load(Constants.BLACK_IMG_PATH+name.toLowerCase()+".png"));
        this.position=Util.copyPosition(position);
        generateOffset();
    }
    
    public Piece(char pieceChar,int[] position){
        this.pieceChar=pieceChar;
        this.position=Util.copyPosition(position);
        for(String names:Constants.PIECE_NAMES){
            if(pieceChar==names.charAt(0)){
                this.name=names;
                break;
            }
        }
        white=Util.isUpperCase(name);
        img=white?new ImageIcon(Constants.WHITE_IMG_PATH+name.toLowerCase()+".png"):new ImageIcon(Constants.BLACK_IMG_PATH+name.toLowerCase()+".png");
        generateOffset();
    }
    
    public void generateOffset(){
        switch(Util.toUpper(pieceChar)){
            case Constants.WHITE_KING:
                value = Constants.KING_VALUE;
                offset=Constants.KING_OFFSET;
                break;
            case Constants.WHITE_QUEEN:
                value = Constants.QUEEN_VALUE;
                offset=Constants.QUEEN_OFFSET;
                break;
            case Constants.WHITE_KNIGHT:
                value = Constants.KNIGHT_VALUE;
                offset=Constants.KNIGHT_OFFSET;
                break;
            case Constants.WHITE_ROOK:
                offset=Constants.ROOK_OFFSET;
                break;
            case Constants.WHITE_BISHOP:
                value = Constants.BISHOP_VALUE;
                offset=Constants.BISHOP_OFFSET;
                break;
            default:
                value = Constants.PAWN_VALUE;
                offset=white?Constants.WHITE_PAWN_OFFSET:Constants.BLACK_PAWN_OFFSET;
        }
        pseudoLegalMoves=new ArrayList<>();
    }
    
    public Piece copy(){
       return new Piece(this.name,this.position);
    }
    
    public boolean isAlly(Piece piece){
        return (white&&piece.white)||(!white&&!piece.white);
    }
    
    public boolean isAlly(char pChar){
        return (Util.isUpperCase(pChar)&&Util.isUpperCase(pieceChar))||(!Util.isUpperCase(pChar)&&!Util.isUpperCase(pieceChar));
    }
    
    public boolean isAlly(Tile tile)
    {
        if(tile.isOccupied()){
            return isAlly(tile.piece);
        }else{
            return false;
        }
    }
        
}
