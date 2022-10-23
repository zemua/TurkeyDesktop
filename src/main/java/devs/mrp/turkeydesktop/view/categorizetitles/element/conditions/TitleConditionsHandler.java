/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizetitles.element.conditions;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import devs.mrp.turkeydesktop.common.RemovableLabel;
import devs.mrp.turkeydesktop.common.impl.ConfirmationWithDelayFactory;
import devs.mrp.turkeydesktop.database.titledlog.TitledLog;
import devs.mrp.turkeydesktop.database.titles.TitleServiceFactory;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import javax.swing.JTextArea;

/**
 *
 * @author miguel
 */
public class TitleConditionsHandler extends PanelHandler<TitleConditionsEnum, AWTEvent, FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent>> {

    private ConfirmationWithDelay popupMaker = new ConfirmationWithDelayFactory();
    
    private TitledLog mTitledLog;
    private TitleService titleService = TitleServiceFactory.getService();
    private static final Logger logger = Logger.getLogger(TitleConditionsHandler.class.getName());
    private static final int shortWaitingSeconds = 15;
    
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
                    exit();
                    break;
                case POSITIVE_BUTTON:
                    String textForCondition = ((JLabel)getPanel().getProperty(TitleConditionsEnum.NEW_CONDITION_TEXT)).getText();
                    popupMaker.show(this.getFrame(), () -> {
                        // positive
                        addCondition(textForCondition, Title.Type.POSITIVE);
                    }, () -> {
                        // negative do nothing
                        // intentionally empty
                    }, shortWaitingSeconds);
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
        JTextArea title = (JTextArea)getPanel().getProperty(TitleConditionsEnum.TITLE);
        title.setText(mTitledLog.getTitle());
    }
    
    private void fillConditions() {
        JPanel conditionsPanel = (JPanel)getPanel().getProperty(TitleConditionsEnum.CONDITIONS_PANEL);
        conditionsPanel.removeAll();
        String title = ((JTextComponent)getPanel().getProperty(TitleConditionsEnum.TITLE)).getText();
        titleService.findContainedByAndNegativeFirst(title, titles -> {
            titles.stream().forEach(t -> {
                        TitleCondition label = new TitleCondition(t);
                        conditionsPanel.add(label);
                        label.addFeedbackListener((tipo,feedback) -> {
                            if (RemovableLabel.Action.DELETE.equals(feedback)) {
                                if (Title.Type.NEGATIVE.equals(t.getType())) {
                                    popupMaker.show(this.getFrame(), () -> {
                                        // positive
                                        removeCondition(tipo.getSubStr());
                                    }, () -> {
                                        // negative
                                        // do nothing, intentionally left blank
                                    });
                                } else {
                                    removeCondition(tipo.getSubStr());
                                }
                            }
                        });
                    });
            conditionsPanel.revalidate();
            conditionsPanel.updateUI();
        });
    }
    
    private void addCondition(String substr, Title.Type type) {
        Title title = new Title();
        title.setSubStr(substr);
        title.setType(type);
        titleService.save(title, r -> {});
        fillConditions();
    }
    
    private void removeCondition(String substr) {
        titleService.deleteBySubString(substr, r -> {});
        fillConditions();
        
    }

    @Override
    protected void doBeforeExit() {
        // blank
    }
    
}
