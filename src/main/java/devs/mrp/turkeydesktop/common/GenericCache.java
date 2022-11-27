/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

import java.util.function.Supplier;

/**
 *
 * @author ncm55070
 */
public interface GenericCache<K,T> {
    public T put(K key, T data);
    public T get(K key, Supplier<T> compute);
    public T remove(K key);
}
