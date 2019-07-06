package game;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;

import java.io.IOException;
import java.nio.file.Paths;

class Arrow {
    private RenderWindow window;
    private Player player;

    private Sprite Arrow;

    private Texture arrow_L = new Texture();
    private Texture arrow_R = new Texture();
    private Texture arrow_U = new Texture();
    private Texture arrow_D = new Texture();

    private int x = 0;
    private int y = 0;
    private String direction;
    private int speed = 10;

    private Room room;

    private boolean alive = true;

    private SoundStream splat = new SoundStream(window, "splat");

    Arrow(RenderWindow window, Player player, int speed, Room room) throws IOException {

        this.window = window;
        this.player = player;
        this.direction = player.getDirection();
        this.speed = speed;
        this.room = room;

        x = player.getXPos();
        y = player.getYPos();

        arrow_L.loadFromFile(Paths.get("sprites/Arrow_Left.png"));
        arrow_R.loadFromFile(Paths.get("sprites/Arrow_Right.png"));
        arrow_U.loadFromFile(Paths.get("sprites/Arrow_Up.png"));
        arrow_D.loadFromFile(Paths.get("sprites/Arrow_Down.png"));

        Arrow = new Sprite(arrow_U);
        Arrow.setScale(1, 1);

        if (player.getDirection().equals("DOWN")) {
            Arrow.setTexture(arrow_D);
        }
        if (player.getDirection().equals("UP")) {
            Arrow.setTexture(arrow_U);
        }
        if (player.getDirection().equals("LEFT")) {
            Arrow.setTexture(arrow_L);
        }
        if (player.getDirection().equals("RIGHT")) {
            Arrow.setTexture(arrow_R);
        }
    }

    void move() {
        if (alive) {
            if (direction.equals("UP")) {
                y -= speed;
            }
            if (direction.equals("DOWN")) {
                y += speed;
            }
            if (direction.equals("LEFT")) {
                x -= speed;
            }
            if (direction.equals("RIGHT")) {
                x += speed;
            }

            Arrow.setPosition(x, y);
        }
    }

    private void checkBoundaries() {
        if (x > 1050 || x < 150 || y < 100 || y > 550) {
            kill();
        }

        for (int i = 0; i<player.getGuardArray().length; i++) {
            int guardX = player.getGuardArray()[i].getStats().getXPos();
            int guardY = player.getGuardArray()[i].getStats().getYPos();
            if (((guardX >= x) && (guardX <= x + 100) || (guardX <= x) && (guardX >= x - 100)) && ((guardY >= y) && (guardY <= y + 100) || (guardY <= y) && (guardY >= y - 100))) {
                player.getGuardArray()[i].getStats().takeDamage(30);
                player.getGuardArray()[i].getStats().setChase();
                player.getGuardArray()[i].damageFlash();
                splat.stop();
                splat.play();

                kill();
            }
        }

        try {
            for (int i = 0; i < room.getBox().length; i++) {
                int guardX = room.getBox()[i].getXPosition();
                int guardY = room.getBox()[i].getYPosition();
                if (((guardX >= x) && (guardX <= x + 50) || (guardX <= x) && (guardX >= x - 50)) && ((guardY >= y) && (guardY <= y + 50) || (guardY <= y) && (guardY >= y - 50))) {
                    kill();
                }
            }
        }
        catch (Exception ignored) {}

        try {
            int guardX = room.getBoss().getStats().getXPos();
            int guardY = room.getBoss().getStats().getYPos();
            if (((guardX >= x) && (guardX <= x + 100) || (guardX <= x) && (guardX >= x - 100)) && ((guardY >= y) && (guardY <= y + 100) || (guardY <= y) && (guardY >= y - 100))) {
                room.getBoss().getStats().takeDamage(10);
                splat.stop();
                splat.play();
                kill();
            }
        }
        catch (Exception ignored) {}

    }

    void draw() {
        if (alive) {
            checkBoundaries();
            window.draw(Arrow);
        }
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    void kill() {
        alive = false;
    }

}
