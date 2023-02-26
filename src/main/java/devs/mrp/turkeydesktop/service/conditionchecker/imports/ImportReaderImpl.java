package devs.mrp.turkeydesktop.service.conditionchecker.imports;

import devs.mrp.turkeydesktop.common.FileHandler;
import io.reactivex.rxjava3.core.Single;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImportReaderImpl implements ImportReader {
    
    // data example type YYYY-MM-DD-XXXXXXXX -> 2022-02-18-28800000
    private static final String ENTRY_PATTERN = "[\\d]{4}-[\\d]{2}-[\\d]{2}-\\d+$";
    private static final Pattern PATTERN = Pattern.compile(ENTRY_PATTERN);
    private static final long timeBetweenSyncs = 1*60*1000; // 1 minute
    
    private final Map<String,CachedValues> cachedValues;
    private final FileHandler fileHandler;
    
    public ImportReaderImpl(ImportReaderFactory factory) {
        this.cachedValues = new HashMap<>();
        this.fileHandler = factory.getFileHandler();
    }
            
    private class CachedValues {
        long lastUpdated = 0;
        List<ImportValue> values;
    }
    
    @Override
    public List<ImportValue> getValuesFromFile(String path) {
        return streamFromFile(path).collect(Collectors.toList());
    }
    
    private Stream<ImportValue> streamFromFile(String path) { // TODO get values from cache if times is not yet expired
        CachedValues cached = cachedValues.get(path);
        long now = System.currentTimeMillis();
        if (cached != null && cached.lastUpdated + timeBetweenSyncs > now) {
            return cached.values.stream();
        }
        // if the value is not cached or time has expired
        if (cached == null) {
            cached = new CachedValues();
            cached.values = Collections.EMPTY_LIST;
        }
        try {
            String contents = fileHandler.readAllLinesFromFile(new File(path));
            cached.values = Arrays.asList(contents.split(System.lineSeparator()))
                    .stream()
                    .map(line -> mapEntry(line))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            cached.lastUpdated = now;
            cachedValues.put(path, cached);
            return cached.values.stream();
        } catch (IOException ex) {
            Logger.getLogger(ImportReaderImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Stream.empty();
        }
    }
    
    @Override
    public List<ImportValue> getValuesFromFileBetweenDates(String path, LocalDate dateFrom, LocalDate dateTo) {
        return streamFromFileBetweenDates(path, dateFrom, dateTo)
                .collect(Collectors.toList());
    }
    
    private Stream<ImportValue> streamFromFileBetweenDates(String path, LocalDate dateFrom, LocalDate dateTo) {
        return streamFromFile(path)
                .filter(element -> element.getDate().isEqual(dateFrom) || element.getDate().isAfter(dateFrom))
                .filter(element -> element.getDate().isEqual(dateTo) || element.getDate().isBefore(dateTo));
    }
    
    @Override
    public Single<Long> getTotalSpentFromFileBetweenDates(String path, LocalDate dateFrom, LocalDate dateTo) {
        return Single.just(streamFromFileBetweenDates(path, dateFrom, dateTo)
                .collect(Collectors.summingLong(ImportValue::getSpent)));
    }
    
    private ImportValue mapEntry(String entry){
        boolean valid = PATTERN.matcher(entry).matches();
        if (!valid) {
            return null;
        }
        String[] values = entry.split("-");
        Integer year = Integer.parseInt(values[0]);
        Integer month = Integer.parseInt(values[1])+1; // TODO from Android it comes in a range of 0 to 11, but LocalDate has a range of 1 to 12
        Integer day = Integer.parseInt(values[2]);
        Long spent = Long.parseLong(values[3]);
        LocalDate date = LocalDate.of(year, month, day);
        ImportValue importValue = new ImportValue();
        importValue.setDate(date);
        importValue.setSpent(spent);
        return importValue;
    }
    
}
