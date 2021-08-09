package chess;

import java.awt.Color;

public class Board {

    Tile[][] tile;
    char[][] board;

    boolean checkForWhite;
    boolean checkForBlack;
    boolean checkResolved;

    public Board() {
        checkForWhite = false;
        checkForBlack = false;
        checkResolved = false;
        tile = new Tile[Constants.NUM_OF_COLUMNS][Constants.NUM_OF_ROWS];
        board = new char[Constants.NUM_OF_COLUMNS][Constants.NUM_OF_ROWS];
        for (int i = 0; i < Constants.NUM_OF_COLUMNS; i++) {
            for (int j = 0; j < Constants.NUM_OF_ROWS; j++) {
                tile[i][j] = new Tile(i, j);
                board[i][j] = ' ';
            }
        }
        loadPositionFromFen(Constants.STARTING_FEN);
    }

    public void loadPositionFromFen(String fen) {
        int file = 0;
        int rank = 0;
        for (int k = 0; k < fen.length(); k++) {
            char c = fen.charAt(k);
            if (c == '/') {
                file = 0;
                rank++;
            } else {
                if (Character.isDigit(c)) {
                    int d = Util.getNumericValue(c);
                    file += d;
                } else {
                    for (String piece : Constants.PIECE_NAMES) {
                        if (c == piece.charAt(0)) {
                            boolean isBlack = Util.isLower(c);
                            Piece p = new Piece(piece.toLowerCase(), isBlack, file, rank);
                            getTile(file, rank).place(p);
                            board[file][rank] = p.getPieceChar();
                            file++;
                        }
                    }
                }
            }
        }
    }

    public void calculateCheck(boolean forBlackToMove) {
        checkForWhite = false;
        checkForBlack = false;
        checkResolved = true;
        Color toCheck = forBlackToMove ? Constants.WHITE_PIECE_COLOR :Constants.BLACK_PIECE_COLOR;
        Tile targetKingTile = forBlackToMove ? getTile('k') : getTile('K');
        for (Tile[] col : tile) {
            for (Tile row : col) {
                Piece piece = row.getPiece();
                if (piece != null && !Util.isKing(piece) && piece.getColor() == toCheck) {
//                    piece.generateLegalMoves(this);
//                    ArrayList<Tile> legalMoves = piece.getLegalMoves();
//                    for (Tile t : legalMoves) {
//                        if (Util.isOfSamePosition(t, targetKingTile)) {
//                            if (!forBlackToMove) {
//                                checkForWhite = true;
//                            } else {
//                                checkForBlack = true;
//                            }
//                            checkResolved = false;
//                            break;
//                        }
//                    }
//                    piece.getLegalMoves().clear();
                    if(piece.isPossibleToReach(this, targetKingTile)){
                        if(forBlackToMove){
                           checkForBlack=true; 
                        }else{
                            checkForWhite=true;
                        }
                        checkResolved=false;
                        break;
                    }
                }
            }
            if (!checkResolved) {
                break;
            }
        }
    }

    public boolean isWhiteOnCheck() {
        calculateCheck(false);
        return checkForWhite;
    }

    public boolean isBlackOnCheck() {
        calculateCheck(true);
        return checkForBlack;
    }

    public boolean isLegal(Piece piece, Tile t) {
        piece.generateLegalMoves(this);
        if (!piece.isLegalMove(t)) {
            return false;
        }
        Tile prevTile = getTile(piece);
        move(piece, t);
        boolean isLegal;
        if (piece.getColor() == Constants.BLACK_PIECE_COLOR) {
            isLegal = !isBlackOnCheck();
        } else {
            isLegal = !isWhiteOnCheck();
        }
        move(piece, prevTile);
        return isLegal;
    }
    
    public void move(Piece piece,Tile nt){
        Tile pTile=getTile(piece);
        getTile(piece).emptyTile();
        emptySpace(pTile.getPosition());
        nt.place(piece);
        piece.setPosition(nt);
        board[pTile.getPosition()[0]][pTile.getPosition()[1]]=piece.getPieceChar();
    }

    public void emptySpace(int x, int y) {
        board[x][y] = ' ';
    }

    public void emptySpace(Tile t) {
        emptySpace(t.getPosition());
    }

    public void emptySpace(int[] pos) {
        emptySpace(pos[0], pos[1]);
    }

    public int[] getKingPosition(boolean isBlack) {
        char pieceChar = isBlack ? 'k' : 'K';
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == pieceChar) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public Tile getTile(Piece piece) {
        int[] position = piece.getPosition();
        return getTile(position);
    }

    public Tile getTile(int[] position) {
        return getTile(position[0], position[1]);
    }

    public Tile getTile(char c) {
        for (Tile[] col : tile) {
            for (Tile row : col) {
                if (c == row.getPieceChar()) {
                    return row;
                }
            }
        }
        return null;
    }

    public Tile getTile(int x, int y) {
        int[] position = new int[]{x, y};
        for (Tile[] col : tile) {
            for (Tile row : col) {
                if (Util.isOfSamePosition(row.getPosition(), position)) {
                    return row;
                }
            }
        }
        return null;
    }

    public Tile[][] getTiles() {
        return tile;
    }

    public char[][] getPiecesOnBoard() {
        return board;
    }

}
