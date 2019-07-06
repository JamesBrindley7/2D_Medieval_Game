package game;

import java.io.IOException;
import java.util.Random;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;

/**
 * The guard stats, these are unique to the guard and are accessed from the guard class
 * @author James Brindley
 *
 */
public class AIStats {
	
    private int deltaX = 0;  //X offset
    private int deltaY = 0;	//Y offset
    private int xPosition;	//Current X position
    private int yPosition;	//Current Y position
    private int xPreviousPosition;	//X position before player was sighted
    private int yPreviousPosition;	//Y positon before player was sighted
    private int X5ago;
    private int Y5ago;
    
    
    private int ConeDxPosition;	//Current X position
    private int ConeDyPosition;	//Current Y position
    private int ConeUxPosition;	//Current X position
    private int ConeUyPosition;	//Current Y position
    private int ConeRxPosition;	//Current X position
    private int ConeRyPosition;	//Current Y position
    private int ConeLxPosition;	//Current X position
    private int ConeLyPosition;	//Current Y position
    
    
    private int speed = 5;	//1 = patrol speed, 2 = normal movement speed, 5 = chasing speed
    private int attackdistance;	//Attacking distance	
    
    private int LastAttack = 0;
    private int primaryattackspeed;
    private int secondaryattackspeed;
    private int tertiaryattackspeed;
    
    private int primarydamage;	//Attack Damage
    private int secondarydamage;	//Attack Damage
    private int tertiarydamage;	//Attack Damage
    
    private int health;	//Guard Health
    private int sightdistance = 200;	//Guards sight distance to see the player first
    private int sighttolose = 400;	//Guards sight distance to lose the player
    private int sightwidth = 150; 	//Guards sight width to see the player
    private String enemyname;
    private String enemytype;
 
    
   
    private int direction = 3; //0 = right, 1 = left, 2 = up, 3 = down
    private boolean guardIdle = true; //true = Idle, false = chasing player
    private boolean returning = false; //Returning to old position after chase
    private boolean alive = true;	//Guards alive status
    private boolean attacking = false;	//Guards attacking status
    
    
    private boolean patrolDirectionX = false; //false = right, true = left
    private boolean patrolDirectionY = false; //false = up, true = down
    private boolean[] patrolboxstatus = new boolean[4]; 	//Stages of patrol completed
    private boolean destinationreached = true;	//Destination status of the guard
    RenderWindow window;
    
    public AIStats(RenderWindow window, int xPosition, int yPosition, int attackdistance, int primarydamage, int secondarydamage, int tertiarydamage, int health, int sightdistance, int sighttolose, int sightwidth, String enemyname, String enemytype, int primaryattackspeed, int secondaryattackspeed, int tertiaryattackspeed) throws IOException {
        //Initialise all the variables
    	this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.attackdistance = attackdistance;
        this.primarydamage = primarydamage;
        this.secondarydamage = secondarydamage;
        this.tertiarydamage = tertiarydamage;
        this.health = health;
        this.sightdistance = sightdistance;
        this.sighttolose = sighttolose;
        this.sightwidth = sightwidth;
        this.enemyname = enemyname;
        this.primaryattackspeed = primaryattackspeed;
        this.secondaryattackspeed = secondaryattackspeed;
        this.tertiaryattackspeed = tertiaryattackspeed;
        this.enemytype = enemytype;
        this.window = window;
    }
    RenderWindow getwindow() {
		return window;
    }
    /**
     * Gets the size of the guards texture
     * @return
     */
    int checkSize() {
    	if (enemytype == "Guard") {
    		return 80;
    	}
    	if (enemytype == "Boss") {
    		if (enemyname == "Boss1" || enemyname == "Boss3") {
    			return 120;	
    		}
    		else if(enemyname == "Boss2") {
    			return 200;
    		}
    	}
    	return 80;
    }
    /**
     * Method to deduct health from the guard
     * @param damage The amount of damage to deduct from the the guard
     */
    void takeDamage(int damage) {
		this.health -= damage;
        System.out.println(health);
		if (health <= 0) {
	      	alive = false;
	      	System.out.println(alive);

	        }
	}
    
