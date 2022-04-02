/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.external;

import java.util.List;

/**
 *
 * @author miguel
 */
public interface ExternalGroupService {
    public long add(ExternalGroup element);
    public long update(ExternalGroup element);
    public List<ExternalGroup> findAll();
    public ExternalGroup findById(long id);
    public long deleteById(long id);
    public List<ExternalGroup> findByGroup(Long id);
    public List<ExternalGroup> findByFile(String file);
    public long deleteByGroup(Long id);
}
