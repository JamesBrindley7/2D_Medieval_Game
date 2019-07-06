package game;

import org.jsfml.audio.Music;
import org.jsfml.graphics.RenderWindow;
import java.io.IOException;
import java.nio.file.Paths;


/**
 * Add sound stream for any sound
 * @author Yiyang Lai
 */
public class SoundStream {
    private RenderWindow window;
    private String soundname;
    private Music music;
    public SoundStream (RenderWindow window, String s)
    {
        this.window = window;
        soundname= s;

        try {
            music = new Music();
            music.openFromFile(Paths.get( "audio/"+s+".wav"));
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void looping()
    {
        music.setLoop(true);
    }

    public void play(){
        music.play();
    }

    public void pause(){
        music.pause();
    }

    public void stop() {
        music.stop();
    }
}
