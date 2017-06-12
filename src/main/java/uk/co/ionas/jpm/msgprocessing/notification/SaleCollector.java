package uk.co.ionas.jpm.msgprocessing.notification;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import uk.co.ionas.jpm.msgprocessing.supplier.AdjustableSale;

public class SaleCollector implements Collector<
		AdjustableSale
		, Map<Integer, List<AdjustableSale>>
		, Map<Integer, List<AdjustableSale>>> 
	{

	private final Notifyable notificator;
	
	private SaleCollector(Notifyable notificator) {
		this.notificator = notificator;
	}
	
	public static SaleCollector make(Notifyable notificator) {
		return new SaleCollector(notificator);
	}
	
	@Override
	public Supplier<Map<Integer, List<AdjustableSale>>> supplier() {
		return ConcurrentHashMap::new;
	}
	
	@Override
	public BiConsumer<Map<Integer, List<AdjustableSale>>, AdjustableSale> accumulator() {
		return (map, message) -> {
			Integer type = message.getProduct().getType();
			List<AdjustableSale> messages = null;
			if (!map.containsKey(type)) {
				messages = new ArrayList<AdjustableSale>();
				map.put(type, messages);
			} else {
				messages = map.get(type);
			}
			messages.add(message);
			this.notificator.onMessage(message, map);
		};
	}

	@Override
	public Set<java.util.stream.Collector.Characteristics> characteristics() {
		return EnumSet.of(Characteristics.CONCURRENT);
	}

	@Override
	public BinaryOperator<Map<Integer, List<AdjustableSale>>> combiner() {
		return (map1, map2) -> {
			map2.forEach((k, v) -> {
				if (map1.containsKey(k)) {
					map1.get(k).addAll(v);
				} else {
					map1.put(k, v);
				}
			});
			return map1;
		};
	}

	@Override
	public Function<Map<Integer, List<AdjustableSale>>, Map<Integer, List<AdjustableSale>>> finisher() {
		return (map) -> {
			this.notificator.onFinish(map);
			return map;
		};
	}

}
