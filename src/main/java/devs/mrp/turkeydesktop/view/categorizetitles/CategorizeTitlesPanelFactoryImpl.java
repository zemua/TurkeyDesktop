package devs.mrp.turkeydesktop.view.categorizetitles;

import devs.mrp.turkeydesktop.database.titledlog.TitledLogServiceFacade;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public class CategorizeTitlesPanelFactoryImpl implements CategorizeTitlesPanelFactory {
    
    private FactoryInitializer factory;
    
    public CategorizeTitlesPanelFactoryImpl(FactoryInitializer initializer) {
        this.factory = initializer;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent> getPanel(){
        return new CategorizeTitlesPanel();
    }
    
    @Override
    public PanelHandler<CategorizeTitlesEnum, AWTEvent, FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent>> getHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        return new CategorizeTitlesHandler(frame, caller, this);
    }

    @Override
    public TitledLogServiceFacade getTitledLogServiceFacade() {
        return factory.getTitledLogFacadeFactory().getService();
    }
    
}
