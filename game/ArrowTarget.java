package game;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;

import java.io.IOException;
import java.nio.file.Paths;

class ArrowTarget {

    private RenderWindow window;
    private Player player;

    private Sprite arrowTarget;

    private Texture red = new Texture();
    private Texture green = new Texture();

    private int x = 0;
    private int y = 0;

    private boolean triggered = false;

    ArrowTarget(RenderWindow window, Player player, int x, int y) throws IOException {
        this.window = window;
        this.player = player;
        this.x = x;
        this.y = y;

        red.loadFromFile(Paths.get("sprites/ArrowTarget_Red.png"));
        green.loadFromFile(Paths.get("sprites/ArrowTarget_Green.png"));

        arrowTarget = new Sprite(red);
        arrowTarget.setScale(2, 2);
        arrowTarget.setPosition(x, y);
    }

    void draw() {
        for (int i=0; i<player.getArrowsNum(); i++) {
            int arrowX = player.getArrows()[i].getX();
            int arrowY = player.getArrows()[i].getY();
            if (((arrowX > x) && (arrowX < x+50) || (arrowX < x) && (arrowX > x-50)) && ((arrowY > y) && (arrowY < y+50) || (arrowY < y) && (arrowY > y-50))) {
                changeColour();
                player.getArrows()[i].kill();
            }
        }

        window.draw(arrowTarget);
    }

    private void changeColour() {
        if (!(triggered)) {
            arrowTarget.setTexture(green);
            triggered = true;

            SoundStream pickup = new SoundStream(window, "pickup");
            pickup.play();
        }
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    boolean getTriggered() {
        return triggered;
    }
}
