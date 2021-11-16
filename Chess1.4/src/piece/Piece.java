package piece;

import util.Constants;
import util.ResourceLoader;
import util.Util;

import javax.swing.ImageIcon;

public class Piece {

    public ImageIcon img;

    public char pieceChar;


    public Piece(char pieceChar){
        this.pieceChar = pieceChar;
        img = getImage(pieceChar);
    }

    public static ImageIcon getImage(char pieceChar){
        boolean white = Util.isUpperCase(pieceChar);
        String name = getName(pieceChar);
        return white?new ImageIcon((Constants.WHITE_IMG_PATH+name.toLowerCase()+".png")):new ImageIcon((Constants.BLACK_IMG_PATH+name.toLowerCase()+".png"));
    }

    public static String getName(char pieceChar){
        for(String name:Constants.PIECE_NAMES){
            if(pieceChar == name.charAt(0)){
                return name;
            }
        }
        return "";
    }

    public Piece copy(){
        return new Piece(pieceChar);
    }

}
