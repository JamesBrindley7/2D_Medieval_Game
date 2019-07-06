package game;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.window.VideoMode;

import java.io.IOException;
import java.util.ArrayList;

import static game.SoundStream.*;
import static org.jsfml.window.WindowStyle.FULLSCREEN;

/**
 * This class is used to host the game window, from which the game rooms render
 * @author Will Lumby
 * @version 1.0
 */
class GameArea {

    private static final String VERSION = "1.0.2_01";
    private static final int RESOLUTIONX = 1280;
    private static final int RESOLUTIONY = 720;
    private static final boolean TESTMODE = true;

    private ArrayList<SoundEffects> soundEffects= new ArrayList<SoundEffects>();

    /**
     * This is the constructor for the gameplay area (the window)
     * @throws IOException IOException
     */
    GameArea() throws IOException {

        RenderWindow window = new RenderWindow();

        window.create(VideoMode.getDesktopMode(), "Regicide", FULLSCREEN);
        window.setVerticalSyncEnabled(true);
        window.setTitle("Regicide Release" + VERSION + " testMode: " + TESTMODE);
        KeyboardHandler kh = new KeyboardHandler(window);


        soundEffects.add(new MusicPlayer(window));


        View view = new View(new FloatRect(0, 0, RESOLUTIONX, RESOLUTIONY));
        window.setView(view);

        Menu menu = new Menu(window, kh);

        //Level_1 level1 = new Level_1(window, kh);
        //Level_1.playerOnLevel = true;


    }
}
