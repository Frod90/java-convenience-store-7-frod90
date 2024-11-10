package store.domain;

public class Receipt {

	private final Product product;
	private final int totalQuantity;
	private final int freeQuantity;

	private Receipt(Product product, int totalQuantity, int freeQuantity) {
		this.product = product;
		this.totalQuantity = totalQuantity;
		this.freeQuantity = freeQuantity;
	}

	public static Receipt of(Product product, int totalQuantity, int freeQuantity) {
		return new Receipt(product, totalQuantity, freeQuantity);
	}

	public long calculatePurchasedPrice() {
		return (long)product.getPrice() * totalQuantity;
	}

	public long calculatePromotionDiscountPrice() {
		return (long)product.getPrice() * freeQuantity;
	}

	public long calculateNetPrice() {
		if (freeQuantity == 0) {
			return calculatePurchasedPrice();
		}

		return 0;
	}

	public Product getProduct() {
		return product;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public int getFreeQuantity() {
		return freeQuantity;
	}
}
