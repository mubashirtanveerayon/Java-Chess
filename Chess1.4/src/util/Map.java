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


    public static final float getPositionalAdvantage(char pieceChar,int file,int rank){
        switch(Util.toUpper(pieceChar)){
            case Constants.WHITE_KING:
                return 0.0f;
            case Constants.WHITE_ROOK:
            case Constants.WHITE_QUEEN:
            case Constants.WHITE_KNIGHT:
            case Constants.WHITE_BISHOP:
                float pieceValue = Util.getValue(pieceChar);
                if(Util.isUpperCase(pieceChar)){
                    switch(rank){
                        case 0:
                            return pieceValue;
                        case 1:
                            return pieceValue*0.875f;
                        case 2:
                            return pieceValue*0.775f;
                        case 3:
                            return pieceValue*0.675f;
                        case 4:
                            return pieceValue*0.575f;
                        case 5:
                            return pieceValue*0.475f;
                        case 6:
                            return pieceValue*0.375f;
                        default:
                            return 0.0f;
                    }
                }else{
                    switch(rank){
                        case 7:
                            return pieceValue;
                        case 6:
                            return pieceValue*0.875f;
                        case 5:
                            return pieceValue*0.775f;
                        case 4:
                            return pieceValue*0.675f;
                        case 3:
                            return pieceValue*0.575f;
                        case 2:
                            return pieceValue*0.475f;
                        case 1:
                            return pieceValue*0.375f;
                        default:
                            return 0.0f;
                    }
                }

            default:
               if(Util.isUpperCase(pieceChar)){
                   return WHITE_PAWN_MAP[file+rank*Constants.COLUMNS];
               }else{
                   return BLACK_PAWN_MAP[file+rank*Constants.COLUMNS];
               }
        }
    }

}
