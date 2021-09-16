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
        if (GameParameter.whiteToMove) {
            sb.append(Constants.WHITE);
        } else {
            sb.append(Constants.BLACK);
        }

        sb.append(" ");
        sb.append(getCastlingRights(board));
        sb.append(" ");
        sb.append(getEnPassantSquare(board));
        
        sb.append(" ");
        sb.append(GameParameter.halfMove);
        sb.append(" ");
        sb.append(GameParameter.fullMove);
        
        return sb.toString();
    }

    public static String getCastlingRights(Board board) {
        StringBuilder sb = new StringBuilder();
        String lastCastlingFen = GameParameter.history.get(GameParameter.history.size() - 1).split(" ")[2];
        boolean castlingPossible = !lastCastlingFen.equals("-");
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
        try {
            if (!castlingPossible || sb.length() == 0 || sb.charAt(sb.length() - 1) == ' ') {
                sb.append("-");
            }
        } catch (Exception ex) {
            sb.append("-");
        }
        return sb.toString();
    }

    public static String getEnPassantSquare(Board board) {
        StringBuilder sb = new StringBuilder();
        if (GameParameter.moves.isEmpty()) {
            return GameParameter.history.get(GameParameter.history.size() - 1).split(" ")[3];
        } else {
            String lastMoveStr = GameParameter.moves.get(GameParameter.moves.size() - 1);

            int[][] lastMove = parseMove(lastMoveStr);
            
            int[] initPosition = lastMove[0];
            int[] lastPosition = lastMove[1];

            if (Util.toUpper(board.boardChars[lastPosition[0]][lastPosition[1]]) == Constants.WHITE_PAWN) {
                int rDiff = Math.abs(lastPosition[1] - initPosition[1]);
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
        return file >= 0 && rank >= 0 && file < Constants.NUM_OF_COLUMNS && rank < Constants.NUM_OF_ROWS;
    }

    public static boolean isValid(int[] position) {
        return isValid(position[0], position[1]);
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

    public static int[] cvtPosition(String strPos) {
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
    
    public static int[][] parseMove(String bestMove){
        int[] initPosition = cvtPosition(new StringBuffer(bestMove).delete(2, 4).toString());
        int[] finalPosition = cvtPosition(new StringBuffer(bestMove).delete(0, 2).toString());
        return new int[][]{initPosition,finalPosition};
    }
}
