/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.util.ArrayList;

import util.Constants;
import util.Map;
import util.Util;

/**
 *
 * @author ayon2
 */
public class Engine {
    

    public char[][] board;
    
    public String history;
    public String lastMove;
    public int halfMove;
    public int fullMove;
    public boolean whiteToMove;
    public String fen;

    int evalFile,evalRank;
    
    public Engine(String fen){
        this.fen = fen;
        history = fen;
        lastMove = "";
        halfMove = Integer.parseInt(fen.split(" ")[4]);
        fullMove = Integer.parseInt(fen.split(" ")[5]);
        board = Util.loadBoard(fen);
        whiteToMove = fen.split(" ")[1].equals(String.valueOf(Constants.WHITE));
    }
    
    public String move(int[] move){
        boolean legal = board[move[0]][move[1]]!=Constants.EMPTY_CHAR&&((Util.isUpperCase(board[move[0]][move[1]]) && whiteToMove) || (!Util.isUpperCase(board[move[0]][move[1]]) && !whiteToMove));
        if(!legal){
            return fen;
        }
        history=fen;
        lastMove=Util.parseMove(new int[]{move[0],move[1],move[2],move[3]});
        halfMove++;
        if(!whiteToMove){
            fullMove++;
        }
        whiteToMove = !whiteToMove;
        if(Util.toUpper(board[move[0]][move[1]]) == Constants.WHITE_PAWN || board[move[2]][move[3]] != Constants.EMPTY_CHAR){
            halfMove = 0;
        }
        if(Util.toUpper(board[move[0]][move[1]]) == Constants.WHITE_KING && Math.abs(move[0] - move[2]) == 2){
            if(move[2] == 2){
                board[3][move[3]] = board[0][move[3]];
                board[0][move[3]] = Constants.EMPTY_CHAR;
            }else{
                board[5][move[3]] = board[7][move[3]];
                board[7][move[3]] = Constants.EMPTY_CHAR;
            }
        }else if(Util.toUpper(board[move[0]][move[1]]) == Constants.WHITE_PAWN && move[0]-move[2] != 0 && board[move[2]][move[3]] == Constants.EMPTY_CHAR){
            board[move[2]][move[1]] = Constants.EMPTY_CHAR;
        }
        board[move[2]][move[3]] = board[move[0]][move[1]];
        board[move[0]][move[1]] = Constants.EMPTY_CHAR;
        if(move[3] == 7 && board[move[2]][move[3]] == Constants.BLACK_PAWN){
                if(move.length == 5) {
                    if (move[4] == 1) {
                        board[move[2]][move[3]] = Constants.BLACK_KNIGHT;
                    } else if (move[4] == 2) {
                        board[move[2]][move[3]] = Constants.BLACK_BISHOP;
                    } else if (move[4] == 3) {
                        board[move[2]][move[3]] = Constants.BLACK_ROOK;
                    }else{
                        board[move[2]][move[3]] = Constants.BLACK_QUEEN;
                    }
                }else{
                    board[move[2]][move[3]] = Constants.BLACK_QUEEN;
                }
        }else if(move[3] == 0 && board[move[2]][move[3]] == Constants.WHITE_PAWN){
                if(move.length == 5) {
                    if (move[4] == 1) {
                        board[move[2]][move[3]] = Constants.WHITE_KNIGHT;
                    } else if (move[4] == 2) {
                        board[move[2]][move[3]] = Constants.WHITE_BISHOP;
                    } else if (move[4] == 3) {
                        board[move[2]][move[3]] = Constants.WHITE_ROOK;
                    }
                }else{
                    board[move[2]][move[3]] = Constants.WHITE_QUEEN;
                }
        }
        fen = loadFen();
        return fen;
    }
    
