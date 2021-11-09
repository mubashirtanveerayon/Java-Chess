package util;

import java.net.URL;

public class ResourceLoader {

    public static URL load(String path) {
        URL input = ResourceLoader.class.getResource(path);

        if (input == null) {
            input = ResourceLoader.class.getResource("/" + path);
            System.out.println(input);
        }
        return input;
    }

}
