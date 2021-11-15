package board;

import engine.Engine;
import java.util.ArrayList;
import piece.Piece;
import ui.GamePanel;
import util.Constants;
import util.Parameters;
import util.Util;

public class Board {

    public Square[][] boardSquares;
    public Engine engine;

    public Board(String fen){
        boardSquares = new Square[Constants.COLUMNS][Constants.ROWS];
        engine = new Engine(fen);
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                boardSquares[i][j] = new Square(new int[]{i,j});
            }
        }
        refactor();
    }

    public void refactor(){
        if(Parameters.FLIP){
            for(int i=Constants.COLUMNS-1;i>=0;i--){
                for(int j=Constants.ROWS-1;j>=0;j--){
                    char pieceChar = engine.board[i][j];
                    if(GamePanel.selected==null){
                        boardSquares[Constants.COLUMNS-1-i][Constants.ROWS-1-j].setBackground(boardSquares[Constants.COLUMNS-1-i][Constants.ROWS-1-j].bgColor);
                    }
                    if(pieceChar == Constants.EMPTY_CHAR){
                        boardSquares[Constants.COLUMNS-1-i][Constants.ROWS-1-j].piece = null;
                    }else{
                        boardSquares[Constants.COLUMNS-1-i][Constants.ROWS-1-j].piece = new Piece(pieceChar);
                    }
                    boardSquares[Constants.COLUMNS-1-i][Constants.ROWS-1-j].showPiece();
                }
            }
        }else{
            for(int i=0;i<Constants.COLUMNS;i++){
                for(int j=0;j<Constants.ROWS;j++){
                    char pieceChar = engine.board[i][j];
                    if(GamePanel.selected==null){
                        boardSquares[i][j].setBackground(boardSquares[i][j].bgColor);
                    }
                    if(pieceChar == Constants.EMPTY_CHAR){
                        boardSquares[i][j].piece = null;
                    }else{
                        boardSquares[i][j].piece = new Piece(pieceChar);
                    }
                    boardSquares[i][j].showPiece();
                }
            }
        }
    }

    public Square getSquare(int[] pos){
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                if(Util.samePosition(boardSquares[i][j].position,pos)){
                    return boardSquares[i][j];
                }
            }
        }
        return null;
    }

}
