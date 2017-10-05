package share.fair.fairshare;

import org.junit.Test;

import share.fair.fairshare.models.User;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by niryo on 04/10/2017.
 */

public class UserTest {
    @Test
    public void createNewUser() {
        User user1 = new User("testName");
        assertEquals(user1.getName(), "testName");
    }

    @Test
    public void createNewUserWithData() {
        User user = new User("testId","testName", 88.500);
        assertEquals(user.getId(), "testId");
        assertEquals(user.getName(), "testName");
        assertEquals(user.getBallance(), 88.500);
    }

    @Test
    public void newUserShouldHaveAUniqueId() {
        User user1 = new User("testName");
        User user2 = new User("testName");
        assertNotEquals(user1.getId(), user2.getId());
    }

    @Test
    public void newUserShouldHaveZeroBallance() {
        User user = new User("testName");
        assertEquals(user.getBallance(), 0.0);
    }


}
