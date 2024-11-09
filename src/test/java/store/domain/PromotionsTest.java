package store.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PromotionsTest {

	@DisplayName("프로모션 목록을 생성할 수 있다.")
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

		Map<String, Promotion> inputPromotions = Map.of(
			promotion01.getName(), promotion01,
			promotion02.getName(), promotion02,
			promotion03.getName(), promotion03
		);

		// when
		Promotions result = Promotions.from(inputPromotions);

		// then
		assertThat(result)
			.extracting("promotions")
			.isEqualTo(inputPromotions)
			.extracting("Frod Promotion", "Roy Promotion", "Hana Promotion")
			.hasSize(3)
			.containsExactly(promotion01, promotion02, promotion03);

	}

	@Test
	void findBy() {

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

		Map<String, Promotion> inputPromotions = Map.of(
			promotion01.getName(), promotion01,
			promotion02.getName(), promotion02,
			promotion03.getName(), promotion03
		);

		Promotions promotions = Promotions.from(inputPromotions);

		// when
		Promotion resultPromotion = promotions.findBy("Frod Promotion");

		// then
		assertThat(resultPromotion).isEqualTo(promotion01);
	}
}