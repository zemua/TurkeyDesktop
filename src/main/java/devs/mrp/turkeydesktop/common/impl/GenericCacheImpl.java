/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common.impl;

import devs.mrp.turkeydesktop.common.GenericCache;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 *
 * @author ncm55070
 */
public class GenericCacheImpl<K,T> implements GenericCache<K,T> {
    
    private Map<K,TimeStampedData> cache = new HashMap<>();
    
    private long millisToExpire;
    
    public GenericCacheImpl() {
        millisToExpire = 60*1000; // 1 minute
    }
    
    public GenericCacheImpl(int minutes) {
        millisToExpire = minutes * 60 * 1000;
    }

    @Override
    public T put(K key, T data) {
        return cache.put(key, new TimeStampedData(data)).data;
    }

    @Override
    public T get(K key, Supplier<T> compute) {
        TimeStampedData tsd = cache.get(key);
        if (tsd == null || tsd.isExpired()) {
            return cache.put(key, new TimeStampedData(compute.get())).data;
        }
        return tsd.data;
    }
    
    private class TimeStampedData {
        LocalDateTime timeStamp;
        T data;
        TimeStampedData(T t) {
            timeStamp = LocalDateTime.now();
            data = t;
        }
        boolean isExpired() {
            return timeStamp.plus(millisToExpire, ChronoUnit.MILLIS).isBefore(LocalDateTime.now());
        }
    }
    
}
