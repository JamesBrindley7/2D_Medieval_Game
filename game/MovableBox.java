package game;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

/**
 *  A smple movable box class that the player can push and pull
 * @author James Brindley
 *
 */
public class MovableBox {
	private RenderWindow window;
	private Sprite Box;
	private Player player;
	private int XPosition;
	private int YPosition;
	private float XSize;
	private float YSize;
	private int maxX = 1130;	//Max X coordinate the guard can go
    private int maxY = 575;	//Max Y coordinate the guard can go
    private int minX = 150;	//Min X coordinate the guard can go
    private int minY = 150;	//Min Y coordinate the guard can go
	private int deltaX = 0; //X offset
    private int deltaY = 0;	//Y offset

	private Texture box = new Texture();
    
	/**
	 * The constructor for the box
	 * @param window The window that the box will be drew in
	 * @param XPosition	The x position of the box (Top left corner)
	 * @param YPosition The y position of the box (Top left corner)
	 * @param XSize The size of the box X side
	 * @param YSize The size of the box Y side
	 * @param player The players instance so the coordinates can be accessed
	 * @throws IOException
	 */
	public MovableBox(RenderWindow window,int XPosition, int YPosition, int XSize, int YSize, Player player) throws IOException {
		this.XPosition = XPosition; //X coordinate
		this.YPosition = YPosition; //Y coordinate
		this.window = window;
		this.XSize = XSize;
		this.YSize = YSize;
		this.player = player;
		box.loadFromFile(Paths.get("sprites/Stone_Brick.png"));
		Box = new Sprite(box);
		Box.setScale(5, 5);

		Box.setPosition(XPosition, YPosition);
	}
	void draw() {
		window.draw(Box);
	}
	
	/**
	 * This method checks to see if the player is in range to push the box
	 */
	boolean checkpush() {
		boolean check = false;
		if (player.getDirection() == "LEFT") {
			 if (player.getXPos() > (int)(XPosition + XSize)-10 && player.getXPos() <= (int)(XPosition + XSize)) {
				if (player.getYPos() < (YPosition + YSize) && player.getYPos()+80 > YPosition) {
					check = true;
				}
			}
		}
		if (player.getDirection() == "RIGHT") {
			if (player.getXPos()+60 >= (int)(XPosition) && player.getXPos()+60 <= (int)(XPosition+10)) {
				if (player.getYPos() < (YPosition + YSize) && player.getYPos()+80 > YPosition) {
					check = true;
				}
			}
		}
		if (player.getDirection() == "UP") {
			if (player.getYPos() <= (int)(YPosition + YSize) && player.getYPos() >= (int)(YPosition + YSize-10)) {
				if (player.getXPos() < (XPosition + XSize) && player.getXPos()+60 > XPosition) {
					check = true;
				}
			}
		}
		if (player.getDirection() == "DOWN") {
			if (player.getYPos()+80 >= (int)(YPosition) && player.getYPos()+80 <= (int)(YPosition+10)) {
				if (player.getXPos() < (XPosition + XSize) && player.getXPos()+60 > XPosition) {
					check = true;
				}
			}
		}
		return check;
	}
	/**
	 * This method checks to see if the player is in range to pull the box
	 */
	boolean checkpull() {
		boolean check = false;
		if (player.getDirection() == "LEFT") {
			if (player.getXPos()+70 > (int)(XPosition-10) && player.getXPos()+60 < (int)(XPosition+10)) {	//If the players X coordinate is within 10 pixels of the range then the player can move
				if (player.getYPos() < (YPosition + YSize) && player.getYPos()+80 > YPosition) {
					check = true;
				}
			}
		}
		else if (player.getDirection() == "RIGHT") {
			if (player.getXPos()-10 < (int)(XPosition + XSize+10) && player.getXPos() > (int)(XPosition + XSize-10)) {
				if (player.getYPos() < (YPosition + YSize) && player.getYPos()+80 > YPosition) {
					check = true;
					System.out.println("he");
				}
			}
		}
		else if (player.getDirection() == "UP") {
			if (player.getYPos()+90 > (int)(YPosition-10) && player.getYPos()+80 < (int)(YPosition+10)) {
				if (player.getXPos() < (XPosition + XSize) && player.getXPos()+60 > XPosition) {
					check = true;
				}
			}
		}
		else if (player.getDirection() == "DOWN") {
			if (player.getYPos()-10 < (int)(YPosition + YSize+10) && player.getYPos() > (int)(YPosition + YSize-10)) {
				if (player.getXPos() < (XPosition + XSize) && player.getXPos()+60 > XPosition) {
					check = true;
				}
			}
		}
		return check;
	}
	/**
	 * This method checks to see if the player is in collision range of the box, if they are then set the players delta to 0 depending on the direction
	 * @throws IOException
	 */
	void boxcollision() throws IOException {
       if (player.getXPos() >= (int)(XPosition + XSize)-10 && player.getXPos() <= (int)(XPosition + XSize)) { //If the players X is equal to the right side of the box or 10 pixels into the right side of the box then stop
    	   if (player.getYPos() < (YPosition + YSize) && player.getYPos() > YPosition-80) {
    		   if(player.getDirection() != "RIGHT") {	//Checks to see if the direction the player is facing is the other way, aka dont block if pulling
    				player.setDeltaX(0);
    			}
    		}
       }
       else if (player.getXPos()+60 >= (int)(XPosition) && player.getXPos()+60 <= (int)(XPosition+10)) {
    	   if (player.getYPos() < (YPosition + YSize) && player.getYPos() > YPosition-80) {
    		   if(player.getDirection() != "LEFT") {
       				player.setDeltaX(0);
    		   }
    	   }
       }
       else if (player.getYPos() <= (int)(YPosition + YSize) && player.getYPos() >= (int)(YPosition + YSize-10)) {
    	   if (player.getXPos() < (XPosition + XSize) && player.getXPos() > XPosition-60) {
    		   if(player.getDirection() != "DOWN") {
       				player.setDeltaY(0);
    		   }
    	   }
       }
       else	if (player.getYPos()+80 >= (int)(YPosition) && player.getYPos()+80 <= (int)(YPosition+10)) {
    	   if (player.getXPos() < (XPosition + XSize) && player.getXPos() > XPosition-60) {
    		   if(player.getDirection() != "UP") {
       				player.setDeltaY(0);
    		   }
    	   }
       }
	}
	
