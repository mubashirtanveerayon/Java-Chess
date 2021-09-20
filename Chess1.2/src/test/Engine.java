package test;

import board.Board;
import board.Move;
import util.Constants;
import util.Util;

import java.util.ArrayList;

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

    public boolean isKingInCheck(boolean white){

        return false;
    }

    ArrayList<int[]> generateMove(char pieceChar,int[] position){
        ArrayList<int[]> legalMoves = new ArrayList<>();

        return legalMoves;
    }

    public boolean move(int[] fromSquare,int[] toSquare){
        char from = board[fromSquare[0]][fromSquare[1]];
        if(from != Constants.EMPTY_CHAR && Util.toUpper(from) == Constants.WHITE_KING){
            return false;
        }
        ArrayList<int[]> legalMoves = generateMove(from,fromSquare);
        for(int[] moves : legalMoves){
            if(Util.samePosition(moves,toSquare)){
                board[fromSquare[0]][fromSquare[1]] = Constants.EMPTY_CHAR;
                //need to check for special cases
                if(Util.toUpper(from) == Constants.WHITE_KING){
                    int fDiff = Math.abs(fromSquare[0] - toSquare[1]);
                    if(fDiff == 2){
                        int rank = fromSquare[1];
                        if(toSquare[0] == 2){
                            char rook = board[0][rank];
                            board[0][rank] = Constants.EMPTY_CHAR;
                            board[3][rank] = rook;
                        }else{
                            char rook = board[7][rank];
                            board[7][rank] = Constants.EMPTY_CHAR;
                            board[5][rank] = rook;
                        }
                    }
                }else if(Util.toUpper(from) == Constants.WHITE_PAWN){

                }
                board[toSquare[0]][toSquare[1]] = from;
                break;
            }
        }
        return true;
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
