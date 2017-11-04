package share.fair.fairshare;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import share.fair.fairshare.models.Action;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by niryo on 12/10/2017.
 */

public class ActionTest {
    private List<Action.Operation> operationList = new ArrayList<>();

    @Before
    public void init() {
        this.operationList.add(new Action.Operation("user1", 100.0, 50.0));
        this.operationList.add(new Action.Operation("user2",  0.0, 50.0));
    }

    @Test
    public void createNewAction() {
        Action action = new Action(this.operationList,"testCreatorName", "testDescription", true);
        assertEquals(action.getCreatorName(),"testCreatorName");
        assertEquals(action.getDescription(),"testDescription");
        assertEquals(action.isEditable(),true);
        assertEquals(action.getOperations(), this.operationList);
    }

    @Test
    public void shouldHaveCreationTimestamp() {
        Action action = new Action(this.operationList,"testCreatorName", "testDescription", true);
        assertTrue(action.getTimeCreated() + 100  >=  new Date().getTime());
    }

    @Test
    public void shouldHaveUniqueId() {
        Action action1 = new Action(this.operationList,"testCreatorName", "testDescription", true);
        Action action2 = new Action(this.operationList,"testCreatorName", "testDescription", true);
        assertNotEquals(action1.getId(),action2.getId());
    }

    @Test
    public void getOppositeActionShouldContainOppositeOperations() {
        Action action = new Action(this.operationList,"testCreatorName", "testDescription", true);
        Action oppositeAction = action.getOpositeAction();
        assertEquals(oppositeAction.getOperations().get(0).getAmountPaid(), -100.0, 0.001);
        assertEquals(oppositeAction.getOperations().get(0).getShare(), -50.0, 0.001);
        assertEquals(oppositeAction.getOperations().get(1).getAmountPaid(), 0.0, 0.001);
        assertEquals(oppositeAction.getOperations().get(1).getShare(), -50.0, 0.001);
        assertEquals(oppositeAction.getDescription(), action.getDescription() +" (cancelled)");
    }
    @Test
    public void oppositeActionShouldContainCancelInDescription() {
        Action action = new Action(this.operationList,"testCreatorName", "testDescription", true);
        Action oppositeAction = action.getOpositeAction();
        assertEquals(oppositeAction.getDescription(), action.getDescription() +" (cancelled)");
    }
    @Test
    public void oppositeActionShouldBeUnEditable() {
        Action action = new Action(this.operationList,"testCreatorName", "testDescription", true);
        Action oppositeAction = action.getOpositeAction();
        assertEquals(oppositeAction.isEditable(), false);
    }
    @Test
    public void oppositeActionShouldShouldNotHavAutoCalculatedShare() {
        List<Action.Operation> operationListWithAutoCalculatedShare = new ArrayList<>();
        operationListWithAutoCalculatedShare.add(new Action.Operation("user1", 100.0, 50.0, true));
        operationListWithAutoCalculatedShare.add(new Action.Operation("user2",  0.0, 50.0, true));
        Action action = new Action(operationListWithAutoCalculatedShare,"testCreatorName", "testDescription", true);
        Action oppositeAction = action.getOpositeAction();
        assertEquals(oppositeAction.getOperations().get(0).isShareAutoCalculated(), false);
        assertEquals(oppositeAction.getOperations().get(1).isShareAutoCalculated(), false);
    }
}
