package game;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is used to handle the Level 3 start room.
 * @author Will Lumby
 * @version 1.0
 */
class Room_3_3 extends Room {

    private KeyboardHandler kh;
    private RenderWindow window;

    private Sprite backgroundRoom;
    private Font font = new Font();
    private Text wasd;

    private boolean doorLockedUp = true;

    private SpeedPlate[] speedPlate;

    /*
    Living entities are declared first here
     */
    private Player player;
    private GuardSword[] testGuard = new GuardSword[0];

    private Texture backgroundUnlockedRight = new Texture();

    private boolean plate1Triggered = false;
    private boolean plate2Triggered = false;
    private boolean plate3Triggered = false;

    /**
     * This is the constructor for the room
     * @param window The render window that GameArea is using
     * @param kh The keyboard handler that GameArea is using
     * @throws IOException IOException
     */
    Room_3_3(RenderWindow window, KeyboardHandler kh, Level level) throws IOException {
        super(window, kh, level);

        this.kh = kh;
        this.window = window;

        /*
        Background block
         */

        Path path = Paths.get("sprites/Room_v2_Door_Left_Door_Up_Locked.png");
        Path path2 = Paths.get("sprites/Room_v2_Door_Left_Door_Up.png");

        Texture background = new Texture();
        background.loadFromFile(path);
        backgroundUnlockedRight.loadFromFile(path2);

        backgroundRoom = new Sprite(background);
        backgroundRoom.setScale(1, 1);

        /*
        End background block
         */

        font.loadFromFile(Paths.get("sprites/Kavivanar-Regular.ttf"));
        wasd = new Text("", font, 64);
        wasd.setPosition(350, 200);


        /*
        Living entities block
         */

        player = new Player(window, 150, 300, testGuard, this);
        speedPlate = new SpeedPlate[3];
        speedPlate[0] = new SpeedPlate(window, player, 600, 175);
        speedPlate[1] = new SpeedPlate(window, player, 600, 325);
        speedPlate[2] = new SpeedPlate(window, player, 600, 475);

        /*
        End living entities block
         */
    }

    /**
     * This method is used when the player is in this room, the handle the rendering and interaction of elements
     * @throws IOException IOException
     */
    @Override
    void currentRoomLoop() throws IOException {
        while (window.isOpen() && (currentRoom)) {

            kh.checkLoop();
            window.clear();

            player.checkItem("Bow");

            player.move();

            /*
            Draw block
             */

            window.draw(backgroundRoom);

            window.draw(wasd);

            speedPlate[0].draw();
            speedPlate[1].draw();
            speedPlate[2].draw();

            player.draw();
            player.getPlayerHealth().draw();
            /*
            End draw block
             */

            window.display();

            handleKeyboard(kh, player);

            playerX = player.getXPos();
            playerY = player.getYPos();

            if (speedPlate[0].checkPlayer()) {
                plate1Triggered = true;
            }

            if (speedPlate[1].checkPlayer()) {
                plate2Triggered = true;
            }

            if (speedPlate[2].checkPlayer()) {
                plate3Triggered = true;
            }

            if (plate1Triggered && plate2Triggered && plate3Triggered) {
                unlockDoor("UP");
            }

            /*
            Room changing block
             */

            if ((playerX < 150) && (playerY < 400) && (playerY > 250)) {
                changeRoom("LEFT");
            }

            if ((playerX > 550) && (playerX < 700) && (playerY < 100)) {
                if (!doorLockedUp) {
                    changeRoom("UP");
                }
            }

            /*
            End room changing block
             */
        }
    }

    @Override
    Player getPlayer() {
        return player;
    }

    @Override
    void setPlayerXRelative(int x) {
        player.setXRelative(x);
        playerX += x;
    }

    @Override
    void setPlayerYRelative(int y) {
        player.setYRelative(y);
        playerY += y;
    }

    @Override
    void unlockDoor(String s) {
        if (s.equals("UP")) {
            doorLockedUp = false;
        }

        if (!doorLockedUp) {
            backgroundRoom.setTexture(backgroundUnlockedRight);
        }

    }
}
