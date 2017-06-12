package uk.co.ionas.jpm.msgprocessing.tests;

import org.junit.Assert;
import org.junit.Test;

import uk.co.ionas.jpm.msgprocessing.operation.Adjustment;
import uk.co.ionas.jpm.msgprocessing.supplier.AdjustableSale;

public class OperationTest extends TestBase {

	
	@Test
	public void testAdd() {
		AdjustableSale sale = getAdjustableSale(1, 1);
		Adjustment adj = getAddAdjustment(1);
		Assert.assertTrue(adj.applies(sale));
		Assert.assertTrue(sale.getValue().doubleValue() == 2.0);
	}
	
	
	@Test
	public void testSubtract() {
		AdjustableSale sale = getAdjustableSale(2, 1);
		Adjustment adj = getSubtractAdjustment(0.5);
		Assert.assertTrue(adj.applies(sale));
		Assert.assertTrue(sale.getValue().doubleValue() == 0.5);
	}
	
	
	@Test
	public void testMultiply() {
		AdjustableSale sale = getAdjustableSale(3, 1);
		Adjustment adj = getMultiplyAdjustment(2);
		Assert.assertTrue(adj.applies(sale));
		Assert.assertTrue(sale.getValue().doubleValue() == 2.0);
	}
}
