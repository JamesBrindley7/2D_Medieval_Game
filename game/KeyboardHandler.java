package game;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Window;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

class KeyboardHandler {

    //Set the variables for keys pressed to false
    private static boolean wPressed = false;
    private static boolean sPressed = false;
    private static boolean aPressed = false;
    private static boolean dPressed = false;
    private static boolean shiftPressed = false;
    private static boolean ePressed = false;
    private static boolean escapePressed = false;
    private static boolean enterPressed = false;
    private static boolean onePressed = false;

    //Declare the window
    private Window window;

    //Constructor
    KeyboardHandler(Window window) {
        this.window = window;
    }

    //Method for a check loop
    void checkLoop() {
        //Poll window events
        for (Event event : window.pollEvents()) {
            switch (event.type) {

                //If the event is a key press
                case KEY_PRESSED:

                    //Work out what key was pressed
                    KeyEvent keyEvent = event.asKeyEvent();

                    //Set the corresponding variable to true for the pressed key
                    if (keyEvent.key == Keyboard.Key.W) {
                        wPressed = true;
                    }
                    if (keyEvent.key == Keyboard.Key.S) {
                        sPressed = true;
                    }
                    if (keyEvent.key == Keyboard.Key.A) {
                        aPressed = true;
                    }
                    if (keyEvent.key == Keyboard.Key.D) {
                        dPressed = true;
                    }
                    if (keyEvent.key == Keyboard.Key.LSHIFT) {
                        shiftPressed = true;
                    }
                    if (keyEvent.key == Keyboard.Key.ESCAPE) {
                        escapePressed = true;
                    }
                    if (keyEvent.key == Keyboard.Key.RETURN) {
                        enterPressed = true;
                    }
                    if (keyEvent.key == Keyboard.Key.E) {
                        ePressed = true;
                    }
                    if (keyEvent.key == Keyboard.Key.NUM1) {
                        onePressed = true;
                    }

                    break;

                //If the event was a key release
                case KEY_RELEASED:

                    //Work out what key was pressed
                    KeyEvent keyEventR = event.asKeyEvent();

                    //Set the corresponding variable to false for the released key
                    if (keyEventR.key == Keyboard.Key.W){
                        wPressed = false;
                    }
                    if (keyEventR.key == Keyboard.Key.S) {
                        sPressed = false;
                    }
                    if (keyEventR.key == Keyboard.Key.A) {
                        aPressed = false;
                    }
                    if (keyEventR.key == Keyboard.Key.D) {
                        dPressed = false;
                    }
                    if (keyEventR.key == Keyboard.Key.LSHIFT) {
                        shiftPressed = false;
                    }
                    if (keyEventR.key == Keyboard.Key.ESCAPE) {
                        escapePressed = false;
                    }
                    if (keyEventR.key == Keyboard.Key.RETURN) {
                    	enterPressed = false;
                    }
                    if (keyEventR.key == Keyboard.Key.E) {
                        ePressed = false;
                    }
                    if (keyEventR.key == Keyboard.Key.NUM1) {
                        onePressed = false;
                    }

                    break;
            }
        }
    }

    boolean wPressed() {
        return wPressed;
    }

    boolean sPressed() {
        return sPressed;
    }

    boolean aPressed() {
        return aPressed;
    }

    boolean dPressed() {
        return dPressed;
    }

    boolean ePressed() {
        return ePressed;
    }

    boolean shiftPressed() {
        return shiftPressed;
    }

    boolean escapePressed() {
        return escapePressed;
    }

    boolean enterPressed() {
        return enterPressed;
    }

    boolean onePressed() {
        return onePressed;
    }
}
