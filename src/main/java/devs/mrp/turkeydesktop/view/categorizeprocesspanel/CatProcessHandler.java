/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizeprocesspanel;

import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.logandtype.FLogAndTypeService;
import devs.mrp.turkeydesktop.database.logandtype.ILogAndTypeService;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.list.CategorizerElement;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author miguel
 */
public class CatProcessHandler extends PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>> {

    ILogAndTypeService typedService = FLogAndTypeService.getService();
    
    public CatProcessHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        super(frame, caller);
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent> initPanel() {
        return FCatProcessPanel.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case BACK:
                    this.getCaller().show();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void doExtraBeforeShow() {
        
        //attachItemsToList(new Date(), new Date());
        attachItemsToListPanel(new Date(), new Date());
    }
    
    /*private void attachItemsToList(Date from, Date to) {
        DefaultListModel<CategorizerElement> listModel = (DefaultListModel)this.getPanel().getProperty(CatProcessEnum.LIST_MODEL);
        listModel.clear(); // clear in case it has been filled before
        List<Tripla<String, Long, Type.Types>> triplas = typedService.getTypedLogGroupedByProcess(from, to);
        triplas.sort((c1,c2) -> c2.getValue2().compareTo(c1.getValue2()));
        triplas.forEach(t -> {
            CategorizerElement element = new CategorizerElement();
            element.init(t.getValue1(), t.getValue3());
            listModel.add(0, element);
        });
    }*/
    
    private void attachItemsToListPanel(Date from, Date to) {
        // TODO
        JPanel panel = (JPanel)this.getPanel().getProperty(CatProcessEnum.LIST_PANEL);
        panel.removeAll(); // clear in case it has been filled before
        List<Tripla<String, Long, Type.Types>> triplas = typedService.getTypedLogGroupedByProcess(from, to);
        triplas.sort((c1,c2) -> c2.getValue2().compareTo(c1.getValue2()));
        triplas.forEach(t -> {
            CategorizerElement element = new CategorizerElement(panel.getWidth(), panel.getHeight());
            element.init(t.getValue1(), t.getValue3());
            panel.add(element);
        });
    }
    
}
