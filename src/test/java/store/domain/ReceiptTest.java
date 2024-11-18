package store.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ReceiptTest {

	@DisplayName("구매 결과를 생성할 수 있다.")
	@Test
	void of() {
		// given
		Promotion promotion = Promotion.of("Frod Promotion", 2, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("티즐", 2000, promotion);

		// when
		Receipt receipt = Receipt.of(product, 5, 1);

		// then
		assertThat(receipt)
			.extracting("product", "totalQuantity", "freeQuantity")
			.containsExactly(product, 5, 1);

	}

	@DisplayName("총 구매 금액를 계산할 수 있다")
	@Test
	void calculatePurchasedPrice() {
		// given
		Promotion promotion = Promotion.of("Frod Promotion", 2, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("티즐", 2000, promotion);
		Receipt receipt = Receipt.of(product, 5, 1);

		// when
		long purchasedPrice = receipt.calculatePurchasedPrice();

		// then
		assertThat(purchasedPrice).isEqualTo(10000);
	}

	@DisplayName("프로모션 할인 금액을 계산할 수 있다.")
	@Test
	void calculatePromotionDiscountPrice() {
		// given
		Promotion promotion = Promotion.of("Frod Promotion", 2, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		Product product = Product.create("티즐", 2000, promotion);
		Receipt receipt = Receipt.of(product, 7, 2);

		// when
		long promotionDiscountPrice = receipt.calculatePromotionDiscountPrice();

		// then
		assertThat(promotionDiscountPrice).isEqualTo(4000);
	}

	@DisplayName("프로모션 혜택을 받지 않은 상품의 구매 금액을 얻을 수 있다.")
	@CsvSource({
		"2, 0",
		"0, 14000"
	})
	@ParameterizedTest(name = "{0}개의 프로모션 혜택을 얻으면 금액은 {1}이다")
	void calculateNetPurchasedPrice(int freeQuantity, int expectedPurchasePrice) {
		// given
		Promotion promotion = Promotion.of("Frod Promotion", 2, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);
		Product product = Product.create("티즐", 2000, promotion);

		Receipt receipt = Receipt.of(product, 7, freeQuantity);

		// when
		long netPurchasedPrice = receipt.calculateNetPurchasedPrice();

		// then
		assertThat(netPurchasedPrice).isEqualTo(expectedPurchasePrice);
	}
}