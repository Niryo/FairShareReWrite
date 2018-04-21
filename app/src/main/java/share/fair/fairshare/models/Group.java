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
    private String key;
    private List<User> users = new ArrayList<>();
    private List<PaymentAction> paymentActions = new ArrayList<>();


    private long lastSyncTime = 0;


    public Group(String name) {
        this.key = new BigInteger(130, new SecureRandom()).toString(32);
        this.name = name;
    }

    public long getLastSyncTime() {
        return lastSyncTime;
    }
    public Group(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public List<PaymentAction> getPaymentActions() {
        return paymentActions;
    }

    public void addAction(PaymentAction paymentAction) {
        this.paymentActions.add(paymentAction);
        this.applyAction(paymentAction);
    }

    public void cancelAction(PaymentAction paymentAction) {
        paymentAction.makeActionUnEditale();
        addAction(paymentAction.getOpositeAction());
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
        List<PaymentAction.Operation> operations = new ArrayList<>();
        for(User user: users) {
            if(user != userToRemove) {
                operations.add(new PaymentAction.Operation(user.getId(), user.getName(), 0, splitBalance));
            } else {
                operations.add(new PaymentAction.Operation(user.getId(), user.getName(), 0, balance));
            }
        }
        String description = "Remove " + userToRemove.getName() + " from the group";
        PaymentAction paymentAction = new PaymentAction(operations,"creatorName",description, false);
        applyAction(paymentAction);
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

    private void applyAction(PaymentAction paymentAction) {
        for(PaymentAction.Operation operation : paymentAction.getOperations()) {
            User user = this.findUserById(operation.getUserId());
            user.setBallance(user.getBalance() + operation.getAmountPaid() - operation.getShare());
        }
    }

    public PaymentAction getActionById(String id) {
        for(PaymentAction paymentAction : this.getPaymentActions()) {
            if(paymentAction.getId().equals(id)) {
                return paymentAction;
            }
        }
        return null;
    }
}
