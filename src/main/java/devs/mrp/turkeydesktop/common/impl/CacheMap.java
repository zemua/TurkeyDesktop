/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ncm55070
 */
@Slf4j
public class CacheMap<K,V> extends LinkedHashMap<K,V> {
    
    private int maxSize = 100;
    
    public CacheMap() {
        maxSize = 100;
    }
    
    public CacheMap(int maxSize) {
        this.maxSize = maxSize;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> entry) {
        boolean isRemove = this.size() > maxSize;
        if (isRemove) {
            log.debug("Removing oldest entry {} of map, call stack trace: {}", entry.toString(), Thread.currentThread().getStackTrace());
        }
        return isRemove;
    }
    
}
