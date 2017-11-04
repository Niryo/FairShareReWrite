package share.fair.fairshare.models;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by niryo on 12/10/2017.
 */

public class Action {
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

    public Action getOpositeAction() {
        List<Action.Operation> oppositeOperations = new ArrayList<>();
        for(Action.Operation operation: this.operations){
            oppositeOperations.add(operation.getOppositeOperation());
        }
        return new Action(oppositeOperations, creatorName,description +" (cancelled)",false);
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public Action(List<Operation> operations, String creatorName, String description, boolean isEditable){
        this.id = new BigInteger(130, new SecureRandom()).toString(32).substring(0, 10);
        this.timeCreated = new Date().getTime();

        this.isEditable = isEditable;
        this.operations = operations;
        this.creatorName = creatorName;
        this.description = description;

    }

    public static class Operation {
        private final String  userId;
        private final double  amountPaid;
        private final double share;
        private final boolean isShareAutoCalculated;

        public boolean isShareAutoCalculated() {
            return isShareAutoCalculated;
        }

        public Operation(String userId, double amountPaid, double share) {
            this.userId = userId;
            this.amountPaid = amountPaid;
            this.share = share;
            this.isShareAutoCalculated = false;
        }

        public Operation(String userId, double amountPaid, double share, boolean isShareAutoCalculated) {
            this.userId = userId;
            this.amountPaid = amountPaid;
            this.share = share;
            this.isShareAutoCalculated = isShareAutoCalculated;
        }

        public Operation getOppositeOperation() {
            return new Operation(userId, -amountPaid, -share , false);
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

    }
}