    /**
     * This gets the alive status of the guard
     * @return alive The alive status of the guard
     */
    boolean getalivestatus(){
    	return alive;
    }
    boolean checkprimaryattack() {
		if (LastAttack >= primaryattackspeed) {
			LastAttack=0;
			return true;
		}
		else {
			LastAttack++;
			return false;
		}
    }
    boolean checksecondaryattack() {
		if (LastAttack >= secondaryattackspeed) {
			LastAttack=0;
			return true;
		}
		else {
			LastAttack++;
			return false;
		}
    }
    boolean checktertiaryattack() {
		if (LastAttack >= tertiaryattackspeed) {
			LastAttack=0;
			return true;
		}
		else {
			LastAttack++;
			return false;
		}
    }
	
    /**
     * This unleashes the guard to follow the player and saves previous coordinates so the guard can return
     */
	void setChase() {
		guardIdle = false;
		xPreviousPosition = xPosition;
		yPreviousPosition = yPosition;
	}
	
	/**
	 * Makes the guard return to the old position before the chase 
	 */
	void setIdle() {
    	guardIdle = true;
    }
	
	/**
	 * Returns the status of the guard, if hes idle or not
	 * @return guardIdle The idle status of the guard
	 */
	boolean getIdlestate() {
		return guardIdle;
	}
	
