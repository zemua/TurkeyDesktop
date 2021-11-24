/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import java.sql.ResultSet;

/**
 *
 * @author miguel
 */
public class TimeLogService {
    
    TimeLogDao repo = new TimeLogRepository();
    
    public long add(TimeLog element) {
        if(element == null) {
            return -1;
        } else {
            return repo.add(element);
        }
    }
    
    public long update(TimeLog element) {
        if (element == null || element.getId() <= 0) {
            return -1;
        } else {
            return repo.update(element);
        }
    }
    
    public ResultSet findLast24H() {
        return repo.findAll();
    }
    
    public ResultSet findById(long id) {
        return repo.findById(id);
    }
    
    public long deleteById(long id) {
        return repo.deleteById(id);
    }
    
}
