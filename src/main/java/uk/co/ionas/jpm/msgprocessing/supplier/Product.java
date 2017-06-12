package uk.co.ionas.jpm.msgprocessing.supplier;

import java.util.Random;

public abstract class Product {
	
	private final int type;
	
	public Product() {
		type = new Random().nextInt(3) + 1;
	}

	public Product(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}

}
