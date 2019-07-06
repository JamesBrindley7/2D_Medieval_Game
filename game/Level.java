package game;

import org.jsfml.graphics.RenderWindow;
import java.io.IOException;

abstract class Level {
    static boolean playerOnLevel = false;
    static String levelName = "_UNNAMED_";

    protected int playerRoom = 0;

    protected RenderWindow window;
    protected KeyboardHandler kh;

    protected int levelNum;

    public SoundStream bgm;
    public SoundStream boss;

    Level(RenderWindow window, KeyboardHandler kh) throws IOException {
        this.window = window;
        this.kh = kh;
    }

    int getLevelNum() {
        return levelNum;
    }

    void changeRoom(String s) throws IOException {
        /*
        Level specific
         */
    }

    void unlockDoor(int roomNum, String direction) throws IOException {
        /*
        Level specific
         */
    }

    void setItem(String s, boolean b) {
        /*
        Level specific
         */
    }

    void changeLevel() throws IOException {
        /*
        Level specific
         */
    }

    boolean getItem(String s) {
        return false;
    }

    Room getRoom(int i) {
        return null;
    }

    int getPlayerRoom() {
        return playerRoom;
    }
}
