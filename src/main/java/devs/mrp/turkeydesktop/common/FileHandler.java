/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.common;

import devs.mrp.turkeydesktop.database.config.FConfigElementService;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author miguel
 */
public class FileHandler {
    
    private static final long millisBetweenOperations = 60*1000;
    private static long lastExport = 0;
    private static IConfigElementService configService = FConfigElementService.getService();
    private static Map<String,CachedValue> readerCache = new HashMap<>();
    private static Map<String,Long> lastWrittings = new HashMap<>();
    
    private static class CachedValue {
        String value;
        long lastUpdated;
    }
    
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
        writeToTxt(file, text, false);
    }
    
    private static void writeToTxt(File file, String text, boolean resetTimer) throws IOException {
        long now = System.currentTimeMillis();
        Long lastWrite = lastWrittings.containsKey(file.getPath()) ? lastWrittings.get(file.getPath()) : 0;
        if (lastWrite + millisBetweenOperations > now) {
            return;
        }
        if (resetTimer) {
            lastWrittings.put(file.getPath(), 0L);
        } else {
            lastWrittings.put(file.getPath(), now);
        }
        File target = createFileIfNotExists(file, "txt");
        exportToFile(target, text);
    }
    
    public static void clearTxt(File file) throws IOException {
        writeToTxt(file, StringUtils.EMPTY, true);
    }
    
    public static void exportAccumulated(long time) throws IOException {
        long now = System.currentTimeMillis();
        if (lastExport + millisBetweenOperations > now) {
            return;
        }
        lastExport = now;
        String exportPath = configService.configElement(ConfigurationEnum.EXPORT_PATH).getValue();
        if (!StringUtils.EMPTY.equals(exportPath) && Boolean.valueOf(configService.configElement(ConfigurationEnum.EXPORT_TOGGLE).getValue())) {
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
    
    private static String getCache(File file, long now) throws IOException {
        if (readerCache.containsKey(file.getPath()) && now < readerCache.get(file.getPath()).lastUpdated + millisBetweenOperations) {
            return readerCache.get(file.getPath()).value;
        }
        if (!file.exists() || !file.canRead() || !file.isFile())  {
            throw new IOException("Cannot read from file");
        }
        return null;
    }
    
    public static String readFirstLineFromFile(File file) throws IOException {
        long now = System.currentTimeMillis();
        String cach = getCache(file, now);
        if (Objects.nonNull(cach)){
            return cach;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                CachedValue cached = new CachedValue();
                cached.lastUpdated = now;
                cached.value = line;
                readerCache.put(file.getPath(), cached);
                return line;
            }
        }
        return StringUtils.EMPTY;
    }
    
    public static String readAllLinesFromFile(File file) throws IOException {
        long now = System.currentTimeMillis();
        String cach = getCache(file, now);
        if (Objects.nonNull(cach)){
            return cach;
        }
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (!first) {
                    builder.append(System.lineSeparator());
                }
                builder.append(line);
                first = false;
            }
            CachedValue cached = new CachedValue();
            cached.lastUpdated = now;
            cached.value = builder.toString();
            readerCache.put(file.getPath(), cached);
        }
        return builder.toString();
    }
    
}
