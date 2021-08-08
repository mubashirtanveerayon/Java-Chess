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

    public Piece(String name, boolean isBlack, int file, int rank) {
        imgLocation = isBlack ? Constants.BLACK_IMG_PATH + name + ".png" : Constants.WHITE_IMG_PATH + name + ".png";
        position = new int[]{file, rank};
        pieceChar = isBlack ? name.charAt(0) : Util.upper(name.charAt(0));
        ArrayList<Tile> legalMoves = new ArrayList();
        c = isBlack ? Constants.BLACK_PIECE_COLOR : Constants.WHITE_PIECE_COLOR;
        img = new ImageIcon(imgLocation);
        generateValue();
    }

    void generateValue() {
        char tempChar = Util.lower(pieceChar);
        switch (tempChar) {
            case 'p':
                value = Constants.PAWN_VALUE;
                break;
            case 'n':
                value = Constants.KNIGHT_VALUE;
                break;
            case 'b':
                value = Constants.BISHOP_VALUE;
                break;
            case 'r':
                value = Constants.ROOK_VALUE;
                break;
            case 'q':
                value = Constants.QUEEN_VALUE;
                break;
            case 'k':
                value = Constants.KING_VALUE;
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
        if (Util.upper(pieceChar)== 'B') {
            int[][] offset = {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}};
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
                }
            }
        } else if (Util.upper(pieceChar)== 'R') {
            int[][] offset = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
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
                }
            }
        } else if (Util.upper(pieceChar)== 'Q') {
            int[][] offset = {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}, {-1, 0}, {1, 0}, {0, 1}, {0, -1}};
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
                }
            }
        } else if (Util.upper(pieceChar)== 'K') {
            int[][] offset = {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}, {-1, 0}, {1, 0}, {0, 1}, {0, -1}};
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
                        } else if ((Util.isBlackKing(this)&&Util.isWhiteKing(tp))||(Util.isWhiteKing(this)&&Util.isBlackKing(tp))) {
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
        } else if (Util.upper(pieceChar)== 'N') {
            int[][] offset = {{2, -1}, {1, -2}, {1, 2}, {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}};
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
