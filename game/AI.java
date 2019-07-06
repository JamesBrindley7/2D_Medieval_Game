package game;

import java.io.IOException;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
/**
 * A guard class that controls all guard functions such as moving and detection including cone sight
 * @author James Brindley
 * @version 1.55
 */
public class AI {
	
	private RenderWindow window; // The render window, e.g. the screen
    protected Sprite GuardAI;		//The sprite/guard instance
    private Sprite GuardConeLeft;	
    private Sprite GuardConeRight;	
    private Sprite GuardConeUp;	
    private Sprite GuardConeDown;	
    Random rand = new Random();	//Random number instance for the random coordinate generator
    private List<Integer> obstaclelist = new ArrayList();
    private List<MovableBox> BoxList = new ArrayList();
    private AIStats Stats;
    private int playerX;
	private int playerY;
	private int maxX;	//Max X coordinate the guard can go
    private int maxY;	//Max Y coordinate the guard can go
    private int minX;	//Min X coordinate the guard can go
    private int minY;	//Min Y coordinate the guard can go
    private int randomx = 0;	//The random X coordinate
    private int randomy = 0;	//The random Y coordinate
    private int checkstuck = 0;
    
    private int changecount = 0;
    private int lastattack = 0;
    private boolean hitbox = false;
    
    private Texture player_L = new Texture();
    private Texture player_R = new Texture();
    private Texture player_D = new Texture();
    private Texture player_U = new Texture();
    private Texture cone_L = new Texture();
    private Texture cone_R = new Texture();
    private Texture cone_D = new Texture();
    private Texture cone_U = new Texture();
    private Texture dead = new Texture();
    
    
	/**
	 * Constuctor for the AI
	 * @param window The window to draw to
	 * @param xPosition The x position for the AI to start at
	 * @param yPosition The y position for the AI to start at
	 * @param attackdistance The attack distance of the AI
	 * @param primarydamage The primary damage of the AI
	 * @param secondarydamage The secondary damage of the AI
	 * @param tertiarydamage The tertiary damage of the AI
	 * @param health The health of the AI
	 * @param sightdistance The sight distance of the AI
	 * @param sighttolose The sight to lose the player 
	 * @param sightwidth The sight width of the AI cone
	 * @param enemyname The name of the AI
	 * @param enemytype The type of the AI e.g. guard etc
	 * @param primaryattackspeed The primary attack speed of the AI
	 * @param secondaryattackspeed The secomdary attack speed of the AI
	 * @param tertiaryattackspeed The tertiary attack speed of the AI
	 * @throws IOException
	 */
    public AI(RenderWindow window, int xPosition, int yPosition, int attackdistance, int primarydamage, int secondarydamage, int tertiarydamage, int health, int sightdistance, int sighttolose, int sightwidth, String enemyname,String enemytype, int primaryattackspeed, int secondaryattackspeed, int tertiaryattackspeed) throws IOException {
    	Stats = new AIStats(window, xPosition, yPosition,attackdistance, primarydamage,secondarydamage,tertiarydamage, health, sightdistance, sighttolose, sightwidth, enemyname,enemytype, primaryattackspeed, secondaryattackspeed, tertiaryattackspeed);
		 	
		 this.window = window;
		 //Get the path for the player sprite and set the texture
	     Path path = Paths.get("sprites/"+enemyname+".png"); //get the path to the picture using the enemy name passed on
	     Texture guard = new Texture();
	     guard.loadFromFile(path);

	     //Create a sprite for the player and change the scale
	     GuardAI = new Sprite(guard);
	     GuardAI.setScale(5, 5);
	     
	        
	     player_L.loadFromFile(Paths.get("sprites/"+enemyname+"_Left.png"));
	     player_R.loadFromFile(Paths.get("sprites/"+enemyname+"_Right.png"));
	     player_D.loadFromFile(Paths.get("sprites/"+enemyname+".png"));
	     player_U.loadFromFile(Paths.get("sprites/"+enemyname+"_Up.png"));
	     cone_L.loadFromFile(Paths.get("sprites/ViewCone_Left.png"));
	     cone_R.loadFromFile(Paths.get("sprites/ViewCone_Right.png"));
	     cone_D.loadFromFile(Paths.get("sprites/ViewCone_Down.png"));
	     cone_U.loadFromFile(Paths.get("sprites/ViewCone_Up.png"));
         dead.loadFromFile(Paths.get("sprites/"+enemyname+"_Dead.png"));
	       
         GuardConeLeft = new Sprite(cone_L);
         GuardConeLeft.setScale(5, 5);
         GuardConeRight = new Sprite(cone_R);
         GuardConeRight.setScale(5, 5);
         GuardConeUp = new Sprite(cone_U);
         GuardConeUp.setScale(5, 5);
         GuardConeDown = new Sprite(cone_D);
         GuardConeDown.setScale(5, 5);
	     //Move the player to default position
	     GuardAI.move(Stats.getXPos(), Stats.getYPos());
	     
	     //60 x 80 down
	     //160 x 90
	     
	     Stats.setConeLXPos(Stats.getXPos()-130);
 		 Stats.setConeRXPos(Stats.getXPos()+50);
 		 Stats.setConeUXPos(Stats.getXPos()+10);
 		 Stats.setConeDXPos(Stats.getXPos()+10);
 		 
 		 Stats.setConeLYPos(Stats.getYPos());
		 Stats.setConeRYPos(Stats.getYPos()+10);
		 Stats.setConeUYPos(Stats.getYPos()-150);
		 Stats.setConeDYPos(Stats.getYPos()+60);
 		
	     
	     GuardConeLeft.move(Stats.getConeLXPos(), Stats.getConeLYPos());
	     GuardConeRight.move(Stats.getConeRXPos(), Stats.getConeRYPos());
	     GuardConeUp.move(Stats.getConeUXPos(), Stats.getConeUYPos());
	     GuardConeDown.move(Stats.getConeDXPos(), Stats.getConeDYPos());
	        
    }
	/**
	 * Draw the guard onto the window
	 */
	void draw() {
		window.draw(GuardAI); //Draw the GuardAI on the window
	}
	   /**
	    * Method to have the guard stand watch and face a certain directon
	    * @param Direction The direction the guard faces
	    */
	void standWatch(int Direction){ 	//0 = right, 1 = left, 2 = up, 3 = down
		if(Stats.getAlive() == true) {
			if(Stats.getReturning() == true) {
				guardreturn();
			}
			else {
				if(Stats.getGuardIdle() == true) { //If the player hasn't been spotted
					Stats.setDirection(Direction);
					detection(); //Check detections
					changeimage();
				}
				else if(Stats.getGuardIdle() == false) { //If the player has been spotted then follow the player instead
					Stats.setSpeed(7);
					movetopostion(playerX, playerY);
				}
			}
		}
	}
	/**
	 *  This method makes the guard patrol right to left between coordinates until player is spotted
	 * @param postion1 This is the start position on the X axis
	 * @param postion2 This is the end position on the X axis
	 */
	void patrolLineX(int postion1, int postion2) {
		if(Stats.getAlive() == true) {
			if(Stats.getReturning() == true) {
				guardreturn();
			}
			else {
				if(Stats.getGuardIdle() == true) { //If the player hasn't been spotted
					Stats.setSpeed(2);
					//If the guards position is more than or equal to the end position then switch direction to left 
					if (Stats.getXPos() >= postion2) {
						Stats.setDirection(1); //Left
					}
					//If the guards position is more than or equal to the start position then switch direction to right 
					if (Stats.getXPos() <= postion1) {
						Stats.setDirection(0); //Right
					}
					//Move the player in the right direction
					if (Stats.getDirection() == 1) {
						Stats.setDeltaX(-Stats.getSpeed());
					}
					if (Stats.getDirection() == 0) {
						Stats.setDeltaX(Stats.getSpeed());
					}
					boxcCollision();
					if (hitbox == true) {
						if(Stats.getDirection() == 1) {
							Stats.setDirection(0);
							Stats.setDeltaX(Stats.getSpeed());
						}
						else {
							Stats.setDirection(1);
							Stats.setDeltaX(-Stats.getSpeed());
						}
					}
					Stats.setXPos(Stats.getXPos() + Stats.getDeltaX());	//Update X position
					GuardAI.move(Stats.getDeltaX(), 0);  //Move guard
					detection(); //Check detections
					changeimage();
				}
				else if(Stats.getGuardIdle() == false) { //If the player has been spotted then follow the player instead
					Stats.setSpeed(7);
					movetopostion(playerX, playerY);
				}
			}
		}
	}
		
		
	/**
	 * This method makes the guard patrol down to up between coordinates until player is found 
	 * @param postion1 This is the start position on the Y axis
	 * @param postion2 This is the end position on the Y axis
	 */
	void patrolLineY(int postion1, int postion2) {
		if(Stats.getAlive() == true) {
			if(Stats.getReturning() == true) {
				guardreturn();
			}
			else {
				if(Stats.getGuardIdle() == true) {
					Stats.setSpeed(2);
					//If the guards position is more than or equal to the end position then switch direction to down
					if (Stats.getYPos() <= postion1) { 
						Stats.setDirection(3); //Down
					}
					//If the guards position is more than or equal to the start position then switch direction to up
					if (Stats.getYPos() >= postion2) {
						Stats.setDirection(2); //Up
					}
					//Move the player in the right direction
					if (Stats.getDirection() == 3) {
						Stats.setDeltaY(Stats.getSpeed());
						GuardAI.setTexture(player_D);
					}
					if (Stats.getDirection() == 2) {
						Stats.setDeltaY(-Stats.getSpeed());
						GuardAI.setTexture(player_U);
					}
					boxcCollision();
					if (hitbox == true) {
						if(Stats.getDirection() == 3) {
							Stats.setDirection(2);
							Stats.setDeltaY(-Stats.getSpeed());
						}
						else {
							Stats.setDirection(3);
							Stats.setDeltaY(Stats.getSpeed());
						}
					}
					Stats.setYPos(Stats.getYPos() + Stats.getDeltaY());	//Update Y position
					GuardAI.move(0, Stats.getDeltaY());  //Move guard
					detection();	//Check detections
					changeimage();
				}
				else if(Stats.getGuardIdle() == false) { //If the player has been spotted then follow the player instead
					Stats.setSpeed(7);
					movetopostion(playerX, playerY);
				}
			}
		}
	}
		
