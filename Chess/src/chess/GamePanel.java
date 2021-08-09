package chess;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

    Tile[][] tile;
    Board board;
    boolean selected;
    Tile selectedTile;
    boolean whiteToMove;

    public GamePanel(Board board_) {
        super(new GridLayout(Constants.NUM_OF_COLUMNS, Constants.NUM_OF_ROWS));
        board = board_;
        tile = board.getTiles();
        for (int i = 0; i < Constants.NUM_OF_COLUMNS; i++) {
            for (int j = 0; j < Constants.NUM_OF_ROWS; j++) {
                add(tile[j][i]);
                tile[j][i].addActionListener(this);
            }
        }
        selected = false;
        whiteToMove = true;
        renderBoard();
    }

    public void renderBoard() {
        for (int i = 0; i < Constants.NUM_OF_COLUMNS; i++) {
            for (int j = 0; j < Constants.NUM_OF_ROWS; j++) {
                tile[i][j].showPiece();
            }
        }
        tile = board.getTiles();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < Constants.NUM_OF_COLUMNS; i++) {
            for (int j = 0; j < Constants.NUM_OF_ROWS; j++) {
                if (e.getSource() == tile[i][j]) {
                    if (selected) {
                        if (!selectedTile.isEmpty()) {
                            Piece piece = selectedTile.getPiece();
                            if (whiteToMove && piece.getColor() == Constants.WHITE_PIECE_COLOR) {
                                if (board.isLegal(piece, tile[i][j])) {
                                    board.move(piece, tile[i][j]);
                                    whiteToMove=false;
                                }else{
                                    System.out.println("Not a legal move!");
                                }
                            }else if(!whiteToMove&&piece.getColor() == Constants.BLACK_PIECE_COLOR){
                                if (board.isLegal(piece, tile[i][j])) {
                                    board.move(piece, tile[i][j]);
                                    whiteToMove = true;
                                }else{
                                    System.out.println("Not a legal move!");
                                }
                            }
                        }
                    } else {
                        selectedTile = tile[i][j];
                    }
                    selected = !selected;
                }
            }
        }
        renderBoard();
    }

}
