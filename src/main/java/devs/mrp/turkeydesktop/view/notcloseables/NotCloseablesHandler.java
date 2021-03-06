/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.notcloseables;

import devs.mrp.turkeydesktop.database.closeables.CloseableService;
import devs.mrp.turkeydesktop.database.closeables.CloseableServiceFactory;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.database.type.TypeService;
import devs.mrp.turkeydesktop.database.type.TypeServiceFactory;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.groups.review.switchable.Switchable;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author miguel
 */
public class NotCloseablesHandler extends PanelHandler<NotCloseablesEnum, Object, FeedbackerPanelWithFetcher<NotCloseablesEnum, Object>> {
    
    private static final Logger logger = Logger.getLogger(NotCloseablesHandler.class.getName());
    private final LocaleMessages localeMessages = LocaleMessages.getInstance();
    
    private final TypeService typeService = TypeServiceFactory.getService();
    private final CloseableService closeableService = CloseableServiceFactory.getService();

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
        try {
            refreshProcesses();
        } catch (Exception e) {
            
        }
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
        
        List<Type> dependables = typeService.findByType(Type.Types.DEPENDS);
        dependables.forEach(process -> {
            boolean canClose = closeableService.canBeClosed(process.getProcess());
            Switchable switchable = new Switchable(process.getProcess(), !canClose, true);
            switchable.addFeedbackListener((processId, feedback) -> {
                if (!feedback) { // if the checkbox was unchecked with this event
                    closeableService.deleteById(processId);
                } else { // if the checkbox was cheked with this event
                    closeableService.add(processId);
                }
            });
            panel.add(switchable);
        });
        
        panel.revalidate();
        panel.updateUI();
    }
    
}
