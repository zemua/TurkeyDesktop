/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

/**
 *
 * @author ncm55070
 */
public enum SaveAction {
    EXISTING(0L), SAVED(1L), UPDATED(2L);
    private Long l;
    SaveAction(Long l) {
        this.l = l;
    }
    public Long get() {
        return l;
    }
}
