package devs.mrp.turkeydesktop.view.mainpanel;

import devs.mrp.turkeydesktop.view.FeedbackerPanel;

public abstract class FeedbackerPanelWithFetcher<T, E> extends FeedbackerPanel<T, E> {
    
    public abstract Object getProperty(T property);
    
}
