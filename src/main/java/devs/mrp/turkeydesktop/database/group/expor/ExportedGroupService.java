/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.expor;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public interface ExportedGroupService {
    public Single<Long> add(ExportedGroup element);
    public Single<Long> update(ExportedGroup element);
    public Observable<ExportedGroup> findAll();
    public Maybe<ExportedGroup> findById(ExportedGroupId id);
    public Single<Long> deleteById(ExportedGroupId id);
    public Observable<ExportedGroup> findByGroup(long id);
    public Observable<ExportedGroup> findByFileAndGroup(long groupId, String file);
    public Single<Long> deleteByGroup(long id);
}
