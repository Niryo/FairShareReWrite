package share.fair.fairshare.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by niryo on 12/10/2017.
 */

public class PaymentAction implements Serializable{
    private List<Operation> operations = new ArrayList<Operation>();
    private long timeCreated;
    private String creatorInstallationId;

    public String getDescription() {
        return description;
    }

    private String description;
    private String creatorName;
    private String id;

    public void makeActionUnEditale() {
        isEditable = false;
    }

    private boolean isEditable;

    public String getId() {
        return id;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public PaymentAction getOpositeAction() {
        List<PaymentAction.Operation> oppositeOperations = new ArrayList<>();
        for(PaymentAction.Operation operation: this.operations){
            oppositeOperations.add(operation.getOppositeOperation());
        }
        return new PaymentAction(oppositeOperations, creatorName,description +" (cancelled)",false);
    }

    public long getTimeCreated() {
        return timeCreated;
    }


    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isEditable", this.isEditable);
            jsonObject.put("description", this.description);
            jsonObject.put("timeStamp", this.timeCreated);
            jsonObject.put("actionId", this.id);
            jsonObject.put("creatorName", this.creatorName);
            jsonObject.put("installationId", this.creatorInstallationId);
            JSONArray jsonOperations = new JSONArray();
            for (Operation operation : operations) {
                jsonOperations.put(operation.toJSON());
            }
            jsonObject.put("operations", jsonOperations);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public PaymentAction(List<Operation> operations, String creatorName, String description, boolean isEditable){
        this.id = new BigInteger(130, new SecureRandom()).toString(32).substring(0, 10);
        this.timeCreated = new Date().getTime();

        this.isEditable = isEditable;
        this.operations = operations;
        this.creatorName = creatorName;
        this.description = description;

    }

    public PaymentAction(JSONObject jsonAction) {
        try {
            this.isEditable = jsonAction.getBoolean("isEditable");
            this.description = jsonAction.getString("description");
            this.timeCreated = jsonAction.getLong("timeStamp");
            this.id = jsonAction.getString("actionId");
            this.creatorName = jsonAction.getString("creatorName");
            JSONArray jsonOperations = jsonAction.getJSONArray("operations");
            for (int i = 0; i < jsonOperations.length(); i++) {
                operations.add(new Operation(jsonOperations.getJSONObject(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class Operation implements Serializable{
        private final String  userId;
        private final String userName;
        private final double  amountPaid;
        private final double share;
        private final boolean isShareAutoCalculated;

        public boolean isShareAutoCalculated() {
            return isShareAutoCalculated;
        }

        public Operation(JSONObject jsonOperation) throws JSONException {
                this.isShareAutoCalculated = !jsonOperation.getBoolean("hasUserAddedShare");
                this.userName = jsonOperation.getString("username");
                this.userId = jsonOperation.getString("userId");
                this.amountPaid = jsonOperation.getDouble("paid");
                this.share = jsonOperation.getDouble("share");
        }

        public Operation(String userId, String userName, double amountPaid, double share) {
            this.userId = userId;
            this.userName = userName;
            this.amountPaid = amountPaid;
            this.share = share;
            this.isShareAutoCalculated = false;
        }


        public Operation(String userId, String userName, double amountPaid, double share, boolean isShareAutoCalculated) {
            this.userId = userId;
            this.userName = userName;

            this.amountPaid = amountPaid;
            this.share = share;
            this.isShareAutoCalculated = isShareAutoCalculated;
        }

        public Operation getOppositeOperation() {
            return new Operation(userId, userName, -amountPaid, -share , false);
        }
        public String getUserName() {
            return userName;
        }

        public String getUserId() {
            return userId;
        }

        public double getAmountPaid() {
            return amountPaid;
        }

        public double getShare() {
            return share;
        }

        public JSONObject toJSON() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("hasUserAddedShare", !this.isShareAutoCalculated);
                jsonObject.put("username", this.userName);
                jsonObject.put("userId", this.userId);
                jsonObject.put("paid", this.amountPaid);
                jsonObject.put("share", this.share);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        }
}
