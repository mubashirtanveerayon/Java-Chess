package util;

import java.net.URL;

public class ResourceLoader {

    public static URL load(String path) {
        URL input = ResourceLoader.class.getClassLoader().getResource(path);

        if (input == null) {
            input = ResourceLoader.class.getResource("/" + path);
        }
        return input;
    }

}