	/**
	 * Gets the damage stat of the guard
	 * @return damage The damage the guard inflicted on a hit
	 */
    int getPrimaryDamageStat() {
    	return primarydamage;
    }
    /**
	 * Gets the damage stat of the guard
	 * @return damage The damage the guard inflicted on a hit
	 */
    int getSecondartDamageStat() {
    	return secondarydamage;
    }
    /**
	 * Gets the damage stat of the guard
	 * @return damage The damage the guard inflicted on a hit
	 */
    int getTertiaryDamageStat() {
    	return tertiarydamage;
    }
    /**
	 * Sets the offset of the past position
	 * @param deltaX X coordinate offset
	 */
	void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }
	
	/**
	 * Sets the offset of the past position
	 * @param deltaY Y coordinate offset
	 */
    void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }
    /**
	 * Sets the offset of the past position
	 * @param deltaX X coordinate offset
	 */
	int getDeltaX() {
        return deltaX;
    }
	
	/**
	 * Sets the offset of the past position
	 * @param deltaY Y coordinate offset
	 */
    int getDeltaY() {
        return deltaY;
    }
	
    /**
     * Returns the current X Position
     * @return xPosition The X position of the guard
     */
    int getXPos() {
        return xPosition;
    }

    /**
     * Returns the current Y Position
     * @return yPosition The Y position of the guard
     */
    int getYPos() {
        return yPosition;
    }
    /**
     * Updates the X position of the guard
     * @param xPosition New X position
     */
    void setXPos(int xPosition) {
        this.xPosition = xPosition;
    }
    /**
     * Updates the Y position of the guard
     * @param yPosition New Y Position
     */
    void setYPos(int yPosition) {
    	this.yPosition = yPosition;
    }
    /**
     * Gets the previous X position, where the guard was at before it started to chase the player
     * @return xPreviousPosition The last X position
     */
    int getXPrePos() {
        return xPreviousPosition;
    }
    /**
     * Gets the previous Y position, where the guard was at before it started to chase the player
     * @return yPreviousPosition The last Y position
     */
    int getYPrePos() {
    	return yPreviousPosition;
    }
    /**
     * Sets a new Y position to save so that the guard can go back 
     * @param xPosition The current X position
     */
    void setYPrePos(int xPosition) {
        this.xPreviousPosition = xPosition;
    }
    /**
     * Sets a new X position to save so that the guard can go back
     * @param yPosition The current Y position
     */
    void setXPrePos(int yPosition) {
    	this.yPreviousPosition = yPosition;
    }
    /**
     * Sets a new speed for the guard to go at
     * @param speed The speed the guard travels at
     */
    void setSpeed(int speed) {
    	this.speed = speed;
    }
    /**
     * Gets the current speed that the guard is travelling at
     * @return speed The speed the guard is walking/running
     */
    int getSpeed() {
    	return speed;
    }
    /**
     * Gets the attacking distance of the guard
     * @return attackdistance The range the guard can attack from
     */
    int getAttackDis() {
    	return attackdistance;
    }
    /**
     * Gets the health of the guard
     * @return health The current health
     */
    int getHealth() {
    	return health;
    }
    /**
     * Gets the sight distance, the distance the player can be seen by the guard
     * @return sightdistance The distance the guard can see the player from
     */
    int getSightDis() {
    	return sightdistance;
    }
    /**
     * Gets the sight to lose distance, this is where the player is out of view from the guard
     * @return sighttolose The distance to lose track of the player
     */
    int getSightToLose() {
    	return sighttolose;
    }
    /**
     * Gets the sight width, the width of the guards vision
     * @return sightwidth The width of the guards view
     */
    int getSightWidth() {
    	return sightwidth;
    }
    /**
     * Sets the direction the guard is facing. 0 = right, 1 = left, 2 = up, 3 = down
     * @param direction The direction the guard is facing
     */
    void setDirection(int direction) {
    	this.direction = direction;
    }
    /**
     * Gets the direction the guard is facing
     * @return direction the direction the guard is looking
     */
    int getDirection() {
    	return direction;
    }
    /**
     * Sets a new new guard idle status, e.g. is the guard chasing the player
     * @param status Is the guard in pursuit of the player
     */
    void setGuardIdle(boolean status) {
    	this.guardIdle = status;
    }
    /**
     * Gets the current status of the guards idle status. True is patroling, false is chasing
     * @return guardIdle The status of the guards idle status
     */
    boolean getGuardIdle() {
    	return guardIdle;
    }
    /**
     * Sets the current guard status of returning to its original position
     * @param returning The status of the guards return
     */
    void setReturning(boolean returning) {
    	this.returning = returning;
    }
    /**
     * gets the current guard returning status
     * @return returning The status of the guards return
     */
    boolean getReturning() {
    	return returning;
    }
    /**
     * sets the alive status of the guard
     * @param alive Is the guard alive
     */
    void setAlive(boolean alive) {
    	this.alive = alive;
    }
    /**
     * gets the current alive status of the guard
     * @return alive The current alive status
     */
    boolean getAlive() {
    	return alive;
    }
    /**
     * Sets attacking status of the guard
     * @param attacking The attacking status
     */
    void setAttacking(boolean attacking) {
    	this.attacking = attacking;
    }
	/**
	 * Returns the attacking state of the guard
	 * @return attacking If the guard is attacking or not (e.g. in range)
	 */
    boolean getAttacking() {
    	return attacking;
    }
    /**
 	* Returns the attacking speed of the guard
 	* @return attackspeed the speed the guard can attack
 	*/
    int getPrimaryAttackSpeed() {
    	return primaryattackspeed;
    }
    /**
     * Returns the attacking speed of the guard
     * @return attackspeed the speed the guard can attack
     */
    int getSecondaryAttackSpeed() {
    	return secondaryattackspeed;
    }
    /**
     * Returns the attacking speed of the guard
     * @return attackspeed the speed the guard can attack
     */
    int getTertiaryAttackSpeed() {
    	return tertiaryattackspeed;
    }
    /**
     * sets the patrol direction on the X coordinate for the guard
     * @param direction The direction the guard is travelling
     */
    void setPatrolDirX(boolean direction) {
    	this.patrolDirectionX = direction;
    }
    /**
     * gets the current patrol direction on the X coordinate for the guard
     * @return direction The direction the guard is travelling
     */
    boolean getPatrolDirX() {
    	return patrolDirectionX;
    }
    /**
     * sets the patrol direction on the Y coordinate for the guard
     * @param direction The direction the guard is travelling
     */
    void setPatrolDirY(boolean direction) {
    	this.patrolDirectionY = direction;
    }
    /**
     * gets the current patrol direction on the Y coordinate for the guard
     * @return direction The direction the guard is travelling
     */
    boolean getPatrolDirY() {
    	return patrolDirectionY;
    }
    /**
     * sets the patrol box completion status of the specified stage
     * @param status The new status of completion of the stage
     * @param stage The stage of the box
     */
    void setPatrolbox(boolean status, int stage) {
    	patrolboxstatus[stage] = status;
    }
    /**
     * get the patrol box completion status
     * @param stage The wanted part of the cycle to check completion
     * @return	patrolboxstatus[stage] The status of the guard patroling in a box
     */
    boolean getPatrolbox(int stage) {
    	return patrolboxstatus[stage];
    }
    /**
     * sets the status of the guard of reaching its destination
     * @param reached The new status of the guard
     */
    void setReachDes(boolean reached) {
    	this.destinationreached = reached;
    }
    /**
     * Gets the status of the guard if its reached its destination
     * @return destinationreached The status of the guard
     */
    boolean getReachDes() {
    	return destinationreached;
    }
    int getX5ago() {
    	return X5ago;
    }
    int getY5ago() {
    	return Y5ago;
    }
    void setX5ago(int X5ago) {
    	this.X5ago = X5ago;
    }
    void setY5ago(int Y5ago) {
    	this.Y5ago = Y5ago;
    }
    
    
    /**
     * Returns the current X Position
     * @return xPosition The X position of the guard
     */
    int getConeDXPos() {
        return ConeDxPosition;
    }

    /**
     * Returns the current Y Position
     * @return yPosition The Y position of the guard
     */
    int getConeDYPos() {
        return ConeDyPosition;
    }
    /**
     * Updates the X position of the guard
     * @param xPosition New X position
     */
    void setConeDXPos(int ConeDxPosition) {
        this.ConeDxPosition = ConeDxPosition;
    }
    /**
     * Updates the Y position of the guard
     * @param yPosition New Y Position
     */
    void setConeDYPos(int ConeDyPosition) {
    	this.ConeDyPosition = ConeDyPosition;
    }
    
    /**
     * Returns the current X Position
     * @return xPosition The X position of the guard
     */
    int getConeUXPos() {
        return ConeUxPosition;
    }

    /**
     * Returns the current Y Position
     * @return yPosition The Y position of the guard
     */
    int getConeUYPos() {
        return ConeUyPosition;
    }
    /**
     * Updates the X position of the guard
     * @param xPosition New X position
     */
    void setConeUXPos(int ConeUxPosition) {
        this.ConeUxPosition = ConeUxPosition;
    }
    /**
     * Updates the Y position of the guard
     * @param yPosition New Y Position
     */
    void setConeUYPos(int ConeUyPosition) {
    	this.ConeUyPosition = ConeUyPosition;
    }
    
    /**
     * Returns the current X Position
     * @return xPosition The X position of the guard
     */
    int getConeLXPos() {
        return ConeLxPosition;
    }

    /**
     * Returns the current Y Position
     * @return yPosition The Y position of the guard
     */
    int getConeLYPos() {
        return ConeLyPosition;
    }
    /**
     * Updates the X position of the guard
     * @param xPosition New X position
     */
    void setConeLXPos(int ConeLxPosition) {
        this.ConeLxPosition = ConeLxPosition;
    }
    /**
     * Updates the Y position of the guard
     * @param yPosition New Y Position
     */
    void setConeLYPos(int ConeLyPosition) {
    	this.ConeLyPosition = ConeLyPosition;
    }
    
    /**
     * Returns the current X Position
     * @return xPosition The X position of the guard
     */
    int getConeRXPos() {
        return ConeRxPosition;
    }

    /**
     * Returns the current Y Position
     * @return yPosition The Y position of the guard
     */
    int getConeRYPos() {
        return ConeRyPosition;
    }
    /**
     * Updates the X position of the guard
     * @param xPosition New X position
     */
    void setConeRXPos(int ConeRxPosition) {
        this.ConeRxPosition = ConeRxPosition;
    }
    /**
     * Updates the Y position of the guard
     * @param yPosition New Y Position
     */
    void setConeRYPos(int ConeRyPosition) {
    	this.ConeRyPosition = ConeRyPosition;
    }
    String getenemytype() {
    	return enemytype;
    }
    
}