    public boolean[] generateCastlingMove(boolean white) {
        boolean queenSideCastling, kingSideCastling;
        String cFen = fen.split(" ")[2];
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
                boolean queenSideUnoccupied = board[1][rank] == Constants.EMPTY_CHAR
                        && board[2][rank] == Constants.EMPTY_CHAR
                        && board[3][rank] == Constants.EMPTY_CHAR;
                if (queenSideUnoccupied) {
                    for (int i = 2; i < 4; i++) {
                        board[i][rank] = king;
                        board[kPos[0]][kPos[1]] = Constants.EMPTY_CHAR;
                        boolean check = isKingInCheck(white);
                        board[kPos[0]][kPos[1]] = king;
                        board[i][rank] = Constants.EMPTY_CHAR;
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
                boolean kingSideUnoccupied = board[5][rank] == Constants.EMPTY_CHAR
                        && board[6][rank] == Constants.EMPTY_CHAR;
                if (kingSideUnoccupied) {
                    for (int i = 5; i < 7; i++) {
                        board[i][rank] = king;
                        board[kPos[0]][kPos[1]] = Constants.EMPTY_CHAR;
                        boolean check = isKingInCheck(white);
                        board[i][rank] = Constants.EMPTY_CHAR;
                        board[kPos[0]][kPos[1]] = king;
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
    
    public boolean isKingInCheck(boolean white){
        int[] kingPos = Util.getKingPosition(board, white);
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                if(board[i][j] != Constants.EMPTY_CHAR){
                    if((white&&!Util.isUpperCase(board[i][j])) || (!white&&Util.isUpperCase(board[i][j]))){
                        switch(Util.toUpper(board[i][j])){
                            case Constants.WHITE_PAWN:{
                                if(Math.abs(kingPos[0] - i) == 1){
                                    if((Util.isUpperCase(board[i][j])&&kingPos[1]-j==-1)||(!Util.isUpperCase(board[i][j])&&kingPos[1]-j==1)){
                                        return true;
                                    }
                                }
                                break;
                            } case Constants.WHITE_KING:
                              case Constants.WHITE_KNIGHT:{
                                int[][] direction = Util.getDirection(kingPos[0]-i,kingPos[1]-j,board[i][j]);
                                for(int[] dir:direction){
                                    if(i+dir[0] == kingPos[0] && j+dir[1] == kingPos[1])    {
                                        return true;
                                    }
                                }
                                break;
                            } case Constants.WHITE_ROOK:{
                                if(kingPos[0] == i){
                                    int k;
                                    if(kingPos[1]<j){
                                        for(k=kingPos[1]+1;k<j;k++){
                                            if(board[i][k] != Constants.EMPTY_CHAR){
                                                break;
                                            }
                                        }
                                        if(j==k){
                                            return true;
                                        }
                                    }else{
                                        for(k=j+1;k<kingPos[1];k++){
                                            if(board[i][k] != Constants.EMPTY_CHAR){
                                                break;
                                            }
                                        }
                                        if(kingPos[1]==k){
                                            return true;
                                        }
                                    }
                                }else if(kingPos[1] == j){
                                    int k;
                                    if(kingPos[0]<i){
                                        for(k=kingPos[0]+1;k<i;k++){
                                            if(board[k][j] != Constants.EMPTY_CHAR){
                                                break;
                                            }
                                        }
                                        if(i==k){
                                            return true;
                                        }
                                    }else{
                                        for(k=i+1;k<kingPos[0];k++){
                                            if(board[k][j] != Constants.EMPTY_CHAR){
                                                break;
                                            }
                                        }
                                        if(kingPos[0]==k){
                                            return true;
                                        }
                                    }
                                }
                                break;
                            } case Constants.WHITE_BISHOP:{
                                if(Math.abs(kingPos[0] - i) == Math.abs(kingPos[1] - j)){
                                    int[][] direction = Util.getDirection(kingPos[0]-i,kingPos[1]-j,board[i][j]);
                                    for(int[] dir : direction){
                                        int file = i;
                                        int rank = j;
                                        file+=dir[0];
                                        rank+=dir[1];
                                        while(file!=kingPos[0] && rank!=kingPos[1]){
                                            if(board[file][rank] != Constants.EMPTY_CHAR){
                                                break;
                                            }
                                            file+=dir[0];
                                            rank+=dir[1];
                                        }
                                        if(file==kingPos[0] && rank==kingPos[1]){
                                            return true;
                                        }
                                    }
                                }
                                break;
                            } case Constants.WHITE_QUEEN:{
                                //horizontal
                                if(kingPos[0] == i){
                                    int k;
                                    if(kingPos[1]<j){
                                        for(k=kingPos[1]+1;k<j;k++){
                                            if(board[i][k] != Constants.EMPTY_CHAR){
                                                break;
                                            }
                                        }
                                        if(j==k){
                                            return true;
                                        }
                                    }else{
                                        for(k=j+1;k<kingPos[1];k++){
                                            if(board[i][k] != Constants.EMPTY_CHAR){
                                                break;
                                            }
                                        }
                                        if(kingPos[1]==k){
                                            return true;
                                        }
                                    }
                                }else if(kingPos[1] == j){
                                    int k;
                                    if(kingPos[0]<i){
                                        for(k=kingPos[0]+1;k<i;k++){
                                            if(board[k][j] != Constants.EMPTY_CHAR){
                                                break;
                                            }
                                        }
                                        if(i==k){
                                            return true;
                                        }
                                    }else{
                                        for(k=i+1;k<kingPos[0];k++){
                                            if(board[k][j] != Constants.EMPTY_CHAR){
                                                break;
                                            }
                                        }
                                        if(kingPos[0]==k){
                                            return true;
                                        }
                                    }
                                }
                                //diagonal
                                if(Math.abs(kingPos[0] - i) == Math.abs(kingPos[1] - j)){
                                    int[][] direction = Util.getDirection(kingPos[0]-i,kingPos[1]-j,board[i][j]);
                                    for(int[] dir : direction){
                                        int file = i;
                                        int rank = j;
                                        file+=dir[0];
                                        rank+=dir[1];
                                        while(file!=kingPos[0] && rank!=kingPos[1]){
                                            if(board[file][rank] != Constants.EMPTY_CHAR){
                                                break;
                                            }
                                            file+=dir[0];
                                            rank+=dir[1];
                                        }
                                        if(file==kingPos[0] && rank==kingPos[1]){
                                            return true;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }


    public ArrayList<int[]> generateMove(char pieceChar,int[] position,int[][] direction){
        ArrayList<int[]> legalMoves = new ArrayList<>();
        boolean white = Util.isUpperCase(pieceChar);
        switch(Util.toUpper(pieceChar)){
            case Constants.WHITE_KING:{
                for(int[] dir:direction){
                    int file = position[0];
                    int rank = position[1];
                    file+=dir[0];
                    rank+=dir[1];
                    if(Util.isValid(file,rank)){
                        if(board[file][rank] == Constants.EMPTY_CHAR){
                            char to = board[file][rank];
                            board[file][rank] = pieceChar;
                            board[position[0]][position[1]] = Constants.EMPTY_CHAR;
                            if(!isKingInCheck(white)){
                                legalMoves.add(new int[]{file,rank});
                            }
                            board[file][rank] = to;
                            board[position[0]][position[1]] = pieceChar;
                        }else{
                            if(!Util.isAlly(pieceChar,board[file][rank])){
                                char to = board[file][rank];
                                board[file][rank] = pieceChar;
                                board[position[0]][position[1]] = Constants.EMPTY_CHAR;
                                if(!isKingInCheck(white)){
                                    legalMoves.add(new int[]{file,rank});
                                }
                                board[file][rank] = to;
                                board[position[0]][position[1]] = pieceChar;
                            }
                        }
                    }
                }
                int rank = white ? 7:0;
                boolean[] castling = generateCastlingMove(white);
                if(castling[0]){
                    legalMoves.add(new int[]{2,rank});
                }
                if(castling[1]){
                    legalMoves.add(new int[]{6,rank});
                }
                break;
            }
            case Constants.WHITE_KNIGHT:{
                for(int[] dir:direction){
                    int file = position[0];
                    int rank = position[1];
                    file+=dir[0];
                    rank+=dir[1];
                    if(Util.isValid(file,rank)){
                        if(board[file][rank] == Constants.EMPTY_CHAR){
                            char to = board[file][rank];
                            board[file][rank] = pieceChar;
                            board[position[0]][position[1]] = Constants.EMPTY_CHAR;
                            if(!isKingInCheck(white)){
                                legalMoves.add(new int[]{file,rank});
                            }
                            board[file][rank] = to;
                            board[position[0]][position[1]] = pieceChar;
                        }else{
                            if(!Util.isAlly(pieceChar,board[file][rank])){
                                char to = board[file][rank];
                                board[file][rank] = pieceChar;
                                board[position[0]][position[1]] = Constants.EMPTY_CHAR;
                                if(!isKingInCheck(white)){
                                    legalMoves.add(new int[]{file,rank});
                                }
                                board[file][rank] = to;
                                board[position[0]][position[1]] = pieceChar;
                            }
                        }
                    }
                }
                break;
            }
            case Constants.WHITE_PAWN:{
                int limit = (Util.isUpperCase(pieceChar) && position[1] == 6) || (!Util.isUpperCase(pieceChar) && position[1] == 1) ? 2 : 1;
                for(int[] dir:direction){
                    int file = position[0];
                    int rank = position[1];
                    file+=dir[0];
                    rank+=dir[1];
                    if(Util.isValid(file,rank)){
                        if(Math.abs(dir[0]) == 1 && Math.abs(dir[1]) == 1){
                            if(board[file][rank] != Constants.EMPTY_CHAR && !Util.isAlly(board[file][rank], pieceChar)){
                                char to = board[file][rank];
                                board[file][rank] = pieceChar;
                                board[position[0]][position[1]] = Constants.EMPTY_CHAR;
                                if(!isKingInCheck(white)){
                                    legalMoves.add(new int[]{file,rank});
                                    if((Util.isUpperCase(pieceChar)&&rank == 0)||(!Util.isUpperCase(pieceChar)&&rank == 7)){
                                        legalMoves.add(new int[]{file,rank,1});//knight
                                        legalMoves.add(new int[]{file,rank,2});//bishop
                                        legalMoves.add(new int[]{file,rank,3});//rook
                                    }
                                }
                                board[file][rank] = to;
                                board[position[0]][position[1]] = pieceChar;
                            }
                        }else{
                            for(int i=0;i<limit && Util.isValid(file,rank);i++){
                                if(board[file][rank] == Constants.EMPTY_CHAR){
                                    char to = board[file][rank];
                                    board[file][rank] = pieceChar;
                                    board[position[0]][position[1]] = Constants.EMPTY_CHAR;
                                    if(!isKingInCheck(white)){
                                        legalMoves.add(new int[]{file,rank});
                                        if((Util.isUpperCase(pieceChar)&&rank == 0)||(!Util.isUpperCase(pieceChar)&&rank == 7)){
                                            legalMoves.add(new int[]{file,rank,1});//knight
                                            legalMoves.add(new int[]{file,rank,2});//bishop
                                            legalMoves.add(new int[]{file,rank,3});//rook
                                        }
                                    }
                                    board[file][rank] = to;
                                    board[position[0]][position[1]] = pieceChar;
                                }else{
                                    break;
                                }
                                file+=dir[0];
                                rank+=dir[1];
                            }
                        }
                    }
                }
                String enPassant = fen.split(" ")[3];
                if(!enPassant.equals("-")){
                    int[] enPassantSquare = Util.cvtPosition(enPassant);
                    if(Math.abs(position[0]-enPassantSquare[0]) == 1 && Math.abs(position[1]-enPassantSquare[1]) == 1){
                        char piece = board[enPassantSquare[0]][position[1]];
                        board[enPassantSquare[0]][position[1]] = Constants.EMPTY_CHAR;
                        board[position[0]][position[1]] = Constants.EMPTY_CHAR;
                        board[enPassantSquare[0]][enPassantSquare[1]] = pieceChar;
                        if(!isKingInCheck(white)){
                            legalMoves.add(new int[]{enPassantSquare[0],enPassantSquare[1]});
                        }
                        board[enPassantSquare[0]][position[1]] = piece;
                        board[position[0]][position[1]] = pieceChar;
                        board[enPassantSquare[0]][enPassantSquare[1]] = Constants.EMPTY_CHAR;
                    }
                }
                break;
            }
            default:{
                for(int[] dir:direction){
                    int file = position[0];
                    int rank = position[1];
                    file+=dir[0];
                    rank+=dir[1];
                    while(Util.isValid(file,rank)){
                        if(board[file][rank] == Constants.EMPTY_CHAR){
                            char to = board[file][rank];
                            board[file][rank] = pieceChar;
                            board[position[0]][position[1]] = Constants.EMPTY_CHAR;
                            if(!isKingInCheck(white)){
                                legalMoves.add(new int[]{file,rank});
                            }
                            board[file][rank] = to;
                            board[position[0]][position[1]] = pieceChar;
                        }else{
                            if(!Util.isAlly(board[file][rank], pieceChar)){
                                char to = board[file][rank];
                                board[file][rank] = pieceChar;
                                board[position[0]][position[1]] = Constants.EMPTY_CHAR;
                                if(!isKingInCheck(white)){
                                    legalMoves.add(new int[]{file,rank});
                                }
                                board[file][rank] = to;
                                board[position[0]][position[1]] = pieceChar;
                            }
                            break;
                        }
                        file+=dir[0];
                        rank+=dir[1];
                    }
                }
            }
        }
        return legalMoves;
    }

    
    
    public String loadFen(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Constants.ROWS; i++) {
            int gap = 0;
            for (int j = 0; j < Constants.ROWS; j++) {
                if (board[j][i] != Constants.EMPTY_CHAR) {
                    if (gap != 0) {
                        sb.append(gap);
                    }
                    sb.append(board[j][i]);
                    gap = 0;
                } else {
                    gap++;
                }
            }
            if (gap != 0) {
                sb.append(gap);
            }
            if (i != Constants.COLUMNS - 1) {
                sb.append('/');
            }
        }
        sb.append(" ");
        if (whiteToMove) {
            sb.append(Constants.WHITE);
        } else {
            sb.append(Constants.BLACK);
        }

        sb.append(" ");
        sb.append(getCastlingRights());
        sb.append(" ");
        sb.append(getEnPassantSquare());

        sb.append(" ");
        sb.append(halfMove);
        sb.append(" ");
        sb.append(fullMove);

        return sb.toString();
    }
    
    public String getCastlingRights(){
        StringBuilder sb = new StringBuilder();
        String lastCastlingFen = history.split(" ")[2];
        boolean castlingPossible = !lastCastlingFen.equals("-");
        if (castlingPossible) {
            boolean wKingInPosition = board[4][7] == Constants.WHITE_KING;
            boolean bKingInPosition = board[4][0] == Constants.BLACK_KING;
            if (wKingInPosition) {
                boolean wKRookInPosition = board[7][7] == Constants.WHITE_ROOK;
                boolean wQRookInPosition = board[0][7] == Constants.WHITE_ROOK;
                if (wKRookInPosition) {
                    if (lastCastlingFen.contains(String.valueOf(Constants.WHITE_KING))) {
                        sb.append(Constants.WHITE_KING);
                    }
                }
                if (wQRookInPosition) {
                    if (lastCastlingFen.contains(String.valueOf(Constants.WHITE_QUEEN))) {
                        sb.append(Constants.WHITE_QUEEN);
                    }
                }
            }
            if (bKingInPosition) {
                boolean bKRookInPosition = board[7][0] == Constants.BLACK_ROOK;
                boolean bQRookInPosition = board[0][0] == Constants.BLACK_ROOK;
                if (bKRookInPosition) {
                    if (lastCastlingFen.contains(String.valueOf(Constants.BLACK_KING))) {
                        sb.append(Constants.BLACK_KING);
                    }
                }
                if (bQRookInPosition) {
                    if (lastCastlingFen.contains(String.valueOf(Constants.BLACK_QUEEN))) {
                        sb.append(Constants.BLACK_QUEEN);
                    }
                }
            }
        }
        try {
            if (!castlingPossible || sb.length() == 0 || sb.charAt(sb.length() - 1) == ' ') {
                sb.append("-");
            }
        } catch (Exception ex) {
            sb.append("-");
        }
        return sb.toString();
    }

    public String getEnPassantSquare(){
        StringBuilder sb = new StringBuilder();
        if (lastMove.isEmpty()) {
            return history.split(" ")[3];
        } else {
            String lastMoveStr = lastMove;

            int[][] lastMove = Util.parseMove(lastMoveStr);

            int[] initPosition = lastMove[0];
            int[] lastPosition = lastMove[1];

            if (Util.toUpper(board[lastPosition[0]][lastPosition[1]]) == Constants.WHITE_PAWN) {
                int rDiff = Math.abs(lastPosition[1] - initPosition[1]);
                if (rDiff == 2) {
                    sb.append(Constants.FILES.charAt(lastPosition[0]));
                    if (Util.isUpperCase(board[lastPosition[0]][lastPosition[1]])) {
                        sb.append(Constants.RANKS.charAt(lastPosition[1] + 1));
                    } else {
                        sb.append(Constants.RANKS.charAt(lastPosition[1] - 1));
                    }
                } else {
                    sb.append("-");
                }
            } else {
                sb.append("-");
            }
        }
        return sb.toString();
    }
    
    public long moveGeneration(int depth){
        if(depth == 0){
            return 1;
        }
        
        long numPositions = 0;
        ArrayList<int[]> legalMoves = getLegalMoves();
        char[][] prevBoardChars = Util.copyBoard(board);
        boolean prevWhiteToMove = whiteToMove;
        int prevHalfMove = halfMove;
        int prevFullMove = fullMove;
        String prevFen = fen;
        String prevHistory = history;
        String prevLastMove = lastMove;

        for(int[] moves:legalMoves){
            move(moves);
            numPositions+=moveGeneration(depth-1);
            board = Util.copyBoard(prevBoardChars);
            history = prevHistory;
            lastMove = prevLastMove;
            whiteToMove = prevWhiteToMove;
            halfMove = prevHalfMove;
            fullMove = prevFullMove;
            fen = prevFen;
        }
        return numPositions;
    }

    public boolean checkMate(boolean white){
        if(isKingInCheck(white)){
            for(int i=0;i<Constants.COLUMNS;i++){
                for(int j=0;j<Constants.ROWS;j++){
                    char piece = board[i][j];
                    if(piece!=Constants.EMPTY_CHAR){
                        if((white&&Util.isUpperCase(piece)) || (!white&&!Util.isUpperCase(piece))){
                            ArrayList<int[]> legalMoves = generateMove(piece,new int[]{i,j},Util.getOffset(piece));
                            if(!legalMoves.isEmpty()){
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }else{
            return false;
        }
    }

    public boolean isDraw(){
        return isFiftyMove()  || isStaleMate()|| isThreeFoldRepetition() || isInsufficientMaterial();
    }

    public boolean isFiftyMove(){
        return halfMove >= 100 && !getLegalMoves().isEmpty();
    }


    public boolean isStaleMate(){
        return getLegalMoves().isEmpty() && !checkMate(whiteToMove);
    }

    public boolean isInsufficientMaterial(){
        return false;
    }

    public boolean isThreeFoldRepetition(){
        return false;
    }

    public float evaluateBoard(boolean white){
        float eval = 0f;
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                if(board[i][j]!=Constants.EMPTY_CHAR){
                    if(white){
                        //material comparison and positional advantages
                        if(Util.isUpperCase(board[i][j])){
                            eval += (Util.getValue(board[i][j]));//+(endGameWeight())*Map.positionalAdvantage(board[i][j],i,j);//+((1.0f/endGameWeight())*Map.positionalAdvantage(board[i][j],i,j)));
                            if(board[i][j] == Constants.WHITE_PAWN) {//Util.toUpper(board[i][j]) == Constants.WHITE_KNIGHT){
                                eval+=Map.positionalAdvantage(board[i][j],i,j);
                            }else if(board[i][j] == Constants.WHITE_KNIGHT){
                                eval+=Map.positionalAdvantage(board[i][j],i,j)*count()*0.005f;
                            }
                        }else{
                            eval -= (Util.getValue(board[i][j]));//+(endGameWeight())*Map.positionalAdvantage(board[i][j],i,j);//+((1.0f/endGameWeight())*Map.positionalAdvantage(board[i][j],i,j)));
                            if(board[i][j] == Constants.BLACK_PAWN ){
                                eval-=Map.positionalAdvantage(board[i][j],i,j);
                            }else if(board[i][j] == Constants.BLACK_KNIGHT){
                                eval-=Map.positionalAdvantage(board[i][j],i,j)*count()*0.005f;
                            }
                        }
                    }else{
                        //material comparison and positional advantages
                        if(Util.isUpperCase(board[i][j])){
                            eval -= (Util.getValue(board[i][j]));//+(endGameWeight())*Map.positionalAdvantage(board[i][j],i,j);//+((1.0f/endGameWeight())*Map.positionalAdvantage(board[i][j],i,j)));
                            if(board[i][j] == Constants.WHITE_PAWN) {//Util.toUpper(board[i][j]) == Constants.WHITE_KNIGHT){
                                eval-=Map.positionalAdvantage(board[i][j],i,j);
                            }else if(board[i][j] == Constants.WHITE_KNIGHT){
                                eval-=Map.positionalAdvantage(board[i][j],i,j)*count()*0.005f;
                            }
                        }else{
                            eval += (Util.getValue(board[i][j]));//+(endGameWeight())*Map.positionalAdvantage(board[i][j],i,j);//+((1.0f/endGameWeight())*Map.positionalAdvantage(board[i][j],i,j)));
                            if(board[i][j] == Constants.BLACK_PAWN ){
                                eval+=Map.positionalAdvantage(board[i][j],i,j);
                            }else if(board[i][j] == Constants.BLACK_KNIGHT){
                                eval+=Map.positionalAdvantage(board[i][j],i,j)*count()*0.005f;
                            }
                        }
                    }
                }
            }
        }
        return eval;
    }

    public float kingSafety(boolean white){
        int[] kingPos = Util.getKingPosition(board,white);
        float distFromCenterFile = kingPos[0]-3.5f;
        float distFromCenterRank = kingPos[1]-3.5f;
        float distFromCenter = (float)Math.sqrt((distFromCenterFile*distFromCenterFile)+(distFromCenterRank*distFromCenterRank));
        return -distFromCenter*endGameWeight();
    }

    public float centerControl(boolean white){
        return 0;
    }

    public float evaluate(boolean white){
        float eval = 0f;
        evalFile = 0;
        evalRank = 0;
        for(char c:fen.split(" ")[0].toCharArray()) {
            if (Character.isDigit(c)) {
                evalFile += Util.getNumericValue(c);
            } else if (c == '/') {
                evalRank++;
                evalFile = 0;
            } else {
                if (Util.isUpperCase(c)) {
                    eval += white ? (Util.getValue(c) + Map.getPositionalAdvantage(c, evalFile, evalRank)) : (Util.getValue(c) + Map.getPositionalAdvantage(c, evalFile, evalRank)) * -1;
                } else {
                    eval -= white ? (Util.getValue(c) + Map.getPositionalAdvantage(c, evalFile, evalRank)) : (Util.getValue(c) + Map.getPositionalAdvantage(c, evalFile, evalRank)) * -1;
                }
                evalFile++;
            }
        }
        return eval;
    }

    public int count() {
        int count = 0;
        for (int i = 0; i < Constants.COLUMNS; i++) {
            for (int j = 0; j < Constants.ROWS; j++) {
                if (board[i][j] != Constants.EMPTY_CHAR) {
                    count++;
                }
            }
        }
        return count;
    }

    public ArrayList<int[]> getLegalMoves(){
        ArrayList<int[]> legalMoves = new ArrayList<>();
        ArrayList<int[]> pieceMoves = new ArrayList<>();
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                if(board[i][j]!=Constants.EMPTY_CHAR){
                    if((whiteToMove&&Util.isUpperCase(board[i][j]))||(!whiteToMove&&!Util.isUpperCase(board[i][j]))) {
                       pieceMoves = generateMove(board[i][j],new int[]{i,j},Util.getOffset(board[i][j]));
                        for(int[] move:pieceMoves){
                            if(move.length==2) {
                                legalMoves.add(new int[]{i, j, move[0], move[1]});
                            }else{
                                legalMoves.add(new int[]{i, j, move[0], move[1],move[2]});
                            }
                        }
                        pieceMoves.clear();
                    }
                }
            }
        }//rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8
        return legalMoves;

    }

    public float endGameWeight(){
        return (float)(1.0f/count())*Constants.PARTIAL_VALUE*fullMove;
    }

    public Engine copy(){
        return new Engine(fen);
    }


}
