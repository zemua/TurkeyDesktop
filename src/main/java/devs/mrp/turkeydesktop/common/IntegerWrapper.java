/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

/**
 *
 * @author ncm55070
 */
public class IntegerWrapper {
    
    private int i;
    
    public IntegerWrapper() {
        this.i = 0;
    }
    
    public IntegerWrapper(int j) {
        this.i = j;
    }

    public int get() {
        return i;
    }

    public void set(int i) {
        this.i = i;
    }
    
    public void increase() {
        this.i ++;
    }
}
