/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

/**
 *
 * @author zemua
 */
public interface ConfirmationWithDelay {
    public void show(String title, String msg, String cancel, String confirm, Runnable runnable);
}