	/**
	 * This method is to make the guard patrol in a box shape until the player is found.
	 * It starts at (Xstartpostion,Ystartpostion) and moves to (Xendpostion, Ystartpostion) ||
	 * (Xendpostion, Ystartpostion) then to (Xendpostion, Yendpostion) || 
	 * (Xendpostion, Yendpostion) then to (Xstartpostion, Yendpostion) ||
	 * (Xstartpostion, Yendpostion) and finally to (Xstartpostion, Ystartpostion) ||
	 * Until its back at the start then repeats
	 * 
	 * @param Xstartpostion This is the x position the guard starts at
	 * @param Xendpostion	This is the x position the guard will stop at
	 * @param Ystartpostion	This is the Y position the guard starts at
	 * @param Yendpostion	This is the Y position the gaurd ends at
	 */
	void patrolBox(int Xstartpostion, int Xendpostion, int Ystartpostion, int Yendpostion) {
		if(Stats.getAlive() == true) {
			if(Stats.getReturning() == true) {
				guardreturn();
			}
			else {
				if(Stats.getGuardIdle() == true) {
					Stats.setSpeed(2);
					//Checks if the first part of the box has been completed
					if (Stats.getPatrolbox(0) == false) {
						//If the guards position is still not equal to the end position then do again
						if (Stats.getXPos() < Xendpostion) { 
							Stats.setDirection(0); //Right
							if (checkstuck == 5){
								if(Stats.getX5ago() == Stats.getXPos() && Stats.getY5ago() == Stats.getYPos()) {
									Stats.setPatrolbox(true, 0);
									checkstuck = 0;
									return;
								}
								checkstuck = 0;
								Stats.setX5ago(Stats.getXPos());
								Stats.setY5ago(Stats.getYPos());
							}
							else {
								checkstuck++;
							}
							patrolLineX(Xstartpostion,Xendpostion);
						}
						//If the guards position is equal to the end (e.g. completed this section) mark as done and move onto next part
						else if (Stats.getXPos() >= Xendpostion){
							Stats.setPatrolbox(true, 0); 	
							Stats.setDeltaX(0); //Reset delta
							Stats.setDeltaY(0);
						}
					}
					//Checks if the second part of the box has been completed
					else if (Stats.getPatrolbox(1) == false) {
						//If the guards position is still not equal to the end position then do again
						if (Stats.getYPos() < Yendpostion) { 
							Stats.setDirection(3); //Down
							if (checkstuck == 5){
								if(Stats.getX5ago() == Stats.getXPos() && Stats.getY5ago() == Stats.getYPos()) {
									Stats.setPatrolbox(true, 1);
									checkstuck = 0;
									return;
								}
								checkstuck = 0;
								Stats.setX5ago(Stats.getXPos());
								Stats.setY5ago(Stats.getYPos());
							}
							else {
								checkstuck++;
							}
							patrolLineY(Ystartpostion,Yendpostion);
						}
						//If the guards position is equal to the end (e.g. completed this section) mark as done and move onto next part
						else if (Stats.getYPos() >= Yendpostion){
							Stats.setPatrolbox(true,1);
							Stats.setDeltaX(0); //Reset delta
							Stats.setDeltaY(0);
						}
					}
					//Checks if the third part of the box has been completed
					else if (Stats.getPatrolbox(2) == false) {
						//If the guards position is still not equal to the end position then do again
						if (Stats.getXPos() > Xstartpostion) { 
							Stats.setDirection(1); //Left
							if (checkstuck == 5){
								if(Stats.getX5ago() == Stats.getXPos() && Stats.getY5ago() == Stats.getYPos()) {
									Stats.setPatrolbox(true, 2);
									checkstuck = 0;
									return;
								}
								checkstuck = 0;
								Stats.setX5ago(Stats.getXPos());
								Stats.setY5ago(Stats.getYPos());
							}
							else {
								checkstuck++;
							}
							patrolLineX(Xstartpostion,Xendpostion);
						}
						//If the guards position is equal to the end (e.g. completed this section) mark as done and move onto next part
						else if (Stats.getXPos() <= Xstartpostion){
							Stats.setPatrolbox(true,2);
							Stats.setDeltaX(0); //Reset delta
							Stats.setDeltaY(0);
						}
					}
					//Checks if the fourth part of the box has been completed
					else if (Stats.getPatrolbox(3) == false) {
						//If the guards position is still not equal to the end position then do again
						if (Stats.getYPos() > Ystartpostion) {
							Stats.setDirection(2); //Up
							if (checkstuck == 5){
								if(Stats.getX5ago() == Stats.getXPos() && Stats.getY5ago() == Stats.getYPos()) {
									Stats.setPatrolbox(true, 3);
									checkstuck = 0;
									return;
								}
								checkstuck = 0;
								Stats.setX5ago(Stats.getXPos());
								Stats.setY5ago(Stats.getYPos());
							}
							else {
								checkstuck++;
							}
							patrolLineY(Ystartpostion,Yendpostion);
						}
						//If the guards position is equal to the end (e.g. completed this section) mark as done and move onto next part
						else if (Stats.getYPos() <= Ystartpostion){
							Stats.setPatrolbox(true,3); //Update current direction
							Stats.setDeltaX(0); //Reset delta
							Stats.setDeltaY(0);
						}
					}
					//Checks if the whole cycle has been completed, if so then reset and start it again
					if (Stats.getPatrolbox(0) && Stats.getPatrolbox(1) && Stats.getPatrolbox(2) && Stats.getPatrolbox(3) == true) {
						Stats.setPatrolbox(false,0);
						Stats.setPatrolbox(false,1);
						Stats.setPatrolbox(false,2);
						Stats.setPatrolbox(false,3);
					}
					detection(); //Check detections
					changeimage();
				}
				else if(Stats.getGuardIdle() == false) { //If the guard spots the player then follow them
					Stats.setSpeed(7);
					movetopostion(playerX, playerY);
				}
			}
		}
	}
	
