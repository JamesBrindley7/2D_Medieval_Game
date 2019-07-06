package game;

import org.jsfml.graphics.RenderWindow;
import java.io.IOException;

class BowItem extends Item {

    private RenderWindow window;
    private Player player;
    private Room room;

    BowItem(RenderWindow window, Player player, int x, int y, Room room) throws IOException {
        super(window, "Bow", player, x, y);

        this.window = window;
        this.player = player;
        this.room = room;
    }

    @Override
    void pickUp() throws IOException {
        if (player.checkCollisionObject(itemX+((itemSprite.getTexture().getSize().x)/2), itemY+((itemSprite.getTexture().getSize().y)/2))) {
            System.out.println("Player picked up " + itemName);
            pickedUp = true;

            SoundStream pickup = new SoundStream(window,"pickup");
            pickup.play();

            room.getLevel().setItem("Bow", true);
        }
    }

    boolean getPickedUp() {
        return pickedUp;
    }
}
