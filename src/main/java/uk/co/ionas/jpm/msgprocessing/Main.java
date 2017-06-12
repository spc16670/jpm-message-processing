package uk.co.ionas.jpm.msgprocessing;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import uk.co.ionas.jpm.msgprocessing.notification.NotificationProcessor;
import uk.co.ionas.jpm.msgprocessing.notification.Notifyable;
import uk.co.ionas.jpm.msgprocessing.notification.SaleCollector;
import uk.co.ionas.jpm.msgprocessing.operation.AddAdjustment;
import uk.co.ionas.jpm.msgprocessing.operation.MultiplyAdjustment;
import uk.co.ionas.jpm.msgprocessing.operation.SubtractAdjustment;
import uk.co.ionas.jpm.msgprocessing.supplier.AdjustableSale;
import uk.co.ionas.jpm.msgprocessing.supplier.Sale;
import uk.co.ionas.jpm.msgprocessing.supplier.SaleSource;

public class Main {

	public static final int MESSAGE_LIMIT = 50;
	
	public static void main(String[] args) throws InterruptedException {

		Notifyable notificator = new NotificationProcessor(Arrays.asList(
				new AddAdjustment(BigDecimal.valueOf(0.20))
				, new SubtractAdjustment(BigDecimal.valueOf(0.30))
				, new MultiplyAdjustment(BigDecimal.valueOf(0.50))
			));
		
		try (SaleSource supplier = SaleSource.getInstance()) {
			Stream<Sale> stream = Stream.generate(supplier);
			stream
				.limit(MESSAGE_LIMIT)
				.map(s -> new AdjustableSale(s))
				.collect(SaleCollector.make(notificator));
		}

	}
	
}
