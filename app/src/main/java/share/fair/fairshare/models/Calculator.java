package share.fair.fairshare.models;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by niryo on 04/10/2017.
 */

public class Calculator {
    public static Double EPSILON = 0.0001;
    public static Double CalculateShares(List<BillLine> billLines) throws TotalPaidNotEqualsToTotalShareException, NotEnoughMoneyToPayTheBillException {
        double totalPaid = 0.0;
        double totalShare = 0.0;
        Map<String,Double> result = new HashMap();
        ArrayList<BillLine> billLinesWithoutShare = new ArrayList<BillLine>(); //a list of users that have no share.

        //we calculate the total paid sum and the total share.
        //the share of the users without share is (totalPaid-totalShare)/numOfUserWithoutShare

        for (BillLine billLine : billLines) {
            totalPaid += billLine.amountPaid;
            if (Double.isNaN(billLine.share)) {
                billLinesWithoutShare.add(billLine);
            } else {
                totalShare += billLine.share;
            }
        }
        //if there isn't amount money to pay the bill, there is a problem:
        if(totalPaid < totalShare){
            throw new NotEnoughMoneyToPayTheBillException();
        }
        //now we split the share evenly between all the users without a share:

        double totalPaidWithoutShares = totalPaid - totalShare;

        double splitEvenShare = 0.0;
        if (billLinesWithoutShare.size() > 0) {
            splitEvenShare = totalPaidWithoutShares / billLinesWithoutShare.size();
        }
        if (billLinesWithoutShare.size() == 0 && totalPaidWithoutShares > 0 + EPSILON) {
            throw new TotalPaidNotEqualsToTotalShareException();
        }

        return splitEvenShare;
    }

    public static class TotalPaidNotEqualsToTotalShareException extends Exception {}
    public static class NotEnoughMoneyToPayTheBillException extends Exception {}

    public static class BillLine {
        private final String userId;
        private double amountPaid;
        private double share;

        public BillLine(String userId, Double amountPaid, Double share) {
            this.userId = userId;
            this.amountPaid = amountPaid;
            this.share = share;
        }

        public void setAmountPaid(Double amountPaid) {
            this.amountPaid = amountPaid;
        }

        public String getUserId() {
            return userId;
        }
        public Double getAmountPaid() {
            return amountPaid;
        }
        public Double getShare() {
            return share;
        }

        public void setShare(Double share) {
            this.share = share;
        }
    }
}
