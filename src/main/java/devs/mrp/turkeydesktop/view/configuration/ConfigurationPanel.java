/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.configuration;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author miguel
 */
public class ConfigurationPanel extends FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent> {

    private List<FeedbackListener<ConfigurationPanelEnum, AWTEvent>> listeners = new ArrayList<>();
    /**
     * Creates new form ConfigurationPanel
     */
    public ConfigurationPanel() {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        proportionLabel = new javax.swing.JLabel();
        proportionSlider = new javax.swing.JSlider();
        lockDownButton = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        fromHours = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        fromMinutes = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        toHours = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        toMinutes = new javax.swing.JSpinner();
        notifyLockButton = new javax.swing.JToggleButton();
        minutesLock = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        notifyMinLeftButton = new javax.swing.JToggleButton();
        jLabel6 = new javax.swing.JLabel();
        minLeftToNotify = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        exportToggle = new javax.swing.JToggleButton();
        jLabel8 = new javax.swing.JLabel();
        exportButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        importButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        importPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        idleSpinner = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        notifySound = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        midnightSpinner = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();

        jScrollPane1.setHorizontalScrollBar(null);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages"); // NOI18N
        proportionLabel.setText(bundle.getString("proportion")); // NOI18N

        proportionSlider.setMajorTickSpacing(1);
        proportionSlider.setMaximum(10);
        proportionSlider.setMinimum(1);
        proportionSlider.setMinorTickSpacing(1);
        proportionSlider.setPaintLabels(true);
        proportionSlider.setPaintTicks(true);
        proportionSlider.setSnapToTicks(true);
        proportionSlider.setValue(Integer.parseInt(ConfigurationEnum.PROPORTION.getDefault()));
        proportionSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                proportionSliderStateChanged(evt);
            }
        });
        proportionSlider.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                proportionSliderPropertyChange(evt);
            }
        });

        lockDownButton.setText(bundle.getString("lockdown")); // NOI18N
        lockDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lockDownButtonActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle.getString("from")); // NOI18N

        fromHours.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(23L), Long.valueOf(1L)));
        fromHours.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fromHoursStateChanged(evt);
            }
        });

        jLabel2.setText(bundle.getString("hours")); // NOI18N

        fromMinutes.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(59L), Long.valueOf(1L)));
        fromMinutes.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fromMinutesStateChanged(evt);
            }
        });

        jLabel4.setText(bundle.getString("to")); // NOI18N

        toHours.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(23L), Long.valueOf(1L)));
        toHours.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                toHoursStateChanged(evt);
            }
        });

        jLabel5.setText(bundle.getString("hours")); // NOI18N

        toMinutes.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(59L), Long.valueOf(1L)));
        toMinutes.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                toMinutesStateChanged(evt);
            }
        });

        notifyLockButton.setText(bundle.getString("notify")); // NOI18N
        notifyLockButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                notifyLockButtonStateChanged(evt);
            }
        });

        minutesLock.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(10L), Long.valueOf(1L), Long.valueOf(60L), Long.valueOf(1L)));
        minutesLock.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                minutesLockStateChanged(evt);
            }
        });

        jLabel3.setText(bundle.getString("minBeforeLock")); // NOI18N

        notifyMinLeftButton.setText(bundle.getString("notify")); // NOI18N
        notifyMinLeftButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                notifyMinLeftButtonStateChanged(evt);
            }
        });

        jLabel6.setText(bundle.getString("whenIhave")); // NOI18N

        minLeftToNotify.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(10L), Long.valueOf(1L), Long.valueOf(60L), Long.valueOf(1L)));
        minLeftToNotify.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                minLeftToNotifyStateChanged(evt);
            }
        });

        jLabel7.setText(bundle.getString("minLeft")); // NOI18N

        exportToggle.setText(bundle.getString("exportAccumulated")); // NOI18N
        exportToggle.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                exportToggleStateChanged(evt);
            }
        });

        jLabel8.setText(bundle.getString("toThisTxt")); // NOI18N

        exportButton.setText(bundle.getString("noFile")); // NOI18N
        exportButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                exportButtonStateChanged(evt);
            }
        });
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        jLabel9.setText(bundle.getString("addImport")); // NOI18N

        importButton.setText(bundle.getString("select")); // NOI18N
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importButtonActionPerformed(evt);
            }
        });

        importPanel.setLayout(new javax.swing.BoxLayout(importPanel, javax.swing.BoxLayout.PAGE_AXIS));
        jScrollPane2.setViewportView(importPanel);

        jLabel10.setText(bundle.getString("considerIdel")); // NOI18N

        idleSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(1L), Long.valueOf(1L), Long.valueOf(15L), Long.valueOf(1L)));
        idleSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                idleSpinnerStateChanged(evt);
            }
        });

        jLabel11.setText(bundle.getString("minutes")); // NOI18N

        jLabel12.setText(bundle.getString("forPositiveOnly")); // NOI18N

        notifySound.setText(bundle.getString("notifyWithSound")); // NOI18N
        notifySound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notifySoundActionPerformed(evt);
            }
        });

        jLabel13.setText(bundle.getString("changeOfDay")); // NOI18N

        midnightSpinner.setModel(new javax.swing.SpinnerNumberModel(3, 0, 6, 1));
        midnightSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                midnightSpinnerStateChanged(evt);
            }
        });

        jLabel14.setText(bundle.getString("hoursAfterMidnight")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(proportionLabel)
                        .addGap(12, 12, 12)
                        .addComponent(proportionSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lockDownButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fromHours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fromMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toHours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(notifyLockButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(minutesLock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(notifyMinLeftButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(minLeftToNotify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(notifySound))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(exportToggle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idleSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(midnightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)))
                .addContainerGap(263, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(proportionLabel)
                    .addComponent(proportionSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lockDownButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fromHours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(fromMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(toHours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(toMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(notifyLockButton)
                    .addComponent(minutesLock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(notifyMinLeftButton)
                    .addComponent(jLabel6)
                    .addComponent(minLeftToNotify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(notifySound))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exportToggle)
                    .addComponent(jLabel8)
                    .addComponent(exportButton))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(importButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(idleSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(midnightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel1);

        backButton.setText(bundle.getString("back")); // NOI18N
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        giveFeedback(ConfigurationPanelEnum.BACK, evt);
    }//GEN-LAST:event_backButtonActionPerformed

    private void proportionSliderPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_proportionSliderPropertyChange
        //giveFeedback(ConfigurationPanelEnum.PROPORTION, null);
    }//GEN-LAST:event_proportionSliderPropertyChange

    private void proportionSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_proportionSliderStateChanged
        giveFeedback(ConfigurationPanelEnum.PROPORTION, null);
    }//GEN-LAST:event_proportionSliderStateChanged

    private void lockDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lockDownButtonActionPerformed
        giveFeedback(ConfigurationPanelEnum.LOCKDOWN, evt);
    }//GEN-LAST:event_lockDownButtonActionPerformed

    private void fromHoursStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fromHoursStateChanged
        giveFeedback(ConfigurationPanelEnum.LOCKDOWN_FROM, null);
    }//GEN-LAST:event_fromHoursStateChanged

    private void fromMinutesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fromMinutesStateChanged
        giveFeedback(ConfigurationPanelEnum.LOCKDOWN_FROM, null);
    }//GEN-LAST:event_fromMinutesStateChanged

    private void toHoursStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_toHoursStateChanged
        giveFeedback(ConfigurationPanelEnum.LOCKDOWN_TO, null);
    }//GEN-LAST:event_toHoursStateChanged

    private void toMinutesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_toMinutesStateChanged
        giveFeedback(ConfigurationPanelEnum.LOCKDOWN_TO, null);
    }//GEN-LAST:event_toMinutesStateChanged

    private void notifyLockButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_notifyLockButtonStateChanged
        giveFeedback(ConfigurationPanelEnum.LOCKDOWN_NOTIFY, null);
    }//GEN-LAST:event_notifyLockButtonStateChanged

    private void minutesLockStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_minutesLockStateChanged
        giveFeedback(ConfigurationPanelEnum.LOCKDOWN_NOTIFY_MIN, null);
    }//GEN-LAST:event_minutesLockStateChanged

    private void notifyMinLeftButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_notifyMinLeftButtonStateChanged
        giveFeedback(ConfigurationPanelEnum.NOTIFY_MIN_LEFT, null);
    }//GEN-LAST:event_notifyMinLeftButtonStateChanged

    private void minLeftToNotifyStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_minLeftToNotifyStateChanged
        giveFeedback(ConfigurationPanelEnum.NOTIFY_MIN_LEFT_QTY, null);
    }//GEN-LAST:event_minLeftToNotifyStateChanged

    private void exportToggleStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_exportToggleStateChanged
        giveFeedback(ConfigurationPanelEnum.EXPORT_TOGGLE, null);
    }//GEN-LAST:event_exportToggleStateChanged

    private void exportButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_exportButtonStateChanged

    }//GEN-LAST:event_exportButtonStateChanged

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        giveFeedback(ConfigurationPanelEnum.EXPORT_BUTTON, evt);
    }//GEN-LAST:event_exportButtonActionPerformed

    private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
        giveFeedback(ConfigurationPanelEnum.IMPORT_BUTTON, evt);
    }//GEN-LAST:event_importButtonActionPerformed

    private void idleSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_idleSpinnerStateChanged
        giveFeedback(ConfigurationPanelEnum.IDLE_SPINNER, null);
    }//GEN-LAST:event_idleSpinnerStateChanged

    private void notifySoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notifySoundActionPerformed
        giveFeedback(ConfigurationPanelEnum.NOTIFY_WITH_SOUND, evt);
    }//GEN-LAST:event_notifySoundActionPerformed

    private void midnightSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_midnightSpinnerStateChanged
        giveFeedback(ConfigurationPanelEnum.CHANGE_OF_DAY, null);
    }//GEN-LAST:event_midnightSpinnerStateChanged

    @Override
    public void addFeedbackListener(FeedbackListener<ConfigurationPanelEnum, AWTEvent> listener) {
        listeners.add(listener);
    }

    @Override
    public void giveFeedback(ConfigurationPanelEnum tipo, AWTEvent feedback) {
        listeners.forEach(l -> l.giveFeedback(tipo, feedback));
    }

        @Override
    public Object getProperty(ConfigurationPanelEnum property) {
        switch (property) {
            case PROPORTION:
                return proportionSlider;
            case LOCKDOWN:
                return lockDownButton;
            case LOCKDOWN_FROM_HOUR:
                return fromHours;
            case LOCKDOWN_FROM_MIN:
                return fromMinutes;
            case LOCKDOWN_TO_HOUR:
                return toHours;
            case LOCKDOWN_TO_MIN:
                return toMinutes;
            case LOCKDOWN_NOTIFY:
                return notifyLockButton;
            case LOCKDOWN_NOTIFY_MIN:
                return minutesLock;
            case NOTIFY_MIN_LEFT:
                return notifyMinLeftButton;
            case NOTIFY_MIN_LEFT_QTY:
                return minLeftToNotify;
            case EXPORT_BUTTON:
                return exportButton;
            case EXPORT_TOGGLE:
                return exportToggle;
            case IMPORT_BUTTON:
                return importButton;
            case IMPORT_PANEL:
                return importPanel;
            case IDLE_SPINNER:
                return idleSpinner;
            case NOTIFY_WITH_SOUND:
                return notifySound;
            case CHANGE_OF_DAY:
                return midnightSpinner;
            default:
                return null;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton exportButton;
    private javax.swing.JToggleButton exportToggle;
    private javax.swing.JSpinner fromHours;
    private javax.swing.JSpinner fromMinutes;
    private javax.swing.JSpinner idleSpinner;
    private javax.swing.JButton importButton;
    private javax.swing.JPanel importPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToggleButton lockDownButton;
    private javax.swing.JSpinner midnightSpinner;
    private javax.swing.JSpinner minLeftToNotify;
    private javax.swing.JSpinner minutesLock;
    private javax.swing.JToggleButton notifyLockButton;
    private javax.swing.JToggleButton notifyMinLeftButton;
    private javax.swing.JCheckBox notifySound;
    private javax.swing.JLabel proportionLabel;
    private javax.swing.JSlider proportionSlider;
    private javax.swing.JSpinner toHours;
    private javax.swing.JSpinner toMinutes;
    // End of variables declaration//GEN-END:variables
}
