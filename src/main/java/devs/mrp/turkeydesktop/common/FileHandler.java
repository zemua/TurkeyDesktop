/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.common;

import devs.mrp.turkeydesktop.database.config.FConfigElementService;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
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
    private static IConfigElementService configService = FConfigElementService.getService();
    
    public static File createFileIfNotExists(File file, String extension) throws IOException {
        File target = file;
        String path = target.getPath();
        String mExtension = extension;
        if (!mExtension.startsWith(".")) {
            mExtension = ".".concat(mExtension);
        }
        if (!path.endsWith(mExtension)) {
            path = path.concat(mExtension);
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
        File target = createFileIfNotExists(file, "txt");
        exportToFile(target, text);
    }
    
    public static void exportAccumulated(long time) throws IOException {
        long now = System.currentTimeMillis();
        if (lastOperation + millisBetweenOperations > now) {
            return;
        }
        lastOperation = now;
        String exportPath = configService.configElement(ConfigurationEnum.EXPORT_PATH).getValue();
        if (!"".equals(exportPath) && Boolean.valueOf(configService.configElement(ConfigurationEnum.EXPORT_TOGGLE).getValue())) {
            exportToFile(createFileIfNotExists(new File(exportPath), "txt"), String.valueOf(time));
        }
    }
    
    private static void exportToFile(File file, String text) throws IOException {
        if (!file.exists() || !file.canWrite() || !file.isFile())  {
            throw new IOException("Cannot write to file");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(text);
        }
    }
    
}
