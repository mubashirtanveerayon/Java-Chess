/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;

/**
 *
 * @author ayon2
 */
public class Util {

    public static int getNumericValue(char c) {
        return Integer.parseInt(Character.toString(c));
    }

    public static boolean isUpperCase(char t) {
        return t == toUpper(t);
    }

    public static char toUpper(char c) {
        return Character.toString(c).toUpperCase().charAt(0);
    }

    public static boolean isOfSameDir(int a, int b) {
        return (a < 0 && b < 0) || (a > 0 && b > 0) || (a == 0 && b == 0);
    }

    public static boolean samePosition(int[] a, int[] b) {
        return a[0] == b[0] && a[1] == b[1];
    }

    public static boolean isAlly(char ch1, char ch2) {
        return (Util.isUpperCase(ch2) && Util.isUpperCase(ch1)) || (!Util.isUpperCase(ch1) && !Util.isUpperCase(ch2));
    }

    public static int[][] getOffset(char pieceChar) {
        switch (toUpper(pieceChar)) {
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

    public static float getValue(char pieceChar){
        switch (Util.toUpper(pieceChar)) {
            case Constants.WHITE_KING:
                return Constants.KING_VALUE;
            case Constants.WHITE_KNIGHT:
                return Constants.KNIGHT_VALUE;
            case Constants.WHITE_QUEEN:
                return Constants.QUEEN_VALUE;
            case Constants.WHITE_ROOK:
                return Constants.ROOK_VALUE;
            case Constants.WHITE_BISHOP:
                return Constants.BISHOP_VALUE;
            default:
                return Constants.PAWN_VALUE;
        }
    }

    public static int[][] getDirection(int dx, int dy, char pieceChar) {
        ArrayList<int[]> path = new ArrayList<>();
        int[][] offset = getOffset(pieceChar);
        if (toUpper(pieceChar) == Constants.WHITE_PAWN) {
            for (int i = 0; i < offset.length - 1; i++) {
                if (isOfSameDir(offset[i][0], dx) && isOfSameDir(offset[i][1], dy)) {
                    path.add(offset[i]);
                }
            }
        } else {
            for (int[] p : offset) {
                if (isOfSameDir(p[0], dx) && isOfSameDir(p[1], dy)) {
                    path.add(p);
                }
            }
        }
        return copyArrayList(path);
    }

    public static boolean isValid(int file, int rank) {
        return file >= 0 && rank >= 0 && file < Constants.COLUMNS && rank < Constants.ROWS;
    }

    public static int[][] copyArrayList(ArrayList<int[]> from) {
        int[][] nList = new int[from.size()][];
        for (int i = 0; i < from.size(); i++) {
            nList[i] = from.get(i);
        }
        return nList;
    }

    public static int[] cvtPosition(String strPos) {
        strPos = strPos.toLowerCase();
        int file, rank;
        try {
            file = Constants.FILES.indexOf(strPos.charAt(0));
            rank = Constants.RANKS.indexOf(strPos.charAt(1));
        } catch (ArrayIndexOutOfBoundsException ex) {
            file = -1;
            rank = -1;
        }
        return new int[]{file, rank};
    }

    public static String toString(int[] position) {
        String file = String.valueOf(Constants.FILES.charAt(position[0]));
        String rank = String.valueOf(Constants.RANKS.charAt(position[1]));
        return file + rank;
    }
    
    public static int[][] parseMove(String pos){
        int[] initPosition = cvtPosition(new StringBuffer(pos).delete(2, pos.length()).toString());
        int[] finalPosition = null;
        if(pos.length()==5) {
            finalPosition = cvtPosition(Character.toString(pos.charAt(2)) + Character.toString(pos.charAt(3)));
        }
        else { finalPosition = cvtPosition(new StringBuffer(pos).delete(0, 2).toString());}

        return new int[][]{initPosition,finalPosition};
    }

    public static String parseMove(int[] from,int[] to){
        return toString(from)+toString(to);
    }

    public static String parseMove(int[] move ){
        if(move.length == 5){
            return toString(new int[]{move[0],move[1]})+toString(new int[]{move[2],move[3]})+String.valueOf(move[4]);
        }
        return toString(new int[]{move[0],move[1]})+toString(new int[]{move[2],move[3]});
    }

    public static int[] getKingPosition(char[][] boardChars,boolean white){
        char target=white?Constants.WHITE_KING:Constants.BLACK_KING;
        for(int i=0;i<Constants.COLUMNS;i++){
            for (int j = 0; j < Constants.ROWS; j++) {
                if(boardChars[i][j]==target){
                    return new int[]{i,j};
                }
            }
        }
        return null;
    }
    
    public static String printBoard(char[][] boardChar,boolean flipped) {
        if(flipped){
            String board = "\n    h   g   f   e   d   c   b   a\n  +---+---+---+---+---+---+---+---+\n" ;
            for(int i=Constants.COLUMNS-1;i>=0;i--){
                board += String.valueOf(Constants.ROWS - i) + " | ";
                for(int j = Constants.ROWS-1; j >= 0; j--){
                    board += (String.valueOf(boardChar[j][i]) + " | ");
                }
                board += String.valueOf(Constants.ROWS - i) + "\n  +---+---+---+---+---+---+---+---+\n";
            }
            board+="    h   g   f   e   d   c   b   a\n";
            return board;
        }else{
            String board = "\n    a   b   c   d   e   f   g   h\n  +---+---+---+---+---+---+---+---+\n";
            for (int i = 0; i < Constants.COLUMNS; i++) {
                board += String.valueOf(Constants.ROWS - i) + " | ";
                for (int j = 0; j < Constants.ROWS; j++) {
                    board += (String.valueOf(boardChar[j][i]) + " | ");
                }         //r | n | b | q | k | b | n | r |
                board += String.valueOf(Constants.ROWS - i) + "\n  +---+---+---+---+---+---+---+---+\n";
            }
            board += "    a   b   c   d   e   f   g   h\n";
            return board;
        }
    }
    
    public static char[][] loadBoard(String fen){
        int file = 0;
        int rank = 0;
        char[][] board = new char[Constants.COLUMNS][Constants.ROWS];
        for(char c:fen.split(" ")[0].toCharArray()){
            if(c == '/'){
                rank++;
                file = 0;
            }else if(Character.isDigit(c)){
                file += Util.getNumericValue(c);
            }else{
                board[file][rank] = c;
                file++;
            }
        }
        refactorBoard(board);
        return board;
    }

    public static void refactorBoard(char[][] board){
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                boolean isPiece = false;
                for(char piece:Constants.WHITE_PIECE_CHAR.toCharArray()){
                    if(toUpper(board[i][j]) == piece){
                        isPiece = true;
                        break;
                    }
                }
                if(!isPiece){
                    board[i][j] = Constants.EMPTY_CHAR;
                }
            }
        }
    }
    
