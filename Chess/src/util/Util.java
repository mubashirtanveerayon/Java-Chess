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

    public static int getNumericValue(char c) {
        int[] values = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int i = 0; i < values.length; i++) {
            if (String.valueOf(c).equals(String.valueOf(values[i]))) {
                return values[i];
            }
        }
        return -1;
    }

    public static boolean isUpperCase(String text) {
        return text.equals(text.toUpperCase());
    }

    public static boolean isUpperCase(char t) {
        return t == toUpper(t);
    }

    public static char toUpper(char ch) {
        return String.valueOf(ch).toUpperCase().charAt(0);
    }

    public static int[] copyPosition(int[] position) {
        return new int[]{position[0], position[1]};
    }

    public static boolean isOfSameDir(int a, int b) {
        return (a < 0 && b < 0) || (a > 0 && b > 0) || (a == 0 && b == 0);
    }

    public static boolean samePosition(int[] a, int[] b) {
        return a[0] == b[0] && a[1] == b[1];
    }

    public static boolean sameoffset(int[][] dir1, int[][] dir2) {
        if (dir1.length != dir2.length) {
            return false;
        }
        for (int i = 0; i < dir1.length; i++) {
            if (!samePosition(dir1[i], dir2[i])) {
                return false;
            }
        }
        return true;
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

    public static String loadFenFromBoard(Board board) {
        StringBuilder sb = new StringBuilder();
        Tile[][] boardTiles = board.boardTiles;
        for (int i = 0; i < Constants.NUM_OF_ROWS; i++) {
            int gap = 0;
            for (int j = 0; j < Constants.NUM_OF_ROWS; j++) {
                if (boardTiles[j][i].isOccupied()) {
                    if (gap != 0) {
                        sb.append(gap);
                    }
                    sb.append(boardTiles[j][i].getPieceChar());
                    gap = 0;
                } else {
                    gap++;
                }
            }
            if (gap != 0) {
                sb.append(gap);
            }
            if (i != Constants.NUM_OF_COLUMNS - 1) {
                sb.append('/');
            }
        }
        sb.append(" ");
        if (Move.whiteToMove) {
            sb.append(Constants.WHITE);
        } else {
            sb.append(Constants.BLACK);
        }

        String lastCastlingFen = Move.history.get(Move.history.size() - 1).split(" ")[2];
        boolean castlingPossible = !lastCastlingFen.equals("-");
        sb.append(" ");
        if (castlingPossible) {

            boolean wKingInPosition = board.boardChars[4][7] == Constants.WHITE_KING;
            boolean bKingInPosition = board.boardChars[4][0] == Constants.BLACK_KING;
            if (wKingInPosition) {
                boolean wKRookInPosition = board.boardChars[7][7] == Constants.WHITE_ROOK;
                boolean wQRookInPosition = board.boardChars[0][7] == Constants.WHITE_ROOK;
                if (wKRookInPosition) {
                    if (lastCastlingFen.contains(String.valueOf(Constants.WHITE_KING))) {
                        sb.append(Constants.WHITE_KING);
                    }
                }
                if (wQRookInPosition) {
                    if (lastCastlingFen.contains(String.valueOf(Constants.WHITE_QUEEN))) {
                        sb.append(Constants.WHITE_QUEEN);
                    }
                }
            }
            if (bKingInPosition) {
                boolean bKRookInPosition = board.boardChars[7][0] == Constants.BLACK_ROOK;
                boolean bQRookInPosition = board.boardChars[0][0] == Constants.BLACK_ROOK;
                if (bKRookInPosition) {
                    if (lastCastlingFen.contains(String.valueOf(Constants.BLACK_KING))) {
                        sb.append(Constants.BLACK_KING);
                    }
                }
                if (bQRookInPosition) {
                    if (lastCastlingFen.contains(String.valueOf(Constants.BLACK_QUEEN))) {
                        sb.append(Constants.BLACK_QUEEN);
                    }
                }
            }
        }

        if (!castlingPossible || sb.charAt(sb.length() - 1) == ' ') {
            sb.append("-");
        }

        sb.append(" ");

        if (Move.moves.isEmpty()) {
            sb.append("-");
        } else {
            String lastMove = Move.moves.get(Move.moves.size() - 1);

            int[] initPosition = new int[]{Constants.FILES.indexOf(lastMove.charAt(0)),Constants.RANKS.indexOf(lastMove.charAt(1))};
            int[] lastPosition = new int[]{Constants.FILES.indexOf(lastMove.charAt(2)), Constants.RANKS.indexOf(lastMove.charAt(3))};

            if (Util.toUpper(board.boardChars[lastPosition[0]][lastPosition[1]]) == Constants.WHITE_PAWN) {
                int rDiff = Math.abs(lastPosition[1] - initPosition[1]);
                System.out.println(rDiff);
                if (rDiff == 2) {
                    sb.append(Constants.FILES.charAt(lastPosition[0]));
                    if (Util.isUpperCase(board.boardChars[lastPosition[0]][lastPosition[1]])) {
                        sb.append(Constants.RANKS.charAt(lastPosition[1] + 1));
                    } else {
                        sb.append(Constants.RANKS.charAt(lastPosition[1] - 1));
                    }
                } else {
                    sb.append("-");
                }
            } else {
                sb.append("-");
            }
        }

//
//        String cFen = Move.history.get(Move.history.size() - 1).split(" ")[2];
//        boolean castlingPossible = !cFen.equals("-");
//        sb.append(" ");
//        if (castlingPossible) {
//            
//            //index 0 = qSide, 1 = king itself
//            int[] bScore = new int[3],wScore = new int[3];
//            int length = Move.history.size() - 1;
//            for (int i = 0; i < length; i++) {
//                String pFen = Move.history.get(i).split(" ")[0];
//                char[][] pBoardChars = new char[Constants.NUM_OF_COLUMNS][Constants.NUM_OF_ROWS];
//                int file = 0;
//                int rank = 0;
//                for(int j=0;j<pFen.length();j++){
//                    char c = pFen.charAt(j);
//                    if(c == '/'){
//                        file = 0;
//                        rank ++;
//                    }else if(Character.isDigit(c)){
//                        file += getNumericValue(c);
//                    }else{
//                        pBoardChars[file][rank] = c;
//                    }
//                    file ++;
//                }
//                boolean wKingMoved = (pBoardChars[4][7] != Constants.WHITE_KING);
//                boolean bKingMoved = (pBoardChars[4][0] != Constants.BLACK_KING);
//                boolean wQRookMoved = (pBoardChars[0][7] != Constants.WHITE_ROOK);
//                boolean wKRookMoved = (pBoardChars[7][7] != Constants.WHITE_ROOK);
//                boolean bQRookMoved = (pBoardChars[0][0] != Constants.BLACK_ROOK);
//                boolean bKRookMoved = (pBoardChars[7][0] != Constants.BLACK_ROOK);
//                
//                if(!wKingMoved){
//                    if(!wKRookMoved){
//                        wScore[2] ++;
//                    }
//                    if(!wQRookMoved){
//                        wScore[0] ++;
//                    }
//                    wScore[1] ++;
//                }
//                if(!bKingMoved){
//                    if(!bKRookMoved){
//                        bScore[2] ++;
//                    }
//                    if(!bQRookMoved){
//                        bScore[0] ++;
//                    }
//                    bScore[1] ++;
//                }
//                
//            }
//
//            boolean wKingInPosition = board.boardChars[4][7] == Constants.WHITE_KING;
//            boolean bKingInPosition = board.boardChars[4][0] == Constants.BLACK_KING;
//            boolean wQRookInPosition = board.boardChars[0][7] == Constants.WHITE_ROOK && cFen.contains(String.valueOf(Constants.WHITE_QUEEN));
//            boolean wKRookInPosition = board.boardChars[7][7] == Constants.WHITE_ROOK && cFen.contains(String.valueOf(Constants.WHITE_KING));
//            boolean bQRookInPosition = board.boardChars[0][0] == Constants.BLACK_ROOK && cFen.contains(String.valueOf(Constants.BLACK_QUEEN));
//            boolean bKRookInPosition = board.boardChars[7][0] == Constants.BLACK_ROOK && cFen.contains(String.valueOf(Constants.BLACK_KING));
//            boolean wKingMoved = wScore[1] != length-1;
//            boolean bKingMoved = bScore[1] != length-1;
//            boolean wQRookMoved = wScore[0] != length-1;
//            boolean wKRookMoved = wScore[2] != length-1;
//            boolean bQRookMoved = bScore[0] != length-1;
//            boolean bKRookMoved = bScore[2] != length-1;
//
//            if (wKingInPosition && wKRookInPosition && !wKingMoved && !wKRookMoved) {
//                sb.append(Constants.WHITE_KING);
//            }
//            if (wKingInPosition && wQRookInPosition && !wKingMoved && !wQRookMoved) {
//                sb.append(Constants.WHITE_QUEEN);
//            }
//
//            if (bKingInPosition && bKRookInPosition && !bKingMoved && !bKRookMoved) {
//                sb.append(Constants.BLACK_KING);
//            }
//            if (bKingInPosition && bQRookInPosition && !bKingMoved && !bQRookMoved) {
//                sb.append(Constants.BLACK_QUEEN);
//            }
//
//        }
//
//        if (!castlingPossible || sb.charAt(sb.length() - 1) == ' ') {
//            sb.append("-");
//        }
        return sb.toString();
    }

    public static Board loadBoardFromFen(String Fen) {
        String fen = Fen.split(" ")[0];
        Tile[][] boardTiles = new Tile[Constants.NUM_OF_COLUMNS][Constants.NUM_OF_ROWS];
        for (int i = 0; i < Constants.NUM_OF_COLUMNS; i++) {
            for (int j = 0; j < Constants.NUM_OF_ROWS; j++) {
                boardTiles[i][j] = new Tile(i, j);
            }
        }
        int file = 0;
        int rank = 0;
        for (int i = 0; i < fen.length(); i++) {
            char c = fen.charAt(i);
            if (c == '/') {
                file = 0;
                rank++;
            } else if (Character.isDigit(c)) {
                file += getNumericValue(c);
            } else {
                Piece piece = new Piece(c, new int[]{file, rank});
                boardTiles[file][rank].piece = piece;
                file++;
            }
        }
        Board board = new Board(boardTiles);
        return board;
    }

    public static int[][] getDirection(int dx, int dy, int[][] offset, char pieceChar) {
        ArrayList<int[]> path = new ArrayList<>();
        if (toUpper(pieceChar) == Constants.WHITE_PAWN) {
            for (int i = 0; i < offset.length - 2; i++) {
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
        return file >= 0 && rank >= 0 && file < Constants.NUM_OF_COLUMNS && rank < Constants.NUM_OF_ROWS;
    }

    public static ArrayList<int[]> copy(ArrayList<int[]> from) {
        ArrayList<int[]> newList = new ArrayList<>();
        for (int i = 0; i < from.size(); i++) {
            newList.add(from.get(i));
        }
        return newList;
    }

    public static int[][] copyArrayList(ArrayList<int[]> from) {
        int[][] nList = new int[from.size()][];
        for (int i = 0; i < from.size(); i++) {
            nList[i] = from.get(i);
        }
        return nList;
    }
}
