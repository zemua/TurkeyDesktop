/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

import javax.swing.JFrame;

/**
 *
 * @author zemua
 */
public interface ConfirmationWithDelay {
    public void show(JFrame parent, String msg, String cancel, String confirm, Runnable runnablePositive, Runnable runnableNegative, int secondsDelay);
    public void show(JFrame parent, Runnable runnablePositive, Runnable runnableNegative);
}
