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
class Room_1_1 extends Room {

    private KeyboardHandler kh;
    private RenderWindow window;

    private Sprite backgroundRoom;

    private Font font = new Font();
    private Text wasd;

    /*
    Living entities are declared first here
     */
    private Player player;
    private GuardSword[] testGuard = new GuardSword[1];
    private Key key;

    /**
     * This is the constructor for the room
     * @param window The render window that GameArea is using
     * @param kh The keyboard handler that GameArea is using
     * @throws IOException IOException
     */
    Room_1_1(RenderWindow window, KeyboardHandler kh, Level level) throws IOException {
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
        wasd = new Text("Use LSHIFT to attack.", font, 64);
        wasd.setPosition(350, 450);


        /*
        Living entities block
         */

        testGuard[0] = new GuardSword(window, 600, 200, this);
        player = new Player(window, 1000, 300, testGuard, this);
        key = new Key(window, player, 600, 350, this, 0, "RIGHT");

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

            player.move();
            testGuard[0].checkAlive();
            testGuard[0].patrolBox(400, 800, 200, 450);
            testGuard[0].GuardsetPlayerCo(player.getXPos(), player.getYPos());
            testGuard[0].setBoundaries(150, width - 100, 90, height - 220);

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

    void setPlayerXRelative(int x) {
        player.setXRelative(x);
        playerX += x;
    }

    void setPlayerYRelative(int y) {
        player.setYRelative(y);
        playerY += y;
    }
}

