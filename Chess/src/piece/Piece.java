package piece;

import board.Board;
import board.Tile;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import util.Constants;
import util.ResourceLoader;
import util.Util;

public class Piece {
    
    
    public ImageIcon img;
    public int[] position;
    public boolean white;
    public String name;
    public char pieceChar;
    public int[][] offset;
    public ArrayList<int[]> pseudoLegalMoves;
    
    public Piece(String name,int[] position){
        this.name=name;
        pieceChar=name.charAt(0);
        white=Util.isUpperCase(name.charAt(0));
        img=white?new ImageIcon(ResourceLoader.load(Constants.WHITE_IMG_PATH+name.toLowerCase()+".png")):new ImageIcon(ResourceLoader.load(Constants.BLACK_IMG_PATH+name.toLowerCase()+".png"));
        this.position=position;
        generateOffset();
    }
    
    public Piece(char pieceChar,int[] position){
        this.pieceChar=pieceChar;
        this.position=position;
        for(String name:Constants.PIECE_NAMES){
            if(pieceChar==name.charAt(0)){
                this.name=name;
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
                offset=Constants.KING_OFFSET;
                break;
            case Constants.WHITE_QUEEN:
                offset=Constants.QUEEN_OFFSET;
                break;
            case Constants.WHITE_KNIGHT:
                offset=Constants.KNIGHT_OFFSET;
                break;
            case Constants.WHITE_ROOK:
                offset=Constants.ROOK_OFFSET;
                break;
            case Constants.WHITE_BISHOP:
                offset=Constants.BISHOP_OFFSET;
                break;
            default:
                offset=white?Constants.WHITE_PAWN_OFFSET:Constants.BLACK_PAWN_OFFSET;
        }
        pseudoLegalMoves=new ArrayList<>();
    }
        
    public ArrayList<int[]> getLegalMoves(Board board){
        generatePseudoLegalMoves(offset,board);
        board.refactorBoardChars();
        ArrayList<int[]> toRemove = new ArrayList<>();
        for(int[] iter:pseudoLegalMoves){
            char prev=board.boardChars[iter[0]][iter[1]];
            board.boardChars[position[0]][position[1]]=' ';
            board.boardChars[iter[0]][iter[1]]=pieceChar;
            int[] target = board.getKingPosition(white);
            for(int f=0;f<Constants.NUM_OF_COLUMNS;f++){
                for(int r=0;r<Constants.NUM_OF_ROWS;r++){
                    if(board.boardChars[f][r]!=' '){
                        if(white){
                            if(!Util.isUpperCase(board.boardChars[f][r])){
                               Piece piece = new Piece(board.boardChars[f][r],new int[]{f,r});
                               int dx=target[0]-piece.position[0];
                               int dy=target[1]-piece.position[1];
                               ArrayList<int[]> dir=Util.getDirection(dx, dy, piece.offset,piece.pieceChar);
                               piece.generatePseudoLegalMoves(dir, board);
                               for(int[] d:piece.pseudoLegalMoves){
                                   if(Util.samePosition(d,target)){
                                       System.out.println(pieceChar+":"+iter[0]+","+iter[1]);
                                       toRemove.add(iter);
                                       break;
                                   }
                               }
                            }
                        }else{
                            if(Util.isUpperCase(board.boardChars[f][r])){
                                Piece piece = new Piece(board.boardChars[f][r],new int[]{f,r});
                                int dx=target[0]-piece.position[0];
                               int dy=target[1]-piece.position[1];
                                ArrayList<int[]> dir=Util.getDirection(dx, dy, piece.offset,piece.pieceChar);
                                piece.generatePseudoLegalMoves(dir, board);
                                for(int[] d:piece.pseudoLegalMoves){
                                   if(Util.samePosition(d,target)){
                                       System.out.println(pieceChar+":"+iter[0]+","+iter[1]);
                                       toRemove.add(iter);
                                       break;
                                   }
                               }
                            }
                        }
                    }
                }
            }
            board.boardChars[iter[0]][iter[1]]=prev;
            board.boardChars[position[0]][position[1]]=pieceChar;
        }
        pseudoLegalMoves.removeAll(toRemove);
        return pseudoLegalMoves;
    }
    
    public void generatePseudoLegalMoves(int[][] legaldir,Board board){
        pseudoLegalMoves.clear();
        switch(Util.toUpper(pieceChar)){
            case Constants.WHITE_KNIGHT:{
                for(int[] dir:legaldir){
                    int file = position[0];
                    int rank=position[1];
                    file+=dir[0];
                    rank+=dir[1];
                    if(Util.isValid(file, rank)){
                        if(board.boardChars[file][rank]==' '){
                            pseudoLegalMoves.add(new int[]{file,rank});
                        }else{
                            if(!isAlly(board.boardChars[file][rank])){
                                pseudoLegalMoves.add(new int[]{file,rank});
                            }
                        }
                    }
                }
                break;
            }case Constants.WHITE_PAWN:{
                int length=(white&&position[1]==6)||(!white&&position[1]==1)?legaldir.length:legaldir.length-1;
                for(int i=0;i<length;i++){
                    int file = position[0];
                    int rank = position[1];
                    file+=legaldir[i][0];
                    rank+=legaldir[i][1];
                    if(Util.isValid(file, rank)){
                        if (i > 1) {
                            if (board.boardChars[file][rank] == ' ') {
                                pseudoLegalMoves.add(new int[]{file, rank});
                            }
                        } else {
                            if (board.boardChars[file][rank] != ' ' && !isAlly(board.boardChars[file][rank])) {
                                pseudoLegalMoves.add(new int[]{file, rank});
                            }
                        }
                    }
                }
                break;
            }case Constants.WHITE_KING:{
                for(int[] dir:legaldir){
                    int file = position[0];
                    int rank = position[1];
                    file+=dir[0];
                    rank+=dir[1];
                     if(Util.isValid(file, rank)){
                         if(board.boardChars[file][rank]==' '){
                              pseudoLegalMoves.add(new int[]{file, rank});
                         }else if(!isAlly(board.boardChars[file][rank])){
                              pseudoLegalMoves.add(new int[]{file, rank});
                         }
                     }
                }
                break;
            }default:{
                for(int dir[] : legaldir){
                    int file = position[0];
                    int rank = position[1];
                    file+=dir[0];
                    rank+=dir[1];
                    while(Util.isValid(file, rank)){
                         if(board.boardChars[file][rank]==' '){
                             pseudoLegalMoves.add(new int[]{file, rank});
                         }else{
                             if(!isAlly(board.boardChars[file][rank])){
                                 pseudoLegalMoves.add(new int[]{file, rank});
                             }
                             break;
                         }
                        file+=dir[0];
                        rank+=dir[1];
                    }
                }
            }
        }
    }
    
    public void generatePseudoLegalMoves(ArrayList<int[]> legaldir,Board board){
        int[][] nlegaldir=new int[legaldir.size()][];
        for(int i=0;i<legaldir.size();i++){
            nlegaldir[i]=legaldir.get(i);
        }
        generatePseudoLegalMoves(nlegaldir,board);
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
