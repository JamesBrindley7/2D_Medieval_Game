package game;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;

import java.io.IOException;

public class HealthPickup extends Item {


    public HealthPickup(RenderWindow window, Player player, int x, int y) throws IOException {
        super(window, "Heart", player, x, y);
    }

    @Override
    public void pickUp() {
        if (player.checkCollisionObject(itemX+((itemSprite.getTexture().getSize().x)/2), itemY+((itemSprite.getTexture().getSize().y)/2))) {
            System.out.println("Player picked up " + itemName);
            player.getPlayerHealth().heal();
            pickedUp = true;
        }
    }

    public boolean getPickedUp() {
        return pickedUp;
    }
}