	/**
	 * This method is the pull method, it allows the player to move the box along with them altering the position 
	 */
	void pull() {
		if (player.getDirection() == "LEFT") {
			if (player.getXPos()+60 == getXPosition()) {
				setDeltaX(0);
			}
			else {
				setDeltaX(-7);
			}
			setDeltaY(0);
		}
		else if (player.getDirection() == "RIGHT") {
			if (player.getXPos() == getXPosition() + (int) getXSize()) {
				setDeltaX(0);
			}
			else {
				setDeltaX(7);
			}
			setDeltaY(0);
		}
		else if (player.getDirection() == "UP") {
			if (player.getYPos()+80 == getYPosition()) {
				setDeltaY(0);
			}
			else {
				setDeltaY(-7);
			}
			setDeltaX(0);
		}
		else if (player.getDirection() == "DOWN") {
			if (player.getYPos() == getYPosition() + (int) getYSize()) {
				setDeltaY(0);
			}
			else {
				setDeltaY(7);
			}
			setDeltaX(0);
		}
		Vector2f Position = new Vector2f(getDeltaX(), getDeltaY());
		setXPosition(getXPosition()+getDeltaX());
		setYPosition(getYPosition()+getDeltaY());
		Box.move(Position);
	}
	/**
	 * This method is the push method, is the opposite of the pull method but also implememnts the wall collision so the box doesnt go off the map
	 */
	void push() {
		if (player.getDirection() == "LEFT") {
			setDeltaX(-7);
			setDeltaY(0);
		}
		if (player.getDirection() == "RIGHT") {
			setDeltaX(7);
			setDeltaY(0);
		}
		if (player.getDirection() == "UP") {
			setDeltaY(-7);
			setDeltaX(0);
		}
		if (player.getDirection() == "DOWN") {
			setDeltaY(7);
			setDeltaX(0);
		}
		wallcollision();
		Vector2f Position = new Vector2f(getDeltaX(), getDeltaY());
		setXPosition(getXPosition()+getDeltaX());
		setYPosition(getYPosition()+getDeltaY());
		Box.move(Position);
	}
	
	/**
	 * Wall collision to see if the box is off the set coordinates of the map, if it is then stop movement and stand still in that direction
	 */
	void wallcollision() {
		if (player.getDirection() == "RIGHT") {
			if (XPosition+XSize >= maxX) {
				setDeltaX(0);
			}
		}
		if (player.getDirection() == "DOWN") {
			if (YPosition+YSize >= maxY) {
	    		setDeltaY(0);
	    	}
	    }
		if (player.getDirection() == "LEFT") {
			if (XPosition <= minX) {
				setDeltaX(0);
			}
		}
	    if (player.getDirection() == "UP") {
	    	if (YPosition <= minY) {
	    		setDeltaY(0);
	    	}
	    }
	}
	/**
	 * Sets the boundaries of the map
	 * @param minX The minimum X coordinate the box can go
	 * @param maxX The maximum X coordinate the box can go
	 * @param minY The minimum Y coordinate the box can go
	 * @param maxY The maximum Y coordinate the box can go
	 */
	void setBoundaries(int minX, int maxX, int minY, int maxY) {
	    	this.maxX = maxX;
	    	this.maxY = maxY;
	    	this.minX = minX;
	    	this.minY = minY;
	    }  
	int getXPosition() {
		return XPosition;
	}
	int getYPosition() {
		return YPosition;
	}
	void setXPosition(int XPosition) {
		this.XPosition = XPosition;
	}
	void setYPosition(int YPosition) {
		this.YPosition = YPosition;
	}
	float getXSize() {
		return XSize;
	}
	float getYSize() {
		return YSize;
	}
	int getDeltaX() {
		return this.deltaX;
	}
	int getDeltaY() {
		return this.deltaY;
	}
	void setDeltaX(int deltaX) {
		this.deltaX = deltaX;
	}
	void setDeltaY(int deltaY) {
		this.deltaY = deltaY;
	}
}
