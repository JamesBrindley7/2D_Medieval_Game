package game;

import org.jsfml.graphics.RenderWindow;
import java.io.IOException;

class Key extends Item {

    private int xPosition;
    private int yPosition;

    private Player player;
    private RenderWindow window;

    private Room room;

    private int roomNum;
    private String doorDirection;

    Key(RenderWindow window, Player player, int x, int y, Room room, int roomNum, String doorDirection) throws IOException {
        super(window, "Key", player, x, y);

        xPosition = x;
        yPosition = y;

        itemX = xPosition;
        itemY = yPosition;

        this.room = room;
        this.player = player;
        this.window = window;
        this.roomNum = roomNum;
        this.doorDirection = doorDirection;

    }

    @Override
    void pickUp() throws IOException {
        if (player.checkCollisionObject(itemX+((itemSprite.getTexture().getSize().x)/2), itemY+((itemSprite.getTexture().getSize().y)/2))) {
            System.out.println("Player picked up " + itemName);
            pickedUp = true;

            SoundStream pickup = new SoundStream(window,"pickup");
            pickup.play();

            room.getLevel().unlockDoor(roomNum, doorDirection);
        }
    }

    boolean getPickedUp() {
        return pickedUp;
    }
}
