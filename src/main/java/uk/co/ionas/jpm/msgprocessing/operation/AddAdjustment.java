package uk.co.ionas.jpm.msgprocessing.operation;

import java.math.BigDecimal;

import uk.co.ionas.jpm.msgprocessing.supplier.AdjustableSale;
import uk.co.ionas.jpm.msgprocessing.utils.Currency;

public class AddAdjustment extends Adjustment {
	
	public AddAdjustment(BigDecimal val) {
		super("Add", val);
	}

	@Override
	public boolean applies(AdjustableSale sale) {
		if (sale.getProduct().getType() == 1) {
			sale.adjust(this);
			return true;
		}
		return false;
	}

	@Override
	public BigDecimal calculate(BigDecimal val) {
		return val.add(this.getVal());
	}

	@Override
	public String toString() {
		return super.getName() + " (+" + Currency.format(super.getVal()) + "p) ";
	}
}
