/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.expor;

import java.util.List;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface ExportedGroupService {
    public Observable<Long> add(ExportedGroup element);
    public Observable<Long> update(ExportedGroup element);
    public Observable<List<ExportedGroup>> findAll();
    public Observable<ExportedGroup> findById(long id);
    public Observable<Long> deleteById(long id);
    public Observable<List<ExportedGroup>> findByGroup(long id);
    public Observable<List<ExportedGroup>> findByFileAndGroup(long groupId, String file);
    public Observable<Long> deleteByGroup(long id);
}
