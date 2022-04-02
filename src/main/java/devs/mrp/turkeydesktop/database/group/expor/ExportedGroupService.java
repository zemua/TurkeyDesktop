/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.expor;

import java.util.List;

/**
 *
 * @author miguel
 */
public interface ExportedGroupService {
    public long add(ExportedGroup element);
    public long update(ExportedGroup element);
    public List<ExportedGroup> findAll();
    public ExportedGroup findById(long id);
    public long deleteById(long id);
    public List<ExportedGroup> findByGroup(long id);
    public List<ExportedGroup> findByFileAndGroup(long groupId, String file);
    public long deleteByGroup(long id);
}
