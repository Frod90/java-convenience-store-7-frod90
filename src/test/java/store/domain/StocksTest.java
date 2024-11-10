package store.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StocksTest {

	@DisplayName("재고 목록을 생성할 수 있다.")
	@Test
	void from() {

		// given
		Promotion promotion01 = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);
		Promotion promotion02 = Promotion.of("Roy Promotion", 1, 1,
			LocalDateTime.of(2001, 5, 11, 0, 0),
			LocalDateTime.of(2001, 6, 11, 0, 0)
		);
		Promotion promotion03 = Promotion.of("Hana Promotion", 1, 1,
			LocalDateTime.of(2001, 7, 1, 0, 0),
			LocalDateTime.of(2001, 8, 1, 0, 0)
		);

		List<Product> products = List.of(
			Product.create("제로 콜라", 2000, promotion01),
			Product.create("아메리카노", 3000, promotion02),
			Product.create("녹차라떼", 4000, promotion03)
		);

		Map<String, Stock> inputStocks = products.stream()
			.collect(Collectors.toMap(
				Product::getName,
				Stock::from,
				(oldValue, newValue) -> oldValue,
				LinkedHashMap::new
			));

		// when
		Stocks stocks = Stocks.from(inputStocks);

		// then
		assertThat(stocks.getStocks())
			.hasSize(3)
			.extractingFromEntries(Map.Entry::getValue)
			.containsExactly(
				inputStocks.get("제로 콜라"),
				inputStocks.get("아메리카노"),
				inputStocks.get("녹차라떼")
			);
	}
}