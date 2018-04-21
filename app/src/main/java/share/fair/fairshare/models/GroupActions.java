package share.fair.fairshare.models;

import share.fair.fairshare.Constants.GroupActionTypes;
import share.fair.fairshare.interfaces.IGroupAction;

/**
 * Created by niryo on 21/04/2018.
 */

public class GroupActions {
    public static class NewPaymentAction implements IGroupAction {
        public String type = GroupActionTypes.PAYMENT_ACTION;
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
    }
}
