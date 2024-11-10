package store.io.input.provider;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.Test;

import store.domain.Product;
import store.domain.Promotion;
import store.domain.Promotions;
import store.domain.Stocks;

class StockProviderTest {

	@Test
	void provide() {

		// given
		StockProvider stockProvider = new StockProvider();

		Promotion promotion01 = Promotion.of("frod promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);
		Promotion promotion02 = Promotion.of("roy promotion", 2, 1,
			LocalDateTime.of(2001, 5, 11, 0, 0),
			LocalDateTime.of(2001, 6, 11, 0, 0)
		);
		Promotion promotion03 = Promotion.of("hana promotion", 2, 2,
			LocalDateTime.of(2001, 7, 1, 0, 0),
			LocalDateTime.of(2001, 8, 1, 0, 0)
		);

		Map<String, Promotion> inputPromotions = Map.of(
			promotion01.getName(), promotion01,
			promotion02.getName(), promotion02,
			promotion03.getName(), promotion03
		);

		Promotions promotions = Promotions.from(inputPromotions);
		String testProductPath = "src/test/resources/test_products.md";

		// when
		Stocks stocks = stockProvider.provide(promotions, testProductPath);

		// then
		assertThat(stocks.getStocks())
			.hasSize(4)
			.extractingFromEntries(Map.Entry::getValue)
			.extracting("product", "promotionQuantity", "generalQuantity")
			.containsExactly(
				tuple(Product.create("제로콜라", 2000, promotion01), 10, 20),
				tuple(Product.create("아메리카노", 3000, promotion02), 16, 7),
				tuple(Product.create("녹차라떼", 4500, promotion03), 9, 0),
				tuple(Product.create("물", 2000, Promotion.getNoneInstance()), 0, 10)
			);

	}
}