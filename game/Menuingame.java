package game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Texture;
/**
 * In game menu for regicide
 * @author James Brindley
 *
 */
public class Menuingame {
	File save = new File("Save.txt");
	List<Integer> level = new ArrayList<Integer>();
	
	private RenderWindow window;
	KeyboardHandler kh;
	private Font font = new Font();
    private Text Resume;
    private Text Save;
    private Text Exit;
    private Texture background;
    private int levelnum;
    private int change = 1;

	private Sprite backgroundRoom;
	
	private List<Text> options = new ArrayList();
	private int currentchoice = 0;
	private int check = 0;
	private int wpress = 0;
	private int spress = 0;
	private int shiftpress = 0;
	/**
	 * Constuctor for the menu
	 * @param window The window to display the menu too
	 * @param kh The keyboard handler
	 * @param levelnum The current level number the player is on
	 * @throws IOException
	 */
	Menuingame(RenderWindow window, KeyboardHandler kh, int levelnum) throws IOException{
		this.window = window;
		this.kh = kh;
	        this.levelnum = levelnum;
	        
	        font.loadFromFile(Paths.get("sprites/Kavivanar-Regular.ttf"));
	        Resume = new Text("Resume", font, 64);
	        Resume.setPosition(530, 200);
	        
	        Save = new Text("Save", font, 64);
	        Save.setPosition(570, 300);
	        
	        Exit = new Text("Exit", font, 64);
	        Exit.setPosition(585, 400);
	        
	        options.add(Resume);
	        options.add(Save);
	        options.add(Exit);

	        displaymenu(kh);
	       
	}
	/**
	 * Loop to display the menu and keep it displayed until a button is clicked
	 * @param kh
	 * @throws IOException
	 */
	void displaymenu(KeyboardHandler kh) throws IOException {
		change =1;
		while(change == 1){
			kh.checkLoop();
			window.clear();

	        window.draw(Resume);
	        window.draw(Save);
	        window.draw(Exit);
	        
	        window.display();
	        updatemenu();
		}
	}
	/**
	 * If resume is pressed then stop the loop allowing it to display the game again
	 * @throws IOException
	 */
	void resumepressed() throws IOException {
		change = 0;
	}
	/**
	 * If the save button is pressed then make a new writer and write the level number to the save file
	 * @param levelnumber The level number
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	void savepressed(int levelnumber) throws NumberFormatException, IOException {
		BufferedWriter writer = null;
		String level = Integer.toString(levelnumber);
		try
		{
		    writer = new BufferedWriter( new FileWriter("Save.txt"));
		    writer.write(level);
		}
		catch ( IOException e)
		{
		}
		finally
		{
			writer.close();
			resumepressed();
		}
	}
	/**
	 * If exit is pressed then exit the game
	 */
	void exitpressed() {
		System.exit(0);
	}
	/**
	 * The method that updates the menus buttons and checks the buttons pressed
	 * @throws IOException
	 */
	void updatemenu() throws IOException {
		if (currentchoice == 0) {
			options.get(0).setColor(Color.RED);
			options.get(1).setColor(Color.WHITE);
			options.get(2).setColor(Color.WHITE);
		}
		else if (currentchoice == 1) {
			options.get(0).setColor(Color.WHITE);
			options.get(1).setColor(Color.RED);
			options.get(2).setColor(Color.WHITE);
		}
		else if (currentchoice == 2) {
			options.get(0).setColor(Color.WHITE);
			options.get(1).setColor(Color.WHITE);
			options.get(2).setColor(Color.RED);
		}
		if (kh.wPressed()) {
            wpress = 1;
        }
        else {
            if (wpress == 1) {
            	currentchoice--;
            }
            wpress = 0;
        }
		if (kh.sPressed()) {
            spress = 1;
        }
        else {
            if (spress == 1) {
            	currentchoice++;
            }
            spress = 0;
        }
		if (currentchoice < 0) {
			currentchoice = 2;
		}
		else if (currentchoice > 2) {
			currentchoice = 0;
		}
		if (kh.enterPressed()) {
            shiftpress = 1;
        }
        else {
            if (shiftpress == 1) {
        		if (currentchoice == 0) {
        			resumepressed();
        		}
        		else if (currentchoice == 1) {
        			savepressed(levelnum);
        		}
        		else if (currentchoice == 2) {
        			exitpressed();
        		}
        	}
            shiftpress = 0;
       }
	}
}
