/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import board.Board;
import board.Move;
import board.Tile;
import java.util.ArrayList;
import piece.Piece;

/**
 *
 * @author ayon2
 */
public class Util {
    public static int getNumericValue(char c){
        int[] values={0,1,2,3,4,5,6,7,8,9};
        for(int i=0;i<values.length;i++){
            if(String.valueOf(c).equals(String.valueOf(values[i]))){
                return values[i];
            }
        }
        return -1;
    }
    
    public static boolean isUpperCase(String text){
        return text.equals(text.toUpperCase());
    }
    
    public static boolean isUpperCase(char t){
        return t==toUpper(t);
    }
    
    public static char toUpper(char ch){
        return String.valueOf(ch).toUpperCase().charAt(0);
    }
    
    public static int[] copyPosition(int[] position){
        return new int[]{position[0],position[1]};
    }
    
    public static boolean isOfSameDir(int a,int b){
        return (a<0&&b<0)||(a>0&&b>0)||(a==0&&b==0);
    }
    
    public static boolean samePosition(int[] a,int[] b){
        return a[0]==b[0]&&a[1]==b[1];
    }
    
    public static boolean sameoffset(int[][] dir1,int[][] dir2){
        if(dir1.length!=dir2.length){
            return false;
        }
        for (int i = 0; i < dir1.length; i++) {
            if(!samePosition(dir1[i],dir2[i])){
                return false;
            }
        }
        return true;
    }
    
    public static ArrayList<int[]> generateCaptureMove(char pieceChar, int[] position, int[][] direction, Board board) {
        ArrayList<int[]> captureMoves = new ArrayList<>();
        switch (Util.toUpper(pieceChar)) {
            case Constants.WHITE_KNIGHT: {
                for (int[] dir : direction) {
                    int file = position[0];
                    int rank = position[1];
                    file += dir[0];
                    rank += dir[1];
                    if (Util.isValid(file, rank)) {
                        if (board.boardChars[file][rank] != Constants.EMPTY_CHAR) {
                            if (!isAlly(pieceChar, board.boardChars[file][rank])) {
                                captureMoves.add(new int[]{file, rank});
                            }
                        }
                    }
                }
                break;
            }
            case Constants.WHITE_PAWN: {
                int[][] defoffset = getOffset(pieceChar);
                int length = !((Util.isUpperCase(pieceChar) && position[1] == 6) || (!Util.isUpperCase(pieceChar) && position[1] == 1)) && Util.sameoffset(defoffset, direction) ? direction.length - 1 : direction.length;
                for (int i = 0; i < length; i++) {
                    int file = position[0];
                    int rank = position[1];
                    file += direction[i][0];
                    rank += direction[i][1];
                    if (Util.isValid(file, rank)) {
                        if (i < 1) {
                            if (board.boardChars[file][rank] != Constants.EMPTY_CHAR && !isAlly(pieceChar, board.boardChars[file][rank])) {
                                captureMoves.add(new int[]{file, rank});
                            }
                        }
                    }
                }
                break;
            }
            case Constants.WHITE_KING: {
                for (int[] dir : direction) {
                    int file = position[0];
                    int rank = position[1];
                    file += dir[0];
                    rank += dir[1];
                    if (Util.isValid(file, rank)) {
                        if (board.boardChars[file][rank] != Constants.EMPTY_CHAR) {
                            if (!isAlly(pieceChar, board.boardChars[file][rank])) {
                                captureMoves.add(new int[]{file, rank});
                            }
                        }
                    }
                }
                break;
            }
            default: {
                for (int dir[] : direction) {
                    int file = position[0];
                    int rank = position[1];
                    file += dir[0];
                    rank += dir[1];
                    while (Util.isValid(file, rank)) {
                        if (board.boardChars[file][rank] != Constants.EMPTY_CHAR) {
                            if (!isAlly(pieceChar, board.boardChars[file][rank])) {
                                captureMoves.add(new int[]{file, rank});
                            }
                            break;
                        }
                        file += dir[0];
                        rank += dir[1];
                    }
                }
            }
        }

        return captureMoves;
    }
    
    public static ArrayList<int[]> generateCaptureMove(char pieceChar, int[] position, Board board) {
        return generateCaptureMove(pieceChar, position, getOffset(pieceChar), board);
    }
    
