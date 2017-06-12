package uk.co.ionas.jpm.msgprocessing.operation;

import java.math.BigDecimal;

import uk.co.ionas.jpm.msgprocessing.supplier.AdjustableSale;
import uk.co.ionas.jpm.msgprocessing.utils.Currency;

public class SubtractAdjustment extends Adjustment {
	
	public SubtractAdjustment(BigDecimal val) {
		super("Subtract", val);
	}

	@Override
	public BigDecimal calculate(BigDecimal val) {
		if (this.getVal().doubleValue() > val.doubleValue()) 
			throw new IllegalArgumentException( this.getVal() + " cannot be subtracted from " + val);
		return val.subtract(this.getVal());
	}
	
	@Override
	public boolean applies(AdjustableSale sale) {
		if (sale.getProduct().getType() == 2) {
			sale.adjust(this);
			return true;
		}
		return false;
	}


	@Override
	public String toString() {
		return super.getName() + " (-" + Currency.format(super.getVal()) + "p) ";
	}
}
