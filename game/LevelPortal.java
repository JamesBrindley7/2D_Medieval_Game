package game;

import org.jsfml.graphics.RenderWindow;

import java.io.IOException;

class LevelPortal extends Item {

    Room room;

    public LevelPortal(RenderWindow window, Player player, int x, int y, Room room) throws IOException {
        super(window, "LevelPortal", player, x, y);
        this.room = room;
    }

    @Override
    public void pickUp() throws IOException {
        if (player.checkCollisionObject(itemX+((itemSprite.getTexture().getSize().x)/2), itemY+((itemSprite.getTexture().getSize().y)/2))) {
            System.out.println("Player picked up " + itemName);
            room.getLevel().changeLevel();
            pickedUp = true;
        }
    }

    public boolean getPickedUp() {
        return pickedUp;
    }
}
