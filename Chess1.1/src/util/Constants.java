package util;

import java.awt.Color;
import java.io.File;
import javax.swing.ImageIcon;

public class Constants {

    public static final int NUM_OF_ROWS = 8;

    public static final int NUM_OF_COLUMNS = 8;
    
    public static final Color WHITE_PIECE_COLOR=Color.white;

    public static final Color BLACK_PIECE_COLOR=Color.black;
    
    public static final char WHITE = 'w';
    
    public static final char BLACK = 'b';
    
    public static final char WHITE_PAWN = 'P';

    public static final char WHITE_KNIGHT = 'N';

    public static final char WHITE_BISHOP = 'B';

    public static final char WHITE_ROOK = 'R';

    public static final char WHITE_QUEEN = 'Q';

    public static final char WHITE_KING = 'K';

    public static final char BLACK_PAWN = 'p';

    public static final char BLACK_KNIGHT = 'n';

    public static final char BLACK_BISHOP = 'b';

    public static final char BLACK_ROOK = 'r';

    public static final char BLACK_QUEEN = 'q';

    public static final char BLACK_KING = 'k';
    
    public static final char EMPTY_CHAR = ' ';

    public static final String CAPTURE_AUDIO_PATH = "src"+File.separator+"assets"+File.separator+"audio"+File.separator+"cap.wav";

    public static final String WHITE_IMG_PATH="src"+File.separator+"assets"+File.separator+"white"+File.separator;

    public static final String BLACK_IMG_PATH="src"+File.separator+"assets"+File.separator+"black"+File.separator;

    public static final String AUDIO_PATH="src"+File.separator+"assets"+File.separator+"audio"+File.separator+"move.wav";

    public static final int PAWN_VALUE = 200;

    public static final int KNIGHT_VALUE = 300;

    public static final int BISHOP_VALUE = 300;

    public static final int ROOK_VALUE = 500;

    public static final int QUEEN_VALUE = 900;
    
    public static final int KING_VALUE = 100;
    
    public static final int NULL_VALUE = 0;

    public static final int CHECK_VALUE = 5000;
    
    public static final int CHECKMATE_VALUE = Integer.MAX_VALUE;
    
    public static final String FILES = "abcdefgh";
    
    public static final String RANKS = "87654321";
    
    public static final  String STARTING_FEN="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    
    public static final String[] PIECE_NAMES={"pawn", "PAWN", "bishop", "BISHOP", "night", "NIGHT", "rook", "ROOK", "queen", "QUEEN", "king", "KING"};
    
    public static final int[][] QUEEN_OFFSET= {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}, {-1, 0}, {1, 0}, {0, 1}, {0, -1}};

    public static final int[][] BISHOP_OFFSET= {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}};

    public static final int[][] ROOK_OFFSET= {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};

    public static final int[][] KING_OFFSET= {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}, {0, 1}, {0, -1}, {-1, 0}, {1, 0}};

    public static final int[][] KNIGHT_OFFSET= {{2, -1}, {1, -2}, {1, 2}, {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},{2,1}};

    public static final int[][] BLACK_PAWN_OFFSET= {{-1,1},{1,1},{0,1}};

    public static final int[][] WHITE_PAWN_OFFSET= {{-1,-1},{1,-1},{0,-1}};
    
    public static final int[] WHITE_QUEEN_SIDE_CASTLING = {2,7};

    public static final int[] BLACK_QUEEN_SIDE_CASTLING = {2,0};
    
    public static final int[] WHITE_ROOK_SIDE_CASTLING = {6,7};

    public static final int[] BLACK_ROOK_SIDE_CASTLING = {6,0};
    
    public static final int[][] WHITE_CASTLING_MOVES = {{2,7},{3,7},{6,7},{5,7}};
    
    public static final int[][] BLACK_CASTLING_MOVES = {{2,0},{3,0},{6,0},{5,0}};
    
}
