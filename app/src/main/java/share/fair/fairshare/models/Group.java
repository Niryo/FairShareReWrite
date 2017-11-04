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
    private List<Action> actions = new ArrayList<>();


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

    public List<Action> getActions() {
        return actions;
    }

    public void addAction(Action action) {
        this.actions.add(action);
        this.applyAction(action);
    }

    public void cancelAction(Action action) {
        action.makeActionUnEditale();
        addAction(action.getOpositeAction());
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

    public void createUser(String userName){
        User newUser = new User(userName);
        this.users.add(newUser);
    }

    public User findUserById(String userId){
        for(User user: this.users) {
            if(user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public void removeUserById(String userId) {
        User userToRemove = null;
        for(User user: this.users) {
            if(user.getId().equals(userId)) {
                userToRemove = user;
            }
        }
        this.users.remove(userToRemove);
    }

    private void applyAction(Action action) {
        for(Action.Operation operation : action.getOperations()) {
            User user = this.findUserById(operation.getUserId());
            user.setBallance(user.getBallance() + operation.getAmountPaid() - operation.getShare());
        }
    }

    public Action getActionById(String id) {
        for(Action action: this.getActions()) {
            if(action.getId().equals(id)) {
                return action;
            }
        }
        return null;
    }
}
