package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;

/**
 * Class for the first boss, includes and passes on the stats for the AI to the main AI class
 * @author James Brindley
 *
 */
public class Boss2 extends Boss {

	//Stats for the first boss
	Random rand = new Random();	//Random number instance for the random coordinate generator
	int attacktype;
	boolean chosen = false;

	private static int attackdistance = 150;	//Attacking distance

	private static int primarydamage = 10;	//Attack Damage
	private static int secondarydamage = 40;	//Attack Damage
	private static int tertiarydamage = 80;	//Attack Damage

	private static int health = 400;	//Boss Health
	private static int sightdistance = 700;	//Boss sight distance to see the player first
	private static int sighttolose = 1000;	//Boss sight distance to lose the player
	private static int sightwidth = 600; 	//Boss sight width to see the player

	private static int primaryattackspeed = 40;
	private static int secondaryattackspeed = 50;
	private static int tertiaryattackspeed = 60;
	private int chargerup = 0;
	private int standattack = 0;
	private int drawcone = 0;
	private int drawcircle = 0;
	private boolean conesetup = false;
	private int diameter = 0;
	private boolean attacking = false;

	private Sprite GuardConeLeft;
	private Sprite GuardConeRight;
	private Sprite GuardConeUp;
	private Sprite GuardConeDown;
	private Sprite GuardCircle;
	private Texture cone_L = new Texture();
	private Texture cone_R = new Texture();
	private Texture cone_D = new Texture();
	private Texture cone_U = new Texture();
	private Texture circle = new Texture();

	private static String enemyname = "Boss2";

	RenderWindow window;

	private Room room;

