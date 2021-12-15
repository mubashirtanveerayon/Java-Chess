package util;

public class Map {

    public static final int[] MID_RANKS = {3,4};

    public static final float[] BLACK_PAWN_MAP = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
                                                  0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
            -0.5f,-0.5f,-0.5f,-0.5f,-0.5f,-0.5f,-0.5f,-0.5f,
            0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,
                                                    0.4f,0.4f,0.45f,0.45f,0.45f,0.45f,0.4f,0.4f,
                                                    0.46f,0.46f,0.5f,0.5f,0.5f,0.5f,0.46f,0.46f,
                                                    0.55f,0.55f,0.55f,0.55f,0.55f,0.55f,0.55f,0.55f,
                                                    0.5f*Constants.PAWN_VALUE,0.5f*Constants.PAWN_VALUE,0.5f*Constants.PAWN_VALUE,0.5f*Constants.PAWN_VALUE,0.5f*Constants.PAWN_VALUE,0.5f*Constants.PAWN_VALUE,0.5f*Constants.PAWN_VALUE,0.5f*Constants.PAWN_VALUE};

    public static final float[] WHITE_PAWN_MAP = {0.5f*Constants.PAWN_VALUE,0.5f*Constants.PAWN_VALUE,0.5f*Constants.PAWN_VALUE,0.5f*Constants.PAWN_VALUE,0.5f*Constants.PAWN_VALUE,0.5f*Constants.PAWN_VALUE,0.5f*Constants.PAWN_VALUE,0.5f*Constants.PAWN_VALUE,
            0.55f,0.55f,0.55f,0.55f,0.55f,0.55f,0.55f,0.55f,
            0.46f,0.46f,0.5f,0.5f,0.5f,0.5f,0.46f,0.46f,
            0.4f,0.4f,0.45f,0.45f,0.45f,0.45f,0.4f,0.4f,
            0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,
            -0.5f,-0.5f,-0.5f,-0.5f,-0.5f,-0.5f,-0.5f,-0.5f,
            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,};

    public static final float[] WHITE_ROOK_MAP = {0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,
            1f,2.5f,2.5f,2.5f,2.5f,2.5f,2.5f,1.0f,
            0.5f,0.5f,0.55f,0.55f,0.55f,0.55f,0.5f,0.5f,
            0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,
            0.4f,0.45f,0.45f,0.45f,0.45f,0.45f,0.45f,0.4f,
            0.3f,0.4f,0.4f,0.4f,0.4f,0.4f,0.4f,0.3f,
            0.0f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.0f,
            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};

    public static final float[] BLACK_ROOK_MAP = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
            0.0f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.0f,
            0.3f,0.4f,0.4f,0.4f,0.4f,0.4f,0.4f,0.3f,
            0.4f,0.45f,0.45f,0.45f,0.45f,0.45f,0.45f,0.4f,
            0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,
            0.5f,0.5f,0.55f,0.55f,0.55f,0.55f,0.5f,0.5f,
            1f,2.5f,2.5f,2.5f,2.5f,2.5f,2.5f,1.0f,
            0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE};

//    public static final float[] WHITE_BISHOP_MAP = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
//            0.0f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.0f,
//            0.3f,0.4f,0.4f,0.4f,0.4f,0.4f,0.4f,0.3f,
//            0.4f,0.45f,0.45f,0.45f,0.45f,0.45f,0.45f,0.4f,
//            0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,
//            0.5f,0.5f,0.55f,0.55f,0.55f,0.55f,0.5f,0.5f,
//            1f,2.5f,2.5f,2.5f,2.5f,2.5f,2.5f,1.0f,
//            0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE};
//
//
//    public static final float[] BLACK_BISHOP_MAP = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
//            0.0f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.0f,
//            0.3f,0.4f,0.4f,0.4f,0.4f,0.4f,0.4f,0.3f,
//            0.4f,0.45f,0.45f,0.45f,0.45f,0.45f,0.45f,0.4f,
//            0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,
//            0.5f,0.5f,0.55f,0.55f,0.55f,0.55f,0.5f,0.5f,
//            1f,2.5f,2.5f,2.5f,2.5f,2.5f,2.5f,1.0f,
//            0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE,0.5f*Constants.ROOK_VALUE};

    public static final float[] WHITE_KING_MAP = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
                                            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
                                            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
                                            0.05f,0.05f,0.05f,0.05f,0.05f,0.05f,0.05f,0.05f,
                                            0.10f,0.10f,0.10f,0.10f,0.10f,0.10f,0.10f,0.10f,
                                            0.10f,0.10f,0.1f,0.10f,0.10f,0.10f,0.10f,0.10f,
                                            0.50f,0.30f,0.20f,0.15f,0.15f,0.20f,0.30f,0.50f,
                                            0.30f,1.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.30f};

    public static final float[] BLACK_KING_MAP = {0.30f,1.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.30f,
            0.50f,0.30f,0.20f,0.15f,0.15f,0.20f,0.30f,0.50f,
            0.10f,0.10f,0.10f,0.10f,0.10f,0.10f,0.10f,0.10f,
            0.10f,0.10f,0.10f,0.10f,0.10f,0.10f,0.10f,0.10f,
            0.05f,0.05f,0.05f,0.05f,0.05f,0.05f,0.05f,0.05f,
            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
            0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};



