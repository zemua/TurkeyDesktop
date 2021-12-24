/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizeprocesspanel;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.logandtype.FLogAndTypeService;
import devs.mrp.turkeydesktop.database.logandtype.ILogAndTypeService;
import devs.mrp.turkeydesktop.database.type.FTypeService;
import devs.mrp.turkeydesktop.database.type.ITypeService;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.list.CategorizerElement;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.list.CategorizerStaticData;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author miguel
 */
public class CatProcessHandler extends PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>> {

    private static final int FILTER_ALL = 0;
    private static final int FILTER_NOT_CATEGORIZED = 1;
    private static final int FILTER_POSITIVE = 2;
    private static final int FILTER_NEGATIVE = 3;
    private static final int FILTER_NEUTRAL = 4;
    private static final int FILTER_DEPENDS = 5;
    
    private Logger logger = Logger.getLogger(CatProcessHandler.class.getName());
    private FeedbackListener<Type.Types,String> mListener;
    private ITypeService typeService;
    
    ILogAndTypeService typedService = FLogAndTypeService.getService();
    
    public CatProcessHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        super(frame, caller);
        typeService = FTypeService.getService();
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
                case UPDATE:
                    updateItemsInList();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void doExtraBeforeShow() {
        
        //attachItemsToList(new Date(), new Date());
        attachItemsToListPanel(new Date(), new Date(), FILTER_ALL);
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
    
    private void attachItemsToListPanel(Date from, Date to, int filter) {
        JPanel panel = (JPanel)this.getPanel().getProperty(CatProcessEnum.LIST_PANEL);
        panel.removeAll(); // clear in case it has been filled before
        List<Tripla<String, Long, Type.Types>> triplas = typedService.getTypedLogGroupedByProcess(from, to);
        triplas.sort((c1,c2) -> c2.getValue2().compareTo(c1.getValue2()));
        triplas.forEach(t -> {
            if (ifPassFilter(t.getValue3(), filter)) {
                CategorizerElement element = new CategorizerElement(panel.getWidth(), panel.getHeight());
                element.init(t.getValue1(), t.getValue3());
                panel.add(element);
                setRadioListener(element);
            } else {
                panel.updateUI();
            }
        });
        panel.revalidate();
    }
    
    private void updateItemsInList() {
        Date from = (Date)this.getPanel().getProperty(CatProcessEnum.FROM);
        Date to = (Date)this.getPanel().getProperty(CatProcessEnum.TO);
        int filter = (Integer)this.getPanel().getProperty(CatProcessEnum.FILTER);
        attachItemsToListPanel(from, to, filter);
    }
    
    private boolean ifPassFilter(Type.Types t, int filter) {
        if (filter == FILTER_ALL) {return true;}
        if (t.equals(Type.Types.UNDEFINED) && filter == FILTER_NOT_CATEGORIZED) {return true;}
        if (t.equals(Type.Types.POSITIVE) && filter == FILTER_POSITIVE) {return true;}
        if (t.equals(Type.Types.NEGATIVE) && filter == FILTER_NEGATIVE) {return true;}
        if (t.equals(Type.Types.NEUTRAL) && filter == FILTER_NEUTRAL) {return true;}
        if (t.equals(Type.Types.DEPENDS) && filter == FILTER_DEPENDS) {return true;}
        return false;
    }
    
    private void setRadioListener(CategorizerElement el) {
        if (mListener == null) {
            mListener = new FeedbackListener<Type.Types, String>() {
                @Override
                public void giveFeedback(Type.Types tipo, String feedback) {
                    addCategorization(feedback, tipo);
                }
            };
        }
        el.addFeedbackListener(mListener);
    }
    
    private void addCategorization(String process, Type.Types type) {
        Type t = new Type();
        t.setProcess(process);
        t.setType(type);
        typeService.add(t);
    }
    
}
