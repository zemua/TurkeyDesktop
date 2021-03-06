/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizetitles;

import devs.mrp.turkeydesktop.common.Feedbacker;
import devs.mrp.turkeydesktop.database.titledlog.TitledLogServiceFacadeFactory;
import devs.mrp.turkeydesktop.database.titledlog.TitledLog;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.categorizetitles.element.CategorizeTitlesElement;
import devs.mrp.turkeydesktop.view.categorizetitles.element.conditions.FTitleConditionsPanel;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import devs.mrp.turkeydesktop.database.titledlog.TitledLogServiceFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author miguel
 */
public class CategorizeTitlesHandler extends PanelHandler<CategorizeTitlesEnum, AWTEvent, FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent>> {

    TitledLogServiceFacade facadeService = TitledLogServiceFacadeFactory.getService();
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
        panel.removeAll(); // clear in case it has been filled before
        List<TitledLog> titledLogs = facadeService.getLogsDependablesWithTitleConditions(from, to);
        titledLogs.sort((c1, c2) -> Long.valueOf(c2.getElapsed()).compareTo(c1.getElapsed()));
        titledLogs.stream()
                .filter(c -> getFilterText().isEmpty() ? true : StringUtils.containsIgnoreCase(c.getTitle(), getFilterText()))
                .forEach(t -> {
            if (ifPassFilter(t, filter)) {
                CategorizeTitlesElement element = new CategorizeTitlesElement(t.getTitle(), t.getQtyPositives(), t.getQtyNegatives());
                element.setTitledLog(t);
                panel.add(element);
                setTagClickListener(element, t);
            }
        });
        panel.updateUI();
        panel.revalidate();
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
        if (filter == CategorizeTitlesFilter.FILTER_NOT_CATEGORIZED.getFilter() && log.getQtyNegatives() == 0 && log.getQtyPositives() == 0) {
            return true;
        }
        if (filter == CategorizeTitlesFilter.FILTER_NEGATIVE.getFilter() && log.getQtyNegatives() > 0) {
            return true;
        }
        if (filter == CategorizeTitlesFilter.FILTER_POSITIVE.getFilter() && log.getQtyPositives() > 0) {
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
