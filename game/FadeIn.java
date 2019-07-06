package game;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

import java.io.IOException;

class FadeIn {

    private RectangleShape rect = new RectangleShape(new Vector2f(1280, 720));
    private RenderWindow window;

    private int o = 0;
    private String type;
    private String color = "BLACK";

    FadeIn(RenderWindow window, String type) throws IOException {
        rect.setFillColor(new Color(0, 0, 0, 255));
        this.window = window;
        this.type = type;
    }

    FadeIn(RenderWindow window, String type, String s) {
        if (s.equals("BLUE")) {
            rect.setFillColor(new Color(0, 0, 255, 255));
            this.window = window;
            this.type = type;
            color = "BLUE";
        }
    }

    void draw() {
        window.draw(rect);
        if (color.equals("BLACK")) {
            rect.setFillColor(new Color(0, 0, 0, 255 - o));
        }
        else if (color.equals("BLUE")) {
            rect.setFillColor(new Color(0, 0, 255, 255 - o));
        }

        if (type.equals("in")) {
            o += 4;
        }
        else {
            o -= 4;
        }
    }
}
