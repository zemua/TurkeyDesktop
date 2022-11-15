/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author ncm55070
 */
public interface DbCache<KEY, VALUE> {
    public Single<SaveAction> save(VALUE value);
    public Maybe<VALUE> read(KEY key);
    public Single<Boolean> remove(KEY key);
    public Observable<VALUE> getAll();
    public boolean contains(KEY key);
    
    public enum SaveAction {
        EXISTING(0L), SAVED(1L), UPDATED(2L);
        private Long l;
        SaveAction(Long l) {
            this.l = l;
        }
        Long get() {
            return l;
        }
    }
}
