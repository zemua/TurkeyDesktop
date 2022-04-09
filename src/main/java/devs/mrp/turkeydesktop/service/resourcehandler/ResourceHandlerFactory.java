/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.service.resourcehandler;

import java.awt.Image;

/**
 *
 * @author ncm55070
 */
public class ResourceHandlerFactory {
    
    public static ResourceHandler<Image,ImagesEnum> getImagesHandler() {
        return new ImageResourceHandler();
    }
    
}
