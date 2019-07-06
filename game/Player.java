package game;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the class used to handle the player stats, animations, and drawing.
 * @author Will Lumby
 * @version 1.0
 */

class Player {
    private RenderWindow window;
    private Room room;

    private Sprite playerSprite;
    private Sprite slashSprite;

    private int deltaX = 0;
    private int deltaY = 0;
    private int xPosition = 480;
    private int yPosition = 400;
    private String direction = "DOWN";
    private boolean slashVisible = false;

    //Player gameplay stats
    private static final int meleeRange = 120;
    private boolean hasBow = false;
    private static PlayerHealth playerHealth;
    private boolean canShoot = true;
    private boolean canMelee = true;

    private Texture player_W = new Texture();
    private Texture player_E = new Texture();
    private Texture player_S = new Texture();
    private Texture player_N = new Texture();

    private Texture slash = new Texture();
    private Texture slash_down = new Texture();

    private AI[] guardArray;
    private Arrow[] arrows = new Arrow[100];

    private int arrowsNum = 0;

    private SoundStream splat;
    private SoundStream attacksound;

    /**
     * The constructor for the player class
     * @param window The game window to add the player to
     * @throws IOException IOException
     */
    Player(RenderWindow window, int startX, int startY, AI[] testGuard, Room room) throws IOException {

        this.window = window;
        this.room = room;

        splat = new SoundStream(window, "splat");
        attacksound = new SoundStream(window, "attacking");

        guardArray = testGuard;

        xPosition = startX;
        yPosition = startY;

        playerHealth = new PlayerHealth(window, room);

        player_W.loadFromFile(Paths.get("sprites/Player_W.png"));
        player_E.loadFromFile(Paths.get("sprites/Player_E.png"));
        player_S.loadFromFile(Paths.get("sprites/Player_S.png"));
        player_N.loadFromFile(Paths.get("sprites/Player_N.png"));

        slash.loadFromFile(Paths.get("sprites/Sword_Slash.png"));
        slash_down.loadFromFile(Paths.get("sprites/Sword_Slash_Down.png"));

        //Create a sprite for the player and change the scale
        playerSprite = new Sprite(player_S);
        playerSprite.setScale(5, 5);

        slashSprite = new Sprite(slash);
        slashSprite.setScale(7, 7);

         if (room.getLevel().getItem("Bow")) {
             hasBow = true;
         }

         System.out.println("Player has bow?: " + hasBow);

        //Move the player to default position
        playerSprite.move(xPosition, yPosition);
    }

    /**
     * This method is used to draw the player sprite.
     */
    void draw() {
        window.draw(playerSprite);
        if (slashVisible) {
            window.draw(slashSprite);
        }
        for (int i=0;i<arrowsNum;i++) {
            arrows[i].move();
            arrows[i].draw();
        }
    }

    void destroyHealth() {
        playerHealth = null;
    }

    /**
     * This method is used to move the player and update its co-ordinates
     */
    void move() {
        playerSprite.move(deltaX, deltaY);
        slashSprite.move(deltaX, deltaY);
        xPosition += deltaX;
        yPosition += deltaY;
    }

    /**
     * This method is used to change the sprite for the player to a specified one.
     * @param s The specified player sprite.
     * @throws IOException IOException
     */
    private void changeSprite(String s) throws IOException {
        switch (s) {
            case "LEFT":
                playerSprite.setTexture(player_W);
                setDirection("LEFT");
                break;
            case "RIGHT":
                playerSprite.setTexture(player_E);
                setDirection("RIGHT");
                break;
            case "UP":
                playerSprite.setTexture(player_N);
                setDirection("UP");
                break;
            case "DOWN":
                playerSprite.setTexture(player_S);
                setDirection("DOWN");
                break;
            default:
                playerSprite.setTexture(player_S);
                setDirection("DOWN");
                break;
        }
    }

