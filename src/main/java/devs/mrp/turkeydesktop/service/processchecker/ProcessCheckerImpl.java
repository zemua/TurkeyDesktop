/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.processchecker;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.Dupla;
import java.util.Optional;

/**
 *
 * @author miguel
 */
public class ProcessCheckerImpl implements ProcessChecker {
    
    ChainHandler<ProcessInfo> chainHandler;
    ProcessInfo processInfo;
    
    public ProcessCheckerImpl() {
        chainHandler = CheckerChainFactory.getChain();
        processInfo = ProcessInfoFactory.getNew();
    }
    
    @Override
    public void refresh() {
        chainHandler.receiveRequest("both", processInfo);
    }

    @Override
    public String currentProcessName() {
        return Optional.ofNullable(processInfo.getProcessName()).orElse("");
    }

    @Override
    public String currentWindowTitle() {
        return Optional.ofNullable(processInfo.getWindowTitle()).orElse("");
    }
    
    @Override
    public String currentProcessPid() {
        return Optional.ofNullable(processInfo.getProcessPid()).orElse("");
    }
}
