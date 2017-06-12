package uk.co.ionas.jpm.msgprocessing.supplier;

import java.math.BigDecimal;

import org.junit.Assert;

import uk.co.ionas.jpm.msgprocessing.utils.Currency;


/**
 * 
 * @author szymon.czaja
 *
 */
public abstract class Sale {

	private final Product product;
	private BigDecimal value;
	
	public Sale(Product product, BigDecimal value) {
		Assert.assertNotNull(product);
		Assert.assertNotNull(value);
		this.product = product;
		this.value = value;
	}

	public Product getProduct() {
		return product;
	}
	
	public BigDecimal getValue() {
		return value;
	}

	void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("%-8s %d - %-7s", "Product Type", product.getType(), Currency.format(value));
	}
	
}