    /**
     * This method is used to change the xspeed of the player (defined at deltaX - rate of change of position)
     * @param deltaX xspeed of the player
     * @throws IOException IOException
     */
    void setDeltaX(int deltaX) throws IOException {
        this.deltaX = deltaX;

        if (this.deltaX > 0) {
            changeSprite("RIGHT");
        }
        else if (this.deltaX < 0) {
            changeSprite("LEFT");
        }
    }

    /**
     * This method is used to change the yspeed of the player (defined at deltaY - rate of change of position)
     * @param deltaY yspeed of the player
     * @throws IOException IOException
     */
    void setDeltaY(int deltaY) throws IOException {
        this.deltaY = deltaY;

        if (this.deltaY > 0) {
            changeSprite("DOWN");
        }
        else if (this.deltaY < 0) {
            changeSprite("UP");
        }
    }

    /**
     * This method is used for checking if the player is colliding with a co-ordinate. Uses scale and size to find centre of player.
     * @param x The X Co-ordinate of the location to check
     * @param y The Y Co-ordinate of the location to check
     * @return Whether the player is colliding or not
     */
    boolean checkCollisionObject(int x, int y) {
        return ( (getXPos()+ 0.5 * playerSprite.getTexture().getSize().x * playerSprite.getScale().x <= (x+50))
                && (getXPos()+ 0.5 * playerSprite.getTexture().getSize().x * playerSprite.getScale().x >= (x-50))
                && (getYPos()+ 0.5 * playerSprite.getTexture().getSize().y * playerSprite.getScale().y <= (y+50))
                && (getYPos()+ 0.5 * playerSprite.getTexture().getSize().y * playerSprite.getScale().y >= (y-50)) );
    }

    /**
     * This method is used to check collisions for vertical screen length walls
     * @param xLimit The x co ord of the wall
     * @return Which side of the wall the player is on
     */
    int checkCollisionVerticalWall(int xLimit) {
        if (getXPos() > (xLimit - 10)) {
            return 1;
        }
        else  {
            return 0;
        }
    }

    /**
     * This method is used to check collisions for horizontal screen length walls
     * @param yLimit The y co ord of the wall
     * @return Which side of the wall the player is on
     */
    int checkCollisionHorizWall(int yLimit) {
        if (getYPos() > (yLimit - 10)) {
            return 1;
        }
        else  {
            return 0;
        }
    }

