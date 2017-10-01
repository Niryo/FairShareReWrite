package share.fair.fairshare.models;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by niryo on 28/09/2017.
 */

public class Group {
    private String name;
    private String id;
    private List<User> users = new ArrayList<>();


    public Group(String name) {
        this.id = new BigInteger(130, new SecureRandom()).toString(32);
        this.name = name;
    }

    public Group(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        this.users.add(user);

    }

    public void removeUserById(String userId) {
        User userToRemove = null;
        for(User user: this.users) {
            if(user.getId() == userId) {
                userToRemove = user;
            }
        }
        this.users.remove(userToRemove);
    }
}
