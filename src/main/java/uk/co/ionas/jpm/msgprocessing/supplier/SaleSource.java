package uk.co.ionas.jpm.msgprocessing.supplier;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class SaleSource implements Supplier<Sale>, AutoCloseable {
	
	private static final int SHUTDOWN_TIMEOUT_SEC = 3;
	private static final int Q_CAPACITY = 12;
	
	private static volatile SaleSource INSTANCE;
	
	private final AtomicBoolean goOn = new AtomicBoolean(true);
	
	private final BlockingQueue<Sale> queue;
	private final ExecutorService executor;
	
	
	/**
	 * 
	 */
	private SaleSource() {
		queue = new ArrayBlockingQueue<Sale>(Q_CAPACITY);
		executor = Executors.newSingleThreadExecutor();
	}
	
	
	/**
	 * Use Double‐Checked Locking to ensure thread safety
	 * @return
	 */
	public static SaleSource getInstance() {
		if (INSTANCE == null) {
			synchronized(SaleSource.class) {
				if (INSTANCE == null) {
					INSTANCE = new SaleSource();
					INSTANCE.produce();
				}	
			}
		}
		return INSTANCE;
	}
	
	
	/**
	 * 
	 */
	private void produce() {
		executor.submit(() -> {
			while (goOn.get()) {
				
				Product product = new Product() {};
				double p = 1 + (100 - 1) *  new Random().nextDouble();
				BigDecimal price = BigDecimal.valueOf(p);
				Sale msg = new Sale(product, price){};
				queue.offer(msg);
				
			}
		});
	}
	
	
	/**
	 * 
	 */
	@Override
	public Sale get() {
		Sale sale = null;
		try {
			 sale = queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return sale;
	}

	
	/**
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public void close() throws InterruptedException {
		goOn.set(false);
		executor.shutdown();
		executor.awaitTermination(SHUTDOWN_TIMEOUT_SEC, TimeUnit.SECONDS);
	}

}