    void meleeAttack() {
        if (canMelee) {
            for (int i = 0; i < guardArray.length; i++) {
                AI testGuard = guardArray[i];
                int guardXPos = testGuard.getStats().getXPos();
                int guardYPos = testGuard.getStats().getYPos();

                System.out.println("x " + guardXPos);
                System.out.println("y " + guardYPos);

                switch (direction) {
                    case "UP":
                        System.out.println("PLAYER ATTACK UP");
                        if ((guardXPos > xPosition - 50) && (guardXPos < xPosition + 50) && (guardYPos < yPosition) && (guardYPos > yPosition - meleeRange)) {
                            System.out.println("PLAYER ATTACK HIT");
                            testGuard.getStats().takeDamage(40);
                            testGuard.damageFlash();
                            testGuard.getStats().setChase();
                            splat.stop();
                            attacksound.stop();
                            splat.play();
                        } else {
                            splat.stop();
                            attacksound.stop();
                            attacksound.play();
                        }
                        break;
                    case "RIGHT":
                        System.out.println("PLAYER ATTACK RIGHT");
                        slashSprite.setPosition(xPosition + 50, yPosition);
                        slashSprite.setScale(7, 7);
                        slashSprite.setTexture(slash);
                        slashVisible = true;
                        slashTimer();
                        if ((guardXPos < xPosition + meleeRange) && (guardXPos > xPosition) && (guardYPos > yPosition - 50) && (guardYPos < yPosition + 50)) {
                            System.out.println("PLAYER ATTACK HIT");
                            testGuard.getStats().takeDamage(40);
                            testGuard.damageFlash();
                            testGuard.getStats().setChase();
                            splat.stop();
                            attacksound.stop();
                            splat.play();
                        } else {
                            splat.stop();
                            attacksound.stop();
                            attacksound.play();
                        }
                        break;
                    case "LEFT":
                        System.out.println("PLAYER ATTACK LEFT");
                        slashSprite.setPosition(xPosition + 10, yPosition);
                        slashSprite.setScale(-7, 7);
                        slashSprite.setTexture(slash);
                        slashVisible = true;
                        slashTimer();
                        if ((guardXPos > xPosition - meleeRange) && (guardXPos < xPosition) && (guardYPos > yPosition - 50) && (guardYPos < yPosition + 50)) {
                            System.out.println("PLAYER ATTACK HIT");
                            testGuard.getStats().takeDamage(40);
                            testGuard.damageFlash();
                            testGuard.getStats().setChase();
                            splat.stop();
                            attacksound.stop();
                            splat.play();
                        } else {
                            splat.stop();
                            attacksound.stop();
                            attacksound.play();
                        }
                        break;
                    case "DOWN":
                        System.out.println("PLAYER ATTACK DOWN");
                        slashSprite.setPosition(xPosition - 30, yPosition + 60 + deltaY);
                        slashSprite.setScale(7, 7);
                        slashSprite.setTexture(slash_down);
                        slashVisible = true;
                        slashTimer();
                        if ((guardXPos > xPosition - 50) && (guardXPos < xPosition + 50) && (guardYPos > yPosition) && (guardYPos < yPosition + meleeRange)) {
                            System.out.println("PLAYER ATTACK HIT");
                            testGuard.getStats().takeDamage(40);
                            testGuard.damageFlash();
                            testGuard.getStats().setChase();
                            splat.stop();
                            attacksound.stop();
                            splat.play();
                        } else {
                            splat.stop();
                            attacksound.stop();
                            attacksound.play();
                        }
                        break;
                    default:
                        break;
                }
            }
            canMelee = false;
            meleeTimer();
        }
    }

    void shootArrow() throws IOException {
        if ((hasBow) && (canShoot)) {
            arrows[arrowsNum] = new Arrow(window, this, 20, room);
            arrowsNum++;
            canShoot = false;
            shootTimer();
        }
    }

    int getArrowsNum() {
        return arrowsNum;
    }

    private void setDirection(String s) {
        direction = s;
    }

    String getDirection() {
        return direction;
    }

    /**
     * This method is used to get the current x position of the player.
     * @return Player Xposition
     */
    int getXPos() {
        return xPosition;
    }

    /**
     * This method is used to get the current y position of the player.
     * @return Player Yposition
     */
    int getYPos() {
        return yPosition;
    }

    void setXRelative(int x) {
        playerSprite.move(x, 0);
        xPosition += x;
    }

    void setYRelative(int y) {
        playerSprite.move(0, y);
        yPosition += y;
    }

    private void slashTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                slashVisible = false;
            }
        }, 150);
    }

    private void shootTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                canShoot = true;
            }
        }, 500);
    }

    private void meleeTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                canMelee = true;
            }
        }, 500);
    }

    void checkItem(String s) {
        if (s.equals("Bow")) {
            if (room.getLevel().getItem("Bow")) {
                hasBow = true;
            }
            else {
                hasBow = false;
            }
}
    }

    void knockback() {
        switch (direction) {
            case "UP":
                deltaY = 20;
                break;
            case "DOWN":
                deltaY = -20;
                break;
            case "LEFT":
                deltaX = 20;
                break;
            case "RIGHT":
                deltaX = -20;
                break;
        }
        playerSprite.setColor(new Color(255, 0, 0, 255));
        move();
        draw();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                deltaX = deltaX * 2;
                deltaY = deltaY * 2;
                move();
                draw();
            }
        }, 10);

        Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                playerSprite.setColor(new Color(255, 255, 255, 255));
            }
        }, 200);
    }

    PlayerHealth getPlayerHealth() {
        return playerHealth;
    }

    Arrow[] getArrows() {
        return arrows;
    }

    AI[] getGuardArray() {
        return guardArray;
    }
}