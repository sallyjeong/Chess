package chessproject;


public class Constants {
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 6000;
    public static final String USERNAME_ERROR = "error. invalid username";
    public static final String JOIN_ROOM_ERROR = "error. invalid room"; //for private room
    public static final String QUICK_MATCH_WAIT = "waiting to be matched";
    public static final char CHAT_DATA = '1';
    public static final char MOVE_DATA = '2';
    public static final char USERNAME_DATA = '3';
    public static final char JOIN_PRIV_ROOM_DATA = '4';//for private room
    public static final char CREATE_ROOM_DATA = '5';//for private room
    public static final char QUICK_MATCH_DATA = '6';//for public room
    public static final char JOIN_PUB_ROOM_DATA = '7';//for private room
    public static final char COLOUR_DATA = '8';

}