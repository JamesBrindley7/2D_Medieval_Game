package game;

import java.io.IOException;

import org.jsfml.graphics.RenderWindow;

/**
 * Class for the swordsman guard, includes and passes on the stats for the guard to the main guard class
 * @author James Brindley
 *
 */
public class GuardSword extends Guard {
	
	//Stats for the Sword Guard
    private static int attackdistance = 100;	//Attacking distance	
    private static int damage = 1;	//Attack Damage
    private static int health = 100;	//Guard Health
    private static int sightdistance = 160;	//Guards sight distance to see the player first
    private static int sighttolose = 400;	//Guards sight distance to lose the player
    private static int sightwidth = 90; 	//Guards sight width to see the player
    private static int attackspeed = 50;
    private static String enemyname = "Sword_Guard";

    private Room room;
    
	public GuardSword(RenderWindow window, int xPosition, int yPosition, Room room) throws IOException {
		super(window, xPosition, yPosition,attackdistance, damage, health, sightdistance, sighttolose, sightwidth, enemyname, attackspeed);
		//Super the parameters to the class it inherits from e.g. Guard class
        this.room = room;
	}

    public void primaryAttack() throws IOException {
        if (getStats().getAlive()) {
            if (getStats().getAttacking() == true) {
                if(getStats().checkprimaryattack()) {
                    if(checkcone(getStats().getAttackDis())) {
                        System.out.println("Primary attack, Dealt damage to player");
                        room.damagePlayer(damage);
                    }
                }
            }
        }
    }
}
