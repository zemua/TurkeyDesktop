/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizeprocesspanel;

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
public class CatProcessPanel extends FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent> {

    private final List<FeedbackListener<CatProcessEnum, AWTEvent>> listeners = new ArrayList<>();

    // Netbeans UI builder enforces JList<String> and that prevents us from adding any other data except String to the model
    // unless we leave the model not parameterized ¯\_(ツ)_/¯
    //DefaultListModel listModel = new DefaultListModel<CategorizerElement>();
    /**
     * Creates new form CatProcessPanel
     */
    public CatProcessPanel() {
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
        jScrollPane1 = new javax.swing.JScrollPane();
        listScrollPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        fromLabel = new javax.swing.JLabel();
        toLabel = new javax.swing.JLabel();
        dateFrom = new com.toedter.calendar.JDateChooser();
        dateTo = new com.toedter.calendar.JDateChooser();
        selectShowType = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        filterText = new javax.swing.JTextField();
        filterClear = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages"); // NOI18N
        backButton.setText(bundle.getString("back")); // NOI18N
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        listScrollPanel.setLayout(new javax.swing.BoxLayout(listScrollPanel, javax.swing.BoxLayout.PAGE_AXIS));
        jScrollPane1.setViewportView(listScrollPanel);

        fromLabel.setText(bundle.getString("from")); // NOI18N

        toLabel.setText(bundle.getString("to")); // NOI18N

        dateFrom.setDate(new Date());
        dateFrom.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateFromPropertyChange(evt);
            }
        });

        dateTo.setDate(new Date());
        dateTo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateToPropertyChange(evt);
            }
        });

        selectShowType.setModel(getComboBoxModel());
        selectShowType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectShowTypeActionPerformed(evt);
            }
        });
        selectShowType.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                selectShowTypePropertyChange(evt);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fromLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectShowType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterText, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterClear)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(selectShowType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(filterText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(filterClear))
                    .addComponent(dateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toLabel)
                    .addComponent(dateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backButton)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        giveFeedback(CatProcessEnum.BACK, null);
    }//GEN-LAST:event_backButtonActionPerformed

    private void dateFromPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateFromPropertyChange
        setDateChooserErrorColor();
        if ("date".equals(evt.getPropertyName())) {
            sendUpdate();
        }
    }//GEN-LAST:event_dateFromPropertyChange

    private void dateToPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateToPropertyChange
        setDateChooserErrorColor();
        if ("date".equals(evt.getPropertyName())) {
            sendUpdate();
        }
    }//GEN-LAST:event_dateToPropertyChange

    private void selectShowTypePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_selectShowTypePropertyChange

    }//GEN-LAST:event_selectShowTypePropertyChange

    private void selectShowTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectShowTypeActionPerformed
            sendUpdate();
    }//GEN-LAST:event_selectShowTypeActionPerformed

    private void filterClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterClearActionPerformed
        filterText.setText(StringUtils.EMPTY);
        giveFeedback(CatProcessEnum.TEXT_FILTER, evt);
    }//GEN-LAST:event_filterClearActionPerformed

    private void filterTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filterTextKeyTyped
        giveFeedback(CatProcessEnum.TEXT_FILTER, evt);
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
            giveFeedback(CatProcessEnum.UPDATE, null);
        }
    }

    @Override
    public Object getProperty(CatProcessEnum property) {
        switch (property) {
            /*case LIST_MODEL:
                return listModel;*/
            case LIST_PANEL:
                return listScrollPanel;
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
    public void addFeedbackListener(FeedbackListener<CatProcessEnum, AWTEvent> listener) {
        listeners.add(listener);
    }

    @Override
    public void giveFeedback(CatProcessEnum tipo, AWTEvent feedback) {
        listeners.forEach(l -> l.giveFeedback(tipo, feedback));
    }

    private ComboBoxModel getComboBoxModel() {
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
        LocaleMessages m = LocaleMessages.getInstance();

        comboModel.addElement(m.getString("all"));
        comboModel.addElement(m.getString("notCategorized"));
        comboModel.addElement(m.getString("positive"));
        comboModel.addElement(m.getString("negative"));
        comboModel.addElement(m.getString("neutral"));
        comboModel.addElement(m.getString("depends"));
        
        comboModel.setSelectedItem(m.getString("notCategorized"));

        return comboModel;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private com.toedter.calendar.JDateChooser dateFrom;
    private com.toedter.calendar.JDateChooser dateTo;
    private javax.swing.JButton filterClear;
    private javax.swing.JTextField filterText;
    private javax.swing.JLabel fromLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel listScrollPanel;
    private javax.swing.JComboBox<String> selectShowType;
    private javax.swing.JLabel toLabel;
    // End of variables declaration//GEN-END:variables
}
