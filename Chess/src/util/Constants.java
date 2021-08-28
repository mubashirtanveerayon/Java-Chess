package util;

import java.awt.Color;
import java.io.File;
import javax.swing.ImageIcon;

public class Constants {

    public static final int NUM_OF_ROWS = 8;

    public static final int NUM_OF_COLUMNS = 8;
    
    public static final Color WHITE_PIECE_COLOR=Color.white;

    public static final Color BLACK_PIECE_COLOR=Color.black;

    public static final boolean WHITE = true;

    public static final boolean BLACK = false;

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
    
    public static final String WHITE_IMG_PATH="images"+File.separator+"white"+File.separator;

    public static final String BLACK_IMG_PATH="images"+File.separator+"black"+File.separator;

    public static final String WHITE_PAWN_IMG_PATH = "src"+File.separator+"images"+File.separator+"white"+File.separator+"pawn.png";

    public static final String WHITE_ROOK_IMG_PATH = "src"+File.separator+"images"+File.separator+"white"+File.separator+"rook.png";

    public static final String WHITE_KNIGHT_IMG_PATH = "src"+File.separator+"images"+File.separator+"white"+File.separator+"knight.png";

    public static final String WHITE_BISHOP_IMG_PATH = "src"+File.separator+"images"+File.separator+"white"+File.separator+"bishop.png";

    public static final String WHITE_QUEEN_IMG_PATH = "src"+File.separator+"images"+File.separator+"white"+File.separator+"queen.png";

    public static final String WHITE_KING_IMG_PATH = "src"+File.separator+"images"+File.separator+"white"+File.separator+"king.png";

    public static final String BLACK_PAWN_IMG_PATH = "src"+File.separator+"images"+File.separator+"black"+File.separator+"pawn.png";

    public static final String BLACK_ROOK_IMG_PATH = "src"+File.separator+"images"+File.separator+"black"+File.separator+"rook.png";

    public static final String BLACK_KNIGHT_IMG_PATH = "src"+File.separator+"images"+File.separator+"black"+File.separator+"knight.png";

    public static final String BLACK_BISHOP_IMG_PATH = "src"+File.separator+"images"+File.separator+"black"+File.separator+"bishop.png";

    public static final String BLACK_QUEEN_IMG_PATH = "src"+File.separator+"images"+File.separator+"black"+File.separator+"queen.png";

    public static final String BLACK_KING_IMG_PATH = "src"+File.separator+"images"+File.separator+"black"+File.separator+"king.png";

    public static final ImageIcon WHITE_PAWN_IMG = new ImageIcon("src"+File.separator+"images"+File.separator+"white"+File.separator+"pawn.png");

    public static final ImageIcon WHITE_ROOK_IMG = new ImageIcon("src"+File.separator+"images"+File.separator+"white"+File.separator+"rook.png");

    public static final ImageIcon WHITE_KNIGHT_IMG = new ImageIcon("src"+File.separator+"images"+File.separator+"white"+File.separator+"knight.png");

    public static final ImageIcon WHITE_BISHOP_IMG = new ImageIcon("src"+File.separator+"images"+File.separator+"white"+File.separator+"bishop.png");

    public static final ImageIcon WHITE_QUEEN_IMG= new ImageIcon("src"+File.separator+"images"+File.separator+"white"+File.separator+"queen.png");

    public static final ImageIcon WHITE_KING_IMG= new ImageIcon("src"+File.separator+"images"+File.separator+"white"+File.separator+"king.png");

    public static final ImageIcon BLACK_PAWN_IMG = new ImageIcon("src"+File.separator+"images"+File.separator+"black"+File.separator+"pawn.png");

    public static final ImageIcon BLACK_ROOK_IMG = new ImageIcon("src"+File.separator+"images"+File.separator+"black"+File.separator+"rook.png");

    public static final ImageIcon BLACK_KNIGHT_IMG = new ImageIcon("src"+File.separator+"images"+File.separator+"black"+File.separator+"knight.png");

    public static final ImageIcon BLACK_BISHOP_IMG = new ImageIcon("src"+File.separator+"images"+File.separator+"black"+File.separator+"bishop.png");

    public static final ImageIcon BLACK_QUEEN_IMG = new ImageIcon("src"+File.separator+"images"+File.separator+"black"+File.separator+"queen.png");

    public static final ImageIcon BLACK_KING_IMG = new ImageIcon("src"+File.separator+"images"+File.separator+"black"+File.separator+"king.png");
    
    public static final double PAWN_VALUE = 2.0D;

    public static final double KNIGHT_VALUE = 3.0D;

    public static final double BISHOP_VALUE = 3.0D;

    public static final double ROOK_VALUE = 5.0D;

    public static final double QUEEN_VALUE = 9.0D;
    
    public static final double KING_VALUE = 1.0D;
    
    public static final double NULL_VALUE = 0D;

    public static final double CHECK_VALUE = 50.0D;
    
    public static final double CHECKMATE_VALUE = Double.MAX_VALUE;
    
    public static final  String STARTING_FEN="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    
    public static final String[] PIECE_NAMES={"pawn", "PAWN", "bishop", "BISHOP", "night", "NIGHT", "rook", "ROOK", "queen", "QUEEN", "king", "KING"};
    
    public static final int[][] QUEEN_OFFSET= {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}, {-1, 0}, {1, 0}, {0, 1}, {0, -1}};

    public static final int[][] BISHOP_OFFSET= {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}};

    public static final int[][] ROOK_OFFSET= {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};

    public static final int[][] KING_OFFSET= {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}, {-1, 0}, {1, 0}, {0, 1}, {0, -1}};

    public static final int[][] KNIGHT_OFFSET= {{2, -1}, {1, -2}, {1, 2}, {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},{2,1}};

    public static final int[][] BLACK_PAWN_OFFSET= {{-1,1},{1,1},{0,1},{0,2}};

    public static final int[][] WHITE_PAWN_OFFSET= {{-1,-1},{1,-1},{0,-1},{0,-2}};
}
