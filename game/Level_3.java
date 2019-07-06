package game;

import org.jsfml.graphics.RenderWindow;
import java.io.IOException;

class Level_3 extends Level {

    private Room[] rooms = new Room[10];
    private boolean playerHasBow = true;

    Level_3(RenderWindow window, KeyboardHandler kh) throws IOException {
        super(window, kh);
        levelNum = 3;

        /*
        Rooms must be declared here first and added to the level array
         */
        rooms[0] = new Room_3_0(window, kh, this);
        rooms[1] = new Room_3_1(window, kh, this);
        rooms[2] = new Room_3_2(window, kh, this);
        rooms[3] = new Room_3_3(window, kh, this);
        rooms[4] = new Room_3_4(window, kh, this);
        rooms[5] = new Room_3_5(window, kh, this);
        rooms[6] = new Room_3_6(window, kh, this);
        rooms[7] = new Room_3_7(window, kh, this);
        rooms[8] = new Room_3_8(window, kh, this);
        rooms[9] = new Room_3_9(window, kh, this);

        bgm = new SoundStream(window,"bgm");
        boss = new SoundStream(window, "finalboss");
        bgm.stop();
        bgm.looping();
        bgm.play();

        playerRoom = 0;
        rooms[0].setCurrentRoom(true);
    }

    @Override
    void changeRoom(String s) throws IOException {

        SoundStream step = new SoundStream(window,"step");
        step.play();

        switch (playerRoom) {
            case 0:
                switch (s) {
                    case "DOWN":
                        playerRoom = 1;
                        rooms[0].setCurrentRoom(false);
                        rooms[1].setPlayerYRelative(50);
                        rooms[1].setCurrentRoom(true);
                        break;
                }
                break;
            case 1:
                switch (s) {
                    case "RIGHT":
                        playerRoom = 2;
                        rooms[1].setCurrentRoom(false);
                        rooms[2].setPlayerXRelative(50);
                        rooms[2].setCurrentRoom(true);
                        break;
                    case "UP":
                        playerRoom = 0;
                        rooms[1].setCurrentRoom(false);
                        rooms[0].setPlayerYRelative(-50);
                        rooms[0].setCurrentRoom(true);
                        break;
                }
                break;
            case 2:
                switch (s) {
                    case "UP":
                        playerRoom = 5;
                        rooms[2].setCurrentRoom(false);
                        rooms[5].setPlayerYRelative(-50);
                        rooms[5].setCurrentRoom(true);
                        break;
                    case "LEFT":
                        playerRoom = 1;
                        rooms[2].setCurrentRoom(false);
                        rooms[1].setPlayerXRelative(-50);
                        rooms[1].setCurrentRoom(true);
                        break;
                    case "RIGHT":
                        playerRoom = 3;
                        rooms[2].setCurrentRoom(false);
                        rooms[3].setPlayerXRelative(50);
                        rooms[3].setCurrentRoom(true);
                        break;
                }
                break;
            case 3:
                switch (s) {
                    case "UP":
                        playerRoom = 4;
                        rooms[3].setCurrentRoom(false);
                        rooms[4].setPlayerYRelative(-50);
                        rooms[4].setCurrentRoom(true);
                        break;
                    case "LEFT":
                        playerRoom = 2;
                        rooms[3].setCurrentRoom(false);
                        rooms[2].setPlayerXRelative(-50);
                        rooms[2].setCurrentRoom(true);
                        break;
                }
                break;
            case 4:
                switch (s) {
                    case "DOWN":
                        playerRoom = 3;
                        rooms[4].setCurrentRoom(false);
                        rooms[3].setPlayerYRelative(50);
                        rooms[3].setCurrentRoom(true);
                        break;
                }
                break;
            case 5:
                switch (s) {
                    case "UP":
                        playerRoom = 6;
                        rooms[5].setCurrentRoom(false);
                        rooms[6].setPlayerYRelative(-50);
                        rooms[6].setCurrentRoom(true);
                        break;
                    case "DOWN":
                        playerRoom = 2;
                        rooms[5].setCurrentRoom(false);
                        rooms[2].setPlayerYRelative(50);
                        rooms[2].setCurrentRoom(true);
                        break;
                }
                break;
            case 6:
                switch (s) {
                    case "DOWN":
                        playerRoom = 5;
                        rooms[6].setCurrentRoom(false);
                        rooms[5].setPlayerYRelative(50);
                        rooms[5].setCurrentRoom(true);
                        break;
                    case "RIGHT":
                        playerRoom = 7;
                        rooms[6].setCurrentRoom(false);
                        rooms[7].setPlayerXRelative(50);
                        rooms[7].setCurrentRoom(true);
                        break;
                }
                break;
            case 7:
                switch (s) {
                    case "LEFT":
                        playerRoom = 6;
                        rooms[7].setCurrentRoom(false);
                        rooms[6].setPlayerXRelative(50);
                        rooms[6].setCurrentRoom(true);
                        break;
                    case "UP":
                        playerRoom = 8;
                        rooms[7].setCurrentRoom(false);
                        rooms[8].setPlayerYRelative(-50);
                        rooms[8].setCurrentRoom(true);
                        break;
                }
                break;
            case 8:
                switch (s) {
                    case "DOWN":
                        playerRoom = 7;
                        rooms[8].setCurrentRoom(false);
                        rooms[7].setPlayerYRelative(50);
                        rooms[7].setCurrentRoom(true);
                        break;
                    case "RIGHT":
                        playerRoom = 9;
                        bgm.stop();
                        boss.play();
                        boss.looping();
                        rooms[8].setCurrentRoom(false);
                        rooms[9].setPlayerXRelative(50);
                        rooms[9].setCurrentRoom(true);
                        break;
                }
                break;
        }
    }

    @Override
    void unlockDoor(int roomNum, String direction) throws IOException {
        rooms[roomNum].unlockDoor(direction);
    }

    @Override
    void setItem(String s, boolean b) {
        if (s.equals("Bow")) {
            playerHasBow = b;
        }
    }

    @Override
    boolean getItem(String s) {
        if (s.equals("Bow")) {
            return playerHasBow;
        }
        else return false;
    }

    @Override
    void changeLevel() throws IOException {
        rooms[9].endcredits();
    }

    @Override
    Room getRoom(int i) {
        return rooms[i];
    }
}