	/**
	 * This method lets the guard randomly wander around the map until it finds the player. 
	 * It does this by generating a random number for the X and Y coordinates and then divides it by 10 then multiplys it by 10 so its divisible by 10.
	 * Then uses the move to position to get to that randomly generated position
	 */
	void wander() {
		if(Stats.getAlive() == true) {
			if(Stats.getReturning() == true) {
				guardreturn();
			}	
			else {
				if(Stats.getGuardIdle() == true) { //If guard isnt chasing anyone and moving freely
					Stats.setSpeed(2);
					if (Stats.getReachDes() == false) {
						if (checkstuck == 5){
							if(Stats.getX5ago() == Stats.getXPos() && Stats.getY5ago() == Stats.getYPos()) {
								Stats.setReachDes(true);
							}
							checkstuck = 0;
							Stats.setX5ago(Stats.getXPos());
							Stats.setY5ago(Stats.getYPos());
						}
						else {
							checkstuck++;
						}
					}
					else if (Stats.getReachDes() == true) { //If the guard needs a new destination
						
						randomx = (rand.nextInt((maxX - minX) + 1) + minX)/10; //Randomly generate 2 numbers then divide and mutiply by 10 to make sure steps work
						randomx = randomx * 10;
						randomy = (rand.nextInt((maxY - minY) + 1) + minY)/10;
						randomy = randomy * 10;
						Stats.setReachDes(false);	//Resets destnation reached
					}
					movetopostion(randomx,randomy);	//Moves guard to the random position chosen from the two numbers
					if (Stats.getXPos()+5 >= randomx && Stats.getXPos()-5 <= randomx) {	//If guard gets to the destination position then change destination reached to true
						if (Stats.getYPos()+5 >= randomy && Stats.getYPos()-5 <= randomy) {
							Stats.setReachDes(true); //Returned
						}
					}
					
				}
				else if(Stats.getGuardIdle() == false) { //If guard has spotted the player then follow
					Stats.setSpeed(7);
					movetopostion(playerX, playerY);
				}
			}
		}	
	}
		
