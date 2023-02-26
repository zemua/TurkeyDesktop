package devs.mrp.turkeydesktop.view.categorizetitles.element.conditions;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import devs.mrp.turkeydesktop.common.RemovableLabel;
import devs.mrp.turkeydesktop.common.impl.ConfirmationWithDelayFactory;
import devs.mrp.turkeydesktop.database.titledlog.TitledLog;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import java.awt.AWTEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

public class TitleConditionsHandler extends PanelHandler<TitleConditionsEnum, AWTEvent, FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent>, TitleConditionsPanelFactory> {

    private static final int shortWaitingSeconds = 15;
    
    private ConfirmationWithDelay popupMaker = new ConfirmationWithDelayFactory();
    
    private final TitleConditionsPanelFactory factory;
    private final TitledLog mTitledLog;
    private final TitleService titleService;
    
    public TitleConditionsHandler(JFrame frame, PanelHandler<?, ?, ?, ?> caller, TitledLog titledLog, TitleConditionsPanelFactory factory) {
        super(frame, caller, factory);
        mTitledLog = titledLog;
        titleService = factory.getTitleService();
        this.factory = factory;
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent> initPanel(TitleConditionsPanelFactory factory) {
        return factory.getPanel();
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
                case NEUTRAL_BUTTON:
                    String conditionText = ((JLabel)getPanel().getProperty(TitleConditionsEnum.NEW_CONDITION_TEXT)).getText();
                    popupMaker.show(this.getFrame(), () -> {
                        // positive
                        addCondition(conditionText, Title.Type.NEUTRAL);
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
        
        Observer<Title> subscriber = new Observer<Title>() {
            @Override
            public void onComplete() {
                conditionsPanel.revalidate();
                conditionsPanel.updateUI();
            }

            @Override
            public void onError(Throwable thrwbl) {
                // nothing to do here
            }

            @Override
            public void onNext(Title t) {
                TitleCondition label = new TitleCondition(t);
                conditionsPanel.add(label);
                label.addFeedbackListener((tipo,feedback) -> {
                    if (RemovableLabel.Action.DELETE.equals(feedback)) {
                        if (Title.Type.NEGATIVE.equals(t.getType())) {
                            popupMaker.show(TitleConditionsHandler.this.getFrame(), () -> {
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
            }

            @Override
            public void onSubscribe(Disposable d) {
                // nothing here
            }
        };
        
        titleService.findContainedByAndNegativeFirst(title).subscribe(subscriber);
    }
    
    private void addCondition(String substr, Title.Type type) {
        Title title = new Title();
        title.setSubStr(substr);
        title.setType(type);
        titleService.save(title).subscribe();
        fillConditions();
    }
    
    private void removeCondition(String substr) {
        titleService.deleteBySubString(substr).subscribe();
        fillConditions();
        
    }

    @Override
    protected void doBeforeExit() {
        // blank
    }
    
}
