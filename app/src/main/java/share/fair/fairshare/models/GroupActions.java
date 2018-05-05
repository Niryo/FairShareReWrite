package share.fair.fairshare.models;

import share.fair.fairshare.Constants.GroupActionTypes;
import share.fair.fairshare.interfaces.IGroupAction;

/**
 * Created by niryo on 21/04/2018.
 */

public class GroupActions {
    public static class NewPaymentAction implements IGroupAction {
        private final String type = GroupActionTypes.NEW_PAYMENT_ACTION;
        public String id;
        public long timeStamp;
        public String installationId;
        public PaymentAction paymentAction;

        public NewPaymentAction(String id, long timeStamp, String installationId, PaymentAction paymentAction) {
            this.id = id;
            this.timeStamp = timeStamp;
            this.installationId = installationId;
            this.paymentAction = paymentAction;
        }

        @Override
        public String getType() {
            return this.type;
        }

        @Override
        public long getTimestamp() {
            return this.timeStamp;
        }
    }

    public static class UpdateLastSyncTimestampAction implements IGroupAction {
        private final String type = GroupActionTypes.UPDATE_LAST_SYNC_TIMESTAMP;
        public long timeStamp;

        public UpdateLastSyncTimestampAction(long timeStamp) {
            this.timeStamp = timeStamp;
        }

        @Override
        public String getType() {
            return this.type;
        }

        @Override
        public long getTimestamp() {
            return this.timeStamp;
        }
    }

    public static class AddUserAction implements IGroupAction {
        private final String type = GroupActionTypes.USER_ADDED_ACTION;
        public String id;
        public String installationId;
        public long timeStamp;
        public String userName;

        public AddUserAction(String id, long timeStamp,String installationId, String userName) {
            this.id = id;
            this.installationId = installationId;
            this.timeStamp = timeStamp;
            this.userName = userName;
        }

        @Override
        public String getType() {
            return this.type;
        }

        @Override
        public long getTimestamp() {
            return this.timeStamp;
        }
    }
}
