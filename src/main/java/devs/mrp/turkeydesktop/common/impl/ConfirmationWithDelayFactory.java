/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common.impl;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import javax.swing.JFrame;

/**
 *
 * @author zemua
 */
public class ConfirmationWithDelayFactory implements ConfirmationWithDelay {

    @Override
    public void show(JFrame parent, String msg, String cancel, String confirm, Runnable runnablePositive, Runnable runnableNegative, int secondsDelay) {
        new ConfirmationWithDelayImpl().show(parent, msg, cancel, confirm, runnablePositive, runnableNegative, secondsDelay);
    }

    @Override
    public void show(JFrame parent, Runnable runnablePositive, Runnable runnableNegative) {
        new ConfirmationWithDelayImpl().show(parent, runnablePositive, runnableNegative);
    }
    
}
