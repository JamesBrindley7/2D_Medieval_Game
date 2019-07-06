package game;

import org.jsfml.graphics.RenderWindow;
import java.io.IOException;

class GrabClaw extends Item {

    private RenderWindow window;
    private Player player;
    private Room room;

    GrabClaw(RenderWindow window, Player player, int x, int y, Room room) throws IOException {
        super(window, "GrabClaw", player, x, y);

        this.window = window;
        this.player = player;
        this.room = room;

        setScale(5);
    }

    @Override
    void pickUp() throws IOException {
        if (player.checkCollisionObject(itemX+((itemSprite.getTexture().getSize().x)/2), itemY+((itemSprite.getTexture().getSize().y)/2))) {
            System.out.println("Player picked up " + itemName);
            pickedUp = true;

            SoundStream pickup = new SoundStream(window,"pickup");
            pickup.play();
        }
    }

    boolean getPickedUp() {
        return pickedUp;
    }
}