    public static char[][] copyBoard(char[][] boardChars){
        char[][] newBoard = new char[Constants.COLUMNS][Constants.ROWS];
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                newBoard[i][j] = boardChars[i][j];
            }
        }
        return newBoard;
    }

    public static boolean FENValidator(String fen){
        String[] fenParts = fen.split(" ");
        if(fenParts.length != 6){
            return false;
        }
        if(!fenParts[0].contains(String.valueOf(Constants.WHITE_KING))||!fenParts[0].contains(String.valueOf(Constants.BLACK_KING))){
            return false;
        }
        //rnb1k1nr/pppp1ppp/4p3/8/1b4q1/2N1PN2/PPPP2PP/R1BQKB1R w KQkq - 5 7
        String[] rows = fenParts[0].split("/");
        if(rows.length != 8){
            return false;
        }
        for(String row:rows){
            if(row.length() > 8){
                return false;
            }
            int count = 0;
            for(char c:row.toCharArray()){
                if(Character.isDigit(c)){
                    count+=getNumericValue(c);
                }else{
                    count++;
                }
            }
            if(count != 8){
                return false;
            }
        }
        if(fenParts[1].length() != 1){
            return false;
        }
        if(fenParts[2].length() > 4){
            return false;
        }
        if(fenParts[3].length() > 2){
            return false;
        }
        try{
            Integer.parseInt(fenParts[4]);
            Integer.parseInt(fenParts[5]);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }

}