	/**
	 * This allows the guard to move to a certain position on the screen given the x and y coordinates. 
	 * It does this by randomly generating a number between 1 and 2 to see if it should move X or Y. 
	 * It then decides if it should move left or right/ up or down depending on the players position compared to its own
	 * This is also used to make the guard chase the player
	 * 
	 * @param x The destinations X coordinate
	 * @param y The destinations Y coordinate
	 */
	void movetopostion(int x, int y) {
		if(Stats.getAlive() == true) {
			if(checkcone(Stats.getAttackDis())) {
				Stats.setAttacking(true);
			}
			else {
				Stats.setAttacking(false);
			}
			detection();
			if(Stats.getAttacking() == true) {
				
			}
			else {
				int numobstacle = obstaclelist.size();
				int movnumobstacle = BoxList.size();
				if (numobstacle != 0) {
					for (int i = 0; i<numobstacle; i=+4) { //Checks all objects
						if (x >= obstaclelist.get(i) && x <= obstaclelist.get(i) + obstaclelist.get(i+2)) { //Detects if the object is blocking the player on the X Axis
							if(y >= obstaclelist.get(i+1) && y >= obstaclelist.get(i+1) + obstaclelist.get(i+3)) { //Detects if its blocking the player on the Y Axis
								Stats.setReachDes(true);
							}
						}
					}
				}
				if (movnumobstacle != 0) {
					for (int i = 0; i<numobstacle; i++) { //Checks all objects
						if (x >= BoxList.get(i).getXPosition() && x <= BoxList.get(i).getXPosition() + BoxList.get(i).getXSize()) { //Detects if the object is blocking the player on the X Axis
							if(y >= BoxList.get(i).getYPosition() && y >= BoxList.get(i).getYPosition() + BoxList.get(i).getYSize()) { //Detects if its blocking the player on the Y Axis
								Stats.setReachDes(true);
							}
						}
					}
				}
	
				int XY = (int) (Math.random() * 2); 	//Decide whether to do X or Y
				if (XY == 0) {		//if 0 then move X
					if (Stats.getXPos()-5 > x) {	//If current position is more than the destination go left
						Stats.setDeltaX(-Stats.getSpeed());
						Stats.setDirection(1); //Left
					}
					else if (Stats.getXPos()+5 < x) {	//If current position is less than the destination go right
						Stats.setDeltaX(Stats.getSpeed());
						Stats.setDirection(0); //Right
					}
					else {
						Stats.setDeltaX(0); //If its already reached its destination then stay
					}
				} 
				else if (XY == 1){	//if 1 then move Y
					if (Stats.getYPos()-5 > y) {	//If current position is more than the destination go up
						Stats.setDeltaY(-Stats.getSpeed());
						Stats.setDirection(2); //Up
					}
					else if (Stats.getYPos()+5 < y) { 	//If current position is more than the destination go down
						Stats.setDeltaY(Stats.getSpeed());
						Stats.setDirection(3); //Down
					}
					else {
						Stats.setDeltaY(0); //If its already reached its destination then stay
					}
				}
				if (changecount == 17) {
					changeimage();
					changecount = 0;
				}
				changecount++;
				collision();	//Detect collisions with the wall and make sure it stays in boundries
				boxcCollision();
				Stats.setXPos(Stats.getXPos() + Stats.getDeltaX());	//Update the guards positions
				Stats.setYPos(Stats.getYPos() + Stats.getDeltaY());
	       
				GuardAI.move(Stats.getDeltaX(), Stats.getDeltaY());
			}
		}
	}
	