//    public static float getPositionalAdvantage(char pieceChar,int file,int rank){
//
//        switch(Util.toUpper(pieceChar)){
//            case Constants.WHITE_KING:
//                return 0.0f;
//            case Constants.WHITE_ROOK:
//            case Constants.WHITE_QUEEN:
//            case Constants.WHITE_KNIGHT:
//            case Constants.WHITE_BISHOP:
//            case Constants.WHITE_PAWN:
//                float pieceValue = Util.getValue(pieceChar);
//                if(Util.isUpperCase(pieceChar)){
//                    switch(rank){
//                        case 0:
//                            return pieceValue*0.425f;
//                        case 1:
//                            return pieceValue*0.4f;
//                        case 2:
//                            return pieceValue*0.375f;
//                        case 3:
//                            return pieceValue*0.35f;
//                        case 4:
//                            return pieceValue*0.325f;
//                        case 5:
//                            return pieceValue*0.3f;
//                        case 6:
//                            return pieceValue*0.275f;
//                        default:
//                            return 0.0f;
//                    }
//                }else{
//                    switch(rank){
//                        case 7:
//                            return pieceValue*0.425f;
//                        case 6:
//                            return pieceValue*0.4f;
//                        case 5:
//                            return pieceValue*0.375f;
//                        case 4:
//                            return pieceValue*0.35f;
//                        case 3:
//                            return pieceValue*0.325f;
//                        case 2:
//                            return pieceValue*0.3f;
//                        case 1:
//                            return pieceValue*0.275f;
//                        default:
//                            return 0.0f;
//                    }
//                }
//        }
//        return 0.0f;
//    }


    public static float[] WP_MAP = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
            5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f,
            1.0f, 1.0f, 2.0f, 3.0f, 3.0f, 2.0f, 1.0f, 1.0f,
            0.5f, 0.5f, 1.0f, 2.5f, 2.5f, 1.0f, 8.5f, 0.5f,
            0.0f, 0.0f, 0.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f,
            0.5f, -0.5f, -1.0f, 0.0f, 0.0f, -1.0f, -0.5f, 0.5f,
            0.5f, 1.0f, 1.0f, -2.0f, -2.0f, 1.0f, 1.0f, 0.5f,
            0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};

    public static float getPositionalAdvantage(char pieceChar,int file,int rank){
        switch(Util.toUpper(pieceChar)){
            case Constants.WHITE_KING:
                if(Util.isUpperCase(pieceChar)){
                    return WHITE_KING_MAP[rank*Constants.ROWS+file];
                }else{
                    return BLACK_KING_MAP[rank*Constants.ROWS+file];
                }
            case Constants.WHITE_PAWN:
                if(Util.isUpperCase(pieceChar)){
                    return WHITE_PAWN_MAP[rank*Constants.ROWS+file];
                }else{
                    return BLACK_PAWN_MAP[rank*Constants.ROWS+file];
                }
            case Constants.WHITE_ROOK:
            case Constants.WHITE_QUEEN:
            case Constants.WHITE_KNIGHT:
            case Constants.WHITE_BISHOP:
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


    public static float positionalAdvantage(char piece,int file,int rank){
        switch(piece){
            case Constants.WHITE_PAWN:{
                return Map.WHITE_PAWN_MAP[file+rank*Constants.ROWS];
            }case Constants.BLACK_PAWN:{
                return Map.BLACK_PAWN_MAP[file+rank*Constants.ROWS];
            }case Constants.WHITE_KING:{
                return Map.WHITE_KING_MAP[file+rank*Constants.ROWS];
            }case Constants.BLACK_KING:{
                return Map.BLACK_KING_MAP[file+rank*Constants.ROWS];
            }case Constants.WHITE_ROOK:{
                return Map.WHITE_ROOK_MAP[file+rank*Constants.ROWS];
            } case Constants.BLACK_ROOK:{
                return Map.BLACK_ROOK_MAP[file+rank*Constants.ROWS];
            }default:{
                float distFromCenterFile = Math.abs(file-3.5f);
                float distFromCenterRank = Math.abs(rank-3.5f);
                float distFromCenter = (float)Math.sqrt(distFromCenterFile*distFromCenterFile+distFromCenterRank*distFromCenterRank);
                return Util.getValue(piece)*(1.0f/distFromCenter);
            }
        }
    }

}
