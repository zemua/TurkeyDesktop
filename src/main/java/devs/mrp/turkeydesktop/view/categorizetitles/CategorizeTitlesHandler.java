/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizetitles;

import devs.mrp.turkeydesktop.common.Feedbacker;
import devs.mrp.turkeydesktop.database.titledlog.TitledLogFacadeFactoryImpl;
import devs.mrp.turkeydesktop.database.titledlog.TitledLog;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.categorizetitles.element.CategorizeTitlesElement;
import devs.mrp.turkeydesktop.view.categorizetitles.element.conditions.FTitleConditionsPanel;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import devs.mrp.turkeydesktop.database.titledlog.TitledLogServiceFacade;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author miguel
 */
@Slf4j
public class CategorizeTitlesHandler extends PanelHandler<CategorizeTitlesEnum, AWTEvent, FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent>> {

    TitledLogServiceFacade facadeService = TitledLogFacadeFactoryImpl.getService();
    Logger logger = Logger.getLogger(CategorizeTitlesHandler.class.getName());

    public CategorizeTitlesHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        super(frame, caller);
    }

    @Override
    protected FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent> initPanel() {
        return CategorizeTitlesPanelFactory.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case BACK:
                    exit();
                    break;
                case UPDATE:
                    updateItemsInList();
                    break;
                case TEXT_FILTER:
                    updateItemsInList();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void doExtraBeforeShow() {
        //attachItemsToListPanel(new Date(), new Date());
        updateItemsInList();
    }

    private void updateItemsInList() {
        Date from = (Date) this.getPanel().getProperty(CategorizeTitlesEnum.FROM);
        Date to = (Date) this.getPanel().getProperty(CategorizeTitlesEnum.TO);
        int filter = (Integer) this.getPanel().getProperty(CategorizeTitlesEnum.FILTER);
        attachItemsToListPanel(from, to, filter);
    }

    private void attachItemsToListPanel(Date from, Date to, int filter) {
        JPanel panel = (JPanel) this.getPanel().getProperty(CategorizeTitlesEnum.LIST_PANEL);
        if (panel == null) {
            return;
        }

        Observer<TitledLog> subscriber = new Observer<TitledLog>() {
            @Override
            public void onComplete() {
                SwingUtilities.invokeLater(() -> {
                    panel.updateUI();
                    panel.revalidate();
                });
            }

            @Override
            public void onError(Throwable thrwbl) {
                // nothing to do here
            }

            @Override
            public void onNext(TitledLog t) {
                log.debug("Adding TitledLog to Panel: {}", t.toString());
                CategorizeTitlesElement element = new CategorizeTitlesElement(t.getTitle(), t.getQtyPositives(), t.getQtyNegatives());
                element.setTitledLog(t);
                panel.add(element);
                setTagClickListener(element, t);
            }

            @Override
            public void onSubscribe(Disposable d) {
                // nothing here
            }
        };

        panel.removeAll(); // clear in case it has been filled before
        facadeService.getLogsDependablesWithTitleConditions(from, to)
                .filter(c -> getFilterText().isEmpty() ? true : StringUtils.containsIgnoreCase(c.getTitle(), getFilterText()))
                .toSortedList((c1, c2) -> Long.valueOf(c2.getElapsed()).compareTo(c1.getElapsed()))
                .flatMapObservable(Observable::fromIterable)
                .filter(t -> ifPassFilter(t, filter))
                .subscribe(subscriber);
    }
    
    private String getFilterText() {
        Object object = this.getPanel().getProperty(CategorizeTitlesEnum.TEXT_FILTER);
        if (!(object instanceof JTextField)) {
            logger.log(Level.SEVERE, "text filter field not found, fallbacking to empty filter");
            return StringUtils.EMPTY;
        }
        JTextField textField = (JTextField) object;
        return textField.getText();
    }

    private boolean ifPassFilter(TitledLog log, int filter) {
        if (filter == CategorizeTitlesFilter.FILTER_ALL.getFilter()) {
            return true;
        }
        if (filter == CategorizeTitlesFilter.FILTER_NOT_CATEGORIZED.getFilter() && log.getQtyNegatives() == 0 && log.getQtyPositives() == 0 && log.getQtyNeutral() == 0) {
            return true;
        }
        if (filter == CategorizeTitlesFilter.FILTER_NEGATIVE.getFilter() && log.getQtyNegatives() > 0) {
            return true;
        }
        if (filter == CategorizeTitlesFilter.FILTER_POSITIVE.getFilter() && log.getQtyPositives() > 0) {
            return true;
        }
        if (filter == CategorizeTitlesFilter.FILTER_NEUTRAL.getFilter() && log.getQtyNeutral() > 0) {
            return true;
        }
        return false;
    }

    private void setTagClickListener(Feedbacker<JLabel, String> label, TitledLog titledLog) {
        label.addFeedbackListener((tipo, feedback) -> {
            var handler = FTitleConditionsPanel.getHandler(getFrame(), CategorizeTitlesHandler.this, titledLog);
            handler.show();
        });
    }

    @Override
    protected void doBeforeExit() {
        // blank
    }

}