	/**
	 * Checks for collision with all the movable box and then deals with it
	 */
	void boxcCollision() {	//0 = right, 1 = left, 2 = up, 3 = down
		for(int i = 0; i < BoxList.size(); i++) {
			if (Stats.getXPos()+Stats.checkSize() <= (int)(BoxList.get(i).getXPosition()) && Stats.getXPos()+Stats.checkSize() >= (int)(BoxList.get(i).getXPosition()-10)) { //If the players X is equal to the right side of the box or 10 pixels into the right side of the box then stop
				if (Stats.getYPos() < (BoxList.get(i).getYPosition() + BoxList.get(i).getYSize()) && Stats.getYPos() > BoxList.get(i).getYPosition()-Stats.checkSize()) {
					if(Stats.getDirection() == 0) {
						Stats.setDeltaX(0);
						hitbox = true;
					}
				}
			}
			else if (Stats.getXPos() >= (int)(BoxList.get(i).getXPosition() + BoxList.get(i).getXSize())  && Stats.getXPos() <= (int)(BoxList.get(i).getXPosition()+BoxList.get(i).getXSize()+10)) {
				if (Stats.getYPos() < (BoxList.get(i).getYPosition() + BoxList.get(i).getYSize()) && Stats.getYPos() > BoxList.get(i).getYPosition()-Stats.checkSize()) {
					if(Stats.getDirection() == 1) {
					Stats.setDeltaX(0);
					hitbox = true;
					}
				}
			}
			else if (Stats.getYPos() >= (int)(BoxList.get(i).getYPosition() + BoxList.get(i).getYSize())  && Stats.getYPos() <= (int)(BoxList.get(i).getYPosition() + BoxList.get(i).getYSize()+10)) {
				if (Stats.getXPos() < (BoxList.get(i).getXPosition() + BoxList.get(i).getXSize()) && Stats.getXPos() > BoxList.get(i).getXPosition()-Stats.checkSize()) {
					if(Stats.getDirection() == 2) {
					Stats.setDeltaY(0);
					hitbox = true;
					}
				}
			}
			else if (Stats.getYPos()+Stats.checkSize() <= (int)(BoxList.get(i).getYPosition())  && Stats.getYPos()+Stats.checkSize() >= (int)(BoxList.get(i).getYPosition()-10)) {
				if (Stats.getXPos() < (BoxList.get(i).getXPosition() + BoxList.get(i).getXSize()) && Stats.getXPos() > BoxList.get(i).getXPosition()-Stats.checkSize()) {	
					if(Stats.getDirection() == 3) {
					Stats.setDeltaY(0);
					hitbox = true;
					}
				}
			}
			else {
				hitbox = false;
			}
		}
	}
	/**
	 * Player detection if the player is in the line of sight of the guard.
	 * Uses nested loops, 1 for each possible direction of the guard, 
	 * They search the view area of the guard which is specified in the variables sight distance and sight width.
	 * If the players x and y coordinates are in this area then the guard sets chase
	 */
	void detection(){
		int detected = 0;
		int conecounter = 50;
		int mean = (Stats.getSightDis()-50) / Stats.getSightWidth();
		int numobstacle = obstaclelist.size();
		int movnumobstacle = BoxList.size();
		
		if (Stats.getGuardIdle() == true) {
			if(Stats.getDirection() == 0) {		//0 = right, 1 = left, 2 = up, 3 = downs
				for(int x = Stats.checkSize(); x<Stats.getSightDis()+Stats.checkSize(); x++) { //Loop to check X pixels for the sight distance
					for(int y = -(conecounter/2); y<conecounter/2; y++) { //Loop to check Y pixels for the sight width
						if(playerX == Stats.getXPos()+x && playerY == Stats.getYPos()+y) { //If player position is in the checked pixel then mark as player being detected
							if (numobstacle == 0 && movnumobstacle == 0) {
								Stats.setChase();
								return;
							}
							else {
								if (numobstacle != 0) {
									for (int i = 0; i<numobstacle; i=+4) { //Checks all objects
										if (playerX >= obstaclelist.get(i) && Stats.getXPos() <=obstaclelist.get(i)) { //Detects if the object is blocking the player on the X Axis
											if(playerY >= obstaclelist.get(i+1) && playerY <= obstaclelist.get(i+1)+ obstaclelist.get(i+3)) { //Detects if its blocking the player on the Y Axis
												return;
											}
										}
									}
								}
								if (movnumobstacle != 0) {
									for (int i = 0; i<movnumobstacle; i++) { //Checks all object
										if (playerX >= BoxList.get(i).getXPosition() && Stats.getXPos() <= BoxList.get(i).getXPosition()) { //Detects if the object is blocking the player on the X Axis
											if(playerY >= BoxList.get(i).getYPosition() && playerY <= BoxList.get(i).getYPosition() + BoxList.get(i).getYSize()) { //Detects if its blocking the player on the Y Axis
												return;
											}
										}
									}
								}
							}
							Stats.setChase();
							return;
						}
					}
					if(Stats.getSightWidth() > conecounter) {
						conecounter = conecounter + mean ;
					}
				}
			}
			else if(Stats.getDirection() == 1) {		//0 = right, 1 = left, 2 = up, 3 = down
				for(int x = 0; x<Stats.getSightDis(); x++) {	//Loop to check X pixels for the sight distance
					for(int y = -(conecounter/2); y<conecounter/2; y++) {	//Loop to check Y pixels for the sight width
						if(playerX == Stats.getXPos()-x && playerY == Stats.getYPos()+y) { 	//If player position is in the checked pixel then mark as player being detected
							if (numobstacle == 0) {
								Stats.setChase();
								return;
							}
							else {
								if (numobstacle != 0) {
									for (int i = 0; i<numobstacle; i=+4) { //Checks all objects
										if (playerX <= obstaclelist.get(i) && Stats.getXPos() >= obstaclelist.get(i)) { //Detects if the object is blocking the player on the X Axis
											if(playerY >= obstaclelist.get(i+1) && playerY <= obstaclelist.get(i+1)+ obstaclelist.get(i+3)) { //Detects if its blocking the player on the Y Axis
												return;
											}
										}
									}
								}
								if (movnumobstacle != 0) {
									for (int i = 0; i<numobstacle; i++) { //Checks all objects
										if (playerX <= BoxList.get(i).getXPosition() && Stats.getXPos() >= BoxList.get(i).getXPosition()) { //Detects if the object is blocking the player on the X Axis
											if(playerY >= BoxList.get(i).getYPosition() && playerY <= BoxList.get(i).getYPosition() + BoxList.get(i).getYSize()) { //Detects if its blocking the player on the Y Axis
												return;
											}
										}
									}
								}
							}
							Stats.setChase();
							return;
						}
					}
					if(Stats.getSightWidth() > conecounter) {
						conecounter = conecounter + mean ;
					}
				}
			}
			else if(Stats.getDirection() == 2) {		//0 = right, 1 = left, 2 = up, 3 = down
				for(int y = 0; y<Stats.getSightDis(); y++) { 	//Loop to check Y pixels for the sight distance
					for(int x = -(conecounter/2); x<conecounter/2; x++) { //Loop to check X pixels for the sight width
						if(playerX == Stats.getXPos()+x && playerY == Stats.getYPos()-y) {	//If player position is in the checked pixel then mark as player being detected
							if (numobstacle == 0) {
								Stats.setChase();
								return;
							}
							else {
								if (numobstacle != 0) {
									for (int i = 0; i<numobstacle; i=+4) { //Checks all objects
										if (playerY <= obstaclelist.get(i+1) && Stats.getYPos() >= obstaclelist.get(i+1)) { //Detects if the object is blocking the player on the X Axis
											if(playerX >= obstaclelist.get(i) && playerX <= obstaclelist.get(i)+ obstaclelist.get(i+2)) { //Detects if its blocking the player on the Y Axis
												return;
											}
										}
									}
								}
								if (movnumobstacle != 0) {
									for (int i = 0; i<numobstacle; i++) { //Checks all objects
										if (playerY <= BoxList.get(i).getXPosition() && Stats.getYPos() >= BoxList.get(i).getYPosition()) { //Detects if the object is blocking the player on the X Axis
											if(playerX >= BoxList.get(i).getXPosition() && playerY <= BoxList.get(i).getXPosition() + BoxList.get(i).getXSize()) { //Detects if its blocking the player on the Y Axis
												return;
											}
										}
									}
								}
							Stats.setChase();
							return;
							}
						}
					if(Stats.getSightWidth() > conecounter) {
						conecounter = conecounter + mean ;
						}
					}
				}
			}
			else if(Stats.getDirection() == 3) {		//0 = right, 1 = left, 2 = up, 3 = down
				for(int y = Stats.checkSize(); y<Stats.getSightDis()+Stats.checkSize(); y++) {	//Loop to check Y pixels for the sight distance
					for(int x = -(conecounter/2); x<conecounter/2; x++) { //Loop to check X pixels for the sight width
						if(playerX == Stats.getXPos()+x && playerY == Stats.getYPos()+y) {	//If player position is in the checked pixel then mark as player being detected
							if (numobstacle == 0) {
								Stats.setChase();
								return;
							}
							else {
								if (numobstacle != 0) {
									for (int i = 0; i<numobstacle; i=+4) { //Checks all objects
										if (playerY >= obstaclelist.get(i+1) && Stats.getYPos() <= obstaclelist.get(i+1)) { //Detects if the object is blocking the player on the X Axis
											if(playerX >= obstaclelist.get(i) && playerX <= obstaclelist.get(i)+ obstaclelist.get(i+2)) { //Detects if its blocking the player on the Y Axis
												return;
											}
										}
									}
								}
								if (movnumobstacle != 0) {
									for (int i = 0; i<numobstacle; i++) { //Checks all objects
										if (playerY >= BoxList.get(i).getXPosition() && Stats.getYPos() <= BoxList.get(i).getYPosition()) { //Detects if the object is blocking the player on the X Axis
											if(playerX >= BoxList.get(i).getXPosition() && playerY <= BoxList.get(i).getXPosition() + BoxList.get(i).getXSize()) { //Detects if its blocking the player on the Y Axis
												return;
											}
										}
									}
								}
							Stats.setChase();
							return;
						}
					}
					if(Stats.getSightWidth() > conecounter) {
						conecounter = conecounter + mean ;
					}
				}
			}
		}
		else if (Stats.getGuardIdle() == false) {
			for(int x = -Stats.getSightToLose(); x<Stats.getSightToLose(); x++) {	//Loop to check Y pixels for the sight distance
				for(int y = -Stats.getSightToLose(); y<Stats.getSightToLose(); y++) { //Loop to check X pixels for the sight width
					if(playerX == Stats.getXPos()+x && playerY == Stats.getYPos()+y) {	//If player position is in the checked pixel then mark as player being detected
						detected++;
					}
				}
			}
			if(detected == 0) {
				Stats.setGuardIdle(true);
				movetopostion(Stats.getXPrePos(), Stats.getYPrePos());
				Stats.setReturning(true);
				Stats.setAttacking(false);
			}
		}
		}
	}
	/**
	 * Checks a cone to see if the player is inside it
	 * @return	if the player is there or not (True or False)
	 */
	boolean checkcone(int AttackDistance){
		int conecounter = 5;
		int mean = (Stats.getSightDis()-50) / Stats.getSightWidth();
		
		if(Stats.getDirection() == 0) {	
			for(int x = 0; x<AttackDistance; x++) { //Loop to check X pixels for the sight distance
				for(int y = -(conecounter/2); y<(conecounter/2); y++) { //Loop to check Y pixels for the sight width
					if(playerX == Stats.getXPos()+x && playerY == Stats.getYPos() + y) {
						return true;
					}
				}
				if(AttackDistance*2 > conecounter) {
					conecounter = conecounter + mean ;
				}

			}
		}
		else if(Stats.getDirection() == 1) {
			for(int x = 0; x<AttackDistance; x++) { //Loop to check X pixels for the sight distance
				for(int y = -(conecounter/2); y<(conecounter/2); y++) { //Loop to check Y pixels for the sight width
					if(playerX == Stats.getXPos()-x && playerY == Stats.getYPos() + y) {
						return true;
					}
				}
				if(AttackDistance*2 > conecounter) {
					conecounter = conecounter + mean ;
				}
			}
		}
		else if(Stats.getDirection() == 2) {
			for(int y = 0; y<AttackDistance; y++) { //Loop to check X pixels for the sight distance
				for(int x = -(conecounter/2); x<(conecounter/2); x++) { //Loop to check Y pixels for the sight width
					if(playerX == Stats.getXPos()+x && playerY == Stats.getYPos() - y) {
						return true;
					}
				}
				if(AttackDistance*2 > conecounter) {
					conecounter = conecounter + mean ;
				}
			}
		}
		else if(Stats.getDirection() == 3) {
			for(int y = 0; y<AttackDistance; y++) { //Loop to check X pixels for the sight distance
				for(int x = -(conecounter/2); x<(conecounter/2); x++) { //Loop to check Y pixels for the sight width
					if(playerX == Stats.getXPos()+x && playerY == Stats.getYPos() + y) {
						return true;
					}
				}
				if(AttackDistance*2 > conecounter) {
					conecounter = conecounter + mean ;
				}
			}
		}
		return false;
	}
	/**
	 * Checks a circle radius to see if the player is inside it
	 * @param xdiameter	The diameter of the circle to check
	 * @return	if the player is there or not (True or False)
	 */
	boolean checkcircle(int xdiameter) {
		boolean reverse = false;
		int conecounter = 4;
		int mean = xdiameter-4 / 2;
		int startX = Stats.getXPos() - xdiameter/2+(getStats().checkSize()/2);
		int endX = Stats.getXPos() + xdiameter/2+(getStats().checkSize()/2);
		Stats.getYPos();
		
		for(int x = startX; x<endX; x++) { //Loop to check X pixels for the sight distance
			for(int y = Stats.getYPos()-(conecounter/2); y<Stats.getYPos()+(conecounter/2); y++) { //Loop to check Y pixels for the sight width
				if(playerX == x && playerY == y) {
					return true;
				}
			}
			if (reverse == true) {
				conecounter = conecounter + mean;
			}
			else {
				conecounter = conecounter + mean;
			}
			if (conecounter == xdiameter) {
				reverse = true;
			}
		}
		return false;
	}
	
