/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.imports;

import java.util.List;

/**
 *
 * @author miguel
 */
public interface ImportService {
    public long add(String path);
    public List<String> findAll();
    public boolean exists(String path);
    public long deleteById(String path);
}
