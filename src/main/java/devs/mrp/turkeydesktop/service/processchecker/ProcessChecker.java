/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.processchecker;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.Dupla;

/**
 *
 * @author miguel
 */
public class ProcessChecker implements IProcessChecker {
    
    ChainHandler<Dupla<String, String>> chainHandler;
    Dupla<String, String> dupla;
    
    public ProcessChecker() {
        chainHandler = FCheckerChain.getChain();
        dupla = new Dupla<>();
    }

    @Override
    public String currentProcessName() {
        chainHandler.receiveRequest("both", dupla);
        return dupla.getValue2();
    }

    @Override
    public String currentWindowTitle() {
        return dupla.getValue1();
    }
    
}