	/**
	 * This gets and sets and players coordinates so the guard knows where to go
	 * @param X Players X Coordinate
	 * @param Y	Players Y Coordinate
	 */
	void GuardsetPlayerCo(int X, int Y) { //Set new player coordinates
		playerX = X;
		playerY = Y;
	}
	/**
	 * Makes a static Obstacle for the guard
	 * @param x	The X coordinate of the Obstacle
	 * @param y	The Y coordinate of the Obstacle
	 * @param xSize	The X size of the Obstacle
	 * @param ySize	The Y size of the Obstacle
	 */
	void makeStaticObstacle(int x, int y, int xSize, int ySize) {
		obstaclelist.add(x);
		obstaclelist.add(y);
		obstaclelist.add(xSize);
		obstaclelist.add(ySize);
	}
	/**
	 * Makes a movable Obstacle for the guard by adding the box instance to the guard
	 * @param Box The movable box instance
	 */
	void makeMovableObstacle(MovableBox Box) {
		BoxList.add(Box);
	}
	/**
	 * Border collision to make sure the guard doesn't go off the floor and onto the walls
	 */
	void collision() {
		if (Stats.getXPos() >= maxX) {
			if(Stats.getDirection() == 0) {
				Stats.setDeltaX(0);
			}
	    }
	    if (Stats.getYPos() >= maxY) {
	    	if(Stats.getDirection() == 3) {
				Stats.setDeltaY(0);
			}
	    }
	    if (Stats.getXPos() <= minX) {
	    	if(Stats.getDirection() == 1) {
				Stats.setDeltaX(0);
			}
	    	
	    }
	    if (Stats.getYPos() <= minY) {
	    	if(Stats.getDirection() == 2) {
				Stats.setDeltaX(0);
			}
	    	
	    }
	}
	
