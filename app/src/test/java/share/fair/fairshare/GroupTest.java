package share.fair.fairshare;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import share.fair.fairshare.models.PaymentAction;
import share.fair.fairshare.models.Group;
import share.fair.fairshare.models.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by niryo on 29/09/2017.
 */

public class GroupTest {
    private Group group;

    @Before
    public void init() {
        this.group = new Group("testGroup");
    }

    @Test
    public void newGroupShouldHaveName(){
        Group testGroup = new Group("testName");
        assertEquals( testGroup.getName(), "testName");
    }

    @Test
    public void newGroupShouldHaveUniqueId(){
        Group group1 = new Group("testName");
        Group group2 = new Group("testName");
        assertNotEquals(group1.getKey(), group2.getKey());
    }

    @Test
    public void shouldAllowCreatingGroupWithId(){
        Group testGroup = new Group("testId", "testName");
        assertEquals( testGroup.getName(), "testName");
        assertEquals( testGroup.getKey(), "testId");
    }

    @Test
    public void addUser() {
        User user = new User("testName");
        this.group.addUser(user);
        assertEquals(this.group.getUsers().get(0), user);
    }

    @Test
    public void createUser() {
        this.group.createUser("testUserName");
        assertEquals(this.group.getUsers().get(0).getName(), "testUserName");
    }



    @Test
    public void removeUserByIdShouldRemoveAUserFromTheUserList() {
        User user = new User("testName");
        String id = user.getId();
        this.group.addUser(user);
        this.group.removeUserById(id);
        assertEquals(this.group.getUsers().size(), 0);
    }

    @Test
    public void removeUserByIdShouldSplitHisDebtsToAllGroupMembers() {
        User user1 = new User("testName");
        User user2 = new User("testName2");
        User user3 = new User("testName2");
        group.addUser(user1);
        group.addUser(user2);
        group.addUser(user3);
        List<PaymentAction.Operation> operations = new ArrayList<>();
        operations.add(new PaymentAction.Operation(user1.getId(), user1.getName(), 100.0, 0.0));
        operations.add(new PaymentAction.Operation(user2.getId(), user2.getName(), 0.0, 50.0));
        operations.add(new PaymentAction.Operation(user3.getId(), user3.getName(), 0.0, 50.0));
        group.addPaymentAction(new PaymentAction(operations, "bla", "description", true));
        group.removeUserById(user3.getId());
        assertEquals(75.0, user1.getBalance(), 0.001);
        assertEquals(-75.0, user2.getBalance(), 0.001);
    }

    @Test
    public void removeUserByIdShouldSplitHisProfitToAllGroupMembers() {
        User user1 = new User("testName");
        User user2 = new User("testName2");
        User user3 = new User("testName2");
        group.addUser(user1);
        group.addUser(user2);
        group.addUser(user3);
        List<PaymentAction.Operation> operations = new ArrayList<>();
        operations.add(new PaymentAction.Operation(user1.getId(), user1.getName(), 100.0, 0.0));
        operations.add(new PaymentAction.Operation(user2.getId(), user2.getName(), 0.0, 50.0));
        operations.add(new PaymentAction.Operation(user3.getId(), user3.getName(), 0.0, 50.0));
        group.addPaymentAction(new PaymentAction(operations, "bla", "description", true));
        group.removeUserById(user1.getId());
        assertEquals(user1.getBalance(), 0.0, 0.001);
        assertEquals(user2.getBalance(), 0.0, 0.001);
    }

    @Test
    public void findUserById() {
        User user = new User("testName");
        String id = user.getId();
        this.group.addUser(user);
        assertEquals(this.group.findUserById(id).getName(), "testName");
    }

    @Test
    public void addAction() {
        User user1 = new User("testName");
        User user2 = new User("testName2");
        group.addUser(user1);
        group.addUser(user2);
        List<PaymentAction.Operation> operations = new ArrayList<>();
        operations.add(new PaymentAction.Operation(user1.getId(), user1.getName(), 50.0, 0.0));
        operations.add(new PaymentAction.Operation(user2.getId(), user2.getName(), 0.0, 50.0));
        group.addPaymentAction(new PaymentAction(operations, "bla", "description", true));
        assertEquals(user1.getBalance(), 50.0, 0.001);
        assertEquals(user2.getBalance(), -50.0, 0.001);
    }

    @Test
    public void getAllActions() {

    }

    @Test
    public void getActionById() {
        //todo: refactor after adding a create actio with id
        User user1 = new User("testName");
        User user2 = new User("testName2");
        group.addUser(user1);
        group.addUser(user2);
        List<PaymentAction.Operation> operations = new ArrayList<>();
        operations.add(new PaymentAction.Operation(user1.getId(), user1.getName(), 50.0, 0.0));
        operations.add(new PaymentAction.Operation(user2.getId(), user2.getName(), 0.0, 50.0));
        PaymentAction paymentAction = new PaymentAction(operations, "bla", "description", true);
        group.addPaymentAction(paymentAction);
        assertEquals(group.getPaymentActionById(paymentAction.getId()), paymentAction);
    }

    @Test
    public void cancelAction(){
        User user1 = new User("testName");
        User user2 = new User("testName2");
        group.addUser(user1);
        group.addUser(user2);
        List<PaymentAction.Operation> operations = new ArrayList<>();
        operations.add(new PaymentAction.Operation(user1.getId(), user1.getName(), 50.0, 0.0));
        operations.add(new PaymentAction.Operation(user2.getId(), user2.getName(), 0.0, 50.0));
        PaymentAction paymentAction = new PaymentAction(operations, "bla", "description", true);
        group.addPaymentAction(paymentAction);
        group.cancelAction(paymentAction);
        assertEquals(paymentAction.isEditable(),false);
        assertEquals(user1.getBalance(), 0.0, 0.001);
        assertEquals(user2.getBalance(), 0.0, 0.001);
        assertEquals(group.getPaymentActions().size(),2);
    }
}
