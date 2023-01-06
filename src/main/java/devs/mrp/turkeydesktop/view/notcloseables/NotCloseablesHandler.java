/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.notcloseables;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import devs.mrp.turkeydesktop.common.impl.ConfirmationWithDelayFactory;
import devs.mrp.turkeydesktop.database.closeables.CloseableService;
import devs.mrp.turkeydesktop.database.closeables.CloseableFactory;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.database.type.TypeService;
import devs.mrp.turkeydesktop.database.type.TypeServiceFactory;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.groups.review.switchable.Switchable;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author miguel
 */
@Slf4j
public class NotCloseablesHandler extends PanelHandler<NotCloseablesEnum, Object, FeedbackerPanelWithFetcher<NotCloseablesEnum, Object>> {
    
    private ConfirmationWithDelay popupMaker = new ConfirmationWithDelayFactory();
    
    private static final Logger logger = Logger.getLogger(NotCloseablesHandler.class.getName());
    private final LocaleMessages localeMessages = LocaleMessages.getInstance();
    
    private final TypeService typeService = TypeServiceFactory.getService();
    private final CloseableService closeableService = CloseableFactory.getService();

    public NotCloseablesHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        super(frame, caller);
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<NotCloseablesEnum, Object> initPanel() {
        this.setPanel(NotCloseablesPanelFactory.getPanel());
        return this.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<NotCloseablesEnum, Object> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case BACK:
                    exit();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void doExtraBeforeShow() {
        log.info("do extra before show");
        try {
            refreshProcesses();
        } catch (Exception e) {
            
        }
        log.info("finishing do extra before show");
    }

    @Override
    protected void doBeforeExit() {
        // blank
    }
    
    private void refreshProcesses() {
        Object object = this.getPanel().getProperty(NotCloseablesEnum.PANEL);
        if (object == null || !(object instanceof JPanel)) {
            return;
        }
        JPanel panel = (JPanel) object;
        panel.removeAll();
        
        Observer<Type> subscriber = new Observer<Type>() {
            @Override
            public void onComplete() {
                log.debug("refreshProcesses completed");
            }

            @Override
            public void onError(Throwable thrwbl) {
                log.debug("Error on refreshProcesses subscriber", (Object[])thrwbl.getStackTrace());
            }

            @Override
            public void onNext(Type process) {
                log.debug("thread: {}", Thread.currentThread().getName());
                log.debug("processing {}", process.getProcess());
                closeableService.canBeClosed(process.getProcess()).subscribe(canClose -> {
                    Switchable switchable = new Switchable(process.getProcess(), !canClose, true);
                    switchable.addFeedbackListener((processId, feedback) -> {
                        if (!feedback) { // if the checkbox was unchecked with this event
                            closeableService.deleteById(processId).subscribe();
                        } else { // if the checkbox was cheked with this event
                            popupMaker.show(NotCloseablesHandler.this.getFrame(), () -> {
                                // positive
                                closeableService.add(processId).subscribe();
                            }, () -> {
                                // negative
                                switchable.setSelected(false);
                            });
                        }
                    });
                    SwingUtilities.invokeLater(() -> {
                                log.debug("adding to panel {}", switchable);
                                panel.add(switchable);
                                switchable.revalidate();
                                switchable.updateUI();
                            });
                });
            }
            
            @Override
            public void onSubscribe(Disposable d) {
                log.debug("Subscribing to refreshProcesses");
            }
        };
        log.debug("subscribing");
        typeService.findByType(Type.Types.DEPENDS).subscribe(subscriber);
        log.debug("subscribed");
    }
}
