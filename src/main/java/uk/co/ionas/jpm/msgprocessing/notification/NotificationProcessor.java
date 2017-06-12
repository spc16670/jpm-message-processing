package uk.co.ionas.jpm.msgprocessing.notification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import uk.co.ionas.jpm.msgprocessing.operation.Adjustment;
import uk.co.ionas.jpm.msgprocessing.supplier.AdjustableSale;
import uk.co.ionas.jpm.msgprocessing.utils.Currency;

/*
 * Processing requirements
 *  All sales must be recorded
 *  All messages must be processed
 *  After every 10th message received your application should log a report detailing the number 
 * 		of sales of each product and their total value.
 *  After 50 messages your application should log that it is pausing, stop accepting new messages
 * 		and log a report of the adjustments that have been made to each sale type while the application was running.
 * 
 * Sales and Messages
 *  A sale has a product type field and a value – you should choose sensible types for these.
 *  Any number of different product types can be expected. There is no fixed set.
 *  A message notifying you of a sale could be one of the following types
 * o Message Type 1 – contains the details of 1 sale E.g apple at 10p
 * o Message Type 2 – contains the details of a sale and the number of occurrences of that sale. 
 * 						E.g 20 sales of apples at 10p each.
 * o Message Type 3 – contains the details of a sale and an adjustment operation to be applied to all stored sales of this product 
 * 						type. Operations can be add, subtract, or multiply 
 * 						e.g Add 20p apples would instruct your application to add 20p to each sale
 */

public class NotificationProcessor implements Notifyable {

	private final List<Adjustment> operations; 
	private final AtomicInteger counter = new AtomicInteger(0);
	private final Object printLock = new Object();
	private final long start = System.currentTimeMillis();
	private long end = 0;
	private boolean suppressed = false;
	
	public NotificationProcessor(List<Adjustment> operations) {
		this.operations = new ArrayList<Adjustment>(operations);
	}
	
	@Override
	public void onMessage(AdjustableSale sale, Map<Integer, List<AdjustableSale>> map) {
		int no = counter.incrementAndGet();
		StringBuilder sb = new StringBuilder();
		sb.append(sale);
		
		this.operations.stream()
			.filter(o -> o.applies(sale))
			.peek(o -> sb.append(o))
			.count();
		
		print(sb.toString());
		if (no % 10 == 0) {
			synchronized (printLock) {
				String divider = String.join("", Collections.nCopies(22, "-"));
				String header = String.format("-- Summary information after %dth message %s", no, divider);
				print(header);
				print(String.format("%10s %7s %12s %10s %10s %10s", "type", "count", "sum", "min", "avg", "max"));
				for (Integer key : map.keySet()) {
					List<AdjustableSale> messages = map.get(key);
					DoubleSummaryStatistics stats = messages.stream()
							.map(m -> m.getValue())
							.collect(Collectors.summarizingDouble(BigDecimal::doubleValue)); 
					long count = stats.getCount();
					String sum = Currency.format(stats.getSum());
					String min = Currency.format(stats.getMin());
					String avg = Currency.format(stats.getAverage());
					String max = Currency.format(stats.getMax());
					print(String.format("Product %2d %7d %12s %10s %10s %10s", key, count, sum, min, avg, max));
				}
			}
		}
	}

	@Override
	public void onFinish(Map<Integer, List<AdjustableSale>> map) {
		print("Processing stopping...");
		this.end = System.currentTimeMillis();
		print("Processing took " + (this.end - this.start) + "ms");
		for (Integer key : map.keySet()) {
			List<AdjustableSale> sales = map.get(key);
			sales.stream().forEach(s -> print(s.toString()));
		}
	}
	
	public boolean isSuppressed() {
		return suppressed;
	}

	public void setSuppressed(boolean suppress) {
		this.suppressed = suppress;
	}

	private void print(String msg) {
		if (!this.suppressed) System.out.println(msg);
	}
	
}
