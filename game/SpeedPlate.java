package game;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;

import java.nio.file.Paths;
import java.io.IOException;

class SpeedPlate {

    private RenderWindow window;
    private Player player;
    private int xPosition;
    private int yPosition;

    private Sprite speedPlate;
    private Texture tex = new Texture();

    SpeedPlate(RenderWindow window, Player player, int x, int y) throws IOException {
        this.window = window;
        this.player = player;
        this.xPosition = x;
        this.yPosition = y;

        tex.loadFromFile(Paths.get("sprites/Pressure_Plate.png"));

        speedPlate = new Sprite(tex);
        speedPlate.setScale(5, 5);
        speedPlate.setPosition(xPosition, yPosition);
    }

    void draw() {
        window.draw(speedPlate);
    }

    boolean checkPlayer() {
        if ((player.getXPos() >= xPosition - 100) && (player.getXPos() <= xPosition + 100) && (player.getYPos() >= yPosition - 100) && (player.getYPos() <= yPosition + 100)) {
            return true;
        }
        else return false;
    }
}
