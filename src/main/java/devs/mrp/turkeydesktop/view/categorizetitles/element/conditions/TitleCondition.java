/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizetitles.element.conditions;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.common.Feedbacker;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

/**
 *
 * @author miguel
 */
public class TitleCondition extends JPanel implements Feedbacker<Title, TitleCondition.Action> {
    
    private LocaleMessages locale = LocaleMessages.getInstance();
    private List<FeedbackListener<Title, TitleCondition.Action>> listeners = new ArrayList<>();
    
    private Title title;
    private JLabel label;
    private JButton button;
    
    public enum Action {
        DELETE;
    }
    
    public TitleCondition(Title t) {
        title = t;
        initializeLabel();
        initializeButton();
        initializePanel();
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    @Override
    public void addFeedbackListener(FeedbackListener<Title, TitleCondition.Action> listener) {
        listeners.add(listener);
    }

    @Override
    public void giveFeedback(Title tipo, TitleCondition.Action feedback) {
        listeners.forEach(l -> l.giveFeedback(tipo, feedback));
    }
    
    private void initializePanel() {
        this.setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(label);
        this.add(button);
    }
    
    private void initializeLabel() {
        label = new JLabel();
        label.setText(title.toString());
        // clicking on the label will show the delete button
        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                showButton();
            }
        });
    }
    
    private void initializeButton() {
        button = new JButton();
        button.setText(locale.getString("remove"));
        button.setEnabled(false);
        button.setVisible(false);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                giveFeedback(title, Action.DELETE);
                hideButton();
            }
        });
    }
    
    public void showButton() {
        button.setEnabled(true);
        button.setVisible(true);
        // hide back the button in 3 seconds
        SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                Thread.sleep(3000);
                return null;
            }
            @Override
            protected void done() {
                hideButton();
            }
        };
        sw.execute();
    }
    
    public void hideButton() {
        if (button != null) {
            button.setEnabled(false);
            button.setVisible(false);
        }
    }
    
}
