package share.fair.fairshare;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import share.fair.fairshare.models.Action;
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
        assertNotEquals(group1.getId(), group2.getId());
    }

    @Test
    public void shouldAllowCreatingGroupWithId(){
        Group testGroup = new Group("testId", "testName");
        assertEquals( testGroup.getName(), "testName");
        assertEquals( testGroup.getId(), "testId");
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
    public void removeUserById() {
        User user = new User("testName");
        String id = user.getId();
        this.group.addUser(user);
        this.group.removeUserById(id);
        assertEquals(this.group.getUsers().size(), 0);
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
        List<Action.Operation> operations = new ArrayList<>();
        operations.add(new Action.Operation(user1.getId(), 50.0, 0.0));
        operations.add(new Action.Operation(user2.getId(), 0.0, 50.0));
        group.addAction(new Action(operations, "bla", "description", true));
        assertEquals(user1.getBallance(), 50.0, 0.001);
    }
}
