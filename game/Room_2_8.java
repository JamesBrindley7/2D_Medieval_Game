package game;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is used to handle the Level 1_1 room
 * @author Will Lumby
 * @version 1.0
 */
class Room_2_8 extends Room {

    private KeyboardHandler kh;
    private RenderWindow window;

    private Sprite backgroundRoom;
    private Text bossName;
    private Font font = new Font();

    /*
    Living entities are declared first here
     */
    private Player player;
    private Boss2 boss;
    private GuardSword[] testGuard;
    private LevelPortal levelPortal;
    private RectangleShape bossHealth;

    /**
     * This is the constructor for the room
     * @param window The render window that GameArea is using
     * @param kh The keyboard handler that GameArea is using
     * @throws IOException IOException
     */
    Room_2_8(RenderWindow window, KeyboardHandler kh, Level level) throws IOException {
        super(window, kh, level);

        this.kh = kh;
        this.window = window;

        /*
        Background block
         */

        Path path = Paths.get("sprites/Room_Door_Locked_Up.png");

        Texture background = new Texture();
        background.loadFromFile(path);
        backgroundRoom = new Sprite(background);
        backgroundRoom.setScale(1, 1);

        /*
        End background block
         */

        font.loadFromFile(Paths.get("sprites/Kavivanar-Regular.ttf"));
        bossName = new Text("Shadowmancer", font, 48);
        bossName.setPosition(250, 620);

        /*
        Living entities block
         */

        testGuard = new GuardSword[0];
        player = new Player(window, 600, 50, testGuard, this);
        boss = new Boss2(window, 600, 500, this);
        levelPortal = new LevelPortal(window, player, 600, 400, this);

        bossHealth = new RectangleShape(new Vector2f(boss.getStats().getHealth(), 25));
        bossHealth.setFillColor(Color.RED);
        bossHealth.setPosition(200, 650);

        boss.movetopostion(player.getXPos(), player.getYPos());

        /*
        End living entities block
         */
    }

    @Override
    void damagePlayer(int i) throws IOException {
        if (player.getPlayerHealth().getHealth() == 1) {
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

            if (boss.checkRandomAttack() == 0) {
                boss.standWatch(2);
            }
            else {
                boss.randomAttack();
            }

            player.move();
            boss.checkAlive();
            boss.setBoundaries(150, width - 100, 90, height - 220);
            boss.GuardsetPlayerCo(player.getXPos(), player.getYPos());



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
            bossHealth.setSize(new Vector2f(boss.getStats().getHealth()*2, 25));
            if (boss.getStats().getHealth() > 0) {
                window.draw(bossHealth);
                window.draw(bossName);
            }
            else {
            	boss.wipevairables();
            }


            boss.UpdateCone();
            if (boss.getdrawcircle() == 1) {
                window.draw(boss.drawcircle());

            }
            else if (boss.getdrawcone() == 1) {
                window.draw(boss.drawattackcone());
            }

            boss.draw();
            player.draw();
            player.getPlayerHealth().draw();

            if (!boss.getStats().getAlive()) {
                levelPortal.draw();
                levelPortal.pickUp();
            }


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
    Boss getBoss() {
        return boss;
    }
}

