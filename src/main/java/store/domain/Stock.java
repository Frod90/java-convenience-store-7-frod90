package store.domain;

import static store.common.ErrorMessage.*;

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
		validateIncrementQuantity(promotionQuantity);
		this.promotionQuantity += promotionQuantity;
	}

	public void incrementGeneralQuantity(int generalQuantity) {
		validateIncrementQuantity(generalQuantity);
		this.generalQuantity += generalQuantity;
	}

	private void validateIncrementQuantity(int quantity) {
		if (quantity <= 0) {
			throw new IllegalStateException(INCREMENT_NOT_NATURAL_NUMBER.getMessage());
		}
	}

	public void deductQuantity(int quantity, LocalDateTime now) {
		if (hasActivePromotionProductAndExistPromotionQuantity(now)) {
			deductQuantityWithPromotion(quantity);
			return;
		}

		validateDeductQuantityWithoutPromotion(quantity);
		generalQuantity -= quantity;
	}

	private void deductQuantityWithPromotion(int quantity) {
		validateDeductQuantityWithPromotion(quantity);

		if (quantity > promotionQuantity) {
			generalQuantity -= (quantity - promotionQuantity);
			promotionQuantity = 0;
			return;
		}

		promotionQuantity -= quantity;
	}

	private void validateDeductQuantityWithPromotion(int quantity) {
		if (quantity > promotionQuantity + generalQuantity) {
			throw new IllegalArgumentException(OVER_FLOW_STOCK_QUANTITY.getMessage());
		}
	}

	private void validateDeductQuantityWithoutPromotion(int quantity) {
		if (quantity > generalQuantity) {
			throw new IllegalArgumentException(OVER_FLOW_STOCK_QUANTITY.getMessage());
		}
	}

	public PromotionResult calculatePromotion(int purchasedQuantity) {
		int availablePromotionQuantity = Math.min(promotionQuantity, purchasedQuantity);

		int freeQuantity = product.calculateFreeQuantity(availablePromotionQuantity);
		int restQuantity = product.calculateRestQuantity(availablePromotionQuantity);

		int extraQuantity = calculateExtraQuantity(purchasedQuantity, availablePromotionQuantity);
		int unApplicableQuantity = calculateUnApplicableQuantity(purchasedQuantity, restQuantity);

		return PromotionResult.of(freeQuantity, extraQuantity, unApplicableQuantity);
	}

	private int calculateExtraQuantity(int purchasedQuantity, int availablePromotionQuantity) {
		int extraQuantity = product.calculateExtraQuantity(availablePromotionQuantity);

		if (promotionQuantity >= purchasedQuantity + extraQuantity) {
			return extraQuantity;
		}
		return 0;
	}

	private int calculateUnApplicableQuantity(int purchasedQuantity, int restQuantity) {
		if (promotionQuantity > purchasedQuantity) {
			return 0;
		}
		return purchasedQuantity - promotionQuantity + restQuantity;
	}

	public boolean hasPromotionProduct() {
		return product.hasPromotion();
	}

	public boolean hasActivePromotionProductAndExistPromotionQuantity(LocalDateTime comparedDateTime) {
		return hasActivePromotionProduct(comparedDateTime) && promotionQuantity > 0;
	}

	public boolean hasActivePromotionProduct(LocalDateTime comparedDateTime) {
		return product.hasActivePromotion(comparedDateTime);
	}

	public void validateOverFlowPurchasedQuantity(int purchasedQuantity) {
		if (hasOverFlowPurchasedQuantity(purchasedQuantity)) {
			throw new IllegalArgumentException(OVER_FLOW_STOCK_QUANTITY.getMessage());
		}
	}

	private boolean hasOverFlowPurchasedQuantity(int purchasedQuantity) {
		return purchasedQuantity > promotionQuantity + generalQuantity;
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
