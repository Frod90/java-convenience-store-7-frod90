package store.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Product {

	private final String name;
	private final int price;
	private final Promotion promotion;

	private Product(String name, int price, Promotion promotion) {
		this.name = name;
		this.price = price;
		this.promotion = promotion;
	}

	public static Product create(String name, int price, Promotion promotion) {
		return new Product(name, price, promotion);
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public Promotion getPromotion() {
		return promotion;
	}

	public boolean hasPromotion() {
		return promotion.isPromotion();
	}

	public boolean hasActivePromotion(LocalDateTime comparedDateTime) {
		return promotion.isActive(comparedDateTime);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Product product = (Product)o;
		return Objects.equals(name, product.name);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}
}
