/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.groups.review;

import devs.mrp.turkeydesktop.common.FeedbackListener;
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

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages"); // NOI18N
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
                .addComponent(jButton1)
                .addContainerGap(344, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(260, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        giveFeedback(GroupReviewEnum.BACK, evt);
    }//GEN-LAST:event_jButton1ActionPerformed

    @Override
    public Object getProperty(GroupReviewEnum property) {
        switch (property) {
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables
}
