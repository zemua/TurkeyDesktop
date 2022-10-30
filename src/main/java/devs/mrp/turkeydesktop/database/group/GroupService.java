/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

import java.util.List;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface GroupService {
    public Observable<Long> add(Group element);
    public Observable<Long> update(Group element);
    public Observable<List<Group>> findAll();
    public Observable<Group> findById(long id);
    public Observable<Long> deleteById(long id);
    
    public Observable<List<Group>> findAllPositive();
    public Observable<List<Group>> findAllNegative();
    
    public Observable<Integer> setPreventClose(long groupId, boolean preventClose);
    public Observable<Boolean> isPreventClose(long groupId);
}