	/**
	 * sets the guard to attack
	 */
	void attacking() {
		Stats.setAttacking(true);
	}
	
	/**
	 * gets the obstacle list
	 * @return obstaclelist The obstacle list of all the obstacles the guard has (Static)
	 */
	List<Integer> getobstaclelist() {
		return obstaclelist;
	}
	/**
	 * Changes the image of the guard based on his direction
	 */
	void changeimage() {
		if (Stats.getDirection() == 0) {
			GuardAI.setTexture(player_R);
		}
		if (Stats.getDirection() == 1) {
			GuardAI.setTexture(player_L);
		}
		if (Stats.getDirection() == 2) {
			GuardAI.setTexture(player_U);
		}
		if (Stats.getDirection() == 3) {
			GuardAI.setTexture(player_D);
		}
	}
	
	/**
	 * Starts the guard returning to his previous position
	 */
	void guardreturn() {
		Stats.setSpeed(1);
		movetopostion(Stats.getXPrePos(), Stats.getYPrePos());
		
		if (Stats.getXPos()+5 >= Stats.getXPrePos() && Stats.getXPos()-5 <= Stats.getXPrePos()) {
			if (Stats.getYPos()+5 >= Stats.getYPrePos() && Stats.getYPos()-5 <= Stats.getYPrePos()) {
				Stats.setReturning(false); //Returned
			}
		}
	}
	/**
	 * Sets the boundaries for the level
	 * @param maxX This is the max X coordinate the guard can go to
	 * @param minX This is the min X coordinate the guard can go to
	 * @param maxY This is the max Y coordinate the guard can go to
	 * @param minY This is the min Y coordinate the guard can go to
	 */
    void setBoundaries(int minX, int maxX, int minY, int maxY) {
    	this.maxX = maxX;
    	this.maxY = maxY;
    	this.minX = minX;
    	this.minY = minY;
    }

