package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;

/**
 * The main menu class for when the game starts
 * @author James Brindley
 *
 */
public class DeadMenu {
	File save = new File("Save.txt");
	List<Integer> level = new ArrayList<Integer>();
	
	private RenderWindow window;
	KeyboardHandler kh;
	private Font font = new Font();
    private Text Main;
    private Text Load;
    private Text Exit;
    private Text Dead;
    private int change =1;
	
	private List<Text> options = new ArrayList();
	private int currentchoice = 0;
	private int wpress = 0;
	private int spress = 0;
	private int shiftpress = 0;
	/**
	 * The constructor for the main menu
	 * @param window The window to draw too
	 * @param kh The keyboard handlers
	 * @throws IOException
	 */
	DeadMenu(RenderWindow window, KeyboardHandler kh) throws IOException{
		this.window = window;
		this.kh = kh;
		
	        
	        font.loadFromFile(Paths.get("sprites/Kavivanar-Regular.ttf"));
	       
	        Dead = new Text("You're Dead", font, 100);
	        Dead.setPosition(400, 50);
	        
	        Main = new Text("Main Menu", font, 64);
	        Main.setPosition(500, 300);
	        
	        Load = new Text("Load Last Save", font, 64);
	        Load.setPosition(430, 400);
	        
	        Exit = new Text("Exit", font, 64);
	        Exit.setPosition(580, 500);
	        options.add(Main);
	        options.add(Load);
	        options.add(Exit);
	        displaymenu(kh);
	       
	}
	/**
	 * method to display the menu with all the buttons
	 * @param kh The keyboard handler
	 * @throws IOException
	 */
	void displaymenu(KeyboardHandler kh) throws IOException {
		change =1;
		while(change == 1){
			kh.checkLoop();
			window.clear();

			window.draw(Dead);
	        window.draw(Main);
	        window.draw(Load);
	        window.draw(Exit);
	        
	        
	        window.display();
	        updatemenu();
		}
	}
	/**
	 * Method for the play button, this loads the first level and then exits the loop
	 * @throws IOException
	 */
	void mainpressed() throws IOException {
		change = 0;
		Menu menu = new Menu(window, kh);
	}
	/**
	 * Method for the load button, this loads the save file and and reads the level number inside
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	void loadpressed() throws NumberFormatException, IOException {
		BufferedReader reader = null;
		String text = null;
		try {
			reader = new BufferedReader(new FileReader(save));

		    text = reader.readLine();
		    
		} catch (FileNotFoundException e) {
			mainpressed();
		} finally {
		    try {
		        if (text.equals("")) {
		            reader.close();
		            //mainpressed();
		        }
		        else {
		        	if (text.equals("1")) {
		        		Level_1 level1 = new Level_1(window, kh);
						Level_2 level2 = new Level_2(window, kh);
						Level_3 level3 = new Level_3(window, kh);
		        	}
		        	else if (text.equals("2")){
		        		Level_2 level2 = new Level_2(window, kh);
						Level_3 level3 = new Level_3(window, kh);
		        	}
		        	else if (text.equals("3")){
						Level_3 level3 = new Level_3(window, kh);
		        	}
		        }
		    } catch (Exception e) {
		    }
		}
	}
	/**
	 * Method for exit button, exits the game when pressed
	 */
	void exitpressed() {
		System.exit(0);
	}
	/**
	 * Method to update the menu and the buttons as well as the button clicks
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
        				mainpressed();
        			}
        			else if (currentchoice == 1) {
        				loadpressed();
        			}
        			else if (currentchoice == 2) {
        				exitpressed();
        			}
        		}
            shiftpress = 0;
            }
	}
}
