package game;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This abstract class provides a way to implement shared functionality for all pickups/items
 *
 * @author Jay Golder
 */
abstract class Item {
    private RenderWindow window;
    protected String itemName;
    protected Sprite itemSprite;
    protected Player player;
    protected boolean pickedUp = false;
    protected int itemX = 0;
    protected int itemY = 0;

    /**
     * Constructor for items
     *
     * @param window The game window to render the item in
     * @param name   The name of the item (must relate to pathname of sprite)
     * @param player The player that will interact with the item
     * @throws IOException When file is not found
     */
    Item(RenderWindow window, String name, Player player, int x, int y) throws IOException {
        this.window = window;
        this.itemName = name;
        this.player = player;
        this.itemX = x;
        this.itemY = y;
        Path path = Paths.get("sprites/" + itemName + ".png");
        Texture item = new Texture();
        item.loadFromFile(path);
        itemSprite = new Sprite(item);
        itemSprite.setScale(1, 1);
        itemSprite.setPosition(itemX, itemY);

    }

    void draw() {
        window.draw(itemSprite);
    }

    void pickUp() throws IOException {}

    void setScale(int i) {
        itemSprite.setScale(i, i);
    }
}
