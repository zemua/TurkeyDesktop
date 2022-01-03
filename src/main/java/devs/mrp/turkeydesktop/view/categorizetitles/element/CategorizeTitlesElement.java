/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizetitles.element;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.common.Feedbacker;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

/**
 *
 * @author miguel
 */
public class CategorizeTitlesElement extends JLabel implements Feedbacker<JLabel, String> {
    
    private LocaleMessages locale = LocaleMessages.getInstance();
    private List<FeedbackListener<JLabel, String>> listeners = new ArrayList<>();
    
    private String title;
    private int positiveMatches;
    private int negativeMatches;
    
    public CategorizeTitlesElement(String title, int positiveMatches, int negativeMatches) {
        this.title = title;
        this.positiveMatches = positiveMatches;
        this.negativeMatches = negativeMatches;
        initializeLabel();
    }
    
    @Override
    public void addFeedbackListener(FeedbackListener<JLabel, String> listener) {
        listeners.add(listener);
    }

    @Override
    public void giveFeedback(JLabel tipo, String feedback) {
        listeners.forEach(l -> l.giveFeedback(tipo, feedback));
    }
    
    private void initializeLabel() {
        this.setText(String.format("%s [+%d] [-%d]",
                title, positiveMatches, negativeMatches));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                giveFeedback(CategorizeTitlesElement.this, title);
            }
        });
    }
    
}
