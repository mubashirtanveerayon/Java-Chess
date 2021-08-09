package chess;

import java.util.ArrayList;

public class Util {
    
    static char lower(char c){
        return String.valueOf(c).toLowerCase().charAt(0);
    }
    
    static char upper(char c){
        return String.valueOf(c).toUpperCase().charAt(0);
    }
    
    static int getNumericValue(char c){
        int[] values={0,1,2,3,4,5,6,7,8,9};
        for(int i=0;i<values.length;i++){
            if(String.valueOf(c).equals(String.valueOf(values[i]))){
                return values[i];
            }
        }
        return -1;
    }
    
    static boolean isOfSamePosition(Tile t1,Tile t2){
        return isOfSamePosition(t1.getPosition(),t2.getPosition());
    }
    
    static boolean isOfSamePosition(int[] a1,int[] a2){
        return isOfSamePosition(a1[0],a2[0],a1[1],a2[1]);
    }
    
    static boolean isOfSamePosition(int x1,int x2,int y1,int y2){
        return x1==x2&&y1==y2;
    }
    
    static boolean isUpper(char c){
        return upper(c)==c;
    }
    
    static boolean isLower(char c){
        return lower(c)==c;
    }
    
    static boolean isValid(int x,int y){
        return x>=0&&x<=Constants.NUM_OF_COLUMNS-1&&y>=0&&y<=Constants.NUM_OF_ROWS-1; 
    }
    
    static boolean isBlackPiece(Piece piece){
        return piece.getColor() == Constants.BLACK_PIECE_COLOR;
    }
    
    static boolean isWhitePiece(Piece piece){
        return !isBlackPiece(piece);
    }
    
    static boolean isWhiteKing(Piece piece){
        return piece.getPieceChar()=='K';
    }
    
    static boolean isBlackKing(Piece piece){
        return piece.getPieceChar()=='k';
    }
    
    static boolean isKing(Piece piece){
        return !isWhiteKing(piece)&&!isBlackKing(piece);
    }
    
    static boolean isOfSameSign(int i,int j){
        return (i<0&&j<0) || (i>0&&j>0)|| (i==0&&j==0);
    }
    
    static ArrayList<int[]> getPossibleWay(int dx,int dy,int[][] offset){
        ArrayList<int[]> newoffset=new ArrayList<>();
        for(int[] position:offset){
            int file=position[0];
            int rank=position[1];
            if(isOfSameSign(file,dx)&&isOfSameSign(rank,dy)){
                newoffset.add(position);
            }
        }
        return newoffset;
    }
    
}
