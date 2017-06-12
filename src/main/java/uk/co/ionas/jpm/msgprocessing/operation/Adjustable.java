package uk.co.ionas.jpm.msgprocessing.operation;

import uk.co.ionas.jpm.msgprocessing.supplier.AdjustableSale;

@FunctionalInterface
public interface Adjustable {

	/**
	 * @param sale
	 * @return flag to indicate whether adjustment applies
	 */
	public abstract boolean applies(AdjustableSale sale);
	
}
