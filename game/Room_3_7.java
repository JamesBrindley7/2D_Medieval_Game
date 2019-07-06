package game;

import org.jsfml.graphics.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is used to handle the Level 1_1 room
 * @author Will Lumby
 * @version 1.0
 */
class Room_3_7 extends Room {

    private KeyboardHandler kh;
    private RenderWindow window;

    private Sprite backgroundRoom;

    private Texture backgroundUnlockedTex = new Texture();

    private Font font = new Font();
    private Text wasd;

    /*
    Living entities are declared first here
     */
    private Player player;
    private GuardSword testGuard[] = new GuardSword[0];
    private ArrowTarget arrowTarget1;
    private ArrowTarget arrowTarget2;

    /**
     * This is the constructor for the room
     * @param window The render window that GameArea is using
     * @param kh The keyboard handler that GameArea is using
     * @throws IOException IOException
     */
    Room_3_7(RenderWindow window, KeyboardHandler kh, Level level) throws IOException {
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
        backgroundUnlockedTex.loadFromFile(path2);

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

        player = new Player(window, 200, 300, testGuard, this);
        arrowTarget1 = new ArrowTarget(window, player, 1000, 250);
        arrowTarget2 = new ArrowTarget(window, player, 1000, 400);

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

            player.move();

            player.checkItem("Bow");

            /*
            Draw block
             */

            window.draw(backgroundRoom);
            window.draw(wasd);

            arrowTarget1.draw();
            arrowTarget2.draw();
            player.draw();
            player.getPlayerHealth().draw();

            /*
            End draw block
             */

            window.display();

            if (arrowTarget1.getTriggered() && arrowTarget2.getTriggered()) {
                getLevel().unlockDoor(7, "UP");
            }

            /*
            Keyboard handling block
             */

            handleKeyboard(kh, player);

            /*
            End keyboard handling block
             */

            playerX = player.getXPos();
            playerY = player.getYPos();

            /*
            Room changing block
             */

            if ((playerX < 150) && (playerY < 400) && (playerY > 250)) {
                changeRoom("LEFT");
            }

            if ((playerX > 550) && (playerX < 700) && (playerY < 100)) {
                if (!doorLocked) {
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

    void setPlayerXRelative(int x) {
        player.setXRelative(x);
        playerX += x;
    }

    void setPlayerYRelative(int y) {
        player.setYRelative(y);
        playerY += y;
    }

    @Override
    void unlockDoor(String s) {
        doorLocked = false;
        backgroundRoom.setTexture(backgroundUnlockedTex);
    }
}

