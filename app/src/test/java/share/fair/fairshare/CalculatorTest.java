package share.fair.fairshare;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import share.fair.fairshare.models.Calculator;
import share.fair.fairshare.models.User;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by niryo on 04/10/2017.
 */

public class CalculatorTest {
    @Test
    public void noShares() throws Calculator.TotalPaidNotEqualsToTotalShareException, Calculator.NotEnoughMoneyToPayTheBillException {
        Calculator.BillLine billLine1 = new Calculator.BillLine("user1", 100.0, Double.NaN);
        Calculator.BillLine billLine2 = new Calculator.BillLine("user2", 0.0,  Double.NaN);
        Calculator.BillLine billLine3 = new Calculator.BillLine("user3", 0.0,  Double.NaN);
        List<Calculator.BillLine> billLines = new ArrayList<>();
        billLines.add(billLine1);
        billLines.add(billLine2);
        billLines.add(billLine3);
        assertEquals(Calculator.CalculateShares(billLines), 33.333, 0.001);
    }

    @Test
    public void noShares2() throws Calculator.TotalPaidNotEqualsToTotalShareException, Calculator.NotEnoughMoneyToPayTheBillException {
        Calculator.BillLine billLine1 = new Calculator.BillLine("user1", 100.0,  Double.NaN);
        Calculator.BillLine billLine2 = new Calculator.BillLine("user2", 25.0,  Double.NaN);
        Calculator.BillLine billLine3 = new Calculator.BillLine("user3", 25.0,  Double.NaN);
        List<Calculator.BillLine> billLines = new ArrayList<>();
        billLines.add(billLine1);
        billLines.add(billLine2);
        billLines.add(billLine3);
        assertEquals(Calculator.CalculateShares(billLines), 50.0, 0.001);
    }

    @Test
    public void withShare() throws Calculator.TotalPaidNotEqualsToTotalShareException, Calculator.NotEnoughMoneyToPayTheBillException {
        Calculator.BillLine billLine1 = new Calculator.BillLine("user1", 100.0, 50.0);
        Calculator.BillLine billLine2 = new Calculator.BillLine("user2", 0.0,  Double.NaN);
        Calculator.BillLine billLine3 = new Calculator.BillLine("user3", 0.0,  Double.NaN);
        List<Calculator.BillLine> billLines = new ArrayList<>();
        billLines.add(billLine1);
        billLines.add(billLine2);
        billLines.add(billLine3);
        assertEquals(Calculator.CalculateShares(billLines), 25.0, 0.001);
    }

    @Test
    public void withShare2() throws Calculator.TotalPaidNotEqualsToTotalShareException, Calculator.NotEnoughMoneyToPayTheBillException {
        Calculator.BillLine billLine1 = new Calculator.BillLine("user1", 0.0, 50.0);
        Calculator.BillLine billLine2 = new Calculator.BillLine("user2", 100.0,  Double.NaN);
        Calculator.BillLine billLine3 = new Calculator.BillLine("user3", 0.0,  Double.NaN);
        List<Calculator.BillLine> billLines = new ArrayList<>();
        billLines.add(billLine1);
        billLines.add(billLine2);
        billLines.add(billLine3);

        assertEquals(Calculator.CalculateShares(billLines), 25.0, 0.001);
    }

    @Test(expected = Calculator.TotalPaidNotEqualsToTotalShareException.class)
    public void totalPaidGreaterThanTotalShare() throws Calculator.TotalPaidNotEqualsToTotalShareException, Calculator.NotEnoughMoneyToPayTheBillException {
        Calculator.BillLine billLine1 = new Calculator.BillLine("user1", 100.0, 50.0);
        Calculator.BillLine billLine2 = new Calculator.BillLine("user2", 100.0, 50.0);
        Calculator.BillLine billLine3 = new Calculator.BillLine("user3", 100.0, 50.0);
        List<Calculator.BillLine> billLines = new ArrayList<>();
        billLines.add(billLine1);
        billLines.add(billLine2);
        billLines.add(billLine3);
        Calculator.CalculateShares(billLines);
    }

    @Test(expected = Calculator.NotEnoughMoneyToPayTheBillException.class)
    public void notEnoughMoneyToPayTheBill() throws Calculator.TotalPaidNotEqualsToTotalShareException, Calculator.NotEnoughMoneyToPayTheBillException {
        Calculator.BillLine billLine1 = new Calculator.BillLine("user1", 0.0, 50.0);
        Calculator.BillLine billLine2 = new Calculator.BillLine("user2", 0.0, 50.0);
        Calculator.BillLine billLine3 = new Calculator.BillLine("user3", 0.0, 50.0);
        List<Calculator.BillLine> billLines = new ArrayList<>();
        billLines.add(billLine1);
        billLines.add(billLine2);
        billLines.add(billLine3);
        Calculator.CalculateShares(billLines);
    }
}