	public Boss2(RenderWindow window, int xPosition, int yPosition, Room room) throws IOException {
		super(window, xPosition, yPosition,attackdistance, primarydamage, secondarydamage, tertiarydamage, health, sightdistance, sighttolose, sightwidth, enemyname, primaryattackspeed, secondaryattackspeed, tertiaryattackspeed);
		this.room = room;
		cone_L.loadFromFile(Paths.get("sprites/AttackCone_Left.png"));
		cone_R.loadFromFile(Paths.get("sprites/AttackCone_Right.png"));
		cone_D.loadFromFile(Paths.get("sprites/AttackCone_Down.png"));
		cone_U.loadFromFile(Paths.get("sprites/AttackCone_Up.png"));
		circle.loadFromFile(Paths.get("sprites/AttackCircle.png"));
		GuardConeLeft = new Sprite(cone_L);
		GuardConeLeft.setScale(8, 9);
		GuardConeRight = new Sprite(cone_R);
		GuardConeRight.setScale(8, 9);
		GuardConeUp = new Sprite(cone_U);
		GuardConeUp.setScale(9, 8);
		GuardConeDown = new Sprite(cone_D);
		GuardConeDown.setScale(9, 8);
		GuardCircle = new Sprite(circle); //13pixels
		GuardCircle.setScale(1, 1);
		this.window = window;


		guardAI.setScale(3.5f, 3.5f);
		//Move the player to default position
		//Super the parameters to the class it inherits from e.g. Guard class
	}
	/**
	 * Checks the random attack, if boss is attacking and charging up return 1 or if hes in range return 1 otherwise return 0 to let him move
	 * @return
	 */
	public int checkRandomAttack() {
		if (chosen == false) {
			chosen = true;
			int randomattack = rand.nextInt((100 - 1) + 1) + 1; //Randomly generate 2 numbers then divide and multiply by 10 to make sure steps work
			if (randomattack <= 60) {
				attacktype = 1;
			}
			else if(randomattack <=90) {
				attacktype = 2;
			}
			else if (randomattack <= 100) {
				attacktype = 3;
			}
		}
		if (attacktype == 1) {
			return checkPrimaryAttack();
		}
		else if (attacktype == 2) {
			return checkSecondaryAttack();
		}
		else if (attacktype == 3) {
			return checkTertiaryAttack();
		}
		return 3;
	}
	/**
	 * Gets the random number that was created in the random attack check and match it to the correct attack
	 */
	public void randomAttack() {
		if(attacktype == 1) {
			primaryAttack();
		}
		else if(attacktype == 2) {
			secondaryAttack();
		}
		else if(attacktype == 3) {
			tertiaryAttack();
		}
	}
	/**
	 * Creates the attack cone to show the right attack cone depending on the direction
	 */
	public void createattackcone() {
		if(getStats().getDirection() == 0) { //Right
			GuardConeRight.setPosition(getStats().getXPos()+90, getStats().getYPos()+20);
		}
		if(getStats().getDirection() == 1) {	//Left
			GuardConeLeft.setPosition(getStats().getXPos()-180, getStats().getYPos()+20);
		}
		if(getStats().getDirection() == 2) { //Up
			GuardConeUp.setPosition(getStats().getXPos()+20, getStats().getYPos()-150);
		}
		if(getStats().getDirection() == 3) { //Down
			GuardConeDown.setPosition(getStats().getXPos()+10, getStats().getYPos()+60);
		}
	}
	/**
	 * Create a circle to show the attack radious of the guard
	 */
	public void createattackcircle() {
		GuardCircle.setPosition(getStats().getXPos() - (diameter/2)+(getStats().checkSize()/2) , getStats().getYPos() - (diameter/2)+ (getStats().checkSize()/2));
	}
	/**
	 * Draw the circle
	 * @return The circle sprite
	 */
	public Sprite drawcircle() {
		return GuardCircle;
	}
	/**
	 * Draw the correct cone
	 * @return The cone sprite
	 */
	public Sprite drawattackcone() {
		if(getStats().getDirection() == 0) {
			return GuardConeRight;
		}
		else if(getStats().getDirection() == 1) {
			return GuardConeLeft;
		}
		else if(getStats().getDirection() == 2) {
			return GuardConeUp;
		}
		else if(getStats().getDirection() == 3) {
			return GuardConeDown;
		}
		return GuardConeDown;
	}
	/**
	 * Charges up the primary attack and attacks the player if still isnide the area
	 */
	public void primaryAttack() {	//Normal hit (Cone)
		if (getStats().getAlive()) {
			changeimage();
			drawcircle = 0;
			if (chargerup == 0) {
				drawcone = 0;
				chosen = false;
				if(checkcone(getStats().getAttackDis())) {
					System.out.println("Primary attack, Dealt damage to player");
					try {
						room.damagePlayer(1);
					}
					catch (Exception ignored) {}
				}
				standattack = 0;
				return;
			}
			else {
				if (conesetup) {
					createattackcone();
					conesetup = false;
				}
				drawcone = 1;
				chargerup--;
			}
		}
	}
	/**
	 * Charges up the secondary attack and attacks the player if still isnide the area
	 */
	public void secondaryAttack() { 	//Hammer Spin (Circle around)
		if (getStats().getAlive()) {
			changeimage();
			drawcone = 0;
			GuardCircle.setScale(23, 23);
			if (chargerup == 0) {
				drawcircle = 0;
				chosen = false;
				if(checkcircle(300)) {
					System.out.println("secondary attack, Dealt damage to player");
					try {
						room.damagePlayer(1);
					}
					catch (Exception ignored) {}
				}
				standattack = 0;
				return;
			}
			else {
				if (conesetup) {
					createattackcircle();
					conesetup = false;
				}
				drawcircle = 1;
				chargerup--;
			}
		}
	}
	/**
	 * Charges up the tertiary attack and attacks the player if still isnide the area
	 */
	public void tertiaryAttack() {		//Hammer Slam (Bigger circle)
		if (getStats().getAlive()) {
			changeimage();
			drawcone = 0;
			GuardCircle.setScale(38, 38);
			if (chargerup == 0) {
				drawcircle = 0;
				chosen = false;
				if(checkcircle(500)) {
					System.out.println("tertiary attack, Dealt damage to player");
					try {
						room.damagePlayer(1);
					}
					catch (Exception ignored) {}
				}
				standattack = 0;
				return;
			}
			else {
				if (conesetup) {
					createattackcircle();
					conesetup = false;
				}
				drawcircle = 1;
				chargerup--;
			}
		}
	}
	/**
	 * Checks to see if the guard can attack with the primary
	 * @return
	 */
	public int checkPrimaryAttack() { 	//Hammer Spin (Circle around)
		if (getStats().getAlive()) {
			if (standattack == 0) {
				chargerup = getStats().getPrimaryAttackSpeed();
				if(getStats().checkprimaryattack()) {
					if(checkcone(getStats().getAttackDis()+20)) {
						standattack = 1;
						conesetup = true;
						return 1;
						//if it returns 0 means nothing and keep moving
						//if it returns 1 it means in range and do attack so dont move
					}
					else {
						return 0;
					}
				}
				else {
					return 0;
				}
			}
			else {
				return 1;
			}
		}
		return 0;
	}
	/**
	 * Checks to see if the guard can attack with the secondary
	 * @return
	 */
	public int checkSecondaryAttack() { 	//Hammer Spin (Circle around)
		if (getStats().getAlive()) {
			if (standattack == 0) {
				chargerup = getStats().getSecondaryAttackSpeed();
				if(getStats().checksecondaryattack()) {//Returns true if the timer is over and the boss can attack
					if(checkcircle(300+30)) {
						diameter = 300;
						standattack = 1;
						conesetup = true;
						return 1;
						//if it returns 0 means nothing and keep moving
						//if it returns 1 it means in range and do attack so dont move
					}
					else {
						return 0;
					}
				}
			}
			else {
				return 1;
			}
		}
		return 0;
	}
	/**
	 * Checks to see if the guard can attack with the tertiary
	 * @return
	 */
	public int checkTertiaryAttack() { 	//Hammer Spin (Circle around)
		if (getStats().getAlive()) {
			if (standattack == 0) {
				chargerup = getStats().getTertiaryAttackSpeed();
				if(getStats().checktertiaryattack()) {
					if(checkcircle(500+30)) {
						diameter = 500;
						standattack = 1;
						conesetup = true;
						return 1;
						//if it returns 0 means nothing and keep moving
						//if it returns 1 it means in range and do attack so dont move
					}
					else {
						return 0;
					}
				}
			}
			else {
				return 1;
			}
		}
		return 0;
	}
	/**
	 * gets the cone sprite
	 * @return
	 */
	public int getdrawcone() {
		return drawcone;
	}
	/**
	 * gets the circle sprite
	 * @return
	 */
	public int getdrawcircle() {
		return drawcircle;
	}
	/**
	 * Wipes all the varaibles so it doesnt draw anything
	 */
	public void wipevairables() {
		chargerup = 50;
		standattack = 0;
		drawcone = 0;
		drawcircle = 0;
		conesetup = false;
		diameter = 0;
		attacking = false;
	}
}
