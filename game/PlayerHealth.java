package game;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class provides a way to keep track of (and alter) the player's health and to display it on the screen
 *
 * @author Jay Golder
 */
public class PlayerHealth {
    private RenderWindow window;
    private Room room;
    private Sprite sprite;
    private int health = 3;
    private Texture health0 = new Texture();
    private Texture health1 = new Texture();
    private Texture health2 = new Texture();
    private Texture health3 = new Texture();

    /**
     * Constructor for PlayerHealth
     *
     * @param window The game window to render the item in
     */
    public PlayerHealth(RenderWindow window, Room room) {
        this.window = window;
        this.room = room;
        Path path0 = Paths.get("sprites/Heart0.png");
        Path path1 = Paths.get("sprites/Heart1.png");
        Path path2 = Paths.get("sprites/Heart2.png");
        Path path3 = Paths.get("sprites/Heart3.png");

        try {
            health0.loadFromFile(path0);
            health1.loadFromFile(path1);
            health2.loadFromFile(path2);
            health3.loadFromFile(path3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sprite = new Sprite(health3);
        sprite.setScale(1, 1);
        sprite.setPosition(0, 0);
    }

    private void changeSprite() {
        switch (health) {
            case 0:
                sprite.setTexture(health0);
                break;
            case 1:
                sprite.setTexture(health1);
                break;
            case 2:
                sprite.setTexture(health2);
                break;
            case 3:
                sprite.setTexture(health3);
                break;
        }
    }

    public void draw() {
        changeSprite();
        window.draw(sprite);
    }

    public int getHealth() {
        return health;
    }

    public void heal() {
        if (health < 3) {
            health++;
            System.out.println("Health increased to " + health);
        }
    }

    public void hurt() throws IOException {
        if (health > 0) {
            health--;
            System.out.println("Health decreased to " + health);

            System.out.println(room.getLevel());
            System.out.println(room.getLevel().getRoom(room.getLevel().getPlayerRoom()));
            System.out.println(room.getLevel().getRoom(room.getLevel().getPlayerRoom()).getPlayer());
            room.getLevel().getRoom(room.getLevel().getPlayerRoom()).getPlayer().knockback();

            SoundStream pain = new SoundStream(window,"pain");
            pain.play();

            if (health == 0) {
                room.dead();
            }
        }
    }
}
