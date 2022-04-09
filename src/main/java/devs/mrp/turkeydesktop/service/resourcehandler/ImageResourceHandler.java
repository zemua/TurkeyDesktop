/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.service.resourcehandler;

import java.awt.Image;
import java.awt.Toolkit;

/**
 *
 * @author ncm55070
 */
public class ImageResourceHandler implements ResourceHandler<Image, ImagesEnum> {

    @Override
    public Image getResource(ImagesEnum name) {
        switch (name) {
            case TURKEY:
                return Toolkit.getDefaultToolkit().getImage(getClass().getResource("/turkey.png"));
            case BADGE:
                return Toolkit.getDefaultToolkit().getImage(getClass().getResource("/badge.png"));
            case FIRE:
                return Toolkit.getDefaultToolkit().getImage(getClass().getResource("/fire.png"));
            case SNOW:
                return Toolkit.getDefaultToolkit().getImage(getClass().getResource("/snow.png"));
            default:
                return Toolkit.getDefaultToolkit().getImage(getClass().getResource("/no-image.png"));
        }
    }
    
}
