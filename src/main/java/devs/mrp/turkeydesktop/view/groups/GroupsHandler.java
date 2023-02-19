package devs.mrp.turkeydesktop.view.groups;

import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import java.awt.AWTEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GroupsHandler extends PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>, GroupsPanelFactory> {

    private final GroupsPanelFactory factory;
    private final Group.GroupType type;
    private final GroupService groupService;
    
    public GroupsHandler(PanelHandlerData<Group.GroupType> data, GroupsPanelFactory factory) {
        super(data.getFrame(), data.getCaller(), factory);
        this.type = data.getEntity();
        this.factory = factory;
        this.groupService = factory.getGroupSerice();
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent> initPanel(GroupsPanelFactory fact) {
        return fact.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent> panel) {
        panel.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case BACK:
                    exit();
                    break;
                case ADD:
                    addGroup();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void doExtraBeforeShow() {
        refreshPanelList();
    }
    
    private void addToPanelList(Group g, JPanel panel) {
        JLabel label = new JLabel();
        label.setText(g.getName());
        setClickListener(label, g);
        panel.add(label);
    }
    
    private void refreshPanelList() {
        JPanel panel = (JPanel)this.getPanel().getProperty(GroupsEnum.PANEL_LIST);
        if (panel == null || !(panel instanceof JPanel)) {return;}
        panel.removeAll();
        Observer<Group> subscriber = new Observer<>() {
            @Override
            public void onComplete() {
                panel.revalidate();
                panel.updateUI();
            }

            @Override
            public void onError(Throwable thrwbl) {
                // nothing here
            }

            @Override
            public void onNext(Group t) {
                addToPanelList(t, panel);
            }

            @Override
            public void onSubscribe(Disposable d) {
                // nothing to do here
            }
        };
        if (type == Group.GroupType.POSITIVE) {
            groupService.findAllPositive().subscribe(subscriber);
        } else {
            groupService.findAllNegative().subscribe(subscriber);
        }
        
    }
    
    private void addGroup() {
        JTextField field = (JTextField)this.getPanel().getProperty(GroupsEnum.TEXT);
        if (field == null || !(field instanceof JTextField) || field.getText().isBlank()){return;}
        String name = field.getText();
        Group group = new Group();
        group.setName(name);
        group.setType(this.type);
        groupService.add(group)
                .doOnSuccess(r -> {
                    field.setText("");
                    refreshPanelList();
                })
                .subscribe();
    }
    
    private void setClickListener(JLabel label, Group group){
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                launchReviewGroupHandler(group);
            }
        });
    }
    
    private void launchReviewGroupHandler(Group group){
        PanelHandlerData<Group> data = new PanelHandlerData<>(this.getFrame(), this, group);
        factory.getReviewGroupHandler(data).show();
    }

    @Override
    protected void doBeforeExit() {
        // blank
    }
    
}
