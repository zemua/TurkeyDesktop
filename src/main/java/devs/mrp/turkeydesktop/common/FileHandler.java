/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 * @author miguel
 */
public class FileHandler {
    
    private static final long millisBetweenOperations = 60*1000;
    private static long lastOperation = 0;
    
    public static File createFileIfNotExists(File file, String extension) throws IOException {
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
    
    public static void writeToTxt(File file, String text) throws IOException {
        long now = System.currentTimeMillis();
        if (lastOperation + millisBetweenOperations > now) {
            return;
        }
        lastOperation = now;
        File target = createFileIfNotExists(file, ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(text);
        }
    }
    
}
