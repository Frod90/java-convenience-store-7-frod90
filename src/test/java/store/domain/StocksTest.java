package store.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StocksTest {

	@DisplayName("재고 목록을 생성할 수 있다.")
	@Test
	void from() {

		// given
		Promotion promotion01 = Promotion.of("Frod Promotion", 1, 1, LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0));
		Promotion promotion02 = Promotion.of("Roy Promotion", 1, 1, LocalDateTime.of(2001, 5, 11, 0, 0),
			LocalDateTime.of(2001, 6, 11, 0, 0));
		Promotion promotion03 = Promotion.of("Hana Promotion", 1, 1, LocalDateTime.of(2001, 7, 1, 0, 0),
			LocalDateTime.of(2001, 8, 1, 0, 0));

		List<Product> products = List.of(Product.create("제로 콜라", 2000, promotion01),
			Product.create("아메리카노", 3000, promotion02), Product.create("녹차라떼", 4000, promotion03));

		Map<String, Stock> inputStocks = products.stream()
			.collect(
				Collectors.toMap(Product::getName, Stock::from, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		// when
		Stocks stocks = Stocks.from(inputStocks);

		// then
		assertThat(stocks.getStocks()).hasSize(3)
			.extractingFromEntries(Map.Entry::getValue)
			.containsExactly(inputStocks.get("제로 콜라"), inputStocks.get("아메리카노"), inputStocks.get("녹차라떼"));
	}

	@DisplayName("제품을 조회할 수 있다.")
	@Test
	void findAvailableProductBy() {

		// given
		Promotion promotion = Promotion.of("2+1 프로모션", 2, 1, LocalDateTime.of(2022, 1, 1, 0, 0),
			LocalDateTime.of(2022, 12, 31, 23, 59));

		Product product01 = Product.create("제로 콜라", 2000, promotion);
		Product product02 = Product.create("오렌지 주스", 1500, Promotion.getNoneInstance());

		Stock stock01 = Stock.from(product01);
		Stock stock02 = Stock.from(product02);

		stock01.incrementPromotionQuantity(10);
		stock01.incrementGeneralQuantity(10);
		stock02.incrementGeneralQuantity(10);

		Map<String, Stock> stockMap = Map.of(
			stock01.getProduct().getName(), stock01,
			stock02.getProduct().getName(), stock02
		);

		Stocks stocks = Stocks.from(stockMap);

		// when
		Product foundProduct = stocks.findAvailableProductBy("제로 콜라", 5);

		// then
		assertThat(foundProduct.getName()).isEqualTo("제로 콜라");
	}

	@Test
	@DisplayName("없는 제품을 조회할 수 없다.")
	void findAvailableProductByWithoutProduct() {

		// given
		Promotion promotion = Promotion.of("2+1 프로모션", 2, 1,
			LocalDateTime.of(2022, 1, 1, 0, 0),
			LocalDateTime.of(2022, 12, 31, 23, 59)
		);

		Product product01 = Product.create("제로 콜라", 2000, promotion);
		Product product02 = Product.create("오렌지 주스", 1500, Promotion.getNoneInstance());

		Stock stock01 = Stock.from(product01);
		Stock stock02 = Stock.from(product02);

		stock01.incrementPromotionQuantity(10);
		stock01.incrementGeneralQuantity(10);
		stock02.incrementGeneralQuantity(10);

		Map<String, Stock> stockMap = Map.of(
			stock01.getProduct().getName(), stock01,
			stock02.getProduct().getName(), stock02
		);

		Stocks stocks = Stocks.from(stockMap);

		// when // then
		assertThatThrownBy(() -> stocks.findAvailableProductBy("null", 5)).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("[ERROR]");
	}

	@Test
	@DisplayName("재고가 부족한 상품은 조회할 수 없다.")
	void findAvailableProductByWithLackQuantity() {

		// given
		Promotion promotion = Promotion.of("2+1 프로모션", 2, 1,
			LocalDateTime.of(2022, 1, 1, 0, 0),
			LocalDateTime.of(2022, 12, 31, 23, 59)
		);

		Product product01 = Product.create("제로 콜라", 2000, promotion);
		Product product02 = Product.create("오렌지 주스", 1500, Promotion.getNoneInstance());

		Stock stock01 = Stock.from(product01);
		Stock stock02 = Stock.from(product02);

		Map<String, Stock> stockMap = Map.of(
			stock01.getProduct().getName(), stock02,
			stock02.getProduct().getName(), stock02
		);

		Stocks stocks = Stocks.from(stockMap);

		// when // then
		assertThatThrownBy(() -> stocks.findAvailableProductBy("제로 콜라", 5))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("[ERROR]");
	}

	@DisplayName("프로모션 결과를 계산할 수 있다.")
	@CsvSource({
		"5, 1, 1, 0",
		"15, 3, 0, 6"
	})
	@ParameterizedTest
	void calculatePromotion(int purchasedQuantity, int expectedFreeQuantity, int expectedExtraQuantity,
		int expectedUnApplicableQuantity) {

		// given
		Promotion promotion = Promotion.of("2+1 프로모션", 2, 1,
			LocalDateTime.of(2022, 1, 1, 0, 0),
			LocalDateTime.of(2022, 12, 31, 23, 59)
		);

		Product product01 = Product.create("제로 콜라", 2000, promotion);
		Product product02 = Product.create("오렌지 주스", 1500, Promotion.getNoneInstance());

		Stock stock01 = Stock.from(product01);
		Stock stock02 = Stock.from(product02);

		stock01.incrementPromotionQuantity(10);
		stock01.incrementGeneralQuantity(10);
		stock02.incrementGeneralQuantity(10);

		Map<String, Stock> stockMap = Map.of(
			stock01.getProduct().getName(), stock01,
			stock02.getProduct().getName(), stock02
		);

		Stocks stocks = Stocks.from(stockMap);
		LocalDateTime openDateTime = LocalDateTime.of(2022, 6, 1, 0, 0);

		// when
		PromotionResult promotionResult = stocks.calculatePromotion("제로 콜라", purchasedQuantity, openDateTime);

		// then
		assertThat(promotionResult)
			.extracting("freeQuantity", "extraQuantity", "unApplicableQuantity")
			.containsExactly(expectedFreeQuantity, expectedExtraQuantity, expectedUnApplicableQuantity);
	}

	@Test
	@DisplayName("프로모션이 활성화 되어 있지 않으면 기본 프로모션 결과를 얻는다")
	void calculatePromotionWithoutActive() {

		// given
		Promotion promotion = Promotion.of("2+1 프로모션", 2, 1,
			LocalDateTime.of(2022, 1, 1, 0, 0),
			LocalDateTime.of(2022, 12, 31, 23, 59)
		);

		Product productWithPromotion = Product.create("제로 콜라", 2000, promotion);
		Product productWithoutPromotion = Product.create("오렌지 주스", 1500, Promotion.getNoneInstance());

		Stock stockWithPromotion = Stock.from(productWithPromotion);
		Stock stockWithoutPromotion = Stock.from(productWithoutPromotion);

		stockWithPromotion.incrementPromotionQuantity(10);
		stockWithPromotion.incrementGeneralQuantity(10);
		stockWithoutPromotion.incrementGeneralQuantity(10);

		Map<String, Stock> stockMap = Map.of(stockWithPromotion.getProduct().getName(), stockWithoutPromotion,
			stockWithoutPromotion.getProduct().getName(), stockWithoutPromotion);

		Stocks stocks = Stocks.from(stockMap);
		LocalDateTime openDateTime = LocalDateTime.of(2022, 6, 1, 0, 0);

		// when
		PromotionResult promotionResult = stocks.calculatePromotion("오렌지 주스", 5, openDateTime);

		assertThat(promotionResult).extracting("freeQuantity", "extraQuantity", "unApplicableQuantity")
			.containsExactly(0, 0, 0);
	}

	@Test
	@DisplayName("재고를 차감할 수 있다.")
	void deductQuantity() {

		// given
		Promotion promotion = Promotion.of("2+1 프로모션", 2, 1, LocalDateTime.of(2022, 1, 1, 0, 0),
			LocalDateTime.of(2022, 12, 31, 23, 59));

		Product product01 = Product.create("제로 콜라", 2000, promotion);
		Product product02 = Product.create("오렌지 주스", 1500, Promotion.getNoneInstance());

		Stock stock01 = Stock.from(product01);
		Stock stock02 = Stock.from(product02);

		stock01.incrementPromotionQuantity(10);
		stock01.incrementGeneralQuantity(10);
		stock02.incrementGeneralQuantity(10);

		Map<String, Stock> stockMap = Map.of(
			stock01.getProduct().getName(), stock01,
			stock02.getProduct().getName(), stock02
		);

		Stocks stocks = Stocks.from(stockMap);

		Receipts receipts = Receipts.from(
			List.of(
				Receipt.of(stock01.getProduct(), 5, 2),  // 5개 구매, 2개 프로모션 적용
				Receipt.of(stock02.getProduct(), 3, 0)  // 3개 구매, 프로모션 없음
			),
			MemberShip.of(false)
		);

		LocalDateTime now = LocalDateTime.of(2022, 6, 1, 0, 0);
		stocks.deductQuantity(receipts, now);

		assertAll(
			() -> assertThat(stock01.getPromotionQuantity()).isEqualTo(5),
			() -> assertThat(stock01.getGeneralQuantity()).isEqualTo(10),
			() -> assertThat(stock02.getGeneralQuantity()).isEqualTo(7)
		);
	}
}