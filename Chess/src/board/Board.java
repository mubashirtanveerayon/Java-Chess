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
        refactorBoardChars();
    }
    
    public Board(Tile[][] board){
        boardTiles=board;
        boardChars=new char[Constants.NUM_OF_COLUMNS][Constants.NUM_OF_ROWS];
        refactorBoardChars();
    }
    
    public void refactorBoardChars(){
        for(int i=0;i<Constants.NUM_OF_COLUMNS;i++){
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
        refactorBoardChars();
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
    
}
