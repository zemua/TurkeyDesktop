package devs.mrp.turkeydesktop.view.categorizeprocesspanel;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.common.impl.ConfirmationWithDelayFactory;
import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeService;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.database.type.TypeService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.list.CategorizerElement;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import java.awt.AWTEvent;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.apache.commons.lang3.StringUtils;

public class CatProcessHandler extends PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>, CatProcessPanelFactory> {
    
    private CatProcessPanelFactory factory;

    private ConfirmationWithDelay popupMaker = new ConfirmationWithDelayFactory();
    
    private static final int FILTER_ALL = 0;
    private static final int FILTER_NOT_CATEGORIZED = 1;
    private static final int FILTER_POSITIVE = 2;
    private static final int FILTER_NEGATIVE = 3;
    private static final int FILTER_NEUTRAL = 4;
    private static final int FILTER_DEPENDS = 5;
    
    private final Logger logger = Logger.getLogger(CatProcessHandler.class.getName());
    private final TypeService typeService;
    private final LogAndTypeFacadeService logAndTypeFacadeService;
    
    private FeedbackListener<Type.Types,String> mListener;
    
    public CatProcessHandler(PanelHandlerData<?> data, CatProcessPanelFactory factory) {
        super(data.getFrame(), data.getCaller(), factory);
        typeService = factory.getTypeService();
        logAndTypeFacadeService = factory.getLogAndTypeFacadeService();
        this.factory = factory;
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent> initPanel(CatProcessPanelFactory factory) {
        return factory.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case BACK:
                    exit();
                    break;
                case UPDATE:
                    updateItemsInList();
                    break;
                case TEXT_FILTER:
                    updateItemsInList();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void doExtraBeforeShow() {
        int filter = (Integer)this.getPanel().getProperty(CatProcessEnum.FILTER);
        attachItemsToListPanel(new Date(), new Date(), filter);
    }
    
    private void attachItemsToListPanel(Date from, Date to, int filter) {
        JPanel panel = (JPanel)this.getPanel().getProperty(CatProcessEnum.LIST_PANEL);
        if (panel == null) {return;}
        panel.removeAll(); // clear in case it has been filled before
        Observer<Tripla<String, Long, Type.Types>> subscriber = new Observer<Tripla<String, Long, Type.Types>>() {
            @Override
            public void onComplete() {
                SwingUtilities.invokeLater(() -> {
                    panel.updateUI();
                    panel.revalidate();
                });
            }

            @Override
            public void onError(Throwable thrwbl) {
                // nothing here
            }

            @Override
            public void onNext(Tripla<String, Long, Type.Types> t) {
                CategorizerElement element = new CategorizerElement(panel.getWidth(), panel.getHeight());
                element.init(t.getValue1(), t.getValue3());
                panel.add(element);
                setRadioListener(element);
            }

            @Override
            public void onSubscribe(Disposable d) {
                // nothing here
            }

        };
        logAndTypeFacadeService.getTypedLogGroupedByProcess(from, to)
                .toSortedList((c1,c2) -> c2.getValue2().compareTo(c1.getValue2()))
                .flatMapObservable(Observable::fromIterable)
                .filter(c -> textFromFilter().isEmpty() ? true : StringUtils.containsIgnoreCase(c.getValue1(), textFromFilter()))
                .filter(t -> ifPassFilter(t.getValue3(), filter))
                .subscribe(subscriber);
    }
    
    private String textFromFilter() {
        Object object = this.getPanel().getProperty(CatProcessEnum.TEXT_FILTER);
        if (!(object instanceof JTextField)) {
            logger.log(Level.SEVERE, "incorrect text filter field, fallbacking to empty filter");
            return StringUtils.EMPTY;
        }
        JTextField textField = (JTextField) object;
        return textField.getText();
    }
    
    private void updateItemsInList() {
        Date from = (Date)this.getPanel().getProperty(CatProcessEnum.FROM);
        Date to = (Date)this.getPanel().getProperty(CatProcessEnum.TO);
        int filter = (Integer)this.getPanel().getProperty(CatProcessEnum.FILTER);
        attachItemsToListPanel(from, to, filter);
    }
    
    private boolean ifPassFilter(Type.Types t, int filter) {
        if (filter == FILTER_ALL) {return true;}
        if (t.equals(Type.Types.UNDEFINED) && filter == FILTER_NOT_CATEGORIZED) {return true;}
        if (t.equals(Type.Types.POSITIVE) && filter == FILTER_POSITIVE) {return true;}
        if (t.equals(Type.Types.NEGATIVE) && filter == FILTER_NEGATIVE) {return true;}
        if (t.equals(Type.Types.NEUTRAL) && filter == FILTER_NEUTRAL) {return true;}
        if (t.equals(Type.Types.DEPENDS) && filter == FILTER_DEPENDS) {return true;}
        return false;
    }
    
    private void setRadioListener(CategorizerElement el) {
        if (mListener == null) {
            mListener = new FeedbackListener<Type.Types, String>() {
                @Override
                public void giveFeedback(Type.Types tipo, String feedback) {
                    addCategorization(feedback, tipo);
                }
            };
        }
        el.addFeedbackListener(mListener);
    }
    
    private void addCategorization(String process, Type.Types targetType) {
        typeService.findById(process).defaultIfEmpty(new Type(process, Type.Types.UNDEFINED)).subscribe(savedType -> {
            Type t = new Type();
            t.setProcess(process);
            t.setType(targetType);
            if (isChangeToLessRestrictive(targetType, savedType)) {
                popupMaker.show(this.getFrame(), () -> {
                    // runnable positive
                    typeService.add(t).subscribe();
                }, () -> {
                    // runnable negative
                    updateItemsInList();
                });
            } else {
                typeService.add(t).subscribe();
            }
        });
    }
    
    private boolean isChangeToLessRestrictive(Type.Types target, Type saved) {
        return Type.Types.POSITIVE.equals(target) // if we want to change to positive
                || Type.Types.NEGATIVE.equals(saved.getType()) // if we are moving away from negative
                || (Type.Types.DEPENDS.equals(saved.getType()) && !Type.Types.NEGATIVE.equals(target)); // if we are moving away from depends to any other than negative
    }

    @Override
    protected void doBeforeExit() {
        // intentionally left blank
    }
    
}
