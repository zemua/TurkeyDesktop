/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.closeables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    public long add(String element) {
        if (element == null) {
            return -1;
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            ResultSet rs = repo.findById(element);
            try{
                if (rs.next()){
                return 0;
            }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return repo.add(new Closeable(element));
        }
    }

    @Override
    public List<Closeable> findAll() {
        return listFromResultSet(repo.findAll());
    }

    @Override
    public Closeable findById(String id) {
        ResultSet set = repo.findById(id);
        Closeable closeable = new Closeable();
        try {
            if (set.next()) {
                closeable.setProcess(set.getString(Closeable.PROCESS_NAME));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return closeable;
    }

    @Override
    public boolean canBeClosed(String process) {
        ResultSet set = repo.findById(process);
        try {
            if (set.next()) {
                return false;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public long deleteById(String id) {
        return repo.deleteById(id);
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
