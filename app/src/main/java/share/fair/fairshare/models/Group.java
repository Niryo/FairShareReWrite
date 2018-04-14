package share.fair.fairshare.models;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by niryo on 28/09/2017.
 */

public class Group implements Serializable {
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

    public String getKey() {
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

    private void clearUserBalance(User userToRemove) {
        double balance = userToRemove.getBalance();
        double splitBalance = - balance / (users.size() -1 ) ;
        List<Action.Operation> operations = new ArrayList<>();
        for(User user: users) {
            if(user != userToRemove) {
                operations.add(new Action.Operation(user.getId(), user.getName(), 0, splitBalance));
            } else {
                operations.add(new Action.Operation(user.getId(), user.getName(), 0, balance));
            }
        }
        String description = "Remove " + userToRemove.getName() + " from the group";
        Action action = new Action(operations,"creatorName",description, false);
        applyAction(action);
    }


    private List<Action.Operation> test(User userToRemove) {
        double balance = userToRemove.getBalance();
        double splitBalance = - balance / (users.size() -1 ) ;
        List<Action.Operation> operations = new ArrayList<>();
        for(User user: users) {
            if(user != userToRemove) {
                operations.add(new Action.Operation(user.getId(), user.getName(), 0, splitBalance));
            } else {
                operations.add(new Action.Operation(user.getId(), user.getName(), 0, balance));
            }
        }

        return operations;
    }

    public void removeUserById(String userId) {
        User userToRemove = null;
        for(User user: this.users) {
            if(user.getId().equals(userId)) {
                userToRemove = user;
            }
        }
        clearUserBalance(userToRemove);
        this.users.remove(userToRemove);
    }

    private void applyAction(Action action) {
        for(Action.Operation operation : action.getOperations()) {
            User user = this.findUserById(operation.getUserId());
            user.setBallance(user.getBalance() + operation.getAmountPaid() - operation.getShare());
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
