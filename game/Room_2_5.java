package game;

import org.jsfml.graphics.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is used to handle the Level 2_4 room
 * @author Will Lumby
 * @version 1.0
 */
class Room_2_5 extends Room {

    private KeyboardHandler kh;
    private RenderWindow window;

    private Sprite backgroundRoom;

    private Font font = new Font();
    private Text wasd;
    private boolean doorLockedLeft = true;

    /*
    Living entities are declared first here
     */
    private Player player;
    private GuardSword[] testGuard = new GuardSword[2];

    private Texture backgroundUnlockedTex;

    /**
     * This is the constructor for the room
     * @param window The render window that GameArea is using
     * @param kh The keyboard handler that GameArea is using
     * @throws IOException IOException
     */
    Room_2_5(RenderWindow window, KeyboardHandler kh, Level level) throws IOException {
        super(window, kh, level);

        this.kh = kh;
        this.window = window;

        /*
        Background block
         */

        Path path = Paths.get("sprites/Room_v2_Door_Down_Door_Up_Door_Right_Locked.png");
        Path path2 = Paths.get("sprites/Room_v2_Door_Down_Door_Up_Door_Right_Unlocked.png");

        Texture background = new Texture();
        backgroundUnlockedTex = new Texture();
        background.loadFromFile(path);
        backgroundUnlockedTex.loadFromFile(path2);
        backgroundRoom = new Sprite(background);
        backgroundRoom.setScale(1, 1);

        /*
        End background block
         */

        font.loadFromFile(Paths.get("sprites/Kavivanar-Regular.ttf"));
        wasd = new Text("", font, 64);
        wasd.setPosition(300, 450);


        /*
        Living entities block
         */

        testGuard[0] = new GuardSword(window, 200, 200, this);
        testGuard[1] = new GuardSword(window, 1000, 470, this);
        player = new Player(window, 600, 500, testGuard, this);

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

    /**
     * This method is used when the player is in this room, the handle the rendering and interaction of elements
     * @throws IOException IOException
     */
    @Override
    void currentRoomLoop() throws IOException {
        while (window.isOpen() && (currentRoom)) {

            kh.checkLoop();
            window.clear();

            testGuard[0].primaryAttack();
            testGuard[1].primaryAttack();
            player.move();

            for (int i = 0; i < testGuard.length; i++) {
                testGuard[i].checkAlive();
                testGuard[i].GuardsetPlayerCo(player.getXPos(), player.getYPos());
                testGuard[i].setBoundaries(150, width - 100, 90, height - 220);
            }
            testGuard[0].patrolBox(200, 1000, 200, 201);
            testGuard[1].patrolBox(200, 1000, 470, 471);

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
                changeRoom("UP");
            }

            if ((playerX > 550) && (playerX < 700) && (playerY > 450)) {
                changeRoom("DOWN");
            }

            if ((playerX < 150) && (playerY < 400) && (playerY > 250)) {
                if (!(doorLockedLeft)) {
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
        if (s.equals("LEFT")) {
            doorLockedLeft = false;
        }

        if (!doorLockedLeft) {
            backgroundRoom.setTexture(backgroundUnlockedTex);
        }

    }
}

