package chessproject;

/** [Constants.java]
 * Used to store Constant variables that are shared among various classes
 * @author Katherine Liu, Sally Jeong
 * @version 1.0 Jan 25, 2021
 */
public class Constants {
    // networking
    public static final String HOST = "127.0.0.1"; //35.183.5.5
    public static final int PORT = 6000;

    // responses
    public static final String USERNAME_ERROR = "error. invalid username";
    public static final String JOIN_ROOM_ERROR = "error. invalid room";
    public static final String QUICK_MATCH_WAIT = "waiting to be matched";
    public static final String QUICK_MATCH_JOINED = "joined game";
    public static final String REQUEST = "!request";
    public static final String DONE = "!done";

    // data types
    public static final char CHAT_DATA = 'a';
    public static final char MOVE_DATA = 'b';
    public static final char USERNAME_DATA = 'c';
    public static final char CREATE_ROOM_DATA = 'd';
    public static final char JOIN_PRIV_ROOM_DATA = 'e';
    public static final char QUICK_MATCH_DATA = 'f';
    public static final char JOIN_PUB_ROOM_DATA = 'g';
    public static final char COLOUR_DATA = 'h';
    public static final char BOARD_DATA = 'i';
    public static final char LEAVE_ROOM_DATA = 'j';
    public static final char QUIT_DATA = 'k';
    public static final char ROOM_NAMES_DATA = 'l';
    public static final char UPDATE_LIST = 'm';
    public static final char START_DATA = 'n';
    public static final char DRAW_DATA = 'o';
    public static final char GAME_OVER_DATA = 'p';

    // game and move symbols
    public static final String PAWN_INDICATOR = "P";
    public static final String CHECK = "+";
    public static final String CHECKMATE = "#";
    public static final String PROMOTE = "=";
    public static final String MOVE = "-";
    public static final String CAPTURE = "x";
    public static final String CASTLE_1 = "O-O";
    public static final String CASTLE_2 = "O-O-O";
}