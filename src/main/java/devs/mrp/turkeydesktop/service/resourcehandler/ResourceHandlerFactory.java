package devs.mrp.turkeydesktop.service.resourcehandler;

import java.awt.Image;

public class ResourceHandlerFactory {
    
    public static ResourceHandler<Image,ImagesEnum> getImagesHandler() {
        return new ImageResourceHandler();
    }
    
}
