/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizetitles;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author miguel
 */
public class CategorizeTitlesPanel extends FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent> {

    private final List<FeedbackListener<CategorizeTitlesEnum, AWTEvent>> listeners = new ArrayList<>();
    
    private int fromInitiated = 0;
    private int toInitiated = 0;
    
    /**
     * Creates new form CategorizeProcessPanel
     */
    public CategorizeTitlesPanel() {
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

        jButton1 = new javax.swing.JButton();
        fromLabel = new javax.swing.JLabel();
        dateFrom = new com.toedter.calendar.JDateChooser();
        toLabel = new javax.swing.JLabel();
        dateTo = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        listPanel = new javax.swing.JPanel();
        selectShowType = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        filterText = new javax.swing.JTextField();
        filterClear = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages"); // NOI18N
        jButton1.setText(bundle.getString("back")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        fromLabel.setText(bundle.getString("from")); // NOI18N

        dateFrom.setDate(new Date());
        dateFrom.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateFromPropertyChange(evt);
            }
        });

        toLabel.setText(bundle.getString("to")); // NOI18N

        dateTo.setDate(new Date());
        dateTo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateToPropertyChange(evt);
            }
        });

        listPanel.setLayout(new javax.swing.BoxLayout(listPanel, javax.swing.BoxLayout.PAGE_AXIS));
        jScrollPane1.setViewportView(listPanel);

        selectShowType.setModel(getComboBoxModel());
        selectShowType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectShowTypeActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle.getString("filter")); // NOI18N

        filterText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                filterTextKeyTyped(evt);
            }
        });

        filterClear.setText(bundle.getString("clear")); // NOI18N
        filterClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(fromLabel)
                        .addGap(18, 18, 18)
                        .addComponent(dateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(toLabel)
                        .addGap(18, 18, 18)
                        .addComponent(dateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(selectShowType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterText, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterClear)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toLabel)
                    .addComponent(dateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromLabel)
                    .addComponent(jButton1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(selectShowType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(filterText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(filterClear)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        giveFeedback(CategorizeTitlesEnum.BACK, evt);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void dateFromPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateFromPropertyChange
        setDateChooserErrorColor();
        fromInitiated ++;
        if (initiated()) {
            sendUpdate();
        }
    }//GEN-LAST:event_dateFromPropertyChange

    private void dateToPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateToPropertyChange
        setDateChooserErrorColor();
        toInitiated ++;
        if (initiated()) {
            sendUpdate();
        }
    }//GEN-LAST:event_dateToPropertyChange

    private void selectShowTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectShowTypeActionPerformed
        if (initiated()){
            sendUpdate();
        }
    }//GEN-LAST:event_selectShowTypeActionPerformed

    private void filterClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterClearActionPerformed
        filterText.setText(StringUtils.EMPTY);
        giveFeedback(CategorizeTitlesEnum.TEXT_FILTER, evt);
    }//GEN-LAST:event_filterClearActionPerformed

    private void filterTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filterTextKeyTyped
        giveFeedback(CategorizeTitlesEnum.TEXT_FILTER, evt);
    }//GEN-LAST:event_filterTextKeyTyped

    private void setDateChooserErrorColor() {
        if (isFromCorrect()) {
            fromLabel.setForeground(Color.BLACK);
        } else {
            fromLabel.setForeground(Color.RED);
        }

        if (isToCorrect()) {
            toLabel.setForeground(Color.BLACK);
        } else {
            toLabel.setForeground(Color.RED);
        }
    }

    private boolean isFromCorrect() {
        if (dateFrom.getDate() != null && dateTo.getDate() != null) {
            return dateFrom.getDate().compareTo(dateTo.getDate()) <= 0;
        }
        return dateFrom.getDate() != null;
    }

    private boolean isToCorrect() {
        if (dateFrom.getDate() != null && dateTo.getDate() != null) {
            return dateFrom.getDate().compareTo(dateTo.getDate()) <= 0;
        }
        return dateTo.getDate() != null;
    }

    private boolean isFromAndToCorrect() {
        return isFromCorrect() && isToCorrect();
    }

    private void sendUpdate() {
        if (isFromAndToCorrect()) {
            giveFeedback(CategorizeTitlesEnum.UPDATE, null);
        }
    }
    
    private boolean initiated() {
        // When initiating the fields on load, datePropertyChange is called 2 times instead of 1
        return fromInitiated > 1 && toInitiated > 1;
    }
    
    @Override
    public Object getProperty(CategorizeTitlesEnum property) {
        switch (property) {
            case LIST_PANEL:
                return listPanel;
            case FROM:
                return dateFrom.getDate();
            case TO:
                return dateTo.getDate();
            case FILTER:
                return selectShowType.getSelectedIndex();
            case TEXT_FILTER:
                return filterText;
            default:
                return null;
        }
    }

    @Override
    public void addFeedbackListener(FeedbackListener<CategorizeTitlesEnum, AWTEvent> listener) {
        listeners.add(listener);
    }

    @Override
    public void giveFeedback(CategorizeTitlesEnum tipo, AWTEvent feedback) {
        listeners.forEach(l -> l.giveFeedback(tipo, feedback));
    }
    
    private ComboBoxModel getComboBoxModel() {
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
        LocaleMessages m = LocaleMessages.getInstance();

        comboModel.addElement(m.getString("all"));
        comboModel.addElement(m.getString("notCategorized"));
        comboModel.addElement(m.getString("positive"));
        comboModel.addElement(m.getString("negative"));
        
        comboModel.setSelectedItem(m.getString("notCategorized"));

        return comboModel;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dateFrom;
    private com.toedter.calendar.JDateChooser dateTo;
    private javax.swing.JButton filterClear;
    private javax.swing.JTextField filterText;
    private javax.swing.JLabel fromLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel listPanel;
    private javax.swing.JComboBox<String> selectShowType;
    private javax.swing.JLabel toLabel;
    // End of variables declaration//GEN-END:variables
}
