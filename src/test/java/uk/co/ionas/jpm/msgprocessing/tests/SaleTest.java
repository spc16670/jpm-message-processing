package uk.co.ionas.jpm.msgprocessing.tests;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.co.ionas.jpm.msgprocessing.notification.Notifyable;
import uk.co.ionas.jpm.msgprocessing.notification.SaleCollector;
import uk.co.ionas.jpm.msgprocessing.supplier.AdjustableSale;
import uk.co.ionas.jpm.msgprocessing.supplier.Sale;
import uk.co.ionas.jpm.msgprocessing.supplier.SaleSource;

public class SaleTest extends TestBase {

	static SaleSource supplier;
	
	@BeforeClass
	public static void init() {
		supplier = SaleSource.getInstance();
	}
	
	@Test
	public void testAllProcessed() {
		int limit = 50;
		Notifyable notificator = getNotificator(Arrays.asList());
		Stream<Sale> stream = Stream.generate(supplier);
		Map<Integer, List<AdjustableSale>> results = stream
			.limit(limit)
			.map(s -> new AdjustableSale(s))
			.collect(SaleCollector.make(notificator));
		Set<Integer> keys = results.keySet();
		long count = keys.stream()
				.map(i -> results.get(i))
				.flatMap(l -> l.stream())
				.count();
		Assert.assertTrue(limit == count);
	}
	
	
	@Test
	public void testAllAdjusted() throws InterruptedException {
		Notifyable notificator = getNotificator(Arrays.asList(
				getAddAdjustment(0.5)
				, getMultiplyAdjustment(0.5)
				, getSubtractAdjustment(0.2)
				));
		Stream<Sale> stream = Stream.generate(supplier);
		Map<Integer, List<AdjustableSale>> results = stream
			.limit(50)
			.map(s -> new AdjustableSale(s))
			.collect(SaleCollector.make(notificator));
		Set<Integer> keys = results.keySet();
		long count = keys.stream()
				.map(i -> results.get(i))
				.flatMap(l -> l.stream())
				.filter(s -> s.getAdjustment() == null)
				.count();
		Assert.assertTrue(count == 0);
	}
	
	
	@AfterClass
	public static void end() throws InterruptedException {
		supplier.close();
	}
}
