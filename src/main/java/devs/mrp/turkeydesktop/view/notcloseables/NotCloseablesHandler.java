package devs.mrp.turkeydesktop.view.notcloseables;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import devs.mrp.turkeydesktop.database.closeables.CloseableService;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.database.type.TypeService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.groups.review.switchable.Switchable;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotCloseablesHandler extends PanelHandler<NotCloseablesEnum, Object, FeedbackerPanelWithFetcher<NotCloseablesEnum, Object>, NotCloseablesPanelFactory> {
    
    private final NotCloseablesPanelFactory factory;
    private final ConfirmationWithDelay popupMaker;
    private final TypeService typeService;
    private final CloseableService closeableService;

    public NotCloseablesHandler(JFrame frame, PanelHandler<?, ?, ?, ?> caller, NotCloseablesPanelFactory factory) {
        super(frame, caller, factory);
        this.typeService = factory.getTypeService();
        this.closeableService = factory.getCloseableService();
        this.popupMaker = factory.getPopupMaker();
        this.factory = factory;
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<NotCloseablesEnum, Object> initPanel(NotCloseablesPanelFactory factory) {
        this.setPanel(factory.getPanel());
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
