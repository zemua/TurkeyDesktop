/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.notcloseables;

import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class NotCloseablesHandler extends PanelHandler<NotCloseablesEnum, Object, FeedbackerPanelWithFetcher<NotCloseablesEnum, Object>> {

    public NotCloseablesHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        super(frame, caller);
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<NotCloseablesEnum, Object> initPanel() {
        // TODO
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
        // TODO
    }

    @Override
    protected void doBeforeExit() {
        // blank
    }
    
}
