package store.domain;

import java.time.LocalDateTime;
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

	public void incrementPromotionQuantity(int promotionQuantity) {
		this.promotionQuantity += promotionQuantity;
	}

	public void incrementGeneralQuantity(int generalQuantity) {
		this.generalQuantity += generalQuantity;
	}

	public boolean hasPromotionProduct() {
		return product.hasPromotion();
	}

	public boolean hasActivePromotionProduct(LocalDateTime comparedDateTime) {
		return product.hasActivePromotion(comparedDateTime);
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
