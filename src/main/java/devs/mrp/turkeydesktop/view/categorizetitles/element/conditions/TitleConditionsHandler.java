/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizetitles.element.conditions;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.database.titledlog.TitledLog;
import devs.mrp.turkeydesktop.database.titles.FTitleService;
import devs.mrp.turkeydesktop.database.titles.ITitleService;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 *
 * @author miguel
 */
public class TitleConditionsHandler extends PanelHandler<TitleConditionsEnum, AWTEvent, FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent>> {

    private TitledLog mTitledLog;
    private ITitleService titleService = FTitleService.getService();
    private static final Logger logger = Logger.getLogger(TitleConditionsHandler.class.getName());
    
    public TitleConditionsHandler(JFrame frame, PanelHandler<?, ?, ?> caller, TitledLog titledLog) {
        super(frame, caller);
        mTitledLog = titledLog;
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent> initPanel() {
        return FTitleConditionsPanel.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case BACK:
                    this.getCaller().show();
                    break;
                case POSITIVE_BUTTON:
                    addCondition(((JLabel)getPanel().getProperty(TitleConditionsEnum.NEW_CONDITION_TEXT)).getText(), Title.Type.POSITIVE);
                    break;
                case NEGATIVE_BUTTON:
                    addCondition(((JLabel)getPanel().getProperty(TitleConditionsEnum.NEW_CONDITION_TEXT)).getText(), Title.Type.NEGATIVE);
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void doExtraBeforeShow() {
        fillFields();
        fillConditions();
    }
    
    private void fillFields() {
        JTextField title = (JTextField)getPanel().getProperty(TitleConditionsEnum.TITLE);
        title.setText(mTitledLog.getTitle());
    }
    
    private void fillConditions() {
        JPanel conditionsPanel = (JPanel)getPanel().getProperty(TitleConditionsEnum.CONDITIONS_PANEL);
        conditionsPanel.removeAll();
        String title = ((JTextComponent)getPanel().getProperty(TitleConditionsEnum.TITLE)).getText();
        List<Title> titles = titleService.findContainedByAndNegativeFirst(title);
        titles.stream().forEach(t -> {
                    TitleCondition label = new TitleCondition(t);
                    conditionsPanel.add(label);
                    label.addFeedbackListener((tipo,feedback) -> {
                        switch (feedback) {
                            case DELETE:
                                removeCondition(tipo.getSubStr());
                                break;
                            default:
                                break;
                        }
                    });
                });
        conditionsPanel.revalidate();
        conditionsPanel.updateUI();
    }
    
    private void addCondition(String substr, Title.Type type) {
        Title title = new Title();
        title.setSubStr(substr);
        title.setType(type);
        titleService.save(title);
        fillConditions();
    }
    
    private void removeCondition(String substr) {
        titleService.deleteBySubString(substr);
        fillConditions();
        
    }
    
}
