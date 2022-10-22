/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.common.SingleConsumerFactory;
import devs.mrp.turkeydesktop.common.WorkerFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class ImportServiceImpl implements ImportService {

    private static final ImportsDao repo = ImportsRepository.getInstance();
    private static final Logger logger = Logger.getLogger(ImportServiceImpl.class.getName());

    @Override
    public void add(String path, LongConsumer c) {
        LongConsumer consumer = SingleConsumerFactory.getLongConsumer(c);
        exists(path, existsResult -> {
            if (!existsResult){
                WorkerFactory.runLongWorker(() -> repo.add(path), consumer);
            } else {
                consumer.accept(0);
            }
        });
    }

    @Override
    public void findAll(Consumer<List<String>> c) {
        Consumer<List<String>> consumer = SingleConsumerFactory.getStringListConsumer(c);
        WorkerFactory.runResultSetWorker(() -> repo.findAll(), set -> {
            List<String> elements = new ArrayList<>();
            try {
                while (set.next()) {
                    elements.add(set.getString(ConfigurationEnum.IMPORT_PATH.toString()));
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            consumer.accept(elements);
        });
    }

    @Override
    public void exists(String path, Consumer<Boolean> c) {
        Consumer<Boolean> consumer = SingleConsumerFactory.getBooleanConsumer(c);
        if (path == null || "".equals(path) || path.length() > 500) {
            consumer.accept(false);
            return;
        }
        WorkerFactory.runResultSetWorker(() -> repo.findById(path), rs -> {
            try {
                consumer.accept(rs.next());
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
                consumer.accept(false);
            }
        });
    }

    @Override
    public void deleteById(String path, LongConsumer c) {
        LongConsumer consumer = SingleConsumerFactory.getLongConsumer(c);
        if (path == null) {
            consumer.accept(-1);
            return;
        }
        WorkerFactory.runLongWorker(() -> repo.deleteById(path), consumer);
    }

}
