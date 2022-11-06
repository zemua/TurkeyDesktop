/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.imports;

import rx.Observable;
import rx.Single;

/**
 *
 * @author miguel
 */
public interface ImportService {
    public Single<Long> add(String path);
    public Observable<String> findAll();
    public Single<Boolean> exists(String path);
    public Single<Long> deleteById(String path);
}
