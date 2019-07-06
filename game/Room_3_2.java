package game;

import org.jsfml.graphics.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is used to handle the Level 3_1 room
 * @author Will Lumby
 * @version 1.0
 */
class Room_3_2 extends Room {

    private KeyboardHandler kh;
    private RenderWindow window;

    private Sprite backgroundRoom;

    private Font font = new Font();
    private Text wasd;
    private boolean doorLockedUp = true;

    /*
    Living entities are declared first here
     */
    private Player player;
    private GuardSword[] testGuard = new GuardSword[3];


    Texture background = new Texture();
    Texture backgroundUnlocked = new Texture();

    /**
     * This is the constructor for the room
     * @param window The render window that GameArea is using
     * @param kh The keyboard handler that GameArea is using
     * @throws IOException IOException
     */
    Room_3_2(RenderWindow window, KeyboardHandler kh, Level level) throws IOException {
        super(window, kh, level);

        this.kh = kh;
        this.window = window;

        Box = new MovableBox[0];

        /*
        Background block
         */

        Path path = Paths.get("sprites/Room_v2_Door_Down_Left_Right_Up_Locked.png");
        Path path2 = Paths.get("sprites/Room_v2_Door_Down_Left_Right_Up_Unlocked.png");


        background.loadFromFile(path);
        backgroundUnlocked.loadFromFile(path2);

        backgroundRoom = new Sprite(background);
        backgroundRoom.setScale(1, 1);

        /*
        End background block
         */

        font.loadFromFile(Paths.get("sprites/Kavivanar-Regular.ttf"));
        wasd = new Text("", font, 64);
        wasd.setPosition(350, 450);


        /*
        Living entities block
         */

        testGuard[0] = new GuardSword(window, 250, 500, this);
        testGuard[1] = new GuardSword(window, 500, 500, this);
        testGuard[2] = new GuardSword(window, 750, 200, this);
        player = new Player(window, 150, 300, testGuard, this);

        /*
        End living entities block
         */
    }

    @Override
    void damagePlayer(int i) throws IOException {
        if (player.getPlayerHealth().getHealth() == 1)
        {
            getLevel().bgm.stop();
            getLevel().boss.stop();
        }

        player.getPlayerHealth().hurt();
    }

    @Override
    Player getPlayer() {
        return player;
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
            for (int i = 0; i < testGuard.length; i++) {
                testGuard[i].checkAlive();
                testGuard[i].setBoundaries(150, width - 100, 90, height - 220);
                testGuard[i].GuardsetPlayerCo(player.getXPos(), player.getYPos());
                testGuard[i].boxcCollision();
                testGuard[i].primaryAttack();
            }

            testGuard[0].patrolBox(250, 251, 200, 500);
            testGuard[1].patrolBox(500, 501, 200, 500);
            testGuard[2].patrolBox(750, 751, 200, 500);

            player.checkItem("Bow");

            /*
            Draw block
             */

            window.draw(backgroundRoom);
            window.draw(wasd);

            for (int i = 0; i < testGuard.length; i++) {
                testGuard[i].UpdateCone();
                testGuard[i].draw();
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

            if ((playerX > 550) && (playerX < 700) && (playerY < 100)) {
                if (!doorLockedUp) {
                    changeRoom("UP");
                }
            }

            if ((playerX > 1050) && (playerY < 400) && (playerY > 250)) {
                changeRoom("RIGHT");
            }

            if ((playerX < 150) && (playerY < 400) && (playerY > 250)) {
                changeRoom("LEFT");
            }

            /*
            End room changing block
             */
        }
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
        if (s.equals("UP")) {
            doorLockedUp = false;
        }

        if (!doorLockedUp) {
            backgroundRoom.setTexture(backgroundUnlocked);
        }

    }
}

