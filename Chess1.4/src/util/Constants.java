package util;

import java.io.File;

public class Constants {

    public static final float PARTIAL_VALUE = 0.5f;

    public static final String[] PIECE_NAMES={"pawn", "PAWN", "bishop", "BISHOP", "night", "NIGHT", "rook", "ROOK", "queen", "QUEEN", "king", "KING"};

    public static final String WHITE_PIECE_CHAR = "PRNBQK";

    public static final String WHITE_IMG_PATH="src"+File.separator+"assets"+File.separator+"white"+File.separator;

    public static final String BLACK_IMG_PATH="src"+File.separator+"assets"+File.separator+"black"+File.separator;

    public static final String AUDIO_PATH="src"+File.separator+"assets"+File.separator+"audio"+File.separator+"sound.wav";

    public static final String VERSION = "1.4";

    public static int SEARCH_DEPTH = 4;

    public static final int ROWS = 8;

    public static final int COLUMNS = 8;

    public static final char WHITE = 'w';

    public static final char BLACK = 'b';

    public static final char WHITE_PAWN = 'P';

    public static final char WHITE_KNIGHT = 'N';

    public static final char WHITE_BISHOP = 'B';

    public static final char WHITE_ROOK = 'R';

    public static final char WHITE_QUEEN = 'Q';

    public static final char WHITE_KING = 'K';

    public static final char BLACK_PAWN = 'p';

    public static final char BLACK_ROOK = 'r';

    public static final char BLACK_QUEEN = 'q';

    public static final char BLACK_KING = 'k';

    public static final char EMPTY_CHAR = ' ';

    public static final float PAWN_VALUE = 2.0f;

    public static final float KNIGHT_VALUE = 3.0f;

    public static final float BISHOP_VALUE = 3.5f;

    public static final float ROOK_VALUE = 5.5f;

    public static final float QUEEN_VALUE = 10.0f;

    public static final float KING_VALUE = 1.0f;

    public static final String FILES = "abcdefgh";

    public static final String RANKS = "87654321";
                                                             // 2 3 4 5
    public static final  String STARTING_FEN="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static final int[][] QUEEN_OFFSET= {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}, {-1, 0}, {1, 0}, {0, 1}, {0, -1}};

    public static final int[][] BISHOP_OFFSET= {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}};

    public static final int[][] ROOK_OFFSET= {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};

    public static final int[][] KING_OFFSET= {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}, {0, 1}, {0, -1}, {-1, 0}, {1, 0}};

    public static final int[][] KNIGHT_OFFSET= {{2, -1}, {1, -2}, {1, 2}, {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},{2,1}};

    public static final int[][] BLACK_PAWN_OFFSET= {{-1,1},{1,1},{0,1}};

    public static final int[][] WHITE_PAWN_OFFSET= {{-1,-1},{1,-1},{0,-1}};

}
