package store.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PromotionResultTest {

	@DisplayName("프로모션 결과를 생성할 수 있다.")
	@Test
	void of() {

		// given
		int freeQuantity = 1;
		int extraQuantity = 2;
		int unApplicableQuantity = 0;

		// when
		PromotionResult promotionResult = PromotionResult.of(freeQuantity, extraQuantity, unApplicableQuantity);

		// then
		assertThat(promotionResult)
			.extracting("freeQuantity", "extraQuantity", "unApplicableQuantity")
			.containsExactly(1,2,0);
	}

	@DisplayName("추가 무료 프로모션 수량을 가지고 있는지 확인할 수 있다.")
	@CsvSource({
		"2, true",
		"0, false"
	})
	@ParameterizedTest(name = "추가 무료 프로모션 수량이 {0}이면 결과는 {1}이다")
	void hasExtraQuantity(int extraQuantity, boolean expect) {

		// given
		int freeQuantity = 1;
		int unApplicableQuantity = 0;

		PromotionResult promotionResult = PromotionResult.of(freeQuantity, extraQuantity, unApplicableQuantity);

		// when
		boolean result = promotionResult.hasExtraQuantity();

		// then
		assertThat(result).isEqualTo(expect);

	}

	@DisplayName("적용 불가능한 프로모션 수량을 가지고 있는지 확인할 수 있다.")
	@CsvSource({
		"2, true",
		"0, false"
	})
	@ParameterizedTest(name = "적용 불가능한 프로모션 수량이 {0}이면 결과는 {1}이다")
	void hasUnApplicableQuantity(int unApplicableQuantity, boolean expect) {

		// given
		int freeQuantity = 1;
		int extraQuantity = 0;

		PromotionResult promotionResult = PromotionResult.of(freeQuantity, extraQuantity, unApplicableQuantity);

		// when
		boolean result = promotionResult.hasUnApplicableQuantity();

		// then
		assertThat(result).isEqualTo(expect);
	}
}