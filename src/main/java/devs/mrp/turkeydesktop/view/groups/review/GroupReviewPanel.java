/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.groups.review;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author miguel
 */
public class GroupReviewPanel extends FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent> {
    
    private List<FeedbackListener<GroupReviewEnum, AWTEvent>> listeners = new ArrayList<>();

    /**
     * Creates new form GroupReviewPanel
     */
    public GroupReviewPanel() {
        initComponents();
        enableAddConditionButton();
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
        groupLabel = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        processPanel = new javax.swing.JPanel();
        titlePanel = new javax.swing.JPanel();
        conditionsPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        targetNameComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        addConditionButton = new javax.swing.JButton();
        hourSpinner = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        minuteSpinner = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        daySpinner = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        conditionsPanelList = new javax.swing.JPanel();
        configurationPanel = new javax.swing.JPanel();
        groupNameText = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        deleteText = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages"); // NOI18N
        jButton1.setText(bundle.getString("back")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        groupLabel.setText("jLabel1");

        processPanel.setLayout(new javax.swing.BoxLayout(processPanel, javax.swing.BoxLayout.PAGE_AXIS));
        jTabbedPane1.addTab(bundle.getString("process"), processPanel); // NOI18N

        titlePanel.setLayout(new javax.swing.BoxLayout(titlePanel, javax.swing.BoxLayout.PAGE_AXIS));
        jTabbedPane1.addTab(bundle.getString("title"), titlePanel); // NOI18N

        jLabel2.setText(bundle.getString("if")); // NOI18N

        targetNameComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                targetNameComboBoxItemStateChanged(evt);
            }
        });
        targetNameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                targetNameComboBoxActionPerformed(evt);
            }
        });
        targetNameComboBox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                targetNameComboBoxPropertyChange(evt);
            }
        });

        jLabel3.setText(bundle.getString("hasUsed")); // NOI18N

        jLabel4.setText(bundle.getString("inTheLast")); // NOI18N

        jLabel5.setText(bundle.getString("days")); // NOI18N

        addConditionButton.setText(bundle.getString("add")); // NOI18N
        addConditionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addConditionButtonActionPerformed(evt);
            }
        });

        hourSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(99L), Long.valueOf(1L)));
        hourSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hourSpinnerStateChanged(evt);
            }
        });
        hourSpinner.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                hourSpinnerPropertyChange(evt);
            }
        });

        jLabel6.setText(bundle.getString("hours")); // NOI18N

        minuteSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(15L), Long.valueOf(0L), Long.valueOf(59L), Long.valueOf(1L)));
        minuteSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                minuteSpinnerStateChanged(evt);
            }
        });
        minuteSpinner.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                minuteSpinnerPropertyChange(evt);
            }
        });

        jLabel7.setText(bundle.getString("minutes")); // NOI18N

        daySpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 30, 1));
        daySpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                daySpinnerStateChanged(evt);
            }
        });
        daySpinner.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                daySpinnerPropertyChange(evt);
            }
        });

        conditionsPanelList.setLayout(new javax.swing.BoxLayout(conditionsPanelList, javax.swing.BoxLayout.PAGE_AXIS));
        jScrollPane1.setViewportView(conditionsPanelList);

        javax.swing.GroupLayout conditionsPanelLayout = new javax.swing.GroupLayout(conditionsPanel);
        conditionsPanel.setLayout(conditionsPanelLayout);
        conditionsPanelLayout.setHorizontalGroup(
            conditionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(conditionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(conditionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(conditionsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(targetNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hourSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(minuteSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(daySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addConditionButton)
                        .addGap(0, 381, Short.MAX_VALUE)))
                .addContainerGap())
        );
        conditionsPanelLayout.setVerticalGroup(
            conditionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(conditionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(conditionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(targetNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(hourSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(minuteSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel4)
                    .addComponent(daySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(addConditionButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("conditions"), conditionsPanel); // NOI18N

        jButton2.setText(bundle.getString("saveGroupName")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle.getString("writeDeleteClickConfirm")); // NOI18N

        jToggleButton1.setText(bundle.getString("confirm")); // NOI18N
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout configurationPanelLayout = new javax.swing.GroupLayout(configurationPanel);
        configurationPanel.setLayout(configurationPanelLayout);
        configurationPanelLayout.setHorizontalGroup(
            configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configurationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(configurationPanelLayout.createSequentialGroup()
                        .addComponent(groupNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addComponent(jLabel1)
                    .addGroup(configurationPanelLayout.createSequentialGroup()
                        .addComponent(deleteText, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton1)))
                .addContainerGap(598, Short.MAX_VALUE))
        );
        configurationPanelLayout.setVerticalGroup(
            configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configurationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(groupNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton1))
                .addContainerGap(80, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(bundle.getString("configuration"), configurationPanel); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(groupLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(groupLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        jTabbedPane1.getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        giveFeedback(GroupReviewEnum.BACK, evt);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void addConditionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addConditionButtonActionPerformed
        giveFeedback(GroupReviewEnum.ADD_CONDITION_BUTTON, evt);
    }//GEN-LAST:event_addConditionButtonActionPerformed

    private void targetNameComboBoxPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_targetNameComboBoxPropertyChange
        
    }//GEN-LAST:event_targetNameComboBoxPropertyChange

    private void hourSpinnerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_hourSpinnerPropertyChange
        
    }//GEN-LAST:event_hourSpinnerPropertyChange

    private void minuteSpinnerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_minuteSpinnerPropertyChange
        
    }//GEN-LAST:event_minuteSpinnerPropertyChange

    private void daySpinnerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_daySpinnerPropertyChange
        
    }//GEN-LAST:event_daySpinnerPropertyChange

    private void targetNameComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_targetNameComboBoxItemStateChanged
        enableAddConditionButton();
    }//GEN-LAST:event_targetNameComboBoxItemStateChanged

    private void hourSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_hourSpinnerStateChanged
        enableAddConditionButton();
    }//GEN-LAST:event_hourSpinnerStateChanged

    private void minuteSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_minuteSpinnerStateChanged
        enableAddConditionButton();
    }//GEN-LAST:event_minuteSpinnerStateChanged

    private void daySpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_daySpinnerStateChanged
        enableAddConditionButton();
    }//GEN-LAST:event_daySpinnerStateChanged

    private void targetNameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_targetNameComboBoxActionPerformed
        
    }//GEN-LAST:event_targetNameComboBoxActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        giveFeedback(GroupReviewEnum.SAVE_GROUP_NAME, evt);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        giveFeedback(GroupReviewEnum.DELETE_GROUP, evt);
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    @Override
    public Object getProperty(GroupReviewEnum property) {
        switch (property) {
            case GROUP_LABEL:
                return groupLabel;
            case PROCESS_PANEL:
                return processPanel;
            case TITLE_PANEL:
                return titlePanel;
            case CONDITIONS_PANEL:
                return conditionsPanel;
            case CONDITIONS_PANEL_LIST:
                return conditionsPanelList;
            case CONFIGURATION_PANEL:
                return configurationPanel;
            case TARGET_NAME_COMBO_BOX:
                return targetNameComboBox;
            case HOUR_SPINNER:
                return hourSpinner;
            case MINUTE_SPINNER:
                return minuteSpinner;
            case DAY_SPINNER:
                return daySpinner;
            case GROUP_NAME_TEXT:
                return groupNameText;
            case DELETE_TEXT:
                return deleteText;
            default:
                return null;
        }
    }

    @Override
    public void addFeedbackListener(FeedbackListener<GroupReviewEnum, AWTEvent> listener) {
        listeners.add(listener);
    }

    @Override
    public void giveFeedback(GroupReviewEnum tipo, AWTEvent feedback) {
        listeners.forEach(l -> l.giveFeedback(tipo, feedback));
    }
    
    private void enableAddConditionButton() {
        if (targetNameComboBox.getSelectedItem() == null) {
            addConditionButton.setEnabled(false);
            return;
        }
        if (hourSpinner.getValue().equals(0L) && minuteSpinner.getValue().equals(0L)) {
            addConditionButton.setEnabled(false);
            return;
        }
        addConditionButton.setEnabled(true);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addConditionButton;
    private javax.swing.JPanel conditionsPanel;
    private javax.swing.JPanel conditionsPanelList;
    private javax.swing.JPanel configurationPanel;
    private javax.swing.JSpinner daySpinner;
    private javax.swing.JTextField deleteText;
    private javax.swing.JLabel groupLabel;
    private javax.swing.JTextField groupNameText;
    private javax.swing.JSpinner hourSpinner;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JSpinner minuteSpinner;
    private javax.swing.JPanel processPanel;
    private javax.swing.JComboBox<Group> targetNameComboBox;
    private javax.swing.JPanel titlePanel;
    // End of variables declaration//GEN-END:variables
}
