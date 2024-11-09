package store.domain;

import java.util.Objects;

public class Stock {

	private final Product product;
	private int promotionQuantity;
	private int generalQuantity;

	private Stock(Product product) {
		this.product = product;
	}

	public static Stock from(Product product) {
		return new Stock(product);
	}

	public void incrementPromotionCount(int promotionQuantity) {
		this.promotionQuantity += promotionQuantity;
	}

	public void incrementGeneralCount(int generalQuantity) {
		this.generalQuantity += generalQuantity;
	}

	public Product getProduct() {
		return product;
	}

	public int getPromotionQuantity() {
		return promotionQuantity;
	}

	public int getGeneralQuantity() {
		return generalQuantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Stock stock = (Stock)o;
		return Objects.equals(product, stock.product);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(product);
	}
}
