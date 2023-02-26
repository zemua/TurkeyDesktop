package devs.mrp.turkeydesktop.view.container;

import javax.swing.JFrame;

public class MainContainerFactoryImpl implements MainContainerFactory {
    
    private static MainContainerFactoryImpl instance;
    
    private MainContainerFactoryImpl() {}
    
    public static MainContainerFactoryImpl getInstance() {
        if (instance == null) {
            instance = new MainContainerFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public JFrame getContainer() {
        return new TurkeyDesktop();
    }
}
