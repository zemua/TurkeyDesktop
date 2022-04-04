/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.processchecker;

/**
 *
 * @author miguel
 */
public class ProcessCheckerFactory {
    
    public static ProcessChecker getNew() {
        return new ProcessCheckerImpl();
    }
    
}
