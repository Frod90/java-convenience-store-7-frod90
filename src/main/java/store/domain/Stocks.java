package store.domain;

import static store.common.ErrorMessage.*;

import java.time.LocalDateTime;
import java.util.Map;

public class Stocks {

	private final Map<String, Stock> stocks;

	private Stocks(Map<String, Stock> stocks) {
		this.stocks = stocks;
	}

	public static Stocks from(Map<String, Stock> stocks) {
		return new Stocks(stocks);
	}

	public Product findAvailableProductBy(String productName, int purchasedQuantity) {
		Stock stock = stocks.get(productName);

		validateExistProduct(productName);
		stock.validateOverFlowPurchasedQuantity(purchasedQuantity);

		return stock.getProduct();
	}

	private void validateExistProduct(String productName) {
		if (!stocks.containsKey(productName)) {
			throw new IllegalArgumentException(NOT_EXIST_PRODUCT.getMessage());
		}
	}

	public PromotionResult calculatePromotion(String productName, int purchasedQuantity,
		LocalDateTime comparedDateTime) {
		Stock stock = stocks.get(productName);

		if (stock.hasActivePromotionProductAndExistPromotionQuantity(comparedDateTime)) {
			return stock.calculatePromotion(purchasedQuantity);
		}

		return PromotionResult.of(0, 0, 0);
	}

	public void deductQuantity(Receipts receipts, LocalDateTime now) {

		for (Receipt receipt : receipts.getReceipts()) {
			Product product = receipt.getProduct();
			Stock stock = stocks.get(product.getName());
			stock.deductQuantity(receipt.getTotalQuantity(), now);
		}
	}

	public Map<String, Stock> getStocks() {
		return stocks;
	}
}
