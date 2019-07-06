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
class Room_1_2 extends Room {

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
    private PressurePlate pressurePlate;

    /**
     * This is the constructor for the room
     * @param window The render window that GameArea is using
     * @param kh The keyboard handler that GameArea is using
     * @throws IOException IOException
     */
    Room_1_2(RenderWindow window, KeyboardHandler kh, Level level) throws IOException {
        super(window, kh, level);

        this.kh = kh;
        this.window = window;

        Box = new MovableBox[1];

        /*
        Background block
         */

        Path path = Paths.get("sprites/Room_v2_Door_Left_Locked_Up.png");
        Path pathUnlocked = Paths.get("sprites/Room_v2_Door_Left_Door_Up.png");

        Texture background = new Texture();
        background.loadFromFile(path);

        backgroundUnlockedTex.loadFromFile(pathUnlocked);

        backgroundRoom = new Sprite(background);
        backgroundRoom.setScale(1, 1);

        /*
        End background block
         */

        font.loadFromFile(Paths.get("sprites/Kavivanar-Regular.ttf"));
        wasd = new Text("Boxes can be pushed.", font, 64);
        wasd.setPosition(350, 450);


        /*
        Living entities block
         */

        player = new Player(window, 600, 150, testGuard, this);

        Box[0] = new MovableBox(window, 300, 200, 80, 80, player);
        pressurePlate = new PressurePlate(window, player, 1000, 450, Box[0], this);

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

            Box[0].boxcollision();
            player.move();

            player.checkItem("Bow");

            /*
            Draw block
             */

            window.draw(backgroundRoom);
            pressurePlate.draw();
            window.draw(wasd);
            player.draw();
            Box[0].draw();
            player.getPlayerHealth().draw();


            /*
            End draw block
             */

            window.display();

            /*
            Keyboard handling block
             */

            handleKeyboard(kh, player);

            boolean check = Box[0].checkpush();
            if (check) {
                Box[0].push();
            }

            if (kh.ePressed()) {
                check = false;
                check = Box[0].checkpull();
                if (check) {
                    Box[0].pull();
                }
            }

            /*
            End keyboard handling block
             */

            playerX = player.getXPos();
            playerY = player.getYPos();

            pressurePlate.boxCheck();

            /*
            Room changing block
             */

            if ((playerX > 550) && (playerX < 700) && (playerY < 100)) {
                changeRoom("UP");
            }
            if ((playerX < 150) && (playerY < 400) && (playerY > 250)) {
                if (!(doorLocked)) {
                    changeRoom("LEFT");
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

