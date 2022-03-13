/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
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
public class ImportServiceImpl implements ImportService {

    private static final ImportsDao repo = ImportsRepository.getInstance();
    private static final Logger logger = Logger.getLogger(ImportServiceImpl.class.getName());

    @Override
    public long add(String path) {
        if (!exists(path)){
            return repo.add(path);
        }
        return 0;
    }

    @Override
    public List<String> findAll() {
        List<String> elements = new ArrayList<>();
        ResultSet set = repo.findAll();
        try {
            while (set.next()) {
                elements.add(set.getString(ConfigurationEnum.IMPORT_PATH.toString()));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return elements;
    }

    @Override
    public boolean exists(String path) {
        if (path == null || "".equals(path) || path.length() > 500) {
            return false;
        }
        ResultSet rs = repo.findById(path);
        try {
            return rs.next();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public long deleteById(String path) {
        if (path == null) {
            return -1;
        }
        return repo.deleteById(path);
    }

}
