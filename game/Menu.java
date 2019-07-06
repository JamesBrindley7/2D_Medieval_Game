package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
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
 * The main menu class for when the game starts
 * @author James Brindley
 *
 */
public class Menu {
	File save = new File("Save.txt");
	List<Integer> level = new ArrayList<Integer>();
	
	private RenderWindow window;
	KeyboardHandler kh;
	private Sprite background;
	private Font font = new Font();
    private Text Play;
    private Text Load;
    private Text Exit;
    private Text Options;
    private int change =1;

	private Sprite Logo;
	
	private List<Text> options = new ArrayList();
	private int currentchoice = 0;
	private int check = 0;
	private int wpress = 0;
	private int spress = 0;
	private int shiftpress = 0;
	/**
	 * The constructor for the main menu
	 * @param window The window to draw too
	 * @param kh The keyboard handlers
	 * @throws IOException
	 */
	Menu(RenderWindow window, KeyboardHandler kh) throws IOException{
		this.window = window;
		this.kh = kh;
		
		Path logopath = Paths.get("sprites/Game_Logo.png");
		Path backgroundP = Paths.get("sprites/Menu_Background.png");

		Texture backgroundTex = new Texture();
		backgroundTex.loadFromFile(backgroundP);

		background = new Sprite(backgroundTex);

		Texture Logobackground = new Texture();
		Logobackground.loadFromFile(logopath);
		Logo = new Sprite(Logobackground);
		Logo.setScale(1, 1);
		Logo.move(440, 50);

		font.loadFromFile(Paths.get("sprites/Kavivanar-Regular.ttf"));
		Play = new Text("Play", font, 64);
		Play.setPosition(580, 200);

		Load = new Text("Load Game", font, 64);
		Load.setPosition(490, 300);

		Options = new Text("Options", font, 64);
		Options.setPosition(530, 400);

		Exit = new Text("Exit", font, 64);
		Exit.setPosition(580, 500);
		options.add(Play);
		options.add(Load);
		options.add(Options);
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
			window.draw(background);
			window.draw(Logo);

	        window.draw(Play);
	        window.draw(Load);
	        window.draw(Options);
	        window.draw(Exit);
	        
	        
	        window.display();
	        updatemenu();
		}
	}
	/**
	 * Method for the play button, this loads the first level and then exits the loop
	 * @throws IOException
	 */
	void playpressed() throws IOException {
		change = 0;
		Level_1 level1 = new Level_1(window, kh);
		Level_2 level2 = new Level_2(window, kh);
		Level_3 level3 = new Level_3(window, kh);
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
			playpressed();
		} finally {
		    try {
		        if (text.equals("")) {
		            reader.close();
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
		    } catch (IOException e) {
		    }
		}
	}
	/**
	 * Method for option button
	 */
	void optionspressed() {
		
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
			options.get(3).setColor(Color.WHITE);
		}
		else if (currentchoice == 1) {
			options.get(0).setColor(Color.WHITE);
			options.get(1).setColor(Color.RED);
			options.get(2).setColor(Color.WHITE);
			options.get(3).setColor(Color.WHITE);
		}
		else if (currentchoice == 2) {
			options.get(0).setColor(Color.WHITE);
			options.get(1).setColor(Color.WHITE);
			options.get(2).setColor(Color.RED);
			options.get(3).setColor(Color.WHITE);
		}
		else if (currentchoice == 3) {
			options.get(0).setColor(Color.WHITE);
			options.get(1).setColor(Color.WHITE);
			options.get(2).setColor(Color.WHITE);
			options.get(3).setColor(Color.RED);
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
			currentchoice = 3;
		}
		else if (currentchoice > 3) {
			currentchoice = 0;
		}
		if (kh.enterPressed()) {
            shiftpress = 1;
        }
        else {
            if (shiftpress == 1) {
        			if (currentchoice == 0) {
        				playpressed();
        			}
        			else if (currentchoice == 1) {
        				loadpressed();
        			}
        			else if (currentchoice == 2) {
        				optionspressed();
        			}
        			else if (currentchoice == 3) {
        				exitpressed();
        			}
        		}
            shiftpress = 0;
            }
	}
}
