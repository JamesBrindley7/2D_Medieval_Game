package game;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;

/**
 * Class for the archer guard, includes and passes on the stats for the guard to the main guard class
 * @author James Brindley
 *
 */
public class Guard extends AI {
    //Stats for the first boss
    private static int secondarydamage = 0;    //Attack Damage
    private static int tertiarydamage = 0;    //Attack Damage

    private static int secondaryattackspeed = 100000;
    private static int tertiaryattackspeed = 100000;

    private static String enemytype = "Guard";

    public Guard(RenderWindow window, int xPosition, int yPosition, int attackdistance, int primarydamage, int health, int sightdistance, int sighttolose, int sightwidth, String enemyname, int primaryattackspeed) throws IOException {
        super(window, xPosition, yPosition, attackdistance, primarydamage, secondarydamage, tertiarydamage, health, sightdistance, sighttolose, sightwidth, enemyname, enemytype, 50, secondaryattackspeed, tertiaryattackspeed);
        //Super the parameters to the class it inherits from e.g. Guard class
        //Create a new instance of guard stats that stores all information about them
    }
}
