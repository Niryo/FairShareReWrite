package share.fair.fairshare;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import share.fair.fairshare.models.PaymentAction;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by niryo on 12/10/2017.
 */

public class ActionTest {
    private List<PaymentAction.Operation> operationList = new ArrayList<>();

    @Before
    public void init() {
        this.operationList.add(new PaymentAction.Operation("user1", "user1", 100.0, 50.0));
        this.operationList.add(new PaymentAction.Operation("user2", "user2", 0.0, 50.0));
    }

    @Test
    public void createNewAction() {
        PaymentAction paymentAction = new PaymentAction(this.operationList,"testCreatorName", "testDescription", true);
        assertEquals(paymentAction.getCreatorName(),"testCreatorName");
        assertEquals(paymentAction.getDescription(),"testDescription");
        assertEquals(paymentAction.isEditable(),true);
        assertEquals(paymentAction.getOperations(), this.operationList);
    }

    @Test
    public void shouldHaveCreationTimestamp() {
        PaymentAction paymentAction = new PaymentAction(this.operationList,"testCreatorName", "testDescription", true);
        assertTrue(paymentAction.getTimeCreated() + 100  >=  new Date().getTime());
    }

    @Test
    public void shouldHaveUniqueId() {
        PaymentAction paymentAction1 = new PaymentAction(this.operationList,"testCreatorName", "testDescription", true);
        PaymentAction paymentAction2 = new PaymentAction(this.operationList,"testCreatorName", "testDescription", true);
        assertNotEquals(paymentAction1.getId(), paymentAction2.getId());
    }

    @Test
    public void getOppositeActionShouldContainOppositeOperations() {
        PaymentAction paymentAction = new PaymentAction(this.operationList,"testCreatorName", "testDescription", true);
        PaymentAction oppositePaymentAction = paymentAction.getOpositeAction();
        assertEquals(oppositePaymentAction.getOperations().get(0).getAmountPaid(), -100.0, 0.001);
        assertEquals(oppositePaymentAction.getOperations().get(0).getShare(), -50.0, 0.001);
        assertEquals(oppositePaymentAction.getOperations().get(1).getAmountPaid(), 0.0, 0.001);
        assertEquals(oppositePaymentAction.getOperations().get(1).getShare(), -50.0, 0.001);
        assertEquals(oppositePaymentAction.getDescription(), paymentAction.getDescription() +" (cancelled)");
    }
    @Test
    public void oppositeActionShouldContainCancelInDescription() {
        PaymentAction paymentAction = new PaymentAction(this.operationList,"testCreatorName", "testDescription", true);
        PaymentAction oppositePaymentAction = paymentAction.getOpositeAction();
        assertEquals(oppositePaymentAction.getDescription(), paymentAction.getDescription() +" (cancelled)");
    }
    @Test
    public void oppositeActionShouldBeUnEditable() {
        PaymentAction paymentAction = new PaymentAction(this.operationList,"testCreatorName", "testDescription", true);
        PaymentAction oppositePaymentAction = paymentAction.getOpositeAction();
        assertEquals(oppositePaymentAction.isEditable(), false);
    }
    @Test
    public void oppositeActionShouldShouldNotHavAutoCalculatedShare() {
        List<PaymentAction.Operation> operationListWithAutoCalculatedShare = new ArrayList<>();
        operationListWithAutoCalculatedShare.add(new PaymentAction.Operation("user1", "user1", 100.0, 50.0, true));
        operationListWithAutoCalculatedShare.add(new PaymentAction.Operation("user2", "user2", 0.0, 50.0, true));
        PaymentAction paymentAction = new PaymentAction(operationListWithAutoCalculatedShare,"testCreatorName", "testDescription", true);
        PaymentAction oppositePaymentAction = paymentAction.getOpositeAction();
        assertEquals(oppositePaymentAction.getOperations().get(0).isShareAutoCalculated(), false);
        assertEquals(oppositePaymentAction.getOperations().get(1).isShareAutoCalculated(), false);
    }
}
