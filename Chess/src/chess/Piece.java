package chess;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Piece {

    ImageIcon img;
    Color c;
    int[] position;
    double value;
    ArrayList<Tile> legalMoves;
    String imgLocation;
    char pieceChar;
    int[][] offset;

    public Piece(String name, boolean isBlack, int file, int rank) {
        imgLocation = isBlack ? Constants.BLACK_IMG_PATH + name + ".png" : Constants.WHITE_IMG_PATH + name + ".png";
        position = new int[]{file, rank};
        pieceChar = isBlack ? name.charAt(0) : Util.upper(name.charAt(0));
        c = isBlack ? Constants.BLACK_PIECE_COLOR : Constants.WHITE_PIECE_COLOR;
        img = new ImageIcon(imgLocation);
        legalMoves = new ArrayList<>();
        generateValueAndOffset();
    }

    void generateValueAndOffset() {
        char tempChar = Util.lower(pieceChar);
        switch (tempChar) {
            case 'p':
                value = Constants.PAWN_VALUE;
                offset = Constants.PAWN_OFFSET;
                break;
            case 'n':
                value = Constants.KNIGHT_VALUE;
                offset = Constants.KNIGHT_OFFSET;
                break;
            case 'b':
                value = Constants.BISHOP_VALUE;
                offset = Constants.BISHOP_OFFSET;
                break;
            case 'r':
                value = Constants.ROOK_VALUE;
                offset = Constants.ROOK_OFFSET;
                break;
            case 'q':
                value = Constants.QUEEN_VALUE;
                offset = Constants.QUEEN_OFFSET;
                break;
            case 'k':
                value = Constants.KING_VALUE;
                offset = Constants.KING_OFFSET;
                break;
            default:
                value = Constants.NULL_VALUE;
        }
    }

    public void setPosition(int x, int y) {
        position = new int[]{x, y};
    }

    public void setPosition(int[] pos) {
        setPosition(pos[0], pos[1]);
    }

    public void setPosition(Tile t) {
        setPosition(t.getPosition());
    }

    public void generateLegalMoves(Board board) {
        legalMoves.clear();
        Tile[][] tile = board.getTiles();
        switch (Util.upper(pieceChar)) {
            case 'P': {
                if (c == Constants.BLACK_PIECE_COLOR) {
                    int length = position[1] == 1 ? offset.length : offset.length - 1;
                    for (int i = 0; i < length; i++) {
                        int file = position[0];
                        int rank = position[1];
                        file += offset[i][0];
                        rank += offset[i][1];
                        if (Util.isValid(file, rank)) {
                            if (i == 0 || i == offset.length - 1) {
                                if (tile[file][rank].isEmpty()) {
                                    legalMoves.add(tile[file][rank]);
                                }
                            } else {
                                if (!tile[file][rank].isEmpty() && !isAlly(tile[file][rank].getPiece())) {
                                    legalMoves.add(tile[file][rank]);
                                }
                            }
                        }
                    }
                } else {
                    int length = position[1] == 6 ? offset.length : offset.length - 1;
                    for (int i = 0; i < length; i++) {
                        int file = position[0];
                        int rank = position[1];
                        file += (offset[i][0] * -1);
                        rank += (offset[i][1] * -1);
                        if (Util.isValid(file, rank)) {
                            if (i == 0 || i == offset.length - 1) {
                                if (tile[file][rank].isEmpty()) {
                                    legalMoves.add(tile[file][rank]);
                                }
                            } else {
                                if (!tile[file][rank].isEmpty() && !isAlly(tile[file][rank].getPiece())) {
                                    legalMoves.add(tile[file][rank]);
                                }
                            }
                        }
                    }
                }
                break;
            }
            case 'K': {
                for (int i = 0; i < offset.length; i++) {
                    int file = position[0];
                    int rank = position[1];
                    file += offset[i][0];
                    rank += offset[i][1];
                    if (Util.isValid(file, rank)) {
                        Piece tp = tile[file][rank].getPiece();
                        if (tp != null) {
                            if (isAlly(tp)) {
                                continue;
                            } else if ((Util.isBlackKing(this) && Util.isWhiteKing(tp)) || (Util.isWhiteKing(this) && Util.isBlackKing(tp))) {
                                continue;
                            } else {
                                legalMoves.add(tile[file][rank]);
                                continue;
                            }
                        } else {
                            legalMoves.add(tile[file][rank]);
                        }
                    }
                }
                break;
            }
            case 'N': {
                for (int i = 0; i < offset.length; i++) {
                    int file = position[0];
                    int rank = position[1];
                    file += offset[i][0];
                    rank += offset[i][1];
                    if (Util.isValid(file, rank)) {
                        Piece tp = tile[file][rank].getPiece();
                        if (tp != null) {
                            if (isAlly(tp)) {
                                continue;
                            } else {
                                legalMoves.add(tile[file][rank]);
                                continue;
                            }
                        } else {
                            legalMoves.add(tile[file][rank]);
                        }
                    }
                }
                break;
            }
            default: {
                for (int i = 0; i < offset.length; i++) {
                    int file = position[0];
                    int rank = position[1];
                    file += offset[i][0];
                    rank += offset[i][1];
                    while (Util.isValid(file, rank)) {
                        Piece tp = tile[file][rank].getPiece();
                        if (tp != null) {
                            if (isAlly(tp)) {
                                break;
                            } else {
                                legalMoves.add(tile[file][rank]);
                                break;
                            }
                        } else {
                            legalMoves.add(tile[file][rank]);
                        }
                        file += offset[i][0];
                        rank += offset[i][1];
                    }
                }
                break;
            }
        }
    }

    public boolean isLegalMove(Tile t) {
        int[] tpos = t.getPosition();
        for (Tile tile : legalMoves) {
            if (Util.isOfSamePosition(tpos, tile.getPosition())) {
                return true;
            }
        }
        return false;
    }

    public boolean isPossibleToReach(Board board, Piece piece) {
        return isPossibleToReach(board, board.getTile(piece));
    }

    public boolean isPossibleToReach(Board board, Tile tile) {
        return isPossibleToReach(board, tile.getPosition());
    }

    public boolean isPossibleToReach(Board board, int[] pos) {
        return isPossibleToReach(board, pos[0], pos[1]);
    }

    public boolean isPossibleToReach(Board board, int x, int y) {
        if (Util.isOfSamePosition(position[0], x, position[1], y)) {
            return false;
        }
        int dx = x - position[0];
        int dy = y - position[1];
        ArrayList<int[]> possibleWays = Util.getPossibleWay(dx, dy, offset);
        char temp = Util.lower(pieceChar);
        Tile[][] tile = board.getTiles();
        switch (temp) {
            case 'p': {
                if (c == Constants.BLACK_PIECE_COLOR) {
                    for (int i = 1; i < offset.length - 1; i++) {
                        int file = position[0];
                        int rank = position[1];
                        file += offset[i][0];
                        rank += offset[i][1];
                        if (Util.isValid(file, rank)) {
                            if (Util.isOfSamePosition(file, x, rank, y)) {
                                return true;
                            }
                        }
                    }
                } else {
                    for (int i = 1; i < offset.length - 1; i++) {
                        int file = position[0];
                        int rank = position[1];
                        file += (offset[i][0] * -1);
                        rank += (offset[i][1] * -1);
                        if (Util.isValid(file, rank)) {
                            if (Util.isOfSamePosition(file, x, rank, y)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
            case 'k': {
                for (int[] array : possibleWays) {
                    int file = position[0];
                    int rank = position[1];
                    file += array[0];
                    rank += array[1];
                    if (Util.isValid(file, rank)) {
                        if (Util.isOfSamePosition(file, x, rank, y)) {
                            return true;
                        }
                    }
                }
                return false;
            }
            case 'n': {
                for (int[] array : possibleWays) {
                    int file = position[0];
                    int rank = position[1];
                    file += array[0];
                    rank += array[1];
                    if (Util.isValid(file, rank)) {
                        if (Util.isOfSamePosition(file, x, rank, y)) {
                            return true;
                        }
                    }
                }
                return false;
            }
            default: {
                for (int[] array : possibleWays) {
                    int file = position[0];
                    int rank = position[1];
                    file += array[0];
                    rank += array[1];
                    while (Util.isValid(file, rank)) {
                        if (Util.isOfSamePosition(file, x, rank, y)) {
                            return true;
                        } else {
                            if (!tile[file][rank].isEmpty()) {
                                break;
                            }
                        }
                        file += array[0];
                        rank += array[1];
                    }
                }
                return false;
            }
        }
    }

    public boolean isAlly(char piece) {
        return (Util.isUpper(pieceChar) && Util.isUpper(piece)) || (Util.isLower(piece) && Util.isLower(pieceChar));
    }

    public boolean isAlly(Piece piece) {
        return c == piece.c;
    }

    public ImageIcon getPieceImage() {
        return img;
    }

    public int[] getPosition() {
        return position;
    }

    public Color getColor() {
        return c;
    }

    public char getPieceChar() {
        return pieceChar;
    }

    public ArrayList<Tile> getLegalMoves() {
        return legalMoves;
    }

}
