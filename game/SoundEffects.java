package game;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.audio.SoundSource;
import org.jsfml.graphics.RenderWindow;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Add sound effect class
 * @author Yiyang LaI
 */
public class SoundEffects {

    public String[] soundlist;
    public SoundBuffer[] soundBuffer;
    public Sound[] sound;
    public RenderWindow window;

    public SoundEffects(RenderWindow window)
    {
        this.window=window;
    }

    public void allocateSpace(int n)
    {
        soundBuffer=new SoundBuffer[n];
        sound= new Sound[n];
        soundlist= new String[n];
    }

    public void addSoundIntoList()
    {

    }

    public void loadSound(int n)
    {
        for (int i=0;i<n;i++)
        {
            soundBuffer[i]=new SoundBuffer();
            sound[i]= new Sound();
            try
            {
                soundBuffer[i].loadFromFile(Paths.get("audio/"+soundlist[i]+".wav"));
            }catch (IOException e){
                e.printStackTrace();
            }
            sound[i].setBuffer(soundBuffer[i]);
        }
    }
    public int getIndex(String s)
    {
        return 0;
    }

    public void playSound(String s) {
        if (sound[getIndex(s)].getStatus() != SoundSource.Status.PLAYING)
            sound[getIndex(s)].play();
    }
    public void setLoops(String s)
    {
    sound[getIndex(s)].setLoop(true);
    }
}