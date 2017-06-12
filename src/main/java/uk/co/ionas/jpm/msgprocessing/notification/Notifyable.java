package uk.co.ionas.jpm.msgprocessing.notification;

import java.util.List;
import java.util.Map;

import uk.co.ionas.jpm.msgprocessing.supplier.AdjustableSale;

public interface Notifyable {

	public void onMessage(AdjustableSale sale, Map<Integer, List<AdjustableSale>> acc);
	
	public void onFinish(Map<Integer, List<AdjustableSale>> acc);
}
