package store.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class StockTest {

	private static Stream<Arguments> providePromotionForCheckingHasPromotion() {
		return Stream.of(
			Arguments.of(
				Promotion.of("Frod Promotion", 1, 1,
					LocalDateTime.of(2001, 4, 21, 0, 0),
					LocalDateTime.of(2001, 5, 21, 0, 0)
				),
				true
			),
			Arguments.of(Promotion.getNoneInstance(), false)
		);
	}

	private static Stream<Arguments> provideDateTimeForCheckingActivePromotion() {
		return Stream.of(
			Arguments.of(LocalDateTime.of(2001, 4, 20, 23, 59, 59), false),
			Arguments.of(LocalDateTime.of(2001, 4, 21, 0, 0), true),
			Arguments.of(LocalDateTime.of(2001, 5, 20, 23, 59, 59), true),
			Arguments.of(LocalDateTime.of(2001, 5, 21, 0, 0, 0), false)
		);
	}

	@DisplayName("재고를 생성할 수 있다.")
	@Test
	void from() {
		// given
		Promotion promotion = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("제로 콜라", 2000, promotion);

		// when
		Stock stock = Stock.from(product);

		// then
		assertThat(stock)
			.extracting("product", "promotionQuantity", "generalQuantity")
			.containsExactly(product, 0, 0);
	}

	@DisplayName("프로모션 상품 수량을 증가시킬 수 있다.")
	@Test
	void incrementPromotionQuantity() {
		// given
		Promotion promotion = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("제로 콜라", 2000, promotion);
		Stock stock = Stock.from(product);

		// when
		stock.incrementPromotionQuantity(1);
		stock.incrementPromotionQuantity(2);
		stock.incrementPromotionQuantity(3);

		// then
		assertThat(stock.getPromotionQuantity()).isEqualTo(6);
	}

	@DisplayName("0이하의 수량으로 프로모션 수량을 증가시킬 수 없다.")
	@ValueSource(ints = {-1000, -1, 0})
	@ParameterizedTest(name = "{0}은 프로모션 수량 증가에 입력할 수 없습니다.")
	void incrementPromotionQuantityWithoutNaturalNumber(int incrementQuantity) {
		// given
		Promotion promotion = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("제로 콜라", 2000, promotion);
		Stock stock = Stock.from(product);

		// when //then
		assertThatThrownBy(() -> stock.incrementPromotionQuantity(incrementQuantity))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("[ERROR]");
	}

	@DisplayName("일반 상품 수량을 증가시킬 수 있다.")
	@Test
	void incrementGeneralQuantity() {
		// given
		Promotion promotion = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("제로 콜라", 2000, promotion);
		Stock stock = Stock.from(product);

		// when
		stock.incrementGeneralQuantity(2);
		stock.incrementGeneralQuantity(2);
		stock.incrementGeneralQuantity(2);

		// then
		assertThat(stock.getGeneralQuantity()).isEqualTo(6);
	}

	@DisplayName("0이하의 수량으로 일반 상품 수량을 증가시킬 수 없다.")
	@ValueSource(ints = {-999, -1, 0})
	@ParameterizedTest(name = "{0}은 일반 상품 수량 증가에 입력할 수 없습니다.")
	void incrementGeneralQuantityWithoutNaturalNumber(int incrementQuantity) {
		// given
		Promotion promotion = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("제로 콜라", 2000, promotion);
		Stock stock = Stock.from(product);

		// when //then
		assertThatThrownBy(() -> stock.incrementGeneralQuantity(incrementQuantity))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("[ERROR]");
	}

	@DisplayName("상품이 프로모션을 가지고 있는지 확인할 수 있다.")
	@MethodSource("providePromotionForCheckingHasPromotion")
	@ParameterizedTest
	void hasPromotionProduct(Promotion testPromotion, boolean expect) {

		// given
		Product product = Product.create("제로 콜라", 2000, testPromotion);
		Stock stock = Stock.from(product);

		// when
		boolean result = stock.hasPromotionProduct();

		// then
		assertThat(result).isEqualTo(expect);

	}

	@DisplayName("프로모션이 활성화된 상품을 가지고 있는지 확인할 수 있다.")
	@MethodSource("provideDateTimeForCheckingActivePromotion")
	@ParameterizedTest(name = "{0} 시각에 재고는 활성화된 상품을 가지고 있다는 것은 {1}이다")
	void hasActivePromotionProduct(LocalDateTime testDateTime, boolean expect) {

		// given
		Promotion promotion = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("제로 콜라", 2000, promotion);
		Stock stock = Stock.from(product);

		// when
		boolean result = stock.hasActivePromotionProduct(testDateTime);

		// then
		assertThat(result).isEqualTo(expect);
	}

	@DisplayName("재고 수량을 차감할 수 있다.")
	@ParameterizedTest(name = "{0} 시각에 {1}개를 구매하면 프로모션 재고는 {2}개가 남고, 일반 재고는 {3}개가 남는다.")
	@CsvSource({
		"2001-04-25T00:00:00, 5, 5, 10",
		"2001-04-25T00:00:00, 15, 0, 5",
		"2001-06-01T00:00:00, 5, 10, 5"
	})
	void deductQuantity(
		LocalDateTime now, int quantity,
		int expectedPromotionQuantity, int expectedGeneralQuantity
	) {
		// given
		Promotion promotion = Promotion.of("Food Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("제로 콜라", 2000, promotion);
		Stock stock = Stock.from(product);
		stock.incrementPromotionQuantity(10);
		stock.incrementGeneralQuantity(10);

		// when
		stock.deductQuantity(quantity, now);

		// then
		assertAll(
			() -> assertThat(stock.getPromotionQuantity()).isEqualTo(expectedPromotionQuantity),
			() -> assertThat(stock.getGeneralQuantity()).isEqualTo(expectedGeneralQuantity)
		);
	}

	@DisplayName("재고를 초과하여 차감하려고 할 때 예외가 발생한다.")
	@ParameterizedTest(name = "{0}시각에 {1}개를 구매할 수 없다.")
	@CsvSource({
		"2001-04-25T00:00:00, 25",
		"2001-06-01T00:00:00, 15"
	})
	void deductQuantityWithOverFlow(LocalDateTime now, int quantity) {
		// given
		Promotion promotion = Promotion.of("Food Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("제로 콜라", 2000, promotion);
		Stock stock = Stock.from(product);
		stock.incrementPromotionQuantity(10);
		stock.incrementGeneralQuantity(10);

		// when & then
		assertThatThrownBy(() -> stock.deductQuantity(quantity, now))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("[ERROR]");
	}

	@DisplayName("프로모션 결과를 계산할 수 있다.")
	@ParameterizedTest
	@CsvSource({
		"5, 2001-04-25T00:00:00, 2, 1, 0",
		"15, 2001-04-25T00:00:00, 7, 1, 0"
	})
	void calculatePromotion(int purchasedQuantity, LocalDateTime openDateTime,
		int expectedFreeQuantity, int expectedExtraQuantity, int expectedUnApplicableQuantity) {

		// given
		Promotion promotion = Promotion.of("Food Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("제로 콜라", 2000, promotion);
		Stock stock = Stock.from(product);
		stock.incrementPromotionQuantity(20);
		stock.incrementGeneralQuantity(20);

		// when
		PromotionResult result = stock.calculatePromotion(purchasedQuantity, openDateTime);

		// then
		assertThat(result)
			.extracting("freeQuantity", "extraQuantity", "unApplicableQuantity")
			.containsExactly(expectedFreeQuantity, expectedExtraQuantity, expectedUnApplicableQuantity);
	}

	@DisplayName("프로모션이 활성화되어 있지 않으면 프로모션을 계산할 수 없다.")
	@Test
	void calculatePromotionNotInTime() {

		// given
		Promotion promotion = Promotion.of("Food Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("제로 콜라", 2000, promotion);
		Stock stock = Stock.from(product);
		stock.incrementPromotionQuantity(20);
		stock.incrementGeneralQuantity(20);

		LocalDateTime openDateTime = LocalDateTime.of(2001, 5, 21, 0, 0);

		// when // then
		assertThatThrownBy(() -> stock.calculatePromotion(5, openDateTime))
			.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("[ERROR]");
	}

	@DisplayName("프로모션 적용이 가능한지 확인할 수 있다.")
	@CsvSource({
		"2001-04-25T00:00:00, 10, true",
		"2001-05-21T00:00:00, 10, false"
	})
	@ParameterizedTest
	void hasActivePromotionProductAndExistPromotionQuantity(LocalDateTime openDateTime, int incrementQuantity, boolean expect) {

		// given
		Promotion promotion = Promotion.of("Food Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("제로 콜라", 2000, promotion);
		Stock stock = Stock.from(product);
		stock.incrementPromotionQuantity(incrementQuantity);

		// when
		boolean result = stock.hasActivePromotionProductAndExistPromotionQuantity(openDateTime);

		// then
		assertThat(result).isEqualTo(expect);
	}

	@DisplayName("구매 수량이 총 재고 수량을 초과할 수 없다.")
	@Test
	void validateOverFlowPurchasedQuantity() {
		// given
		Promotion promotion = Promotion.of("Food Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("제로 콜라", 2000, promotion);
		Stock stock = Stock.from(product);
		stock.incrementPromotionQuantity(5);
		stock.incrementGeneralQuantity(7);

		// when // then
		assertThatThrownBy(() -> stock.validateOverFlowPurchasedQuantity(20))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("[ERROR]");

	}
}