/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package board;

import java.util.ArrayList;
import piece.Piece;
import util.Constants;
import util.GameParameter;
import util.Util;

/**
 *
 * @author ayon2
 */
public class Move {

    Board board;
    String fen;
    

    public Move(Board board, boolean toMove, String fen) {
        this.board = board;
        this.fen = fen;
        GameParameter.whiteToMove = toMove;
        GameParameter.history.add(this.fen);
        GameParameter.halfMove = Integer.parseInt(GameParameter.history.get(GameParameter.history.size()-1).split(" ")[4]);
        GameParameter.fullMove = Integer.parseInt(GameParameter.history.get(GameParameter.history.size()-1).split(" ")[5]);
    }

    public boolean[] generateCastlingMove(boolean white) {
        boolean queenSideCastling, kingSideCastling;
        String cFen = GameParameter.history.get(GameParameter.history.size() - 1).split(" ")[2];
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
                        int[][] dir = Util.getDirection(dx, dy,board.boardChars[i][j]);
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
                int limit = !((Util.isUpperCase(pieceChar) && position[1] == 6) || (!Util.isUpperCase(pieceChar) && position[1] == 1)) && Util.sameoffset(defoffset, direction) ?  1 : 2;
                for (int i = 0; i < defoffset.length; i++) {
                    int file = position[0];
                    int rank = position[1];
                    file += direction[i][0];
                    rank += direction[i][1];
                    if (Util.isValid(file, rank)) {
                        if (i > 1) {
                            int n = 0;
                            while(n<limit && Util.isValid(file, rank)){
                                if(board.boardChars[file][rank] == Constants.EMPTY_CHAR){
                                    pseudoLegalMoves.add(new int[]{file, rank});
                                }else{
                                    break;
                                }
                                file += direction[i][0];
                                rank += direction[i][1];
                                n++;
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
        
        String enPassantTile = Util.loadFenFromBoard(board).split(" ")[3];
        if(!enPassantTile.equals("-")){
            if(Util.toUpper(pieceChar) == Constants.WHITE_PAWN){
                if(white){
                    if(position[1] == 3){
                        int fDiff = Math.abs(Constants.FILES.indexOf(enPassantTile.charAt(0))-position[0]);
                        if(fDiff == 1){
                            pseudoLegalMoves.add(new int[]{Constants.FILES.indexOf(enPassantTile.charAt(0)),Constants.RANKS.indexOf(enPassantTile.charAt(1))});
                        }
                    }
                }else{
                    if(position[1] == 4){
                        int fDiff = Math.abs(Constants.FILES.indexOf(enPassantTile.charAt(0))-position[0]);
                        if(fDiff == 1){
                            pseudoLegalMoves.add(new int[]{Constants.FILES.indexOf(enPassantTile.charAt(0)),Constants.RANKS.indexOf(enPassantTile.charAt(1))});
                        }
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
        if (!((mPiece.white && GameParameter.whiteToMove) || (!mPiece.white && !GameParameter.whiteToMove))) {
            return false;
        }
        if (mTile.isOccupied() && Util.toUpper(mTile.piece.pieceChar) == Constants.WHITE_KING) {
            return false;
        }
        StringBuilder move = new StringBuilder();
        move.append(Constants.FILES.charAt(mPiece.position[0]));
        move.append(Constants.RANKS.charAt(mPiece.position[1]));
        ArrayList<int[]> legalMoves = generateMove(mPiece.pieceChar, mPiece.position, Util.getOffset(mPiece.pieceChar), false);
        int size = legalMoves.size();
        for (int i = 0; i < size; i++) {
            int[] pos = legalMoves.get(i);
            if (Util.samePosition(pos, mTile.position)) {
                fen = Util.loadFenFromBoard(board);
                GameParameter.history.add(fen);
                if (Util.toUpper(mPiece.pieceChar) == Constants.WHITE_KING) {
                    int diff = Math.abs(mPiece.position[0] - mTile.position[0]);
                    if (diff == 2) {
                        castle(mTile);
                    }
                }else if(Util.toUpper(mPiece.pieceChar) == Constants.WHITE_PAWN){
                    String enPassantTile = Util.loadFenFromBoard(board).split(" ")[3];
                    String mTilePosition = String.valueOf(Constants.FILES.charAt(mTile.position[0]))+String.valueOf(Constants.RANKS.charAt(mTile.position[1]));
                    if(enPassantTile.equals(mTilePosition)){
                        enPassant();
                    }
                }
                Tile pTile = board.getTile(mPiece.position);
                pTile.setIcon(null);
                pTile.piece.position = null;
                pTile.piece = null;
                GameParameter.halfMove++;
                if(mTile.isOccupied() || Util.toUpper(mPiece.pieceChar) == Constants.WHITE_PAWN){
                    GameParameter.halfMove = 0;
                }
                mTile.piece = mPiece;
                mPiece.position = mTile.position;
                mPiece.moved = true;
                move.append(Constants.FILES.charAt(mPiece.position[0]));
                move.append(Constants.RANKS.charAt(mPiece.position[1]));
                GameParameter.moves.add(move.toString());
                board.refactorBoard();
                if(!GameParameter.whiteToMove){
                    GameParameter.fullMove++;
                }
                GameParameter.whiteToMove = !GameParameter.whiteToMove;
                return true;
            }
        }
        return false;
    }

    public boolean move(Tile pTile, Tile mTile) {
        if (pTile.isOccupied()) {
            return move(mTile, pTile.piece);
        }
        return false;
    }

    public void undoMove() {
        board.boardTiles = Util.loadBoardFromFen(fen).boardTiles;
        board.refactorBoard();
    }
    
    public void castle(Tile mTile){
        int file = mTile.position[0];
        int rank = mTile.position[1];
        if(file == 2){
            board.boardTiles[3][rank].piece = board.boardTiles[0][rank].piece;
            board.boardTiles[3][rank].piece.position = board.boardTiles[3][rank].position;
            board.boardTiles[0][rank].piece = null;
        }else if(file == 6){
            board.boardTiles[5][rank].piece = board.boardTiles[7][rank].piece;
            board.boardTiles[5][rank].piece.position = board.boardTiles[5][rank].position;
            board.boardTiles[7][rank].piece = null;
        }
    }
    
    public void enPassant(){
        int[] enPassantTile = Util.cvtPosition(fen.split(" ")[3]);
        if(Util.isValid(enPassantTile)){
            int file = enPassantTile[0];
            int rank = enPassantTile[1];
            if(rank == 2){
                board.boardTiles[file][rank+1].piece = null;
                GameParameter.halfMove = 0;
            }else if(rank == 5){
                board.boardTiles[file][rank-1].piece = null;
                GameParameter.halfMove = 0;
            }
        }
    }
    
}
