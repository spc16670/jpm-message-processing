package uk.co.ionas.jpm.msgprocessing.operation;

import java.math.BigDecimal;

import uk.co.ionas.jpm.msgprocessing.supplier.AdjustableSale;

public class MultiplyAdjustment extends Adjustment {
	
	public MultiplyAdjustment(BigDecimal val) {
		super("Multiply", val);
	}

	@Override
	public boolean applies(AdjustableSale sale) {
		if (sale.getProduct().getType() == 3) {
			sale.adjust(this);
			return true;
		}
		return false;
	}

	@Override
	public BigDecimal calculate(BigDecimal val) {
		return val.multiply(this.getVal());
	}

	@Override
	public String toString() {
		return super.getName() + " (x" + super.getVal() + ") ";
	}
}
