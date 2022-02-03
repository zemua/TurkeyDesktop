/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.common;

/**
 *
 * @author miguel
 */
public class Tripla<T1, T2, T3> {
    
    private T1 value1;
    private T2 value2;
    private T3 value3;

    public T1 getValue1() {
        return value1;
    }

    public void setValue1(T1 value1) {
        this.value1 = value1;
    }

    public T2 getValue2() {
        return value2;
    }

    public void setValue2(T2 value2) {
        this.value2 = value2;
    }

    public T3 getValue3() {
        return value3;
    }

    public void setValue3(T3 value3) {
        this.value3 = value3;
    }
    
}
