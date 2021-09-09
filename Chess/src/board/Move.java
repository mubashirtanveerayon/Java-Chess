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
            boolean check = Util.isKingInCheck(board,white);
            if(check){
                toRemove.add(i);
            }
            board.boardChars[i[0]][i[1]] = prev;
            board.boardChars[position[0]][position[1]] = pieceChar;
        }
        pseudoLegalMoves.removeAll(toRemove);
        if (Util.toUpper(pieceChar) == Constants.WHITE_KING) {
            boolean[] castling = Util.generateCastlingMove(board, white);
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

    //index 0 returns if qSide castling is possible
    
}
