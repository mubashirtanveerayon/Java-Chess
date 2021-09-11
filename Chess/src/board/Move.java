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

    public boolean[] generateCastlingMove(boolean white) {
        boolean queenSideCastling, kingSideCastling;
        String cFen = Move.history.get(Move.history.size() - 1).split(" ")[2];
        char king = white ? Constants.WHITE_KING : Constants.BLACK_KING;
        char queen = white ? Constants.WHITE_QUEEN : Constants.BLACK_QUEEN;
        kingSideCastling = cFen.contains(String.valueOf(king));
        queenSideCastling = cFen.contains(String.valueOf(queen));
        boolean isKingInCheck = isKingInCheck(white);
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
                        boolean check = isKingInCheck(white);
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
                        boolean check = isKingInCheck(white);
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
    
    public boolean isKingInCheck(boolean white) {
        int[] targetPos = board.getKingPosition(white);
        char king = white ? Constants.WHITE_KING : Constants.BLACK_KING;
        for (int i = 0; i < Constants.NUM_OF_COLUMNS; i++) {
            for (int j = 0; j < Constants.NUM_OF_ROWS; j++) {
                if (board.boardChars[i][j] != Constants.EMPTY_CHAR) {
                    if (!Util.isAlly(king, board.boardChars[i][j])) {
                        int dx = targetPos[0] - i;
                        int dy = targetPos[1] - j;
                        int[][] dir = Util.getDirection(dx, dy, Util.getOffset(board.boardChars[i][j]), board.boardChars[i][j]);
                        ArrayList<int[]> captures = generateCaptureMove(board.boardChars[i][j], new int[]{i, j}, dir);
                        for (int[] capture : captures) {
                            if (Util.samePosition(capture, targetPos)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<int[]> generateCaptureMove(char pieceChar, int[] position, int[][] direction) {
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
                            if (!Util.isAlly(pieceChar, board.boardChars[file][rank])) {
                                captureMoves.add(new int[]{file, rank});
                            }
                        }
                    }
                }
                break;
            }
            case Constants.WHITE_PAWN: {
                for (int i = 0; i < direction.length; i++) {
                    int file = position[0];
                    int rank = position[1];
                    file += direction[i][0];
                    rank += direction[i][1];
                    if (Util.isValid(file, rank)) {
                        if (board.boardChars[file][rank] != Constants.EMPTY_CHAR && !Util.isAlly(pieceChar, board.boardChars[file][rank])) {
                            captureMoves.add(new int[]{file, rank});
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
                            if (!Util.isAlly(pieceChar, board.boardChars[file][rank])) {
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
                            if (!Util.isAlly(pieceChar, board.boardChars[file][rank])) {
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

    public ArrayList<int[]> generateCaptureMove(char pieceChar, int[] position) {
        return generateCaptureMove(pieceChar, position, Util.getOffset(pieceChar));
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
                            if (!Util.isAlly(pieceChar, board.boardChars[file][rank])) {
                                pseudoLegalMoves.add(new int[]{file, rank});
                            }
                        }
                    }
                }
                break;
            }
            case Constants.WHITE_PAWN: {
                int[][] defoffset = Util.getOffset(pieceChar);
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
                            if (board.boardChars[file][rank] != Constants.EMPTY_CHAR && !Util.isAlly(pieceChar, board.boardChars[file][rank])) {
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
                        } else if (!Util.isAlly(pieceChar, board.boardChars[file][rank])) {
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
                            if (!Util.isAlly(pieceChar, board.boardChars[file][rank])) {
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
            boolean check = isKingInCheck(white);
            if (check) {
                toRemove.add(i);
            }
            board.boardChars[i[0]][i[1]] = prev;
            board.boardChars[position[0]][position[1]] = pieceChar;
        }
        pseudoLegalMoves.removeAll(toRemove);
        if (Util.toUpper(pieceChar) == Constants.WHITE_KING) {
            boolean[] castling = generateCastlingMove(white);
            int rank = white ? 7 : 0;
            if (castling[0]) {
                pseudoLegalMoves.add(new int[]{2, rank});
            }
            if (castling[1]) {
                pseudoLegalMoves.add(new int[]{6, rank});
            }
        }
        System.out.println((System.nanoTime() - start) / 1000000.00 + " ms");
        return pseudoLegalMoves;
    }

    public ArrayList<int[]> generateMove(char pieceChar, int[] position, boolean pseudoLegal) {
        return generateMove(pieceChar, position, Util.getOffset(pieceChar), pseudoLegal);
    }

    public boolean move(Tile mTile, Piece mPiece) {
        if (!((mPiece.white && whiteToMove) || (!mPiece.white && !whiteToMove))) {
            return false;
        }
        if (mTile.isOccupied() && Util.toUpper(mTile.piece.pieceChar) == Constants.WHITE_KING) {
            return false;
        }
        ArrayList<int[]> legalMoves = generateMove(mPiece.pieceChar, mPiece.position, Util.getOffset(mPiece.pieceChar), false);
        int size = legalMoves.size();
        for (int i = 0; i < size; i++) {
            int[] pos = legalMoves.get(i);
            if (Util.samePosition(pos, mTile.position)) {
                fen = Util.loadFenFromBoard(board);
                history.add(fen);
                if (Util.toUpper(mPiece.pieceChar) == Constants.WHITE_KING) {
                    int diff = Math.abs(mPiece.position[0] - mTile.position[0]);
                    if (diff == 2) {
                        int toFile = mTile.position[0];
                        int rank = mPiece.position[1];
                        if (toFile == 2) {
                            board.boardTiles[3][rank].piece = board.boardTiles[0][rank].piece;
                            board.boardTiles[3][rank].piece.position = board.boardTiles[3][rank].position;
                            board.boardTiles[0][rank].piece = null;
                        } else if (toFile == 6) {
                            board.boardTiles[5][rank].piece = board.boardTiles[7][rank].piece;
                            board.boardTiles[5][rank].piece.position = board.boardTiles[5][rank].position;
                            board.boardTiles[7][rank].piece = null;
                        }
                    }
                }
                Tile pTile = board.getTile(mPiece.position);
                pTile.setIcon(null);
                pTile.piece.position = null;
                pTile.piece = null;
                mTile.piece = mPiece;
                mPiece.position = mTile.position;
                mPiece.moved = true;
                board.refactorBoard();
                whiteToMove = !whiteToMove;
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

}
