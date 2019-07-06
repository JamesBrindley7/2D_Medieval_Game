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
class Room_1_3 extends Room {

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
    private GuardSword[] testGuard = new GuardSword[0];
    private Key key;

    /**
     * This is the constructor for the room
     * @param window The render window that GameArea is using
     * @param kh The keyboard handler that GameArea is using
     * @throws IOException IOException
     */
    Room_1_3(RenderWindow window, KeyboardHandler kh, Level level) throws IOException {
        super(window, kh, level);

        this.kh = kh;
        this.window = window;

        /*
        Background block
         */

        Path path = Paths.get("sprites/Room_v2_Door_Right.png");

        Texture background = new Texture();
        background.loadFromFile(path);

        backgroundRoom = new Sprite(background);
        backgroundRoom.setScale(1, 1);

        /*
        End background block
         */

        font.loadFromFile(Paths.get("sprites/Kavivanar-Regular.ttf"));
        wasd = new Text("Keys unlock doors.", font, 64);
        wasd.setPosition(350, 200);


        /*
        Living entities block
         */

        player = new Player(window, 1000, 300, testGuard, this);
        key = new Key(window, player, 600, 400, this, 0, "LEFT");

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

            if (!key.getPickedUp()){
                key.pickUp();
            }

            /*
            Draw block
             */

            window.draw(backgroundRoom);
            window.draw(wasd);

            if (!key.getPickedUp()){
                key.draw();
            }

            player.draw();
            player.getPlayerHealth().draw();

            /*
            End draw block
             */

            window.display();

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

            if ((playerX > 1050) && (playerY < 400) && (playerY > 250)) {
                changeRoom("RIGHT");
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

