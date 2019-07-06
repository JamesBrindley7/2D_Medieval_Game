package game;

import org.jsfml.graphics.RenderWindow;

/**
 * Add Music player for any sound effects
 * @author Yiyang Lai
 */

public class MusicPlayer extends SoundEffects{
    public  int numberOfSounds=0;
    public MusicPlayer (RenderWindow w)
    {
        super(w);
        this.addSoundIntoList();
    }

    @Override
    public int getIndex(String s) {
        int i;
        for (i=0;i<numberOfSounds;i++)
            if (this.soundlist[i]==s) break;
        return i;
    }

    @Override
    public void addSoundIntoList() {
        this.allocateSpace(numberOfSounds);
        //this.soundlist=new String[]{"movement","attacking","pickupitem","dead","boss"};
        this.loadSound(numberOfSounds);
    }
}
