package uk.co.ionas.jpm.msgprocessing.supplier;

import java.math.BigDecimal;

import uk.co.ionas.jpm.msgprocessing.operation.Adjustment;
import uk.co.ionas.jpm.msgprocessing.utils.Currency;


public class AdjustableSale extends Sale {

	private Adjustment adjustment;
	private BigDecimal originalValue;

	public AdjustableSale(Sale sale) {
		this(sale.getProduct(), sale.getValue());
	}
	
	public AdjustableSale(Product product, BigDecimal value) {
		super(product, value);
		this.originalValue = value;
	}
	
	public Adjustment getAdjustment() {
		return adjustment;
	}

	public void adjust(Adjustment adjustment) {
		this.adjustment = adjustment;
		this.originalValue = this.getValue();
		this.setValue(this.adjustment.calculate(this.getValue()));
	}

	@Override
	public String toString() {
		if (this.adjustment == null) {
			return super.toString();
		} else {
			return super.toString() 
					+ "" + this.adjustment.toString() 
					+ " Price adjusted from " 
					+ Currency.format(this.originalValue)
					+ " to " 
					+ Currency.format(this.getValue());
		}
	}
	
	
}
