/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.mainpanel;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.common.Feedbacker;
import java.awt.AWTEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

/**
 *
 * @author miguel
 */
public class MainPanel extends FeedbackerPanelWithFetcher<MainEnum, AWTEvent> {

    private List<FeedbackListener<MainEnum, AWTEvent>> listeners = new ArrayList<>();
    
    /**
     * Creates new form MainPanel
     */
    public MainPanel() {
        initComponents();
        giveFeedback(MainEnum.READY, null);
    }

    @Override
    public void addFeedbackListener(FeedbackListener<MainEnum, AWTEvent> listener) {
        listeners.add(listener);
    }

    @Override
    public void giveFeedback(MainEnum tipo, AWTEvent feedback) {
        listeners.forEach(l -> l.giveFeedback(tipo, feedback));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        catProcButton = new javax.swing.JButton();
        timesButton = new javax.swing.JButton();
        configButton = new javax.swing.JButton();
        categorizeTitlesButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        mainTimeLabel = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages"); // NOI18N
        catProcButton.setText(bundle.getString("categorizeproc")); // NOI18N
        catProcButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                catProcButtonActionPerformed(evt);
            }
        });

        timesButton.setText(bundle.getString("timesbutton")); // NOI18N
        timesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timesButtonActionPerformed(evt);
            }
        });

        configButton.setText(bundle.getString("configuration")); // NOI18N
        configButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configButtonActionPerformed(evt);
            }
        });

        categorizeTitlesButton.setText(bundle.getString("categorizetitles")); // NOI18N
        categorizeTitlesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categorizeTitlesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(catProcButton)
                        .addGap(18, 18, 18)
                        .addComponent(categorizeTitlesButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(timesButton)
                        .addGap(18, 18, 18)
                        .addComponent(configButton)))
                .addContainerGap(615, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(catProcButton)
                    .addComponent(categorizeTitlesButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timesButton)
                    .addComponent(configButton))
                .addContainerGap(164, Short.MAX_VALUE))
        );

        mainTimeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mainTimeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/turkey.png"))); // NOI18N
        mainTimeLabel.setText("jLabel1");
        mainTimeLabel.setAlignmentX(0.5F);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainTimeLabel)
                .addContainerGap(737, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainTimeLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void catProcButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_catProcButtonActionPerformed
        giveFeedback(MainEnum.CATEGORIZEPROCESS, evt);
    }//GEN-LAST:event_catProcButtonActionPerformed

    private void timesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timesButtonActionPerformed
        giveFeedback(MainEnum.TIMES, evt);
    }//GEN-LAST:event_timesButtonActionPerformed

    private void configButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configButtonActionPerformed
        giveFeedback(MainEnum.CONFIG, evt);
    }//GEN-LAST:event_configButtonActionPerformed

    private void categorizeTitlesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categorizeTitlesButtonActionPerformed
        giveFeedback(MainEnum.CATEGORIZETITLES, evt);
    }//GEN-LAST:event_categorizeTitlesButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton catProcButton;
    private javax.swing.JButton categorizeTitlesButton;
    private javax.swing.JButton configButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel mainTimeLabel;
    private javax.swing.JButton timesButton;
    // End of variables declaration//GEN-END:variables
    
    @Override
    public Object getProperty(MainEnum type) {
        switch (type) {
            case LOGGER:
                // we don't have a log textArea now
                return null;
            case LABELIZER:
                return mainTimeLabel;
            default:
                return null;
        }
    }
    
    

}
