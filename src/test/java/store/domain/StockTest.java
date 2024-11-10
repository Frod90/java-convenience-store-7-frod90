package store.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class StockTest {

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

	private static Stream<Arguments> provideDateTimeForCheckingActivePromotion() {
		return Stream.of(
			Arguments.of(LocalDateTime.of(2001, 4, 20, 23, 59, 59), false),
			Arguments.of(LocalDateTime.of(2001, 4, 21, 0, 0), true),
			Arguments.of(LocalDateTime.of(2001, 5, 20, 23, 59, 59), true),
			Arguments.of(LocalDateTime.of(2001, 5, 21, 0, 0, 0), false)
		);
	}

}