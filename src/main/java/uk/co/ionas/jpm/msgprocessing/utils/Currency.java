package uk.co.ionas.jpm.msgprocessing.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class Currency {

	public static String format(BigDecimal val) {
		NumberFormat fmt = NumberFormat.getCurrencyInstance();
		return fmt.format(val);
	}
	
	public static String format(double val) {
		return format(new BigDecimal(val));
	}
	
}
