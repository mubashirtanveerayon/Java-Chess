package test;

import board.Board;
import board.Move;
import util.Constants;
import util.Util;

public class Engine {

    char[][] board;

    public Engine(Board board){
        this.board = board.boardChars;
    }

    public Engine(String fen){
        board = new char[Constants.NUM_OF_COLUMNS][Constants.NUM_OF_ROWS];
        int file = 0;
        int rank = 0;
        String positionFen = fen.split(" ")[0];
        for(int i=0;i<positionFen.length();i++){
            char c = positionFen.charAt(i);
            if(c == '/'){
                file = 0;
                rank ++;
            }else if(Character.isDigit(c)){
                file += Util.getNumericValue(c);
            }else{
                board[file][rank] = c;
            }
            file ++;
        }
    }

    public int evaluateBoard(){
        return compareMaterial() + compareDevelopment() + compareCenterControl() + compareKingSafety();
    }

    public int compareMaterial(){
        int eval = 0;
        for(char c : Constants.WHITE_PIECE_CHAR.toCharArray()){
            eval += getValue(c);
        }
        return eval;
    }

    public int compareDevelopment(){
        return 0;
    }

    public int compareCenterControl(){
        return 0;
    }

    public int compareKingSafety(){
        return 0;
    }

    //need to do this in a separate thread!!!
    public int[][] getBestMove(){
        return new int[][]{};
    }

    public int getValue(char pieceChar){
        int value = 0;
        for(int f = 0; f < Constants.NUM_OF_COLUMNS; f++){
            for(int r = 0; r < Constants.NUM_OF_ROWS; r++){
                char c = board[f][r];
                if(c != Constants.EMPTY_CHAR){
                    if(Util.toUpper(c) == Util.toUpper(pieceChar)){
                        if(Util.isUpperCase(c)){
                            value -= Util.getValue(pieceChar);
                        }else{
                            value += Util.getValue(pieceChar);
                        }
                    }
                }
            }
        }
        return value;
    }


}
