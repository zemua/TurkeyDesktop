package devs.mrp.turkeydesktop.view.groups;

import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class GroupsHandlerTest {
    
    FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent> panel = CommonMocks.getMock(GroupsPanel.class);
    
    @BeforeEach
    public void setup() {
        FGroupsPanel.setPanelSupplier(() -> panel);
    }

    @Test
    public void add_triggers_groupservice_add() {
        fail("to implement test");
    }
    
}
