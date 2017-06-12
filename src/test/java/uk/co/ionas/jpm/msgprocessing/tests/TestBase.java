package uk.co.ionas.jpm.msgprocessing.tests;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import uk.co.ionas.jpm.msgprocessing.notification.NotificationProcessor;
import uk.co.ionas.jpm.msgprocessing.notification.Notifyable;
import uk.co.ionas.jpm.msgprocessing.operation.AddAdjustment;
import uk.co.ionas.jpm.msgprocessing.operation.Adjustment;
import uk.co.ionas.jpm.msgprocessing.operation.MultiplyAdjustment;
import uk.co.ionas.jpm.msgprocessing.operation.SubtractAdjustment;
import uk.co.ionas.jpm.msgprocessing.supplier.AdjustableSale;
import uk.co.ionas.jpm.msgprocessing.supplier.Product;

public class TestBase {

	public static AdjustableSale getAdjustableSale(int type, double val) {
		Product product = new Product(type) {};
		BigDecimal price = BigDecimal.valueOf(val);
		return new AdjustableSale(product, price){};
	}
	
	public static AdjustableSale getAdjustableSale(double val) {
		Product product = new Product() {};
		BigDecimal price = BigDecimal.valueOf(val);
		return new AdjustableSale(product, price){};
	}
	
	public static AdjustableSale getAdjustableSale() {
		return getAdjustableSale(new Random().nextDouble() * 100);
	}

	public static Adjustment getAddAdjustment(double val) {
		return new AddAdjustment(new BigDecimal(val));
	}
	
	public static Adjustment getSubtractAdjustment(double val) {
		return new SubtractAdjustment(new BigDecimal(val));
	}
	
	public static Adjustment getMultiplyAdjustment(double val) {
		return new MultiplyAdjustment(new BigDecimal(val));
	}
	
	public static Notifyable getNotificator(List<Adjustment> operations) {
		NotificationProcessor notificator = new NotificationProcessor(operations);
		notificator.setSuppressed(true);
		return notificator;
	}
	
}
