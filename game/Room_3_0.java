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
class Room_3_0 extends Room {

    private KeyboardHandler kh;
    private RenderWindow window;

    private Sprite backgroundRoom;
    private Font font = new Font();
    private Text wasd;

    private boolean doorLockedRight = true;
    private boolean doorLockedLeft = true;

    /*
    Living entities are declared first here
     */
    private Player player;
    private GuardSword[] testGuard = new GuardSword[0];

    private Texture backgroundUnlockedRight = new Texture();

    private FadeIn fadeIn;

    /**
     * This is the constructor for the room
     * @param window The render window that GameArea is using
     * @param kh The keyboard handler that GameArea is using
     * @throws IOException IOException
     */
    Room_3_0(RenderWindow window, KeyboardHandler kh, Level level) throws IOException {
        super(window, kh, level);

        this.kh = kh;
        this.window = window;

        /*
        Background block
         */

        Path path = Paths.get("sprites/Room_v2_Door_Down.png");

        Texture background = new Texture();
        background.loadFromFile(path);

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

        player = new Player(window, 600, 300, testGuard, this);

        /*
        End living entities block
         */

        fadeIn = new FadeIn(window, "in", "BLUE");
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

            player.draw();
            player.getPlayerHealth().draw();
            /*
            End draw block
             */

            fadeIn.draw();
            window.display();

            handleKeyboard(kh, player);

            playerX = player.getXPos();
            playerY = player.getYPos();

            /*
            Room changing block
             */

            if ((playerX > 550) && (playerX < 700) && (playerY > 450)) {
                changeRoom("DOWN");
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
        if (s.equals("RIGHT")) {
            doorLockedRight = false;
        }

        if (!doorLockedRight) {
            backgroundRoom.setTexture(backgroundUnlockedRight);
        }

    }
}
