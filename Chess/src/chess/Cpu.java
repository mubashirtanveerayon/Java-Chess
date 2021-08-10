package chess;

import java.util.ArrayList;
import java.util.Random;

public class Cpu {

    Board board;
    ArrayList<Piece> ownPiecesOnBoard;
    Tile moveTile;
    Piece movePiece;
    boolean legalMoveGenerated;

    public Cpu(Board board_) {
        board = board_;
        ownPiecesOnBoard = new ArrayList<>();
        moveTile = null;
        movePiece = null;
        legalMoveGenerated = false;
        calculateOwnPieces();
    }

    public void calculateOwnPieces() {
        ownPiecesOnBoard.clear();
        board.refactorBoard();
        Tile[][] tile = board.getTiles();
        for (int i = 0; i < Constants.NUM_OF_COLUMNS; i++) {
            for (int j = 0; j < Constants.NUM_OF_ROWS; j++) {
                Piece piece = tile[i][j].getPiece();
                if (piece != null && tile[i][j].getPiece().getColor() == Constants.BLACK_PIECE_COLOR) {
                    ownPiecesOnBoard.add(piece);
                }
            }
        }
    }

    public void generateRandomMove() {
        legalMoveGenerated = false;
        moveTile = null;
        movePiece = null;
        while (legalMoveGenerated) {
            while (movePiece == null) {
                int random = new Random().nextInt(ownPiecesOnBoard.size());
                Piece p = ownPiecesOnBoard.get(random);
                p.generateLegalMoves(board);
                if (p.getLegalMoves().isEmpty()) {
                    continue;
                }
                movePiece = p;
            }
            ArrayList<Tile> legalMoves=movePiece.getLegalMoves();
            boolean possibleToMove=false;
            for(Tile tile:legalMoves){
                if(board.isLegal(movePiece, tile)){
                    possibleToMove=true;
                    break;
                }
            }
            if(!possibleToMove){
                movePiece=null;
            }
            while(moveTile==null&&movePiece!=null){
                int randomMove=new Random().nextInt(legalMoves.size());
                moveTile=legalMoves.get(randomMove);
                if(board.isLegal(movePiece, moveTile)){
                    legalMoveGenerated=true;
                    break;
                }else{
                    moveTile=null;
                }
            }            
        }
    }

    public void makeMove() {
        if (legalMoveGenerated) {
            board.move(movePiece, moveTile);
        }
    }

}
