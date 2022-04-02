/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.external;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;

/**
 *
 * @author miguel
 */
public interface ExternalGroupDao extends GeneralDao<ExternalGroup, Long> {
    public ResultSet findByGroup(Long id);
    public ResultSet findByFile(String file);
    public ResultSet findByGroupAndFile(Long id, String file);
    public long deleteByGroup(Long id);
}
