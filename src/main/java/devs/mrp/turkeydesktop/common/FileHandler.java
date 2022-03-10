/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 * @author miguel
 */
public class FileHandler {
    
    public static File createFile(File file, String extension) throws IOException {
        File target = file;
        String path = target.getPath();
        if (!path.endsWith(".txt")) {
            path = path.concat(".txt");
            target = new File(path);
        }
        if (!Files.exists(target.toPath())) {
            Files.createFile(target.toPath());
        }
        return target;
    }
    
}
