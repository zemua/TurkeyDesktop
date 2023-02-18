package devs.mrp.turkeydesktop.view.container;

import javax.swing.JFrame;

public class MainContainerFactoryImpl implements MainContainerFactory {
    
    private final FactoryInitializer factory;
    
    public MainContainerFactoryImpl(FactoryInitializer initializer) {
        this.factory = initializer;
    }
    
    @Override
    public JFrame getContainer() {
        return new TurkeyDesktop();
    }
}
