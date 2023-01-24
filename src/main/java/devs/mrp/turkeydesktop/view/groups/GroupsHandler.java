package devs.mrp.turkeydesktop.view.groups;

import devs.mrp.turkeydesktop.database.group.GroupFactory;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.groups.review.GroupReviewPanelFactory;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import devs.mrp.turkeydesktop.database.group.GroupService;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class GroupsHandler extends PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>> {

    private Group.GroupType type;
    private GroupService groupService = GroupFactory.getService();
    
    public GroupsHandler(JFrame frame, PanelHandler<?, ?, ?> caller, Group.GroupType type) {
        super(frame, caller);
        this.type = type;
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent> initPanel() {
        return FGroupsPanel.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
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
        GroupReviewPanelFactory.getHandler(this.getFrame(), this, group).show();
    }

    @Override
    protected void doBeforeExit() {
        // blank
    }
    
}
