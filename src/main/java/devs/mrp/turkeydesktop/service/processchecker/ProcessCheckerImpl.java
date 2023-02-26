package devs.mrp.turkeydesktop.service.processchecker;

import devs.mrp.turkeydesktop.common.ChainHandler;
import java.util.Optional;

public class ProcessCheckerImpl implements ProcessChecker {
    
    ChainHandler<ProcessInfo> chainHandler;
    ProcessInfo processInfo;
    
    public ProcessCheckerImpl(ProcessCheckerFactory factory) {
        chainHandler = CheckerChainFactory.getChain();
        processInfo = factory.getNewProcessInfo();
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
