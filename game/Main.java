package game;

import java.io.IOException;

public class Main {

    //Main method
    public static void main(String args[]) {
        //Launch a new instance of the game area, catching any thrown IO Errors
        try {
            new GameArea();
        }
        catch (IOException e) {System.out.println(e);}
    }
}