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
        jButton1 = new javax.swing.JButton();

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
                        .addComponent(jLabel3)))
                .addContainerGap(303, Short.MAX_VALUE))
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
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel1);

        jButton1.setText(bundle.getString("back")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        giveFeedback(ConfigurationPanelEnum.BACK, evt);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            default:
                return null;
        }
    }

    @Override
    public void addFeedbackListener(FeedbackListener<ConfigurationPanelEnum, AWTEvent> listener) {
        listeners.add(listener);
    }

    @Override
    public void giveFeedback(ConfigurationPanelEnum tipo, AWTEvent feedback) {
        listeners.forEach(l -> l.giveFeedback(tipo, feedback));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner fromHours;
    private javax.swing.JSpinner fromMinutes;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton lockDownButton;
    private javax.swing.JSpinner minutesLock;
    private javax.swing.JToggleButton notifyLockButton;
    private javax.swing.JLabel proportionLabel;
    private javax.swing.JSlider proportionSlider;
    private javax.swing.JSpinner toHours;
    private javax.swing.JSpinner toMinutes;
    // End of variables declaration//GEN-END:variables
}
