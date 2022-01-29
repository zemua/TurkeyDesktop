/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.groups;

import devs.mrp.turkeydesktop.database.group.FGroupService;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.IGroupService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.groups.review.FGroupReviewPanel;
import devs.mrp.turkeydesktop.view.groups.review.GroupReviewEnum;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author miguel
 */
public class GroupsHandler extends PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>> {

    private Group.GroupType type;
    private IGroupService groupService = FGroupService.getService();
    private PanelHandler<GroupReviewEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent>> reviewHandler;
    
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
                    this.getCaller().show();
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
    
    private void refreshPanelList() {
        JPanel panel = (JPanel)this.getPanel().getProperty(GroupsEnum.PANEL_LIST);
        if (panel == null || !(panel instanceof JPanel)) {return;}
        panel.removeAll();
        List<Group> list = type == Group.GroupType.POSITIVE ? groupService.findAllPositive() : groupService.findAllNegative();
        list.forEach(g -> {
            JLabel label = new JLabel();
            label.setText(g.getName());
            setClickListener(label, g);
            panel.add(label);
        });
        panel.revalidate();
        panel.updateUI();
    }
    
    private void addGroup() {
        JTextField field = (JTextField)this.getPanel().getProperty(GroupsEnum.TEXT);
        if (field == null || !(field instanceof JTextField) || field.getText().isBlank()){return;}
        String name = field.getText();
        Group group = new Group();
        group.setName(name);
        group.setType(this.type);
        groupService.add(group);
        field.setText("");
        refreshPanelList();
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
        if (reviewHandler == null) {
            reviewHandler = FGroupReviewPanel.getHandler(this.getFrame(), this, group);
        }
        reviewHandler.show();
    }
    
}
