package store.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class ProductTest {

	private static Stream<Arguments> provideDateTimeForCheckingActivePromotion() {
		return Stream.of(
			Arguments.of(LocalDateTime.of(2001, 5, 10, 23, 59, 59), false),
			Arguments.of(LocalDateTime.of(2001, 5, 11, 0, 0, 0), true),
			Arguments.of(LocalDateTime.of(2001, 6, 10, 23, 59, 59), true),
			Arguments.of(LocalDateTime.of(2001, 6, 12, 0, 0, 0), false)
		);
	}

	@DisplayName("프로모션을 가진 상품을 생성할 수 있다.")
	@Test
	void createWithPromotion() {

		// given
		Promotions promotions = createPromotions();
		String productName = "펩시 제로콜라";
		int price = 2000;
		String promotionName = "Frod Promotion";
		Promotion promotion = promotions.findBy(promotionName);

		// when
		Product product = Product.create(productName, price, promotion);

		// then
		assertThat(product)
			.extracting("name", "price", "promotion")
			.containsExactly(productName, price, promotion);
	}

	@DisplayName("none 프로모션을 가진 상품을 생성할 수 있다.")
	@Test
	void createWithNonePromotion() {

		// given
		Promotions promotions = createPromotions();
		String productName = "아메리카노";
		int price = 1800;
		String promotionName = "Special Promotion";
		Promotion promotion = promotions.findBy(promotionName);

		// when
		Product product = Product.create(productName, price, promotion);

		// then
		assertThat(product)
			.extracting("name", "price", "promotion")
			.containsExactly(productName, price, Promotion.getNoneInstance());
	}

	@DisplayName("상품이 프로모션을 가지고 있는지 확인할 수 있다.")
	@CsvSource({
		"Frod Promotion, true",
		"Special Promotion, false"
	})
	@ParameterizedTest(name = "{0} 프로모션을 가지고 있는 것은 {1}이다")
	void hasPromotion(String promotionName, boolean expect) {

		// given
		Promotions promotions = createPromotions();
		String productName = "아메리카노";
		int price = 1800;
		Promotion promotion = promotions.findBy(promotionName);

		Product product = Product.create(productName, price, promotion);

		// when
		boolean result = product.hasPromotion();

		// then
		assertThat(result).isEqualTo(expect);
	}

	@DisplayName("상품이 가진 프로모션 활성화되어 있는지 확인할 수 있다.")
	@MethodSource("provideDateTimeForCheckingActivePromotion")
	@ParameterizedTest(name = "{0} 시각에 프로모션 활성화는 {1}이다")
	void hasActivePromotion(LocalDateTime testDateTime, boolean expect) {

		// given
		Promotions promotions = createPromotions();
		String productName = "아메리카노";
		int price = 1800;
		Promotion promotion = promotions.findBy("Roy Promotion");

		Product product = Product.create(productName, price, promotion);

		// when
		boolean result = product.hasActivePromotion(testDateTime);

		// then
		assertThat(result).isEqualTo(expect);
	}

	@DisplayName("프로 모션 증정 수량을 계산할 수 있다")
	@Test
	void calculateFreeQuantity() {
		// given
		Promotion promotion01 = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("티즐", 1500, promotion01);

		// when
		int freeQuantity = product.calculateFreeQuantity(6);

		// then
		assertThat(freeQuantity).isEqualTo(3);
	}

	@DisplayName("프로 모션이 적용되지 않는 수량을 계산할 수 있다")
	@Test
	void calculateRestQuantity() {
		// given
		Promotion promotion01 = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("티즐", 1500, promotion01);

		// when
		int restQuantity = product.calculateRestQuantity(5);

		// then
		assertThat(restQuantity).isEqualTo(1);
	}

	@DisplayName("무료로 얻을 수 있는 프로 모션 추가 수량을 계산할 수 있다")
	@Test
	void calculateExtraQuantity() {
		// given
		Promotion promotion01 = Promotion.of("Frod Promotion", 2, 2,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("티즐", 1500, promotion01);

		// when
		int extraQuantity = product.calculateExtraQuantity(6);

		// then
		assertThat(extraQuantity).isEqualTo(2);
	}

	private Promotions createPromotions() {
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

		Map<String, Promotion> inputPromotions = Map.of(
			promotion01.getName(), promotion01,
			promotion02.getName(), promotion02,
			promotion03.getName(), promotion03
		);

		return Promotions.from(inputPromotions);
	}

}