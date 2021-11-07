package util;

import main.Main;

import java.net.URL;

public class ResourceLoader {

    public static URL load(String path) {
        URL input = Main.class.getResource(path);
        if (input == null) {
            input = Main.class.getResource("/" + path);
        }
        return input;
    }

}
