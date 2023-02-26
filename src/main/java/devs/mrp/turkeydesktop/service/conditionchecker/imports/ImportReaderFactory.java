package devs.mrp.turkeydesktop.service.conditionchecker.imports;

import devs.mrp.turkeydesktop.common.FileHandler;

public interface ImportReaderFactory {
    ImportReader getReader();
    FileHandler getFileHandler();
}