    public static boolean isKingInCheck(Board board, boolean white) {
        int[] targetPos = board.getKingPosition(white);
        for (int i = 0; i < Constants.NUM_OF_COLUMNS; i++) {
            for (int j = 0; j < Constants.NUM_OF_ROWS; j++) {
                if (board.boardChars[i][j] != Constants.EMPTY_CHAR) {
                    if (white) {
                        if (!Util.isUpperCase(board.boardChars[i][j])) {
                            int dx = targetPos[0] - i;
                            int dy = targetPos[1] - j;
                            ArrayList<int[]> dir = Util.getDirection(dx, dy, getOffset(board.boardChars[i][j]), board.boardChars[i][j]);
                            int[][] dirOffset = Util.copyArrayList(dir);
                            ArrayList<int[]> captures = generateCaptureMove(board.boardChars[i][j], new int[]{i, j}, dirOffset, board);
                            for (int[] capture : captures) {
                                if (Util.samePosition(capture, targetPos)) {
                                    return true;
                                }
                            }
                        }
                    } else {
                        if (Util.isUpperCase(board.boardChars[i][j])) {
                            int dx = targetPos[0] - i;
                            int dy = targetPos[1] - j;
                            ArrayList<int[]> dir = Util.getDirection(dx, dy, getOffset(board.boardChars[i][j]), board.boardChars[i][j]);
                            int[][] dirOffset = Util.copyArrayList(dir);
                            ArrayList<int[]> captures = generateCaptureMove(board.boardChars[i][j], new int[]{i, j}, dirOffset, board);
                            for (int[] capture : captures) {
                                if (Util.samePosition(capture, targetPos)) {
                                    System.out.println("gg : " + board.boardChars[i][j]);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isAlly(char ch1, char ch2) {
        return (Util.isUpperCase(ch2) && Util.isUpperCase(ch1)) || (!Util.isUpperCase(ch1) && !Util.isUpperCase(ch2));
    }

    public static int[][] getOffset(char pieceChar) {
        switch (Util.toUpper(pieceChar)) {
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
                if (Util.isUpperCase(pieceChar)) {
                    return Constants.WHITE_PAWN_OFFSET;
                } else {
                    return Constants.BLACK_PAWN_OFFSET;
                }
        }
    }
    
    public static boolean[] generateCastlingMove(Board board, boolean white) {
        boolean queenSideCastling, kingSideCastling;
        String cFen = Move.history.get(Move.history.size() - 1).split(" ")[2];
        char king = white ? Constants.WHITE_KING : Constants.BLACK_KING;
        char queen = white ? Constants.WHITE_QUEEN : Constants.BLACK_QUEEN;
        kingSideCastling = cFen.contains(String.valueOf(king));
        queenSideCastling = cFen.contains(String.valueOf(queen));
        boolean isKingInCheck = isKingInCheck(board, white);
        if (cFen.equals("-") || isKingInCheck) {
            queenSideCastling = false;
            kingSideCastling = false;
        } else {
            int rank = white ? 7 : 0;
            int[] kPos = new int[]{4, rank};
            if (queenSideCastling) {
                boolean queenSideUnoccupied = board.boardChars[1][rank] == Constants.EMPTY_CHAR
                        && board.boardChars[2][rank] == Constants.EMPTY_CHAR
                        && board.boardChars[3][rank] == Constants.EMPTY_CHAR;
                if (queenSideUnoccupied) {
                    for (int i = 2; i < 4; i++) {
                        board.boardChars[i][rank] = king;
                        board.boardChars[kPos[0]][kPos[1]] = Constants.EMPTY_CHAR;
                        boolean check = isKingInCheck(board, white);
                        board.boardChars[kPos[0]][kPos[1]] = king;
                        board.boardChars[i][rank] = Constants.EMPTY_CHAR;
                        if (check) {
                            queenSideCastling = false;
                            break;
                        }
                    }
                } else {
                    queenSideCastling = false;
                }
            }
            if (kingSideCastling) {
                boolean kingSideUnoccupied = board.boardChars[5][rank] == Constants.EMPTY_CHAR
                        && board.boardChars[6][rank] == Constants.EMPTY_CHAR;
                if (kingSideUnoccupied) {
                    for (int i = 5; i < 7; i++) {
                        board.boardChars[i][rank] = king;
                        board.boardChars[kPos[0]][kPos[1]] = Constants.EMPTY_CHAR;
                        boolean check = isKingInCheck(board, white);
                        board.boardChars[i][rank] = Constants.EMPTY_CHAR;
                        board.boardChars[kPos[0]][kPos[1]] = king;
                        if (check) {
                            kingSideCastling = false;
                            break;
                        }
                    }
                } else {
                    kingSideCastling = false;
                }
            }
        }
        return new boolean[]{queenSideCastling, kingSideCastling};
    }
    
    public static String loadFenFromBoard(Board board){
        StringBuilder sb = new StringBuilder();
        Tile[][] boardTiles = board.boardTiles;
        for(int i=0;i<Constants.NUM_OF_ROWS;i++){
            int gap=0;
            for(int j=0;j<Constants.NUM_OF_ROWS;j++){
                if(boardTiles[j][i].isOccupied()){
                    if (gap != 0) {
                        sb.append(gap);
                    }
                    sb.append(boardTiles[j][i].getPieceChar());
                    gap = 0;
                }else{
                    gap++;
                }
            }
            if(gap!=0){
                sb.append(gap);
            }
            if(i!=Constants.NUM_OF_COLUMNS-1){
                sb.append('/');
            }
        }
        if(Move.whiteToMove){
            sb.append(" w");
        }else{
            sb.append(" b");
        }
        
        String prevFen = Move.history.get(Move.history.size()-1).split(" ")[0];

        char[][] prevBoardChars = new char[Constants.NUM_OF_COLUMNS][Constants.NUM_OF_ROWS];
        
        int file = 0;
        int rank = 0;
        for (int i = 0; i < prevFen.length(); i++) {
            char c=prevFen.charAt(i);
            if(c=='/'){
                file=0;
                rank++;
            }else if(Character.isDigit(c)){
                file+=getNumericValue(c);
            }else{
                prevBoardChars[file][rank] = c;
                file++;
            }
        }
        
        String cFen = Move.history.get(Move.history.size()-1).split(" ")[2];
        boolean castlingPossible = !cFen.equals("-");
        sb.append(" ");
        if(castlingPossible){
            boolean wKingInPosition = board.boardChars[4][7] == Constants.WHITE_KING;
            boolean bKingInPosition = board.boardChars[4][0] == Constants.BLACK_KING;
            boolean wQRookInPosition = board.boardChars[0][7] == Constants.WHITE_ROOK;
            boolean wKRookInPosition = board.boardChars[7][7] == Constants.WHITE_ROOK;
            boolean bQRookInPosition = board.boardChars[0][0] == Constants.BLACK_ROOK;
            boolean bKRookInPosition = board.boardChars[7][0] == Constants.BLACK_ROOK;
            boolean wKingMoved = (prevBoardChars[4][7]!=Constants.WHITE_KING);
            boolean bKingMoved = (prevBoardChars[4][0]!=Constants.BLACK_KING);
            boolean wQRookMoved = (prevBoardChars[0][7]!=Constants.WHITE_ROOK);
            boolean wKRookMoved = (prevBoardChars[7][7]!=Constants.WHITE_ROOK);
            boolean bQRookMoved = (prevBoardChars[0][0]!=Constants.BLACK_ROOK);
            boolean bKRookMoved = (prevBoardChars[7][0]!=Constants.BLACK_ROOK);
            
            if(wKingInPosition&&wKRookInPosition&&!wKingMoved&&!wKRookMoved){
                sb.append(Constants.WHITE_KING);
            }
            if(wKingInPosition&&wQRookInPosition&&!wKingMoved&&!wQRookMoved){
                sb.append(Constants.WHITE_QUEEN);
            }
            
            if(bKingInPosition&&bKRookInPosition&&!bKingMoved&&!bKRookMoved){
                sb.append(Constants.BLACK_KING);
            }
            if(bKingInPosition&&bQRookInPosition&&!bKingMoved&&!bQRookMoved){
                sb.append(Constants.BLACK_QUEEN);
            }
            
        }
        
        if(!castlingPossible||sb.charAt(sb.length()-1)==' '){
            sb.append("-");
        }
        
        return sb.toString();
    }    
    
    public static Board loadBoardFromFen(String Fen){
        String fen=Fen.split(" ")[0];
        Tile[][] boardTiles=new Tile[Constants.NUM_OF_COLUMNS][Constants.NUM_OF_ROWS];
        for(int i=0;i<Constants.NUM_OF_COLUMNS;i++){
            for(int j=0;j<Constants.NUM_OF_ROWS;j++){
                boardTiles[i][j]=new Tile(i,j);
            }
        }
        int file=0;
        int rank=0;
        for(int i=0;i<fen.length();i++){
            char c=fen.charAt(i);
            if(c=='/'){
                file=0;
                rank++;
            }else if(Character.isDigit(c)){
                file+=getNumericValue(c);
            }else{
                for(String name:Constants.PIECE_NAMES){
                    if(c==name.charAt(0)){
                        Piece piece = new Piece(name,new int[]{file,rank});
                        boardTiles[file][rank].piece=piece;
                    }
                }
                file++;
            }
        }
        Board board= new Board(boardTiles);
        return board;
    }
    
    public static ArrayList<int[]> getDirection(int dx,int dy,int[][] offset,char pieceChar){
        ArrayList<int[]> path=new ArrayList<>();
        if(toUpper(pieceChar)==Constants.WHITE_PAWN){
            for (int i = 0; i < offset.length-2; i++) {
                if(isOfSameDir(offset[i][0],dx)&&isOfSameDir(offset[i][1],dy)){
                    path.add(offset[i]);
                }
            }
        }else{
            for(int[] p:offset){
                if(isOfSameDir(p[0],dx)&&isOfSameDir(p[1],dy)){
                    path.add(p);
                }
            }
        }
        return path;
    }
    
    public static boolean isValid(int file,int rank){
        return file>=0&&rank>=0&&file<Constants.NUM_OF_COLUMNS&&rank<Constants.NUM_OF_ROWS;
    }
    
    public static ArrayList<int[]> copy(ArrayList<int[]> from){
        ArrayList<int[]> newList = new ArrayList<>();
        for (int i = 0; i < from.size(); i++) {
            newList.add(from.get(i));
        }
        return newList;
    }
    
    public static int[][] copyArrayList(ArrayList<int[]> from){
        int[][] nList = new int[from.size()][];
        for(int i=0;i<from.size();i++){
            nList[i] = from.get(i);
        }
        return nList;
    }
}
