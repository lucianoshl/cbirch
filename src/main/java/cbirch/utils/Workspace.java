package cbirch.utils;

import java.io.File;

/**
 * Created by lucianos on 3/8/17.
 */
public class Workspace {

    public static File resolve() {
        File currentFolder = new File(Workspace.class.getResource(".").getFile());
        do {
            currentFolder = currentFolder.getParentFile();
        } while (!currentFolder.getName().equals("target"));
        return currentFolder.getParentFile().getParentFile();
    }

    public static File getResource(String path) {
        return new File(Workspace.class.getClassLoader().getResource(path).getFile());
    }
}
