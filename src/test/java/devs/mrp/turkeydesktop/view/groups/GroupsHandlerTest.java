package devs.mrp.turkeydesktop.view.groups;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.GroupFactoryImpl;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.TestObserver;
import java.awt.AWTEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupsHandlerTest {
    
    private JFrame frame;
    PanelHandler<?,?,?> caller;
    private GroupsPanel panel = CommonMocks.getMock(GroupsPanel.class);
    private GroupService groupService = CommonMocks.getMock(GroupService.class);
    
    private GroupsHandler groupsHandler;
    
    @Before
    public void setup() {
        GroupsPanelFactoryImpl.setPanelSupplier(() -> panel);
        GroupFactoryImpl.setGroupServiceSupplier(() -> groupService);
        frame = mock(JFrame.class);
        caller = mock(PanelHandler.class);
        
        groupsHandler = new GroupsHandler(frame, caller, Group.GroupType.POSITIVE);
    }

    @Test
    public void add_triggers_and_subscribes_groupservice_add() throws InterruptedException {
        ArgumentCaptor<FeedbackListener<GroupsEnum,AWTEvent>> feedbackCaptor = ArgumentCaptor.forClass(FeedbackListener.class);
        verify(panel, times(1)).addFeedbackListener(feedbackCaptor.capture());
        var feedbackLister = feedbackCaptor.getValue();
        
        Group group = new Group();
        group.setName("group name");
        group.setType(Group.GroupType.POSITIVE);
        
        JTextField field = mock(JTextField.class);
        JPanel list = mock(JPanel.class);
        TestObserver testObserver = TestObserver.create();
        Single<Long> addResult = Single.just(1L).doOnSubscribe(d -> testObserver.onSubscribe(d));
        Observable<Group> groups = Observable.just(group);
        
        when(panel.getProperty(GroupsEnum.TEXT)).thenReturn(field);
        when(field.getText()).thenReturn(group.getName());
        when(groupService.add(ArgumentMatchers.refEq(group))).thenReturn(addResult);
        when(panel.getProperty(GroupsEnum.PANEL_LIST)).thenReturn(list);
        when(groupService.findAllPositive()).thenReturn(groups);
        
        feedbackLister.giveFeedback(GroupsEnum.ADD, null);
        
        verify(groupService, times(1)).add(ArgumentMatchers.refEq(group));
        assertTrue(testObserver.hasSubscription());
        verify(list).revalidate();
        verify(list).updateUI();
    }
    
}
