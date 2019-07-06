package game;

import java.io.IOException;

import org.jsfml.graphics.RenderWindow;

/**
 * Class for the archer guard, includes and passes on the stats for the guard to the main guard class
 * @author James Brindley
 *
 */
public class GuardBow extends Guard {
	
	//Stats for the Sword Guard
    private static int attackdistance = 400;	//Attacking distance	
    private static int damage = 10;	//Attack Damage
    private static int health = 100;	//Guard Health
    private static int sightdistance = 400;	//Guards sight distance to see the player first
    private static int sighttolose = 600;	//Guards sight distance to lose the player
    private static int sightwidth = 250; 	//Guards sight width to see the player
    private static int attackspeed = 90;
    private static String enemyname = "Bow_Guard";
    
	public GuardBow(RenderWindow window, int xPosition, int yPosition) throws IOException {
		super(window, xPosition, yPosition,attackdistance, damage, health, sightdistance, sighttolose, sightwidth, enemyname, attackspeed);
		//Super the parameters to the class it inherits from e.g. Guard class
	}
}
