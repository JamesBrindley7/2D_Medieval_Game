package game;

import org.jsfml.graphics.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is used to handle the Level 2_1 room
 * @author Will Lumby
 * @version 1.0
 */
class Room_3_6 extends Room {

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
    private GuardSword[] testGuard = new GuardSword[1];
    private PressurePlate pressurePlate;

    Texture background = new Texture();
    Texture backgroundUnlocked = new Texture();

    /**
     * This is the constructor for the room
     * @param window The render window that GameArea is using
     * @param kh The keyboard handler that GameArea is using
     * @throws IOException IOException
     */
    Room_3_6(RenderWindow window, KeyboardHandler kh, Level level) throws IOException {
        super(window, kh, level);

        this.kh = kh;
        this.window = window;

        /*
        Background block
         */

        Path path = Paths.get("sprites/Room_v2_Door_Right_Locked_Door_Down.png");
        Path path2 = Paths.get("sprites/Room_v2_Door_Right_Door_Down.png");


        background.loadFromFile(path);
        backgroundUnlocked.loadFromFile(path2);

        backgroundRoom = new Sprite(background);
        backgroundRoom.setScale(1, 1);

        /*
        End background block
         */

        font.loadFromFile(Paths.get("sprites/Kavivanar-Regular.ttf"));
        wasd = new Text("", font, 64);
        wasd.setPosition(250, 450);


        /*
        Living entities block
         */

        testGuard[0] = new GuardSword(window, 600, 200, this);
        player = new Player(window, 600, 500, testGuard, this);

        Box = new MovableBox[1];
        Box[0] = new MovableBox(window, 300, 200, 80, 80, player);
        pressurePlate = new PressurePlate(window, player, 1000, 150, Box[0], this, 6, "RIGHT");

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

            testGuard[0].primaryAttack();

            Box[0].boxcollision();
            player.move();
            testGuard[0].checkAlive();
            testGuard[0].patrolBox(400, 800, 200, 450);
            testGuard[0].GuardsetPlayerCo(player.getXPos(), player.getYPos());
            testGuard[0].setBoundaries(150, width - 100, 90, height - 220);

            player.checkItem("Bow");

            /*
            Draw block
             */

            window.draw(backgroundRoom);
            window.draw(wasd);

            pressurePlate.draw();
            Box[0].draw();
            testGuard[0].UpdateCone();
            testGuard[0].draw();
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

            playerX = player.getXPos();
            playerY = player.getYPos();

            pressurePlate.boxCheck();

            /*
            Room changing block
             */

            if ((playerX > 550) && (playerX < 700) && (playerY > 450)) {
                changeRoom("DOWN");
            }

            if ((playerX > 1050) && (playerY < 400) && (playerY > 250)) {
                if (!doorLocked) {
                    changeRoom("RIGHT");
                }
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
        if (s.equals("RIGHT")) {
            doorLocked = false;
        }

        if (!doorLocked) {
            backgroundRoom.setTexture(backgroundUnlocked);
        }

    }
}

