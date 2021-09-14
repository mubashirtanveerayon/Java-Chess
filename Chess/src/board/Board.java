package board;

import piece.Piece;
import util.Constants;
import util.Util;

public class Board {
    
    public Tile[][] boardTiles;
    public char[][] boardChars;
    
    public Board(){
        boardTiles=new Tile[Constants.NUM_OF_COLUMNS][Constants.NUM_OF_ROWS];
        boardChars=new char[Constants.NUM_OF_COLUMNS][Constants.NUM_OF_ROWS];
        for(int i=0;i<Constants.NUM_OF_COLUMNS;i++){
            for(int j=0;j<Constants.NUM_OF_ROWS;j++){
                boardTiles[i][j]=new Tile(i,j);
            }
        }
        refactorBoard();
    }
    
    public Board(Tile[][] board){
        boardTiles=board;
        boardChars=new char[Constants.NUM_OF_COLUMNS][Constants.NUM_OF_ROWS];
        refactorBoard();
    }
    
    public void refactorBoard(){
        for(int i=0;i<Constants.NUM_OF_COLUMNS;i++){
            if(boardChars[i][0]==Constants.WHITE_PAWN){
                boardTiles[i][0].piece = new Piece(Constants.WHITE_QUEEN,new int[]{i,0});
            }
            if(boardChars[i][7]==Constants.BLACK_PAWN){
                boardTiles[i][7].piece = new Piece(Constants.BLACK_QUEEN,new int[]{i,7});
            }
            for(int j=0;j<Constants.NUM_OF_ROWS;j++){
                boardChars[i][j]=boardTiles[i][j].getPieceChar();
            }
        }
    }
    
    public Tile getTile(int[] pos){
        return getTile(pos[0],pos[1]);
    }
    
    public Tile getTile(int i,int j){
        int[] tpos={i,j};
        for(Tile[] col:boardTiles){
            for(Tile row:col){
                if(Util.samePosition(row.position,tpos)){
                    return row;
                }
            }
        }
        return null;
    }
    
    public void move(Tile mTile,Piece mPiece){
        Tile pTile = getTile(mPiece.position);
        pTile.setIcon(null);
        pTile.piece=null;
        mTile.piece=mPiece;
        mPiece.position=Util.copyPosition(mTile.position);
        mTile.setIcon(mTile.piece.img);
        refactorBoard();
    }
    
    public Tile getKingTile(boolean white){
        char target=white?Constants.WHITE_KING:Constants.BLACK_KING;
        for(Tile[] col:boardTiles){
            for(Tile row:col){
                if(row.getPieceChar()==target){
                    return row;
                }
            }
        }
        return null;
    }
    
    public int[] getKingPosition(boolean white){
        char target=white?Constants.WHITE_KING:Constants.BLACK_KING;
        for(int i=0;i<Constants.NUM_OF_COLUMNS;i++){
            for (int j = 0; j < Constants.NUM_OF_ROWS; j++) {
                if(boardChars[i][j]==target){
                    return new int[]{i,j};
                }
            }
        }
        return null;
    }
    
    public int evaluateBoard(){
        refactorBoard();
        int eval=0;
        for(int i=0;i<Constants.NUM_OF_COLUMNS;i++){
            for (int j = 0; j < Constants.NUM_OF_ROWS; j++) {
             if(boardChars[i][j]!=Constants.EMPTY_CHAR){   
                    switch(boardChars[i][j]){
                        case Constants.WHITE_PAWN:
                            eval-=Constants.PAWN_VALUE;
                            break;
                        case Constants.WHITE_KING:
                            eval-=Constants.KING_VALUE;
                            break;
                        case Constants.WHITE_QUEEN:
                            eval-=Constants.QUEEN_VALUE;
                            break;
                         case Constants.WHITE_ROOK:
                            eval-=Constants.ROOK_VALUE;
                            break;
                        case Constants.WHITE_BISHOP:
                            eval-=Constants.BISHOP_VALUE;
                            break;
                        case Constants.WHITE_KNIGHT:
                            eval-=Constants.KNIGHT_VALUE;
                            break;
                        case Constants.BLACK_PAWN:
                            eval+=Constants.PAWN_VALUE;
                            break;
                        case Constants.BLACK_KING:
                            eval+=Constants.KING_VALUE;
                            break;
                        case Constants.BLACK_QUEEN:
                            eval+=Constants.QUEEN_VALUE;
                            break;
                         case Constants.BLACK_ROOK:
                            eval+=Constants.ROOK_VALUE;
                            break;
                        case Constants.BLACK_BISHOP:
                            eval+=Constants.BISHOP_VALUE;
                            break;
                        case Constants.BLACK_KNIGHT:
                            eval+=Constants.KNIGHT_VALUE;
                            break;
                    }
                }
            }
        }
        return eval;
    }
    
    public void printBoard(){
        System.out.println("Printing board :");
        for(int i=0;i<Constants.NUM_OF_ROWS;i++){
            for(int j=0;j<Constants.NUM_OF_ROWS;j++){
                System.out.print(boardChars[j][i]);
            }
            System.out.println("");
        }
        System.out.println("finished");
    }
    
}
