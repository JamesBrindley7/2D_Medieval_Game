package game;
import org.jsfml.graphics.RenderWindow;

import java.io.IOException;


/**
 * This is the abstract class for rooms. All rooms in the game have their own class and feautures; this class contains
 * all the default variables and methods they will use.
 * @author Will Lumby
 * @version 1.0
 */
abstract class Room {

    public static final int width = 1280;
    public static final int height = 720;

    protected int playerX;
    protected int playerY;

    private RenderWindow window;
    private KeyboardHandler kh;
    private Menuingame ingamemenu;
    private Level level;
    private Player player;

    protected boolean currentRoom = false;

    protected int shiftPress = 0;
    protected int onePress = 0;
    protected int escapePress = 0;

    protected boolean doorLocked = true;

    protected Boss boss;
    protected MovableBox[] Box;
    

    /**
     * This is the constructor for this class. The only was this constructor should be envoked is through a super.
     * @param window The render window created by GameArea
     * @param kh The keyboard handler GameArea is using
     */
    Room(RenderWindow window, KeyboardHandler kh, Level level) {
        this.window = window;
        this.kh = kh;
        this.level = level;
    }

    /**
     * This method is used to change the current room when the player walks through a door
     * @param s The direction of room to travel to
     */
    void changeRoom(String s) throws IOException {
        switch (s) {
            case "UP":
                level.changeRoom("UP");
                break;
            case "DOWN":
                level.changeRoom("DOWN");
                break;
            case "LEFT":
                level.changeRoom("LEFT");
                break;
            case "RIGHT":
                level.changeRoom("RIGHT");
                break;
        }
    }

    /**
     * This method is used to change whether this room is the current room that the player is in
     * @param b Whether the player is currently in this room
     * @throws IOException IOException
     */
    void setCurrentRoom(boolean b) throws IOException {
        currentRoom = b;

        if (currentRoom) {
            currentRoomLoop();
        }
    }

    void currentRoomLoop() throws IOException {
        /*
        Used by individual room for their processes
         */
    }

    void endcredits() {
        /*
        Used by individual room for their processes
         */
    }

    /**
     * This method is used to check if this is the current player room
     * @return Whether the player is currently in this room
     */
    boolean getCurrentRoom() {
        return currentRoom;
    }

    void setPlayerXRelative(int x) {
        /*
        Used by individual room for their processes
         */
    }

    Player getPlayer() {
        return player;
    }

    void dead() throws IOException {
        new DeadMenu(window, kh);
    }

    void setPlayerYRelative(int y) {
        /*
        Used by individual room for their processes
         */
    }

    void unlockDoor(String s) {
        /*
        Used by individual room for their processes
         */
    }

    Level getLevel() {
        return level;
    }

    void unlockDoor() {
    	/*
        Used by individual room for their processes
         */
    }

    void damagePlayer(int i) throws IOException {
        /*
        Used by individual room for their processes
         */
    }

    Boss getBoss() {
        return boss;
    }

    MovableBox[] getBox() {
        return Box;
    }

    void handleKeyboard(KeyboardHandler kh, Player player) throws IOException {

        /*
        Keyboard handling block
        */

        if (kh.wPressed()) {
            if (player.checkCollisionHorizWall(90) == 1) {
                player.setDeltaY(-7);
            }
            else {
                player.setDeltaY(0);
            }
        }
        if (kh.sPressed()) {
            if (player.checkCollisionHorizWall(height-220) == 0) {
                player.setDeltaY(7);
            }
            else {
                player.setDeltaY(0);
            }
        }
        if (kh.aPressed()) {
            if (player.checkCollisionVerticalWall(150) == 1) {
                player.setDeltaX(-7);
            }
            else {
                player.setDeltaX(0);
            }
        }
        if (kh.dPressed()) {
            if (player.checkCollisionVerticalWall(width-200) == 0) {
                player.setDeltaX(7);
            }
            else {
                player.setDeltaX(0);
            }
        }

        if (kh.shiftPressed()) {
            shiftPress = 1;
        }
        else {
            if (shiftPress == 1) {

                player.meleeAttack();
            }
            shiftPress = 0;
        }

        if (kh.onePressed()) {
            onePress = 1;
        }
        else {
            if (onePress == 1) {

                player.shootArrow();
            }
            onePress = 0;
        }

        if (!kh.wPressed() && !kh.sPressed()) {
            player.setDeltaY(0);
        }

        if (!kh.aPressed() && !kh.dPressed()) {
            player.setDeltaX(0);
        }

        if (kh.escapePressed()) {
            escapePress = 1;
        }
        else {
            if (escapePress == 1) {
            	ingamemenu = new Menuingame(window, kh, level.getLevelNum());
            }
            escapePress = 0;
        }

        /*
        End keyboard handling block
        */
    }
}
