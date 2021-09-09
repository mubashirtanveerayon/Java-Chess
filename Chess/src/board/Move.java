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
    public static boolean whiteToMove;
    public static ArrayList<String> history = new ArrayList<>();

    public Move(Board board, boolean toMove, String fen) {
        this.board = board;
        this.fen = fen;
        whiteToMove = toMove;
        history.add(this.fen);
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
                if (white && !Util.isUpperCase(board.boardChars[i][j])) {
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
                } else if (!white && Util.isUpperCase(board.boardChars[i][j])) {
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
            }
        }
        return false;
    }

    public ArrayList<int[]> generateMove(char pieceChar, int[] position, int[][] direction, boolean pseudoLegal) {
        long start = System.nanoTime();
        boolean white = Util.isUpperCase(pieceChar);
        ArrayList<int[]> pseudoLegalMoves = new ArrayList<>();
        switch (Util.toUpper(pieceChar)) {
            case Constants.WHITE_KNIGHT: {
                for (int[] dir : direction) {
                    int file = position[0];
                    int rank = position[1];
                    file += dir[0];
                    rank += dir[1];
                    if (Util.isValid(file, rank)) {
                        if (board.boardChars[file][rank] == Constants.EMPTY_CHAR) {
                            pseudoLegalMoves.add(new int[]{file, rank});
                        } else {
                            if (!isAlly(pieceChar, board.boardChars[file][rank])) {
                                pseudoLegalMoves.add(new int[]{file, rank});
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
                        if (i > 1) {
                            if (board.boardChars[file][rank] == Constants.EMPTY_CHAR) {
                                pseudoLegalMoves.add(new int[]{file, rank});
                            }
                        } else {
                            if (board.boardChars[file][rank] != Constants.EMPTY_CHAR && !isAlly(pieceChar, board.boardChars[file][rank])) {
                                pseudoLegalMoves.add(new int[]{file, rank});
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
                        if (board.boardChars[file][rank] == Constants.EMPTY_CHAR) {
                            pseudoLegalMoves.add(new int[]{file, rank});
                        } else if (!isAlly(pieceChar, board.boardChars[file][rank])) {
                            pseudoLegalMoves.add(new int[]{file, rank});
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
                        if (board.boardChars[file][rank] == Constants.EMPTY_CHAR) {
                            pseudoLegalMoves.add(new int[]{file, rank});
                        } else {
                            if (!isAlly(pieceChar, board.boardChars[file][rank])) {
                                pseudoLegalMoves.add(new int[]{file, rank});
                            }
                            break;
                        }
                        file += dir[0];
                        rank += dir[1];
                    }
                }
            }
        }
        if (pseudoLegal) {
            return pseudoLegalMoves;
        }
        board.refactorBoard();
        ArrayList<int[]> toRemove = new ArrayList<>();
        for (int[] i : pseudoLegalMoves) {
            char prev = board.boardChars[i[0]][i[1]];
            board.boardChars[position[0]][position[1]] = Constants.EMPTY_CHAR;
            board.boardChars[i[0]][i[1]] = pieceChar;
            for (int f = 0; f < Constants.NUM_OF_COLUMNS; f++) {
                for (int r = 0; r < Constants.NUM_OF_ROWS; r++) {
                    if (board.boardChars[f][r] != Constants.EMPTY_CHAR) {
                        if (white) {
                            if (!Util.isUpperCase(board.boardChars[f][r])) {
                                int[] target = board.getKingPosition(Util.isUpperCase(pieceChar));
                                int dx = target[0] - f;
                                int dy = target[1] - r;
                                ArrayList<int[]> pseudo = generateCaptureMove(board.boardChars[f][r], new int[]{f, r}, Util.copyArrayList(Util.getDirection(dx, dy, getOffset(board.boardChars[f][r]), board.boardChars[f][r])), board);
                                for (int[] attack : pseudo) {
                                    if (Util.samePosition(target, attack)) {
                                        toRemove.add(i);
                                        break;
                                    }
                                }
                            }
                        } else {
                            if (Util.isUpperCase(board.boardChars[f][r])) {
                                int[] target = board.getKingPosition(Util.isUpperCase(pieceChar));
                                int dx = target[0] - f;
                                int dy = target[1] - r;
                                ArrayList<int[]> pseudo = generateCaptureMove(board.boardChars[f][r], new int[]{f, r}, Util.copyArrayList(Util.getDirection(dx, dy, getOffset(board.boardChars[f][r]), board.boardChars[f][r])), board);
                                for (int[] attack : pseudo) {
                                    if (Util.samePosition(target, attack)) {
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
        if(Util.toUpper(pieceChar)==Constants.WHITE_KING){
            boolean[] castling = generateCastlingMove(board,white);
            int rank = white?7:0;
            if(castling[0]){
                pseudoLegalMoves.add(new  int[]{2,rank});
            }
            if(castling[1]){
                pseudoLegalMoves.add(new  int[]{6,rank});
            }
        }
        System.out.println((System.nanoTime() - start) / 1000000.00 + " ms");
        return pseudoLegalMoves;
    }

    public ArrayList<int[]> generateMove(char pieceChar, int[] position, boolean pseudoLegal) {
        return generateMove(pieceChar, position, getOffset(pieceChar), pseudoLegal);
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

    public static boolean isAlly(char ch1, char ch2) {
        return (Util.isUpperCase(ch2) && Util.isUpperCase(ch1)) || (!Util.isUpperCase(ch1) && !Util.isUpperCase(ch2));
    }

    public boolean move(Tile mTile, Piece mPiece) {
        if (!((mPiece.white && whiteToMove) || (!mPiece.white && !whiteToMove))) {
            return false;
        }
        if (mTile.isOccupied() && Util.toUpper(mTile.piece.pieceChar) == Constants.WHITE_KING) {
            return false;
        }
        ArrayList<int[]> legalMoves = generateMove(mPiece.pieceChar, mPiece.position, getOffset(mPiece.pieceChar), false);
        for (int[] pos : legalMoves) {
            if (Util.samePosition(pos, mTile.position)) {
                fen = Util.loadFenFromBoard(board);
                history.add(fen);
                System.out.println(fen);
                Tile pTile = board.getTile(mPiece.position);
                pTile.setIcon(null);
                pTile.piece.position = null;
                pTile.piece = null;
                mTile.piece = mPiece;
                mPiece.position = mTile.position;
                mPiece.moved = true;
                whiteToMove = !whiteToMove;
                board.refactorBoard();
                return true;
            }
        }
        return false;
    }

    public boolean move(Tile mTile, Tile pTile) {
        if (pTile.isOccupied()) {
            return move(mTile, pTile.piece);
        }
        return false;
    }

    public void undoMove() {
        board.boardTiles = Util.loadBoardFromFen(fen).boardTiles;
        board.refactorBoard();
    }

    //index 0 returns if qSide castling is possible
    public static boolean[] generateCastlingMove(Board board, boolean white) {
        boolean queenSideCastling , kingSideCastling;
        String cFen = history.get(history.size()-1).split(" ")[2];
        char king = white ? Constants.WHITE_KING : Constants.BLACK_KING;
        char queen = white?Constants.WHITE_QUEEN:Constants.BLACK_QUEEN;
        int k,length = cFen.length();
        for(k=0;k<length;k++){
            if(cFen.charAt(k)==king){
                break;
            }
        }
        kingSideCastling = k!=length-1;
        for(k=0;k<length;k++){
            if(cFen.charAt(k)==queen){ 
                break;
            }
        }
        queenSideCastling = k!=length-1;
        boolean queenSideCastlingPossible = queenSideCastling&&((white && cFen.contains(String.valueOf(Constants.WHITE_QUEEN))) || (!white && cFen.contains(String.valueOf(Constants.BLACK_QUEEN))));
        boolean kingSideCastlingPossible = kingSideCastling&&((white && cFen.contains(String.valueOf(Constants.WHITE_KING))) || (!white && cFen.contains(String.valueOf(Constants.BLACK_KING))));
        boolean isKingInCheck = isKingInCheck(board, white);
        if (isKingInCheck) {
            queenSideCastling = false;
            kingSideCastling = false;
        } else {
            int rank = white ? 7 : 0;
            int[] kPos = new int[]{4, rank};
            if (queenSideCastlingPossible) {
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
            if (kingSideCastlingPossible) {
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
}
