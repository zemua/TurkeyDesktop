/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.closeables;

import devs.mrp.turkeydesktop.common.TurkeyAppFactory;
import java.sql.ResultSet;
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
public class CloseableServiceImpl implements CloseableService {
    
    CloseableDao repo = CloseableRepository.getInstance();
    private static final Logger logger = Logger.getLogger(CloseableServiceImpl.class.getName());
    
    @Override
    public void add(String element, LongConsumer consumer) {
        if (element == null) {
            consumer.accept(-1);
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            ResultSet rs = repo.findById(element);
            try{
                if (rs.next()){
                    consumer.accept(0);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            TurkeyAppFactory.runLongWorker(() -> repo.add(new Closeable(element)), consumer);
        }
    }

    @Override
    public void findAll(Consumer<List<Closeable>> consumer) {
        CloseableServiceFactory.runCloseableListWorker(() -> listFromResultSet(repo.findAll()), consumer);
    }

    @Override
    public void findById(String id, Consumer<Closeable> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findById(id), set -> {
            Closeable closeable = new Closeable();
            try {
                if (set.next()) {
                    closeable.setProcess(set.getString(Closeable.PROCESS_NAME));
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            consumer.accept(closeable);
        });
    }

    @Override
    public void canBeClosed(String process, Consumer<Boolean> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findById(process), set -> {
            try {
                if (set.next()) {
                    consumer.accept(false);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            consumer.accept(true);
        });
    }

    @Override
    public void deleteById(String id, LongConsumer consumer) {
        TurkeyAppFactory.runLongWorker(() -> repo.deleteById(id), consumer);
    }
    
    private List<Closeable> listFromResultSet(ResultSet set) {
        List<Closeable> closeables = new ArrayList<>();
        try {
            while(set.next()) {
                closeables.add(new Closeable(set.getString(Closeable.PROCESS_NAME)));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return closeables;
    }
    
}
