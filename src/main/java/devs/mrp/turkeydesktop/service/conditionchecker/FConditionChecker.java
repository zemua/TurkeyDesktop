/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker;

/**
 *
 * @author miguel
 */
public class FConditionChecker {
    public static IConditionChecker getConditionChecker() {
        return new ConditionChecker();
    }
}
