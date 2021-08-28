/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package board;

import java.util.ArrayList;
import piece.Piece;
import util.Constants;
import util.Util;

/**
 *
 * @author ayon2
 */
public class Move {
    
    Board board;
    String fen;
    
    public Move(Board board){
        this.board = board;
        fen = Util.loadFenFromBoard(board);
    }
    
    public ArrayList<int[]> generateMove(char pieceChar,int[] position,int[][] direction,boolean pseudoLegal){
        long start = System.nanoTime();
        ArrayList<int[]> pseudoLegalMoves = new ArrayList<>();
        switch(Util.toUpper(pieceChar)){
            case Constants.WHITE_KNIGHT:{
                for(int[] dir:direction){
                    int file = position[0];
                    int rank=position[1];
                    file+=dir[0];
                    rank+=dir[1];
                    if(Util.isValid(file, rank)){
                        if(board.boardChars[file][rank]==Constants.EMPTY_CHAR){
                            pseudoLegalMoves.add(new int[]{file,rank});
                        }else{
                            if(!isAlly(pieceChar,board.boardChars[file][rank])){
                                pseudoLegalMoves.add(new int[]{file,rank});
                            }
                        }
                    }
                }
                break;
            }case Constants.WHITE_PAWN:{
                int[][] defoffset = getOffset(pieceChar);
                int length=!((Util.isUpperCase(pieceChar)&&position[1]==6)||(!Util.isUpperCase(pieceChar)&&position[1]==1))&&Util.sameoffset(defoffset,direction)?direction.length-1:direction.length;
                for(int i=0;i<length;i++){
                    int file = position[0];
                    int rank = position[1];
                    file+=direction[i][0];
                    rank+=direction[i][1];
                    if(Util.isValid(file, rank)){
                        if (i > 1) {
                            if (board.boardChars[file][rank] == Constants.EMPTY_CHAR) {
                                pseudoLegalMoves.add(new int[]{file, rank});
                            }
                        } else {
                            if (board.boardChars[file][rank] != Constants.EMPTY_CHAR && !isAlly(pieceChar,board.boardChars[file][rank])) {
                                pseudoLegalMoves.add(new int[]{file, rank});
                            }
                        }
                    }
                }
                break;
            }case Constants.WHITE_KING:{
                for(int[] dir:direction){
                    int file = position[0];
                    int rank = position[1];
                    file+=dir[0];
                    rank+=dir[1];
                     if(Util.isValid(file, rank)){
                         if(board.boardChars[file][rank]==Constants.EMPTY_CHAR){
                              pseudoLegalMoves.add(new int[]{file, rank});
                         }else if(!isAlly(pieceChar,board.boardChars[file][rank])){
                              pseudoLegalMoves.add(new int[]{file, rank});
                         }
                     }
                }
                break;
            }default:{
                for(int dir[] : direction){
                    int file = position[0];
                    int rank = position[1];
                    file+=dir[0];
                    rank+=dir[1];
                    while(Util.isValid(file, rank)){
                         if(board.boardChars[file][rank]==Constants.EMPTY_CHAR){
                             pseudoLegalMoves.add(new int[]{file, rank});
                         }else{
                             if(!isAlly(pieceChar,board.boardChars[file][rank])){
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
        if(pseudoLegal){
            return pseudoLegalMoves;
        }
        board.refactorBoardChars();
        ArrayList<int[]> toRemove = new ArrayList<>();
        for(int[] i:pseudoLegalMoves){
            char prev=board.boardChars[i[0]][i[1]];
            board.boardChars[position[0]][position[1]]=Constants.EMPTY_CHAR;
            board.boardChars[i[0]][i[1]]=pieceChar;
            for (int f = 0; f < Constants.NUM_OF_COLUMNS; f++) {
                for (int r = 0; r < Constants.NUM_OF_ROWS; r++) {
                    if(board.boardChars[f][r]!=Constants.EMPTY_CHAR){
                        if(Util.isUpperCase(pieceChar)){
                            if(!Util.isUpperCase(board.boardChars[f][r])){
                                int[] target = board.getKingPosition(Util.isUpperCase(pieceChar));
                                int dx = target[0]-f;
                                int dy = target[1]-r;
                                ArrayList<int[]> pseudo = generateMove(board.boardChars[f][r],new int[]{f,r},Util.copyArrayList(Util.getDirection(dx, dy, getOffset(board.boardChars[f][r]), board.boardChars[f][r])),true);
                                for(int[] attack:pseudo){
                                    if(Util.samePosition(target, attack)){
                                        toRemove.add(i);
                                        break;
                                    }
                                }
                            }
                        }else{
                            if(Util.isUpperCase(board.boardChars[f][r])){
                                int[] target = board.getKingPosition(Util.isUpperCase(pieceChar));
                                int dx = target[0]-f;
                                int dy = target[1]-r;
                                ArrayList<int[]> pseudo = generateMove(board.boardChars[f][r],new int[]{f,r},Util.copyArrayList(Util.getDirection(dx, dy, getOffset(board.boardChars[f][r]), board.boardChars[f][r])),true);
                                for(int[] attack:pseudo){
                                    if(Util.samePosition(target, attack)){
                                        toRemove.add(i);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            board.boardChars[i[0]][i[1]] = prev;
            board.boardChars[position[0]][position[1]] = pieceChar;
        }
        pseudoLegalMoves.removeAll(toRemove);
        System.out.println(System.nanoTime()-start);
        return pseudoLegalMoves;
    }
    
    public ArrayList<int[]> generateMove(char pieceChar,int[] position,boolean pseudoLegal){
        return generateMove(pieceChar,position,getOffset(pieceChar),pseudoLegal);
    }
    
    public static int[][] getOffset(char pieceChar){
        switch(Util.toUpper(pieceChar)){
            case Constants.WHITE_KING:
                return Constants.KING_OFFSET;
            case Constants.WHITE_KNIGHT:
                return Constants.KNIGHT_OFFSET;
            case Constants.WHITE_QUEEN:
                return Constants.QUEEN_OFFSET;
            case Constants.WHITE_ROOK:
                return Constants.ROOK_OFFSET;
            case Constants.WHITE_BISHOP:
                return Constants.BISHOP_OFFSET;
            default:
                if(Util.isUpperCase(pieceChar)){
                    return Constants.WHITE_PAWN_OFFSET;
                }else{
                    return Constants.BLACK_PAWN_OFFSET;
                }
        }
    }
    
    public static boolean isAlly(char ch1,char ch2){
        return (Util.isUpperCase(ch2)&&Util.isUpperCase(ch1))||(!Util.isUpperCase(ch1)&&!Util.isUpperCase(ch2));
    }
    
    public boolean move(Tile mTile,Piece mPiece){
        if (mTile.isOccupied() && Util.toUpper(mTile.piece.pieceChar) == Constants.WHITE_KING) {
            return false;
        }
        ArrayList<int[]> legalMoves = generateMove(mPiece.pieceChar,mPiece.position,getOffset(mPiece.pieceChar),false);
        for(int[] pos:legalMoves){
            if(Util.samePosition(pos, mTile.position)){
                Tile pTile = board.getTile(mPiece.position);;
                pTile.setIcon(null);
                pTile.piece=null;
                mTile.piece=mPiece;
                mPiece.position=mTile.position;
                mTile.setIcon(mTile.piece.img);
                board.refactorBoardChars();
                fen = Util.loadFenFromBoard(board);
                System.out.println(fen);
                return true;
            }
        }
        return false;
    }
    
    
}
