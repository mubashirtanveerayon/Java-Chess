package util;

public class Map {

    public static final int[] MID_RANKS = {3,4};

    public static final float[] WHITE_PAWN_MAP = {Constants.PAWN_VALUE,Constants.PAWN_VALUE,Constants.PAWN_VALUE,Constants.PAWN_VALUE,Constants.PAWN_VALUE,Constants.PAWN_VALUE,Constants.PAWN_VALUE,Constants.PAWN_VALUE,
                                                  1.75f,1.75f,1.75f,1.75f,1.75f,1.75f,1.75f,1.75f,
                                                  1.55f,1.55f,1.55f,1.55f,1.55f,1.55f,1.55f,1.55f,
                                                  1.25f,1.25f,1.25f,1.25f,1.25f,1.25f,1.25f,1.25f,
                                                  1.15f,1.15f,1.15f,1.15f,1.15f,1.15f,1.15f,1.15f,
                                                  1.05f,1.05f,1.05f,1.05f,1.05f,1.05f,1.05f,1.05f,
                                                  0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
                                                  0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,};
    public static final float[] BLACK_PAWN_MAP = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
                                                  0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
                                                  1.05f,1.05f,1.05f,1.05f,1.05f,1.05f,1.05f,1.05f,
                                                  1.15f,1.15f,1.15f,1.15f,1.15f,1.15f,1.15f,1.15f,
                                                  1.25f,1.25f,1.25f,1.25f,1.25f,1.25f,1.25f,1.25f,
                                                  1.55f,1.55f,1.55f,1.55f,1.55f,1.55f,1.55f,1.55f,
                                                  1.75f,1.75f,1.75f,1.75f,1.75f,1.75f,1.75f,1.75f,
                                                  Constants.PAWN_VALUE,Constants.PAWN_VALUE,Constants.PAWN_VALUE,Constants.PAWN_VALUE,Constants.PAWN_VALUE,Constants.PAWN_VALUE,Constants.PAWN_VALUE,Constants.PAWN_VALUE};
    public static final float[] KING_MAP = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
                                            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
                                            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
                                            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
                                            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
                                            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
                                            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
                                            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};


    public static float getPositionalAdvantage(char pieceChar,int file,int rank){
        switch(Util.toUpper(pieceChar)){
            case Constants.WHITE_KING:
                return 0.0f;
            case Constants.WHITE_ROOK:
            case Constants.WHITE_QUEEN:
            case Constants.WHITE_KNIGHT:
            case Constants.WHITE_BISHOP:
            case Constants.WHITE_PAWN:
                float pieceValue = Util.getValue(pieceChar);
                if(Util.isUpperCase(pieceChar)){
                    switch(rank){
                        case 0:
                            return pieceValue*0.325f;
                        case 1:
                            return pieceValue*0.3f;
                        case 2:
                            return pieceValue*0.275f;
                        case 3:
                            return pieceValue*0.25f;
                        case 4:
                            return pieceValue*0.225f;
                        case 5:
                            return pieceValue*0.2f;
                        case 6:
                            return pieceValue*0.175f;
                        default:
                            return 0.0f;
                    }
                }else{
                    switch(rank){
                        case 7:
                            return pieceValue*0.325f;
                        case 6:
                            return pieceValue*0.3f;
                        case 5:
                            return pieceValue*0.275f;
                        case 4:
                            return pieceValue*0.25f;
                        case 3:
                            return pieceValue*0.225f;
                        case 2:
                            return pieceValue*0.2f;
                        case 1:
                            return pieceValue*0.175f;
                        default:
                            return 0.0f;
                    }
                }
        }
        return 0.0f;
    }

}
