package game;

import java.io.IOException;
import java.util.Random;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;

/**
 * Class for the archer guard, includes and passes on the stats for the guard to the main guard class
 * @author James Brindley
 *
 */
public abstract class Boss extends AI {
	//Stats for the first boss
	Random rand = new Random();	//Random number instance for the random coordinate generator
	int attacktype;
	boolean chosen = false;
    
    private static String enemytype = "Boss";
    protected Sprite guardAI;
    
	public Boss(RenderWindow window, int xPosition, int yPosition, int attackdistance, int primarydamage, int secondarydamage, int tertiarydamage, int health, int sightdistance, int sighttolose, int sightwidth, String enemyname, int primaryattackspeed, int secondaryattackspeed, int tertiaryattackspeed) throws IOException {
		super(window, xPosition, yPosition,attackdistance, primarydamage,secondarydamage,tertiarydamage, health, sightdistance, sighttolose, sightwidth, enemyname,enemytype, primaryattackspeed, secondaryattackspeed, tertiaryattackspeed);
		//Super the parameters to the class it inherits from e.g. Guard class
		//Create a new instance of guard stats that stores all information about them
		guardAI = GuardAI;
		
	}
	public abstract void primaryAttack();
	public abstract void secondaryAttack(); 
	public abstract void tertiaryAttack();
}