    /**
     * Checks if the AI is alive
     */
    void checkAlive() {
        if (!(Stats.getAlive())) {
            GuardAI.setTexture(dead);
        }
    }
    /**
     * Returns all the AI stats
     * @return
     */
    AIStats getStats() {
        return Stats;
    }
    /**
     * Updates the cone to move with 
     */
    void UpdateCone(){
    	if(Stats.getenemytype() == "Guard") {
    		if (Stats.getAlive() == true) {
    			if (Stats.getIdlestate() == true) {
    				if (Stats.getDirection() == 0) {
    					window.draw(GuardConeRight);
    				}
    				else if (Stats.getDirection() == 1) {
    					window.draw(GuardConeLeft);
    				}
    				else if (Stats.getDirection() == 2) {
    					window.draw(GuardConeUp);
    				}
    				else if (Stats.getDirection() == 3) {
    					window.draw(GuardConeDown);
    				}
    			}
    			if (Stats.getDirection() == 0) {
    				Stats.setConeLXPos(Stats.getConeLXPos()+Stats.getDeltaX());
    				Stats.setConeRXPos(Stats.getConeRXPos()+Stats.getDeltaX());
    				Stats.setConeUXPos(Stats.getConeUXPos()+Stats.getDeltaX());
    				Stats.setConeDXPos(Stats.getConeDXPos()+Stats.getDeltaX());
    			}
    			else if (Stats.getDirection() == 1) {
    				Stats.setConeLXPos(Stats.getConeLXPos()-Stats.getDeltaX());
    				Stats.setConeRXPos(Stats.getConeRXPos()-Stats.getDeltaX());
    				Stats.setConeUXPos(Stats.getConeUXPos()-Stats.getDeltaX());
    				Stats.setConeDXPos(Stats.getConeDXPos()-Stats.getDeltaX());
    			}
    			else if (Stats.getDirection() == 2) {
    				Stats.setConeLYPos(Stats.getConeLYPos()-Stats.getDeltaY());
    				Stats.setConeRYPos(Stats.getConeRYPos()-Stats.getDeltaY());
    				Stats.setConeUYPos(Stats.getConeUYPos()-Stats.getDeltaY());
    				Stats.setConeDYPos(Stats.getConeDYPos()-Stats.getDeltaY());
    			}
    			else if (Stats.getDirection() == 3) {
    				Stats.setConeLYPos(Stats.getConeLYPos()+Stats.getDeltaY());
    				Stats.setConeRYPos(Stats.getConeRYPos()+Stats.getDeltaY());
    				Stats.setConeUYPos(Stats.getConeUYPos()+Stats.getDeltaY());
    				Stats.setConeDYPos(Stats.getConeDYPos()+Stats.getDeltaY());
    			}
    			GuardConeLeft.move(Stats.getDeltaX(), Stats.getDeltaY());
    			GuardConeRight.move(Stats.getDeltaX(), Stats.getDeltaY());
    			GuardConeUp.move(Stats.getDeltaX(), Stats.getDeltaY());
    			GuardConeDown.move(Stats.getDeltaX(), Stats.getDeltaY());
    		}
    	}
    }

	void damageFlash() {
		GuardAI.setColor(new Color(255, 0, 0, 255));
		Timer timer2 = new Timer();
		timer2.schedule(new TimerTask() {
			public void run() {
				GuardAI.setColor(new Color(255, 255, 255, 255));
			}
		}, 200);
	}
}