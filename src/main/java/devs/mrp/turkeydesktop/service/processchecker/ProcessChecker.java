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
    
    ChainHandler<IProcessInfo> chainHandler;
    IProcessInfo processInfo;
    
    public ProcessChecker() {
        chainHandler = FCheckerChain.getChain();
        processInfo = FProcessInfo.getNew();
    }

    @Override
    public String currentProcessName() {
        chainHandler.receiveRequest("both", processInfo);
        return processInfo.getProcessName();
    }

    @Override
    public String currentWindowTitle() {
        return processInfo.getWindowTitle();
    }
    
    @Override
    public String currentProcessPid() {
        return processInfo.getProcessPid();
    }
}
