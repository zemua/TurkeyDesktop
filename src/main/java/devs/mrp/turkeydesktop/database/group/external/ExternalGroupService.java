/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.external;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public interface ExternalGroupService {
    public Single<Long> add(ExternalGroup element);
    public Single<Long> update(ExternalGroup element);
    public Observable<ExternalGroup> findAll();
    public Single<ExternalGroup> findById(long id);
    public Single<Long> deleteById(long id);
    public Observable<ExternalGroup> findByGroup(Long id);
    public Observable<ExternalGroup> findByFile(String file);
    public Single<Long> deleteByGroup(Long id);
}
