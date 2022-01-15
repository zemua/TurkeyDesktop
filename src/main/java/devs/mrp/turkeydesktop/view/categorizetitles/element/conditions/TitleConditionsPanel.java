/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizetitles.element.conditions;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.h2.util.StringUtils;

/**
 *
 * @author miguel
 */
public class TitleConditionsPanel extends FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent> {
    
    private final List<FeedbackListener<TitleConditionsEnum, AWTEvent>> listeners = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(TitleConditionsPanel.class.getName());

    /**
     * Creates new form TitleConditionsPanel
     */
    public TitleConditionsPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backButton = new javax.swing.JButton();
        titleText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        negativeButton = new javax.swing.JButton();
        positiveButton = new javax.swing.JButton();
        newConditionText = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        conditionsPanel = new javax.swing.JPanel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages"); // NOI18N
        backButton.setText(bundle.getString("back")); // NOI18N
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        titleText.setEditable(false);
        titleText.setText("jTextField1");
        titleText.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                titleTextMouseDragged(evt);
            }
        });
        titleText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                titleTextMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                titleTextMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                titleTextMouseReleased(evt);
            }
        });
        titleText.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                titleTextPropertyChange(evt);
            }
        });

        jLabel1.setText(bundle.getString("selecttexttoaddcondition")); // NOI18N

        negativeButton.setText(bundle.getString("negative")); // NOI18N
        negativeButton.setEnabled(false);
        negativeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                negativeButtonActionPerformed(evt);
            }
        });

        positiveButton.setText(bundle.getString("positive")); // NOI18N
        positiveButton.setEnabled(false);
        positiveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positiveButtonActionPerformed(evt);
            }
        });

        newConditionText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        newConditionText.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                newConditionTextPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout conditionsPanelLayout = new javax.swing.GroupLayout(conditionsPanel);
        conditionsPanel.setLayout(conditionsPanelLayout);
        conditionsPanelLayout.setHorizontalGroup(
            conditionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 783, Short.MAX_VALUE)
        );
        conditionsPanelLayout.setVerticalGroup(
            conditionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 129, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(conditionsPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(titleText)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(newConditionText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(positiveButton)
                        .addGap(18, 18, 18)
                        .addComponent(negativeButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backButton)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addComponent(titleText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(negativeButton)
                    .addComponent(positiveButton)
                    .addComponent(newConditionText))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        giveFeedback(TitleConditionsEnum.BACK, evt);
    }//GEN-LAST:event_backButtonActionPerformed

    private void titleTextMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleTextMouseDragged
        setConditionText();
    }//GEN-LAST:event_titleTextMouseDragged

    private void newConditionTextPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_newConditionTextPropertyChange
        boolean empty = newConditionText.getText().isEmpty();
        negativeButton.setEnabled(!empty);
        positiveButton.setEnabled(!empty);
    }//GEN-LAST:event_newConditionTextPropertyChange

    private void titleTextPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_titleTextPropertyChange
        
    }//GEN-LAST:event_titleTextPropertyChange

    private void titleTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleTextMouseClicked
        setConditionText();
    }//GEN-LAST:event_titleTextMouseClicked

    private void titleTextMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleTextMousePressed
        setConditionText();
    }//GEN-LAST:event_titleTextMousePressed

    private void titleTextMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleTextMouseReleased
        setConditionText();
    }//GEN-LAST:event_titleTextMouseReleased

    private void positiveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positiveButtonActionPerformed
        giveFeedback(TitleConditionsEnum.POSITIVE_BUTTON, evt);
        // the following will be executed AFTER the listener has handled the feedback and read the label text
        // but this is not thread safe
        newConditionText.setText("");
    }//GEN-LAST:event_positiveButtonActionPerformed

    private void negativeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_negativeButtonActionPerformed
        giveFeedback(TitleConditionsEnum.NEGATIVE_BUTTON, evt);
        // the following will be executed AFTER the listener has handled the feedback and read the label text
        // but this is not thread safe
        newConditionText.setText("");
    }//GEN-LAST:event_negativeButtonActionPerformed

    private void setConditionText() {
        String text = titleText.getSelectedText();
        if (text == null) {
            newConditionText.setText("");
        } else {
            newConditionText.setText(text.trim());
        }
    }
    
    @Override
    public Object getProperty(TitleConditionsEnum property) {
        switch (property) {
            case TITLE:
                return titleText;
            case CONDITIONS_PANEL:
                return conditionsPanel;
            case NEGATIVE_BUTTON:
                return negativeButton;
            case POSITIVE_BUTTON:
                return positiveButton;
            case NEW_CONDITION_TEXT:
                return newConditionText;
            default:
                return null;
        }
    }

    @Override
    public void addFeedbackListener(FeedbackListener<TitleConditionsEnum, AWTEvent> listener) {
        listeners.add(listener);
    }

    @Override
    public void giveFeedback(TitleConditionsEnum tipo, AWTEvent feedback) {
        listeners.forEach(e -> e.giveFeedback(tipo, feedback));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JPanel conditionsPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton negativeButton;
    private javax.swing.JLabel newConditionText;
    private javax.swing.JButton positiveButton;
    private javax.swing.JTextField titleText;
    // End of variables declaration//GEN-END:variables
}
