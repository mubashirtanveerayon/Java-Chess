/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.util.ArrayList;
import util.Constants;
import util.Util;

/**
 *
 * @author ayon2
 */
public class Engine {
    
    public static final int[] MID_RANKS = {3,4};
    public char[][] board;
    
    public String history;
    public String lastMove;
    public int halfMove;
    public int fullMove;
    public boolean whiteToMove;
    public String fen;
    
    public Engine(String fen){
        this.fen = fen;
        history = fen;
        lastMove = "";
        halfMove = Integer.parseInt(fen.split(" ")[4]);
        fullMove = Integer.parseInt(fen.split(" ")[5]);
        board = Util.loadBoard(fen);
        whiteToMove = fen.split(" ")[1].equals(String.valueOf(Constants.WHITE));
    }
    
    public String move(int[] from,int[] to){
        boolean legal = board[from[0]][from[1]]!=Constants.EMPTY_CHAR&&((Util.isUpperCase(board[from[0]][from[1]]) && whiteToMove) || (!Util.isUpperCase(board[from[0]][from[1]]) && !whiteToMove));
        if(!legal){
            return fen;
        }
        history=fen;
        lastMove=Util.parseMove(from, to);
        halfMove++;
        if(!whiteToMove){
            fullMove++;
        }
        whiteToMove = !whiteToMove;
        if(Util.toUpper(board[from[0]][from[1]]) == Constants.WHITE_PAWN || board[to[0]][to[1]] != Constants.EMPTY_CHAR){
            halfMove = 0;
        }
        if(Util.toUpper(board[from[0]][from[1]]) == Constants.WHITE_KING && Math.abs(from[0] - to[0]) == 2){
            if(to[0] == 2){
                board[3][to[1]] = board[0][to[1]];
                board[0][to[1]] = Constants.EMPTY_CHAR;
            }else{
                board[5][to[1]] = board[7][to[1]];
                board[7][to[1]] = Constants.EMPTY_CHAR;
            }
        }else if(Util.toUpper(board[from[0]][from[1]]) == Constants.WHITE_PAWN && from[0]-to[0] != 0 && board[to[0]][to[1]] == Constants.EMPTY_CHAR){
            board[to[0]][from[1]] = Constants.EMPTY_CHAR;
        }
        board[to[0]][to[1]] = board[from[0]][from[1]];
        board[from[0]][from[1]] = Constants.EMPTY_CHAR;
        if(to[1] == 7 && board[to[0]][to[1]] == Constants.BLACK_PAWN){
            board[to[0]][to[1]] = Constants.BLACK_QUEEN;
        }else if(to[1] == 0 && board[to[0]][to[1]] == Constants.WHITE_PAWN){
            board[to[0]][to[1]] = Constants.WHITE_QUEEN;
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
        for (int i = 0; i < Constants.COLUMNS; i++) {
            for(int j=0;j<Constants.ROWS;j++){
                if(board[i][j] != Constants.EMPTY_CHAR){
                    int[] pos = new int[]{i,j};
                    if((white && !Util.isUpperCase(board[i][j])) || (!white && Util.isUpperCase(board[i][j]))){
                        ArrayList<int[]> captures = generateCaptureMove(board[i][j],pos,Util.getDirection(kingPos[0]-pos[0], kingPos[1]-pos[1], board[i][j]));
                        for(int[] move:captures){
                            if(Util.samePosition(kingPos,move)){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public ArrayList<int[]> generateCaptureMove(char pieceChar,int[] position,int[][] direction){
        ArrayList<int[]> captureMoves = new ArrayList<>();
        switch(Util.toUpper(pieceChar)){
            case Constants.WHITE_KING:
            case Constants.WHITE_KNIGHT:{
                for(int[] dir:direction){
                    int file = position[0];
                    int rank = position[1];
                    file+=dir[0];
                    rank+=dir[1];
                    if(Util.isValid(file,rank)){
                        if(board[file][rank] != Constants.EMPTY_CHAR && !Util.isAlly(pieceChar,board[file][rank])){
                            captureMoves.add(new int[]{file,rank});
                        }
                    }
                }
                break;
            }
            case Constants.WHITE_PAWN:{
                for(int[] dir:direction){
                    int file = position[0];
                    int rank = position[1];
                    file+=dir[0];
                    rank+=dir[1];
                    if(Util.isValid(file,rank)){
                        if(Math.abs(dir[0]) == 1 && Math.abs(dir[1]) == 1){
                            if(board[file][rank] != Constants.EMPTY_CHAR && !Util.isAlly(board[file][rank], pieceChar)){
                                captureMoves.add(new int[]{file,rank});
                            }
                        }
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
                        if(board[file][rank] != Constants.EMPTY_CHAR){
                            if(!Util.isAlly(board[file][rank], pieceChar)){
                                captureMoves.add(new int[]{file,rank});
                            }
                            break;
                        }
                        file+=dir[0];
                        rank+=dir[1];
                    }
                }
            }
        }
        return captureMoves;
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
                            legalMoves.add(new int[]{file,rank});
                        }else{
                            if(!Util.isAlly(pieceChar,board[file][rank])){
                                legalMoves.add(new int[]{file,rank});
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
                            legalMoves.add(new int[]{file,rank});
                        }else{
                            if(!Util.isAlly(pieceChar,board[file][rank])){
                                legalMoves.add(new int[]{file,rank});
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
                                legalMoves.add(new int[]{file,rank});
                            }
                        }else{
                            for(int i=0;i<limit && Util.isValid(file,rank);i++){
                                if(board[file][rank] == Constants.EMPTY_CHAR){
                                    legalMoves.add(new int[]{file,rank});
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
                        legalMoves.add(enPassantSquare);
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
                            legalMoves.add(new int[]{file,rank});
                        }else{
                            if(!Util.isAlly(board[file][rank], pieceChar)){
                                legalMoves.add(new int[]{file,rank});
                            }
                            break;
                        }
                        file+=dir[0];
                        rank+=dir[1];
                    }
                }
            }
        }
        ArrayList<int[]> toRemove = new ArrayList<>();
        for(int[] move:legalMoves){
            char to = board[move[0]][move[1]];
            board[move[0]][move[1]] = pieceChar;
            board[position[0]][position[1]] = Constants.EMPTY_CHAR;
            if(isKingInCheck(white)){
                toRemove.add(move);
            }
            board[move[0]][move[1]] = to;
            board[position[0]][position[1]] = pieceChar;
        }
        legalMoves.removeAll(toRemove);
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
        
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                char piece = board[i][j];
                if(piece!=Constants.EMPTY_CHAR){
                    int[] position = new int[]{i,j};
                    if((whiteToMove && Util.isUpperCase(piece)) || (!whiteToMove && !Util.isUpperCase(piece))){
                        ArrayList<int[]> legalMoves = generateMove(piece,position,Util.getOffset(piece));
                        char[][] prevBoardChars = Util.copyBoard(board);
                        boolean prevWhiteToMove = whiteToMove;
                        int prevHalfMove = halfMove;
                        int prevFullMove = fullMove;
                        String prevFen = fen;
                        String prevHistory = history;
                        String prevLastMove = lastMove;
                        for(int[] move:legalMoves){
                            move(position,move);
                            numPositions+=moveGeneration(depth-1);
                            board = Util.copyBoard(prevBoardChars);
                            history = prevHistory;
                            lastMove = prevLastMove;
                            whiteToMove = prevWhiteToMove;
                            halfMove = prevHalfMove;
                            fullMove = prevFullMove;
                            fen = prevFen;
                        }
                    }
                }
            }
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

    public float evaluateBoard(boolean white){
        float eval = 0f;
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                if(board[i][j]!=Constants.EMPTY_CHAR){
                    char piece = board[i][j];
                    int[] position = new int[]{i,j};
                    ArrayList<int[]> dir = new ArrayList<>();
                    int[][] offset = Util.getOffset(piece);
                    for(int rank:MID_RANKS){
                        int dy = rank-j;
                        for(int[] d:offset){
                            if(Util.isOfSameDir(d[1],dy)){
                                dir.add(d);
                            }
                        }
                    }
                    ArrayList<int[]> legalMoves = generateMove(piece,position,Util.copyArrayList(dir));
                    float pieceVal = Util.getValue(piece);
                    if(white){       
                        //material comparison 
                        if(Util.isUpperCase(piece)){
                            eval += pieceVal;
                        }else{
                            eval -= pieceVal;
                        }
                        //center-control
                        for(int rank:MID_RANKS){
                            if(j==rank){
                                if(Util.isUpperCase(piece)){
                                    eval += (float)(pieceVal*Constants.PARTIAL_VALUE);
                                }else{
                                    eval -= (float)(pieceVal*Constants.PARTIAL_VALUE);
                                }
                            }
                            for(int[] moves:legalMoves){
                                if(moves[1] == rank){
                                    if(Util.isUpperCase(piece)){
                                        eval += (float)(pieceVal*Constants.PARTIAL_VALUE);
                                    }else{
                                        eval -= (float)(pieceVal*Constants.PARTIAL_VALUE);
                                    }
                                }
                            }
                        }
                    }else{
                        //material comparison
                        if(Util.isUpperCase(piece)){
                            eval -= pieceVal;
                        }else{
                            eval += pieceVal;
                        }
                        //center-control
                        for(int rank:MID_RANKS){
                            if(j==rank){
                                if(Util.isUpperCase(piece)){
                                    eval -= (float)(pieceVal*Constants.PARTIAL_VALUE);
                                }else{
                                    eval += (float)(pieceVal*Constants.PARTIAL_VALUE);
                                }
                            }
                            for(int[] moves:legalMoves){
                                if(moves[1] == rank){
                                    if(Util.isUpperCase(piece)){
                                        eval -= pieceVal*Constants.PARTIAL_VALUE;
                                    }else{
                                        eval += pieceVal*Constants.PARTIAL_VALUE;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return eval;
    }

    public int count(){
        int count = 0;
        for(int i=0;i<Constants.COLUMNS;i++){
            for(int j=0;j<Constants.ROWS;j++){
                if(board[i][j]!=Constants.EMPTY_CHAR){
                    count++;
                }
            }
        }
        return count;
    }
    
   
}
