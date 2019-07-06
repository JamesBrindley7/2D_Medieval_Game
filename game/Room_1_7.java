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
class Room_1_7 extends Room {

    private KeyboardHandler kh;
    private RenderWindow window;

    private Sprite backgroundRoom;

    private Font font = new Font();
    private Text wasd;

    /*
    Living entities are declared first here
     */
    private Player player;
    private GuardSword[] testGuard = new GuardSword[0];
    private HealthPickup healthPickup;
    private HealthPickup healthPickup2;

    /**
     * This is the constructor for the room
     * @param window The render window that GameArea is using
     * @param kh The keyboard handler that GameArea is using
     * @throws IOException IOException
     */
    Room_1_7(RenderWindow window, KeyboardHandler kh, Level level) throws IOException {
        super(window, kh, level);

        this.kh = kh;
        this.window = window;

        /*
        Background block
         */

        Path path = Paths.get("sprites/Room_v2_Door_Down_Door_Up.png");

        Texture background = new Texture();
        background.loadFromFile(path);
        backgroundRoom = new Sprite(background);
        backgroundRoom.setScale(1, 1);

        /*
        End background block
         */

        font.loadFromFile(Paths.get("sprites/Kavivanar-Regular.ttf"));
        wasd = new Text("Hearts replenish health.", font, 64);
        wasd.setPosition(350, 450);


        /*
        Living entities block
         */

        player = new Player(window, 600, 50, testGuard, this);
        healthPickup = new HealthPickup(window, player, 450, 400);
        healthPickup2 = new HealthPickup(window, player, 800, 400);

        /*
        End living entities block
         */
    }

    @Override
    Player getPlayer() {
        return player;
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


            player.move();

            if (!healthPickup.getPickedUp()){
                healthPickup.pickUp();
            }

            if (!healthPickup2.getPickedUp()){
                healthPickup2.pickUp();
            }

            for (int i = 0; i < testGuard.length; i++) {

                testGuard[i].primaryAttack();
                testGuard[i].checkAlive();
                testGuard[i].GuardsetPlayerCo(player.getXPos(), player.getYPos());
                testGuard[i].setBoundaries(150, width - 100, 90, height - 220);
            }

            player.checkItem("Bow");

            /*
            Draw block
             */

            window.draw(backgroundRoom);
            window.draw(wasd);

            if (!healthPickup.getPickedUp()){
                healthPickup.draw();
            }

            if (!healthPickup2.getPickedUp()){
                healthPickup2.draw();
            }

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

