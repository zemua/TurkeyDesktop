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
public class FProcessChecker {
    
    public static IProcessChecker getNew() {
        return new ProcessChecker();
    }
    
}
