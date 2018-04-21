package share.fair.fairshare.models;

import android.util.Log;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import share.fair.fairshare.Constants.GroupActionTypes;
import share.fair.fairshare.interfaces.IGroupAction;

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

    public void addPaymentAction(PaymentAction paymentAction) {
        this.paymentActions.add(paymentAction);
        this.applyPaymentAction(paymentAction);
    }

    public void cancelAction(PaymentAction paymentAction) {
        paymentAction.makeActionUnEditale();
        addPaymentAction(paymentAction.getOpositeAction());
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
        applyPaymentAction(paymentAction);
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

    private void applyPaymentAction(PaymentAction paymentAction) {
        for(PaymentAction.Operation operation : paymentAction.getOperations()) {
            User user = this.findUserById(operation.getUserId());
            user.setBallance(user.getBalance() + operation.getAmountPaid() - operation.getShare());
        }
    }

    private void applyAddUserAction(GroupActions.AddUserAction addUserAction){
        User newUser = new User(addUserAction.id, addUserAction.userName);
        this.addUser(newUser);
        this.lastSyncTime = addUserAction.timeStamp;
    }

    public PaymentAction getPaymentActionById(String id) {
        for(PaymentAction paymentAction : this.getPaymentActions()) {
            if(paymentAction.getId().equals(id)) {
                return paymentAction;
            }
        }
        return null;
    }

    public void consumeGroupAction(List<IGroupAction> groupActions){
        for(IGroupAction groupAction : groupActions ) {
            Log.d("nir", "action type: " + groupAction.getType());
            if (groupAction.getType().equals(GroupActionTypes.USER_ADDED_ACTION)) {
                this.applyAddUserAction((GroupActions.AddUserAction) groupAction);
            }

        }
    }
}
