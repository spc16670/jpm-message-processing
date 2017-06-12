package uk.co.ionas.jpm.msgprocessing.operation;

import java.math.BigDecimal;

public abstract class Adjustment implements Adjustable {

	private final String name;
	private final BigDecimal val;
	
	public Adjustment(String name, BigDecimal val) {
		this.name = name;
		this.val = val;
	}
	
	/**
	 * Calculates adjusted price
	 * @param originalPrice
	 * @return adjusted price
	 */
	public abstract BigDecimal calculate(BigDecimal originalPrice);
	
	
	public String getName() {
		return name;
	}

	public BigDecimal getVal() {
		return val;
	}
}
