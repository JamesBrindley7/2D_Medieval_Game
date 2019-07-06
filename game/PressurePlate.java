package game;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;

import java.io.IOException;
import java.nio.file.Paths;

class PressurePlate {
    private RenderWindow window;
    private Player player;
    private MovableBox box;
    private Sprite ppSprite;
    private Room room;

    private int xPosition;
    private int yPosition;

    private boolean unlocked = false;

    private Texture pp = new Texture();

    private int roomNum = -1;
    private String direction;

    PressurePlate(RenderWindow window, Player player, int xPosition, int yPosition, MovableBox box, Room room) throws IOException {
        this.window = window;
        this.player = player;
        this.box = box;
        this.room = room;

        this.xPosition = xPosition;
        this.yPosition = yPosition;

        pp.loadFromFile(Paths.get("sprites/Pressure_Plate.png"));
        ppSprite = new Sprite(pp);
        ppSprite.setScale(8, 8);

        ppSprite.move(xPosition, yPosition);
    }

    PressurePlate(RenderWindow window, Player player, int xPosition, int yPosition, MovableBox box, Room room, int roomNum, String direction) throws IOException {
        this.window = window;
        this.player = player;
        this.box = box;
        this.room = room;

        this.xPosition = xPosition;
        this.yPosition = yPosition;

        pp.loadFromFile(Paths.get("sprites/Pressure_Plate.png"));
        ppSprite = new Sprite(pp);
        ppSprite.setScale(8, 8);

        ppSprite.move(xPosition, yPosition);

        this.roomNum = roomNum;
        this.direction = direction;
    }

    void draw() {
        window.draw(ppSprite);
    }

    void boxCheck() {
        if ((box.getXPosition() > xPosition - 100) && (box.getXPosition() < xPosition + 100) && (box.getYPosition() > yPosition - 100) && (box.getYPosition() < yPosition + 100)) {

            if ((!unlocked) && (roomNum == -1)) {
                SoundStream unlock = new SoundStream(window, "unlock");
                unlock.play();
                room.unlockDoor("LEFT");

                unlocked = true;
            }
            else if (!unlocked){
                SoundStream unlock = new SoundStream(window, "unlock");
                unlock.play();
                room.getLevel().getRoom(roomNum).unlockDoor(direction);

                unlocked = true;
            }
        }
    }
}
