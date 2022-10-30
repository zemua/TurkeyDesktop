/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.external;

import java.util.List;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface ExternalGroupService {
    public Observable<Long> add(ExternalGroup element);
    public Observable<Long> update(ExternalGroup element);
    public Observable<List<ExternalGroup>> findAll();
    public Observable<ExternalGroup> findById(long id);
    public Observable<Long> deleteById(long id);
    public Observable<List<ExternalGroup>> findByGroup(Long id);
    public Observable<List<ExternalGroup>> findByFile(String file);
    public Observable<Long> deleteByGroup(Long id);
}
