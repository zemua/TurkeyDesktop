/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker.imports;

import devs.mrp.turkeydesktop.common.FileHandler;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author miguel
 */
public class ImportReaderImpl implements ImportReader {
    
    // data example: 2022-02-18-28800000
    private static String ENTRY_PATTERN = "[0-9](4)-[0-9](2)-"; // TODO
    // TODO review the other files that contain millis for pattern with minus
    
    @Override
    public List<ImportValue> getValuesFromFile(String path) {
        try {
            String contents = FileHandler.readAllLinesFromFile(new File(path));
            return Arrays.asList(contents.split(System.lineSeparator()))
                    .stream()
                    .map(line -> mapEntry(line))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            Logger.getLogger(ImportReaderImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.emptyList();
        }
    }
    
    private ImportValue mapEntry(String entry){
        // TODO
        return null;
    }
    
}
